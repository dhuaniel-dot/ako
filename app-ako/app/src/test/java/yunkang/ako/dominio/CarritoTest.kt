package yunkang.ako.dominio

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import yunkang.ako.datos.entidades.Producto

class CarritoTest {

    // Platos de prueba: solo importan el id y el precio
    private val entrecot = Producto(id = 1, categoriaId = 1, numero = 10, nombre = "Entrecot",
        descripcion = null, precioCentimos = 1850, imagen = null, activo = true)
    private val agua = Producto(id = 2, categoriaId = 1, numero = 1, nombre = "Agua",
        descripcion = null, precioCentimos = 500, imagen = null, activo = true)

    // P-C-01: importe de una línea
    @Test
    fun importeDeUnaLinea() {
        val linea = LineaCarrito(entrecot, 3)
        assertEquals(5550, linea.importe())
    }

    // P-C-02: total del carrito, vacío y con líneas
    @Test
    fun totalDelCarrito() {
        val carrito = Carrito(mesaId = 4)

        assertEquals(0, carrito.total())
        assertTrue(carrito.estaVacio())
        assertEquals(0, carrito.numPlatos())

        carrito.anadir(entrecot, 2)
        carrito.anadir(agua, 1)

        assertEquals(4200, carrito.total())
        assertEquals(3, carrito.numPlatos())
        assertFalse(carrito.estaVacio())
    }

    // P-C-03: líneas idénticas se suman y el tope es 99
    @Test
    fun lineasIdenticasSeSumanYTope99() {
        val carrito = Carrito(mesaId = 4)

        carrito.anadir(entrecot, 2)
        carrito.anadir(entrecot, 1)
        assertEquals(1, carrito.lineas.size)
        assertEquals(3, carrito.lineas[0].cantidad)

        carrito.cambiarCantidad(entrecot.id, 99)
        val cupo = carrito.anadir(entrecot, 1)
        assertEquals(99, carrito.lineas[0].cantidad)
        assertFalse(cupo)   // P74: avisa de que no cabía
    }
}