package yunkang.ako.ui.comun

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import yunkang.ako.R
import yunkang.ako.databinding.FragmentReciboBinding

// 6c · el recibo (wireframe 06c). en comun/ porque lo comparten dos pantallas: Cuenta (6c, para cobrar)
// y el Resumen de ingresos (2g, en solo lectura)
// como la rejilla, pinta lo que le da quien lo aloja (mostrar) y no sabe en qué pantalla está
// por arguments solo lleva lo que necesita para dibujarse: el número de mesa y si es de solo lectura
class ReciboFragment : Fragment(R.layout.fragment_recibo) {

    // en el recibo las líneas no llevan ningún botón
    private val adaptador = LineaAdapter(alMenos = null, alMas = null, alQuitar = null)

    // lo último que le dieron. se guarda aquí porque puede llegar antes de que exista la vista
    private var total = 0

    // su propio portero: el recibo no sabe en qué pantalla está, así que no usa el de la Activity
    private val portero = GuardaDobleToque()

    // [Claude] solo existe mientras hay vista (entre onViewCreated y onDestroyView):
    // mostrar() puede llegar en cualquier momento y tiene que saber si hay algo que pintar
    private var binding: FragmentReciboBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentReciboBinding.bind(view)
        this.binding = binding
        val argumentos = requireArguments()

        binding.textoTitulo.text = getString(R.string.recibo_titulo, argumentos.getInt(ARG_MESA_NUMERO))

        binding.botonAtras.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.listaLineas.layoutManager = LinearLayoutManager(requireContext())
        binding.listaLineas.adapter = adaptador

        // solo lectura (2g): ni calculadora de cambio ni Cobrar
        val soloLectura = argumentos.getBoolean(ARG_SOLO_LECTURA)
        binding.bloqueCambio.isVisible = !soloLectura
        binding.botonCobrar.isVisible = !soloLectura

        // cada vez que cambia lo entregado, sobre para quien lo aloja con los céntimos;
        // sin céntimos dentro si el campo está vacío o no es un importe («,», «1,2,3»): Cambio en blanco
        binding.textoEntregado.doAfterTextChanged { texto ->
            val entregado = Formato.centimosDesde(texto.toString())
            val sobre = if (entregado == null) bundleOf() else bundleOf(ENTREGADO_CENTIMOS to entregado)
            setFragmentResult(CLAVE_ENTREGADO, sobre)
        }

        // cobrar pregunta antes, con la mesa y el total. botones: «Cancelar» / «Cobrar»
        // el sobre de la caja lo recibe quien aloja el recibo (clave CLAVE_COBRAR), que es quien cobra
        // cobrar no depende de lo entregado: vacío o negativo, sigue activo (ficha 6)
        binding.botonCobrar.setOnClickListener {
            if (!portero.permite()) return@setOnClickListener
            if (parentFragmentManager.findFragmentByTag(CLAVE_COBRAR) != null) return@setOnClickListener
            val totalTexto = getString(R.string.comun_precio, Formato.precio(total))
            ConfirmacionDialog.nueva(
                titulo = getString(R.string.cobrar_titulo, argumentos.getInt(ARG_MESA_NUMERO)),
                texto = getString(R.string.cobrar_cuerpo, totalTexto),
                afirmativo = getString(R.string.recibo_btn_cobrar),
                negativo = getString(R.string.comun_cancelar),
                clave = CLAVE_COBRAR
            ).show(parentFragmentManager, CLAVE_COBRAR)
        }

        pintarTotal()
    }

    // al quitar la vista (por ejemplo, cuando otra se pone encima en la pila),
    // la lista suelta su adaptador; si no, el adaptador, que vive con el Fragment, seguiría sujetando la vista vieja
    override fun onDestroyView() {
        binding?.listaLineas?.adapter = null
        super.onDestroyView()
        binding = null
    }

    // quien lo aloja le da las líneas (ya convertidas) y el total (calculado por la base de datos)
    // el adaptador las guarda aunque la vista aún no exista
    fun mostrar(lineas: List<LineaVista>, total: Int) {
        this.total = total
        adaptador.mostrar(lineas)
        pintarTotal()
    }

    // el total, si la vista ya existe; si no, se pinta al crearla (onViewCreated)
    private fun pintarTotal() {
        val binding = binding ?: return
        binding.textoTotal.text = getString(R.string.comun_precio, Formato.precio(total))
    }

    // quien lo aloja le da el cambio ya calculado; vacío (null) = Cambio en blanco
    fun mostrarCambio(cambio: Int?) {
        val binding = binding ?: return
        binding.textoCambio.text =
            if (cambio == null) "" else getString(R.string.comun_precio, Formato.precio(cambio))
    }

    companion object {
        const val CLAVE_ENTREGADO = "entregado"
        const val ENTREGADO_CENTIMOS = "entregado_centimos"
        const val CLAVE_COBRAR = "cobrar"

        private const val ARG_MESA_NUMERO = "mesa_numero"
        private const val ARG_SOLO_LECTURA = "solo_lectura"

        fun nuevo(mesaNumero: Int, soloLectura: Boolean): ReciboFragment {
            val recibo = ReciboFragment()
            recibo.arguments = bundleOf(ARG_MESA_NUMERO to mesaNumero, ARG_SOLO_LECTURA to soloLectura)
            return recibo
        }
    }
}