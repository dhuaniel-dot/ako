package yunkang.ako.ui.resumen

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import yunkang.ako.R
import yunkang.ako.databinding.ActivityResumenIngresosBinding
import yunkang.ako.dominio.modelos.ComandaConTotal
import yunkang.ako.ui.comun.ReciboFragment
import yunkang.ako.ui.comun.apartarDeLasBarras

// pantalla 2g (Resumen de ingresos). solo se llega desde el Panel, detrás del PIN: Cuenta no pide PIN
// y la recaudación quedaría a la vista de cualquiera. es solo el marco: dentro va la lista (ResumenListaFragment)
// y, encima en la pila, el recibo en solo lectura. solo mira: no escribe nada en la base de datos
class ResumenIngresosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResumenIngresosBinding

    // la libreta de 2g, la misma para la lista (como en PedidoActivity)
    private val viewModel: ResumenIngresosViewModel by viewModels { ResumenIngresosViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // de borde a borde, apartado de las barras y del teclado (como SelectorActivity)
        enableEdgeToEdge()
        binding = ActivityResumenIngresosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.contenedor.apartarDeLasBarras()

        // la primera vez, la lista; si Android rehace la pantalla, ya está montada
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.contenedor, ResumenListaFragment())
            }
        } else if (viewModel.comandaRecibo == null) {
            // [Claude] como en Cuenta: si Android mató la app con el recibo delante, se quita el recibo y queda la lista
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        // cada vez que llegan las líneas de la comanda que se mira, se las da al recibo, si se está viendo
        // también le llegan así si Android rehace la pantalla con el recibo delante (modo noche)
        viewModel.lineasRecibo.observe(this) { darDatosAlRecibo() }
    }

    // lista → recibo de una comanda cobrada, en su lugar, sin calculadora ni Cobrar (soloLectura)
    // addToBackStack: Atrás (el del sistema o la flecha) lo quita y vuelve la lista con el mismo día
    fun abrirRecibo(comanda: ComandaConTotal) {
        viewModel.abrirRecibo(comanda)
        val recibo = ReciboFragment.nuevo(comanda.mesaNumero, soloLectura = true)
        // lo que ya hay en el tablón (vacío); las líneas llegan después por el observe de onCreate
        recibo.mostrar(viewModel.lineasRecibo.value ?: emptyList(), comanda.totalCentimos)
        supportFragmentManager.commit {
            replace(R.id.contenedor, recibo)
            addToBackStack(null)
        }
        // [Claude] commit no cambia la vista al momento: executePendingTransactions pone ya el recibo, para que las líneas lo encuentren
        supportFragmentManager.executePendingTransactions()
    }

    private fun darDatosAlRecibo() {
        val comanda = viewModel.comandaRecibo ?: return
        reciboVisible()?.mostrar(viewModel.lineasRecibo.value ?: emptyList(), comanda.totalCentimos)
    }

    private fun reciboVisible(): ReciboFragment? =
        supportFragmentManager.findFragmentById(R.id.contenedor) as? ReciboFragment
}