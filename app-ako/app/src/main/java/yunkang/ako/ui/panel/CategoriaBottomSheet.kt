package yunkang.ako.ui.panel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import yunkang.ako.R
import yunkang.ako.databinding.SheetCategoriaBinding
import yunkang.ako.datos.entidades.Categoria
import yunkang.ako.dominio.modelos.ResultadoGuardado
import yunkang.ako.ui.comun.ConfirmacionDialog
import yunkang.ako.ui.comun.MesasAfectadasDialog

// 2b · Crear o editar una categoría en una hoja inferior (P84: formulario corto → hoja).
// Recibe el id por arguments (0 = nueva), como ConfirmacionDialog: si Android rehace la hoja, no se pierde
class CategoriaBottomSheet : BottomSheetDialogFragment() {

    // La misma libreta que el Panel (como CambiarPinDialog)
    private val viewModel: PanelViewModel by activityViewModels { PanelViewModel.Factory }

    // [Claude] La vista solo existe mientras la hoja está abierta: se guarda aquí y se suelta al cerrarla
    private var _binding: SheetCategoriaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = SheetCategoriaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // [Claude] La hoja no pregunta a la base de datos: busca la categoría en la lista que el Panel ya tiene
        val id = requireArguments().getLong(ARG_ID)
        val categoria = viewModel.categoriasConPlatos.value
            ?.map { it.categoria }
            ?.firstOrNull { it.id == id }

        if (categoria == null) {
            // Crear: solo nombre y foto; sin interruptor (una categoría nace en la carta, P-M-05)
            binding.textoTitulo.setText(R.string.categoria_titulo_nueva)
        } else {
            binding.textoTitulo.setText(R.string.categoria_titulo_editar)
            // Si Android rehízo la hoja (savedInstanceState), se respeta lo que el Propietario ya tecleó
            if (savedInstanceState == null) binding.textoNombre.setText(categoria.nombre)
            // R16: la categoría por defecto se puede renombrar pero nunca eliminar → sin interruptor
            if (!categoria.esPorDefecto) {
                binding.interruptorEnLaCarta.visibility = View.VISIBLE
                if (savedInstanceState == null) binding.interruptorEnLaCarta.isChecked = categoria.activo
            }
        }

        // Guardar solo se enciende con algo escrito (en blanco no hay nombre que guardar)
        binding.botonGuardar.isEnabled = !binding.textoNombre.text.isNullOrBlank()
        binding.textoNombre.doAfterTextChanged {
            binding.campoNombre.error = null
            binding.botonGuardar.isEnabled = !binding.textoNombre.text.isNullOrBlank()
        }

        binding.botonGuardar.setOnClickListener {
            binding.botonGuardar.isEnabled = false   // sin doble toque mientras se trabaja (P142)
            viewLifecycleOwner.lifecycleScope.launch {
                // R6 (P128): si se va a eliminar, PRIMERO se mira qué platos suyos están en mesas pendientes
                if (categoria != null && seVaAEliminar(categoria)) {
                    val afectados = viewModel.platosAfectados(categoria.id)
                    if (afectados.isNotEmpty()) {
                        // Con platos en mesas: el aviso 2e decide; la hoja espera su sobre (abajo)
                        MesasAfectadasDialog.abrirCategoria(childFragmentManager, requireContext(), afectados)
                        binding.botonGuardar.isEnabled = true
                        return@launch
                    }
                }
                // Sin nada que avisar (P8: así se elimina Bebidas): se guarda directamente
                guardar(categoria)
            }
        }

        // El sobre del aviso 2e: se abre y se escucha en el MISMO gestor (el de la hoja, childFragmentManager)
        childFragmentManager.setFragmentResultListener(MesasAfectadasDialog.CLAVE_CATEGORIA, viewLifecycleOwner) { _, sobre ->
            if (sobre.getBoolean(ConfirmacionDialog.RESPUESTA_AFIRMATIVA)) {
                // Eliminar: ahora sí se guarda y se elimina
                binding.botonGuardar.isEnabled = false
                viewLifecycleOwner.lifecycleScope.launch { guardar(categoria) }
            }
            // Cancelar (P57 A): no se guarda nada y la hoja sigue como el Propietario la dejó
        }
    }

    // [Claude] ¿El Propietario ha apagado «En la carta» de una categoría que estaba en la carta?
    // (La de por defecto nunca: su interruptor ni se ve, R16)
    private fun seVaAEliminar(categoria: Categoria): Boolean =
        !categoria.esPorDefecto && categoria.activo && !binding.interruptorEnLaCarta.isChecked

    // [Claude] ¿Lo ha encendido en una categoría eliminada?
    private fun seVaARecuperar(categoria: Categoria): Boolean =
        !categoria.esPorDefecto && !categoria.activo && binding.interruptorEnLaCarta.isChecked

    // Guarda el nombre y, si el interruptor cambió, elimina o recupera (hueco 10: activo solo cambia aquí, P150)
    private suspend fun guardar(categoria: Categoria?) {
        val nombre = binding.textoNombre.text.toString().trim()
        // Editar: la misma categoría con el nombre nuevo (activo lo conserva el repositorio, P150).
        // Crear: una nueva en la carta; el orden (mayor + 1) lo pone el repositorio
        val aGuardar = categoria?.copy(nombre = nombre)
            ?: Categoria(nombre = nombre, imagen = null, orden = 0, activo = true, esPorDefecto = false)

        if (viewModel.guardarCategoria(aGuardar) == ResultadoGuardado.NombreRepetido) {
            // P124: el aviso no mira mayúsculas («carnes» = «Carnes»); la hoja sigue abierta
            binding.campoNombre.error = getString(R.string.categoria_nombre_repetido)
            binding.botonGuardar.isEnabled = true
            return
        }

        if (categoria != null && seVaAEliminar(categoria)) {
            viewModel.eliminarCategoria(categoria.id)     // R5: activo = false; ninguna línea se toca (R6)
        } else if (categoria != null && seVaARecuperar(categoria)) {
            viewModel.recuperarCategoria(categoria.id)    // vuelve con sus platos intactos
        }
        // P146: cerrar sin romper aunque mientras tanto se pulsara Inicio
        dismissAllowingStateLoss()
    }

    // [Claude] La hoja se abre desplegada del todo, para que el teclado no tape Guardar
    override fun onStart() {
        super.onStart()
        val comportamiento = (dialog as BottomSheetDialog).behavior
        comportamiento.state = BottomSheetBehavior.STATE_EXPANDED
        comportamiento.skipCollapsed = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_ID = "id"

        // Sin id (0) = crear; con el id de una categoría = editarla
        fun nueva(categoriaId: Long = 0L): CategoriaBottomSheet {
            val hoja = CategoriaBottomSheet()
            hoja.arguments = bundleOf(ARG_ID to categoriaId)
            return hoja
        }
    }
}