package yunkang.ako.ui.panel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import yunkang.ako.EntradaAko
import yunkang.ako.datos.repositorios.CartaRepository
import yunkang.ako.datos.repositorios.SeguridadRepository
import yunkang.ako.dominio.modelos.CategoriaConPlatos

// La libreta del Panel (pantalla 2): el PIN (1e) y, desde la S6, la carta en modo edición (2a)
class PanelViewModel(
    private val seguridadRepository: SeguridadRepository,
    private val cartaRepository: CartaRepository
) : ViewModel() {

    // 2a (P49 B): el tablón con las cajas. Lo escribe Room a través del repositorio (el grifo, Flow);
    // asLiveData lo cuelga en el tablón y cierra el grifo solo cuando nadie mira la pantalla.
    // Nadie lo escribe a mano: por eso no hay MutableLiveData ni cargar()
    val categoriasConPlatos: LiveData<List<CategoriaConPlatos>> =
        cartaRepository.categoriasConPlatos().asLiveData()

    // 1e (P46 B): primero se comprueba el PIN actual, antes de mirar los nuevos
    suspend fun comprobarPin(pin: String): Boolean = seguridadRepository.comprobarPin(pin)

    // 1e: guarda el PIN nuevo. El repositorio vuelve a mirar el actual (D18) y cambia de hilo (P134)
    suspend fun cambiarPin(actual: String, nuevo: String): Boolean =
        seguridadRepository.cambiarPin(actual, nuevo)

    // P140: la fábrica que construye esta libreta con los dos repositorios de EntradaAko
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as EntradaAko
                PanelViewModel(app.seguridadRepository, app.cartaRepository)
            }
        }
    }
}