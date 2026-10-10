package yunkang.ako.datos.entidades

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Una mesa (las crea la precarga). Tabla "mesa".
// No guarda si está ocupada: eso se calcula mirando si tiene una comanda PENDIENTE.
// El número no se repite: no puede haber dos mesas 7.
@Entity(
    tableName = "mesa",
    indices = [Index(value = ["numero"], unique = true)]
)
data class Mesa(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val numero: Int
)