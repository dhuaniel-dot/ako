package yunkang.ako.ui.pedido

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import yunkang.ako.R
import yunkang.ako.databinding.FragmentCarritoBinding
import yunkang.ako.ui.comun.ConfirmacionDialog
import yunkang.ako.ui.comun.Formato
import yunkang.ako.ui.comun.LineaAdapter

// 5c · El carrito, a pantalla completa encima de la carta (P84): la mesa en la barra, las líneas con − + y Quitar,
// el TOTAL y «Enviar», que pide confirmación con la mesa y el total
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

        // Enviar pide confirmación con la mesa y el total (ficha 5: un envío por error solo se deshace en Cuenta).
        // Los botones dicen lo que hacen: «Cancelar» / «Enviar» (P40). Si ya hay una caja abierta, no se abre otra
        binding.botonEnviar.setOnClickListener {
            if (parentFragmentManager.findFragmentByTag(CLAVE_ENVIAR) != null) return@setOnClickListener
            val carrito = viewModel.carrito.value ?: return@setOnClickListener
            val total = getString(R.string.comun_precio, Formato.precio(carrito.total()))
            ConfirmacionDialog.nueva(
                titulo = getString(R.string.enviar_titulo),
                texto = getString(R.string.enviar_cuerpo, viewModel.mesaNumero, total),
                afirmativo = getString(R.string.carrito_btn_enviar),
                negativo = getString(R.string.comun_cancelar),
                clave = CLAVE_ENVIAR
            ).show(parentFragmentManager, CLAVE_ENVIAR)
        }

        // El sobre de la caja se escucha en el MISMO FragmentManager con el que se abrió
        parentFragmentManager.setFragmentResultListener(CLAVE_ENVIAR, viewLifecycleOwner) { _, sobre ->
            if (!sobre.getBoolean(ConfirmacionDialog.RESPUESTA_AFIRMATIVA)) return@setFragmentResultListener
            viewLifecycleOwner.lifecycleScope.launch {
                if (viewModel.enviar()) {
                    // P72 A: aviso y vuelta a la carta con el carrito vacío; se puede seguir pidiendo (R2).
                    // El aviso va sobre la vista de la Activity porque el carrito se cierra ahora mismo
                    Snackbar.make(
                        requireActivity().findViewById(R.id.contenedor),
                        getString(R.string.enviar_hecho, viewModel.mesaNumero),
                        Snackbar.LENGTH_LONG
                    ).show()
                    // P146: la vuelta llega tras esperar a la base de datos; si la app pasó a segundo plano
                    // mientras tanto, no se cambia de pantalla (la comanda ya está guardada)
                    if (!parentFragmentManager.isStateSaved) parentFragmentManager.popBackStack()
                }
            }
        }
    }

    companion object {
        // [Claude] El nombre de la caja de Enviar y de su sobre
        private const val CLAVE_ENVIAR = "enviar"
    }
}