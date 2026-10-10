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

// 6b · la comanda de una mesa roja (wireframe 06b): barra «← Atrás · Mesa N · Anular», las líneas con Quitar,
// el total y «Dar la cuenta». sin arguments: lee la comanda abierta de la libreta de Cuenta
class ComandaFragment : Fragment(R.layout.fragment_comanda) {

    // la misma que la rejilla y el recibo
    private val viewModel: CuentaViewModel by activityViewModels { CuentaViewModel.Factory }

    // en el nivel 1 no hay − ni + en 6b (llegan con el incremento 9): sus acciones llegan vacías (null)
    // y el adaptador esconde esos botones. solo sale Quitar
    private val adaptador = LineaAdapter(alMenos = null, alMas = null, alQuitar = { linea -> pulsarQuitar(linea) })

    private val cuenta: CuentaActivity
        get() = requireActivity() as CuentaActivity

    // solo mientras hay vista: onDestroyView lo necesita para soltar el adaptador
    private var binding: FragmentComandaBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentComandaBinding.bind(view)
        this.binding = binding

        binding.textoTitulo.text = getString(R.string.comun_mesa, viewModel.mesaNumero)

        binding.botonAtras.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.listaLineas.layoutManager = LinearLayoutManager(requireContext())
        binding.listaLineas.adapter = adaptador

        // las líneas y el total, tal como los deja la libreta (el total lo calcula la base de datos)
        viewModel.lineas.observe(viewLifecycleOwner) { lineas ->
            adaptador.mostrar(lineas)
        }
        viewModel.total.observe(viewLifecycleOwner) { total ->
            binding.textoTotal.text = getString(R.string.comun_precio, Formato.precio(total))
        }

        // dar la cuenta: el recibo (6c) se pone encima; lo abre el marco de Cuenta, que le da los datos
        binding.botonDarCuenta.setOnClickListener {
            if (cuenta.portero.permite()) cuenta.abrirRecibo()
        }

        // anular pregunta antes (no tiene deshacer, ficha 6). botones: «Cancelar» / «Anular»
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

        // sobre del aviso de Anular: con «Anular», la libreta cierra la comanda y se vuelve a la rejilla
        // el botón se apaga mientras trabaja
        parentFragmentManager.setFragmentResultListener(CLAVE_ANULAR, viewLifecycleOwner) { _, sobre ->
            if (!sobre.getBoolean(ConfirmacionDialog.RESPUESTA_AFIRMATIVA)) return@setFragmentResultListener
            binding.botonAnular.isEnabled = false
            viewLifecycleOwner.lifecycleScope.launch {
                if (viewModel.anular()) cuenta.volverARejilla()
                binding.botonAnular.isEnabled = true
            }
        }

        // sobre del aviso de la última línea: con «Quitar y anular», se quita la línea que esperaba en la libreta;
        // la comanda queda anulada con cero líneas y se vuelve a la rejilla. con «Cancelar», se olvida
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

    // al quitar la vista, la lista suelta su adaptador y se olvida el binding (como en ReciboFragment)
    override fun onDestroyView() {
        binding?.listaLineas?.adapter = null
        super.onDestroyView()
        binding = null
    }

    // quitar: sin aviso, salvo si es la última línea (sin líneas = anulada): entonces se pregunta antes de quitar
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
                // si aun así la comanda quedó anulada (la lista iba desfasada), a la rejilla, como arriba
                if (viewModel.quitarLinea(linea.id)) cuenta.volverARejilla()
            }
        }
    }

    companion object {
        private const val CLAVE_ANULAR = "anular"
        private const val CLAVE_ULTIMA_LINEA = "ultima_linea"
    }
}