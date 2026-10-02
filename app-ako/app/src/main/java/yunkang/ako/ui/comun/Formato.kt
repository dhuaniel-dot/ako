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
}