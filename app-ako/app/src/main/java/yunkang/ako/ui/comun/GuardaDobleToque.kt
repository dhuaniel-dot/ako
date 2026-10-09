package yunkang.ako.ui.comun

import android.os.SystemClock

// [Claude] El portero que mira el reloj. Apunta la hora del último toque
// que dejó pasar y, si el siguiente llega antes de medio segundo, lo ignora: así un dedo nervioso no abre
// dos veces la misma pantalla o la misma caja. Una por pantalla, compartida por todos sus botones.
// Receta: https://stackoverflow.com/questions/5608720/android-preventing-double-click-on-a-button
class GuardaDobleToque(private val esperaMs: Long = 500L) {

    // Milisegundos desde que se encendió el móvil (no cambia si se ajusta la hora del reloj)
    private var ultimoToque = 0L

    // true = adelante; false = es un rebote del toque anterior y no se hace nada
    fun permite(): Boolean {
        val ahora = SystemClock.elapsedRealtime()
        if (ahora - ultimoToque < esperaMs) return false
        ultimoToque = ahora
        return true
    }
}
