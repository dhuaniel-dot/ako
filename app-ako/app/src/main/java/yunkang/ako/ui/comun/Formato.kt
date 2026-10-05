package yunkang.ako.ui.comun

// [Claude] Convierte datos en el texto que se ve. Funciones puras: no leen nada de fuera
// (ni base de datos ni strings.xml); la pantalla les pasa lo que necesitan
object Formato {

    // 1850 → «18,50». La pantalla lo mete en comun_precio y sale «18,50 €».
    // Los importes viajan siempre en céntimos (Int) y solo se convierten aquí, al pintar
    fun precio(centimos: Int): String {
        // [Claude] Hoy ningún precio es negativo (Validacion lo impide); el cambio negativo
        // de Cuenta llega en la S9 y entonces se amplía esta función (nada «por si acaso»)
        require(centimos >= 0)
        val euros = centimos / 100          // división entera: 1805 / 100 = 18
        val resto = centimos % 100          // lo que sobra: 1805 % 100 = 5
        // padStart rellena con ceros a la izquierda hasta 2 cifras: 5 → «05»
        return euros.toString() + "," + resto.toString().padStart(2, '0')
    }

    // Une partes con comas y el conector antes de la última: ["5", "6", "7"] y «y» → «5, 6 y 7».
    // El conector llega de strings.xml (comun_y), así que aquí no se escribe ninguna palabra
    fun lista(partes: List<String>, conector: String): String {
        if (partes.size <= 1) return partes.joinToString("")   // 0 partes → «», 1 → ella sola
        return partes.dropLast(1).joinToString(", ") + " " + conector + " " + partes.last()
    }

    // «18,50» → 1850. Es el camino de vuelta de precio(): lo que teclea el Propietario a céntimos.
    // Acepta coma o punto y 0, 1 o 2 decimales («18», «18,5», «18.50»).
    // Devuelve null si el texto no es un precio («», «1,234», «-1», «1,2,3», «abc»): mientras
    // se teclea es normal que todavía no lo sea, por eso no lanza ningún error.
    // Nunca pasa por Double: 0,29 en coma flotante es 0,28999… y saldrían 28 céntimos
    fun centimosDesde(texto: String): Int? {
        val partes = texto.trim().replace('.', ',').split(',')   // «18,50» → ["18", "50"]
        if (partes.size > 2) return null                          // dos separadores: «1,2,3»
        val textoEuros = partes[0]
        val textoCentimos = if (partes.size == 2) partes[1] else ""
        // Solo cifras del 0 al 9: así se quedan fuera las letras, los espacios y el signo «-»
        if (textoEuros.isEmpty() || !textoEuros.all { it in '0'..'9' }) return null
        if (textoCentimos.length > 2 || !textoCentimos.all { it in '0'..'9' }) return null
        // [Claude] Como mucho 7 cifras de euros (9.999.999 €): así euros × 100 siempre cabe en un Int
        if (textoEuros.length > 7) return null
        val euros = textoEuros.toInt()
        // «5» son 50 céntimos, no 5: se rellena con ceros por la derecha («5» → «50», «» → «00»)
        val centimos = textoCentimos.padEnd(2, '0').toInt()
        return euros * 100 + centimos
    }
}