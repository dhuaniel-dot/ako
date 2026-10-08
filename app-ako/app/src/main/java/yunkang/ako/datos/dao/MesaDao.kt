package yunkang.ako.datos.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import yunkang.ako.datos.entidades.Mesa

// Lo que se le puede pedir a Room sobre las mesas.
@Dao
interface MesaDao {

    // Las 60 mesas, por número.
    @Query("SELECT * FROM mesa ORDER BY numero")
    suspend fun todas(): List<Mesa>

    // P167 A: las 60 mesas, por número; Room las vuelve a mandar si cambia la tabla mesa
    @Query("SELECT * FROM mesa ORDER BY numero")
    fun todasObservadas(): Flow<List<Mesa>>
}