package yunkang.ako.ui.selector

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import yunkang.ako.R
import yunkang.ako.databinding.ActivitySelectorBinding

// Pantalla 1 (Selector de rol). Es solo el marco: dentro enseña 1a (Selector) o 1b (Crear PIN)
class SelectorActivity : AppCompatActivity() {

    // El "mando" de las vistas de activity_selector.xml (ViewBinding)
    private lateinit var binding: ActivitySelectorBinding

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

        // La primera vez se monta 1a. Si Android rehace la pantalla, ya está montado y no se repite
        // (la elección entre 1a y 1b según haya PIN llega en la pieza 6, con 1b)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.contenedor, SelectorFragment())
            }
        }
    }
}