package yunkang.ako.datos.repositorios

import yunkang.ako.datos.dao.CategoriaDao
import yunkang.ako.datos.dao.PrecargadosDao
import yunkang.ako.datos.dao.ProductoDao
import yunkang.ako.datos.entidades.Alergeno
import yunkang.ako.datos.entidades.Categoria
import yunkang.ako.datos.entidades.Producto
import yunkang.ako.datos.entidades.ProductoAlergeno
import kotlinx.coroutines.flow.Flow

// [Claude] actores que hacen de DAO en las pruebas: no tocan ninguna base de datos
// solo contestan lo que la prueba necesita; lo demás es todo() ("esto no lo ensayamos")

// contesta que la categoría existe; si está en la carta o eliminada lo decide cada prueba (activa)
// solo hay una categoría, Carnes, de orden 2, y ningún nombre está repetido
// apunta la categoría que le llega al insertar (insertada) y al actualizar (actualizada)
class CategoriaDaoFalso(var activa: Boolean = true) : CategoriaDao {
    var insertada: Categoria? = null
    var actualizada: Categoria? = null
    override suspend fun porId(id: Long): Categoria? =
        Categoria(id = id, nombre = "Carnes", imagen = null, orden = 2, activo = activa, esPorDefecto = false)
    override suspend fun insertar(categoria: Categoria): Long {
        insertada = categoria
        return 9
    }
    override suspend fun actualizar(categoria: Categoria) {
        actualizada = categoria
    }
    override suspend fun todas(): List<Categoria> =
        listOf(Categoria(id = 2, nombre = "Carnes", imagen = null, orden = 2, activo = activa, esPorDefecto = false))
    override fun todasObservadas(): Flow<List<Categoria>> = TODO()
    override suspend fun porDefecto(): Categoria = TODO()
    override suspend fun existeNombre(nombre: String, exceptoId: Long): Boolean = false
}

// apunta qué plato y qué marcas de alérgeno le llegan
// "guardado" es el plato que la prueba dice que ya existe (para editar); vacío si no hay ninguno
// "numeroOcupado": si la prueba dice true, otro plato ya tiene ese número; si no, está libre
// borrarAlergenosDe e insertarAlergenosDe no llevan todo(): las usa guardarAlergenos
class ProductoDaoFalso(var guardado: Producto? = null, var numeroOcupado: Boolean = false) : ProductoDao {
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
    override suspend fun existeNumero(numero: Int, exceptoId: Long): Boolean = numeroOcupado
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

// no se usa en estas pruebas; existe porque CartaRepository lo pide al crearse
class PrecargadosDaoFalso : PrecargadosDao {
    override suspend fun alergenos(): List<Alergeno> = TODO()
}