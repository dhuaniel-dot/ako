package yunkang.ako.ui.plato

import android.os.Bundle
import android.widget.GridLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.checkbox.MaterialCheckBox
import kotlinx.coroutines.launch
import yunkang.ako.R
import yunkang.ako.datos.entidades.Alergeno
import yunkang.ako.datos.entidades.Producto
import yunkang.ako.databinding.ActivityPlatoBinding
import yunkang.ako.dominio.modelos.ResultadoGuardado
import yunkang.ako.ui.comun.ConfirmacionDialog
import yunkang.ako.ui.comun.Formato
import yunkang.ako.ui.comun.MesasAfectadasDialog

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

            // Con los datos ya puestos, se mira cómo queda Guardar y si el número cambió
            actualizarGuardar()
            avisarNumeroCambiado()
        }

        // Cada cambio en nombre, número o precio vuelve a mirar si Guardar se puede encender
        binding.textoNombre.doAfterTextChanged { actualizarGuardar() }
        binding.textoNumero.doAfterTextChanged {
            binding.campoNumero.error = null      // al cambiar el número, se quita el «repetido»
            actualizarGuardar()
            avisarNumeroCambiado()
        }
        binding.textoPrecio.doAfterTextChanged { actualizarGuardar() }

        // Guardar: si se va a eliminar el plato, PRIMERO se miran las mesas (R6 en dos pasos, P128)
        binding.botonGuardar.setOnClickListener {
            binding.botonGuardar.isEnabled = false      // P142: sin doble toque mientras trabaja
            lifecycleScope.launch {
                if (seVaAEliminar()) {
                    val mesas = viewModel.mesasAfectadas()
                    if (mesas.isNotEmpty()) {
                        // Con mesas: decide el aviso 3d; su respuesta llega al sobre de abajo
                        val nombre = checkNotNull(viewModel.datos.value?.plato).nombre
                        MesasAfectadasDialog.abrirPlato(supportFragmentManager, this@PlatoActivity, nombre, mesas)
                        actualizarGuardar()
                        return@launch
                    }
                }
                // [Claude] Sin mesas no hay nada que avisar: se guarda directamente
                guardar()
            }
        }

        // El sobre del aviso 3d: se abre y se escucha en el MISMO gestor (supportFragmentManager),
        // y se escucha desde aquí, onCreate, para que la respuesta encuentre a alguien si Android recrea la pantalla
        supportFragmentManager.setFragmentResultListener(MesasAfectadasDialog.CLAVE_PLATO, this) { _, sobre ->
            if (sobre.getBoolean(ConfirmacionDialog.RESPUESTA_AFIRMATIVA)) {
                // Eliminar: ahora sí se guarda y se elimina
                binding.botonGuardar.isEnabled = false
                lifecycleScope.launch { guardar() }
            }
            // Cancelar: no se guarda nada y el formulario sigue como lo dejó el Propietario
        }

        // El sobre del primer aviso 3e («¿Muevo el plato a Otros?»), también escuchado desde onCreate
        supportFragmentManager.setFragmentResultListener(CLAVE_3E_MOVER, this) { _, sobre ->
            if (sobre.getBoolean(ConfirmacionDialog.RESPUESTA_AFIRMATIVA)) {
                // Sí, mover: el plato pasa a la categoría por defecto (R16: por la columna) y se vuelve a guardar
                val porDefecto = checkNotNull(viewModel.datos.value).categorias.first { it.esPorDefecto }
                viewModel.categoriaElegidaId = porDefecto.id
                binding.textoCategoria.setText(porDefecto.nombre, false)
                binding.botonGuardar.isEnabled = false
                lifecycleScope.launch { guardar() }
            } else {
                // No: segundo aviso, «¿Quieres recuperar Postres?»
                lifecycleScope.launch { preguntarRecuperar() }
            }
            // Cerrar sin contestar no deja sobre (P144 A): no se guarda nada
        }

        // El sobre del segundo aviso 3e («¿Quieres recuperar Postres?»)
        supportFragmentManager.setFragmentResultListener(CLAVE_3E_RECUPERAR, this) { _, sobre ->
            binding.botonGuardar.isEnabled = false
            if (sobre.getBoolean(ConfirmacionDialog.RESPUESTA_AFIRMATIVA)) {
                // Recuperar: la categoría vuelve con sus platos y el plato se guarda ahí, visible
                val categoriaId = checkNotNull(viewModel.categoriaElegidaId)
                lifecycleScope.launch {
                    viewModel.recuperarCategoria(categoriaId)
                    guardar()
                }
            } else {
                // No: «ya es decisión del propietario» (ficha 3). Se guarda ahí, invisible (R15)
                lifecycleScope.launch { guardar(aunqueCategoriaEliminada = true) }
            }
        }

        binding.botonAtras.setOnClickListener { finish() }
    }

    // [Claude] Convierte lo que hay en pantalla en un Producto listo para guardar.
    // Solo se llama con Guardar encendido: los cuatro obligatorios ya se pueden leer
    private fun leerFormulario(): Producto {
        val antes = viewModel.datos.value?.plato               // null = plato nuevo
        val descripcion = binding.textoDescripcion.text.toString().trim()
        return Producto(
            id = antes?.id ?: 0L,                                 // 0 = Room le dará uno nuevo
            categoriaId = checkNotNull(viewModel.categoriaElegidaId),
            numero = binding.textoNumero.text.toString().toInt(),
            nombre = binding.textoNombre.text.toString().trim(),
            descripcion = if (descripcion.isEmpty()) null else descripcion,   // vacía = sin descripción
            precioCentimos = checkNotNull(Formato.centimosDesde(binding.textoPrecio.text.toString())),
            imagen = antes?.imagen,                               // la foto llega en la S12
            // Al editar, como estaba (P150: solo eliminar/recuperar lo cambian). Al crear, el interruptor
            activo = antes?.activo ?: binding.interruptorEnLaCarta.isChecked
        )
    }

    // [Claude] ¿El Propietario ha apagado «En la carta» de un plato que estaba en la carta?
    private fun seVaAEliminar(): Boolean {
        val antes = viewModel.datos.value?.plato ?: return false   // al crear no hay nada que eliminar
        return antes.activo && !binding.interruptorEnLaCarta.isChecked
    }

    // [Claude] ¿Lo ha encendido en un plato eliminado?
    private fun seVaARecuperar(): Boolean {
        val antes = viewModel.datos.value?.plato ?: return false
        return !antes.activo && binding.interruptorEnLaCarta.isChecked
    }

    // Guarda y, si el interruptor cambió, elimina o recupera (como la hoja 2b, P150).
    // aunqueCategoriaEliminada solo lo pone el «No» del segundo aviso 3e
    private suspend fun guardar(aunqueCategoriaEliminada: Boolean = false) {
        when (viewModel.guardar(leerFormulario(), aunqueCategoriaEliminada)) {
            ResultadoGuardado.Ok -> {
                if (seVaAEliminar()) {
                    viewModel.eliminar()          // R5: activo = false; ninguna línea se toca (R6)
                } else if (seVaARecuperar()) {
                    viewModel.recuperar()
                }
                // Vuelta al Panel, que ya está al día él solo (P49 B)
                finish()
            }
            // R9: error rojo en el campo y la pantalla sigue abierta
            ResultadoGuardado.NumeroRepetido -> {
                binding.campoNumero.error = getString(R.string.plato_numero_repetido)
                actualizarGuardar()
            }
            // Cadena 3e (P62): la categoría elegida está eliminada; primero se pregunta si se mueve
            ResultadoGuardado.CategoriaEliminada -> preguntarMover()
            // Solo lo devuelven las categorías: guardarPlato nunca lo da
            ResultadoGuardado.NombreRepetido -> actualizarGuardar()
        }
    }

    // 3e, primer aviso: «La categoría Postres está eliminada. ¿Muevo el plato a Otros?»
    // El nombre de la de por defecto sale de la base de datos: se puede renombrar (R16, D20)
    private fun preguntarMover() {
        val datos = checkNotNull(viewModel.datos.value)
        val elegida = datos.categorias.first { it.id == viewModel.categoriaElegidaId }
        val porDefecto = datos.categorias.first { it.esPorDefecto }
        ConfirmacionDialog.nueva(
            titulo = getString(R.string.panel_categoria_eliminada),
            texto = getString(R.string.categoria_eliminada_cuerpo, elegida.nombre, porDefecto.nombre),
            afirmativo = getString(R.string.categoria_eliminada_btn_mover),
            negativo = getString(R.string.comun_no),
            clave = CLAVE_3E_MOVER
        ).show(supportFragmentManager, "3e_mover")
        actualizarGuardar()
    }

    // 3e, segundo aviso: «¿Quieres recuperar Postres? Platos que volverán a la carta: 2» (P164 B)
    private suspend fun preguntarRecuperar() {
        val datos = checkNotNull(viewModel.datos.value)
        val elegida = datos.categorias.first { it.id == viewModel.categoriaElegidaId }
        val cuantos = viewModel.platosQueVuelven(elegida.id)
        ConfirmacionDialog.nueva(
            titulo = getString(R.string.recuperar_categoria_titulo, elegida.nombre),
            texto = getString(R.string.recuperar_categoria_cuerpo, cuantos),
            afirmativo = getString(R.string.recuperar_categoria_btn_recuperar),
            negativo = getString(R.string.comun_no),
            clave = CLAVE_3E_RECUPERAR
        ).show(supportFragmentManager, "3e_recuperar")
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

    // [Claude] Guardar se enciende solo con los cuatro obligatorios (ficha 3: «todo campo es opcional
    // salvo que una regla o la base de datos lo exijan»). Descripción y alérgenos no cuentan
    private fun actualizarGuardar() {
        val nombreBien = binding.textoNombre.text.toString().isNotBlank()
        val numeroBien = binding.textoNumero.text.toString().toIntOrNull() != null   // «07» se lee como 7
        val precioBien = Formato.centimosDesde(binding.textoPrecio.text.toString()) != null
        val categoriaBien = viewModel.categoriaElegidaId != null
        binding.botonGuardar.isEnabled = nombreBien && numeroBien && precioBien && categoriaBien
    }

    // [Claude] Al editar, si el número tecleado ya no es el guardado, una AYUDA (gris, no bloquea)
    // con el número ANTERIOR: «Si alguien tiene apuntado el 12, dejará de corresponder»
    private fun avisarNumeroCambiado() {
        val plato = viewModel.datos.value?.plato
        val tecleado = binding.textoNumero.text.toString().toIntOrNull()
        if (plato != null && tecleado != null && tecleado != plato.numero) {
            binding.campoNumero.helperText = getString(R.string.plato_numero_cambiado, plato.numero)
        } else {
            binding.campoNumero.helperText = null
        }
    }

    // [Claude] Los nombres de lo que se grapa a la nota. Están aquí para que el Panel
    // y esta pantalla usen exactamente la misma palabra
    companion object {
        const val EXTRA_PRODUCTO_ID = "producto_id"
        const val EXTRA_CATEGORIA_ID = "categoria_id"

        // [Claude] Los nombres de los sobres de los dos avisos 3e (cada pregunta, su sobre)
        const val CLAVE_3E_MOVER = "3e_mover"
        const val CLAVE_3E_RECUPERAR = "3e_recuperar"
    }
}