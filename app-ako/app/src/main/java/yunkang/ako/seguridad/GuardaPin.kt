package yunkang.ako.seguridad

import android.content.Context
import androidx.core.content.edit
import yunkang.ako.dominio.PinSalHash

private const val CLAVE_SAL = "sal"
private const val CLAVE_HASH = "hash"

// el cajón privado del PIN: guarda la sal y el hash, nunca el PIN
// son SharedPreferences privadas de la app: se borran al desinstalarla
class GuardaPin(context: Context) {

    // el archivo "pin" de la app; MODE_PRIVATE: ninguna otra app lo puede leer
    private val preferencias = context.getSharedPreferences("pin", Context.MODE_PRIVATE)

    // ya hay un PIN creado? (1b, crear el PIN, solo sale la primera vez)
    fun existe(): Boolean = preferencias.contains(CLAVE_HASH) && preferencias.contains(CLAVE_SAL)

    // guarda un PIN nuevo: sal al azar + hash. el PIN no se escribe en ningún sitio
    fun guardar(pin: String) {
        val sal = PinSalHash.generarSal()
        val hash = PinSalHash.pbkdf2(pin, sal)
        preferencias.edit {
            putString(CLAVE_SAL, sal)
            putString(CLAVE_HASH, hash)
        }
    }

    // es este el PIN guardado? se pica otra vez con la misma sal y se compara
    fun coincide(pin: String): Boolean {
        val sal = preferencias.getString(CLAVE_SAL, null) ?: return false
        val hash = preferencias.getString(CLAVE_HASH, null) ?: return false
        // [Claude] si el archivo está roto (la sal no es Base64), es «no coincide», no un cierre de la app
        return try {
            PinSalHash.coincide(pin, sal, hash)
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}