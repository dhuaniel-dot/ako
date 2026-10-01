package yunkang.ako.datos.repositorios

import yunkang.ako.datos.dao.CategoriaDao
import yunkang.ako.datos.dao.PrecargadosDao
import yunkang.ako.datos.dao.ProductoDao
import yunkang.ako.datos.entidades.Alergeno
import yunkang.ako.datos.entidades.Categoria
import yunkang.ako.datos.entidades.Producto
import yunkang.ako.datos.entidades.ProductoAlergeno

// [Claude] Actores que hacen de DAO en las pruebas: no tocan ninguna base de datos.
// Solo contestan lo que la prueba necesita; lo demás es TODO() ("esto no lo ensayamos").

// Contesta siempre que la categoría existe y está activa.
class CategoriaDaoFalso : CategoriaDao {
    override suspend fun porId(id: Long): Categoria =
        Categoria(id = id, nombre = "Carnes", imagen = null, orden = 2, activo = true, esPorDefecto = false)
    override suspend fun insertar(categoria: Categoria): Long = TODO()
    override suspend fun actualizar(categoria: Categoria): Unit = TODO()
    override suspend fun todas(): List<Categoria> = TODO()
    override suspend fun porDefecto(): Categoria = TODO()
    override suspend fun existeNombre(nombre: String, exceptoId: Long): Boolean = TODO()
}

// Apunta si alguien intenta guardar un plato nuevo; el número siempre está libre.
class ProductoDaoFalso : ProductoDao {
    var seLlamoInsertar = false
    override suspend fun insertar(producto: Producto): Long {
        seLlamoInsertar = true
        return 1
    }
    override suspend fun existeNumero(numero: Int, exceptoId: Long): Boolean = false
    override suspend fun borrarAlergenosDe(productoId: Long) {}                  // las usa guardarAlergenos
    override suspend fun insertarAlergenosDe(marcas: List<ProductoAlergeno>) {}  // ídem
    override suspend fun actualizar(producto: Producto): Unit = TODO()
    override suspend fun porId(id: Long): Producto? = TODO()
    override suspend fun porCategoria(categoriaId: Long): List<Producto> = TODO()
    override suspend fun visibles(): List<Producto> = TODO()
    override suspend fun hayAlgunoVisible(): Boolean = TODO()
    override suspend fun alergenosDe(productoId: Long): List<Alergeno> = TODO()
}

// No se usa en estas pruebas; existe porque CartaRepository lo pide al crearse.
class PrecargadosDaoFalso : PrecargadosDao {
    override suspend fun insertarAlergenos(alergenos: List<Alergeno>): Unit = TODO()
    override suspend fun alergenos(): List<Alergeno> = TODO()
}