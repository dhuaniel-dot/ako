package yunkang.ako.dominio

import yunkang.ako.datos.entidades.Producto

// Un renglón del carrito: un plato y cuántos van.
// Vive en memoria, no se guarda en la base de datos.
// cantidad es cambiable: solo la cambia Carrito, que respeta el tope de 99.
data class LineaCarrito(
    val producto: Producto,
    var cantidad: Int
) {
    // Lo que cuesta el renglón, en céntimos: precio × cantidad
    fun importe(): Int = Calculadora.importe(producto.precioCentimos, cantidad)
}