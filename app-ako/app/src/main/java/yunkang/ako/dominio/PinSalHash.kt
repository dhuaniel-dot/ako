package yunkang.ako.dominio

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

// La picadora del PIN: calcula, no guarda nada (guardar es cosa de GuardaPin).
object PinSalHash {

    // Las vueltas de picadora, el tamaño de la pasta y el tamaño de la sal
    private const val ITERACIONES = 100_000
    private const val BITS_CLAVE = 256
    private const val BYTES_SAL = 16

    // Una sal nueva al azar, escrita como texto Base64
    fun generarSal(): String {
        val sal = ByteArray(BYTES_SAL)
        SecureRandom().nextBytes(sal)
        return Base64.getEncoder().encodeToString(sal)
    }

    // Pica el PIN con la sal: PBKDF2 con SHA-256 y 100 000 vueltas.
    // Devuelve la pasta como texto Base64.
    // [Claude] clearPassword() borra la copia del PIN que guarda la receta.
    fun pbkdf2(pin: String, sal: String): String {
        val bytesSal = Base64.getDecoder().decode(sal)
        val receta = PBEKeySpec(pin.toCharArray(), bytesSal, ITERACIONES, BITS_CLAVE)
        val picadora = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val pasta = picadora.generateSecret(receta).encoded
        receta.clearPassword()
        return Base64.getEncoder().encodeToString(pasta)
    }

    // ¿Este PIN, picado con la misma sal, da la misma pasta que la guardada?
    // [Claude] MessageDigest.isEqual compara en tiempo constante (buena práctica OWASP): tarda lo mismo
    // acierte o no, para no dar pistas por el tiempo de respuesta
    fun coincide(pin: String, sal: String, hashGuardado: String): Boolean =
        MessageDigest.isEqual(pbkdf2(pin, sal).toByteArray(), hashGuardado.toByteArray())
}