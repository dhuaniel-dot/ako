package yunkang.ako.ui.resumen

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import yunkang.ako.R
import yunkang.ako.databinding.ActivityResumenIngresosBinding

// Pantalla 2g (Resumen de ingresos). Solo se llega desde el Panel, detrás del PIN (RNF-10): Cuenta no pide PIN
// y la recaudación quedaría a la vista de cualquiera. Es solo el marco: dentro va la lista (ResumenListaFragment)
// y, encima en la pila, el recibo en solo lectura (P87 B). Solo mira: no escribe nada en la base de datos
class ResumenIngresosActivity : AppCompatActivity() {

    // El "mando" de las vistas de activity_resumen_ingresos.xml (ViewBinding)
    private lateinit var binding: ActivityResumenIngresosBinding

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
        }
    }
}