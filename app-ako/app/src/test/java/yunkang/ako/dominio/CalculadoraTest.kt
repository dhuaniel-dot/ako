package yunkang.ako.dominio

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculadoraTest {

    // P-C-04: el cambio es entregado − total, y puede salir negativo
    @Test
    fun cambio() {
        assertEquals(800, Calculadora.cambio(totalCentimos = 4200, entregadoCentimos = 5000))
        assertEquals(-200, Calculadora.cambio(totalCentimos = 4200, entregadoCentimos = 4000))
        assertEquals(0, Calculadora.cambio(totalCentimos = 4200, entregadoCentimos = 4200))
    }
}