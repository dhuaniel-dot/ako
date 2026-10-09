package yunkang.ako.ui.comun

// [Claude] Una línea tal como se pinta, venga del carrito (5c) o de una comanda (6b, 6c, 2g).
// Es una caja de transporte para la pantalla: cada libreta convierte sus líneas a LineaVista.
// El id es el del plato en el carrito y el de la línea en una comanda; el importe es precio × cantidad (R10), ya calculado
data class LineaVista(
    val id: Long,
    val cantidad: Int,
    val nombre: String,
    val importeCentimos: Int
)