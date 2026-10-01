package yunkang.ako.datos.repositorios

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import yunkang.ako.dominio.Validacion
import yunkang.ako.seguridad.PinStore

// La puerta única al PIN para las pantallas: nadie más toca PinStore (spec 3).
// Recibe PinStore por constructor (P17). Picar el PIN tarda: se hace fuera del hilo principal (P134).
class SeguridadRepository(private val pinStore: PinStore) {

    // 1a/1b: ¿ya hay PIN (se pide) o es la primera vez (se crea)?
    fun hayPin(): Boolean = pinStore.existe()

    // 1b: guarda el primer PIN; solo 4 cifras (la pantalla ya lo impide, aquí se asegura).
    suspend fun crearPin(pin: String) {
        withContext(Dispatchers.Default) {
            require(Validacion.pinValido(pin)) { "El PIN tiene que tener 4 cifras" }
            pinStore.guardar(pin)
        }
    }

    // 1c y salida de Pedir: ¿es este el PIN guardado?
    suspend fun comprobarPin(pin: String): Boolean = withContext(Dispatchers.Default) {
        pinStore.coincide(pin)
    }

    // 1e (D18): primero el PIN actual; si no coincide, false y el nuevo ni se mira ni se guarda.
    suspend fun cambiarPin(actual: String, nuevo: String): Boolean = withContext(Dispatchers.Default) {
        if (!pinStore.coincide(actual)) {
            false
        } else {
            require(Validacion.pinValido(nuevo)) { "El PIN tiene que tener 4 cifras" }
            pinStore.guardar(nuevo)
            true
        }
    }
}