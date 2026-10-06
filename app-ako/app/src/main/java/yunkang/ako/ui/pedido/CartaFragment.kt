package yunkang.ako.ui.pedido

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import yunkang.ako.R
import yunkang.ako.databinding.FragmentCartaBinding
import yunkang.ako.ui.comun.FilaPlatoAdapter

// 5a · La carta: la barra, la fila de categorías fija arriba y una sola lista de platos por secciones.
// La pastilla del carrito llega en la pieza 17
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

        // La lista de platos, vertical
        binding.listaPlatos.layoutManager = LinearLayoutManager(requireContext())

        // Cada vez que llega la carta (P167 A), se montan la fila y la lista
        viewModel.carta.observe(viewLifecycleOwner) { secciones ->
            // La fila: una categoría por sección (solo las que tienen algún plato visible, R15)
            adaptadorFila.mostrar(secciones.map { it.categoria })

            // La lista: por cada sección, su cabecera y sus platos, pegados en orden (P69 A, ConcatAdapter)
            val trozos = mutableListOf<RecyclerView.Adapter<out RecyclerView.ViewHolder>>()
            for (seccion in secciones) {
                trozos.add(CabeceraAdapter(seccion.categoria.nombre))
                // La misma fila de plato que el Panel (spec 7). [Claude] Tocar un plato abrirá su ficha (pieza 14)
                val platos = FilaPlatoAdapter { }
                platos.mostrar(seccion.platos, categoriaActiva = true)
                trozos.add(platos)
            }
            binding.listaPlatos.adapter = ConcatAdapter(trozos)
        }
    }
}