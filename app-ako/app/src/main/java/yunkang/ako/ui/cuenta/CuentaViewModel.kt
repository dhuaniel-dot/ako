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

// la libreta de la pantalla 6 (Cuenta): la comparten la rejilla (6a), la comanda (6b) y el recibo (6c)
// vive mientras dura Cuenta y muere con CuentaActivity
class CuentaViewModel(
    private val comandaRepository: ComandaRepository
) : ViewModel() {

    // 6a: todas las mesas, libres u ocupadas con su total; llegan solas (grifo → tablón)
    val mesas: LiveData<List<MesaEstado>> = comandaRepository.mesasConEstado().asLiveData()

    // 6b y 6c: la comanda abierta y el número de su mesa (para las barras). solo la libreta los cambia
    var comandaId: Long = 0L
        private set
    var mesaNumero: Int = 0
        private set

    // 6b y 6c: las líneas tal como se pintan y el total. solo esta libreta escribe en ellas (_lineas, _total)
    private val _lineas = MutableLiveData<List<LineaVista>>(emptyList())
    val lineas: LiveData<List<LineaVista>> = _lineas
    private val _total = MutableLiveData(0)
    val total: LiveData<Int> = _total

    // la línea que espera mientras está abierto el aviso de la última línea. vive en la libreta,
    // así sigue ahí si Android rehace la pantalla con el aviso delante
    var lineaPorQuitar: Long? = null

    // la bandera contra el doble toque en Quitar, Anular y Cobrar (como enviando en Pedir)
    // mientras una de las tres trabaja, otra llamada no llega al repositorio
    private var trabajando = false

    // 6a → 6b: se abre la comanda de una mesa roja. primero se vacían las bandejas, para que no se vea
    // ni un instante la mesa anterior. Room cambia de hilo solo
    fun abrirComanda(comandaId: Long, mesaNumero: Int) {
        this.comandaId = comandaId
        this.mesaNumero = mesaNumero
        _lineas.value = emptyList()
        _total.value = 0
        viewModelScope.launch { recargar() }
    }

    // [Claude] sin líneas = anulada: la que se va a quitar es la única línea que queda? se mira la lista que se ve
    fun esUltimaLinea(): Boolean = lineas.value?.size == 1

    // 6b · quitar: suspend, la pantalla espera. devuelve true si la comanda quedó anulada
    // por quitar la última línea; si no, vuelve a preguntar líneas y total (nunca se resta en pantalla)
    suspend fun quitarLinea(lineaId: Long): Boolean {
        if (trabajando) return false
        trabajando = true
        // finally: la bandera nunca se queda encendida (como en PedidoViewModel)
        try {
            val anulada = comandaRepository.quitarLinea(lineaId)
            if (!anulada) recargar()
            return anulada
        } finally {
            trabajando = false
        }
    }

    // 6b · anular: anulada con hora de cierre, sin borrar las líneas. devuelve true si se hizo
    suspend fun anular(): Boolean {
        if (trabajando) return false
        trabajando = true
        try {
            comandaRepository.anular(comandaId)
        } finally {
            trabajando = false
        }
        return true
    }

    // 6c · cobrar: pagada con hora de cierre; eso ya es el histórico que leerá el Resumen de ingresos
    // devuelve true si se hizo
    suspend fun cobrar(): Boolean {
        if (trabajando) return false
        trabajando = true
        try {
            comandaRepository.cobrar(comandaId)
        } finally {
            trabajando = false
        }
        return true
    }

    // 6c: lo que hay que devolver si el cliente entrega esto. negativo = falta dinero, no es un error
    // es una función pura: no guarda nada; cerrar el recibo lo olvida
    fun cambio(entregadoCentimos: Int): Int = Calculadora.cambio(total.value ?: 0, entregadoCentimos)

    // vuelve a preguntar las líneas y el total a la base de datos (nunca se resta en la pantalla)
    private suspend fun recargar() {
        val lineas = comandaRepository.lineasDe(comandaId)
        _lineas.value = lineas.map { LineaVista.de(it) }
        _total.value = comandaRepository.totalDe(comandaId)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as EntradaAko
                CuentaViewModel(app.comandaRepository)
            }
        }
    }
}