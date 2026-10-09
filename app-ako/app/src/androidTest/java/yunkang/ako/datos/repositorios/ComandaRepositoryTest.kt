package yunkang.ako.datos.repositorios

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import yunkang.ako.datos.AppDatabase
import yunkang.ako.datos.Precarga
import yunkang.ako.datos.entidades.Categoria
import yunkang.ako.datos.entidades.EstadoComanda
import yunkang.ako.datos.entidades.Producto
import yunkang.ako.dominio.Carrito

// Enviar y quitar líneas con Room de verdad (base en memoria con la precarga hecha).
@RunWith(AndroidJUnit4::class)
class ComandaRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repositorio: ComandaRepositoryReal
    private lateinit var entrecot: Producto
    private lateinit var helado: Producto
    private var mesa4Id = 0L

    @Before
    fun prepararBase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Igual que en CategoriaPorDefectoTest (repetido a propósito: cada clase se lee sola)
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .addCallback(Precarga(context))
            .build()
        repositorio = ComandaRepositoryReal(db, db.mesaDao(), db.comandaDao())

        runBlocking {
            // Carnes ANTES que Entrecot: un plato tiene que apuntar a una categoría que ya existe (R11)
            val carnesId = db.categoriaDao().insertar(
                Categoria(nombre = "Carnes", imagen = null, orden = 1, activo = true, esPorDefecto = false)
            )
            val idEntrecot = db.productoDao().insertar(
                Producto(categoriaId = carnesId, numero = 12, nombre = "Entrecot", descripcion = null,
                    precioCentimos = 1850, imagen = null, activo = true)
            )
            entrecot = checkNotNull(db.productoDao().porId(idEntrecot))

            // Helado, en la categoría por defecto (se busca por esPorDefecto, nunca por el nombre)
            val porDefectoId = db.categoriaDao().porDefecto().id
            val idHelado = db.productoDao().insertar(
                Producto(categoriaId = porDefectoId, numero = 32, nombre = "Helado", descripcion = null,
                    precioCentimos = 500, imagen = null, activo = true)
            )
            helado = checkNotNull(db.productoDao().porId(idHelado))

            // La mesa 4 se busca por su número: nunca se da por hecho que su id es 4
            mesa4Id = db.mesaDao().todas().first { it.numero == 4 }.id
        }
    }

    @After
    fun cerrarBase() {
        db.close()
    }

    // Dos envíos a la mesa 4 → una sola comanda con dos líneas copiadas (R1, R2, R14)
    @Test
    fun dosEnviosALaMismaMesaVanALaMismaComanda() {
        val primero = Carrito(mesa4Id)
        primero.anadir(entrecot, 2)
        val id1 = runBlocking { repositorio.enviarCarrito(primero) }

        val segundo = Carrito(mesa4Id)
        segundo.anadir(helado, 1)
        val id2 = runBlocking { repositorio.enviarCarrito(segundo) }

        // R1: la misma comanda, y es la pendiente de la mesa 4
        assertEquals(id1, id2)
        assertEquals(id1, runBlocking { db.comandaDao().pendienteDeMesa(mesa4Id) }?.id)

        // R2: dos líneas, una por envío
        assertEquals(2, runBlocking { db.comandaDao().contarLineas(id1) })

        // R14: cada línea lleva el nombre, el precio y la cantidad copiados al enviar
        val lineas = runBlocking { db.comandaDao().lineasDe(id1) }
        assertEquals("Entrecot", lineas[0].nombreProducto)
        assertEquals(1850, lineas[0].precioUnitarioCentimos)
        assertEquals(2, lineas[0].cantidad)
        assertEquals("Helado", lineas[1].nombreProducto)
        assertEquals(500, lineas[1].precioUnitarioCentimos)
        assertEquals(1, lineas[1].cantidad)
    }

    // Quitar la última línea deja la comanda ANULADA y la mesa libre (R7, R3)
    @Test
    fun quitarLaUltimaLineaAnulaLaComanda() {
        val carrito = Carrito(mesa4Id)
        carrito.anadir(entrecot, 1)
        val comandaId = runBlocking { repositorio.enviarCarrito(carrito) }
        val linea = runBlocking { db.comandaDao().lineasDe(comandaId) }.single()

        // true = era la última línea (la pantalla de Cuenta vuelve entonces a la rejilla)
        assertTrue(runBlocking { repositorio.quitarLinea(linea.id) })

        // R7: la comanda no se borra; queda ANULADA, con su hora de cierre y sin líneas
        val comanda = checkNotNull(runBlocking { db.comandaDao().porId(comandaId) })
        assertEquals(EstadoComanda.ANULADA, comanda.estado)
        assertNotNull(comanda.fechaCierre)
        assertEquals(0, runBlocking { db.comandaDao().contarLineas(comandaId) })

        // R3: la mesa 4 sale libre en la rejilla (first = una foto del grifo, no se queda escuchando)
        val rejilla = runBlocking { repositorio.mesasConEstado().first() }
        val mesa4 = rejilla.single { it.mesa.numero == 4 }
        assertNull(mesa4.comandaId)
    }
}