package yunkang.ako.ui.pedido

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import yunkang.ako.R
import yunkang.ako.databinding.FragmentCartaBinding

// 5a · La carta. Por ahora la barra con «Salir» y «Mesa N».
// Las categorías, la lista y la pastilla llegan en las piezas 11, 12 y 17
class CartaFragment : Fragment(R.layout.fragment_carta) {

    // La libreta de la Activity, no una propia: lo que apunte 5b lo tiene que ver 5c
    private val viewModel: PedidoViewModel by activityViewModels { PedidoViewModel.Factory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCartaBinding.bind(view)

        // P89: la barra solo enseña la mesa, siempre a la vista (equivocarse de mesa es el error más caro)
        binding.textoMesa.text = getString(R.string.comun_mesa, viewModel.mesaNumero)

        // «Salir» hace lo mismo que el Atrás del sistema: el guardián de PedidoActivity pide el PIN
        binding.botonSalir.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}