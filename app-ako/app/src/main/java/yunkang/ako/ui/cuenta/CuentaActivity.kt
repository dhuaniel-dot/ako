package yunkang.ako.ui.cuenta

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.google.android.material.snackbar.Snackbar
import yunkang.ako.R
import yunkang.ako.databinding.ActivityCuentaBinding
import yunkang.ako.ui.comun.GuardaDobleToque
import yunkang.ako.ui.comun.RejillaMesasFragment

// Pantalla 6 (Cuenta). Es solo el marco: dentro se apilan la rejilla (6a), la comanda (6b) y el recibo (6c).
// No pide PIN: la usa el camarero (ficha 1). El Resumen de ingresos no vive aquí, sino detrás del PIN (RNF-10)
class CuentaActivity : AppCompatActivity() {

    // El "mando" de las vistas de activity_cuenta.xml (ViewBinding)
    private lateinit var binding: ActivityCuentaBinding

    // La libreta de Cuenta, hecha con su fábrica (P140); sus Fragments piden la misma con activityViewModels
    private val viewModel: CuentaViewModel by viewModels { CuentaViewModel.Factory }

    // H01 B (P171): el portero de Cuenta, uno para toda la pantalla; también lo usarán 6b y 6c
    val portero = GuardaDobleToque()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // P139: de borde a borde, y el contenedor se aparta de las barras del sistema y del teclado (H25)
        enableEdgeToEdge()
        binding = ActivityCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.contenedor) { vista, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            vista.setPadding(barras.left, barras.top, barras.right, barras.bottom)
            insets
        }

        // La primera vez, la rejilla (6a) con su título (P168 C); si Android rehace la pantalla, ya está montada
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.contenedor, RejillaMesasFragment.nueva(R.string.cuenta_titulo))
            }
        }

        // 6a (P167 A): cada vez que cambian las mesas, se las da a la rejilla, si se está viendo
        viewModel.mesas.observe(this) { mesas ->
            rejillaVisible()?.mostrar(mesas)
        }

        // 6a: la rejilla avisa de la mesa tocada y esta pantalla decide qué significa el toque (ficha 6)
        supportFragmentManager.setFragmentResultListener(RejillaMesasFragment.CLAVE_MESA_TOCADA, this) { _, sobre ->
            if (!portero.permite()) return@setFragmentResultListener
            val mesaId = sobre.getLong(RejillaMesasFragment.MESA_ID)
            val numero = sobre.getInt(RejillaMesasFragment.MESA_NUMERO)
            // El sobre no lleva la comanda: se busca la mesa en el tablón (R3: roja = tiene comanda pendiente)
            val estado = viewModel.mesas.value?.find { it.mesa.id == mesaId }
            if (estado?.comandaId == null) {
                // Mesa blanca (nivel 1, D17): solo un aviso. No se crea nada: la comanda nace con el primer Enviar (R2)
                Snackbar.make(
                    binding.contenedor,
                    getString(R.string.cuenta_mesa_sin_comanda, numero),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            // Mesa roja: abrirá 6b (pieza 6)
        }
    }

    // [Claude] La rejilla, si es lo que se ve ahora en el contenedor; si no, vacío
    private fun rejillaVisible(): RejillaMesasFragment? =
        supportFragmentManager.findFragmentById(R.id.contenedor) as? RejillaMesasFragment
}