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

// P-C-08 (R16): siempre hay exactamente una categoría por defecto; no se elimina y no se crea otra.
@RunWith(AndroidJUnit4::class)
class CategoriaPorDefectoTest {

    private lateinit var db: AppDatabase
    private lateinit var repositorio: CartaRepository

    @Before
    fun prepararBase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Base en memoria y SIN addCallback: la precarga la lanzamos nosotros y la esperamos
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        runBlocking { Precarga(context).cargar(db) }

        val comandas = ComandaRepositoryReal(db, db.mesaDao(), db.comandaDao())
        repositorio = CartaRepository(db.categoriaDao(), db.productoDao(), db.precargadosDao(), comandas)
    }

    @After
    fun cerrarBase() {
        db.close()
    }

    // Cuántas categorías tienen esPorDefecto = true (nunca se busca por el nombre, R16)
    private fun contarPorDefecto(): Int =
        runBlocking { db.categoriaDao().todas().count { it.esPorDefecto } }

    @Test
    fun siempreHayUnaSolaCategoriaPorDefecto() {
        // 1. Tras la precarga: una sola, y activa
        assertEquals(1, contarPorDefecto())
        val porDefecto = runBlocking { db.categoriaDao().porDefecto() }
        assertTrue(porDefecto.activo)

        // 2. Intentar eliminarla: sigue activa
        runBlocking { repositorio.eliminarCategoria(porDefecto.id) }
        assertTrue(runBlocking { db.categoriaDao().porDefecto() }.activo)

        // 3. Intentar crear otra por defecto: sigue habiendo una sola
        val varios = Categoria(nombre = "Varios", imagen = null, orden = 0, activo = true, esPorDefecto = true)
        runBlocking { repositorio.guardarCategoria(varios) }
        assertEquals(1, contarPorDefecto())
    }
}