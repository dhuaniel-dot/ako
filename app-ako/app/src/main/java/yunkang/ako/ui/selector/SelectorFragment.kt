package yunkang.ako.ui.selector

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import yunkang.ako.R
import yunkang.ako.databinding.FragmentSelectorBinding
import yunkang.ako.ui.comun.ConfirmacionDialog

// 1a · Selector de rol: el nombre de la app y los tres botones grandes (P101)
class SelectorFragment : Fragment(R.layout.fragment_selector) {

    // La libreta de la Activity (la misma para 1a y 1b): aquí se le pregunta por la puerta de Pedir
    private val viewModel: SelectorViewModel by activityViewModels { SelectorViewModel.Factory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSelectorBinding.bind(view)

        // Propietario: pide el PIN (1c)
        binding.botonPropietario.setOnClickListener {
            PinDialog().show(parentFragmentManager, "pin")
        }

        // Pedir: la puerta (RF-25). Sin ningún plato visible no se entra: se avisa y se queda en 1a
        binding.botonPedir.setOnClickListener {
            binding.botonPedir.isEnabled = false   // mientras pregunta, sin doble toque (P142)
            viewLifecycleOwner.lifecycleScope.launch {
                val motivo = viewModel.motivoPuertaCerrada()
                binding.botonPedir.isEnabled = true
                if (motivo != null) {
                    // P15: la caja de siempre, con un solo botón (solo informa)
                    ConfirmacionDialog.nueva(
                        titulo = getString(R.string.puerta_pedir_titulo),
                        texto = getString(motivo),
                        afirmativo = getString(R.string.comun_aceptar),
                        clave = "puerta_pedir"
                    ).show(parentFragmentManager, "puerta_pedir")
                } else {
                    // [Claude] Provisional: en la pieza 6 aquí se abre la rejilla 1d
                    Log.d("Ako", "Puerta de Pedir abierta")
                }
            }
        }

        // [Claude] Provisional: Cuenta llega en la S9 (6a).
        // Hasta entonces, una caja con un solo botón que lo dice
        binding.botonCuenta.setOnClickListener {
            ConfirmacionDialog.nueva(
                titulo = getString(R.string.selector_btn_cuenta),
                texto = getString(R.string.pendiente_sesion_posterior),
                afirmativo = getString(R.string.comun_aceptar),
                clave = "pendiente"
            ).show(parentFragmentManager, "pendiente")
        }
    }
}