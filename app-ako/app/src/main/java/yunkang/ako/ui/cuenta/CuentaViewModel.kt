package yunkang.ako.ui.cuenta


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import yunkang.ako.EntradaAko
import yunkang.ako.datos.repositorios.ComandaRepository
import yunkang.ako.dominio.modelos.MesaEstado

// La libreta de la pantalla 6 (Cuenta): la comparten la rejilla (6a), la comanda (6b) y el recibo (6c).
// Vive mientras dura Cuenta y muere con CuentaActivity (P108). Crece pieza a pieza
class CuentaViewModel(
    private val comandaRepository: ComandaRepository
) : ViewModel() {

    // 6a (P167 A): las 60 mesas, libres u ocupadas con su total. Room las manda solas cada vez que
    // cambia una comanda o una línea (el grifo, Flow); asLiveData las cuelga en el tablón. Nadie las carga a mano
    val mesas: LiveData<List<MesaEstado>> = comandaRepository.mesasConEstado().asLiveData()

    // P140: la fábrica que sabe construir esta libreta con el puesto de comandas de EntradaAko
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as EntradaAko
                CuentaViewModel(app.comandaRepository)
            }
        }
    }
}