package yunkang.ako.dominio

import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class ComprobacionTest {

    // 1850 y 0 son válidos (si lanzaran, la prueba saldría roja); -1 lanza un error
    @Test
    fun precioValidoYNegativo() {
        Comprobacion.precioValido(1850)
        Comprobacion.precioValido(0)
        assertThrows(IllegalArgumentException::class.java) {
            Comprobacion.precioValido(-1)
        }
    }

    @Test
    fun cantidadEntre1y99() {
        assertFalse(Comprobacion.cantidadValida(0))
        assertTrue(Comprobacion.cantidadValida(1))
        assertTrue(Comprobacion.cantidadValida(99))
        assertFalse(Comprobacion.cantidadValida(100))
    }

    @Test
    fun pinDeCuatroCifras() {
        assertTrue(Comprobacion.pinValido("1234"))
        assertFalse(Comprobacion.pinValido("123"))
        assertFalse(Comprobacion.pinValido("12345"))
        assertFalse(Comprobacion.pinValido("12a4"))
        assertFalse(Comprobacion.pinValido(""))
        assertFalse(Comprobacion.pinValido("12 4"))
    }
}