package yunkang.ako.ui.panel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import yunkang.ako.EntradaAko
import yunkang.ako.datos.repositorios.SeguridadRepository

// La libreta del Panel (pantalla 2). En la S5 solo sabe lo del PIN; la carta llega en la S6
class PanelViewModel(
    private val seguridadRepository: SeguridadRepository
) : ViewModel() {

    // 1e (P46 B): primero se comprueba el PIN actual, antes de mirar los nuevos
    suspend fun comprobarPin(pin: String): Boolean = seguridadRepository.comprobarPin(pin)

    // 1e: guarda el PIN nuevo. El repositorio vuelve a mirar el actual (D18) y cambia de hilo (P134)
    suspend fun cambiarPin(actual: String, nuevo: String): Boolean =
        seguridadRepository.cambiarPin(actual, nuevo)

    // P140: la fábrica que construye esta libreta con el repositorio de EntradaAko
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as EntradaAko
                PanelViewModel(app.seguridadRepository)
            }
        }
    }
}