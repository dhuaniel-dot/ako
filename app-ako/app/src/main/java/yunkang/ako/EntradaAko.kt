package yunkang.ako

import android.app.Application
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import yunkang.ako.datos.AppDatabase
import yunkang.ako.datos.Precarga

// La entrada a Ako: Android la crea antes que cualquier pantalla y vive mientras la app está abierta.
// Guarda lo que es único en toda la app: la base de datos (P17, P116).
class EntradaAko : Application() {

    // "by lazy": se crea la primera vez que alguien la pide; después, siempre la misma.
    val db: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "ako.db")
            .addCallback(Precarga(this))   // la primera vez que se crea, salta la precarga
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        // Al arrancar, abre la base de datos por detrás; si es la primera vez, salta la precarga (P118).
        CoroutineScope(Dispatchers.IO).launch {
            db.openHelper.writableDatabase
        }
    }
}