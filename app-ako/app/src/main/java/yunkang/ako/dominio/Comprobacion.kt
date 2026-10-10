package yunkang.ako.dominio

// las comprobaciones de las reglas. Room no las comprueba (no declara CHECK),
// así que se comprueban aquí antes de guardar
object Comprobacion {

    // máximo de unidades de un mismo plato. solo está escrito aquí
    const val MAXIMO_POR_PLATO = 99

    // cifras del PIN (ficha 1). solo está escrito aquí
    const val LONGITUD_PIN = 4

    // un precio no puede ser negativo; 0 sí vale
    // si es negativo, para en seco con un error
    fun precioValido(centimos: Int) {
        if (centimos < 0) {
            throw IllegalArgumentException("El precio no puede ser negativo: $centimos")
        }
    }

    // una cantidad vale si está entre 1 y 99
    fun cantidadValida(cantidad: Int): Boolean = cantidad in 1..MAXIMO_POR_PLATO

    // el PIN son exactamente cuatro cifras del 0 al 9
    fun pinValido(pin: String): Boolean = pin.length == LONGITUD_PIN && pin.all { it in '0'..'9' }
}