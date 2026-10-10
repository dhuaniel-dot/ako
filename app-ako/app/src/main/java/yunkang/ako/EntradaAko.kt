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
import yunkang.ako.imagenes.Galeria
import yunkang.ako.seguridad.GuardaPin

// la entrada a Ako: Android la crea antes que cualquier pantalla y vive mientras la app está abierta
// guarda lo que es único en toda la app: la base de datos y los repositorios
class EntradaAko : Application() {

    // se crea la primera vez que alguien la pide y después es siempre la misma
    val baseDeDatos: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "ako.db")
            .addCallback(Precarga(this))
            .build()
    }

    // el puesto de comandas: quien lo pida solo ve sus preguntas, no la versión real
    val comandaRepository: ComandaRepository by lazy {
        ComandaRepositoryReal(baseDeDatos, baseDeDatos.mesaDao(), baseDeDatos.comandaDao())
    }

    val cartaRepository: CartaRepository by lazy {
        CartaRepository(baseDeDatos.categoriaDao(), baseDeDatos.productoDao(), baseDeDatos.precargadosDao(), comandaRepository)
    }

    // [Claude] el GuardaPin se crea aquí dentro y no se ofrece suelto: al PIN solo se llega por este repositorio
    val seguridadRepository: SeguridadRepository by lazy {
        SeguridadRepository(GuardaPin(this))
    }

    // la galería de fotos
    val galeria: Galeria by lazy {
        Galeria(this)
    }

    override fun onCreate() {
        super.onCreate()
        // al arrancar, abre la base de datos por detrás; si es la primera vez, salta la precarga
        CoroutineScope(Dispatchers.IO).launch {
            baseDeDatos.openHelper.writableDatabase
        }
    }
}