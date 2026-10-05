package yunkang.ako.ui.plato

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import yunkang.ako.EntradaAko
import yunkang.ako.datos.entidades.Alergeno
import yunkang.ako.datos.entidades.Categoria
import yunkang.ako.datos.entidades.Producto
import yunkang.ako.datos.repositorios.CartaRepository

// [Claude] P162 B: todo lo que el formulario lee al abrirse, junto en una sola bandeja
data class DatosFormulario(
    val plato: Producto?,                 // null = plato nuevo
    val categorias: List<Categoria>,      // para el desplegable; la de por defecto, la última (R16)
    val alergenos: List<Alergeno>,        // los 14, en el orden de la precarga
    val alergenosDelPlato: List<Long>     // los ids que el plato ya lleva marcados (vacía si es nuevo)
)

// La libreta de la pantalla 3 (formulario del plato)
class PlatoViewModel(private val cartaRepository: CartaRepository) : ViewModel() {

    // P162 B: un solo tablón. Solo esta libreta escribe en él (_datos); la pantalla solo lo lee (datos)
    private val _datos = MutableLiveData<DatosFormulario>()
    val datos: LiveData<DatosFormulario> = _datos

    // [Claude] Para no leer dos veces: la libreta sobrevive a que Android recree la pantalla
    private var yaCargado = false

    // [Claude] Lo que el Propietario va eligiendo vive en la libreta para sobrevivir a una recreación
    // (como las casillas de alérgenos, P66 A): la categoría elegida, por su id
    var categoriaElegidaId: Long? = null

    // [Claude] true cuando los campos ya tienen lo guardado; desde ahí manda lo que se teclea
    var formularioRelleno = false

    // P66 A: los alérgenos marcados (sus ids) viven en la libreta, no en las casillas:
    // las casillas se crean de nuevo si Android recrea la pantalla
    val alergenosMarcados = mutableSetOf<Long>()

    // Lee la base de datos UNA sola vez. productoId null = plato nuevo.
    // viewModelScope.launch empieza en el hilo principal; cada llamada de Room
    // se va ella sola a otro hilo y vuelve con el resultado
    fun cargar(productoId: Long?) {
        if (yaCargado) return
        yaCargado = true
        viewModelScope.launch {
            val plato = if (productoId == null) null else cartaRepository.plato(productoId)
            val marcados = if (productoId == null) emptyList() else cartaRepository.alergenosDe(productoId).map { it.id }
            _datos.value = DatosFormulario(
                plato = plato,
                categorias = cartaRepository.categorias(),
                alergenos = cartaRepository.alergenos(),
                alergenosDelPlato = marcados
            )
        }
    }

    // P140: la fábrica que construye esta libreta con el repositorio de la carta de EntradaAko
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as EntradaAko
                PlatoViewModel(app.cartaRepository)
            }
        }
    }
}