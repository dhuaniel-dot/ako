package yunkang.ako.dominio

import yunkang.ako.datos.entidades.Producto

// una línea de la libreta: el plato y cuántos van
// vive en memoria, no se guarda en la base de datos
// cantidad es cambiable: solo la cambia Carrito, que respeta el tope de 99
data class PlatoApuntado(
    val producto: Producto,
    var cantidad: Int
) {
    // lo que cuesta la línea, en céntimos: precio × cantidad
    fun importe(): Int = Calculadora.importe(producto.precioCentimos, cantidad)
}