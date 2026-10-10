package yunkang.ako.imagenes

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.core.graphics.scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.UUID
import kotlin.math.roundToInt

// lado mayor de las fotos, que se guardan reducidas
private const val LADO_FINAL = 1080

// calidad del JPEG: mucho menos peso sin que se note a simple vista
private const val CALIDAD_JPEG = 85

// la carpeta de las fotos dentro del almacenamiento privado de la app
private const val CARPETA = "fotos"

// guarda las fotos elegidas como archivos JPEG en la carpeta privada de la app (archivo, nunca BLOB)
// recibe el contexto de la app (EntradaAko), nunca el de una pantalla
class Galeria(private val context: Context) {

    // pide a Android que el préstamo de la foto no caduque, porque se copia al guardar
    // (documentación oficial del selector de fotos, «Persist media file access»)
    fun conservarPrestamo(uri: Uri) {
        try {
            context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } catch (e: SecurityException) {
            // [Claude] algún selector antiguo no lo permite: se sigue con el préstamo normal
        }
    }

    // copia la foto prestada a un archivo nuestro, reducida y derecha, y devuelve su ruta
    // todo en Dispatchers.IO: leer y escribir fotos tarda y congelaría la pantalla
    suspend fun guardar(uri: Uri): String = withContext(Dispatchers.IO) {
        val giro = leerGiro(uri)
        var foto = leerReducida(uri)

        // [Claude] primero se reduce y después se gira: girar una foto pequeña gasta menos memoria
        // si ya es más pequeña, no se agranda
        val ladoMayor = maxOf(foto.width, foto.height)
        if (ladoMayor > LADO_FINAL) {
            val escala = LADO_FINAL.toFloat() / ladoMayor
            val ancho = (foto.width * escala).roundToInt()
            val alto = (foto.height * escala).roundToInt()
            foto = foto.scale(ancho, alto)
        }
        if (giro != 0) {
            val matriz = Matrix()
            matriz.postRotate(giro.toFloat())
            foto = Bitmap.createBitmap(foto, 0, 0, foto.width, foto.height, matriz, true)
        }

        // [Claude] nombre único: Glide recuerda las fotos por su ruta y con el mismo nombre enseñaría la vieja
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

    // quita un archivo de foto que ya no usa ninguna fila (cambiar la foto, o un guardado que no salió)
    // [Claude] solo borra dentro de fotos/: nunca un archivo de fuera
    // [Claude] sin Dispatchers.IO: borrar es quitar el nombre del archivo, no leer la foto; es instantáneo
    fun borrar(ruta: String) {
        val archivo = File(ruta).canonicalFile
        val carpeta = File(context.filesDir, CARPETA).canonicalFile
        if (archivo.parentFile == carpeta) {
            archivo.delete()
        }
    }

    // lee la foto ya reducida, sin cargarla entera en memoria (guía «Loading Large Bitmaps Efficiently»)
    // la lee dos veces: primero solo las medidas, sin píxeles; después los píxeles, quedándose con uno de cada N
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

    // [Claude] la potencia de 2 más grande que deja el lado mayor en LADO_FINAL o más
    private fun calcularMuestreo(ancho: Int, alto: Int): Int {
        val ladoMayor = maxOf(ancho, alto)
        var muestreo = 1
        while (ladoMayor / (muestreo * 2) >= LADO_FINAL) {
            muestreo *= 2
        }
        return muestreo
    }

    // cuántos grados hay que girar la foto para verla derecha (marca EXIF del móvil)
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

    // abre la foto que prestó el selector; quien la abre la cierra con use
    private fun abrir(uri: Uri) =
        context.contentResolver.openInputStream(uri) ?: throw IOException("No se puede abrir la foto")
}