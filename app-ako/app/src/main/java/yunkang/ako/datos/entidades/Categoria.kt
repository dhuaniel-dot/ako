package yunkang.ako.datos.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Una categoría de la carta (Bebidas, Carnes, Otros…). Tabla "categoria".
// No puede haber dos categorías con el mismo nombre.
// id: el carné; lo pone Room solo al guardar.
// imagen: ruta de la foto; vacía si no tiene.
// orden: posición en la carta.
// activo: false = eliminada (no se borra nunca).
// esPorDefecto: true solo en "Otros" (R16).
@Entity(
    tableName = "categoria",
    indices = [Index(value = ["nombre"], unique = true)]
)
data class Categoria(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nombre: String,
    val imagen: String?,
    val orden: Int,
    val activo: Boolean,

    @ColumnInfo(name = "es_por_defecto")
    val esPorDefecto: Boolean
)