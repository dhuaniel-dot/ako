package yunkang.ako.ui.selector

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import yunkang.ako.R
import yunkang.ako.databinding.DialogPinBinding
import yunkang.ako.dominio.Comprobacion
import yunkang.ako.ui.comun.ComprobadorPin
import yunkang.ako.ui.comun.sacudir

// 1c · Introducir PIN. No decide nada: pregunta a quien lo abrió (un ComprobadorPin) y, si es correcto,
// deja un sobre (CLAVE_RESULTADO) para quien lo abrió. Cancelar cierra sin sobre.
// Mientras se comprueba, Aceptar se apaga: sin doble toque
class PinDialog : DialogFragment() {

    // La Activity que lo abre tiene que saber comprobar un PIN (SelectorActivity o PedidoActivity)
    private val comprobador: ComprobadorPin
        get() = requireActivity() as ComprobadorPin

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogPinBinding.inflate(layoutInflater)

        val dialogo = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.pin_introducir_titulo)
            .setView(binding.root)
            .setNegativeButton(R.string.comun_cancelar, null)
            .setPositiveButton(R.string.comun_aceptar, null)
            .create()

        // [Claude] El Aceptar de serie cierra el diálogo siempre; se cambia al enseñarse,
        // para que con un PIN incorrecto el diálogo siga abierto
        dialogo.setOnShowListener {
            val aceptar = dialogo.getButton(AlertDialog.BUTTON_POSITIVE)
            aceptar.isEnabled = binding.textoPin.text?.length == Comprobacion.LONGITUD_PIN

            // Aceptar solo se enciende con 4 cifras (ficha 1)
            binding.textoPin.doAfterTextChanged {
                binding.campoPin.error = null
                aceptar.isEnabled = binding.textoPin.text?.length == Comprobacion.LONGITUD_PIN
            }

            aceptar.setOnClickListener {
                val pin = binding.textoPin.text.toString()
                aceptar.isEnabled = false
                lifecycleScope.launch {
                    if (comprobador.comprobarPin(pin)) {
                        setFragmentResult(CLAVE_RESULTADO, Bundle())
                        // Si mientras se picaba el PIN la pantalla pasó a segundo plano (Home),
                        // dismiss() normal rompería la app; esta versión cierra igual sin romper
                        dismissAllowingStateLoss()
                    } else {
                        binding.textoPin.text?.clear()
                        binding.campoPin.error = getString(R.string.pin_incorrecto)
                        sacudir(binding.campoPin)
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
