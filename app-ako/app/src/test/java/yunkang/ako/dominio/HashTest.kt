package yunkang.ako.dominio

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

// P-C-05: el hash del PIN
class HashTest {

    @Test
    fun mismoPinYMismaSalCoincide() {
        val sal = Hash.generarSal()
        val hash = Hash.pbkdf2("1234", sal)
        assertTrue(Hash.coincide("1234", sal, hash))
    }

    @Test
    fun otroPinNoCoincide() {
        val sal = Hash.generarSal()
        val hash = Hash.pbkdf2("1234", sal)
        assertFalse(Hash.coincide("1235", sal, hash))
    }

    @Test
    fun otraSalDaOtroHash() {
        val hash1 = Hash.pbkdf2("1234", Hash.generarSal())
        val hash2 = Hash.pbkdf2("1234", Hash.generarSal())
        assertNotEquals(hash1, hash2)
    }

    // [Claude] Con una sal fija la prueba da siempre lo mismo (sin azar, H22)
    @Test
    fun elHashNoContieneElPin() {
        val salFija = java.util.Base64.getEncoder().encodeToString(ByteArray(16))
        val hash = Hash.pbkdf2("1234", salFija)
        assertFalse(hash.contains("1234"))
    }
}