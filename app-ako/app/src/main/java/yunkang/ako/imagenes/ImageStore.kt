package yunkang.ako.imagenes

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.UUID
import kotlin.math.roundToInt

// Lado mayor de las fotos guardadas (RNF-18)
private const val LADO_FINAL = 1080

// Calidad del JPEG: mucho menos peso sin que se note a simple vista (RNF-18)
private const val CALIDAD_JPEG = 85

// La carpeta de las fotos dentro del almacenamiento privado de la app
private const val CARPETA = "fotos"

// Guarda las fotos elegidas como archivos JPEG en la carpeta privada de la app (archivo, nunca BLOB).
// Recibe el contexto de la app (EntradaAko), nunca el de una pantalla.
class ImageStore(private val context: Context) {

    // Pide a Android que el préstamo de la foto no caduque, porque se copia al guardar
    // (documentación oficial del selector de fotos, «Persist media file access»)
    fun conservarPrestamo(uri: Uri) {
        try {
            context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } catch (e: SecurityException) {
            // [Claude] Algún selector antiguo no lo permite: se sigue con el préstamo normal
        }
    }

    // Copia la foto prestada a un archivo nuestro, reducida y derecha, y devuelve su ruta.
    // Todo en Dispatchers.IO: leer y escribir fotos tarda y congelaría la pantalla.
    suspend fun guardar(uri: Uri): String = withContext(Dispatchers.IO) {
        val giro = leerGiro(uri)
        var foto = leerReducida(uri)

        // [Claude] Primero se reduce a 1080 y después se gira: girar una foto pequeña gasta menos memoria.
        // Si ya es más pequeña, no se agranda.
        val ladoMayor = maxOf(foto.width, foto.height)
        if (ladoMayor > LADO_FINAL) {
            val escala = LADO_FINAL.toFloat() / ladoMayor
            val ancho = (foto.width * escala).roundToInt()
            val alto = (foto.height * escala).roundToInt()
            foto = Bitmap.createScaledBitmap(foto, ancho, alto, true)
        }
        if (giro != 0) {
            val matriz = Matrix()
            matriz.postRotate(giro.toFloat())
            foto = Bitmap.createBitmap(foto, 0, 0, foto.width, foto.height, matriz, true)
        }

        // [Claude] Nombre único: Glide recuerda las fotos por su ruta y con el mismo nombre enseñaría la vieja
        val carpeta = File(context.filesDir, CARPETA)
        carpeta.mkdirs()
        val archivo = File(carpeta, "${UUID.randomUUID()}.jpg")
        val escrita = archivo.outputStream().use { foto.compress(Bitmap.CompressFormat.JPEG, CALIDAD_JPEG, it) }
        if (!escrita) {
            archivo.delete()
            throw IOException("No se ha podido escribir la foto")
        }
        archivo.absolutePath
    }

    // Quita un archivo de foto que ya no usa ninguna fila (cambiar la foto, o un guardado que no salió).
    // [Claude] Solo borra dentro de fotos/: nunca un archivo de fuera.
    // [Claude] Sin Dispatchers.IO: borrar es quitar el nombre del archivo, no leer la foto; es instantáneo.
    fun borrar(ruta: String) {
        val archivo = File(ruta).canonicalFile
        val carpeta = File(context.filesDir, CARPETA).canonicalFile
        if (archivo.parentFile == carpeta) {
            archivo.delete()
        }
    }

    // Lee la foto ya reducida, sin cargarla entera en memoria (guía «Loading Large Bitmaps Efficiently»).
    // La lee dos veces: primero solo las medidas, sin píxeles; después los píxeles, quedándose con uno de cada N
    private fun leerReducida(uri: Uri): Bitmap {
        val medidas = BitmapFactory.Options()
        medidas.inJustDecodeBounds = true
        abrir(uri).use { BitmapFactory.decodeStream(it, null, medidas) }
        if (medidas.outWidth <= 0 || medidas.outHeight <= 0) throw IOException("No es una imagen")

        val opciones = BitmapFactory.Options()
        opciones.inSampleSize = calcularMuestreo(medidas.outWidth, medidas.outHeight)
        return abrir(uri).use { BitmapFactory.decodeStream(it, null, opciones) }
            ?: throw IOException("No es una imagen")
    }

    // [Claude] La potencia de 2 más grande que deja el lado mayor en LADO_FINAL o más
    private fun calcularMuestreo(ancho: Int, alto: Int): Int {
        val ladoMayor = maxOf(ancho, alto)
        var muestreo = 1
        while (ladoMayor / (muestreo * 2) >= LADO_FINAL) {
            muestreo *= 2
        }
        return muestreo
    }

    // Cuántos grados hay que girar la foto para verla derecha (marca EXIF del móvil)
    private fun leerGiro(uri: Uri): Int {
        val orientacion = abrir(uri).use {
            ExifInterface(it).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        }
        return when (orientacion) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    // Abre la foto que prestó el selector; quien la abre la cierra con use
    private fun abrir(uri: Uri) =
        context.contentResolver.openInputStream(uri) ?: throw IOException("No se puede abrir la foto")
}