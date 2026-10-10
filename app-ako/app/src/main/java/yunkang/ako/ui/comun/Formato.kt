package yunkang.ako.ui.comun

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.math.abs

// [Claude] datos → el texto que se ve. no lee base de datos ni strings.xml: la pantalla le pasa lo que hace falta
// solo fecha() y hora() miran el móvil (idioma y zona horaria)
object Formato {

    // 1850 → «18,50», −250 → «−2,50». los importes van siempre en céntimos y solo se convierten aquí
    // negativo solo hay uno: el cambio de 6c cuando el cliente da de menos
    fun precio(centimos: Int): String {
        // con negativos la división entera engaña: se trabaja sin signo
        val sinSigno = abs(centimos)
        val euros = sinSigno / 100
        val resto = sinSigno % 100
        val texto = euros.toString() + "," + resto.toString().padStart(2, '0')
        // el signo va delante al final: «−» (U+2212, el de comun_menos), no el guion
        return if (centimos < 0) "\u2212" + texto else texto
    }

    // ["5", "6", "7"] y «y» → «5, 6 y 7». el conector llega de strings.xml (comun_y)
    fun lista(partes: List<String>, conector: String): String {
        if (partes.size <= 1) return partes.joinToString("")
        return partes.dropLast(1).joinToString(", ") + " " + conector + " " + partes.last()
    }

    // «18,50» → 1850: lo que teclea el Propietario, a céntimos. vale coma o punto y 0, 1 o 2 decimales
    // null si todavía no es un precio (se está tecleando): no lanza error
    // nunca pasa por Double: 0,29 saldría 28 céntimos
    fun centimosDesde(texto: String): Int? {
        val partes = texto.trim().replace('.', ',').split(',')
        if (partes.size > 2) return null
        val textoEuros = partes[0]
        val textoCentimos = if (partes.size == 2) partes[1] else ""
        // solo cifras: fuera letras, espacios y el «-»
        if (textoEuros.isEmpty() || !textoEuros.all { it in '0'..'9' }) return null
        if (textoCentimos.length > 2 || !textoCentimos.all { it in '0'..'9' }) return null
        // [Claude] como mucho 7 cifras de euros: así euros × 100 cabe en un Int
        // límite declarado: con 7 cifras, precio × cantidad o el total pueden pasar de lo que cabe en un Int
        if (textoEuros.length > 7) return null
        val euros = textoEuros.toInt()
        // «5» son 50 céntimos: se rellena con ceros por la derecha
        val centimos = textoCentimos.padEnd(2, '0').toInt()
        return euros * 100 + centimos
    }

    // 2g · el campo Día, en la forma larga del idioma del móvil («Jueves, 8 de octubre de 2026»)
    // no va en strings.xml: el orden de día y mes lo sabe Java en cada idioma
    fun fecha(dia: LocalDate): String {
        val idioma = Locale.getDefault()
        val texto = dia.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(idioma))
        // Java escribe «jueves»: se sube solo la primera letra
        return texto.replaceFirstChar { it.titlecase(idioma) }
    }

    // 2g · la hora de cobro («14:32», 24 horas), en la zona del móvil
    fun hora(instante: Long): String =
        Instant.ofEpochMilli(instante)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("HH:mm"))
}