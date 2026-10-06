package yunkang.ako.ui.pedido

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
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

    // La fila de categorías; tocar una salta a su sección
    private val adaptadorFila = CategoriaFilaAdapter { categoriaId -> saltarA(categoriaId) }

    // [Claude] Dónde empieza cada sección en la lista: id de la categoría → posición de su cabecera
    private var posiciones: Map<Long, Int> = emptyMap()

    // [Claude] La categoría resaltada (0 = ninguna todavía: al abrir se resalta la primera)
    private var categoriaActiva: Long = 0L

    // Quien coloca las filas de la lista (y sabe llevar una posición arriba del todo)
    private lateinit var gestorLista: LinearLayoutManager

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
        gestorLista = LinearLayoutManager(requireContext())
        binding.listaPlatos.layoutManager = gestorLista

        // Cada vez que llega la carta (P167 A), se montan la fila y la lista
        viewModel.carta.observe(viewLifecycleOwner) { secciones ->
            // La fila: una categoría por sección (solo las que tienen algún plato visible, R15)
            adaptadorFila.mostrar(secciones.map { it.categoria })

            // La lista: por cada sección, su cabecera y sus platos, pegados en orden (P69 A, ConcatAdapter)
            val trozos = mutableListOf<RecyclerView.Adapter<out RecyclerView.ViewHolder>>()
            val inicios = mutableMapOf<Long, Int>()
            var posicion = 0
            for (seccion in secciones) {
                inicios[seccion.categoria.id] = posicion
                trozos.add(CabeceraAdapter(seccion.categoria.nombre))
                // La misma fila de plato que el Panel (spec 7); tocar un plato abre su ficha (5b)
                val platos = FilaPlatoAdapter { plato -> abrirFicha(plato.id) }
                platos.mostrar(seccion.platos, categoriaActiva = true)
                trozos.add(platos)
                posicion += 1 + seccion.platos.size   // cada sección ocupa su cabecera y sus platos
            }
            posiciones = inicios
            binding.listaPlatos.adapter = ConcatAdapter(trozos)

            // Al abrir, la primera categoría resaltada (es la que se ve arriba)
            if (categoriaActiva == 0L && secciones.isNotEmpty()) {
                categoriaActiva = secciones.first().categoria.id
            }
            adaptadorFila.resaltar(categoriaActiva)
        }
    }

    // 5b encima de la carta. addToBackStack: Atrás quita la ficha y vuelve la carta (y el guardián de Pedir,
    // que solo vigila con la pila vacía, no pide el PIN)
    private fun abrirFicha(productoId: Long) {
        parentFragmentManager.commit {
            replace(R.id.contenedor, FichaPlatoFragment.nueva(productoId))
            addToBackStack(null)
        }
    }

    // RF-29: tocar una categoría SALTA a su sección (no filtra): su cabecera se pone arriba del todo.
    // scrollToPosition solo la haría visible (podría quedar abajo); con «WithOffset» y 0 queda arriba
    private fun saltarA(categoriaId: Long) {
        val posicion = posiciones[categoriaId] ?: return
        gestorLista.scrollToPositionWithOffset(posicion, 0)
        categoriaActiva = categoriaId
        adaptadorFila.resaltar(categoriaId)
    }
}