package yunkang.ako.ui.panel

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import yunkang.ako.databinding.ActivityPanelBinding

// [Claude] Panel del Propietario PROVISIONAL (S5): se llega tras el PIN correcto.
// En la S6 se sustituye por el Panel de verdad (pantalla 2)
class PanelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPanelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // P139: de borde a borde, apartado de las barras del sistema
        enableEdgeToEdge()
        binding = ActivityPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.raiz) { vista, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            vista.setPadding(barras.left, barras.top, barras.right, barras.bottom)
            insets
        }

        // Atrás y Terminar hacen lo mismo: cerrar el Panel y volver a 1a, sin pedir PIN (ficha 1)
        binding.botonAtras.setOnClickListener { finish() }
        binding.botonTerminar.setOnClickListener { finish() }

        // Cambiar PIN se conecta en la pieza 11
    }
}