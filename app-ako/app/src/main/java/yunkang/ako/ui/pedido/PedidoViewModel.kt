package yunkang.ako.ui.pedido

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import yunkang.ako.EntradaAko
import yunkang.ako.datos.entidades.Alergeno
import yunkang.ako.datos.entidades.Producto
import yunkang.ako.datos.repositorios.CartaRepository
import yunkang.ako.datos.repositorios.ComandaRepository
import yunkang.ako.datos.repositorios.SeguridadRepository
import yunkang.ako.dominio.Carrito
import yunkang.ako.dominio.modelos.CategoriaConPlatos
import yunkang.ako.ui.comun.LineaVista

// [Claude] lo que la ficha (5b) enseña de un plato, junto en una sola bandeja
// la lista de alérgenos lleva solo los marcados; si va vacía, la ficha dice «Pregunta al personal»
data class DatosFicha(
    val plato: Producto,
    val alergenos: List<Alergeno>
)

// la libreta de la pantalla 5 (Pedir): la comparten la carta (5a), la ficha (5b) y el carrito (5c)
// vive mientras dura Pedir y muere con PedidoActivity
class PedidoViewModel(
    private val cartaRepository: CartaRepository,
    private val comandaRepository: ComandaRepository,
    private val seguridadRepository: SeguridadRepository
) : ViewModel() {

    // la mesa para la que se pide: el id va a la base de datos; el número, a la barra («Mesa 7»)
    // solo la libreta los cambia (private set)
    var mesaId: Long = 0L
        private set
    var mesaNumero: Int = 0
        private set

    // 5a: la carta por secciones, solo con platos visibles. la escribe Room a través
    // del repositorio (el grifo, Flow); asLiveData la cuelga en el tablón. nadie la carga a mano
    val carta: LiveData<List<CategoriaConPlatos>> = cartaRepository.cartaVisible().asLiveData()

    // 5b: la bandeja de la ficha abierta. solo esta libreta escribe en ella (_ficha);
    // la ficha solo la lee (ficha). vacía (null) mientras llega el plato
    private val _ficha = MutableLiveData<DatosFicha?>()
    val ficha: LiveData<DatosFicha?> = _ficha

    // el carrito de la mesa: lo comparten la carta, la ficha y el carrito. vive solo en la libreta:
    // no se guarda en ningún sitio y se pierde al salir de Pedir. nace en iniciar()
    private val _carrito = MutableLiveData<Carrito>()
    val carrito: LiveData<Carrito> = _carrito

    // 5c: las líneas del carrito tal como se pintan. map las saca del carrito cada vez que suena
    // su timbre: son LineaVista nuevas cada vez, así que la lista siempre ve el cambio de cantidad
    val lineasVista: LiveData<List<LineaVista>> = carrito.map { c ->
        c.lineas.map { linea -> LineaVista(linea.producto.id, linea.cantidad, linea.producto.nombre, linea.importe()) }
    }

    // la Activity fija la mesa al abrirse, y con ella nace el carrito vacío de esa mesa
    // si Android rehace la pantalla, la libreta ya la tiene y no se repite (el carrito sigue lleno)
    fun iniciar(mesaId: Long, mesaNumero: Int) {
        if (this.mesaNumero != 0) return
        this.mesaId = mesaId
        this.mesaNumero = mesaNumero
        _carrito.value = Carrito(mesaId)
    }

    // 5b · añadir: mete el plato en el carrito (si ya estaba, suma en la misma línea; tope 99)
    // devuelve false si ha tenido que topar, para que la ficha avise
    // el carrito cambia por dentro: hay que volver a dejarlo en el tablón para que suene el timbre
    fun anadir(plato: Producto, cantidad: Int): Boolean {
        val carrito = _carrito.value ?: return false
        val sinTopar = carrito.anadir(plato, cantidad)
        _carrito.value = carrito
        return sinTopar
    }

    // 5c · − y +: pone una cantidad a una línea (Carrito no deja salir de 1–99) y vuelve a dejar el carrito
    fun cambiarCantidad(productoId: Long, cantidad: Int) {
        val carrito = _carrito.value ?: return
        carrito.cambiarCantidad(productoId, cantidad)
        _carrito.value = carrito
    }

    // 5c · quitar: quita la línea entera, sin preguntar (ficha 5), y vuelve a dejar el carrito
    fun quitar(productoId: Long) {
        val carrito = _carrito.value ?: return
        carrito.quitar(productoId)
        _carrito.value = carrito
    }

    // [Claude] true mientras se envía: un segundo toque no vuelve a enviar las mismas líneas
    private var enviando = false

    // 5c · enviar: el repositorio, todo en una transacción, crea la comanda o le añade líneas y garantiza una sola
    // comanda abierta por mesa, de 1 a 99 unidades, ningún precio negativo y nombre y precio congelados al enviar
    // si va bien, la libreta cambia el carrito por uno nuevo y vacío de la misma mesa y devuelve true
    // es suspend: la pantalla espera; Room hace el trabajo fuera del hilo de la pantalla
    suspend fun enviar(): Boolean {
        val carrito = _carrito.value ?: return false
        if (enviando || carrito.estaVacio()) return false
        enviando = true
        // finally se ejecuta siempre, también si enviarCarrito fallara a mitad;
        // así la bandera nunca se queda encendida y Enviar sigue funcionando
        try {
            comandaRepository.enviarCarrito(carrito)
            _carrito.value = Carrito(mesaId)
        } finally {
            enviando = false
        }
        return true
    }

    // 5b: llena la bandeja con un plato. la libreta es una para muchas fichas: primero se vacía,
    // para que al abrir Helado no se vea Entrecot un instante. si ya tiene este plato (Android rehízo
    // la pantalla), no se vuelve a leer. viewModelScope empieza en el hilo principal; Room cambia de hilo solo
    fun cargarFicha(productoId: Long) {
        if (_ficha.value?.plato?.id == productoId) return
        _ficha.value = null
        viewModelScope.launch {
            val plato = cartaRepository.plato(productoId) ?: return@launch
            _ficha.value = DatosFicha(plato, cartaRepository.alergenosDe(productoId))
        }
    }

    // salir de Pedir (1c): es este el PIN guardado? el repositorio cambia de hilo
    suspend fun comprobarPin(pin: String): Boolean = seguridadRepository.comprobarPin(pin)

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as EntradaAko
                PedidoViewModel(app.cartaRepository, app.comandaRepository, app.seguridadRepository)
            }
        }
    }
}