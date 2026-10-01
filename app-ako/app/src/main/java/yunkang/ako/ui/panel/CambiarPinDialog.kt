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

// 1e · Cambiar PIN (D18): actual, nuevo y repetido a la vez.
// Orden al Aceptar (P46 B): primero el actual; después, que los nuevos coincidan; por último se guarda
class CambiarPinDialog : DialogFragment() {

    // La libreta del Panel, que es quien abre este diálogo
    private val viewModel: PanelViewModel by activityViewModels { PanelViewModel.Factory }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogCambiarPinBinding.inflate(layoutInflater)

        val dialogo = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.pin_cambiar_titulo)
            .setView(binding.root)
            .setNegativeButton(R.string.comun_cancelar, null)   // cierra sin cambiar nada
            .setPositiveButton(R.string.comun_aceptar, null)    // su trabajo se pone abajo
            .create()

        // [Claude] Igual que en 1c: el Aceptar de serie cerraría siempre; se cambia al enseñarse
        dialogo.setOnShowListener {
            val aceptar = dialogo.getButton(AlertDialog.BUTTON_POSITIVE)
            revisarBoton(binding, aceptar)

            // Al escribir: se quita el aviso de ese campo y se mira si Aceptar se enciende
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
                aceptar.isEnabled = false   // mientras se comprueba, sin doble toque

                lifecycleScope.launch {
                    if (!viewModel.comprobarPin(actual)) {
                        // 1. El actual está mal: aviso y sacudida en el actual; los nuevos no se tocan
                        avisarActualIncorrecto(binding)
                    } else if (nuevo != repite) {
                        // 2. El actual está bien, pero los nuevos no coinciden: se vacían los dos
                        binding.textoNuevo.text?.clear()
                        binding.textoRepite.text?.clear()
                        binding.campoNuevo.error = getString(R.string.pin_no_coinciden)
                        binding.textoNuevo.requestFocus()
                    } else if (viewModel.cambiarPin(actual, nuevo)) {
                        // 3. Todo bien: guardado. Se cierra (P146: sin romper si la app pasó a segundo plano)
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

    // Aceptar solo se enciende con 4 cifras en los tres campos (ficha 1, D18)
    private fun revisarBoton(binding: DialogCambiarPinBinding, aceptar: Button) {
        val actualCompleto = binding.textoActual.text?.length == 4
        val nuevoCompleto = binding.textoNuevo.text?.length == 4
        val repiteCompleto = binding.textoRepite.text?.length == 4
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