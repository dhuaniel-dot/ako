package yunkang.ako.datos.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// Un plato de la carta. Tabla "producto".
@Entity(
    tableName = "producto",
    foreignKeys = [
        ForeignKey(
            entity = Categoria::class,          // apunta a una categoría…
            parentColumns = ["id"],             // …por su carné…
            childColumns = ["categoria_id"],    // …guardado en esta columna
            onDelete = ForeignKey.RESTRICT      // no se puede borrar una categoría con platos (R11)
        )
    ],
    indices = [
        Index(value = ["numero"], unique = true),  // no puede haber dos platos con el mismo número
        Index(value = ["categoria_id"])            // [Claude] Room lo pide para las claves foráneas
    ]
)
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "categoria_id")
    val categoriaId: Long,

    val numero: Int,               // el número que ve el cliente en la carta
    val nombre: String,
    val descripcion: String?,      // opcional

    @ColumnInfo(name = "precio_centimos")
    val precioCentimos: Int,       // 1,50 € se guarda como 150

    val imagen: String?,           // ruta de la foto (S12)
    val activo: Boolean            // false = eliminado (no se borra nunca)
)