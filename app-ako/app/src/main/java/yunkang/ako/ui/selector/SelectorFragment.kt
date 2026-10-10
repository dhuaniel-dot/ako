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
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import yunkang.ako.ui.comun.Tema

// 1a · selector de rol: el nombre de la app y los tres botones grandes
class SelectorFragment : Fragment(R.layout.fragment_selector) {

    private val viewModel: SelectorViewModel by activityViewModels { SelectorViewModel.Factory }

    // el portero de 1a; un doble toque no abre dos cajas del PIN ni dos veces Cuenta
    // Pedir no lo necesita: ya se apaga mientras pregunta
    private val portero = GuardaDobleToque()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSelectorBinding.bind(view)

        // el icono enseña a qué modo se pasa: luna si ahora está en claro, sol si está en oscuro
        val enOscuro = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        if (enOscuro) {
            binding.botonModo.setImageResource(R.drawable.icono_sol)
            binding.botonModo.contentDescription = getString(R.string.selector_modo_claro_cd)
        } else {
            binding.botonModo.setImageResource(R.drawable.icono_luna)
            binding.botonModo.contentDescription = getString(R.string.selector_modo_oscuro_cd)
        }

        // Android rehace la pantalla solo al cambiar el modo
        binding.botonModo.setOnClickListener {
            if (portero.permite()) {
                val nuevo = if (enOscuro) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
                Tema.guardar(requireContext(), nuevo)
                AppCompatDelegate.setDefaultNightMode(nuevo)
            }
        }

        binding.botonPropietario.setOnClickListener {
            if (portero.permite()) {
                PinDialog().show(parentFragmentManager, "pin")
            }
        }

        // Pedir: la puerta. sin ningún plato visible no se entra: se avisa y se queda en 1a
        binding.botonPedir.setOnClickListener {
            binding.botonPedir.isEnabled = false
            viewLifecycleOwner.lifecycleScope.launch {
                val motivo = viewModel.motivoPuertaCerrada()
                binding.botonPedir.isEnabled = true
                if (motivo != null) {
                    // la caja de siempre, con un solo botón (solo informa)
                    ConfirmacionDialog.nueva(
                        titulo = getString(R.string.puerta_pedir_titulo),
                        texto = getString(motivo),
                        afirmativo = getString(R.string.comun_aceptar),
                        clave = CLAVE_PUERTA
                    ).show(parentFragmentManager, CLAVE_PUERTA)
                } else {
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