package yunkang.ako.ui.panel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import yunkang.ako.R
import yunkang.ako.databinding.SheetCategoriaBinding
import yunkang.ako.datos.entidades.Categoria
import yunkang.ako.dominio.modelos.ResultadoGuardado
import yunkang.ako.ui.comun.ConfirmacionDialog
import yunkang.ako.ui.comun.MesasAfectadasDialog
import yunkang.ako.ui.comun.pintarFoto
import java.io.IOException

// 2b · crear o editar una categoría en una hoja inferior (formulario corto → hoja)
// recibe el id por arguments (0 = nueva), como ConfirmacionDialog
class CategoriaBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: PanelViewModel by activityViewModels { PanelViewModel.Factory }

    // [Claude] la vista solo existe mientras la hoja está abierta: se guarda aquí y se suelta al cerrarla
    private var binding: SheetCategoriaBinding? = null

    // la categoría que se edita. null = es nueva, o la lista del Panel todavía no ha llegado
    private var categoria: Categoria? = null

    // la foto elegida y aún sin guardar (su dirección, «content://…»). vive en la hoja
    // y se olvida al cerrarla; onSaveInstanceState la guarda si Android rehace la hoja o mata la app
    private var fotoElegida: String? = null

    // el selector de fotos del sistema, registrado al crear la hoja y no dentro del clic
    private val selectorFotos = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // null = se canceló: no cambia nada
        if (uri != null) {
            viewModel.conservarPrestamo(uri)
            fotoElegida = uri.toString()
            pintarFoto()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = SheetCategoriaBinding.inflate(inflater, container, false)
        this.binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = binding ?: return

        // si Android rehízo la hoja, se recupera la foto que se había elegido
        fotoElegida = savedInstanceState?.getString(CLAVE_FOTO)

        val id = requireArguments().getLong(ARG_ID)
        if (id == 0L) {
            // crear: solo nombre y foto; sin interruptor (una categoría nace en la carta)
            binding.textoTitulo.setText(R.string.categoria_titulo_nueva)
        } else {
            binding.textoTitulo.setText(R.string.categoria_titulo_editar)
            // la hoja no pregunta a la base de datos: escucha el tablón del Panel y pinta una sola vez, cuando llega
            viewModel.categoriasConPlatos.observe(viewLifecycleOwner) { cajas ->
                if (categoria != null) return@observe
                val encontrada = cajas.map { it.categoria }.firstOrNull { it.id == id } ?: return@observe
                categoria = encontrada
                // si Android rehízo la hoja (savedInstanceState), se respeta lo que el Propietario ya tecleó
                if (savedInstanceState == null) binding.textoNombre.setText(encontrada.nombre)
                // la categoría por defecto se puede renombrar pero nunca eliminar → sin interruptor
                if (!encontrada.esPorDefecto) {
                    binding.interruptorEnLaCarta.visibility = View.VISIBLE
                    if (savedInstanceState == null) binding.interruptorEnLaCarta.isChecked = encontrada.activo
                }
                pintarFoto()
                actualizarGuardar()
            }
        }

        // el hueco: al abrir, la elegida (si Android rehízo la hoja) o el «?»; al editar se repinta al llegar la categoría
        pintarFoto()

        binding.botonElegirFoto.setOnClickListener {
            selectorFotos.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // guardar solo se enciende con algo escrito (en blanco no hay nombre que guardar)
        actualizarGuardar()
        binding.textoNombre.doAfterTextChanged {
            binding.campoNombre.error = null
            actualizarGuardar()
        }

        // mientras se trabaja, Guardar se apaga: sin doble toque. la categoría es la de ahora
        // (null solo al crear: Guardar no se enciende antes)
        binding.botonGuardar.setOnClickListener {
            val categoria = categoria
            binding.botonGuardar.isEnabled = false
            viewLifecycleOwner.lifecycleScope.launch {
                // si se va a eliminar, primero se mira qué platos suyos están en mesas pendientes
                if (categoria != null && seVaAEliminar(categoria)) {
                    val afectados = viewModel.platosAfectados(categoria.id)
                    if (afectados.isNotEmpty()) {
                        // con platos en mesas: el aviso 2e decide; la hoja espera su sobre (abajo)
                        MesasAfectadasDialog.abrirCategoria(childFragmentManager, requireContext(), afectados)
                        binding.botonGuardar.isEnabled = true
                        return@launch
                    }
                }
                // sin nada que avisar: se guarda directamente
                guardar(categoria)
            }
        }

        // el sobre del aviso 2e: se abre y se escucha en el mismo gestor (el de la hoja, childFragmentManager)
        childFragmentManager.setFragmentResultListener(MesasAfectadasDialog.CLAVE_CATEGORIA, viewLifecycleOwner) { _, sobre ->
            if (sobre.getBoolean(ConfirmacionDialog.RESPUESTA_AFIRMATIVA)) {
                // eliminar: ahora sí se guarda y se elimina
                binding.botonGuardar.isEnabled = false
                viewLifecycleOwner.lifecycleScope.launch { guardar(categoria) }
            }
            // cancelar: no se guarda nada y la hoja sigue como el Propietario la dejó
        }
    }

    // guarda la foto elegida por si Android rehace la hoja (lo demás lo guardan los campos o se vuelve a leer)
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CLAVE_FOTO, fotoElegida)
    }

    // lo que enseña el hueco redondo: la foto elegida; si no, la que ya tenía la categoría; si no, el «?»
    // Glide entiende las dos direcciones como texto: «content://…» y «/data/…/fotos/….jpg»
    private fun pintarFoto() {
        val binding = binding ?: return
        val foto = fotoElegida ?: categoria?.imagen
        pintarFoto(binding.imagenCategoria, foto, binding.textoNombre.text.toString(), redonda = true)
    }

    // [Claude] al editar, guardar no se enciende hasta que llega la categoría: así nunca se guarda «como nueva»
    private fun actualizarGuardar() {
        val binding = binding ?: return
        val hayNombre = !binding.textoNombre.text.isNullOrBlank()
        val esNueva = requireArguments().getLong(ARG_ID) == 0L
        binding.botonGuardar.isEnabled = hayNombre && (esNueva || categoria != null)
    }

    // la de por defecto nunca: su interruptor ni se ve
    private fun seVaAEliminar(categoria: Categoria): Boolean {
        val binding = binding ?: return false
        return !categoria.esPorDefecto && categoria.activo && !binding.interruptorEnLaCarta.isChecked
    }

    private fun seVaARecuperar(categoria: Categoria): Boolean {
        val binding = binding ?: return false
        return !categoria.esPorDefecto && !categoria.activo && binding.interruptorEnLaCarta.isChecked
    }

    // guarda el nombre y la foto y, si el interruptor cambió, elimina o recupera (activo solo cambia aquí)
    // eliminar: activo = false (nada se borra); ninguna línea se toca. recuperar: vuelve con sus platos intactos
    private suspend fun guardar(categoria: Categoria?) {
        val binding = binding ?: return
        val nombre = binding.textoNombre.text.toString().trim()
        // editar: la misma categoría con el nombre nuevo (activo lo conserva el repositorio; la foto
        // de antes va dentro, y si se eligió otra la cambia la libreta). crear: una nueva en la carta
        val aGuardar = categoria?.copy(nombre = nombre)
            ?: Categoria(nombre = nombre, imagen = null, orden = 0, activo = true, esPorDefecto = false)

        // si la foto elegida no se puede leer, la libreta lanza el error y no se guarda nada
        // [Claude] SecurityException: el préstamo de la foto ya no vale
        val resultado = try {
            viewModel.guardarCategoria(aGuardar, fotoElegida?.toUri())
        } catch (e: IOException) {
            fotoNoUsable()
            return
        } catch (e: SecurityException) {
            fotoNoUsable()
            return
        }

        if (resultado == ResultadoGuardado.NombreRepetido) {
            // el aviso no mira mayúsculas («carnes» = «Carnes»); la hoja sigue abierta
            binding.campoNombre.error = getString(R.string.categoria_nombre_repetido)
            binding.botonGuardar.isEnabled = true
            return
        }

        if (categoria != null && seVaAEliminar(categoria)) {
            viewModel.eliminarCategoria(categoria.id)
        } else if (categoria != null && seVaARecuperar(categoria)) {
            viewModel.recuperarCategoria(categoria.id)
        }
        // cerrar sin romper aunque mientras tanto se pulsara Inicio
        dismissAllowingStateLoss()
    }

    // [Claude] la foto elegida no se ha podido usar: se olvida, se avisa y la hoja sigue abierta
    private fun fotoNoUsable() {
        val binding = binding ?: return
        fotoElegida = null
        pintarFoto()
        Snackbar.make(binding.root, R.string.foto_error, Snackbar.LENGTH_LONG).show()
        binding.botonGuardar.isEnabled = true
    }

    // [Claude] la hoja se abre desplegada del todo, para que el teclado no tape Guardar
    override fun onStart() {
        super.onStart()
        val comportamiento = (dialog as BottomSheetDialog).behavior
        comportamiento.state = BottomSheetBehavior.STATE_EXPANDED
        comportamiento.skipCollapsed = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val ARG_ID = "id"

        private const val CLAVE_FOTO = "foto_elegida"

        // sin id (0) = crear; con el id de una categoría = editarla
        fun nueva(categoriaId: Long = 0L): CategoriaBottomSheet {
            val hoja = CategoriaBottomSheet()
            hoja.arguments = bundleOf(ARG_ID to categoriaId)
            return hoja
        }
    }
}