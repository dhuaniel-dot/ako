package yunkang.ako.ui.panel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import yunkang.ako.R
import yunkang.ako.databinding.SheetCategoriaBinding
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import yunkang.ako.datos.entidades.Categoria
import yunkang.ako.dominio.modelos.ResultadoGuardado

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
            val nombre = binding.textoNombre.text.toString().trim()
            // Editar: la misma categoría con el nombre nuevo (activo lo conserva el repositorio, P150).
            // Crear: una nueva en la carta; el orden (mayor + 1) lo pone el repositorio
            val aGuardar = categoria?.copy(nombre = nombre)
                ?: Categoria(nombre = nombre, imagen = null, orden = 0, activo = true, esPorDefecto = false)

            binding.botonGuardar.isEnabled = false   // sin doble toque mientras se guarda (P142)
            viewLifecycleOwner.lifecycleScope.launch {
                val resultado = viewModel.guardarCategoria(aGuardar)
                if (resultado == ResultadoGuardado.NombreRepetido) {
                    // P124: el aviso no mira mayúsculas («carnes» = «Carnes»); la hoja sigue abierta
                    binding.campoNombre.error = getString(R.string.categoria_nombre_repetido)
                    binding.botonGuardar.isEnabled = true
                } else {
                    // P146: cerrar sin romper aunque mientras tanto se pulsara Inicio
                    dismissAllowingStateLoss()
                }
            }
        }
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