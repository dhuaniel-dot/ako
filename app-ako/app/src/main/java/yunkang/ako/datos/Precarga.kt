package yunkang.ako.datos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import yunkang.ako.R

// La precarga (RF-50): lo que la app trae "de fábrica" la primera vez que se abre.
// Room llama a onCreate UNA sola vez: cuando crea el archivo de la base de datos.
// Todo se escribe aquí dentro, en la base «cruda» que pasa Room, y no por detrás: Android crea la base
// dentro de una transacción que incluye este onCreate; si la app muriera a mitad, no quedaría nada y la
// próxima vez se volvería a crear entera.
// Fuente: https://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper
// [Claude] Aquí no hay DAO: se escribe con los nombres de tablas y columnas de las entidades. Si uno
// estuviera mal, fallaría al abrir la app (no al compilar); lo vigilan las pruebas de Room
class Precarga(private val context: Context) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        // La categoría por defecto va antes que cualquier plato (R16): orden 0.
        val otros = ContentValues()
        otros.put("nombre", context.getString(R.string.precarga_categoria_por_defecto))
        otros.putNull("imagen")
        otros.put("orden", 0)
        otros.put("activo", true)
        otros.put("es_por_defecto", true)
        db.insert("categoria", SQLiteDatabase.CONFLICT_ABORT, otros)

        for (numero in 1..60) {
            val mesa = ContentValues()
            mesa.put("numero", numero)
            db.insert("mesa", SQLiteDatabase.CONFLICT_ABORT, mesa)
        }

        // Los 14 alérgenos, en el orden de la ley (el de strings.xml).
        val textosAlergenos = listOf(
            R.string.alergeno_01_gluten,
            R.string.alergeno_02_crustaceos,
            R.string.alergeno_03_huevos,
            R.string.alergeno_04_pescado,
            R.string.alergeno_05_cacahuetes,
            R.string.alergeno_06_soja,
            R.string.alergeno_07_lacteos,
            R.string.alergeno_08_frutos_cascara,
            R.string.alergeno_09_apio,
            R.string.alergeno_10_mostaza,
            R.string.alergeno_11_sesamo,
            R.string.alergeno_12_sulfitos,
            R.string.alergeno_13_altramuces,
            R.string.alergeno_14_moluscos
        )
        for (texto in textosAlergenos) {
            val alergeno = ContentValues()
            alergeno.put("nombre", context.getString(texto))
            db.insert("alergeno", SQLiteDatabase.CONFLICT_ABORT, alergeno)
        }

        // El ejemplo: categoría Bebidas y el plato 1 · Agua · 1,50 €.
        val bebidas = ContentValues()
        bebidas.put("nombre", context.getString(R.string.precarga_categoria_ejemplo))
        bebidas.putNull("imagen")
        bebidas.put("orden", 1)
        bebidas.put("activo", true)
        bebidas.put("es_por_defecto", false)
        val idBebidas = db.insert("categoria", SQLiteDatabase.CONFLICT_ABORT, bebidas)

        val agua = ContentValues()
        agua.put("categoria_id", idBebidas)
        agua.put("numero", 1)
        agua.put("nombre", context.getString(R.string.precarga_plato_ejemplo))
        agua.putNull("descripcion")
        agua.put("precio_centimos", 150)
        agua.putNull("imagen")
        agua.put("activo", true)
        db.insert("producto", SQLiteDatabase.CONFLICT_ABORT, agua)
    }
}