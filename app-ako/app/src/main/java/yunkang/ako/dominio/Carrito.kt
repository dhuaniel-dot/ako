package yunkang.ako.dominio

import yunkang.ako.datos.entidades.Producto

// la libreta del camarero para una mesa mientras toma nota
// vive en memoria hasta que se pulsa Enviar; no se guarda en la base de datos
class Carrito(val mesaId: Long) {

    val lineas = mutableListOf<PlatoApuntado>()

    // si el plato ya está en el carrito, suma en la misma línea
    // si se pasa de 99, se queda en 99 y devuelve false para que la pantalla avise
    // con menos de 1 no se añade nada y también devuelve false
    fun anadir(producto: Producto, cantidad: Int): Boolean {
        if (cantidad < 1) return false
        val maximo = Comprobacion.MAXIMO_POR_PLATO
        val linea = lineas.find { it.producto.id == producto.id }
        if (linea == null) {
            lineas.add(PlatoApuntado(producto, minOf(cantidad, maximo)))
            return cantidad <= maximo
        }
        val nuevaCantidad = linea.cantidad + cantidad
        linea.cantidad = minOf(nuevaCantidad, maximo)
        return nuevaCantidad <= maximo
    }

    // [Claude] si la cantidad no está entre 1 y 99, no hace nada
    fun cambiarCantidad(productoId: Long, cantidad: Int) {
        if (!Comprobacion.cantidadValida(cantidad)) return
        val linea = lineas.find { it.producto.id == productoId } ?: return
        linea.cantidad = cantidad
    }

    fun quitar(productoId: Long) {
        lineas.removeAll { it.producto.id == productoId }
    }

    // cuántos platos lleva en total (2 Entrecot + 1 Agua = 3)
    fun numPlatos(): Int = lineas.sumOf { it.cantidad }

    // lo que suma todo el carrito, en céntimos. se calcula, no se guarda
    fun total(): Int = lineas.sumOf { it.importe() }

    fun estaVacio(): Boolean = lineas.isEmpty()
}