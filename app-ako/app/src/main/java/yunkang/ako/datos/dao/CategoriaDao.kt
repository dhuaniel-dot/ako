package yunkang.ako.datos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import yunkang.ako.datos.entidades.Categoria

// Lo que se le puede pedir a Room sobre las categorías.
@Dao
interface CategoriaDao {

    // Guarda una categoría nueva y devuelve el id que le ha dado Room.
    @Insert
    suspend fun insertar(categoria: Categoria): Long

    // Guarda los cambios de una categoría que ya existe (la busca por su id).
    @Update
    suspend fun actualizar(categoria: Categoria)

    // Todas las categorías, activas y eliminadas, por su posición.
    // «Otros la última» lo decide el repositorio, no el SQL (P48).
    @Query("SELECT * FROM categoria ORDER BY orden")
    suspend fun todas(): List<Categoria>

    // La categoría por defecto (Otros): se reconoce por esPorDefecto, nunca por el nombre (R16).
    @Query("SELECT * FROM categoria WHERE es_por_defecto = 1 LIMIT 1")
    suspend fun porDefecto(): Categoria

    // ¿Hay otra categoría con ese nombre? Sin mirar mayúsculas (P124).
    // exceptoId: la que se está editando, para que no se encuentre a sí misma (al crear, 0).
    @Query("SELECT EXISTS(SELECT 1 FROM categoria WHERE nombre = :nombre COLLATE NOCASE AND id != :exceptoId)")
    suspend fun existeNombre(nombre: String, exceptoId: Long): Boolean

    // Una categoría por su id (para editarla, eliminarla o recuperarla); vacío si no existe (P152, como ProductoDao.porId).
    @Query("SELECT * FROM categoria WHERE id = :id")
    suspend fun porId(id: Long): Categoria?
}