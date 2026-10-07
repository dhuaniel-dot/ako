package yunkang.ako.ui.resumen

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import yunkang.ako.R
import yunkang.ako.databinding.ActivityResumenIngresosBinding
import yunkang.ako.dominio.modelos.ComandaConTotal
import yunkang.ako.ui.comun.ReciboFragment

// Pantalla 2g (Resumen de ingresos). Solo se llega desde el Panel, detrás del PIN (RNF-10): Cuenta no pide PIN
// y la recaudación quedaría a la vista de cualquiera. Es solo el marco: dentro va la lista (ResumenListaFragment)
// y, encima en la pila, el recibo en solo lectura (P87 B). Solo mira: no escribe nada en la base de datos
class ResumenIngresosActivity : AppCompatActivity() {

    // El "mando" de las vistas de activity_resumen_ingresos.xml (ViewBinding)
    private lateinit var binding: ActivityResumenIngresosBinding

    // La libreta de 2g, hecha con su fábrica (P140); la lista pide la misma con activityViewModels
    private val viewModel: ResumenIngresosViewModel by viewModels { ResumenIngresosViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // P139: de borde a borde, y el contenedor se aparta de las barras del sistema y del teclado (H25)
        enableEdgeToEdge()
        binding = ActivityResumenIngresosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.contenedor) { vista, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            vista.setPadding(barras.left, barras.top, barras.right, barras.bottom)
            insets
        }

        // La primera vez, la lista; si Android rehace la pantalla, ya está montada
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.contenedor, ResumenListaFragment())
            }
        } else if (viewModel.comandaRecibo == null) {
            // [Claude] Como P193 A en Cuenta: Android mató la app con el recibo delante y la libreta renació
            // en blanco, sin saber de qué comanda era. Se quita el recibo y queda la lista
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        // Cada vez que llegan las líneas de la comanda que se mira, se las da al recibo, si se está viendo.
        // También le llegan así si Android rehace la pantalla con el recibo delante (modo noche)
        viewModel.lineasRecibo.observe(this) { darDatosAlRecibo() }
    }

    // Lista → recibo de una comanda cobrada, en su lugar (P87 B), sin calculadora ni Cobrar (soloLectura).
    // addToBackStack: Atrás (el del sistema o la flecha) lo quita y vuelve la lista con el mismo día
    fun abrirRecibo(comanda: ComandaConTotal) {
        viewModel.abrirRecibo(comanda)
        val recibo = ReciboFragment.nuevo(comanda.mesaNumero, soloLectura = true)
        // Lo que ya hay en el tablón (vacío, P197 A); las líneas llegan después por el observe de onCreate
        recibo.mostrar(viewModel.lineasRecibo.value ?: emptyList(), comanda.totalCentimos)
        supportFragmentManager.commit {
            replace(R.id.contenedor, recibo)
            addToBackStack(null)
        }
        // [Claude] commit no cambia la vista al momento: la deja apuntada para dentro de un instante. Las líneas
        // pueden llegar antes y no encontrarían el recibo; executePendingTransactions lo pone ya en su sitio
        supportFragmentManager.executePendingTransactions()
    }

    // [Claude] Las líneas y el total de la comanda que se mira, al recibo, si es lo que se ve ahora
    private fun darDatosAlRecibo() {
        val comanda = viewModel.comandaRecibo ?: return
        reciboVisible()?.mostrar(viewModel.lineasRecibo.value ?: emptyList(), comanda.totalCentimos)
    }

    // [Claude] El recibo, si es lo que se ve ahora en el contenedor; si no, vacío
    private fun reciboVisible(): ReciboFragment? =
        supportFragmentManager.findFragmentById(R.id.contenedor) as? ReciboFragment
}