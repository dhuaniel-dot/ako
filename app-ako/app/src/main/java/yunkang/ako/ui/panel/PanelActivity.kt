package yunkang.ako.ui.panel

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import yunkang.ako.R
import yunkang.ako.databinding.ActivityPanelBinding
import yunkang.ako.ui.comun.ConfirmacionDialog
import yunkang.ako.ui.plato.PlatoActivity

// 2a · Panel del Propietario: se llega tras el PIN correcto (1c).
// El Resumen de ingresos (llega en la S10) abre todavía la caja provisional
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

        // La lista de cajas y sus timbres: el lápiz abre 2b; «+ Plato» y tocar un plato abren
        // el formulario del plato (3a) con una nota (Intent) que lleva grapado un id
        val adaptador = CategoriaAdapter(
            estaPlegada = { id -> viewModel.estaPlegada(id) },
            alPlegar = { categoria -> viewModel.alternarPlegado(categoria.id) },
            alEditar = { categoria -> CategoriaBottomSheet.nueva(categoria.id).show(supportFragmentManager, "categoria") },
            alAnadirPlato = { categoria ->
                // Crear: se grapa la categoría de la caja, que saldrá ya elegida (ficha 3)
                val nota = Intent(this, PlatoActivity::class.java)
                nota.putExtra(PlatoActivity.EXTRA_CATEGORIA_ID, categoria.id)
                startActivity(nota)
            },
            alTocarPlato = { plato ->
                // Editar: se grapa solo el id; la pantalla lee el plato fresco de la base de datos
                val nota = Intent(this, PlatoActivity::class.java)
                nota.putExtra(PlatoActivity.EXTRA_PRODUCTO_ID, plato.id)
                startActivity(nota)
            }
        )
        binding.listaCategorias.layoutManager = LinearLayoutManager(this)
        binding.listaCategorias.adapter = adaptador

        // P49 B: se mira el tablón UNA sola vez, aquí en onCreate. Cada vez que Room manda
        // una lista nueva, se le pasa al encargado; no hace falta recargar en onResume
        viewModel.categoriasConPlatos.observe(this) { cajas -> adaptador.submitList(cajas) }

        // «+» de la barra: hoja 2b para crear una categoría
        binding.botonNuevaCategoria.setOnClickListener {
            CategoriaBottomSheet.nueva().show(supportFragmentManager, "categoria")
        }

        // Resumen de ingresos: caja provisional hasta la S10
        binding.botonResumen.setOnClickListener {
            abrirPendiente(getString(R.string.panel_btn_resumen_ingresos))
        }

        // Atrás y Terminar hacen lo mismo: cerrar el Panel y volver a 1a, sin pedir PIN (ficha 1)
        binding.botonAtras.setOnClickListener { finish() }
        binding.botonTerminar.setOnClickListener { finish() }

        // Cambiar PIN: abre 1e
        binding.botonCambiarPin.setOnClickListener {
            CambiarPinDialog().show(supportFragmentManager, "cambiar_pin")
        }
    }

    // [Claude] La caja provisional de la S5 («Esta parte llega en una sesión posterior»), solo con Aceptar.
    // Hoy solo la usa Resumen de ingresos, hasta la S10
    private fun abrirPendiente(titulo: String) {
        ConfirmacionDialog.nueva(
            titulo = titulo,
            texto = getString(R.string.pendiente_sesion_posterior),
            afirmativo = getString(R.string.comun_aceptar),
            clave = "pendiente"
        ).show(supportFragmentManager, "pendiente")
    }
}