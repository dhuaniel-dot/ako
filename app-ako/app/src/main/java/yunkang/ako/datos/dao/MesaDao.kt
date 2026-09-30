package yunkang.ako.datos.dao

import androidx.room.Dao
import androidx.room.Insert
import yunkang.ako.datos.entidades.Mesa

// Lo que se le puede pedir a Room sobre las mesas.
@Dao
interface MesaDao {

    // Guarda varias mesas de golpe (la precarga mete las 60).
    @Insert
    suspend fun insertarTodas(mesas: List<Mesa>)
}