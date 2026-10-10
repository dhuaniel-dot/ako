package yunkang.ako.datos

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import yunkang.ako.datos.entidades.Categoria
import yunkang.ako.datos.repositorios.CartaRepository
import yunkang.ako.datos.repositorios.ComandaRepositoryReal
import yunkang.ako.dominio.modelos.ResultadoGuardado

// las categorías con Room de verdad (base en memoria con la precarga hecha)
@RunWith(AndroidJUnit4::class)
class CategoriaPorDefectoTest {

    private lateinit var baseDeDatos: AppDatabase
    private lateinit var repositorio: CartaRepository

    @Before
    fun prepararBase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // como la app: la precarga se escribe sola al abrir la base
        baseDeDatos = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .addCallback(Precarga(context))
            .build()

        val comandas = ComandaRepositoryReal(baseDeDatos, baseDeDatos.mesaDao(), baseDeDatos.comandaDao())
        repositorio = CartaRepository(baseDeDatos.categoriaDao(), baseDeDatos.productoDao(), baseDeDatos.precargadosDao(), comandas)
    }

    @After
    fun cerrarBase() {
        baseDeDatos.close()
    }

    // cuántas categorías tienen esPorDefecto = true (nunca se busca por el nombre)
    private fun contarPorDefecto(): Int =
        runBlocking { baseDeDatos.categoriaDao().todas().count { it.esPorDefecto } }

    // siempre hay exactamente una categoría por defecto; no se elimina y no se crea otra
    @Test
    fun siempreHayUnaSolaCategoriaPorDefecto() {
        // tras la precarga: una sola, y activa
        assertEquals(1, contarPorDefecto())
        val porDefecto = runBlocking { baseDeDatos.categoriaDao().porDefecto() }
        assertTrue(porDefecto.activo)

        // intentar eliminarla: sigue activa
        runBlocking { repositorio.eliminarCategoria(porDefecto.id) }
        assertTrue(runBlocking { baseDeDatos.categoriaDao().porDefecto() }.activo)

        // intentar crear otra por defecto: sigue habiendo una sola
        val varios = Categoria(nombre = "Varios", imagen = null, orden = 0, activo = true, esPorDefecto = true)
        runBlocking { repositorio.guardarCategoria(varios) }
        assertEquals(1, contarPorDefecto())
    }

    // «carnes» y «Carnes» son el mismo nombre; lo decide el SQL (COLLATE NOCASE), por eso se prueba con Room
    @Test
    fun nombreRepetidoSinMirarMayusculas() {
        val carnes = Categoria(nombre = "Carnes", imagen = null, orden = 0, activo = true, esPorDefecto = false)
        runBlocking { repositorio.guardarCategoria(carnes) }
        val enMinusculas = Categoria(nombre = "carnes", imagen = null, orden = 0, activo = true, esPorDefecto = false)
        assertEquals(ResultadoGuardado.NombreRepetido, runBlocking { repositorio.guardarCategoria(enMinusculas) })
    }
}