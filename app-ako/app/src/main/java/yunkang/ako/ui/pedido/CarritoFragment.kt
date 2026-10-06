package yunkang.ako.ui.pedido

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import yunkang.ako.R
import yunkang.ako.databinding.FragmentCarritoBinding
import yunkang.ako.ui.comun.Formato
import yunkang.ako.ui.comun.LineaAdapter

// 5c · El carrito, a pantalla completa encima de la carta (P84): la mesa en la barra, las líneas con − + y Quitar,
// el TOTAL y «Enviar» (que envía en la pieza 20)
class CarritoFragment : Fragment(R.layout.fragment_carrito) {

    // La libreta de la Activity: el mismo carrito que la carta y la ficha
    private val viewModel: PedidoViewModel by activityViewModels { PedidoViewModel.Factory }

    // Las líneas; cada botón le pide el cambio a la libreta (la pantalla nunca toca el carrito)
    private val adaptador = LineaAdapter(
        alMenos = { linea -> viewModel.cambiarCantidad(linea.id, linea.cantidad - 1) },
        alMas = { linea -> viewModel.cambiarCantidad(linea.id, linea.cantidad + 1) },
        alQuitar = { linea -> viewModel.quitar(linea.id) }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCarritoBinding.bind(view)

        // La mesa en la barra: equivocarse de mesa es el error más caro (ficha 5)
        binding.textoTitulo.text = getString(R.string.carrito_titulo, viewModel.mesaNumero)

        // ← Atrás: como el del sistema; con el carrito encima, el guardián está apagado y se vuelve a la carta
        binding.botonAtras.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.listaLineas.layoutManager = LinearLayoutManager(requireContext())
        binding.listaLineas.adapter = adaptador

        // Cada vez que cambia el carrito: las líneas, el TOTAL (R10, calculado) y Enviar apagado si está vacío (R4)
        viewModel.lineasVista.observe(viewLifecycleOwner) { lineas ->
            adaptador.mostrar(lineas)
        }
        viewModel.carrito.observe(viewLifecycleOwner) { carrito ->
            binding.textoTotal.text = getString(R.string.comun_precio, Formato.precio(carrito.total()))
            binding.botonEnviar.isEnabled = !carrito.estaVacio()
        }
    }
}