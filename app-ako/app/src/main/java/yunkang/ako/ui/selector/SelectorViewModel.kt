package yunkang.ako.ui.selector

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import yunkang.ako.EntradaAko
import yunkang.ako.R
import yunkang.ako.datos.repositorios.CartaRepository
import yunkang.ako.datos.repositorios.ComandaRepository
import yunkang.ako.datos.repositorios.SeguridadRepository
import yunkang.ako.dominio.modelos.MesaEstado

// La libreta de la pantalla 1: sobrevive a que Android rehaga la pantalla.
// Las vistas le preguntan a ella, nunca al repositorio (spec 3).
class SelectorViewModel(
    private val seguridadRepository: SeguridadRepository,
    private val cartaRepository: CartaRepository,
    comandaRepository: ComandaRepository
) : ViewModel() {

    // 1d (P167 A): las 60 mesas, libres u ocupadas con su total. Las escribe Room a través del puesto
    // de comandas (el grifo, Flow); asLiveData las cuelga en el tablón. Nadie las carga a mano
    val mesas: LiveData<List<MesaEstado>> = comandaRepository.mesasConEstado().asLiveData()

    // P141: ¿hay ya un PIN guardado? Leerlo es inmediato, por eso no es suspend
    fun hayPin(): Boolean = seguridadRepository.hayPin()

    // 1b (P142): guarda el primer PIN. Es suspend: la pantalla espera a que esté guardado.
    // Quien saca el trabajo del hilo de la pantalla es el repositorio (P134), no la libreta
    suspend fun crearPin(pin: String) {
        seguridadRepository.crearPin(pin)
    }

    // 1c: ¿es este el PIN guardado? También suspend; el repositorio cambia de hilo (P134)
    suspend fun comprobarPin(pin: String): Boolean = seguridadRepository.comprobarPin(pin)

    // Puerta de Pedir (RF-25): vacío si se puede entrar; si no, el texto que dice por qué.
    // La regla («¿hay algún plato visible?», R15) la sabe el repositorio; la libreta solo elige el mensaje
    suspend fun motivoPuertaCerrada(): Int? {
        if (cartaRepository.hayPlatoVisible()) return null
        return if (cartaRepository.hayPlatoExistente()) {
            R.string.puerta_pedir_categorias_eliminadas
        } else {
            R.string.puerta_pedir_carta_vacia
        }
    }

    // P140: la fábrica que sabe construir esta libreta con los repositorios de EntradaAko
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as EntradaAko
                SelectorViewModel(app.seguridadRepository, app.cartaRepository, app.comandaRepository)
            }
        }
    }
}