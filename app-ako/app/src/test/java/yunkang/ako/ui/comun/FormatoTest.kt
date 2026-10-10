package yunkang.ako.ui.comun

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

// fecha() y hora() no se prueban aquí: dependen del idioma y la zona del ordenador que pase la prueba
class FormatoTest {

    // siempre dos decimales con coma, y el menos de un negativo es «−» (U+2212), no el guion del teclado
    @Test
    fun precio() {
        assertEquals("0,00", Formato.precio(0))
        assertEquals("0,05", Formato.precio(5))
        assertEquals("18,50", Formato.precio(1850))
        assertEquals("\u22122,50", Formato.precio(-250))
    }

    // lo que teclea el Propietario: coma o punto y hasta 2 decimales; lo que no es un precio da null
    @Test
    fun centimosDesde() {
        assertEquals(1850, Formato.centimosDesde("18,50"))
        assertEquals(1850, Formato.centimosDesde("18.5"))
        assertEquals(1800, Formato.centimosDesde("18"))
        assertEquals(500, Formato.centimosDesde("5,"))
        assertEquals(999999900, Formato.centimosDesde("9999999"))
        assertNull(Formato.centimosDesde(",5"))
        assertNull(Formato.centimosDesde("1,234"))
        assertNull(Formato.centimosDesde("-1"))
        assertNull(Formato.centimosDesde("abc"))
        assertNull(Formato.centimosDesde(""))
        assertNull(Formato.centimosDesde("10000000"))
        assertNull(Formato.centimosDesde("1,2,3"))
    }

    // comas entre las partes y el conector solo antes de la última; con una o ninguna, sin conector
    @Test
    fun lista() {
        assertEquals("", Formato.lista(emptyList(), "y"))
        assertEquals("5", Formato.lista(listOf("5"), "y"))
        assertEquals("5 y 6", Formato.lista(listOf("5", "6"), "y"))
        assertEquals("5, 6 y 7", Formato.lista(listOf("5", "6", "7"), "y"))
    }
}
