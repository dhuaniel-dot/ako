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
import yunkang.ako.ui.panel.PanelActivity

// Pantalla 1 (Selector de rol). Es solo el marco: dentro enseña 1a (Selector) o 1b (Crear PIN)
class SelectorActivity : AppCompatActivity() {

    // El "mando" de las vistas de activity_selector.xml (ViewBinding)
    private lateinit var binding: ActivitySelectorBinding

    // La libreta de la pantalla 1, hecha con su fábrica (P140)
    private val viewModel: SelectorViewModel by viewModels { SelectorViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // P139: la pantalla ocupa el móvil de borde a borde...
        enableEdgeToEdge()
        binding = ActivitySelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ...y el contenedor se aparta de las barras del sistema (la hora arriba, los botones abajo)
        ViewCompat.setOnApplyWindowInsetsListener(binding.contenedor) { vista, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
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
    }
}