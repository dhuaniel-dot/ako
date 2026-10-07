package yunkang.ako.ui.cuenta

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import yunkang.ako.R
import yunkang.ako.databinding.FragmentComandaBinding
import yunkang.ako.ui.comun.Formato
import yunkang.ako.ui.comun.LineaAdapter

// 6b · La comanda de una mesa roja (wireframe 06b): barra «← Atrás · Mesa N · Anular», las líneas con Quitar,
// el TOTAL y «Dar la cuenta». Sin arguments (P190 A): lee la comanda abierta de la libreta de Cuenta
class ComandaFragment : Fragment(R.layout.fragment_comanda) {

    // La libreta de la Activity: la misma que la rejilla y el recibo
    private val viewModel: CuentaViewModel by activityViewModels { CuentaViewModel.Factory }

    // P185 C: en el nivel 1 no hay − ni + en 6b (incremento 9): sus acciones llegan vacías (null)
    // y el adaptador esconde esos botones. Quitar llega en la pieza siguiente
    private val adaptador = LineaAdapter(alMenos = null, alMas = null, alQuitar = null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentComandaBinding.bind(view)

        // «Mesa 4» en la barra
        binding.textoTitulo.text = getString(R.string.comun_mesa, viewModel.mesaNumero)

        // ← Atrás: como el del sistema; quita 6b de la pila y vuelve a la rejilla
        binding.botonAtras.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.listaLineas.layoutManager = LinearLayoutManager(requireContext())
        binding.listaLineas.adapter = adaptador

        // Las líneas y el TOTAL, tal como los deja la libreta (R10: el total lo calcula la base de datos)
        viewModel.lineas.observe(viewLifecycleOwner) { lineas ->
            adaptador.mostrar(lineas)
        }
        viewModel.total.observe(viewLifecycleOwner) { total ->
            binding.textoTotal.text = getString(R.string.comun_precio, Formato.precio(total))
        }
    }
}