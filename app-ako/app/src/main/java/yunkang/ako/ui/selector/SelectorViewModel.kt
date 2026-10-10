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

// la libreta de la pantalla 1: sobrevive a que Android rehaga la pantalla
// las vistas le preguntan a ella, nunca al repositorio
class SelectorViewModel(
    private val seguridadRepository: SeguridadRepository,
    private val cartaRepository: CartaRepository,
    comandaRepository: ComandaRepository
) : ViewModel() {

    // las 60 mesas, libres u ocupadas con su total: Room las manda solas cada vez que cambian (Flow → LiveData); nadie las carga a mano
    val mesas: LiveData<List<MesaEstado>> = comandaRepository.mesasConEstado().asLiveData()

    // hay ya un PIN guardado? leerlo es inmediato, por eso no es suspend
    fun hayPin(): Boolean = seguridadRepository.hayPin()

    // 1b: guarda el primer PIN. es suspend: la pantalla espera a que esté guardado
    // quien saca el trabajo del hilo de la pantalla es el repositorio, no la libreta
    suspend fun crearPin(pin: String) {
        seguridadRepository.crearPin(pin)
    }

    // 1c: es este el PIN guardado? también suspend; el repositorio cambia de hilo
    suspend fun comprobarPin(pin: String): Boolean = seguridadRepository.comprobarPin(pin)

    // puerta de Pedir: vacío si se puede entrar; si no, el texto que dice por qué
    // la regla («hay algún plato visible?») la sabe el repositorio; la libreta solo elige el mensaje
    suspend fun motivoPuertaCerrada(): Int? {
        if (cartaRepository.hayPlatoVisible()) return null
        return if (cartaRepository.hayPlatoExistente()) {
            R.string.puerta_pedir_categorias_eliminadas
        } else {
            R.string.puerta_pedir_carta_vacia
        }
    }

    // la fábrica que construye esta libreta con los repositorios de EntradaAko
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as EntradaAko
                SelectorViewModel(app.seguridadRepository, app.cartaRepository, app.comandaRepository)
            }
        }
    }
}