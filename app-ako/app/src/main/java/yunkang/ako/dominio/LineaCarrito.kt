package yunkang.ako.dominio

import yunkang.ako.datos.entidades.Producto

// Un renglón del carrito: un plato y cuántos van.
// Vive en memoria, no se guarda en la base de datos (R10).
data class LineaCarrito(
    val producto: Producto,
    var cantidad: Int          // cambiable (P119): solo la cambia Carrito, que respeta el tope de 99
) {
    // Lo que cuesta el renglón, en céntimos: precio × cantidad
    fun importe(): Int = Calculadora.importe(producto.precioCentimos, cantidad)
}