package yunkang.ako.datos.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// un plato de la carta. tabla "producto"
// no se puede borrar una categoría con platos
// no puede haber dos platos con el mismo número
// [Claude] el índice de categoria_id: Room lo pide para las claves foráneas
// numero: el número que ve el cliente en la carta
// precioCentimos: 1,50 € se guarda como 150
// activo: false = eliminado (no se borra nunca)
@Entity(
    tableName = "producto",
    foreignKeys = [
        ForeignKey(
            entity = Categoria::class,
            parentColumns = ["id"],
            childColumns = ["categoria_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["numero"], unique = true),
        Index(value = ["categoria_id"])
    ]
)
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "categoria_id")
    val categoriaId: Long,

    val numero: Int,
    val nombre: String,
    val descripcion: String?,

    @ColumnInfo(name = "precio_centimos")
    val precioCentimos: Int,

    val imagen: String?,
    val activo: Boolean
)