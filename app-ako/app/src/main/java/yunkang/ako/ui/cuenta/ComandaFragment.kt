package yunkang.ako.ui.cuenta

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import yunkang.ako.R
import yunkang.ako.databinding.FragmentComandaBinding
import yunkang.ako.ui.comun.ConfirmacionDialog
import yunkang.ako.ui.comun.Formato
import yunkang.ako.ui.comun.LineaAdapter
import yunkang.ako.ui.comun.LineaVista

// 6b · La comanda de una mesa roja (wireframe 06b): barra «← Atrás · Mesa N · Anular», las líneas con Quitar,
// el TOTAL y «Dar la cuenta». Sin arguments (P190 A): lee la comanda abierta de la libreta de Cuenta
class ComandaFragment : Fragment(R.layout.fragment_comanda) {

    // La libreta de la Activity: la misma que la rejilla y el recibo
    private val viewModel: CuentaViewModel by activityViewModels { CuentaViewModel.Factory }

    // P185 C: en el nivel 1 no hay − ni + en 6b (incremento 9): sus acciones llegan vacías (null)
    // y el adaptador esconde esos botones. Solo sale Quitar
    private val adaptador = LineaAdapter(alMenos = null, alMas = null, alQuitar = { linea -> pulsarQuitar(linea) })

    // [Claude] El marco de Cuenta: su portero (P171) y la vuelta a la rejilla
    private val cuenta: CuentaActivity
        get() = requireActivity() as CuentaActivity

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

        // Dar la cuenta: el recibo (6c) se pone encima; lo abre el marco de Cuenta, que le da los datos (P184 A)
        binding.botonDarCuenta.setOnClickListener {
            if (cuenta.portero.permite()) cuenta.abrirRecibo()
        }

        // Anular pregunta antes (no tiene deshacer, ficha 6). Botones: «Cancelar» / «Anular» (P40)
        binding.botonAnular.setOnClickListener {
            if (!cuenta.portero.permite()) return@setOnClickListener
            if (parentFragmentManager.findFragmentByTag(CLAVE_ANULAR) != null) return@setOnClickListener
            ConfirmacionDialog.nueva(
                titulo = getString(R.string.anular_titulo, viewModel.mesaNumero),
                texto = getString(R.string.anular_cuerpo),
                afirmativo = getString(R.string.comanda_btn_anular),
                negativo = getString(R.string.comun_cancelar),
                clave = CLAVE_ANULAR
            ).show(parentFragmentManager, CLAVE_ANULAR)
        }

        // Sobre del aviso de Anular: con «Anular», la libreta cierra la comanda y se vuelve a la rejilla.
        // El botón se apaga mientras trabaja (P142)
        parentFragmentManager.setFragmentResultListener(CLAVE_ANULAR, viewLifecycleOwner) { _, sobre ->
            if (!sobre.getBoolean(ConfirmacionDialog.RESPUESTA_AFIRMATIVA)) return@setFragmentResultListener
            binding.botonAnular.isEnabled = false
            viewLifecycleOwner.lifecycleScope.launch {
                if (viewModel.anular()) cuenta.volverARejilla()
                binding.botonAnular.isEnabled = true
            }
        }

        // Sobre del aviso R7: con «Quitar y anular», se quita la línea que esperaba en la libreta (P186 A);
        // la comanda queda ANULADA con cero líneas y se vuelve a la rejilla. Con «Cancelar», se olvida
        parentFragmentManager.setFragmentResultListener(CLAVE_ULTIMA_LINEA, viewLifecycleOwner) { _, sobre ->
            val lineaId = viewModel.lineaPorQuitar
            viewModel.lineaPorQuitar = null
            if (lineaId == null || !sobre.getBoolean(ConfirmacionDialog.RESPUESTA_AFIRMATIVA)) {
                return@setFragmentResultListener
            }
            viewLifecycleOwner.lifecycleScope.launch {
                if (viewModel.quitarLinea(lineaId)) cuenta.volverARejilla()
            }
        }
    }

    // Quitar: sin aviso, salvo si es la última línea (R7): entonces se pregunta ANTES de quitar
    private fun pulsarQuitar(linea: LineaVista) {
        if (!cuenta.portero.permite()) return
        if (viewModel.esUltimaLinea()) {
            if (parentFragmentManager.findFragmentByTag(CLAVE_ULTIMA_LINEA) != null) return
            viewModel.lineaPorQuitar = linea.id
            ConfirmacionDialog.nueva(
                titulo = getString(R.string.ultima_linea_titulo),
                texto = getString(R.string.ultima_linea_cuerpo, viewModel.mesaNumero),
                afirmativo = getString(R.string.ultima_linea_btn_quitar_anular),
                negativo = getString(R.string.comun_cancelar),
                clave = CLAVE_ULTIMA_LINEA
            ).show(parentFragmentManager, CLAVE_ULTIMA_LINEA)
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.quitarLinea(linea.id)
            }
        }
    }

    companion object {
        // [Claude] Los nombres de las dos cajas de 6b y de sus sobres
        private const val CLAVE_ANULAR = "anular"
        private const val CLAVE_ULTIMA_LINEA = "ultima_linea"
    }
}