package yunkang.ako

import android.app.Application
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import yunkang.ako.datos.AppDatabase
import yunkang.ako.datos.Precarga
import yunkang.ako.datos.repositorios.CartaRepository
import yunkang.ako.datos.repositorios.ComandaRepository
import yunkang.ako.datos.repositorios.ComandaRepositoryReal
import yunkang.ako.datos.repositorios.SeguridadRepository
import yunkang.ako.imagenes.ImageStore
import yunkang.ako.seguridad.PinStore

// La entrada a Ako: Android la crea antes que cualquier pantalla y vive mientras la app está abierta.
// Guarda lo que es único en toda la app: la base de datos y los repositorios (P17, P116).
class EntradaAko : Application() {

    // "by lazy": se crea la primera vez que alguien la pide; después, siempre la misma.
    val db: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "ako.db")
            .addCallback(Precarga(this))   // la primera vez que se crea, salta la precarga
            .build()
    }

    // El puesto de comandas: quien lo pida solo ve sus preguntas, no la versión real (P132).
    val comandaRepository: ComandaRepository by lazy {
        ComandaRepositoryReal(db, db.mesaDao(), db.comandaDao())
    }

    // La carta pregunta a comandas por el puesto, nunca por su DAO (P127).
    val cartaRepository: CartaRepository by lazy {
        CartaRepository(db.categoriaDao(), db.productoDao(), db.precargadosDao(), comandaRepository)
    }

    // [Claude] El PinStore se crea aquí dentro y no se ofrece suelto: al PIN solo se llega por este repositorio.
    val seguridadRepository: SeguridadRepository by lazy {
        SeguridadRepository(PinStore(this))
    }

    // El almacén de fotos, con el contexto de la app (vive lo mismo que la app, nunca el de una pantalla)
    val imageStore: ImageStore by lazy {
        ImageStore(this)
    }

    override fun onCreate() {
        super.onCreate()
        // Al arrancar, abre la base de datos por detrás; si es la primera vez, salta la precarga (P118).
        CoroutineScope(Dispatchers.IO).launch {
            db.openHelper.writableDatabase
        }
    }
}