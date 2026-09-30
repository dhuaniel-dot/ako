package yunkang.ako.datos.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Una categoría de la carta (Bebidas, Carnes, Otros…). Tabla "categoria".
@Entity(
    tableName = "categoria",
    indices = [Index(value = ["nombre"], unique = true)]  // no puede haber dos categorías con el mismo nombre
)
data class Categoria(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,            // el carné; lo pone Room solo al guardar

    val nombre: String,
    val imagen: String?,         // ruta de la foto; vacía si no tiene (fotos: S12)
    val orden: Int,              // posición en la carta
    val activo: Boolean,         // false = eliminada (no se borra nunca)

    @ColumnInfo(name = "es_por_defecto")
    val esPorDefecto: Boolean    // true solo en "Otros" (R16)
)