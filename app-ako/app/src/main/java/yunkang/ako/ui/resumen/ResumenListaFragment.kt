package yunkang.ako.ui.resumen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import yunkang.ako.R
import yunkang.ako.databinding.FragmentResumenListaBinding
import yunkang.ako.ui.comun.Formato

// 2g · La lista del Resumen de ingresos (wireframe 02g): el día, las comandas cobradas y el pie.
// [Claude] Es un Fragment (P87 B) para que el recibo la sustituya en el hueco de la Activity, como en Cuenta.
// Crece pieza a pieza: la lista y el pie (pieza 5), el calendario (6) y tocar una comanda (7)
class ResumenListaFragment : Fragment(R.layout.fragment_resumen_lista) {

    // La libreta de la Activity: si Android rehace la pantalla, sigue con el mismo día
    private val viewModel: ResumenIngresosViewModel by activityViewModels { ResumenIngresosViewModel.Factory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentResumenListaBinding.bind(view)

        // ← Atrás: como el del sistema; con la lista delante, cierra 2g y vuelve al Panel sin pedir PIN
        binding.botonAtras.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // El campo Día enseña el día que se mira, escrito por Formato.fecha («Jueves, 17/09/2026»)
        viewModel.dia.observe(viewLifecycleOwner) { dia ->
            binding.textoDia.setText(Formato.fecha(dia))
        }
    }
}