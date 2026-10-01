package yunkang.ako.ui.selector

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import yunkang.ako.R
import yunkang.ako.databinding.FragmentSelectorBinding

// 1a · Selector de rol: el nombre de la app y los tres botones grandes (P101)
class SelectorFragment : Fragment(R.layout.fragment_selector) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSelectorBinding.bind(view)

        // Propietario: pide el PIN (1c). Pedir y Cuenta se conectan en la pieza 12
        binding.botonPropietario.setOnClickListener {
            PinDialog().show(parentFragmentManager, "pin")
        }
    }
}