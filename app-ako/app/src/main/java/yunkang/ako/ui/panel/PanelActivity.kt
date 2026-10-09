package yunkang.ako.ui.panel

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import yunkang.ako.databinding.ActivityPanelBinding
import yunkang.ako.ui.comun.GuardaDobleToque
import yunkang.ako.ui.plato.PlatoActivity
import yunkang.ako.ui.resumen.ResumenIngresosActivity
import yunkang.ako.ui.comun.apartarDeLasBarras

// 2a · Panel del Propietario: se llega tras el PIN correcto (1c).
// [Resumen de ingresos] abre 2g, que solo vive aquí, detrás del PIN (RNF-10)
class PanelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPanelBinding

    // La libreta del Panel, construida por su fábrica (la misma que usa CambiarPinDialog)
    private val viewModel: PanelViewModel by viewModels { PanelViewModel.Factory }

    // El portero del Panel. Un segundo toque en menos de medio segundo no abre
    // otra pantalla del plato ni otra hoja o caja encima de la primera
    private val portero = GuardaDobleToque()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // De borde a borde, apartado de las barras y del teclado (como SelectorActivity)
        enableEdgeToEdge()
        binding = ActivityPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.raiz.apartarDeLasBarras()

        // La lista de cajas y sus timbres: el lápiz abre 2b; «+ Plato» y tocar un plato abren
        // el formulario del plato (3a) con una nota (Intent) que lleva grapado un id.
        // Cada timbre pasa antes por el portero
        val adaptador = CategoriaAdapter(
            estaPlegada = { id -> viewModel.estaPlegada(id) },
            alPlegar = { categoria -> viewModel.alternarPlegado(categoria.id) },
            alEditar = { categoria ->
                if (portero.permite()) {
                    CategoriaBottomSheet.nueva(categoria.id).show(supportFragmentManager, "categoria")
                }
            },
            alAnadirPlato = { categoria ->
                if (portero.permite()) {
                    // Crear: se grapa la categoría de la caja, que saldrá ya elegida (ficha 3)
                    val nota = Intent(this, PlatoActivity::class.java)
                    nota.putExtra(PlatoActivity.EXTRA_CATEGORIA_ID, categoria.id)
                    startActivity(nota)
                }
            },
            alTocarPlato = { plato ->
                if (portero.permite()) {
                    // Editar: se grapa solo el id; la pantalla lee el plato fresco de la base de datos
                    val nota = Intent(this, PlatoActivity::class.java)
                    nota.putExtra(PlatoActivity.EXTRA_PRODUCTO_ID, plato.id)
                    startActivity(nota)
                }
            }
        )
        binding.listaCategorias.layoutManager = LinearLayoutManager(this)
        binding.listaCategorias.adapter = adaptador

        // Se mira el tablón UNA sola vez, aquí en onCreate. Cada vez que Room manda
        // una lista nueva, se le pasa al encargado; no hace falta recargar en onResume
        viewModel.categoriasConPlatos.observe(this) { cajas -> adaptador.submitList(cajas) }

        // «+» de la barra: hoja 2b para crear una categoría
        binding.botonNuevaCategoria.setOnClickListener {
            if (portero.permite()) {
                CategoriaBottomSheet.nueva().show(supportFragmentManager, "categoria")
            }
        }

        // Resumen de ingresos: abre 2g. Al volver con Atrás, el Panel sigue abierto: no se pide otra vez el PIN
        binding.botonResumen.setOnClickListener {
            if (portero.permite()) {
                startActivity(Intent(this, ResumenIngresosActivity::class.java))
            }
        }

        // Atrás y Terminar hacen lo mismo: cerrar el Panel y volver a 1a, sin pedir PIN (ficha 1)
        binding.botonAtras.setOnClickListener { finish() }
        binding.botonTerminar.setOnClickListener { finish() }

        // Cambiar PIN: abre 1e
        binding.botonCambiarPin.setOnClickListener {
            if (portero.permite()) {
                CambiarPinDialog().show(supportFragmentManager, "cambiar_pin")
            }
        }
    }
}