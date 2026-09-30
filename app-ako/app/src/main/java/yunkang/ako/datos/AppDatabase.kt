package yunkang.ako.datos

import androidx.room.Database
import androidx.room.RoomDatabase
import yunkang.ako.datos.entidades.Alergeno
import yunkang.ako.datos.entidades.Categoria
import yunkang.ako.datos.entidades.Comanda
import yunkang.ako.datos.entidades.LineaComanda
import yunkang.ako.datos.entidades.Mesa
import yunkang.ako.datos.entidades.Producto
import yunkang.ako.datos.entidades.ProductoAlergeno
import yunkang.ako.datos.dao.CategoriaDao
import yunkang.ako.datos.dao.MesaDao
import yunkang.ako.datos.dao.PrecargadosDao
import yunkang.ako.datos.dao.ProductoDao

// La base de datos de Ako: el archivador con sus 7 cajones.
// La única instancia la guarda EntradaAko (P116).
@Database(
    entities = [
        Categoria::class,
        Producto::class,
        Alergeno::class,
        ProductoAlergeno::class,
        Mesa::class,
        Comanda::class,
        LineaComanda::class
    ],
    version = 1,             // se sube si algún día cambian las tablas
    exportSchema = false     // no guardamos una copia del esquema en un archivo aparte
)
abstract class AppDatabase : RoomDatabase() {

    // Los mostradores del archivero; Room escribe su código al compilar.
    abstract fun categoriaDao(): CategoriaDao
    abstract fun productoDao(): ProductoDao
    abstract fun mesaDao(): MesaDao
    abstract fun precargadosDao(): PrecargadosDao
}