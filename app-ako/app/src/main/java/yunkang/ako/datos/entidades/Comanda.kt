package yunkang.ako.datos.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// El pedido de una mesa, de principio a fin. Tabla "comanda".
// No guarda el total: se calcula sumando sus líneas (R10).
// [Claude] El índice de mesa_id: Room lo pide para las claves foráneas.
// fechaCreacion: cuándo se envió el primer pedido (instante en milisegundos).
// fechaCierre: cuándo se cobró o se anuló; vacío mientras está PENDIENTE.
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
    indices = [Index(value = ["mesa_id"])]
)
data class Comanda(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "mesa_id")
    val mesaId: Long,

    val estado: EstadoComanda,

    @ColumnInfo(name = "fecha_creacion")
    val fechaCreacion: Long,

    @ColumnInfo(name = "fecha_cierre")
    val fechaCierre: Long?
)