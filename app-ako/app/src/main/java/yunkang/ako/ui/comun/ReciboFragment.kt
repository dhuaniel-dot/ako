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

// 6c · El recibo (wireframe 06c). En comun/ porque lo comparten dos pantallas (spec 3): Cuenta (6c, para cobrar)
// y, en la S10, el Resumen de ingresos (2g, en solo lectura).
// P184 A: como la rejilla, pinta lo que le da quien lo aloja (mostrar) y no sabe en qué pantalla está.
// Por arguments solo lleva lo que necesita para dibujarse: el número de mesa y si es de solo lectura
class ReciboFragment : Fragment(R.layout.fragment_recibo) {

    // P185 C: en el recibo las líneas no llevan ningún botón
    private val adaptador = LineaAdapter(alMenos = null, alMas = null, alQuitar = null)

    // Lo último que le dieron. Se guarda aquí porque puede llegar antes de que exista la vista
    private var total = 0

    // H01 B (P171): su propio portero; el recibo no sabe en qué pantalla está, así que no usa el de la Activity
    private val portero = GuardaDobleToque()

    // [Claude] El "mando" de las vistas, solo mientras la vista existe (entre onViewCreated y onDestroyView):
    // mostrar() puede llegar en cualquier momento y tiene que saber si hay algo que pintar
    private var binding: FragmentReciboBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentReciboBinding.bind(view)
        this.binding = binding
        val argumentos = requireArguments()

        // «Mesa 4 · Recibo»
        binding.textoTitulo.text = getString(R.string.recibo_titulo, argumentos.getInt(ARG_MESA_NUMERO))

        // ← Atrás: como el del sistema; vuelve a la vista de abajo (6b en Cuenta)
        binding.botonAtras.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.listaLineas.layoutManager = LinearLayoutManager(requireContext())
        binding.listaLineas.adapter = adaptador

        // Solo lectura (2g, S10): ni calculadora de cambio ni Cobrar
        val soloLectura = argumentos.getBoolean(ARG_SOLO_LECTURA)
        binding.bloqueCambio.isVisible = !soloLectura
        binding.botonCobrar.isVisible = !soloLectura

        // P192 B: cada vez que cambia lo entregado, sobre para quien lo aloja con los céntimos;
        // sin céntimos dentro si el campo está vacío o no es un importe («,», «1,2,3»): Cambio en blanco (P187 A)
        binding.textoEntregado.doAfterTextChanged { texto ->
            val entregado = Formato.centimosDesde(texto.toString())
            val sobre = if (entregado == null) bundleOf() else bundleOf(ENTREGADO_CENTIMOS to entregado)
            setFragmentResult(CLAVE_ENTREGADO, sobre)
        }

        // Cobrar pregunta antes, con la mesa y el total. Botones: «Cancelar» / «Cobrar» (P40).
        // El sobre de la caja lo recibe quien aloja el recibo (clave CLAVE_COBRAR), que es quien cobra.
        // Cobrar no depende de lo entregado: vacío o negativo, sigue activo (ficha 6)
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    // Quien lo aloja le da las líneas (ya convertidas) y el TOTAL (R10: calculado por la base de datos)
    fun mostrar(lineas: List<LineaVista>, total: Int) {
        this.total = total
        adaptador.mostrar(lineas)   // el adaptador las guarda aunque la vista aún no exista
        pintarTotal()
    }

    // El TOTAL, si la vista ya existe; si no, se pinta al crearla (onViewCreated)
    private fun pintarTotal() {
        val binding = binding ?: return
        binding.textoTotal.text = getString(R.string.comun_precio, Formato.precio(total))
    }

    // P192 B: quien lo aloja le da el cambio ya calculado; vacío (null) = Cambio en blanco (P187 A)
    fun mostrarCambio(cambio: Int?) {
        val binding = binding ?: return
        binding.textoCambio.text =
            if (cambio == null) "" else getString(R.string.comun_precio, Formato.precio(cambio))
    }

    companion object {
        // [Claude] Los sobres que salen del recibo hacia quien lo aloja, y lo que lleva dentro el de lo entregado
        const val CLAVE_ENTREGADO = "entregado"
        const val ENTREGADO_CENTIMOS = "entregado_centimos"
        const val CLAVE_COBRAR = "cobrar"

        // [Claude] Los nombres de lo que va en los arguments (solo se usan en este archivo)
        private const val ARG_MESA_NUMERO = "mesa_numero"
        private const val ARG_SOLO_LECTURA = "solo_lectura"

        // La forma de crear un recibo (P184 A): los arguments se conservan si Android rehace el Fragment
        fun nuevo(mesaNumero: Int, soloLectura: Boolean): ReciboFragment {
            val recibo = ReciboFragment()
            recibo.arguments = bundleOf(ARG_MESA_NUMERO to mesaNumero, ARG_SOLO_LECTURA to soloLectura)
            return recibo
        }
    }
}