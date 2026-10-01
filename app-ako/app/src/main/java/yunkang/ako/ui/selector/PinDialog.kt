package yunkang.ako.ui.selector

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import yunkang.ako.R
import yunkang.ako.databinding.DialogPinBinding
import android.animation.ObjectAnimator

// 1c · Introducir PIN. No decide nada: pregunta a la libreta y, si es correcto,
// deja un sobre (CLAVE_RESULTADO) para quien lo abrió. Cancelar cierra sin sobre
class PinDialog : DialogFragment() {

    // La libreta de la Activity que lo abre (hoy, la pantalla 1)
    private val viewModel: SelectorViewModel by activityViewModels { SelectorViewModel.Factory }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogPinBinding.inflate(layoutInflater)

        val dialogo = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.pin_introducir_titulo)
            .setView(binding.root)
            .setNegativeButton(R.string.comun_cancelar, null)   // cierra y no deja sobre
            .setPositiveButton(R.string.comun_aceptar, null)    // su trabajo se pone abajo
            .create()

        // [Claude] El Aceptar de serie cierra el diálogo siempre; se cambia al enseñarse,
        // para que con un PIN incorrecto el diálogo siga abierto
        dialogo.setOnShowListener {
            val aceptar = dialogo.getButton(AlertDialog.BUTTON_POSITIVE)
            aceptar.isEnabled = binding.textoPin.text?.length == 4

            // Aceptar solo se enciende con 4 cifras (ficha 1)
            binding.textoPin.doAfterTextChanged {
                binding.campoPin.error = null
                aceptar.isEnabled = binding.textoPin.text?.length == 4
            }

            aceptar.setOnClickListener {
                val pin = binding.textoPin.text.toString()
                aceptar.isEnabled = false   // mientras se comprueba, sin doble toque
                lifecycleScope.launch {
                    if (viewModel.comprobarPin(pin)) {
                        setFragmentResult(CLAVE_RESULTADO, Bundle())
                        dismiss()
                    } else {
                        // Incorrecto: se vacía el campo, se avisa y se sacude
                        binding.textoPin.text?.clear()
                        binding.campoPin.error = getString(R.string.pin_incorrecto)
                        // Sacudida: el campo se mueve a los lados y vuelve a su sitio (spec 10, RNF-13)
                        ObjectAnimator.ofFloat(binding.campoPin, "translationX", 0f, 20f, -20f, 15f, -15f, 0f)
                            .setDuration(400)
                            .start()
                    }
                }
            }
        }
        return dialogo
    }

    companion object {
        // [Claude] El nombre del sobre que deja el diálogo cuando el PIN es correcto
        const val CLAVE_RESULTADO = "pin_correcto"
    }
}