package yunkang.ako.ui.comun

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// de borde a borde: la vista se aparta de las barras del sistema (la hora arriba, los botones
// abajo) y del teclado, para que nada quede tapado al escribir
fun View.apartarDeLasBarras() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { vista, insets ->
        val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
        vista.setPadding(barras.left, barras.top, barras.right, barras.bottom)
        insets
    }
}
