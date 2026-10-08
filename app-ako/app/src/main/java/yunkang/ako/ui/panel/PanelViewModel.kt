package yunkang.ako.ui.panel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import yunkang.ako.EntradaAko
import yunkang.ako.datos.entidades.Categoria
import yunkang.ako.datos.repositorios.CartaRepository
import yunkang.ako.datos.repositorios.SeguridadRepository
import yunkang.ako.dominio.modelos.CategoriaConPlatos
import yunkang.ako.dominio.modelos.PlatoConMesas
import yunkang.ako.dominio.modelos.ResultadoGuardado
import yunkang.ako.imagenes.ImageStore

// La libreta del Panel (pantalla 2): el PIN (1e) y, desde la S6, la carta en modo edición (2a)
class PanelViewModel(
    private val seguridadRepository: SeguridadRepository,
    private val cartaRepository: CartaRepository,
    private val imageStore: ImageStore
) : ViewModel() {

    // 2a (P49 B): el tablón con las cajas. Lo escribe Room a través del repositorio (el grifo, Flow);
    // asLiveData lo cuelga en el tablón y cierra el grifo solo cuando nadie mira la pantalla.
    // Nadie lo escribe a mano: por eso no hay MutableLiveData ni cargar()
    val categoriasConPlatos: LiveData<List<CategoriaConPlatos>> =
        cartaRepository.categoriasConPlatos().asLiveData()

    // P56 A: qué cajas están plegadas. Vive en la libreta, no en la bandeja: las bandejas se reciclan
    private val plegadas = mutableSetOf<Long>()

    fun estaPlegada(categoriaId: Long): Boolean = categoriaId in plegadas

    fun alternarPlegado(categoriaId: Long) {
        if (categoriaId in plegadas) plegadas.remove(categoriaId) else plegadas.add(categoriaId)
    }

    // 2b (P94 C, como el plato): al elegir la foto no se copia nada; se pide el préstamo largo
    fun conservarPrestamo(uri: Uri) = imageStore.conservarPrestamo(uri)

    // 2b: crea o renombra una categoría, con su foto si se eligió una (P13). La lista se pone al día sola (P49 B).
    // La foto elegida se copia AHORA; la vieja se borra solo si se guardó bien, y la copia nueva si no
    // (nombre repetido). Si la foto no se puede leer, ImageStore lanza el error y la hoja avisa
    suspend fun guardarCategoria(c: Categoria, fotoElegida: Uri?): ResultadoGuardado {
        val fotoAntes = c.imagen
        val fotoNueva = if (fotoElegida == null) null else imageStore.guardar(fotoElegida)
        val categoria = if (fotoNueva == null) c else c.copy(imagen = fotoNueva)

        val resultado = cartaRepository.guardarCategoria(categoria)

        if (fotoNueva != null) {
            if (resultado == ResultadoGuardado.Ok) {
                if (fotoAntes != null) imageStore.borrar(fotoAntes)
            } else {
                imageStore.borrar(fotoNueva)
            }
        }
        return resultado
    }

    // 2e: qué platos de la categoría están en mesas pendientes, para avisar antes de eliminarla
    suspend fun platosAfectados(categoriaId: Long): List<PlatoConMesas> =
        cartaRepository.platosAfectadosPorCategoria(categoriaId)

    // 2b: eliminar (activo = false) o recuperar una categoría. Las mesas ya se miraron antes (P128)
    suspend fun eliminarCategoria(id: Long) = cartaRepository.eliminarCategoria(id)

    suspend fun recuperarCategoria(id: Long) = cartaRepository.recuperarCategoria(id)

    // 1e (P46 B): primero se comprueba el PIN actual, antes de mirar los nuevos
    suspend fun comprobarPin(pin: String): Boolean = seguridadRepository.comprobarPin(pin)

    // 1e: guarda el PIN nuevo. El repositorio vuelve a mirar el actual (D18) y cambia de hilo (P134)
    suspend fun cambiarPin(actual: String, nuevo: String): Boolean =
        seguridadRepository.cambiarPin(actual, nuevo)

    // P140: la fábrica que construye esta libreta con los dos repositorios y el almacén de fotos de EntradaAko
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as EntradaAko
                PanelViewModel(app.seguridadRepository, app.cartaRepository, app.imageStore)
            }
        }
    }
}