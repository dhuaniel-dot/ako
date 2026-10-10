package yunkang.ako.ui.pedido

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import yunkang.ako.R
import yunkang.ako.databinding.ActivityPedidoBinding
import yunkang.ako.ui.comun.ComprobadorPin
import yunkang.ako.ui.comun.ConfirmacionDialog
import yunkang.ako.ui.comun.GuardaDobleToque
import yunkang.ako.ui.selector.PinDialog
import yunkang.ako.ui.comun.apartarDeLasBarras

// pantalla 5 (Pedir). es solo el marco: dentro se apilan la carta (5a), la ficha (5b) y el carrito (5c)
// sabe comprobar un PIN (ComprobadorPin): salir de Pedir lo pide (ficha 1)
class PedidoActivity : AppCompatActivity(), ComprobadorPin {

    private lateinit var binding: ActivityPedidoBinding

    // la libreta de Pedir, hecha con su fábrica; sus Fragments piden la misma con activityViewModels
    private val viewModel: PedidoViewModel by viewModels { PedidoViewModel.Factory }

    // [Claude] el guardián de Atrás: en la carta (5a), Atrás no cierra Pedir, pide el PIN
    // solo está encendido con la pila vacía; en la ficha o el carrito, Atrás vuelve a la carta
    private val guardianSalir = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            intentarSalir()
        }
    }

    // el portero de Pedir: dos toques en Salir no abren dos avisos ni dos cajas del PIN
    private val portero = GuardaDobleToque()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // de borde a borde, apartado de las barras y del teclado (como SelectorActivity)
        enableEdgeToEdge()
        binding = ActivityPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.contenedor.apartarDeLasBarras()

        // la mesa llega grapada a la nota (Intent) con la que 1d abrió esta pantalla
        viewModel.iniciar(
            intent.getLongExtra(EXTRA_MESA_ID, 0L),
            intent.getIntExtra(EXTRA_MESA_NUMERO, 0)
        )

        // la primera vez, la carta; si Android rehace la pantalla, ya está montada
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.contenedor, CartaFragment())
            }
        }

        // el guardián se apunta al botón Atrás; se enciende solo cuando la pila está vacía (se ve la carta)
        onBackPressedDispatcher.addCallback(this, guardianSalir)
        guardianSalir.isEnabled = supportFragmentManager.backStackEntryCount == 0
        supportFragmentManager.addOnBackStackChangedListener {
            guardianSalir.isEnabled = supportFragmentManager.backStackEntryCount == 0
        }

        // si en el aviso de platos sin enviar se pulsa «Salir», se sigue al PIN;
        // «Cancelar» (o tocar fuera) no hace nada
        supportFragmentManager.setFragmentResultListener(CLAVE_SALIR, this) { _, sobre ->
            if (sobre.getBoolean(ConfirmacionDialog.RESPUESTA_AFIRMATIVA)) pedirPin()
        }

        // cuando 1c deja el sobre de «PIN correcto», se sale de Pedir: se vuelve a 1a (ficha 1)
        supportFragmentManager.setFragmentResultListener(PinDialog.CLAVE_RESULTADO, this) { _, _ ->
            finish()
        }
    }

    // [Claude] salir de Pedir (ficha 1): primero el aviso si hay platos sin enviar, después el PIN
    // con el carrito vacío, directamente el PIN
    private fun intentarSalir() {
        if (!portero.permite()) return
        val carrito = viewModel.carrito.value
        if (carrito == null || carrito.estaVacio()) {
            pedirPin()
            return
        }
        // «Hay 1 plato… Se perderá» / «Hay 3 platos… Se perderán». el número va dos veces:
        // una para elegir la frase y otra para escribirlo en ella
        val n = carrito.numPlatos()
        ConfirmacionDialog.nueva(
            titulo = getString(R.string.salir_sin_enviar_titulo),
            texto = resources.getQuantityString(R.plurals.salir_sin_enviar_cuerpo, n, n),
            afirmativo = getString(R.string.carta_btn_salir),
            negativo = getString(R.string.comun_cancelar),
            clave = CLAVE_SALIR
        ).show(supportFragmentManager, CLAVE_SALIR)
    }

    // en el nivel 1, salir de Pedir siempre pide el PIN. cancelar deja la carta abierta
    // si se sale, el carrito no se vacía a mano: muere con la libreta al cerrarse Pedir
    private fun pedirPin() {
        PinDialog().show(supportFragmentManager, "pin")
    }

    // PinDialog pregunta aquí; esta pantalla delega en su libreta
    override suspend fun comprobarPin(pin: String): Boolean = viewModel.comprobarPin(pin)

    companion object {
        const val EXTRA_MESA_ID = "mesa_id"
        const val EXTRA_MESA_NUMERO = "mesa_numero"

        private const val CLAVE_SALIR = "salir_sin_enviar"
    }
}