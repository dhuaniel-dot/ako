package yunkang.ako.dominio

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

// La picadora del PIN: calcula, no guarda nada (guardar es cosa de PinStore, S4).
object Hash {

    private const val ITERACIONES = 100_000   // vueltas de picadora (P126)
    private const val BITS_CLAVE = 256        // tamaño de la pasta
    private const val BYTES_SAL = 16          // tamaño de la sal

    // Una sal nueva al azar, escrita como texto Base64 (P122)
    fun generarSal(): String {
        val sal = ByteArray(BYTES_SAL)
        SecureRandom().nextBytes(sal)
        return Base64.getEncoder().encodeToString(sal)
    }

    // Pica el PIN con la sal: PBKDF2 con SHA-256 y 100 000 vueltas.
    // Devuelve la pasta como texto Base64.
    fun pbkdf2(pin: String, sal: String): String {
        val bytesSal = Base64.getDecoder().decode(sal)
        val receta = PBEKeySpec(pin.toCharArray(), bytesSal, ITERACIONES, BITS_CLAVE)
        val picadora = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val pasta = picadora.generateSecret(receta).encoded
        return Base64.getEncoder().encodeToString(pasta)
    }

    // ¿Este PIN, picado con la misma sal, da la misma pasta que la guardada?
    fun coincide(pin: String, sal: String, hashGuardado: String): Boolean =
        pbkdf2(pin, sal) == hashGuardado
}