package yunkang.ako.datos.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// El pedido de una mesa, de principio a fin. Tabla "comanda".
// No guarda el total: se calcula sumando sus líneas (R10).
@Entity(
    tableName = "comanda",
    foreignKeys = [
        ForeignKey(
            entity = Mesa::class,
            parentColumns = ["id"],
            childColumns = ["mesa_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index(value = ["mesa_id"])]     // [Claude] Room lo pide para las claves foráneas
)
data class Comanda(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "mesa_id")
    val mesaId: Long,

    val estado: EstadoComanda,     // PENDIENTE, PAGADA o ANULADA (Room lo guarda como texto, P113)

    @ColumnInfo(name = "fecha_creacion")
    val fechaCreacion: Long,       // instante en milisegundos: cuándo se envió el primer pedido

    @ColumnInfo(name = "fecha_cierre")
    val fechaCierre: Long?         // cuándo se cobró o se anuló; vacío mientras está PENDIENTE
)