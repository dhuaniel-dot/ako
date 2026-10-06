package yunkang.ako.ui.pedido

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

// La libreta de la pantalla 5 (Pedir): la comparten la carta (5a), la ficha (5b) y el carrito (5c).
// Vive mientras dura Pedir y muere con PedidoActivity (spec 3). Crece pieza a pieza
class PedidoViewModel(
    cartaRepository: CartaRepository,
    private val seguridadRepository: SeguridadRepository
) : ViewModel() {

    // La mesa para la que se pide: el id va a la base de datos; el número, a la barra («Mesa 7»).
    // Solo la libreta los cambia (private set)
    var mesaId: Long = 0L
        private set
    var mesaNumero: Int = 0
        private set

    // 5a (P167 A): la carta por secciones, solo con platos visibles (R15). La escribe Room a través
    // del repositorio (el grifo, Flow); asLiveData la cuelga en el tablón. Nadie la carga a mano
    val carta: LiveData<List<CategoriaConPlatos>> = cartaRepository.cartaVisible().asLiveData()

    // P76 A: la Activity la fija al abrirse. Si Android rehace la pantalla, la libreta ya la tiene y no se repite
    fun iniciar(mesaId: Long, mesaNumero: Int) {
        if (this.mesaNumero != 0) return
        this.mesaId = mesaId
        this.mesaNumero = mesaNumero
    }

    // Salir de Pedir (1c): ¿es este el PIN guardado? El repositorio cambia de hilo (P134)
    suspend fun comprobarPin(pin: String): Boolean = seguridadRepository.comprobarPin(pin)

    // P140: la fábrica que sabe construir esta libreta con los repositorios de EntradaAko
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as EntradaAko
                PedidoViewModel(app.cartaRepository, app.seguridadRepository)
            }
        }
    }
}