package yunkang.ako.datos.dao

import androidx.room.Dao
import androidx.room.Insert
import yunkang.ako.datos.entidades.Alergeno

// Lo que viene fijo de fábrica (los 14 alérgenos).
@Dao
interface PrecargadosDao {

    // Guarda los 14 alérgenos de golpe.
    @Insert
    suspend fun insertarAlergenos(alergenos: List<Alergeno>)
}