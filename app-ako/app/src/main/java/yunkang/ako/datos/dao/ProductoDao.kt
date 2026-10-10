package yunkang.ako.datos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import yunkang.ako.datos.entidades.Alergeno
import yunkang.ako.datos.entidades.Producto
import yunkang.ako.datos.entidades.ProductoAlergeno

@Dao
interface ProductoDao {

    @Insert
    suspend fun insertar(producto: Producto): Long

    @Update
    suspend fun actualizar(producto: Producto)

    @Query("SELECT * FROM producto WHERE id = :id")
    suspend fun porId(id: Long): Producto?

    // Panel: todos los platos de una categoría, también los eliminados, por número.
    @Query("SELECT * FROM producto WHERE categoria_id = :categoriaId ORDER BY numero")
    suspend fun porCategoria(categoriaId: Long): List<Producto>

    // Todos los platos existentes (también los eliminados: el Panel los ve todos), por número.
    @Query("SELECT * FROM producto ORDER BY numero")
    fun todosObservados(): Flow<List<Producto>>

    // La carta: platos visibles = activos y con su categoría activa.
    @Query("""
        SELECT p.* FROM producto p
        JOIN categoria c ON c.id = p.categoria_id
        WHERE p.activo = 1 AND c.activo = 1
        ORDER BY c.orden, p.numero
    """)
    fun visibles(): Flow<List<Producto>>

    // ¿Hay al menos un plato visible? (puerta de Pedir)
    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM producto p
            JOIN categoria c ON c.id = p.categoria_id
            WHERE p.activo = 1 AND c.activo = 1
        )
    """)
    suspend fun hayAlgunoVisible(): Boolean

    // ¿Hay al menos un plato existente, visible o no? (puerta de Pedir: elige el mensaje)
    @Query("SELECT EXISTS(SELECT 1 FROM producto)")
    suspend fun hayAlguno(): Boolean

    // ¿Otro plato tiene ya ese número? (avisa antes; la base de datos lo impide igualmente)
    @Query("SELECT EXISTS(SELECT 1 FROM producto WHERE numero = :numero AND id != :exceptoId)")
    suspend fun existeNumero(numero: Int, exceptoId: Long): Boolean

    @Query("""
        SELECT a.* FROM alergeno a
        JOIN producto_alergeno pa ON pa.alergeno_id = a.id
        WHERE pa.producto_id = :productoId
        ORDER BY a.id
    """)
    suspend fun alergenosDe(productoId: Long): List<Alergeno>

    // Quita todas las marcas de alérgeno de un plato (solo las usa guardarAlergenos).
    @Query("DELETE FROM producto_alergeno WHERE producto_id = :productoId")
    suspend fun borrarAlergenosDe(productoId: Long)

    // Pone las marcas nuevas (solo las usa guardarAlergenos).
    @Insert
    suspend fun insertarAlergenosDe(marcas: List<ProductoAlergeno>)

    // Cambia los alérgenos de un plato: borrar las marcas viejas y poner las nuevas,
    // las dos cosas o ninguna.
    @Transaction
    suspend fun guardarAlergenos(productoId: Long, alergenoIds: List<Long>) {
        borrarAlergenosDe(productoId)
        insertarAlergenosDe(alergenoIds.map { ProductoAlergeno(productoId, it) })
    }

    // Guarda un plato (nuevo o editado) y sus alérgenos, las dos cosas o ninguna; devuelve su id.
    @Transaction
    suspend fun guardarConAlergenos(producto: Producto, alergenoIds: List<Long>): Long {
        val productoId = if (producto.id == 0L) {
            insertar(producto)
        } else {
            actualizar(producto)
            producto.id
        }
        guardarAlergenos(productoId, alergenoIds)
        return productoId
    }
}