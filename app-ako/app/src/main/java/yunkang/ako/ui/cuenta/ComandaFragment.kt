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
// el TOTAL y «Dar la cuenta». Sin arguments: lee la comanda abierta de la libreta de Cuenta
class ComandaFragment : Fragment(R.layout.fragment_comanda) {

    // La misma que la rejilla y el recibo
    private val viewModel: CuentaViewModel by activityViewModels { CuentaViewModel.Factory }

    // En el nivel 1 no hay − ni + en 6b (llegan con el incremento 9): sus acciones llegan vacías (null)
    // y el adaptador esconde esos botones. Solo sale Quitar
    private val adaptador = LineaAdapter(alMenos = null, alMas = null, alQuitar = { linea -> pulsarQuitar(linea) })

    // [Claude] El marco de Cuenta: su portero y la vuelta a la rejilla
    private val cuenta: CuentaActivity
        get() = requireActivity() as CuentaActivity

    // Solo mientras hay vista: onDestroyView lo necesita para soltar el adaptador
    private var binding: FragmentComandaBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentComandaBinding.bind(view)
        this.binding = binding

        binding.textoTitulo.text = getString(R.string.comun_mesa, viewModel.mesaNumero)

        // ← Atrás: quita 6b de la pila y vuelve a la rejilla
        binding.botonAtras.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.listaLineas.layoutManager = LinearLayoutManager(requireContext())
        binding.listaLineas.adapter = adaptador

        // Las líneas y el TOTAL, tal como los deja la libreta (el total lo calcula la base de datos)
        viewModel.lineas.observe(viewLifecycleOwner) { lineas ->
            adaptador.mostrar(lineas)
        }
        viewModel.total.observe(viewLifecycleOwner) { total ->
            binding.textoTotal.text = getString(R.string.comun_precio, Formato.precio(total))
        }

        // Dar la cuenta: el recibo (6c) se pone encima; lo abre el marco de Cuenta, que le da los datos
        binding.botonDarCuenta.setOnClickListener {
            if (cuenta.portero.permite()) cuenta.abrirRecibo()
        }

        // Anular pregunta antes (no tiene deshacer, ficha 6). Botones: «Cancelar» / «Anular»
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
        // El botón se apaga mientras trabaja
        parentFragmentManager.setFragmentResultListener(CLAVE_ANULAR, viewLifecycleOwner) { _, sobre ->
            if (!sobre.getBoolean(ConfirmacionDialog.RESPUESTA_AFIRMATIVA)) return@setFragmentResultListener
            binding.botonAnular.isEnabled = false
            viewLifecycleOwner.lifecycleScope.launch {
                if (viewModel.anular()) cuenta.volverARejilla()
                binding.botonAnular.isEnabled = true
            }
        }

        // Sobre del aviso de la última línea: con «Quitar y anular», se quita la línea que esperaba en la libreta;
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

    // Al quitar la vista, la lista suelta su adaptador y se olvida el binding (como en ReciboFragment)
    override fun onDestroyView() {
        binding?.listaLineas?.adapter = null
        super.onDestroyView()
        binding = null
    }

    // Quitar: sin aviso, salvo si es la última línea (sin líneas = anulada): entonces se pregunta ANTES de quitar
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
                // Si aun así la comanda quedó ANULADA (la lista iba desfasada), a la rejilla, como arriba
                if (viewModel.quitarLinea(linea.id)) cuenta.volverARejilla()
            }
        }
    }

    companion object {
        // [Claude] Los nombres de las dos cajas de 6b y de sus sobres
        private const val CLAVE_ANULAR = "anular"
        private const val CLAVE_ULTIMA_LINEA = "ultima_linea"
    }
}