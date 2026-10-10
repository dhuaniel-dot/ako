package yunkang.ako.datos.repositorios

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import yunkang.ako.dominio.Comprobacion
import yunkang.ako.seguridad.GuardaPin

// la puerta única al PIN para las pantallas: nadie más toca GuardaPin
// recibe GuardaPin por constructor. picar el PIN tarda: se hace fuera del hilo principal
class SeguridadRepository(private val guardaPin: GuardaPin) {

    // 1a/1b: ya hay PIN (se pide) o es la primera vez (se crea)?
    fun hayPin(): Boolean = guardaPin.existe()

    // 1b: guarda el primer PIN; solo 4 cifras (la pantalla ya lo impide, aquí se asegura)
    suspend fun crearPin(pin: String) {
        withContext(Dispatchers.Default) {
            // la regla del PIN actual: si ya hay PIN, no se pisa; para cambiarlo hay que dar el actual (cambiarPin)
            check(!guardaPin.existe()) { "Ya hay un PIN: se cambia con cambiarPin" }
            require(Comprobacion.pinValido(pin)) { "El PIN tiene que tener 4 cifras" }
            guardaPin.guardar(pin)
        }
    }

    // 1c y salida de Pedir: es este el PIN guardado?
    suspend fun comprobarPin(pin: String): Boolean = withContext(Dispatchers.Default) {
        guardaPin.coincide(pin)
    }

    // 1e: primero el PIN actual; si no coincide, false y el nuevo ni se mira ni se guarda
    suspend fun cambiarPin(actual: String, nuevo: String): Boolean = withContext(Dispatchers.Default) {
        if (!guardaPin.coincide(actual)) {
            false
        } else {
            require(Comprobacion.pinValido(nuevo)) { "El PIN tiene que tener 4 cifras" }
            guardaPin.guardar(nuevo)
            true
        }
    }
}