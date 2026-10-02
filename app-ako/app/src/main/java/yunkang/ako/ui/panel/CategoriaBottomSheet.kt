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