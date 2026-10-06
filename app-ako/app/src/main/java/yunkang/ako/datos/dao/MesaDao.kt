package yunkang.ako.datos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import yunkang.ako.datos.entidades.Mesa

// Lo que se le puede pedir a Room sobre las mesas.
@Dao
interface MesaDao {

    // Guarda varias mesas de golpe (la precarga mete las 60).
    @Insert
    suspend fun insertarTodas(mesas: List<Mesa>)
    // Las 60 mesas, por número.
    @Query("SELECT * FROM mesa ORDER BY numero")
    suspend fun todas(): List<Mesa>

    // P167 A: las 60 mesas, por número; Room las vuelve a mandar si cambia la tabla mesa
    @Query("SELECT * FROM mesa ORDER BY numero")
    fun todasObservadas(): Flow<List<Mesa>>
}