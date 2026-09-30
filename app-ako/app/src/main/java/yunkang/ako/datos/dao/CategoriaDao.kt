package yunkang.ako.datos.dao

import androidx.room.Dao
import androidx.room.Insert
import yunkang.ako.datos.entidades.Categoria

// Lo que se le puede pedir a Room sobre las categorías.
@Dao
interface CategoriaDao {

    // Guarda una categoría nueva y devuelve el id que le ha dado Room.
    @Insert
    suspend fun insertar(categoria: Categoria): Long
}