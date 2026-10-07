package yunkang.ako.ui.pedido

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import yunkang.ako.R
import yunkang.ako.databinding.ActivityPedidoBinding
import yunkang.ako.ui.comun.ComprobadorPin
import yunkang.ako.ui.comun.ConfirmacionDialog
import yunkang.ako.ui.comun.GuardaDobleToque
import yunkang.ako.ui.selector.PinDialog

// Pantalla 5 (Pedir). Es solo el marco: dentro se apilan la carta (5a), la ficha (5b) y el carrito (5c).
// Sabe comprobar un PIN (ComprobadorPin, P145): salir de Pedir lo pide (ficha 1)
class PedidoActivity : AppCompatActivity(), ComprobadorPin {

    // El "mando" de las vistas de activity_pedido.xml (ViewBinding)
    private lateinit var binding: ActivityPedidoBinding

    // La libreta de Pedir, hecha con su fábrica (P140); sus Fragments piden la misma con activityViewModels
    private val viewModel: PedidoViewModel by viewModels { PedidoViewModel.Factory }

    // [Claude] El guardián de Atrás: en la carta (5a), Atrás no cierra Pedir, pide el PIN.
    // Solo está encendido con la pila vacía; en la ficha o el carrito, Atrás vuelve a la carta
    private val guardianSalir = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            intentarSalir()
        }
    }

    // H01 B (P171): el portero de Pedir; dos toques en Salir no abren dos avisos ni dos cajas del PIN
    private val portero = GuardaDobleToque()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // P139: de borde a borde, y el contenedor se aparta de las barras del sistema y del teclado (H25)
        enableEdgeToEdge()
        binding = ActivityPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.contenedor) { vista, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            vista.setPadding(barras.left, barras.top, barras.right, barras.bottom)
            insets
        }

        // P76 A: la mesa llega grapada a la nota (Intent) con la que 1d abrió esta pantalla
        viewModel.iniciar(
            intent.getLongExtra(EXTRA_MESA_ID, 0L),
            intent.getIntExtra(EXTRA_MESA_NUMERO, 0)
        )

        // La primera vez, la carta; si Android rehace la pantalla, ya está montada
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.contenedor, CartaFragment())
            }
        }

        // El guardián se apunta al botón Atrás; se enciende solo cuando la pila está vacía (se ve la carta)
        onBackPressedDispatcher.addCallback(this, guardianSalir)
        guardianSalir.isEnabled = supportFragmentManager.backStackEntryCount == 0
        supportFragmentManager.addOnBackStackChangedListener {
            guardianSalir.isEnabled = supportFragmentManager.backStackEntryCount == 0
        }

        // RF-38: si en el aviso se pulsa «Salir», se sigue al PIN; «Cancelar» (o tocar fuera) no hace nada
        supportFragmentManager.setFragmentResultListener(CLAVE_SALIR, this) { _, sobre ->
            if (sobre.getBoolean(ConfirmacionDialog.RESPUESTA_AFIRMATIVA)) pedirPin()
        }

        // Cuando 1c deja el sobre de «PIN correcto», se sale de Pedir: se vuelve a 1a (ficha 1)
        supportFragmentManager.setFragmentResultListener(PinDialog.CLAVE_RESULTADO, this) { _, _ ->
            finish()
        }
    }

    // [Claude] Salir de Pedir (ficha 1): primero el aviso si hay platos sin enviar (RF-38), después el PIN.
    // Con el carrito vacío, directamente el PIN
    private fun intentarSalir() {
        if (!portero.permite()) return
        val carrito = viewModel.carrito.value
        if (carrito == null || carrito.estaVacio()) {
            pedirPin()
            return
        }
        // D16: «Hay 1 plato… Se perderá» / «Hay 3 platos… Se perderán». El número va dos veces:
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

    // En el nivel 1, salir de Pedir siempre pide el PIN (P41 del Project). Cancelar deja la carta abierta.
    // RNF-24: si se sale, el carrito no se vacía a mano: muere con la libreta al cerrarse Pedir
    private fun pedirPin() {
        PinDialog().show(supportFragmentManager, "pin")
    }

    // P145: PinDialog pregunta aquí; esta pantalla delega en su libreta
    override suspend fun comprobarPin(pin: String): Boolean = viewModel.comprobarPin(pin)

    companion object {
        // [Claude] Los nombres de lo que va grapado a la nota que abre Pedir
        const val EXTRA_MESA_ID = "mesa_id"
        const val EXTRA_MESA_NUMERO = "mesa_numero"

        // [Claude] El nombre de la caja «¿Salir sin enviar?» y de su sobre
        private const val CLAVE_SALIR = "salir_sin_enviar"
    }
}