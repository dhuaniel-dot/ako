package yunkang.ako.datos.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

// uno de los 14 alérgenos legales (gluten, crustáceos…). tabla "alergeno"
// se crean en la precarga y no se editan
@Entity(tableName = "alergeno")
data class Alergeno(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String
)