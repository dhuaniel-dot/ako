package yunkang.ako.ui.selector

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import yunkang.ako.R
import yunkang.ako.databinding.ActivitySelectorBinding
import yunkang.ako.ui.comun.ComprobadorPin
import yunkang.ako.ui.comun.GuardaDobleToque
import yunkang.ako.ui.comun.RejillaMesasFragment
import yunkang.ako.ui.panel.PanelActivity
import yunkang.ako.ui.pedido.PedidoActivity

// Pantalla 1 (Selector de rol). Es solo el marco: dentro enseña 1a (Selector), 1b (Crear PIN) o 1d (Elegir mesa).
// Sabe comprobar un PIN (ComprobadorPin): es lo que PinDialog le pide
class SelectorActivity : AppCompatActivity(), ComprobadorPin {

    private lateinit var binding: ActivitySelectorBinding

    // La libreta de la pantalla 1, hecha con su fábrica
    private val viewModel: SelectorViewModel by viewModels { SelectorViewModel.Factory }

    // El portero de la rejilla 1d, como en CuentaActivity: dos toques rápidos en una mesa no abren Pedir dos veces
    private val portero = GuardaDobleToque()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // La pantalla ocupa el móvil de borde a borde...
        enableEdgeToEdge()
        binding = ActivitySelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ...y el contenedor se aparta de las barras del sistema (la hora arriba, los botones abajo)
        // y del teclado (ime), para que Aceptar no quede tapado al escribir el PIN
        ViewCompat.setOnApplyWindowInsetsListener(binding.contenedor) { vista, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            vista.setPadding(barras.left, barras.top, barras.right, barras.bottom)
            insets
        }

        // La primera vez: si ya hay PIN, 1a; si no, 1b (ficha 1, flujo).
        // Si Android rehace la pantalla, ya está montado y no se repite
        if (savedInstanceState == null) {
            val vista = if (viewModel.hayPin()) SelectorFragment() else CrearPinFragment()
            supportFragmentManager.commit {
                replace(R.id.contenedor, vista)
            }
        }

        // Cuando 1c deja el sobre de «PIN correcto», esta pantalla decide: abrir el Panel (Propietario)
        supportFragmentManager.setFragmentResultListener(PinDialog.CLAVE_RESULTADO, this) { _, _ ->
            startActivity(Intent(this, PanelActivity::class.java))
        }

        // 1d: cada vez que cambian las mesas, se las da a la rejilla, si se está viendo
        viewModel.mesas.observe(this) { mesas ->
            rejillaVisible()?.mostrar(mesas)
        }

        // 1d: la rejilla avisa de la mesa tocada y esta pantalla abre Pedir con esa mesa grapada.
        // [Claude] Antes quita 1d de la pila: al salir de Pedir se vuelve a 1a, no a la rejilla
        supportFragmentManager.setFragmentResultListener(RejillaMesasFragment.CLAVE_MESA_TOCADA, this) { _, sobre ->
            if (!portero.permite()) return@setFragmentResultListener
            supportFragmentManager.popBackStack()
            val nota = Intent(this, PedidoActivity::class.java)
            nota.putExtra(PedidoActivity.EXTRA_MESA_ID, sobre.getLong(RejillaMesasFragment.MESA_ID))
            nota.putExtra(PedidoActivity.EXTRA_MESA_NUMERO, sobre.getInt(RejillaMesasFragment.MESA_NUMERO))
            startActivity(nota)
        }
    }

    // 1d: la rejilla en modo elegir, encima de 1a. addToBackStack: Atrás la quita y vuelve 1a, sin PIN
    fun abrirElegirMesa() {
        val rejilla = RejillaMesasFragment.nueva(R.string.mesa_elegir_titulo)
        // Las mesas que ya hay en el tablón; las siguientes llegan por el observe de arriba
        rejilla.mostrar(viewModel.mesas.value ?: emptyList())
        supportFragmentManager.commit {
            replace(R.id.contenedor, rejilla)
            addToBackStack(null)
        }
    }

    // [Claude] La rejilla, si es lo que se ve ahora en el contenedor; si no, vacío
    private fun rejillaVisible(): RejillaMesasFragment? =
        supportFragmentManager.findFragmentById(R.id.contenedor) as? RejillaMesasFragment

    // PinDialog pregunta aquí; esta pantalla delega en su libreta
    override suspend fun comprobarPin(pin: String): Boolean = viewModel.comprobarPin(pin)
}