package yunkang.ako.ui.panel

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import yunkang.ako.databinding.ActivityPanelBinding

// 2a · Panel del Propietario: se llega tras el PIN correcto (1c).
// El «+» y el Resumen de ingresos se conectan en las piezas 12 y 18
class PanelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPanelBinding

    // P140: la libreta del Panel, construida por su fábrica (la misma que usa CambiarPinDialog)
    private val viewModel: PanelViewModel by viewModels { PanelViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // P139: de borde a borde, apartado de las barras del sistema y del teclado (H25)
        enableEdgeToEdge()
        binding = ActivityPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.raiz) { vista, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            vista.setPadding(barras.left, barras.top, barras.right, barras.bottom)
            insets
        }

        // La lista de cajas: una debajo de otra (LinearLayoutManager) y su encargado
        val adaptador = CategoriaAdapter()
        binding.listaCategorias.layoutManager = LinearLayoutManager(this)
        binding.listaCategorias.adapter = adaptador

        // P49 B: se mira el tablón UNA sola vez, aquí en onCreate. Cada vez que Room manda
        // una lista nueva, se le pasa al encargado; no hace falta recargar en onResume
        viewModel.categoriasConPlatos.observe(this) { cajas -> adaptador.submitList(cajas) }

        // Atrás y Terminar hacen lo mismo: cerrar el Panel y volver a 1a, sin pedir PIN (ficha 1)
        binding.botonAtras.setOnClickListener { finish() }
        binding.botonTerminar.setOnClickListener { finish() }

        // Cambiar PIN: abre 1e
        binding.botonCambiarPin.setOnClickListener {
            CambiarPinDialog().show(supportFragmentManager, "cambiar_pin")
        }
    }
}