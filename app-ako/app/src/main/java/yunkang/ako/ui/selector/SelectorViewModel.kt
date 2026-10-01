package yunkang.ako.ui.selector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import yunkang.ako.EntradaAko
import yunkang.ako.datos.repositorios.SeguridadRepository

// La libreta de la pantalla 1: sobrevive a que Android rehaga la pantalla.
// Las vistas le preguntan a ella, nunca al repositorio (spec 3).
class SelectorViewModel(
    private val seguridadRepository: SeguridadRepository
) : ViewModel() {

    // P141: ¿hay ya un PIN guardado? Leerlo es inmediato, por eso no es suspend
    fun hayPin(): Boolean = seguridadRepository.hayPin()

    // 1b (P142): guarda el primer PIN. Es suspend: la pantalla espera a que esté guardado.
    // Quien saca el trabajo del hilo de la pantalla es el repositorio (P134), no la libreta
    suspend fun crearPin(pin: String) {
        seguridadRepository.crearPin(pin)
    }

    // P140: la fábrica que sabe construir esta libreta con el repositorio de EntradaAko
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as EntradaAko
                SelectorViewModel(app.seguridadRepository)
            }
        }
    }
}