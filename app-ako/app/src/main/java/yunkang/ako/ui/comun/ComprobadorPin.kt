package yunkang.ako.ui.comun

// lo cumple cada Activity que abre PinDialog (SelectorActivity y PedidoActivity, al salir de Pedir),
// delegando en su propio ViewModel. así el diálogo no depende de qué pantalla lo abrió
interface ComprobadorPin {
    suspend fun comprobarPin(pin: String): Boolean
}
