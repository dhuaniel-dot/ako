package yunkang.ako.ui.pedido

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import yunkang.ako.R
import yunkang.ako.databinding.FragmentFichaPlatoBinding
import yunkang.ako.datos.entidades.Producto
import yunkang.ako.dominio.Calculadora
import yunkang.ako.dominio.Validacion
import yunkang.ako.ui.comun.Formato
import java.io.File

// 5b · Ficha del plato, a pantalla completa encima de la carta (P84): sus datos, los alérgenos
// en un desplegable, la cantidad de 1 a 99 y «Añadir» al carrito
class FichaPlatoFragment : Fragment(R.layout.fragment_ficha_plato) {

    // La libreta de la Activity: la misma que la carta y el carrito
    private val viewModel: PedidoViewModel by activityViewModels { PedidoViewModel.Factory }

    // El "mando" de las vistas; cambia cada vez que Android vuelve a crear la vista
    private lateinit var binding: FragmentFichaPlatoBinding

    // [Claude] La cantidad elegida (de 1 a 99, R4); se guarda si Android rehace la pantalla
    private var cantidad = 1

    // [Claude] El plato de esta ficha, para el importe del botón y para Añadir; vacío hasta que llega
    private var plato: Producto? = null

    // [Claude] ¿Está abierto el desplegable de alérgenos? Plegado al abrir la ficha; se guarda como la cantidad (H07)
    private var alergenosAbiertos = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFichaPlatoBinding.bind(view)

        // ← Atrás hace lo mismo que el del sistema: con la ficha encima, el guardián de Pedir está apagado
        // y Android quita la ficha de la pila: se vuelve a la carta, sin PIN
        binding.botonAtras.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Si Android rehízo la pantalla, se recuperan la cantidad y el desplegable como estaban
        cantidad = savedInstanceState?.getInt(CLAVE_CANTIDAD) ?: 1
        alergenosAbiertos = savedInstanceState?.getBoolean(CLAVE_ALERGENOS_ABIERTOS) ?: false

        // P170 B: se pide el plato y se pinta cuando llega su bandeja
        val productoId = requireArguments().getLong(ARG_PRODUCTO_ID)
        viewModel.cargarFicha(productoId)
        viewModel.ficha.observe(viewLifecycleOwner) { datos ->
            // Bandeja vacía o de otro plato: el nuestro todavía no ha llegado
            if (datos == null || datos.plato.id != productoId) return@observe
            val plato = datos.plato
            this.plato = plato

            // La foto grande (spec 9): Glide siempre, también sin foto (null → el «?» grande);
            // la foto, recortada a todo el marco (decisión 3 B)
            Glide.with(binding.imagenPlato)
                .load(plato.imagen?.let { File(it) })
                .placeholder(R.drawable.foto_cargando)
                .error(R.drawable.ic_sin_foto_grande)
                .centerCrop()
                .into(binding.imagenPlato)
            // Sin foto, «Sin foto» (spec 9); con foto, el nombre del plato
            if (plato.imagen == null) {
                binding.imagenPlato.contentDescription = getString(R.string.comun_sin_foto_cd)
            } else {
                binding.imagenPlato.contentDescription = plato.nombre
            }

            // «12 · Entrecot» entero y «18,50 €»; la descripción solo si la tiene
            binding.textoNumeroNombre.text = getString(R.string.comun_plato_numero_nombre, plato.numero, plato.nombre)
            binding.textoPrecio.text = getString(R.string.comun_precio, Formato.precio(plato.precioCentimos))
            binding.textoDescripcion.text = plato.descripcion
            binding.textoDescripcion.visibility = if (plato.descripcion.isNullOrBlank()) View.GONE else View.VISIBLE

            // Los alérgenos, uno por línea con «•»; sin ninguno, el aviso de pedir al personal (ficha 5)
            binding.textoAlergenos.text = if (datos.alergenos.isEmpty()) {
                getString(R.string.ficha_sin_alergenos)
            } else {
                datos.alergenos.joinToString("\n") { getString(R.string.ficha_alergeno_item, it.nombre) }
            }
            pintarCantidad()
        }

        // El desplegable se abre y se cierra tocando su cabecera (sin gestos, ficha 5)
        binding.cabeceraAlergenos.setOnClickListener {
            alergenosAbiertos = !alergenosAbiertos
            pintarDesplegable()
        }
        pintarDesplegable()

        // − y +: de 1 en 1, sin salirse de 1–99 (los botones se apagan en los límites)
        binding.botonMenos.setOnClickListener {
            if (cantidad > 1) cantidad--
            pintarCantidad()
        }
        binding.botonMas.setOnClickListener {
            if (cantidad < Validacion.MAXIMO_POR_PLATO) cantidad++
            pintarCantidad()
        }
        pintarCantidad()

        // Añadir: al carrito de la libreta y de vuelta a la carta. Si ha topado en 99, se avisa (P74 A);
        // el aviso va sobre la vista de la Activity porque la ficha se cierra en ese mismo momento
        binding.botonAnadir.setOnClickListener {
            val elegido = plato ?: return@setOnClickListener
            val sinTopar = viewModel.anadir(elegido, cantidad)
            if (!sinTopar) {
                Snackbar.make(requireActivity().findViewById(R.id.contenedor), R.string.carrito_tope_99, Snackbar.LENGTH_LONG).show()
            }
            parentFragmentManager.popBackStack()
        }
    }

    // Guarda la cantidad y el desplegable por si Android rehace la pantalla (el resto se vuelve a pintar
    // desde la bandeja)
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CLAVE_CANTIDAD, cantidad)
        outState.putBoolean(CLAVE_ALERGENOS_ABIERTOS, alergenosAbiertos)   // H07 (revisión del 8 oct)
    }

    // Abierto: se ven los alérgenos y la flecha apunta abajo; cerrado: ocultos y flecha a la derecha
    private fun pintarDesplegable() {
        binding.textoAlergenos.visibility = if (alergenosAbiertos) View.VISIBLE else View.GONE
        binding.flechaAlergenos.rotation = if (alergenosAbiertos) 0f else -90f
    }

    // R4 en la pantalla: − apagado en 1 y + apagado en 99 (el 99 está escrito solo en Validacion, P121).
    // El botón dice el importe de la cantidad elegida: precio × cantidad (R10, Calculadora)
    private fun pintarCantidad() {
        binding.textoCantidad.text = getString(R.string.comun_numero, cantidad)
        binding.botonMenos.isEnabled = cantidad > 1
        binding.botonMas.isEnabled = cantidad < Validacion.MAXIMO_POR_PLATO
        val importe = Calculadora.importe(plato?.precioCentimos ?: 0, cantidad)
        binding.botonAnadir.text =
            getString(R.string.ficha_btn_anadir, getString(R.string.comun_precio, Formato.precio(importe)))
    }

    companion object {
        // [Claude] Los nombres del id del plato dentro de los arguments y de lo que se guarda
        private const val ARG_PRODUCTO_ID = "producto_id"
        private const val CLAVE_CANTIDAD = "cantidad"
        private const val CLAVE_ALERGENOS_ABIERTOS = "alergenos_abiertos"

        // La forma de crear una ficha: se le da el id del plato (en los arguments, no en el constructor)
        fun nueva(productoId: Long): FichaPlatoFragment {
            val ficha = FichaPlatoFragment()
            ficha.arguments = bundleOf(ARG_PRODUCTO_ID to productoId)
            return ficha
        }
    }
}