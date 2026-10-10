package yunkang.ako.dominio

// las cuentas del cobro. no guarda nada: entran números, sale un número
object Calculadora {

    // lo que cuesta un renglón, en céntimos: precio × cantidad
    // la usan el carrito y el recibo
    fun importe(precioCentimos: Int, cantidad: Int): Int = precioCentimos * cantidad

    // lo que hay que devolver al cliente: entregado − total
    // si sale negativo, el cliente ha dado de menos; no es un error
    fun cambio(totalCentimos: Int, entregadoCentimos: Int): Int = entregadoCentimos - totalCentimos
}