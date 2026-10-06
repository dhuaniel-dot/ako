package yunkang.ako.ui.comun

// [Claude] P68 A: una línea tal como se pinta, venga del carrito (5c) o, desde la S9, de una comanda (6b, 6c, 2g).
// Es una caja de transporte para la pantalla: cada libreta convierte sus líneas a LineaVista
data class LineaVista(
    val id: Long,              // en el carrito, el id del plato; en una comanda, el de la línea (S9)
    val cantidad: Int,
    val nombre: String,
    val importeCentimos: Int   // precio × cantidad (R10), ya calculado
)