package yunkang.ako.ui.pedido

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import yunkang.ako.R
import yunkang.ako.databinding.ActivityPedidoBinding

// Pantalla 5 (Pedir). Es solo el marco: dentro se apilan la carta (5a), la ficha (5b) y el carrito (5c)
class PedidoActivity : AppCompatActivity() {

    // El "mando" de las vistas de activity_pedido.xml (ViewBinding)
    private lateinit var binding: ActivityPedidoBinding

    // La libreta de Pedir; sus Fragments piden la misma con activityViewModels
    private val viewModel: PedidoViewModel by viewModels()

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
    }

    companion object {
        // [Claude] Los nombres de lo que va grapado a la nota que abre Pedir
        const val EXTRA_MESA_ID = "mesa_id"
        const val EXTRA_MESA_NUMERO = "mesa_numero"
    }
}