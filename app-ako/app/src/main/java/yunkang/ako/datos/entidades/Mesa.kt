package yunkang.ako.datos.entidades

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// una mesa (las crea la precarga). tabla "mesa"
// no guarda si está ocupada: eso se calcula mirando si tiene una comanda pendiente
// el número no se repite: no puede haber dos mesas 7
@Entity(
    tableName = "mesa",
    indices = [Index(value = ["numero"], unique = true)]
)
data class Mesa(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val numero: Int
)