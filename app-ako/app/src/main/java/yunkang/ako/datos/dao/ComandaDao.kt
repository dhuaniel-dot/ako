package yunkang.ako.datos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import yunkang.ako.datos.entidades.Comanda
import yunkang.ako.datos.entidades.LineaComanda
import yunkang.ako.dominio.modelos.MesaConTotal

// Lo que se le puede pedir a Room sobre las comandas y sus líneas.
@Dao
interface ComandaDao {

    @Insert
    suspend fun insertar(comanda: Comanda): Long

    @Update
    suspend fun actualizar(comanda: Comanda)

    @Query("SELECT * FROM comanda WHERE id = :id")
    suspend fun porId(id: Long): Comanda?

    // La comanda PENDIENTE de una mesa (como mucho hay una); vacío si la mesa está libre.
    @Query("SELECT * FROM comanda WHERE mesa_id = :mesaId AND estado = 'PENDIENTE' LIMIT 1")
    suspend fun pendienteDeMesa(mesaId: Long): Comanda?

    // La rejilla: solo las mesas ocupadas (con comanda pendiente), con su total sumado.
    // Room la vuelve a mandar cada vez que cambian comanda, mesa o linea_comanda
    @Query("""
        SELECT m.id AS mesaId, c.id AS comandaId,
               SUM(l.cantidad * l.precio_unitario_centimos) AS totalCentimos
        FROM comanda c
        JOIN mesa m ON m.id = c.mesa_id
        JOIN linea_comanda l ON l.comanda_id = c.id
        WHERE c.estado = 'PENDIENTE'
        GROUP BY c.id
        ORDER BY m.numero
    """)
    fun pendientesConTotal(): Flow<List<MesaConTotal>>

    @Query("SELECT * FROM linea_comanda WHERE comanda_id = :comandaId ORDER BY id")
    suspend fun lineasDe(comandaId: Long): List<LineaComanda>

    @Insert
    suspend fun insertarLineas(lineas: List<LineaComanda>)

    @Query("DELETE FROM linea_comanda WHERE id = :lineaId")
    suspend fun borrarLinea(lineaId: Long)

    // Cuántas líneas le quedan a una comanda (si llega a 0, se anula).
    @Query("SELECT COUNT(*) FROM linea_comanda WHERE comanda_id = :comandaId")
    suspend fun contarLineas(comandaId: Long): Int

    // Resumen de ingresos: las comandas cobradas entre dos instantes.
    @Query("""
        SELECT * FROM comanda
        WHERE estado = 'PAGADA' AND fecha_cierre BETWEEN :inicio AND :fin
        ORDER BY fecha_cierre
    """)
    suspend fun pagadasEntre(inicio: Long, fin: Long): List<Comanda>

    // Para el aviso al eliminar un plato: números de las mesas con comanda PENDIENTE que llevan este plato.
    @Query("""
        SELECT DISTINCT m.numero FROM mesa m
        JOIN comanda c ON c.mesa_id = m.id
        JOIN linea_comanda l ON l.comanda_id = c.id
        WHERE c.estado = 'PENDIENTE' AND l.producto_id = :productoId
        ORDER BY m.numero
    """)
    suspend fun mesasConProductoPendiente(productoId: Long): List<Int>

    // Para el aviso al eliminar una categoría: números de las mesas con comanda PENDIENTE que llevan algún plato
    // de esta categoría que siga en la carta (solo los activos, lo que de verdad se elimina).
    @Query("""
        SELECT DISTINCT m.numero FROM mesa m
        JOIN comanda c ON c.mesa_id = m.id
        JOIN linea_comanda l ON l.comanda_id = c.id
        JOIN producto p ON p.id = l.producto_id
        WHERE c.estado = 'PENDIENTE' AND p.categoria_id = :categoriaId AND p.activo = 1
        ORDER BY m.numero
    """)
    suspend fun mesasConCategoriaPendiente(categoriaId: Long): List<Int>

    // El total de una comanda, sumando sus líneas; sin líneas, 0.
    @Query("SELECT COALESCE(SUM(cantidad * precio_unitario_centimos), 0) FROM linea_comanda WHERE comanda_id = :comandaId")
    suspend fun totalDe(comandaId: Long): Int

    // Una línea por su id (para saber de qué comanda es antes de quitarla); vacío si ya no existe.
    @Query("SELECT * FROM linea_comanda WHERE id = :lineaId")
    suspend fun lineaPorId(lineaId: Long): LineaComanda?
}