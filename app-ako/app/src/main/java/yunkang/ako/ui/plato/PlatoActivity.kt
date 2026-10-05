package yunkang.ako.ui.plato

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import yunkang.ako.R
import yunkang.ako.databinding.ActivityPlatoBinding

// 3a · Formulario del plato. Se abre desde el Panel: «+ Plato» de una caja (crear)
// o tocando un plato (editar)
class PlatoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlatoBinding

    // P140: la libreta de esta pantalla, construida por su fábrica
    private val viewModel: PlatoViewModel by viewModels { PlatoViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // P139: de borde a borde, apartado de las barras del sistema y del teclado (H25)
        enableEdgeToEdge()
        binding = ActivityPlatoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.raiz) { vista, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            vista.setPadding(barras.left, barras.top, barras.right, barras.bottom)
            insets
        }

        // Lo que viene grapado a la nota (Intent). −1 = «no me han dado ningún plato»
        val productoId = intent.getLongExtra(EXTRA_PRODUCTO_ID, -1L)

        // Con plato es editar; sin plato, crear
        if (productoId == -1L) {
            binding.textoTitulo.setText(R.string.plato_titulo_nuevo)
        } else {
            binding.textoTitulo.setText(R.string.plato_titulo_editar)
        }

        // P162 B: se pide la bandeja siempre; si la libreta ya la tiene, no vuelve a leer
        if (productoId == -1L) {
            viewModel.cargar(null)
        } else {
            viewModel.cargar(productoId)
        }

        // PROVISIONAL (se borra en la pieza 6): enseña en Logcat lo que llega
        viewModel.datos.observe(this) { datos ->
            Log.d("Ako", "Plato: ${datos.plato?.nombre} · categorías: ${datos.categorias.size} · alérgenos: ${datos.alergenos.size} · marcados: ${datos.alergenosDelPlato}")
        }

        binding.botonAtras.setOnClickListener { finish() }
    }

    // [Claude] Los nombres de lo que se grapa a la nota. Están aquí para que el Panel
    // y esta pantalla usen exactamente la misma palabra
    companion object {
        const val EXTRA_PRODUCTO_ID = "producto_id"
        const val EXTRA_CATEGORIA_ID = "categoria_id"
    }
}