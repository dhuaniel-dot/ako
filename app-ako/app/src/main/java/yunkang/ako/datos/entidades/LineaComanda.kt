package yunkang.ako.datos.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// una línea de la comanda: "2 × Agua a 1,50 €". tabla "linea_comanda"
// copia el nombre y el precio del plato en el momento de pedir
@Entity(
    tableName = "linea_comanda",
    foreignKeys = [
        ForeignKey(
            entity = Comanda::class,
            parentColumns = ["id"],
            childColumns = ["comanda_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Producto::class,
            parentColumns = ["id"],
            childColumns = ["producto_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["comanda_id"]),
        Index(value = ["producto_id"])
    ]
)
data class LineaComanda(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "comanda_id")
    val comandaId: Long,

    @ColumnInfo(name = "producto_id")
    val productoId: Long,

    val cantidad: Int,

    @ColumnInfo(name = "precio_unitario_centimos")
    val precioUnitarioCentimos: Int,

    @ColumnInfo(name = "nombre_producto")
    val nombreProducto: String
)