package yunkang.ako.ui.pedido

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import yunkang.ako.R
import yunkang.ako.databinding.FragmentFichaPlatoBinding

// 5b · Ficha del plato, a pantalla completa encima de la carta (P84).
// Primera versión (pieza 14): el diseño y la navegación; los datos y la cantidad llegan en la pieza 15
class FichaPlatoFragment : Fragment(R.layout.fragment_ficha_plato) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFichaPlatoBinding.bind(view)

        // ← Atrás hace lo mismo que el del sistema: con la ficha encima, el guardián de Pedir está apagado
        // y Android quita la ficha de la pila: se vuelve a la carta, sin PIN
        binding.botonAtras.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    companion object {
        // [Claude] El nombre del id del plato dentro de los arguments
        private const val ARG_PRODUCTO_ID = "producto_id"

        // La forma de crear una ficha: se le da el id del plato (en los arguments, no en el constructor)
        fun nueva(productoId: Long): FichaPlatoFragment {
            val ficha = FichaPlatoFragment()
            ficha.arguments = bundleOf(ARG_PRODUCTO_ID to productoId)
            return ficha
        }
    }
}