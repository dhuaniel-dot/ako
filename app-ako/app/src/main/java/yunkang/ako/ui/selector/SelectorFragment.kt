package yunkang.ako.ui.selector

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import yunkang.ako.R
import yunkang.ako.databinding.FragmentSelectorBinding
import yunkang.ako.ui.comun.ConfirmacionDialog
import yunkang.ako.ui.comun.GuardaDobleToque
import yunkang.ako.ui.cuenta.CuentaActivity

// 1a · Selector de rol: el nombre de la app y los tres botones grandes
class SelectorFragment : Fragment(R.layout.fragment_selector) {

    private val viewModel: SelectorViewModel by activityViewModels { SelectorViewModel.Factory }

    // El portero de 1a; un doble toque no abre dos cajas del PIN ni dos veces Cuenta.
    // Pedir no lo necesita: ya se apaga mientras pregunta
    private val portero = GuardaDobleToque()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSelectorBinding.bind(view)

        // Propietario: pide el PIN (1c)
        binding.botonPropietario.setOnClickListener {
            if (portero.permite()) {
                PinDialog().show(parentFragmentManager, "pin")
            }
        }

        // Pedir: la puerta. Sin ningún plato visible no se entra: se avisa y se queda en 1a
        binding.botonPedir.setOnClickListener {
            binding.botonPedir.isEnabled = false
            viewLifecycleOwner.lifecycleScope.launch {
                val motivo = viewModel.motivoPuertaCerrada()
                binding.botonPedir.isEnabled = true
                if (motivo != null) {
                    // La caja de siempre, con un solo botón (solo informa)
                    ConfirmacionDialog.nueva(
                        titulo = getString(R.string.puerta_pedir_titulo),
                        texto = getString(motivo),
                        afirmativo = getString(R.string.comun_aceptar),
                        clave = CLAVE_PUERTA
                    ).show(parentFragmentManager, CLAVE_PUERTA)
                } else {
                    // Puerta abierta: la Activity enseña 1d (la rejilla en modo elegir)
                    (requireActivity() as SelectorActivity).abrirElegirMesa()
                }
            }
        }

        // Cuenta: abre la pantalla 6 sin PIN (ficha 1: la usa el camarero)
        binding.botonCuenta.setOnClickListener {
            if (portero.permite()) {
                startActivity(Intent(requireContext(), CuentaActivity::class.java))
            }
        }
    }

    companion object {
        private const val CLAVE_PUERTA = "puerta_pedir"
    }
}