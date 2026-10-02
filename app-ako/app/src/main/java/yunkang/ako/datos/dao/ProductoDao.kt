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

// Lo que se le puede pedir a Room sobre los platos.
@Dao
interface ProductoDao {

    // Guarda un plato nuevo y devuelve el id que le ha dado Room.
    @Insert
    suspend fun insertar(producto: Producto): Long

    // Guarda los cambios de un plato que ya existe.
    @Update
    suspend fun actualizar(producto: Producto)

    // Un plato por su id (vacío si no existe).
    @Query("SELECT * FROM producto WHERE id = :id")
    suspend fun porId(id: Long): Producto?

    // Panel: todos los platos de una categoría, también los eliminados, por número.
    @Query("SELECT * FROM producto WHERE categoria_id = :categoriaId ORDER BY numero")
    suspend fun porCategoria(categoriaId: Long): List<Producto>

    // P49 B: todos los platos existentes (también los eliminados: el Panel los ve todos, R15), por número.
    // Room vuelve a mandar la lista cada vez que cambia la tabla producto
    @Query("SELECT * FROM producto ORDER BY numero")
    fun todosObservados(): Flow<List<Producto>>

    // La carta: platos visibles = activos y con su categoría activa (R15).
    @Query("""
        SELECT p.* FROM producto p
        JOIN categoria c ON c.id = p.categoria_id
        WHERE p.activo = 1 AND c.activo = 1
        ORDER BY c.orden, p.numero
    """)
    suspend fun visibles(): List<Producto>

    // ¿Hay al menos un plato visible? (puerta de Pedir)
    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM producto p
            JOIN categoria c ON c.id = p.categoria_id
            WHERE p.activo = 1 AND c.activo = 1
        )
    """)
    suspend fun hayAlgunoVisible(): Boolean

    // ¿Otro plato tiene ya ese número? (R9: avisa antes; la base de datos lo impide igualmente)
    @Query("SELECT EXISTS(SELECT 1 FROM producto WHERE numero = :numero AND id != :exceptoId)")
    suspend fun existeNumero(numero: Int, exceptoId: Long): Boolean

    // Los alérgenos marcados de un plato.
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
    // las dos cosas o ninguna (P125).
    @Transaction
    suspend fun guardarAlergenos(productoId: Long, alergenoIds: List<Long>) {
        borrarAlergenosDe(productoId)
        insertarAlergenosDe(alergenoIds.map { ProductoAlergeno(productoId, it) })
    }

    // Guarda un plato (nuevo o editado) y sus alérgenos, las dos cosas o ninguna (P137); devuelve su id.
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