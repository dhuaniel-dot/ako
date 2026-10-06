package yunkang.ako.ui.pedido

import androidx.lifecycle.ViewModel

// La libreta de la pantalla 5 (Pedir): la comparten la carta (5a), la ficha (5b) y el carrito (5c).
// Vive mientras dura Pedir y muere con PedidoActivity (spec 3). Crece pieza a pieza
class PedidoViewModel : ViewModel() {

    // La mesa para la que se pide: el id va a la base de datos; el número, a la barra («Mesa 7»).
    // Solo la libreta los cambia (private set)
    var mesaId: Long = 0L
        private set
    var mesaNumero: Int = 0
        private set

    // P76 A: la Activity la fija al abrirse. Si Android rehace la pantalla, la libreta ya la tiene y no se repite
    fun iniciar(mesaId: Long, mesaNumero: Int) {
        if (this.mesaNumero != 0) return
        this.mesaId = mesaId
        this.mesaNumero = mesaNumero
    }
}