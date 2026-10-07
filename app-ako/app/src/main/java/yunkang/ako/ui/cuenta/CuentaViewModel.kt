package yunkang.ako.ui.cuenta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import yunkang.ako.EntradaAko
import yunkang.ako.datos.repositorios.ComandaRepository
import yunkang.ako.dominio.Calculadora
import yunkang.ako.dominio.modelos.MesaEstado
import yunkang.ako.ui.comun.LineaVista

// La libreta de la pantalla 6 (Cuenta): la comparten la rejilla (6a), la comanda (6b) y el recibo (6c).
// Vive mientras dura Cuenta y muere con CuentaActivity (P108). Crece pieza a pieza
class CuentaViewModel(
    private val comandaRepository: ComandaRepository
) : ViewModel() {

    // 6a (P167 A): las 60 mesas, libres u ocupadas con su total. Room las manda solas cada vez que
    // cambia una comanda o una línea (el grifo, Flow); asLiveData las cuelga en el tablón. Nadie las carga a mano
    val mesas: LiveData<List<MesaEstado>> = comandaRepository.mesasConEstado().asLiveData()

    // 6b y 6c: la comanda abierta y el número de su mesa (para las barras). Solo la libreta los cambia
    var comandaId: Long = 0L
        private set
    var mesaNumero: Int = 0
        private set

    // 6b y 6c: las líneas tal como se pintan y el TOTAL. Solo esta libreta escribe en ellas (_lineas, _total)
    private val _lineas = MutableLiveData<List<LineaVista>>(emptyList())
    val lineas: LiveData<List<LineaVista>> = _lineas
    private val _total = MutableLiveData(0)
    val total: LiveData<Int> = _total

    // 6a → 6b: se abre la comanda de una mesa roja. Primero se vacían las bandejas, para que no se vea
    // ni un instante la mesa anterior. viewModelScope empieza en el hilo principal; Room cambia de hilo solo
    fun abrirComanda(comandaId: Long, mesaNumero: Int) {
        this.comandaId = comandaId
        this.mesaNumero = mesaNumero
        _lineas.value = emptyList()
        _total.value = 0
        viewModelScope.launch { recargar() }
    }

    // Vuelve a preguntar las líneas y el total a la base de datos (R10: el total se suma en SQL cada vez,
    // nunca se resta en la pantalla). R14: nombre y precio salen de la línea, congelados al enviar
    private suspend fun recargar() {
        val lineas = comandaRepository.lineasDe(comandaId)
        _lineas.value = lineas.map { linea ->
            // En una comanda, el id de LineaVista es el de la línea (P68 A)
            LineaVista(
                linea.id,
                linea.cantidad,
                linea.nombreProducto,
                Calculadora.importe(linea.precioUnitarioCentimos, linea.cantidad)
            )
        }
        _total.value = comandaRepository.totalDe(comandaId)
    }

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