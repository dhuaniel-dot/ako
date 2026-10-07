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

// 2g · La lista del Resumen de ingresos (wireframe 02g): el día, las comandas cobradas y el pie.
// [Claude] Es un Fragment (P87 B) para que el recibo la sustituya en el hueco de la Activity, como en Cuenta.
// Crece pieza a pieza: la lista y el pie (pieza 5), el calendario (6) y tocar una comanda (7)
class ResumenListaFragment : Fragment(R.layout.fragment_resumen_lista) {

    // La libreta de la Activity: si Android rehace la pantalla, sigue con el mismo día
    private val viewModel: ResumenIngresosViewModel by activityViewModels { ResumenIngresosViewModel.Factory }

    // P86 A: el adaptador de las filas; vive con el Fragment y se le da a cada vista nueva
    private val adaptador = ComandaCobradaAdapter()

    // H01 B (P171): su portero; un segundo toque seguido en el campo Día no abre otro calendario encima
    private val portero = GuardaDobleToque()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentResumenListaBinding.bind(view)

        // ← Atrás: como el del sistema; con la lista delante, cierra 2g y vuelve al Panel sin pedir PIN
        binding.botonAtras.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // La lista, una fila debajo de otra, con una raya entre filas como en el dibujo [Claude]
        binding.listaComandas.layoutManager = LinearLayoutManager(requireContext())
        binding.listaComandas.adapter = adaptador
        binding.listaComandas.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )

        // El campo Día enseña el día que se mira, escrito por Formato.fecha («Jueves, 17/09/2026»)
        viewModel.dia.observe(viewLifecycleOwner) { dia ->
            binding.textoDia.setText(Formato.fecha(dia))
        }

        // P85 A: tocar el campo Día abre el calendario estándar de Android, puesto en el día que se mira.
        // La trampa de los meses: Android los cuenta desde 0 (enero = 0) y java.time desde 1 (enero = 1),
        // así que al abrir se resta uno y al volver se suma uno. El calendario no guarda nada: solo dice qué día mirar
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

        // Lo cobrado ese día: las filas, cuántas y el total del pie (R10: calculados, no guardados).
        // Un día sin cobros no es un error (ficha 2g): el aviso en lugar de la lista, y el pie a 0 y 0,00 €
        viewModel.resumen.observe(viewLifecycleOwner) { resumen ->
            adaptador.mostrar(resumen.comandas)
            val vacio = resumen.comandas.isEmpty()
            binding.textoVacio.isVisible = vacio
            binding.listaComandas.isVisible = !vacio
            binding.textoNumComandas.text = resumen.numComandas.toString()
            binding.textoTotalDia.text = getString(R.string.comun_precio, Formato.precio(resumen.totalCentimos))
        }
    }

    // P196 A: al quitar la vista (el recibo se pone en su lugar, pieza 7), la lista suelta su adaptador
    override fun onDestroyView() {
        FragmentResumenListaBinding.bind(requireView()).listaComandas.adapter = null
        super.onDestroyView()
    }
}