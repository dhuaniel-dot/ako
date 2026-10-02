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

// La carta: categorías, platos y alérgenos. Garantiza R5, R6, R8, R9, R15 y R16 (spec 6).
// Recibe sus herramientas por constructor (P17).
// De las comandas solo puede preguntar lo que ofrece el puesto ComandaRepository (P127, P132).
class CartaRepository(
    private val categoriaDao: CategoriaDao,
    private val productoDao: ProductoDao,
    private val precargadosDao: PrecargadosDao,
    private val comandaRepository: ComandaRepository
) {

    // R16 (P48): todas las categorías por su orden; la de por defecto, siempre la última.
    suspend fun categorias(): List<Categoria> =
        categoriaDao.todas().sortedBy { it.esPorDefecto }

    // Panel (P47 A, P49 B): las cajas con sus platos, al día solas.
    // combine junta los dos grifos: si cambia cualquiera de las dos tablas, se vuelve a montar la lista
    fun categoriasConPlatos(): Flow<List<CategoriaConPlatos>> =
        combine(categoriaDao.todasObservadas(), productoDao.todosObservados()) { categorias, platos ->
            // R16: la de por defecto la última (se reconoce por esPorDefecto, nunca por el nombre), como en categorias()
            categorias.sortedBy { it.esPorDefecto }.map { c ->
                // R15 no se aplica aquí: el Panel ve todos los platos, también los eliminados
                CategoriaConPlatos(c, platos.filter { it.categoriaId == c.id })
            }
        }

    // R16: nunca una segunda por defecto ni la por defecto eliminada; nombre sin repetir (P124).
    suspend fun guardarCategoria(c: Categoria): ResultadoGuardado {
        // [Claude] H12: sin espacios por los lados y nunca vacío (la hoja 2b ya lo impide; aquí se asegura)
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
            // Editada: "por defecto" y "activo" se quedan como están guardados (P150): eliminar y recuperar
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
    suspend fun mesasAfectadasPorCategoria(id: Long): List<Int> =
        comandaRepository.mesasConCategoriaPendiente(id)

    // R6 (P128, hueco 8): ANTES de eliminar una categoría, qué platos suyos están en comandas pendientes
    // y en qué mesas. Solo lee: no toca ninguna línea. Solo cuenta los platos activos (P55, P147)
    suspend fun platosAfectadosPorCategoria(categoriaId: Long): List<PlatoConMesas> {
        // [Claude] Si ninguna mesa tiene platos de esta categoría, no hace falta mirar plato a plato
        if (mesasAfectadasPorCategoria(categoriaId).isEmpty()) return emptyList()

        val afectados = mutableListOf<PlatoConMesas>()
        for (plato in platosDe(categoriaId)) {
            if (!plato.activo) continue   // un plato ya eliminado no se vuelve a eliminar: no se avisa
            // P127: a las comandas se pregunta por el puesto (ComandaRepository), nunca por su DAO
            val mesas = comandaRepository.mesasConPlatoPendiente(plato.id)
            if (mesas.isNotEmpty()) afectados.add(PlatoConMesas(plato.nombre, mesas))
        }
        return afectados
    }

    // R5, R6, R16: eliminar = activo false, sin borrar ni tocar platos ni líneas; la por defecto no se elimina.
    suspend fun eliminarCategoria(id: Long) {
        val categoria = categoriaDao.porId(id) ?: return   // P152: id inexistente = nada, como en los platos
        if (categoria.esPorDefecto) return
        categoriaDao.actualizar(categoria.copy(activo = false))
    }

    // R5: recuperar = vuelve a la carta (activo true), con sus platos tal como estaban.
    suspend fun recuperarCategoria(id: Long) {
        val categoria = categoriaDao.porId(id) ?: return   // P152
        categoriaDao.actualizar(categoria.copy(activo = true))
    }

    // Panel (2a): todos los platos de una categoría, también los eliminados (para poder recuperarlos).
    suspend fun platosDe(categoriaId: Long): List<Producto> =
        productoDao.porCategoria(categoriaId)

    // R15: la carta (5) solo ve platos visibles; la regla vive en ProductoDao.visibles().
    suspend fun platosVisibles(): List<Producto> =
        productoDao.visibles()

    // R15: puerta de Pedir (spec 6): sin ningún plato visible no se entra.
    suspend fun hayPlatoVisible(): Boolean =
        productoDao.hayAlgunoVisible()

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
        // 1. R8: precio negativo = fallo de programación: excepción y no se guarda nada (P11).
        Validacion.precioValido(p.precioCentimos)
        // 2. R9: avisar antes; la base de datos lo impide igualmente (UNIQUE).
        if (productoDao.existeNumero(p.numero, p.id)) {
            return ResultadoGuardado.NumeroRepetido
        }
        // 3. Cadena 3e (P62, P130): solo al crear o mover un plato a una categoría eliminada,
        //    y si el Propietario no ha dicho ya que no a las dos preguntas.
        val categoria = checkNotNull(categoriaDao.porId(p.categoriaId)) { "La categoría ${p.categoriaId} no existe" }
        // El plato tal como está guardado (vacío si es nuevo); editar un id que no existe es un fallo de programación (H13)
        val antes = if (p.id == 0L) null else checkNotNull(productoDao.porId(p.id)) { "El plato ${p.id} no existe" }
        if (!categoria.activo && !aunqueCategoriaEliminada) {
            val esNuevoOMovido = antes == null || antes.categoriaId != p.categoriaId
            if (esNuevoOMovido) {
                return ResultadoGuardado.CategoriaEliminada
            }
        }
        // 4. Guardar el plato y sus alérgenos juntos, en una transacción (P125, P137).
        //    P150: al editar, "activo" se queda como estaba: eliminar y recuperar van por eliminarPlato / recuperarPlato.
        //    distinct(): un alérgeno marcado dos veces no puede romper la transacción (H13).
        val aGuardar = if (antes == null) p else p.copy(activo = antes.activo)
        productoDao.guardarConAlergenos(aGuardar, alergenos.distinct())
        return ResultadoGuardado.Ok
    }

    // R6: mesas con comanda pendiente que llevan este plato (aviso antes de confirmar).
    suspend fun mesasAfectadasPorPlato(id: Long): List<Int> =
        comandaRepository.mesasConPlatoPendiente(id)

    // R5, R6: eliminar = activo false; no se borra la fila ni se toca ninguna línea de comanda.
    suspend fun eliminarPlato(id: Long) {
        val plato = productoDao.porId(id) ?: return
        productoDao.actualizar(plato.copy(activo = false))
    }

    // R5: recuperar = vuelve a la carta (activo true).
    suspend fun recuperarPlato(id: Long) {
        val plato = productoDao.porId(id) ?: return
        productoDao.actualizar(plato.copy(activo = true))
    }
}