package yunkang.ako.datos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import yunkang.ako.datos.entidades.Categoria
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {

    @Insert
    suspend fun insertar(categoria: Categoria): Long

    @Update
    suspend fun actualizar(categoria: Categoria)

    // «Otros la última» lo decide el repositorio, no el SQL.
    @Query("SELECT * FROM categoria ORDER BY orden")
    suspend fun todas(): List<Categoria>

    // La misma lista que todas(), pero Room la vuelve a mandar cada vez que cambia la tabla.
    @Query("SELECT * FROM categoria ORDER BY orden")
    fun todasObservadas(): Flow<List<Categoria>>

    // La categoría por defecto (Otros): se reconoce por esPorDefecto, nunca por el nombre.
    @Query("SELECT * FROM categoria WHERE es_por_defecto = 1 LIMIT 1")
    suspend fun porDefecto(): Categoria

    // ¿Hay otra categoría con ese nombre? Sin mirar mayúsculas.
    // exceptoId: la que se está editando, para que no se encuentre a sí misma (al crear, 0).
    @Query("SELECT EXISTS(SELECT 1 FROM categoria WHERE nombre = :nombre COLLATE NOCASE AND id != :exceptoId)")
    suspend fun existeNombre(nombre: String, exceptoId: Long): Boolean

    // Una categoría por su id (para editarla, eliminarla o recuperarla); vacío si no existe (como ProductoDao.porId).
    @Query("SELECT * FROM categoria WHERE id = :id")
    suspend fun porId(id: Long): Categoria?
}