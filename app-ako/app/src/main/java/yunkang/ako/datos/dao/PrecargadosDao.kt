package yunkang.ako.datos.dao

import androidx.room.Dao
import androidx.room.Query
import yunkang.ako.datos.entidades.Alergeno

// Lo que viene fijo de fábrica (los 14 alérgenos).
@Dao
interface PrecargadosDao {

    // Los 14 alérgenos, en el orden en que se precargaron.
    @Query("SELECT * FROM alergeno ORDER BY id")
    suspend fun alergenos(): List<Alergeno>
}