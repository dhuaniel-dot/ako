package yunkang.ako.datos.repositorios

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import yunkang.ako.datos.entidades.Producto
import yunkang.ako.dominio.modelos.ResultadoGuardado

// guardarPlato con actores en vez de base de datos, sin emulador.
class CartaRepositoryTest {

    // Un plato nuevo (id 0) con el precio que diga cada prueba.
    private fun platoConPrecio(centimos: Int) = Producto(
        categoriaId = 2, numero = 12, nombre = "Entrecot", descripcion = null,
        precioCentimos = centimos, imagen = null, activo = true
    )

    // El repositorio de verdad, pero con actores en vez de DAOs.
    private fun crearRepositorio(
        productoDao: ProductoDaoFalso,
        categoriaDao: CategoriaDaoFalso = CategoriaDaoFalso()
    ) = CartaRepository(categoriaDao, productoDao, PrecargadosDaoFalso(), ComandaRepositoryFalso())

    // 18,50 € se guarda, y se guarda con ese precio
    @Test
    fun precioNormalSeGuarda() {
        val productoDao = ProductoDaoFalso()
        val repositorio = crearRepositorio(productoDao)
        val resultado = runBlocking { repositorio.guardarPlato(platoConPrecio(1850), emptyList()) }
        assertEquals(ResultadoGuardado.Ok, resultado)
        assertTrue(productoDao.seLlamoInsertar)
        assertEquals(1850, productoDao.productoRecibido?.precioCentimos)
    }

    // 0 € se guarda (un plato gratis es válido)
    @Test
    fun precioCeroSeGuarda() {
        val productoDao = ProductoDaoFalso()
        val repositorio = crearRepositorio(productoDao)
        val resultado = runBlocking { repositorio.guardarPlato(platoConPrecio(0), emptyList()) }
        assertEquals(ResultadoGuardado.Ok, resultado)
        assertEquals(0, productoDao.productoRecibido?.precioCentimos)
    }

    // −1,00 € lanza un error y el DAO no recibe nada: ni el plato ni sus alérgenos
    @Test
    fun precioNegativoLanzaYNoGuarda() {
        val productoDao = ProductoDaoFalso()
        val repositorio = crearRepositorio(productoDao)
        assertThrows(IllegalArgumentException::class.java) {
            runBlocking { repositorio.guardarPlato(platoConPrecio(-100), listOf(1L)) }
        }
        assertFalse(productoDao.seLlamoInsertar)
        assertNull(productoDao.productoRecibido)
        assertNull(productoDao.marcasRecibidas)
    }

    // [Claude] La cadena 3e solo salta al crear o mover

    // Plato ya guardado en la categoría 2, para las pruebas de editar
    private val entrecotGuardado = platoConPrecio(1850).copy(id = 7)

    // Plato nuevo en una categoría eliminada → CategoriaEliminada y no se guarda
    @Test
    fun nuevoEnCategoriaEliminadaAvisa() {
        val productoDao = ProductoDaoFalso()
        val repositorio = crearRepositorio(productoDao, CategoriaDaoFalso(activa = false))
        val resultado = runBlocking { repositorio.guardarPlato(platoConPrecio(1850), emptyList()) }
        assertEquals(ResultadoGuardado.CategoriaEliminada, resultado)
        assertNull(productoDao.productoRecibido)
    }

    // Plato que se mueve (de la categoría 2 a la 5, eliminada) → CategoriaEliminada
    @Test
    fun movidoACategoriaEliminadaAvisa() {
        val productoDao = ProductoDaoFalso(guardado = entrecotGuardado)
        val repositorio = crearRepositorio(productoDao, CategoriaDaoFalso(activa = false))
        val movido = entrecotGuardado.copy(categoriaId = 5)
        val resultado = runBlocking { repositorio.guardarPlato(movido, emptyList()) }
        assertEquals(ResultadoGuardado.CategoriaEliminada, resultado)
        assertNull(productoDao.productoRecibido)
    }

    // Plato que se edita SIN moverlo, aunque su categoría esté eliminada → Ok
    @Test
    fun editadoSinMoverSeGuarda() {
        val productoDao = ProductoDaoFalso(guardado = entrecotGuardado)
        val repositorio = crearRepositorio(productoDao, CategoriaDaoFalso(activa = false))
        val renombrado = entrecotGuardado.copy(nombre = "Entrecot de ternera")
        val resultado = runBlocking { repositorio.guardarPlato(renombrado, emptyList()) }
        assertEquals(ResultadoGuardado.Ok, resultado)
        assertEquals("Entrecot de ternera", productoDao.productoRecibido?.nombre)
    }

    // Tercera salida de la cadena: «guárdalo ahí aunque esté eliminada» → Ok, con sus alérgenos
    @Test
    fun aunqueCategoriaEliminadaSeGuarda() {
        val productoDao = ProductoDaoFalso()
        val repositorio = crearRepositorio(productoDao, CategoriaDaoFalso(activa = false))
        val resultado = runBlocking {
            repositorio.guardarPlato(platoConPrecio(1850), listOf(1L, 7L), aunqueCategoriaEliminada = true)
        }
        assertEquals(ResultadoGuardado.Ok, resultado)
        assertEquals(listOf(1L, 7L), productoDao.marcasRecibidas?.map { it.alergenoId })
    }

    // Al editar, «activo» se queda como estaba guardado: guardarPlato no elimina ni recupera
    @Test
    fun editarNoCambiaActivo() {
        val eliminado = entrecotGuardado.copy(activo = false)
        val productoDao = ProductoDaoFalso(guardado = eliminado)
        val repositorio = crearRepositorio(productoDao)
        runBlocking { repositorio.guardarPlato(eliminado.copy(activo = true, nombre = "Otro"), emptyList()) }
        assertEquals(false, productoDao.productoRecibido?.activo)
    }
}
