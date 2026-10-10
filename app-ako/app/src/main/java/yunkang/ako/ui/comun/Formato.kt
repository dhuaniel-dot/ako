package yunkang.ako.ui.comun

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.math.abs

// [Claude] convierte datos en el texto que se ve. funciones puras: no leen nada de fuera
// (ni base de datos ni strings.xml); la pantalla les pasa lo que necesitan
// solo fecha() y hora() miran el móvil: su idioma y su zona horaria
object Formato {

    // 1850 → «18,50» y −250 → «−2,50». la pantalla lo mete en comun_precio y sale «18,50 €»
    // los importes viajan siempre en céntimos (Int) y solo se convierten aquí, al pintar
    // el único importe negativo de la app es el cambio de 6c cuando el cliente da de menos
    fun precio(centimos: Int): String {
        // se trabaja con el valor sin signo, porque con negativos la división entera engaña:
        // −250 / 100 = −2 y −250 % 100 = −50, y saldría «−2,−50». abs(−250) = 250 → «2,50»
        val sinSigno = abs(centimos)
        val euros = sinSigno / 100
        val resto = sinSigno % 100
        val texto = euros.toString() + "," + resto.toString().padStart(2, '0')
        // el signo se pone delante al final: «−» (U+2212, el menos de comun_menos), no el guion «-»
        return if (centimos < 0) "\u2212" + texto else texto
    }

    // une partes con comas y el conector antes de la última: ["5", "6", "7"] y «y» → «5, 6 y 7»
    // el conector llega de strings.xml (comun_y), así que aquí no se escribe ninguna palabra
    fun lista(partes: List<String>, conector: String): String {
        if (partes.size <= 1) return partes.joinToString("")
        return partes.dropLast(1).joinToString(", ") + " " + conector + " " + partes.last()
    }

    // «18,50» → 1850. es el camino de vuelta de precio(): lo que teclea el Propietario a céntimos
    // acepta coma o punto y 0, 1 o 2 decimales («18», «18,5», «18.50»)
    // devuelve null si el texto no es un precio («», «1,234», «-1», «1,2,3», «abc»): mientras
    // se teclea es normal que todavía no lo sea, por eso no lanza ningún error
    // nunca pasa por Double: 0,29 en coma flotante es 0,28999… y saldrían 28 céntimos
    fun centimosDesde(texto: String): Int? {
        val partes = texto.trim().replace('.', ',').split(',')
        if (partes.size > 2) return null
        val textoEuros = partes[0]
        val textoCentimos = if (partes.size == 2) partes[1] else ""
        // solo cifras del 0 al 9: así se quedan fuera las letras, los espacios y el signo «-»
        if (textoEuros.isEmpty() || !textoEuros.all { it in '0'..'9' }) return null
        if (textoCentimos.length > 2 || !textoCentimos.all { it in '0'..'9' }) return null
        // [Claude] como mucho 7 cifras de euros (9.999.999 €): así euros × 100 siempre cabe en un Int
        // límite declarado: con un precio de siete cifras, precio × cantidad o el total de una
        // comanda pueden pasar de 21.474.836,47 € (lo más que cabe en un Int) y saldrían mal
        if (textoEuros.length > 7) return null
        val euros = textoEuros.toInt()
        // «5» son 50 céntimos, no 5: se rellena con ceros por la derecha («5» → «50», «» → «00»)
        val centimos = textoCentimos.padEnd(2, '0').toInt()
        return euros * 100 + centimos
    }

    // 2g · el campo Día, en la forma larga del idioma del móvil:
    // en español «Jueves, 8 de octubre de 2026» y en inglés «Thursday, October 8, 2026»
    // no es una cadena de strings.xml: el orden de día y mes lo sabe Java para cada idioma,
    // y así nadie lee el 8 de octubre como el 10 de agosto
    fun fecha(dia: LocalDate): String {
        val idioma = Locale.getDefault()
        val texto = dia.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(idioma))
        // Java escribe «jueves» y el dibujo dice «Jueves»: se sube solo la primera letra
        return texto.replaceFirstChar { it.titlecase(idioma) }
    }

    // 2g · la hora de cobro: un instante (fechaCierre, milisegundos desde 1970) → «14:32», con 24 horas
    // el mismo instante es las 14:32 en Madrid y las 12:32 en Londres: se escribe en la zona del móvil
    fun hora(instante: Long): String =
        Instant.ofEpochMilli(instante)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("HH:mm"))
}