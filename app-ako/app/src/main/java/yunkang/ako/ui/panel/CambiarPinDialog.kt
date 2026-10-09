package yunkang.ako.ui.panel

import android.animation.ObjectAnimator
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import yunkang.ako.R
import yunkang.ako.databinding.DialogCambiarPinBinding
import yunkang.ako.dominio.Validacion

// 1e · Cambiar PIN: actual, nuevo y repetido a la vez.
// Orden al Aceptar: primero el actual; después, que los nuevos coincidan; por último se guarda.
// Mientras se comprueba, Aceptar se apaga: sin doble toque
class CambiarPinDialog : DialogFragment() {

    private val viewModel: PanelViewModel by activityViewModels { PanelViewModel.Factory }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogCambiarPinBinding.inflate(layoutInflater)

        val dialogo = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.pin_cambiar_titulo)
            .setView(binding.root)
            .setNegativeButton(R.string.comun_cancelar, null)
            .setPositiveButton(R.string.comun_aceptar, null)
            .create()

        // [Claude] Igual que en 1c: el Aceptar de serie cerraría siempre; se cambia al enseñarse
        dialogo.setOnShowListener {
            val aceptar = dialogo.getButton(AlertDialog.BUTTON_POSITIVE)
            revisarBoton(binding, aceptar)

            binding.textoActual.doAfterTextChanged {
                binding.campoActual.error = null
                revisarBoton(binding, aceptar)
            }
            binding.textoNuevo.doAfterTextChanged {
                binding.campoNuevo.error = null
                revisarBoton(binding, aceptar)
            }
            binding.textoRepite.doAfterTextChanged {
                revisarBoton(binding, aceptar)
            }

            aceptar.setOnClickListener {
                val actual = binding.textoActual.text.toString()
                val nuevo = binding.textoNuevo.text.toString()
                val repite = binding.textoRepite.text.toString()
                aceptar.isEnabled = false

                lifecycleScope.launch {
                    if (!viewModel.comprobarPin(actual)) {
                        // El actual está mal: aviso y sacudida en el actual; los nuevos no se tocan
                        avisarActualIncorrecto(binding)
                    } else if (nuevo != repite) {
                        // El actual está bien, pero los nuevos no coinciden: se vacían los dos
                        binding.textoNuevo.text?.clear()
                        binding.textoRepite.text?.clear()
                        binding.campoNuevo.error = getString(R.string.pin_no_coinciden)
                        binding.textoNuevo.requestFocus()
                    } else if (viewModel.cambiarPin(actual, nuevo)) {
                        // Todo bien: guardado. Se cierra sin romper si la app pasó a segundo plano
                        dismissAllowingStateLoss()
                    } else {
                        // El repositorio vuelve a mirar el actual; si dijera que no, mismo aviso
                        avisarActualIncorrecto(binding)
                    }
                }
            }
        }
        return dialogo
    }

    // Aceptar solo se enciende con 4 cifras en los tres campos (ficha 1)
    private fun revisarBoton(binding: DialogCambiarPinBinding, aceptar: Button) {
        val actualCompleto = binding.textoActual.text?.length == Validacion.LONGITUD_PIN
        val nuevoCompleto = binding.textoNuevo.text?.length == Validacion.LONGITUD_PIN
        val repiteCompleto = binding.textoRepite.text?.length == Validacion.LONGITUD_PIN
        aceptar.isEnabled = actualCompleto && nuevoCompleto && repiteCompleto
    }

    // PIN actual incorrecto: se vacía, se avisa y se sacude, como en 1c (RNF-13)
    private fun avisarActualIncorrecto(binding: DialogCambiarPinBinding) {
        binding.textoActual.text?.clear()
        binding.campoActual.error = getString(R.string.pin_incorrecto)
        binding.textoActual.requestFocus()
        ObjectAnimator.ofFloat(binding.campoActual, "translationX", 0f, 20f, -20f, 15f, -15f, 0f)
            .setDuration(400)
            .start()
    }
}