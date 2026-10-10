package yunkang.ako.ui.resumen

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import yunkang.ako.R
import yunkang.ako.databinding.FragmentResumenListaBinding
import yunkang.ako.ui.comun.Formato
import yunkang.ako.ui.comun.GuardaDobleToque
import java.time.LocalDate

// 2g · la lista del Resumen de ingresos (wireframe 02g): el día, las comandas cobradas y el pie
// [Claude] es un Fragment para que el recibo la sustituya en el hueco de la Activity, como en Cuenta
// tiene el día con su calendario, la lista de comandas cobradas con su pie y, al tocar una, su recibo
class ResumenListaFragment : Fragment(R.layout.fragment_resumen_lista) {

    // si Android rehace la pantalla, sigue con el mismo día
    private val viewModel: ResumenIngresosViewModel by activityViewModels { ResumenIngresosViewModel.Factory }

    // el adaptador de las filas; vive con el Fragment y se le da a cada vista nueva
    // tocar una fila: el marco (la Activity) abre su recibo en solo lectura en el lugar de la lista
    private val adaptador = ComandaCobradaAdapter { comanda ->
        if (portero.permite()) (requireActivity() as ResumenIngresosActivity).abrirRecibo(comanda)
    }

    // su portero: un segundo toque seguido no abre otro calendario u otro recibo encima
    private val portero = GuardaDobleToque()

    // solo mientras hay vista: onDestroyView lo necesita para soltar el adaptador
    private var binding: FragmentResumenListaBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentResumenListaBinding.bind(view)
        this.binding = binding

        // ← Atrás: con la lista delante, cierra 2g y vuelve al Panel sin pedir PIN
        binding.botonAtras.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // la lista, una fila debajo de otra, con una raya entre filas como en el dibujo [Claude]
        binding.listaComandas.layoutManager = LinearLayoutManager(requireContext())
        binding.listaComandas.adapter = adaptador
        binding.listaComandas.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )

        // el campo Día enseña el día que se mira, escrito por Formato.fecha («Jueves, 8 de octubre de 2026»)
        viewModel.dia.observe(viewLifecycleOwner) { dia ->
            binding.textoDia.setText(Formato.fecha(dia))
        }

        // tocar el campo Día abre el calendario estándar de Android, puesto en el día que se mira
        // la trampa de los meses: Android los cuenta desde 0 (enero = 0) y java.time desde 1 (enero = 1),
        // así que al abrir se resta uno y al volver se suma uno. el calendario no guarda nada: solo dice qué día mirar
        binding.textoDia.setOnClickListener {
            if (!portero.permite()) return@setOnClickListener
            val dia = viewModel.dia.value ?: return@setOnClickListener
            DatePickerDialog(
                requireContext(),
                { _, anio, mes, diaDelMes -> viewModel.elegirDia(LocalDate.of(anio, mes + 1, diaDelMes)) },
                dia.year,
                dia.monthValue - 1,
                dia.dayOfMonth
            ).show()
        }

        // lo cobrado ese día: las filas, cuántas y el total del pie (calculados, no guardados)
        // un día sin cobros no es un error (ficha 2g): el aviso en lugar de la lista, y el pie a 0 y 0,00 €
        viewModel.resumen.observe(viewLifecycleOwner) { resumen ->
            adaptador.mostrar(resumen.comandas)
            val vacio = resumen.comandas.isEmpty()
            binding.textoVacio.isVisible = vacio
            binding.listaComandas.isVisible = !vacio
            binding.textoNumComandas.text = getString(R.string.comun_numero, resumen.comandas.size)
            binding.textoTotalDia.text = getString(R.string.comun_precio, Formato.precio(resumen.totalCentimos))
        }
    }

    // al quitar la vista, la lista suelta su adaptador y se olvida el binding (como en ReciboFragment)
    override fun onDestroyView() {
        binding?.listaComandas?.adapter = null
        super.onDestroyView()
        binding = null
    }
}