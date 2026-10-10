package yunkang.ako.ui.cuenta

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import yunkang.ako.R
import yunkang.ako.databinding.ActivityCuentaBinding
import yunkang.ako.ui.comun.ConfirmacionDialog
import yunkang.ako.ui.comun.GuardaDobleToque
import yunkang.ako.ui.comun.ReciboFragment
import yunkang.ako.ui.comun.RejillaMesasFragment
import yunkang.ako.ui.comun.apartarDeLasBarras

// pantalla 6 (Cuenta). es solo el marco: dentro se apilan la rejilla (6a), la comanda (6b) y el recibo (6c)
// no pide PIN: la usa el camarero (ficha 1). el Resumen de ingresos no vive aquí, sino detrás del PIN
class CuentaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCuentaBinding

    // la libreta de Cuenta, la misma para sus Fragments (como en PedidoActivity)
    private val viewModel: CuentaViewModel by viewModels { CuentaViewModel.Factory }

    // el portero de Cuenta, uno para toda la pantalla; también lo usan 6b y 6c
    val portero = GuardaDobleToque()

    // [Claude] si la comanda se cerró con la app en segundo plano, la vuelta a la rejilla espera a onResume
    private var vueltaPendiente = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // de borde a borde, apartado de las barras y del teclado (como SelectorActivity)
        enableEdgeToEdge()
        binding = ActivityCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.contenedor.apartarDeLasBarras()

        // la primera vez, la rejilla (6a) con su título; si Android rehace la pantalla, ya está montada
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.contenedor, RejillaMesasFragment.nueva(R.string.cuenta_titulo))
            }
        } else if (viewModel.comandaId == 0L) {
            // Android mató la app con 6b o 6c delante y la libreta renació en blanco: se cierran las cajas abiertas
            // y se vacía la pila (vuelve la rejilla y el camarero toca otra vez la mesa)
            supportFragmentManager.fragments.filterIsInstance<ConfirmacionDialog>().forEach { it.dismiss() }
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        // 6a: cada vez que cambian las mesas, se las da a la rejilla, si se está viendo
        viewModel.mesas.observe(this) { mesas ->
            rejillaVisible()?.mostrar(mesas)
        }

        // 6c: cada vez que cambian las líneas o el total, se los da al recibo, si se está viendo
        // también le llegan así si Android rehace la pantalla con el recibo delante
        viewModel.lineas.observe(this) { darDatosAlRecibo() }
        viewModel.total.observe(this) { darDatosAlRecibo() }

        // 6c: el recibo avisa de lo entregado; la libreta calcula el cambio y se le devuelve al recibo
        // sin céntimos en el sobre (campo vacío o que no es un importe): Cambio en blanco
        supportFragmentManager.setFragmentResultListener(ReciboFragment.CLAVE_ENTREGADO, this) { _, sobre ->
            val cambio = if (sobre.containsKey(ReciboFragment.ENTREGADO_CENTIMOS)) {
                viewModel.cambio(sobre.getInt(ReciboFragment.ENTREGADO_CENTIMOS))
            } else {
                null
            }
            reciboVisible()?.mostrarCambio(cambio)
        }

        // 6c · cobrar: con «Cobrar» en la caja, la libreta deja la comanda pagada y se vuelve a la rejilla,
        // con la mesa blanca (el verde es nivel 3). «Cancelar» (o tocar fuera) no hace nada
        supportFragmentManager.setFragmentResultListener(ReciboFragment.CLAVE_COBRAR, this) { _, sobre ->
            if (!sobre.getBoolean(ConfirmacionDialog.RESPUESTA_AFIRMATIVA)) return@setFragmentResultListener
            lifecycleScope.launch {
                if (viewModel.cobrar()) volverARejilla()
            }
        }

        // mientras 6b o 6c tapan la rejilla, las mesas que manda Room no le llegan (no se ve)
        // cuando la pila se vacía y la rejilla vuelve a verse, se le dan las últimas que hay en el tablón
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                rejillaVisible()?.mostrar(viewModel.mesas.value ?: emptyList())
            }
        }

        // 6a: la rejilla avisa de la mesa tocada y esta pantalla decide qué significa el toque (ficha 6)
        supportFragmentManager.setFragmentResultListener(RejillaMesasFragment.CLAVE_MESA_TOCADA, this) { _, sobre ->
            if (!portero.permite()) return@setFragmentResultListener
            val mesaId = sobre.getLong(RejillaMesasFragment.MESA_ID)
            val numero = sobre.getInt(RejillaMesasFragment.MESA_NUMERO)
            // el sobre no lleva la comanda: se busca la mesa en el tablón (roja = tiene comanda pendiente)
            val comandaId = viewModel.mesas.value?.find { it.mesa.id == mesaId }?.comandaId
            if (comandaId == null) {
                // mesa blanca (nivel 1): solo un aviso. no se crea nada: la comanda nace con el primer Enviar
                Snackbar.make(
                    binding.contenedor,
                    getString(R.string.cuenta_mesa_sin_comanda, numero),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                // mesa roja: la libreta abre su comanda y 6b se pone encima de la rejilla
                // addToBackStack: Atrás (el del sistema o la flecha) la quita y vuelve la rejilla
                viewModel.abrirComanda(comandaId, numero)
                supportFragmentManager.commit {
                    replace(R.id.contenedor, ComandaFragment())
                    addToBackStack(null)
                }
            }
        }
    }

    // 6b → 6c: el recibo de la comanda abierta, encima de 6b. addToBackStack: Atrás vuelve a 6b
    fun abrirRecibo() {
        val recibo = ReciboFragment.nuevo(viewModel.mesaNumero, soloLectura = false)
        // los datos que ya hay en el tablón; si cambian, llegan por los observe de onCreate
        recibo.mostrar(viewModel.lineas.value ?: emptyList(), viewModel.total.value ?: 0)
        supportFragmentManager.commit {
            replace(R.id.contenedor, recibo)
            addToBackStack(null)
        }
    }

    // [Claude] quitar la última línea, Anular y Cobrar acaban aquí: se quitan 6b y 6c de golpe y queda la rejilla, con la mesa ya blanca
    // si la app pasó a segundo plano mientras se esperaba a la base de datos, se hace al volver (onResume)
    fun volverARejilla() {
        if (supportFragmentManager.isStateSaved) {
            vueltaPendiente = true
            return
        }
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun onResume() {
        super.onResume()
        if (vueltaPendiente) {
            vueltaPendiente = false
            volverARejilla()
        }
    }

    // [Claude] las líneas y el total de la libreta, al recibo, si es lo que se ve ahora
    private fun darDatosAlRecibo() {
        reciboVisible()?.mostrar(viewModel.lineas.value ?: emptyList(), viewModel.total.value ?: 0)
    }

    private fun reciboVisible(): ReciboFragment? =
        supportFragmentManager.findFragmentById(R.id.contenedor) as? ReciboFragment

    private fun rejillaVisible(): RejillaMesasFragment? =
        supportFragmentManager.findFragmentById(R.id.contenedor) as? RejillaMesasFragment
}