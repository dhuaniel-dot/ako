package yunkang.ako.datos.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

// Tabla puente: "este plato tiene este alérgeno". Una fila por cada casilla marcada.
// El carné son las dos columnas juntas.
@Entity(
    tableName = "producto_alergeno",
    primaryKeys = ["producto_id", "alergeno_id"],
    foreignKeys = [
        ForeignKey(
            entity = Producto::class,
            parentColumns = ["id"],
            childColumns = ["producto_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Alergeno::class,
            parentColumns = ["id"],
            childColumns = ["alergeno_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index(value = ["alergeno_id"])]
)
data class ProductoAlergeno(
    @ColumnInfo(name = "producto_id")
    val productoId: Long,

    @ColumnInfo(name = "alergeno_id")
    val alergenoId: Long
)