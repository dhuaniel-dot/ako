package yunkang.ako.dominio

import yunkang.ako.datos.entidades.Producto

// La libreta del camarero para una mesa mientras toma nota.
// Vive en memoria hasta que se pulsa Enviar; no se guarda en la base de datos (R10).
class Carrito(val mesaId: Long) {

    // Los renglones del carrito (lista cambiable)
    val lineas = mutableListOf<LineaCarrito>()

    // Añade un plato. Si ya está en el carrito, suma en el mismo renglón.
    // Si se pasa de 99, se queda en 99 y devuelve false para que la pantalla avise.
    fun anadir(producto: Producto, cantidad: Int): Boolean {
        // R4: menos de 1 no es una cantidad: no se añade nada y se avisa con false
        if (cantidad < 1) return false
        val maximo = Validacion.MAXIMO_POR_PLATO
        val linea = lineas.find { it.producto.id == producto.id }
        if (linea == null) {
            lineas.add(LineaCarrito(producto, minOf(cantidad, maximo)))
            return cantidad <= maximo
        }
        val nuevaCantidad = linea.cantidad + cantidad
        linea.cantidad = minOf(nuevaCantidad, maximo)
        return nuevaCantidad <= maximo
    }

    // Pone una cantidad concreta a un plato del carrito.
    // [Claude] Si no está entre 1 y 99, no hace nada.
    fun cambiarCantidad(productoId: Long, cantidad: Int) {
        if (!Validacion.cantidadValida(cantidad)) return
        val linea = lineas.find { it.producto.id == productoId } ?: return
        linea.cantidad = cantidad
    }

    // Quita el renglón de ese plato
    fun quitar(productoId: Long) {
        lineas.removeAll { it.producto.id == productoId }
    }

    // Cuántos platos lleva en total (2 Entrecot + 1 Agua = 3)
    fun numPlatos(): Int = lineas.sumOf { it.cantidad }

    // Lo que suma todo el carrito, en céntimos. Se calcula, no se guarda.
    fun total(): Int = lineas.sumOf { it.importe() }

    fun estaVacio(): Boolean = lineas.isEmpty()
}