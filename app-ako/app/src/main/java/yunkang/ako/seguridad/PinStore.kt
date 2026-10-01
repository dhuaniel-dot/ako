package yunkang.ako.seguridad

import android.content.Context
import androidx.core.content.edit
import yunkang.ako.dominio.Hash

// Los nombres de las dos casillas del archivo (solo se usan en este archivo).
private const val CLAVE_SAL = "sal"
private const val CLAVE_HASH = "hash"

// El cajón privado del PIN: guarda la sal y el hash, nunca el PIN (P122).
// Son SharedPreferences privadas de la app: se borran al desinstalarla.
class PinStore(context: Context) {

    // El archivo "pin" de la app; MODE_PRIVATE: ninguna otra app lo puede leer.
    private val preferencias = context.getSharedPreferences("pin", Context.MODE_PRIVATE)

    // ¿Ya hay un PIN creado? (1b, crear el PIN, solo sale la primera vez)
    fun existe(): Boolean = preferencias.contains(CLAVE_HASH) && preferencias.contains(CLAVE_SAL)

    // Guarda un PIN nuevo: sal al azar + hash. El PIN no se escribe en ningún sitio.
    fun guardar(pin: String) {
        val sal = Hash.generarSal()
        val hash = Hash.pbkdf2(pin, sal)
        preferencias.edit {
            putString(CLAVE_SAL, sal)
            putString(CLAVE_HASH, hash)
        }
    }

    // ¿Es este el PIN guardado? Se pica otra vez con la misma sal y se compara.
    fun coincide(pin: String): Boolean {
        val sal = preferencias.getString(CLAVE_SAL, null) ?: return false
        val hash = preferencias.getString(CLAVE_HASH, null) ?: return false
        // [Claude] Si el archivo está roto (la sal no es Base64), es «no coincide», no un cierre de la app (H15)
        return try {
            Hash.coincide(pin, sal, hash)
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}