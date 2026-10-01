package yunkang.ako.ui.comun

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// La única caja de «¿seguro?» de toda la app (P84): Enviar, Cobrar, Anular, eliminar con mesas,
// cadena 3e, «¿Salir sin enviar?», la puerta de Pedir... Cada uno le pasa sus textos.
// Los botones dicen lo que hacen («Cobrar», «Eliminar»; nunca «Sí», P40)
class ConfirmacionDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Los textos llegan en «arguments», no por el constructor:
        // si Android rehace el diálogo, los arguments se conservan y el constructor no se vuelve a llamar
        val argumentos = requireArguments()
        val clave = argumentos.getString(ARG_CLAVE)!!
        val negativo = argumentos.getString(ARG_NEGATIVO)

        val caja = MaterialAlertDialogBuilder(requireContext())
            .setTitle(argumentos.getString(ARG_TITULO))
            .setMessage(argumentos.getString(ARG_TEXTO))
            .setPositiveButton(argumentos.getString(ARG_AFIRMATIVO)) { _, _ ->
                // Sobre para quien la abrió: se pulsó el afirmativo
                setFragmentResult(clave, bundleOf(RESPUESTA_AFIRMATIVA to true))
            }

        // P15: el botón negativo es opcional (la puerta de Pedir solo informa).
        // [Claude] P144 A: su sobre lo usan por primera vez 2e (S6) y la cadena 3e (S7)
        if (negativo != null) {
            caja.setNegativeButton(negativo) { _, _ ->
                setFragmentResult(clave, bundleOf(RESPUESTA_AFIRMATIVA to false))
            }
        }
        // Tocar fuera o Atrás cierra sin sobre (P144 A)
        return caja.create()
    }

    companion object {
        // Dentro del sobre: true si se pulsó el afirmativo; false si el negativo (P144 A)
        const val RESPUESTA_AFIRMATIVA = "afirmativa"

        // Los nombres de cada texto dentro de «arguments» (solo se usan en este archivo)
        private const val ARG_TITULO = "titulo"
        private const val ARG_TEXTO = "texto"
        private const val ARG_AFIRMATIVO = "afirmativo"
        private const val ARG_NEGATIVO = "negativo"
        private const val ARG_CLAVE = "clave"

        // La forma de crear una caja: se le dan los textos y el nombre de su sobre
        fun nueva(
            titulo: String,
            texto: String,
            afirmativo: String,
            negativo: String? = null,
            clave: String
        ): ConfirmacionDialog {
            val dialogo = ConfirmacionDialog()
            dialogo.arguments = bundleOf(
                ARG_TITULO to titulo,
                ARG_TEXTO to texto,
                ARG_AFIRMATIVO to afirmativo,
                ARG_NEGATIVO to negativo,
                ARG_CLAVE to clave
            )
            return dialogo
        }
    }
}