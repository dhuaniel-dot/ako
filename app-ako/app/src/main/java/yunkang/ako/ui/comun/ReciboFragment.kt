package yunkang.ako.ui.comun

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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

    companion object {
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