package yunkang.ako.datos.repositorios

import yunkang.ako.datos.dao.CategoriaDao
import yunkang.ako.datos.dao.PrecargadosDao
import yunkang.ako.datos.dao.ProductoDao
import yunkang.ako.datos.entidades.Alergeno
import yunkang.ako.datos.entidades.Categoria
import yunkang.ako.datos.entidades.Producto
import yunkang.ako.datos.entidades.ProductoAlergeno
import kotlinx.coroutines.flow.Flow

// [Claude] Actores que hacen de DAO en las pruebas: no tocan ninguna base de datos.
// Solo contestan lo que la prueba necesita; lo demás es TODO() ("esto no lo ensayamos").

// Contesta que la categoría existe; si está en la carta o eliminada lo decide cada prueba (activa).
class CategoriaDaoFalso(var activa: Boolean = true) : CategoriaDao {
    override suspend fun porId(id: Long): Categoria? =
        Categoria(id = id, nombre = "Carnes", imagen = null, orden = 2, activo = activa, esPorDefecto = false)
    override suspend fun insertar(categoria: Categoria): Long = TODO()
    override suspend fun actualizar(categoria: Categoria): Unit = TODO()
    override suspend fun todas(): List<Categoria> = TODO()
    override fun todasObservadas(): Flow<List<Categoria>> = TODO()
    override suspend fun porDefecto(): Categoria = TODO()
    override suspend fun existeNombre(nombre: String, exceptoId: Long): Boolean = TODO()
}

// Apunta qué plato y qué marcas de alérgeno le llegan; el número siempre está libre.
// "guardado" es el plato que la prueba dice que ya existe (para editar); vacío si no hay ninguno.
// borrarAlergenosDe e insertarAlergenosDe no llevan TODO(): las usa guardarAlergenos.
class ProductoDaoFalso(var guardado: Producto? = null) : ProductoDao {
    var seLlamoInsertar = false
    var productoRecibido: Producto? = null
    var marcasRecibidas: List<ProductoAlergeno>? = null
    override suspend fun insertar(producto: Producto): Long {
        seLlamoInsertar = true
        productoRecibido = producto
        return 1
    }
    override suspend fun actualizar(producto: Producto) {
        productoRecibido = producto
    }
    override suspend fun porId(id: Long): Producto? = guardado?.takeIf { it.id == id }
    override suspend fun existeNumero(numero: Int, exceptoId: Long): Boolean = false
    override suspend fun borrarAlergenosDe(productoId: Long) {}
    override suspend fun insertarAlergenosDe(marcas: List<ProductoAlergeno>) {
        marcasRecibidas = marcas
    }
    override suspend fun porCategoria(categoriaId: Long): List<Producto> = TODO()
    override fun todosObservados(): Flow<List<Producto>> = TODO()
    override fun visibles(): Flow<List<Producto>> = TODO()
    override suspend fun hayAlgunoVisible(): Boolean = TODO()
    override suspend fun hayAlguno(): Boolean = TODO()
    override suspend fun alergenosDe(productoId: Long): List<Alergeno> = TODO()
}

// No se usa en estas pruebas; existe porque CartaRepository lo pide al crearse.
class PrecargadosDaoFalso : PrecargadosDao {
    override suspend fun alergenos(): List<Alergeno> = TODO()
}