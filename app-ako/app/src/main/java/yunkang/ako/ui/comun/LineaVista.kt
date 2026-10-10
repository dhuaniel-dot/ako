package yunkang.ako.ui.comun

import yunkang.ako.datos.entidades.LineaComanda
import yunkang.ako.dominio.Calculadora

// [Claude] una línea tal como se pinta, venga del carrito (5c) o de una comanda (6b, 6c, 2g)
// cada libreta convierte sus líneas a LineaVista
// el id es el del plato en el carrito y el de la línea en una comanda; el importe es precio × cantidad, ya calculado
data class LineaVista(
    val id: Long,
    val cantidad: Int,
    val nombre: String,
    val importeCentimos: Int
) {
    companion object {
        // con el nombre y el precio congelados al enviar
        fun de(linea: LineaComanda) = LineaVista(
            linea.id, linea.cantidad, linea.nombreProducto,
            Calculadora.importe(linea.precioUnitarioCentimos, linea.cantidad)
        )
    }
}