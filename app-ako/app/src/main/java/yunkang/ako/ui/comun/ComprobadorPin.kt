package yunkang.ako.ui.comun

// [Claude] P145 (revisión del 1 oct, antes P71 → A): «quien sabe comprobar un PIN».
// Lo cumple cada Activity que abre PinDialog (SelectorActivity y PedidoActivity, al salir de Pedir),
// delegando en su propio ViewModel. Así el diálogo no depende de qué pantalla lo abrió.
interface ComprobadorPin {
    suspend fun comprobarPin(pin: String): Boolean
}
