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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import java.time.LocalDate
import java.time.ZoneId

// El puesto de comandas con Room de verdad (base en memoria con la precarga hecha).
@RunWith(AndroidJUnit4::class)
class ComandaRepositoryTest {

    private lateinit var baseDeDatos: AppDatabase
    private lateinit var repositorio: ComandaRepositoryReal
    private lateinit var entrecot: Producto
    private lateinit var helado: Producto
    private var mesa4Id = 0L
    private var mesa7Id = 0L

    @Before
    fun prepararBase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Igual que en CategoriaPorDefectoTest (repetido a propósito: cada clase se lee sola)
        baseDeDatos = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .addCallback(Precarga(context))
            .build()
        repositorio = ComandaRepositoryReal(baseDeDatos, baseDeDatos.mesaDao(), baseDeDatos.comandaDao())

        runBlocking {
            // Carnes ANTES que Entrecot: un plato tiene que apuntar a una categoría que ya existe
            val carnesId = baseDeDatos.categoriaDao().insertar(
                Categoria(nombre = "Carnes", imagen = null, orden = 1, activo = true, esPorDefecto = false)
            )
            val idEntrecot = baseDeDatos.productoDao().insertar(
                Producto(categoriaId = carnesId, numero = 12, nombre = "Entrecot", descripcion = null,
                    precioCentimos = 1850, imagen = null, activo = true)
            )
            entrecot = checkNotNull(baseDeDatos.productoDao().porId(idEntrecot))

            // Helado, en la categoría por defecto (se busca por esPorDefecto, nunca por el nombre)
            val porDefectoId = baseDeDatos.categoriaDao().porDefecto().id
            val idHelado = baseDeDatos.productoDao().insertar(
                Producto(categoriaId = porDefectoId, numero = 32, nombre = "Helado", descripcion = null,
                    precioCentimos = 500, imagen = null, activo = true)
            )
            helado = checkNotNull(baseDeDatos.productoDao().porId(idHelado))

            // Las mesas 4 y 7 se buscan por su número: nunca se da por hecho que su id es 4 o 7
            mesa4Id = baseDeDatos.mesaDao().todas().first { it.numero == 4 }.id
            mesa7Id = baseDeDatos.mesaDao().todas().first { it.numero == 7 }.id
        }
    }

    @After
    fun cerrarBase() {
        baseDeDatos.close()
    }

    // Dos envíos a la mesa 4 → una sola comanda con dos líneas copiadas
    @Test
    fun dosEnviosALaMismaMesaVanALaMismaComanda() {
        val primero = Carrito(mesa4Id)
        primero.anadir(entrecot, 2)
        val id1 = runBlocking { repositorio.enviarCarrito(primero) }

        val segundo = Carrito(mesa4Id)
        segundo.anadir(helado, 1)
        val id2 = runBlocking { repositorio.enviarCarrito(segundo) }

        // La misma comanda, y es la pendiente de la mesa 4
        assertEquals(id1, id2)
        assertEquals(id1, runBlocking { baseDeDatos.comandaDao().pendienteDeMesa(mesa4Id) }?.id)

        // Dos líneas, una por envío
        assertEquals(2, runBlocking { baseDeDatos.comandaDao().contarLineas(id1) })

        // Cada línea lleva el nombre, el precio y la cantidad copiados al enviar
        val lineas = runBlocking { baseDeDatos.comandaDao().lineasDe(id1) }
        assertEquals("Entrecot", lineas[0].nombreProducto)
        assertEquals(1850, lineas[0].precioUnitarioCentimos)
        assertEquals(2, lineas[0].cantidad)
        assertEquals("Helado", lineas[1].nombreProducto)
        assertEquals(500, lineas[1].precioUnitarioCentimos)
        assertEquals(1, lineas[1].cantidad)
    }

    // Quitar la última línea deja la comanda ANULADA y la mesa libre
    @Test
    fun quitarLaUltimaLineaAnulaLaComanda() {
        val carrito = Carrito(mesa4Id)
        carrito.anadir(entrecot, 1)
        val comandaId = runBlocking { repositorio.enviarCarrito(carrito) }
        val linea = runBlocking { baseDeDatos.comandaDao().lineasDe(comandaId) }.single()

        // true = era la última línea (la pantalla de Cuenta vuelve entonces a la rejilla)
        assertTrue(runBlocking { repositorio.quitarLinea(linea.id) })

        // La comanda no se borra; queda ANULADA, con su hora de cierre y sin líneas
        val comanda = checkNotNull(runBlocking { baseDeDatos.comandaDao().porId(comandaId) })
        assertEquals(EstadoComanda.ANULADA, comanda.estado)
        assertNotNull(comanda.fechaCierre)
        assertEquals(0, runBlocking { baseDeDatos.comandaDao().contarLineas(comandaId) })

        // La mesa 4 sale libre en la rejilla (first = una foto del grifo, no se queda escuchando)
        val rejilla = runBlocking { repositorio.mesasConEstado().first() }
        val mesa4 = rejilla.single { it.mesa.numero == 4 }
        assertNull(mesa4.comandaId)
    }

    // Una línea que ya no existe (doble toque en Quitar) no se quita ni anula la comanda
    @Test
    fun quitarLineaInexistenteNoHaceNada() {
        val carrito = Carrito(mesa4Id)
        carrito.anadir(entrecot, 1)
        val comandaId = runBlocking { repositorio.enviarCarrito(carrito) }
        val quitada = runBlocking { repositorio.quitarLinea(999) }
        val comanda = checkNotNull(runBlocking { baseDeDatos.comandaDao().porId(comandaId) })
        val lineas = runBlocking { baseDeDatos.comandaDao().lineasDe(comandaId) }
        assertFalse(quitada)
        assertEquals(EstadoComanda.PENDIENTE, comanda.estado)
        assertEquals(1, lineas.size)
    }

    // Una comanda nunca nace vacía: enviar un carrito sin platos es un fallo de programación
    @Test
    fun enviarCarritoVacioLanza() {
        val vacio = Carrito(mesa4Id)
        assertThrows(IllegalArgumentException::class.java) { runBlocking { repositorio.enviarCarrito(vacio) } }
    }

    // Una comanda cobrada ya está cerrada y no se puede volver a cobrar
    @Test
    fun cobrarComandaCerradaLanza() {
        val carrito = Carrito(mesa4Id)
        carrito.anadir(entrecot, 1)
        val comandaId = runBlocking { repositorio.enviarCarrito(carrito) }
        runBlocking { repositorio.cobrar(comandaId) }
        assertThrows(IllegalStateException::class.java) { runBlocking { repositorio.cobrar(comandaId) } }
    }

    // El día del Resumen de ingresos acaba a las 23:59:59,999: lo cobrado un milisegundo después ya es de mañana
    @Test
    fun resumenDelDiaRespetaLaMedianoche() {
        val hoy = LocalDate.now()
        val zona = ZoneId.systemDefault()
        val ultimoMilisegundoDeHoy = hoy.atTime(23, 59, 59, 999_000_000).atZone(zona).toInstant().toEpochMilli()
        val primerMilisegundoDeManana = hoy.plusDays(1).atStartOfDay(zona).toInstant().toEpochMilli()

        val carritoMesa4 = Carrito(mesa4Id)
        carritoMesa4.anadir(entrecot, 1)
        val idDeHoy = runBlocking { repositorio.enviarCarrito(carritoMesa4) }
        val carritoMesa7 = Carrito(mesa7Id)
        carritoMesa7.anadir(helado, 1)
        val idDeManana = runBlocking { repositorio.enviarCarrito(carritoMesa7) }

        runBlocking {
            repositorio.cobrar(idDeHoy)
            repositorio.cobrar(idDeManana)
            val cobradaHoy = checkNotNull(baseDeDatos.comandaDao().porId(idDeHoy))
            baseDeDatos.comandaDao().actualizar(cobradaHoy.copy(fechaCierre = ultimoMilisegundoDeHoy))
            val cobradaManana = checkNotNull(baseDeDatos.comandaDao().porId(idDeManana))
            baseDeDatos.comandaDao().actualizar(cobradaManana.copy(fechaCierre = primerMilisegundoDeManana))
        }

        val resumen = runBlocking { repositorio.resumenDelDia(hoy) }
        assertEquals(1, resumen.comandas.size)
        assertEquals(idDeHoy, resumen.comandas[0].comandaId)
    }

    // El total de cada mesa lo suma la base de datos y una mesa sin comanda sale libre
    @Test
    fun mesasConEstadoSumaElTotal() {
        val carrito = Carrito(mesa4Id)
        carrito.anadir(entrecot, 2)
        carrito.anadir(helado, 1)
        runBlocking { repositorio.enviarCarrito(carrito) }
        val rejilla = runBlocking { repositorio.mesasConEstado().first() }
        val mesa4 = rejilla.single { it.mesa.numero == 4 }
        val mesa7 = rejilla.single { it.mesa.numero == 7 }
        assertEquals(4200, mesa4.totalCentimos)
        assertNull(mesa7.comandaId)
    }
}