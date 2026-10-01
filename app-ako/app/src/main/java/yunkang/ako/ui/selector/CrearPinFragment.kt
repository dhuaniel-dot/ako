package yunkang.ako.ui.selector

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import yunkang.ako.R
import yunkang.ako.databinding.FragmentCrearPinBinding

// 1b · Crear PIN: solo en el primer arranque y obligatoria (ficha 1)
class CrearPinFragment : Fragment(R.layout.fragment_crear_pin) {

    // La misma libreta que la Activity: los Fragments de una Activity la comparten (P108, P140)
    private val viewModel: SelectorViewModel by activityViewModels { SelectorViewModel.Factory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCrearPinBinding.bind(view)

        // Cada vez que cambia un campo, se mira si Aceptar se enciende
        binding.textoPin.doAfterTextChanged {
            binding.campoPin.error = null   // al volver a escribir, se quita el aviso
            revisarBoton(binding)
        }
        binding.textoRepite.doAfterTextChanged {
            revisarBoton(binding)
        }

        binding.botonAceptar.setOnClickListener {
            val pin = binding.textoPin.text.toString()
            val repite = binding.textoRepite.text.toString()

            if (pin != repite) {
                // No coinciden: se vacían los dos campos y se avisa bajo el primero
                binding.textoPin.text?.clear()
                binding.textoRepite.text?.clear()
                binding.campoPin.error = getString(R.string.pin_no_coinciden)
                binding.textoPin.requestFocus()
            } else {
                // Coinciden: se apaga Aceptar (sin doble toque, P142), se guarda y se pasa a 1a
                binding.botonAceptar.isEnabled = false
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.crearPin(pin)
                    parentFragmentManager.commit {
                        replace(R.id.contenedor, SelectorFragment())
                    }
                }
            }
        }
    }

    // Aceptar solo se enciende cuando los dos campos tienen 4 cifras (ficha 1)
    private fun revisarBoton(binding: FragmentCrearPinBinding) {
        val pinCompleto = binding.textoPin.text?.length == 4
        val repiteCompleto = binding.textoRepite.text?.length == 4
        binding.botonAceptar.isEnabled = pinCompleto && repiteCompleto
    }
}