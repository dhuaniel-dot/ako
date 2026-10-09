package yunkang.ako.dominio

import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidacionTest {

    // 1850 y 0 son válidos (si lanzaran, la prueba saldría roja); -1 lanza un error
    @Test
    fun precioNegativo() {
        Validacion.precioValido(1850)
        Validacion.precioValido(0)
        assertThrows(IllegalArgumentException::class.java) {
            Validacion.precioValido(-1)
        }
    }

    // [Claude] Cantidad entre 1 y 99
    @Test
    fun cantidadEntre1y99() {
        assertFalse(Validacion.cantidadValida(0))
        assertTrue(Validacion.cantidadValida(1))
        assertTrue(Validacion.cantidadValida(99))
        assertFalse(Validacion.cantidadValida(100))
    }

    // [Claude] El PIN son cuatro cifras
    @Test
    fun pinDeCuatroCifras() {
        assertTrue(Validacion.pinValido("1234"))
        assertFalse(Validacion.pinValido("123"))
        assertFalse(Validacion.pinValido("12345"))
        assertFalse(Validacion.pinValido("12a4"))
    }
}