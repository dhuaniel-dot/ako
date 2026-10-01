package yunkang.ako.datos.repositorios

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import yunkang.ako.datos.entidades.Producto
import yunkang.ako.dominio.modelos.ResultadoGuardado

// P-C-09 (segunda mitad): guardarPlato con actores en vez de base de datos, sin emulador.
class CartaRepositoryTest {

    // Un plato nuevo (id 0) con el precio que diga cada prueba.
    private fun platoConPrecio(centimos: Int) = Producto(
        categoriaId = 2, numero = 12, nombre = "Entrecot", descripcion = null,
        precioCentimos = centimos, imagen = null, activo = true
    )

    // El repositorio de verdad, pero con actores en vez de DAOs.
    private fun crearRepositorio(productoDao: ProductoDaoFalso) = CartaRepository(
        CategoriaDaoFalso(), productoDao, PrecargadosDaoFalso(), ComandaRepositoryFalso()
    )

    // P-C-09: 18,50 € se guarda
    @Test
    fun precioNormalSeGuarda() {
        val productoDao = ProductoDaoFalso()
        val repositorio = crearRepositorio(productoDao)
        val resultado = runBlocking { repositorio.guardarPlato(platoConPrecio(1850), emptyList()) }
        assertEquals(ResultadoGuardado.Ok, resultado)
        assertTrue(productoDao.seLlamoInsertar)
    }

    // P-C-09: 0 € se guarda (un plato gratis es válido)
    @Test
    fun precioCeroSeGuarda() {
        val productoDao = ProductoDaoFalso()
        val repositorio = crearRepositorio(productoDao)
        val resultado = runBlocking { repositorio.guardarPlato(platoConPrecio(0), emptyList()) }
        assertEquals(ResultadoGuardado.Ok, resultado)
        assertTrue(productoDao.seLlamoInsertar)
    }

    // P-C-09: −1,00 € lanza un error y el DAO no recibe nada
    @Test
    fun precioNegativoLanzaYNoGuarda() {
        val productoDao = ProductoDaoFalso()
        val repositorio = crearRepositorio(productoDao)
        assertThrows(IllegalArgumentException::class.java) {
            runBlocking { repositorio.guardarPlato(platoConPrecio(-100), emptyList()) }
        }
        assertFalse(productoDao.seLlamoInsertar)
    }
}