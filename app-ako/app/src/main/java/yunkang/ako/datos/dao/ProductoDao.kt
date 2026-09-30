package yunkang.ako.datos.dao

import androidx.room.Dao
import androidx.room.Insert
import yunkang.ako.datos.entidades.Producto

// Lo que se le puede pedir a Room sobre los platos.
@Dao
interface ProductoDao {

    // Guarda un plato nuevo y devuelve el id que le ha dado Room.
    @Insert
    suspend fun insertar(producto: Producto): Long
}