package yunkang.ako.ui.plato

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import yunkang.ako.EntradaAko
import yunkang.ako.datos.entidades.Alergeno
import yunkang.ako.datos.entidades.Categoria
import yunkang.ako.datos.entidades.Producto
import yunkang.ako.datos.repositorios.CartaRepository
import yunkang.ako.dominio.modelos.ResultadoGuardado
import yunkang.ako.imagenes.Galeria

// [Claude] todo lo que el formulario lee al abrirse, junto en una sola bandeja
data class DatosFormulario(
    // null = plato nuevo
    val plato: Producto?,
    // para el desplegable; la de por defecto, la última
    val categorias: List<Categoria>,
    // los 14, en el orden de la precarga
    val alergenos: List<Alergeno>,
    // los ids que el plato ya lleva marcados (vacía si es nuevo)
    val alergenosDelPlato: List<Long>
)

// la libreta de la pantalla 3 (formulario del plato)
// lo que el Propietario va eligiendo se guarda en una caja fuerte (SavedStateHandle) que
// sobrevive incluso a que Android mate la app en segundo plano; la libreta sola solo sobrevive a recrear la pantalla
class PlatoViewModel(
    private val cartaRepository: CartaRepository,
    private val galeria: Galeria,
    private val cajaFuerte: SavedStateHandle
) : ViewModel() {

    // un solo tablón. solo esta libreta escribe en él (_datos); la pantalla solo lo lee (datos)
    private val _datos = MutableLiveData<DatosFormulario>()
    val datos: LiveData<DatosFormulario> = _datos

    // [Claude] para no leer dos veces: la libreta sobrevive a que Android recree la pantalla
    // (si Android mató la app, la libreta es nueva y vuelve a leer: es lo que toca)
    private var yaCargado = false

    // [Claude] la categoría elegida, por su id (en la caja fuerte)
    var categoriaElegidaId: Long?
        get() = cajaFuerte[CLAVE_CATEGORIA]
        set(valor) { cajaFuerte[CLAVE_CATEGORIA] = valor }

    // [Claude] true cuando los campos ya tienen lo guardado; desde ahí manda lo que se teclea
    // en la caja fuerte: tras una muerte del proceso, Android devuelve los campos y no se vuelven a rellenar
    var formularioRelleno: Boolean
        get() = cajaFuerte[CLAVE_RELLENO] ?: false
        set(valor) { cajaFuerte[CLAVE_RELLENO] = valor }

    // true en cuanto el Propietario toca algo después de rellenar el formulario
    var hayCambios: Boolean
        get() = cajaFuerte[CLAVE_CAMBIOS] ?: false
        set(valor) { cajaFuerte[CLAVE_CAMBIOS] = valor }

    // los alérgenos marcados (sus ids) viven en la libreta, no en las casillas:
    // las casillas se crean de nuevo si Android recrea la pantalla. en la caja fuerte van como lista de números
    val alergenosMarcados: List<Long>
        get() = (cajaFuerte.get<LongArray>(CLAVE_ALERGENOS) ?: LongArray(0)).toList()

    // la foto elegida y aún sin guardar (su dirección, «content://…»), en la caja fuerte
    // getLiveData la convierte en un tablón que la pantalla puede mirar. null = no se ha elegido ninguna
    val fotoElegida: LiveData<String?> = cajaFuerte.getLiveData<String?>(CLAVE_FOTO, null)

    // se trabaja con un conjunto (sin repetidos) y se vuelve a guardar entero
    fun marcarAlergeno(id: Long, marcado: Boolean) {
        val ahora = alergenosMarcados.toMutableSet()
        if (marcado) ahora.add(id) else ahora.remove(id)
        cajaFuerte[CLAVE_ALERGENOS] = ahora.toLongArray()
    }

    // al elegir no se copia nada; se pide el préstamo largo y se apunta la dirección
    fun elegirFoto(uri: Uri) {
        galeria.conservarPrestamo(uri)
        cajaFuerte[CLAVE_FOTO] = uri.toString()
    }

    // [Claude] se olvida la foto elegida (cuando no se ha podido usar): el hueco vuelve a la de antes
    fun olvidarFoto() {
        cajaFuerte[CLAVE_FOTO] = null
    }

    // lee la base de datos una sola vez. productoId null = plato nuevo
    // cada llamada de Room se va sola a otro hilo y vuelve con el resultado
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

    // guarda el plato con sus alérgenos. las reglas las comprueba el repositorio; la pantalla solo reacciona a la respuesta
    // aunqueCategoriaEliminada = true es la tercera salida de 3e: el Propietario dijo «No» a las dos preguntas
    // si se eligió una foto, se copia ahora (como en 2b)
    suspend fun guardar(p: Producto, aunqueCategoriaEliminada: Boolean = false): ResultadoGuardado {
        val fotoAntes = datos.value?.plato?.imagen
        val elegida = fotoElegida.value
        val fotoNueva = if (elegida == null) null else galeria.guardar(elegida.toUri())
        val plato = if (fotoNueva == null) p else p.copy(imagen = fotoNueva)

        val resultado = cartaRepository.guardarPlato(plato, alergenosMarcados, aunqueCategoriaEliminada = aunqueCategoriaEliminada)

        if (fotoNueva != null) {
            if (resultado == ResultadoGuardado.Ok) {
                // guardado bien: la foto vieja ya no la usa ninguna fila (se borra después, nunca antes)
                if (fotoAntes != null) galeria.borrar(fotoAntes)
            } else {
                // [Claude] no se guardó (número repetido, avisos 3e): la copia sobra; al volver a guardar se hace otra
                galeria.borrar(fotoNueva)
            }
        }
        return resultado
    }

    // [Claude] 3e, segundo aviso: cuántos platos volverían a la carta al recuperar la categoría
    // el reverso del aviso de las mesas al eliminar: los activos de esa categoría, sin contar el que se está guardando
    suspend fun platosQueVuelven(categoriaId: Long): Int {
        val idDelPlato = datos.value?.plato?.id
        return cartaRepository.platosDe(categoriaId).count { it.activo && it.id != idDelPlato }
    }

    // [Claude] la categoría vuelve con todos sus platos
    suspend fun recuperarCategoria(id: Long) = cartaRepository.recuperarCategoria(id)

    // primer paso al eliminar: qué mesas tienen este plato en una comanda pendiente, para avisar. solo mira, no toca nada
    // un plato nuevo no está en ninguna mesa
    suspend fun mesasAfectadas(): List<Int> {
        val plato = datos.value?.plato ?: return emptyList()
        return cartaRepository.mesasAfectadasPorPlato(plato.id)
    }

    // la foto no se toca: el plato se puede recuperar entero
    suspend fun eliminar() {
        val plato = datos.value?.plato ?: return
        cartaRepository.eliminarPlato(plato.id)
    }

    // [Claude] recuperar = vuelve a la carta con todo lo que tenía
    suspend fun recuperar() {
        val plato = datos.value?.plato ?: return
        cartaRepository.recuperarPlato(plato.id)
    }

    // createSavedStateHandle: la caja fuerte la saca de la propia pantalla
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as EntradaAko
                PlatoViewModel(app.cartaRepository, app.galeria, createSavedStateHandle())
            }
        }

        // [Claude] las etiquetas de cada cosa dentro de la caja fuerte
        private const val CLAVE_CATEGORIA = "categoria_elegida"
        private const val CLAVE_RELLENO = "formulario_relleno"
        private const val CLAVE_CAMBIOS = "hay_cambios"
        private const val CLAVE_ALERGENOS = "alergenos_marcados"
        private const val CLAVE_FOTO = "foto_elegida"
    }
}