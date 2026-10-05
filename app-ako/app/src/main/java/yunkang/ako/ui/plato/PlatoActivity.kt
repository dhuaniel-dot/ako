package yunkang.ako.ui.plato

import android.os.Bundle
import android.widget.GridLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.checkbox.MaterialCheckBox
import yunkang.ako.R
import yunkang.ako.datos.entidades.Alergeno
import yunkang.ako.databinding.ActivityPlatoBinding
import yunkang.ako.ui.comun.Formato

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

        // Lo que viene grapado a la nota (Intent). −1 = «no me han dado nada»
        val productoId = intent.getLongExtra(EXTRA_PRODUCTO_ID, -1L)
        val categoriaDeLaCaja = intent.getLongExtra(EXTRA_CATEGORIA_ID, -1L)

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

        viewModel.datos.observe(this) { datos ->
            // La lista del desplegable se monta cada vez: si Android recrea la pantalla, la vista es nueva.
            // P61 A: todas las categorías, también las eliminadas, sin marca; la de por defecto, la última
            val nombres = datos.categorias.map { it.nombre }.toTypedArray()
            binding.textoCategoria.setSimpleItems(nombres)
            binding.textoCategoria.setOnItemClickListener { _, _, posicion, _ ->
                viewModel.categoriaElegidaId = datos.categorias[posicion].id
            }

            // Los campos se rellenan una sola vez; después manda lo que teclee el Propietario
            if (!viewModel.formularioRelleno) {
                rellenar(datos, categoriaDeLaCaja)
                viewModel.formularioRelleno = true
            }

            // La categoría elegida vive en la libreta y se pinta cada vez (el campo no guarda su texto:
            // saveEnabled="false"). false = «no filtres la lista por lo escrito»: si no, enseñaría solo esa
            val elegida = datos.categorias.find { it.id == viewModel.categoriaElegidaId }
            if (elegida != null) {
                binding.textoCategoria.setText(elegida.nombre, false)
            }

            // Las casillas también se crean cada vez (vista nueva tras una recreación)
            pintarAlergenos(datos.alergenos)
        }

        binding.botonAtras.setOnClickListener { finish() }
    }

    // Pone en los campos lo que hay guardado (al editar) y elige la categoría
    private fun rellenar(datos: DatosFormulario, categoriaDeLaCaja: Long) {
        val plato = datos.plato
        if (plato != null) {
            binding.textoNombre.setText(plato.nombre)
            binding.textoNumero.setText(plato.numero.toString())
            binding.textoPrecio.setText(Formato.precio(plato.precioCentimos))   // 150 → «1,50»
            binding.textoDescripcion.setText(plato.descripcion)
            binding.interruptorEnLaCarta.isChecked = plato.activo
        }
        // Los alérgenos que ya lleva el plato (vacío si es nuevo)
        viewModel.alergenosMarcados.addAll(datos.alergenosDelPlato)

        // La categoría: la del plato; si es nuevo, la de su caja; si no llegó ninguna,
        // la de por defecto, buscada por su columna y nunca por el nombre (ficha 3, R16)
        val idBuscado = plato?.categoriaId ?: categoriaDeLaCaja
        val categoria = datos.categorias.find { it.id == idBuscado }
            ?: datos.categorias.first { it.esPorDefecto }
        viewModel.categoriaElegidaId = categoria.id
    }

    // Una casilla por cada alérgeno de la base de datos (no se escriben en el XML: son datos, ficha 3).
    // Cuáles están marcadas lo dice la libreta (P66 A)
    private fun pintarAlergenos(alergenos: List<Alergeno>) {
        binding.rejillaAlergenos.removeAllViews()
        for (alergeno in alergenos) {
            val casilla = MaterialCheckBox(this)
            casilla.text = alergeno.nombre
            casilla.isChecked = alergeno.id in viewModel.alergenosMarcados
            // Al tocarla, se apunta o se borra en la libreta
            casilla.setOnCheckedChangeListener { _, marcada ->
                if (marcada) {
                    viewModel.alergenosMarcados.add(alergeno.id)
                } else {
                    viewModel.alergenosMarcados.remove(alergeno.id)
                }
            }
            // [Claude] Dos columnas del mismo ancho: ancho 0 y la columna con peso 1
            val sitio = GridLayout.LayoutParams(
                GridLayout.spec(GridLayout.UNDEFINED),
                GridLayout.spec(GridLayout.UNDEFINED, 1f)
            )
            sitio.width = 0
            binding.rejillaAlergenos.addView(casilla, sitio)
        }
    }

    // [Claude] Los nombres de lo que se grapa a la nota. Están aquí para que el Panel
    // y esta pantalla usen exactamente la misma palabra
    companion object {
        const val EXTRA_PRODUCTO_ID = "producto_id"
        const val EXTRA_CATEGORIA_ID = "categoria_id"
    }
}