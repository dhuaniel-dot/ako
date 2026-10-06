package yunkang.ako.ui.pedido

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import yunkang.ako.R
import yunkang.ako.databinding.FragmentCartaBinding

// 5a · La carta: la barra y la fila de categorías fija arriba.
// La lista por secciones llega en la pieza 12 y la pastilla del carrito en la 17
class CartaFragment : Fragment(R.layout.fragment_carta) {

    // La libreta de la Activity, no una propia: lo que apunte 5b lo tiene que ver 5c
    private val viewModel: PedidoViewModel by activityViewModels { PedidoViewModel.Factory }

    // La fila de categorías. [Claude] Tocar una todavía no hace nada: en la pieza 13 saltará a su sección
    private val adaptadorFila = CategoriaFilaAdapter { }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCartaBinding.bind(view)

        // P89: la barra solo enseña la mesa, siempre a la vista (equivocarse de mesa es el error más caro)
        binding.textoMesa.text = getString(R.string.comun_mesa, viewModel.mesaNumero)

        // «Salir» hace lo mismo que el Atrás del sistema: el guardián de PedidoActivity pide el PIN
        binding.botonSalir.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // La fila: la misma lista de siempre, pero tumbada (horizontal)
        binding.filaCategorias.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.filaCategorias.adapter = adaptadorFila

        // Cada vez que llega la carta (P167 A), la fila enseña una categoría por sección
        // (solo las que tienen algún plato visible, R15)
        viewModel.carta.observe(viewLifecycleOwner) { secciones ->
            adaptadorFila.mostrar(secciones.map { it.categoria })
        }
    }
}