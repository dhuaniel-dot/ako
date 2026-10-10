package yunkang.ako.ui.plato

import android.os.Bundle
import android.widget.GridLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import yunkang.ako.R
import yunkang.ako.datos.entidades.Alergeno
import yunkang.ako.datos.entidades.Producto
import yunkang.ako.databinding.ActivityPlatoBinding
import yunkang.ako.dominio.modelos.ResultadoGuardado
import yunkang.ako.ui.comun.ConfirmacionDialog
import yunkang.ako.ui.comun.Formato
import yunkang.ako.ui.comun.MesasAfectadasDialog
import yunkang.ako.ui.comun.apartarDeLasBarras
import yunkang.ako.ui.comun.pintarFoto
import java.io.IOException

// 3a · formulario del plato. se abre desde el Panel: «+ Plato» de una caja (crear)
// o tocando un plato (editar)
class PlatoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlatoBinding

    private val viewModel: PlatoViewModel by viewModels { PlatoViewModel.Factory }

    // el guardián de Atrás. solo está encendido cuando hay cambios sin guardar;
    // apagado, Atrás cierra la pantalla sin preguntar
    private val guardianAtras = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            preguntarSalir()
        }
    }

    // el selector de fotos del sistema: sin permisos, la app solo recibe la foto que se toca
    // la foto elegida cuenta como cambio sin guardar
    // [Claude] se registra al crear la pantalla, no en el clic: así la foto vuelve aunque Android recree la pantalla
    private val selectorFotos = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // null = se canceló el selector: no cambia nada
        if (uri != null) {
            viewModel.elegirFoto(uri)
            marcarCambio()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // de borde a borde, apartado de las barras y del teclado (como SelectorActivity)
        enableEdgeToEdge()
        binding = ActivityPlatoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.raiz.apartarDeLasBarras()

        // lo que viene grapado a la nota (Intent). −1 = «no me han dado nada»
        val productoId = intent.getLongExtra(EXTRA_PRODUCTO_ID, -1L)
        val categoriaDeLaCaja = intent.getLongExtra(EXTRA_CATEGORIA_ID, -1L)

        if (productoId == -1L) {
            binding.textoTitulo.setText(R.string.plato_titulo_nuevo)
        } else {
            binding.textoTitulo.setText(R.string.plato_titulo_editar)
        }

        // se pide la bandeja siempre; si la libreta ya la tiene, no vuelve a leer
        if (productoId == -1L) {
            viewModel.cargar(null)
        } else {
            viewModel.cargar(productoId)
        }

        viewModel.datos.observe(this) { datos ->
            // la lista del desplegable se monta cada vez: si Android recrea la pantalla, la vista es nueva
            // todas las categorías, también las eliminadas, sin marca; la de por defecto, la última
            val nombres = datos.categorias.map { it.nombre }.toTypedArray()
            binding.textoCategoria.setSimpleItems(nombres)
            binding.textoCategoria.setOnItemClickListener { _, _, posicion, _ ->
                viewModel.categoriaElegidaId = datos.categorias[posicion].id
                marcarCambio()
            }

            // los campos se rellenan una sola vez; después manda lo que teclee el Propietario
            if (!viewModel.formularioRelleno) {
                rellenar(datos, categoriaDeLaCaja)
                viewModel.formularioRelleno = true
            }

            // la categoría elegida vive en la libreta y se pinta cada vez (el campo tiene saveEnabled="false")
            // false = no filtrar la lista por lo escrito; si no, enseñaría solo esa
            val elegida = datos.categorias.find { it.id == viewModel.categoriaElegidaId }
            if (elegida != null) {
                binding.textoCategoria.setText(elegida.nombre, false)
            }

            // las casillas también se crean cada vez (vista nueva tras una recreación)
            pintarAlergenos(datos.alergenos)

            // la foto que ya tenía el plato (si no se ha elegido otra)
            pintarFoto()

            // con los datos ya puestos, se mira cómo queda Guardar y si el número cambió
            actualizarGuardar()
            avisarNumeroCambiado()
        }

        // la foto elegida y aún sin guardar: cada vez que cambia, se vuelve a pintar el hueco
        viewModel.fotoElegida.observe(this) { pintarFoto() }

        binding.botonElegirFoto.setOnClickListener {
            selectorFotos.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // guardar: si se va a eliminar el plato, primero se miran las mesas para avisar; después se elimina
        // mientras trabaja, Guardar se apaga: sin doble toque
        binding.botonGuardar.setOnClickListener {
            binding.botonGuardar.isEnabled = false
            lifecycleScope.launch {
                if (seVaAEliminar()) {
                    val mesas = viewModel.mesasAfectadas()
                    if (mesas.isNotEmpty()) {
                        // con mesas: decide el aviso 3d; su respuesta llega al sobre de abajo
                        val nombre = checkNotNull(viewModel.datos.value?.plato).nombre
                        MesasAfectadasDialog.abrirPlato(supportFragmentManager, this@PlatoActivity, nombre, mesas)
                        actualizarGuardar()
                        return@launch
                    }
                }
                guardar()
            }
        }

        // el sobre del aviso 3d: mismo gestor (supportFragmentManager) y escuchado desde onCreate,
        // para que la respuesta llegue aunque Android recree la pantalla
        supportFragmentManager.setFragmentResultListener(MesasAfectadasDialog.CLAVE_PLATO, this) { _, sobre ->
            if (sobre.getBoolean(ConfirmacionDialog.RESPUESTA_AFIRMATIVA)) {
                // eliminar: ahora sí se guarda y se elimina
                binding.botonGuardar.isEnabled = false
                lifecycleScope.launch { guardar() }
            }
            // cancelar: no se guarda nada y el formulario sigue como lo dejó el Propietario
        }

        // el sobre del primer aviso 3e («Muevo el plato a Otros?»), también escuchado desde onCreate
        supportFragmentManager.setFragmentResultListener(CLAVE_3E_MOVER, this) { _, sobre ->
            if (sobre.getBoolean(ConfirmacionDialog.RESPUESTA_AFIRMATIVA)) {
                // sí, mover: el plato pasa a la categoría por defecto (buscada por la columna) y se vuelve a guardar
                val porDefecto = checkNotNull(viewModel.datos.value).categorias.first { it.esPorDefecto }
                viewModel.categoriaElegidaId = porDefecto.id
                binding.textoCategoria.setText(porDefecto.nombre, false)
                binding.botonGuardar.isEnabled = false
                lifecycleScope.launch { guardar() }
            } else {
                // no: segundo aviso, «Quieres recuperar Postres?»
                lifecycleScope.launch { preguntarRecuperar() }
            }
            // cerrar sin contestar no deja sobre: no se guarda nada
        }

        // el sobre del segundo aviso 3e («Quieres recuperar Postres?»)
        supportFragmentManager.setFragmentResultListener(CLAVE_3E_RECUPERAR, this) { _, sobre ->
            binding.botonGuardar.isEnabled = false
            if (sobre.getBoolean(ConfirmacionDialog.RESPUESTA_AFIRMATIVA)) {
                // recuperar: la categoría vuelve con sus platos y el plato se guarda ahí, visible
                val categoriaId = checkNotNull(viewModel.categoriaElegidaId)
                lifecycleScope.launch {
                    viewModel.recuperarCategoria(categoriaId)
                    guardar()
                }
            } else {
                // no: «ya es decisión del propietario» (ficha 3). se guarda ahí, invisible
                lifecycleScope.launch { guardar(aunqueCategoriaEliminada = true) }
            }
        }

        // el sobre de «Salir sin guardar?»: Salir cierra sin guardar; Cancelar, nada
        // la foto elegida no se copió todavía: al salir no queda ningún archivo que borrar
        supportFragmentManager.setFragmentResultListener(CLAVE_SALIR, this) { _, sobre ->
            if (sobre.getBoolean(ConfirmacionDialog.RESPUESTA_AFIRMATIVA)) {
                finish()
            }
        }

        // el guardián se apunta en la lista de Android y se enciende si ya había cambios
        // (por ejemplo, si Android ha recreado la pantalla)
        onBackPressedDispatcher.addCallback(this, guardianAtras)
        guardianAtras.isEnabled = viewModel.hayCambios

        // la flecha hace exactamente lo mismo que el Atrás del sistema: pasa por el guardián
        binding.botonAtras.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    // [Claude] los timbres de los campos van aquí y no en onCreate: si no, lo que Android devuelve a los campos al recrear contaría como cambio
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // cada cambio en nombre, número o precio vuelve a mirar si Guardar se puede encender
        // (y cuenta como cambio sin guardar)
        binding.textoNombre.doAfterTextChanged {
            actualizarGuardar()
            marcarCambio()
        }
        binding.textoNumero.doAfterTextChanged {
            binding.campoNumero.error = null
            actualizarGuardar()
            avisarNumeroCambiado()
            marcarCambio()
        }
        binding.textoPrecio.doAfterTextChanged {
            actualizarGuardar()
            marcarCambio()
        }
        binding.textoDescripcion.doAfterTextChanged { marcarCambio() }
        binding.interruptorEnLaCarta.setOnCheckedChangeListener { _, _ -> marcarCambio() }
    }

    // [Claude] apunta que hay cambios y enciende el guardián. rellenar el formulario no cuenta
    private fun marcarCambio() {
        if (!viewModel.formularioRelleno) return
        viewModel.hayCambios = true
        guardianAtras.isEnabled = true
    }

    // lo que enseña el hueco: la foto elegida y aún sin guardar; si no, la que ya tenía el plato; si tampoco, el «?»
    private fun pintarFoto() {
        val foto = viewModel.fotoElegida.value ?: viewModel.datos.value?.plato?.imagen
        pintarFoto(binding.imagenPlato, foto, binding.textoNombre.text.toString(), grande = true)
    }

    // «Salir sin guardar? Se perderán los cambios» (ficha 3: no hay nada vivo entre pantallas)
    private fun preguntarSalir() {
        ConfirmacionDialog.nueva(
            titulo = getString(R.string.plato_salir_titulo),
            texto = getString(R.string.plato_salir_cuerpo),
            afirmativo = getString(R.string.carta_btn_salir),
            negativo = getString(R.string.comun_cancelar),
            clave = CLAVE_SALIR
        ).show(supportFragmentManager, CLAVE_SALIR)
    }

    // [Claude] lo que hay en pantalla → un Producto listo para guardar. solo con Guardar encendido (los obligatorios ya se leen)
    // nuevo: id 0 (Room le da uno). la foto es la de antes; si se eligió otra, la pone la libreta
    private fun leerFormulario(): Producto {
        val antes = viewModel.datos.value?.plato
        val descripcion = binding.textoDescripcion.text.toString().trim()
        return Producto(
            id = antes?.id ?: 0L,
            categoriaId = checkNotNull(viewModel.categoriaElegidaId),
            numero = binding.textoNumero.text.toString().toInt(),
            nombre = binding.textoNombre.text.toString().trim(),
            descripcion = if (descripcion.isEmpty()) null else descripcion,
            precioCentimos = checkNotNull(Formato.centimosDesde(binding.textoPrecio.text.toString())),
            imagen = antes?.imagen,
            // al editar, como estaba (solo eliminar/recuperar lo cambian). al crear, el interruptor
            activo = antes?.activo ?: binding.interruptorEnLaCarta.isChecked
        )
    }

    // al crear no hay nada que eliminar
    private fun seVaAEliminar(): Boolean {
        val antes = viewModel.datos.value?.plato ?: return false
        return antes.activo && !binding.interruptorEnLaCarta.isChecked
    }

    private fun seVaARecuperar(): Boolean {
        val antes = viewModel.datos.value?.plato ?: return false
        return !antes.activo && binding.interruptorEnLaCarta.isChecked
    }

    // guarda y, si el interruptor cambió, elimina o recupera (como la hoja 2b)
    // aunqueCategoriaEliminada solo lo pone el «No» del segundo aviso 3e
    private suspend fun guardar(aunqueCategoriaEliminada: Boolean = false) {
        // si la foto elegida no se puede leer, la libreta lanza el error y no se guarda nada
        // [Claude] SecurityException: el préstamo de la foto ya no vale
        val resultado = try {
            viewModel.guardar(leerFormulario(), aunqueCategoriaEliminada)
        } catch (e: IOException) {
            fotoNoUsable()
            return
        } catch (e: SecurityException) {
            fotoNoUsable()
            return
        }
        when (resultado) {
            ResultadoGuardado.Ok -> {
                if (seVaAEliminar()) {
                    viewModel.eliminar()
                } else if (seVaARecuperar()) {
                    viewModel.recuperar()
                }
                // vuelta al Panel, que ya está al día él solo
                finish()
            }
            // el número ya es de otro plato: error rojo en el campo y la pantalla sigue abierta
            ResultadoGuardado.NumeroRepetido -> {
                binding.campoNumero.error = getString(R.string.plato_numero_repetido)
                actualizarGuardar()
            }
            // cadena 3e: la categoría elegida está eliminada; primero se pregunta si se mueve
            ResultadoGuardado.CategoriaEliminada -> preguntarMover()
            // solo lo devuelven las categorías: guardarPlato nunca lo da
            ResultadoGuardado.NombreRepetido -> actualizarGuardar()
        }
    }

    // [Claude] la foto elegida no se ha podido usar (archivo roto, no es una imagen, préstamo caducado):
    // se olvida, se avisa y el formulario sigue abierto; al volver a Guardar, se guarda sin ella
    private fun fotoNoUsable() {
        viewModel.olvidarFoto()
        Snackbar.make(binding.raiz, R.string.foto_error, Snackbar.LENGTH_LONG).show()
        actualizarGuardar()
    }

    // 3e, primer aviso: «La categoría Postres está eliminada. Muevo el plato a Otros?»
    // el nombre de la de por defecto sale de la base de datos: se puede renombrar
    private fun preguntarMover() {
        // llega tras copiar la foto; si mientras tanto la app pasó a segundo plano,
        // la caja no se puede abrir: no se abre y Guardar se vuelve a encender
        if (supportFragmentManager.isStateSaved) {
            actualizarGuardar()
            return
        }
        val datos = checkNotNull(viewModel.datos.value)
        val elegida = datos.categorias.first { it.id == viewModel.categoriaElegidaId }
        val porDefecto = datos.categorias.first { it.esPorDefecto }
        ConfirmacionDialog.nueva(
            titulo = getString(R.string.panel_categoria_eliminada),
            texto = getString(R.string.categoria_eliminada_cuerpo, elegida.nombre, porDefecto.nombre),
            afirmativo = getString(R.string.categoria_eliminada_btn_mover),
            negativo = getString(R.string.comun_no),
            clave = CLAVE_3E_MOVER
        ).show(supportFragmentManager, CLAVE_3E_MOVER)
        actualizarGuardar()
    }

    // 3e, segundo aviso: «Quieres recuperar Postres? Platos que volverán a la carta: 2»
    private suspend fun preguntarRecuperar() {
        val datos = checkNotNull(viewModel.datos.value)
        val elegida = datos.categorias.first { it.id == viewModel.categoriaElegidaId }
        val cuantos = viewModel.platosQueVuelven(elegida.id)
        // lo mismo que en preguntarMover, tras esperar a la cuenta de platos
        if (supportFragmentManager.isStateSaved) {
            actualizarGuardar()
            return
        }
        ConfirmacionDialog.nueva(
            titulo = getString(R.string.recuperar_categoria_titulo, elegida.nombre),
            texto = getString(R.string.recuperar_categoria_cuerpo, cuantos),
            afirmativo = getString(R.string.recuperar_categoria_btn_recuperar),
            negativo = getString(R.string.comun_no),
            clave = CLAVE_3E_RECUPERAR
        ).show(supportFragmentManager, CLAVE_3E_RECUPERAR)
    }

    // pone en los campos lo que hay guardado (al editar) y elige la categoría
    private fun rellenar(datos: DatosFormulario, categoriaDeLaCaja: Long) {
        val plato = datos.plato
        if (plato != null) {
            binding.textoNombre.setText(plato.nombre)
            binding.textoNumero.setText(getString(R.string.comun_numero, plato.numero))
            binding.textoPrecio.setText(Formato.precio(plato.precioCentimos))
            binding.textoDescripcion.setText(plato.descripcion)
            binding.interruptorEnLaCarta.isChecked = plato.activo
        }
        // los alérgenos que ya lleva el plato (vacío si es nuevo), apuntados en la libreta uno a uno
        for (id in datos.alergenosDelPlato) viewModel.marcarAlergeno(id, true)

        // la categoría: la del plato; si es nuevo, la de su caja; si no llegó ninguna,
        // la de por defecto, buscada por su columna y nunca por el nombre (ficha 3)
        val idBuscado = plato?.categoriaId ?: categoriaDeLaCaja
        val categoria = datos.categorias.find { it.id == idBuscado }
            ?: datos.categorias.first { it.esPorDefecto }
        viewModel.categoriaElegidaId = categoria.id
    }

    // una casilla por cada alérgeno de la base de datos (no se escriben en el XML: son datos, ficha 3)
    // cuáles están marcadas lo dice la libreta
    private fun pintarAlergenos(alergenos: List<Alergeno>) {
        binding.rejillaAlergenos.removeAllViews()
        for (alergeno in alergenos) {
            val casilla = MaterialCheckBox(this)
            casilla.text = alergeno.nombre
            casilla.isChecked = alergeno.id in viewModel.alergenosMarcados
            // al tocarla, se apunta o se borra en la libreta (y en su caja fuerte)
            casilla.setOnCheckedChangeListener { _, marcada ->
                viewModel.marcarAlergeno(alergeno.id, marcada)
                marcarCambio()
            }
            // [Claude] dos columnas del mismo ancho: ancho 0 y la columna con peso 1
            val sitio = GridLayout.LayoutParams(
                GridLayout.spec(GridLayout.UNDEFINED),
                GridLayout.spec(GridLayout.UNDEFINED, 1f)
            )
            sitio.width = 0
            binding.rejillaAlergenos.addView(casilla, sitio)
        }
    }

    // [Claude] guardar se enciende solo con los cuatro obligatorios (ficha 3); descripción y alérgenos no cuentan
    // en el número, «07» se lee como 7
    private fun actualizarGuardar() {
        val nombreBien = binding.textoNombre.text.toString().isNotBlank()
        val numeroBien = binding.textoNumero.text.toString().toIntOrNull() != null
        val precioBien = Formato.centimosDesde(binding.textoPrecio.text.toString()) != null
        val categoriaBien = viewModel.categoriaElegidaId != null
        binding.botonGuardar.isEnabled = nombreBien && numeroBien && precioBien && categoriaBien
    }

    // [Claude] al editar, si el número tecleado ya no es el guardado, una ayuda (gris, no bloquea)
    // con el número anterior: «Si alguien tiene apuntado el 12, dejará de corresponder»
    private fun avisarNumeroCambiado() {
        val plato = viewModel.datos.value?.plato
        val tecleado = binding.textoNumero.text.toString().toIntOrNull()
        if (plato != null && tecleado != null && tecleado != plato.numero) {
            binding.campoNumero.helperText = getString(R.string.plato_numero_cambiado, plato.numero)
        } else {
            binding.campoNumero.helperText = null
        }
    }

    // [Claude] los nombres de lo que se grapa a la nota. están aquí para que el Panel
    // y esta pantalla usen exactamente la misma palabra
    companion object {
        const val EXTRA_PRODUCTO_ID = "producto_id"
        const val EXTRA_CATEGORIA_ID = "categoria_id"

        const val CLAVE_3E_MOVER = "3e_mover"
        const val CLAVE_3E_RECUPERAR = "3e_recuperar"

        const val CLAVE_SALIR = "salir"
    }
}