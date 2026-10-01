package yunkang.ako.datos

import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import yunkang.ako.EntradaAko
import yunkang.ako.R
import yunkang.ako.datos.entidades.Alergeno
import yunkang.ako.datos.entidades.Categoria
import yunkang.ako.datos.entidades.Mesa
import yunkang.ako.datos.entidades.Producto

// La precarga (RF-50): lo que la app trae "de fábrica" la primera vez que se abre.
// Room llama a onCreate UNA sola vez: cuando crea el archivo de la base de datos (P16).
class Precarga(private val context: Context) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // Se hace por detrás (corrutina) para no bloquear el arranque de la app.
        // Ojo: no usa el "db" que pasa Room (es la base cruda), sino la de EntradaAko. Las pruebas de
        // Room (S11) con una base en memoria NO registran este callback: llaman a cargar(db) directamente.
        CoroutineScope(Dispatchers.IO).launch {
            cargar((context.applicationContext as EntradaAko).db)
        }
    }

    // Todo el trabajo. Recibe la base de datos para que las pruebas de la S11 puedan usar otra.
    // P149: todo dentro de UNA transacción: o se precarga entero o nada (si la app muriera a mitad,
    // la base no quedaría a medias para siempre).
    suspend fun cargar(db: AppDatabase) = db.withTransaction {

        // 1. La categoría por defecto, ANTES que cualquier plato (R16, P44: orden 0).
        db.categoriaDao().insertar(
            Categoria(
                nombre = context.getString(R.string.precarga_categoria_por_defecto),
                imagen = null,
                orden = 0,
                activo = true,
                esPorDefecto = true
            )
        )

        // 2. Las 60 mesas, de la 1 a la 60.
        val mesas = mutableListOf<Mesa>()
        for (numero in 1..60) {
            mesas.add(Mesa(numero = numero))
        }
        db.mesaDao().insertarTodas(mesas)

        // 3. Los 14 alérgenos, en el orden de la ley (el de strings.xml).
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
        val alergenos = mutableListOf<Alergeno>()
        for (texto in textosAlergenos) {
            alergenos.add(Alergeno(nombre = context.getString(texto)))
        }
        db.precargadosDao().insertarAlergenos(alergenos)

        // 4. El ejemplo (P7): categoría Bebidas y el plato 1 · Agua · 1,50 €.
        val idBebidas = db.categoriaDao().insertar(
            Categoria(
                nombre = context.getString(R.string.precarga_categoria_ejemplo),
                imagen = null,
                orden = 1,
                activo = true,
                esPorDefecto = false
            )
        )
        db.productoDao().insertar(
            Producto(
                categoriaId = idBebidas,   // el id que devolvió insertar al guardar Bebidas (el resguardo del guardarropa)
                numero = 1,
                nombre = context.getString(R.string.precarga_plato_ejemplo),
                descripcion = null,
                precioCentimos = 150,
                imagen = null,
                activo = true
            )
        )
    }
}