package yunkang.ako.datos.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import yunkang.ako.datos.entidades.Mesa

@Dao
interface MesaDao {

    @Query("SELECT * FROM mesa ORDER BY numero")
    suspend fun todas(): List<Mesa>

    @Query("SELECT * FROM mesa ORDER BY numero")
    fun todasObservadas(): Flow<List<Mesa>>
}