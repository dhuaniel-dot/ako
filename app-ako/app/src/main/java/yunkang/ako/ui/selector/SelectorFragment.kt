package yunkang.ako.ui.selector

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import yunkang.ako.R
import yunkang.ako.databinding.FragmentSelectorBinding
import yunkang.ako.ui.comun.ConfirmacionDialog

// 1a · Selector de rol: el nombre de la app y los tres botones grandes (P101)
class SelectorFragment : Fragment(R.layout.fragment_selector) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSelectorBinding.bind(view)

        // Propietario: pide el PIN (1c)
        binding.botonPropietario.setOnClickListener {
            PinDialog().show(parentFragmentManager, "pin")
        }

        // [Claude] Provisional: Pedir llega en la S8 (1d) y Cuenta en la S9 (6a).
        // Hasta entonces, una caja con un solo botón que lo dice
        binding.botonPedir.setOnClickListener {
            ConfirmacionDialog.nueva(
                titulo = getString(R.string.selector_btn_pedir),
                texto = getString(R.string.pendiente_sesion_posterior),
                afirmativo = getString(R.string.comun_aceptar),
                clave = "pendiente"
            ).show(parentFragmentManager, "pendiente")
        }
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