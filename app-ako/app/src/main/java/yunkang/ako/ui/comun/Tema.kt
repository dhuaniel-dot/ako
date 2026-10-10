package yunkang.ako.ui.comun

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

private const val ARCHIVO = "tema"
private const val CLAVE_MODO = "modo"

object Tema {

    fun leer(context: Context): Int =
        context.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)
            .getInt(CLAVE_MODO, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

    fun guardar(context: Context, modo: Int) {
        context.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE).edit { putInt(CLAVE_MODO, modo) }
    }
}