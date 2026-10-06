package yunkang.ako.ui.pedido

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
import yunkang.ako.datos.entidades.Alergeno
import yunkang.ako.datos.entidades.Producto
import yunkang.ako.datos.repositorios.CartaRepository
import yunkang.ako.datos.repositorios.SeguridadRepository
import yunkang.ako.dominio.Carrito
import yunkang.ako.dominio.modelos.CategoriaConPlatos

// [Claude] P170 B: lo que la ficha (5b) enseña de un plato, junto en una sola bandeja
data class DatosFicha(
    val plato: Producto,
    val alergenos: List<Alergeno>     // los marcados; vacía = «Pregunta al personal»
)

// La libreta de la pantalla 5 (Pedir): la comparten la carta (5a), la ficha (5b) y el carrito (5c).
// Vive mientras dura Pedir y muere con PedidoActivity (spec 3). Crece pieza a pieza
class PedidoViewModel(
    private val cartaRepository: CartaRepository,
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

    // 5b (P170 B): la bandeja de la ficha abierta. Solo esta libreta escribe en ella (_ficha);
    // la ficha solo la lee (ficha). Vacía (null) mientras llega el plato
    private val _ficha = MutableLiveData<DatosFicha?>()
    val ficha: LiveData<DatosFicha?> = _ficha

    // El carrito de la mesa (spec 3): lo comparten la carta, la ficha y el carrito. Vive solo en la libreta:
    // no se guarda en ningún sitio (R10) y se pierde al salir de Pedir (RNF-24). Nace en iniciar()
    private val _carrito = MutableLiveData<Carrito>()
    val carrito: LiveData<Carrito> = _carrito

    // P76 A: la Activity la fija al abrirse, y con ella nace el carrito vacío de esa mesa.
    // Si Android rehace la pantalla, la libreta ya la tiene y no se repite (el carrito sigue lleno)
    fun iniciar(mesaId: Long, mesaNumero: Int) {
        if (this.mesaNumero != 0) return
        this.mesaId = mesaId
        this.mesaNumero = mesaNumero
        _carrito.value = Carrito(mesaId)
    }

    // 5b · Añadir: mete el plato en el carrito (si ya estaba, suma en la misma línea; tope 99, R4).
    // Devuelve false si ha tenido que topar, para que la ficha avise (P74 A).
    // El carrito cambia POR DENTRO: hay que volver a dejarlo en el tablón para que suene el timbre
    fun anadir(plato: Producto, cantidad: Int): Boolean {
        val carrito = _carrito.value ?: return false
        val sinTopar = carrito.anadir(plato, cantidad)
        _carrito.value = carrito
        return sinTopar
    }

    // 5b (P170 B): llena la bandeja con un plato. La libreta es una para muchas fichas: primero se vacía,
    // para que al abrir Helado no se vea Entrecot un instante. Si ya tiene este plato (Android rehízo
    // la pantalla), no se vuelve a leer. viewModelScope empieza en el hilo principal; Room cambia de hilo solo
    fun cargarFicha(productoId: Long) {
        if (_ficha.value?.plato?.id == productoId) return
        _ficha.value = null
        viewModelScope.launch {
            val plato = cartaRepository.plato(productoId) ?: return@launch
            _ficha.value = DatosFicha(plato, cartaRepository.alergenosDe(productoId))
        }
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