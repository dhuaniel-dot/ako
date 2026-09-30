package yunkang.ako.datos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import yunkang.ako.datos.entidades.Comanda
import yunkang.ako.datos.entidades.LineaComanda
import yunkang.ako.dominio.modelos.MesaConTotal

// Lo que se le puede pedir a Room sobre las comandas y sus líneas.
@Dao
interface ComandaDao {

    // Guarda una comanda nueva y devuelve el id que le ha dado Room.
    @Insert
    suspend fun insertar(comanda: Comanda): Long

    // Guarda los cambios de una comanda (estado, fecha de cierre).
    @Update
    suspend fun actualizar(comanda: Comanda)

    // Una comanda por su id (vacío si no existe).
    @Query("SELECT * FROM comanda WHERE id = :id")
    suspend fun porId(id: Long): Comanda?

    // R1: la comanda PENDIENTE de una mesa (como mucho hay una); vacío si la mesa está libre.
    @Query("SELECT * FROM comanda WHERE mesa_id = :mesaId AND estado = 'PENDIENTE' LIMIT 1")
    suspend fun pendienteDeMesa(mesaId: Long): Comanda?

    // La rejilla: solo las mesas ocupadas, con su total sumado (R3, R10, P123).
    @Query("""
        SELECT m.id AS mesaId, m.numero AS numero, c.id AS comandaId,
               SUM(l.cantidad * l.precio_unitario_centimos) AS totalCentimos
        FROM comanda c
        JOIN mesa m ON m.id = c.mesa_id
        JOIN linea_comanda l ON l.comanda_id = c.id
        WHERE c.estado = 'PENDIENTE'
        GROUP BY c.id
        ORDER BY m.numero
    """)
    suspend fun pendientesConTotal(): List<MesaConTotal>

    // Las líneas de una comanda, en el orden en que se pidieron.
    @Query("SELECT * FROM linea_comanda WHERE comanda_id = :comandaId ORDER BY id")
    suspend fun lineasDe(comandaId: Long): List<LineaComanda>

    // Guarda las líneas de un envío de golpe.
    @Insert
    suspend fun insertarLineas(lineas: List<LineaComanda>)

    // Quita una línea de una comanda abierta.
    @Query("DELETE FROM linea_comanda WHERE id = :lineaId")
    suspend fun borrarLinea(lineaId: Long)

    // Cuántas líneas le quedan a una comanda (R7: si llega a 0, se anula).
    @Query("SELECT COUNT(*) FROM linea_comanda WHERE comanda_id = :comandaId")
    suspend fun contarLineas(comandaId: Long): Int

    // Resumen de ingresos: las comandas cobradas entre dos instantes (P126).
    @Query("""
        SELECT * FROM comanda
        WHERE estado = 'PAGADA' AND fecha_cierre BETWEEN :inicio AND :fin
        ORDER BY fecha_cierre
    """)
    suspend fun pagadasEntre(inicio: Long, fin: Long): List<Comanda>

    // R6: números de las mesas con comanda PENDIENTE que llevan este plato.
    @Query("""
        SELECT DISTINCT m.numero FROM mesa m
        JOIN comanda c ON c.mesa_id = m.id
        JOIN linea_comanda l ON l.comanda_id = c.id
        WHERE c.estado = 'PENDIENTE' AND l.producto_id = :productoId
        ORDER BY m.numero
    """)
    suspend fun mesasConProductoPendiente(productoId: Long): List<Int>

    // R6: números de las mesas con comanda PENDIENTE que llevan algún plato de esta categoría.
    @Query("""
        SELECT DISTINCT m.numero FROM mesa m
        JOIN comanda c ON c.mesa_id = m.id
        JOIN linea_comanda l ON l.comanda_id = c.id
        JOIN producto p ON p.id = l.producto_id
        WHERE c.estado = 'PENDIENTE' AND p.categoria_id = :categoriaId
        ORDER BY m.numero
    """)
    suspend fun mesasConCategoriaPendiente(categoriaId: Long): List<Int>
}
