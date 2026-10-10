package yunkang.ako.dominio

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PinSalHashTest {

    @Test
    fun mismoPinYMismaSalCoincide() {
        val sal = PinSalHash.generarSal()
        val hash = PinSalHash.pbkdf2("1234", sal)
        assertTrue(PinSalHash.coincide("1234", sal, hash))
    }

    @Test
    fun otroPinNoCoincide() {
        val sal = PinSalHash.generarSal()
        val hash = PinSalHash.pbkdf2("1234", sal)
        assertFalse(PinSalHash.coincide("1235", sal, hash))
    }

    @Test
    fun otraSalDaOtroHash() {
        val hash1 = PinSalHash.pbkdf2("1234", PinSalHash.generarSal())
        val hash2 = PinSalHash.pbkdf2("1234", PinSalHash.generarSal())
        assertNotEquals(hash1, hash2)
    }

    // [Claude] con una sal fija la prueba da siempre lo mismo (sin azar)
    @Test
    fun elHashNoContieneElPin() {
        val salFija = java.util.Base64.getEncoder().encodeToString(ByteArray(16))
        val hash = PinSalHash.pbkdf2("1234", salFija)
        assertFalse(hash.contains("1234"))
    }
}