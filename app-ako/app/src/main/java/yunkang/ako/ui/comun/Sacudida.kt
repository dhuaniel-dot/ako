package yunkang.ako.ui.comun

import android.animation.ObjectAnimator
import android.view.View

// Hasta dónde se mueve el campo a cada lado y cuánto dura todo
private val RECORRIDO = floatArrayOf(0f, 20f, -20f, 15f, -15f, 0f)
private const val DURACION_MS = 400L

// Sacude una vista a los lados y la deja donde estaba: el aviso de «PIN incorrecto»
// que también se nota sin leer (no solo con color)
fun sacudir(vista: View) {
    ObjectAnimator.ofFloat(vista, "translationX", *RECORRIDO)
        .setDuration(DURACION_MS)
        .start()
}
