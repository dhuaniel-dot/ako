package yunkang.ako.dominio


// Las cuentas del cobro. No guarda nada: entran números, sale un número.
object Calculadora {

    // Lo que cuesta un renglón, en céntimos: precio × cantidad.
    // La usan el carrito (ahora) y el recibo (S9) (P120).
    fun importe(precioCentimos: Int, cantidad: Int): Int = precioCentimos * cantidad

    // Lo que hay que devolver al cliente: entregado − total.
    // Si sale negativo, el cliente ha dado de menos; no es un error.
    fun cambio(totalCentimos: Int, entregadoCentimos: Int): Int = entregadoCentimos - totalCentimos
}