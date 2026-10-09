package yunkang.ako.datos.repositorios

import yunkang.ako.datos.dao.CategoriaDao
import yunkang.ako.datos.entidades.Categoria
import yunkang.ako.dominio.modelos.ResultadoGuardado
import yunkang.ako.datos.dao.PrecargadosDao
import yunkang.ako.datos.dao.ProductoDao
import yunkang.ako.datos.entidades.Alergeno
import yunkang.ako.datos.entidades.Producto
import yunkang.ako.dominio.Validacion
import yunkang.ako.dominio.modelos.CategoriaConPlatos
import yunkang.ako.dominio.modelos.PlatoConMesas
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

// La carta: categorías, platos y alérgenos. Garantiza R5, R6, R8, R9, R15 y R16.
// Recibe sus herramientas por constructor.
// De las comandas solo puede preguntar lo que ofrece el puesto ComandaRepository.
class CartaRepository(
    private val categoriaDao: CategoriaDao,
    private val productoDao: ProductoDao,
    private val precargadosDao: PrecargadosDao,
    private val comandaRepository: ComandaRepository
) {

    // R16: todas las categorías por su orden; la de por defecto, siempre la última.
    suspend fun categorias(): List<Categoria> =
        porDefectoAlFinal(categoriaDao.todas())

    // Panel: las cajas con sus platos, al día solas.
    // combine junta los dos grifos: si cambia cualquiera de las dos tablas, se vuelve a montar la lista
    fun categoriasConPlatos(): Flow<List<CategoriaConPlatos>> =
        combine(categoriaDao.todasObservadas(), productoDao.todosObservados()) { categorias, platos ->
            porDefectoAlFinal(categorias).map { c ->
                // R15 no se aplica aquí: el Panel ve todos los platos, también los eliminados
                CategoriaConPlatos(c, platos.filter { it.categoriaId == c.id })
            }
        }

    // R16: nunca una segunda por defecto ni la por defecto eliminada; nombre sin repetir.
    suspend fun guardarCategoria(c: Categoria): ResultadoGuardado {
        // [Claude] Sin espacios por los lados y nunca vacío (la hoja 2b ya lo impide; aquí se asegura)
        val nombre = c.nombre.trim()
        require(nombre.isNotEmpty()) { "El nombre de la categoría no puede estar vacío" }
        if (categoriaDao.existeNombre(nombre, c.id)) {
            return ResultadoGuardado.NombreRepetido
        }
        if (c.id == 0L) {
            // Nueva: va detrás de las demás y nunca es la por defecto.
            val ordenMaximo = categoriaDao.todas().maxOfOrNull { it.orden } ?: 0
            categoriaDao.insertar(c.copy(nombre = nombre, orden = ordenMaximo + 1, esPorDefecto = false))
        } else {
            // Editada: "por defecto" y "activo" se quedan como están guardados: eliminar y recuperar
            // solo se hace por eliminarCategoria / recuperarCategoria, que es donde la pantalla avisa (R6).
            val guardada = checkNotNull(categoriaDao.porId(c.id)) { "La categoría ${c.id} no existe" }
            categoriaDao.actualizar(
                c.copy(
                    nombre = nombre,
                    esPorDefecto = guardada.esPorDefecto,
                    activo = guardada.activo
                )
            )
        }
        return ResultadoGuardado.Ok
    }

    // R6: mesas con comanda pendiente que llevan algún plato de esta categoría (aviso 2e, antes de confirmar).
    private suspend fun mesasAfectadasPorCategoria(id: Long): List<Int> =
        comandaRepository.mesasConCategoriaPendiente(id)

    // R6: ANTES de eliminar una categoría, qué platos suyos están en comandas pendientes
    // y en qué mesas. Solo lee: no toca ninguna línea. Solo cuenta los platos activos:
    // un plato ya eliminado no se vuelve a eliminar y no se avisa
    suspend fun platosAfectadosPorCategoria(categoriaId: Long): List<PlatoConMesas> {
        // [Claude] Si ninguna mesa tiene platos de esta categoría, no hace falta mirar plato a plato
        if (mesasAfectadasPorCategoria(categoriaId).isEmpty()) return emptyList()

        val afectados = mutableListOf<PlatoConMesas>()
        for (plato in platosDe(categoriaId)) {
            if (!plato.activo) continue
            // A las comandas se pregunta por el puesto (ComandaRepository), nunca por su DAO
            val mesas = comandaRepository.mesasConPlatoPendiente(plato.id)
            if (mesas.isNotEmpty()) afectados.add(PlatoConMesas(plato.nombre, mesas))
        }
        return afectados
    }

    // R5: eliminar y recuperar son lo mismo con activo false o true; la fila no se borra nunca
    // y ninguna línea de comanda se toca (R6). R16: la categoría por defecto no se elimina.
    // Un id inexistente no hace nada, como en los platos
    private suspend fun cambiarActivoCategoria(id: Long, activo: Boolean) {
        val categoria = categoriaDao.porId(id) ?: return
        if (!activo && categoria.esPorDefecto) return
        categoriaDao.actualizar(categoria.copy(activo = activo))
    }

    suspend fun eliminarCategoria(id: Long) = cambiarActivoCategoria(id, activo = false)

    suspend fun recuperarCategoria(id: Long) = cambiarActivoCategoria(id, activo = true)

    // Panel (2a): todos los platos de una categoría, también los eliminados (para poder recuperarlos).
    suspend fun platosDe(categoriaId: Long): List<Producto> =
        productoDao.porCategoria(categoriaId)

    // 5a (R15): la carta del cliente, al día sola: cada categoría con sus platos visibles.
    // La regla de «visible» vive en ProductoDao.visibles(). Se recorren las categorías (la de por defecto
    // la última, R16) y se filtran sus platos: visibles() deja Otros la primera (orden 0).
    // Una categoría sin ningún plato visible no sale (ficha 5)
    fun cartaVisible(): Flow<List<CategoriaConPlatos>> =
        combine(categoriaDao.todasObservadas(), productoDao.visibles()) { categorias, platos ->
            porDefectoAlFinal(categorias)
                .map { c -> CategoriaConPlatos(c, platos.filter { it.categoriaId == c.id }) }
                .filter { it.platos.isNotEmpty() }
        }

    // R15: puerta de Pedir: sin ningún plato visible no se entra.
    suspend fun hayPlatoVisible(): Boolean =
        productoDao.hayAlgunoVisible()

    // Puerta de Pedir (RF-25): ¿hay algún plato existente? Si no hay ninguno, «La carta está vacía».
    suspend fun hayPlatoExistente(): Boolean =
        productoDao.hayAlguno()

    // Ficha del plato (3): un plato por su id, para editarlo; vacío si no existe.
    suspend fun plato(id: Long): Producto? =
        productoDao.porId(id)

    // Ficha del plato (3) y carta (5b): los 14 alérgenos, para las casillas y la leyenda.
    suspend fun alergenos(): List<Alergeno> =
        precargadosDao.alergenos()

    // Ficha del plato (3) y carta (5b): los alérgenos marcados de un plato.
    suspend fun alergenosDe(productoId: Long): List<Alergeno> =
        productoDao.alergenosDe(productoId)

    // R8, R9 y cadena 3e: comprueba en este orden y solo entonces guarda el plato y sus alérgenos.
    suspend fun guardarPlato(
        p: Producto,
        alergenos: List<Long>,
        aunqueCategoriaEliminada: Boolean = false
    ): ResultadoGuardado {
        // R8: precio negativo = fallo de programación: excepción y no se guarda nada.
        Validacion.precioValido(p.precioCentimos)
        // R9: avisar antes; la base de datos lo impide igualmente (UNIQUE).
        if (productoDao.existeNumero(p.numero, p.id)) {
            return ResultadoGuardado.NumeroRepetido
        }
        // Cadena 3e: solo al crear o mover un plato a una categoría eliminada,
        // y si el Propietario no ha dicho ya que no a las dos preguntas.
        val categoria = checkNotNull(categoriaDao.porId(p.categoriaId)) { "La categoría ${p.categoriaId} no existe" }
        // El plato tal como está guardado (vacío si es nuevo); editar un id que no existe es un fallo de programación
        val antes = if (p.id == 0L) null else checkNotNull(productoDao.porId(p.id)) { "El plato ${p.id} no existe" }
        if (!categoria.activo && !aunqueCategoriaEliminada) {
            val esNuevoOMovido = antes == null || antes.categoriaId != p.categoriaId
            if (esNuevoOMovido) {
                return ResultadoGuardado.CategoriaEliminada
            }
        }
        // Guardar el plato y sus alérgenos juntos, en una transacción.
        // Al editar, "activo" se queda como estaba: eliminar y recuperar van por eliminarPlato / recuperarPlato.
        // distinct(): un alérgeno marcado dos veces no puede romper la transacción.
        val aGuardar = if (antes == null) p else p.copy(activo = antes.activo)
        productoDao.guardarConAlergenos(aGuardar, alergenos.distinct())
        return ResultadoGuardado.Ok
    }

    // R6: mesas con comanda pendiente que llevan este plato (aviso antes de confirmar).
    suspend fun mesasAfectadasPorPlato(id: Long): List<Int> =
        comandaRepository.mesasConPlatoPendiente(id)

    // R5: eliminar y recuperar un plato son lo mismo con activo false o true; la fila no se borra
    // nunca y ninguna línea de comanda se toca (R6). Un id inexistente no hace nada
    private suspend fun cambiarActivoPlato(id: Long, activo: Boolean) {
        val plato = productoDao.porId(id) ?: return
        productoDao.actualizar(plato.copy(activo = activo))
    }

    suspend fun eliminarPlato(id: Long) = cambiarActivoPlato(id, activo = false)

    suspend fun recuperarPlato(id: Long) = cambiarActivoPlato(id, activo = true)

    // R16: la categoría por defecto siempre la última; se reconoce por esPorDefecto, nunca por el nombre
    private fun porDefectoAlFinal(categorias: List<Categoria>) = categorias.sortedBy { it.esPorDefecto }
}