package yunkang.ako.datos.repositorios

import yunkang.ako.datos.dao.CategoriaDao
import yunkang.ako.datos.entidades.Categoria
import yunkang.ako.dominio.modelos.ResultadoGuardado
import yunkang.ako.datos.dao.PrecargadosDao
import yunkang.ako.datos.dao.ProductoDao
import yunkang.ako.datos.entidades.Alergeno
import yunkang.ako.datos.entidades.Producto
import yunkang.ako.dominio.Validacion

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

    // R16: nunca una segunda por defecto ni la por defecto eliminada; nombre sin repetir (P124).
    suspend fun guardarCategoria(c: Categoria): ResultadoGuardado {
        if (categoriaDao.existeNombre(c.nombre, c.id)) {
            return ResultadoGuardado.NombreRepetido
        }
        if (c.id == 0L) {
            // Nueva: va detrás de las demás y nunca es la por defecto.
            val ordenMaximo = categoriaDao.todas().maxOfOrNull { it.orden } ?: 0
            categoriaDao.insertar(c.copy(orden = ordenMaximo + 1, esPorDefecto = false))
        } else {
            // Editada: "por defecto" se queda como está guardado, y la por defecto no se apaga.
            val guardada = categoriaDao.porId(c.id)
            categoriaDao.actualizar(
                c.copy(
                    esPorDefecto = guardada.esPorDefecto,
                    activo = if (guardada.esPorDefecto) true else c.activo
                )
            )
        }
        return ResultadoGuardado.Ok
    }

    // R6: mesas con comanda pendiente que llevan algún plato de esta categoría (aviso 2e, antes de confirmar).
    suspend fun mesasAfectadasPorCategoria(id: Long): List<Int> =
        comandaRepository.mesasConCategoriaPendiente(id)

    // R5, R6, R16: eliminar = activo false, sin borrar ni tocar platos ni líneas; la por defecto no se elimina.
    suspend fun eliminarCategoria(id: Long) {
        val categoria = categoriaDao.porId(id)
        if (categoria.esPorDefecto) return
        categoriaDao.actualizar(categoria.copy(activo = false))
    }

    // R5: recuperar = vuelve a la carta (activo true), con sus platos tal como estaban.
    suspend fun recuperarCategoria(id: Long) {
        val categoria = categoriaDao.porId(id)
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
        val categoria = categoriaDao.porId(p.categoriaId)
        if (!categoria.activo && !aunqueCategoriaEliminada) {
            val antes = if (p.id == 0L) null else productoDao.porId(p.id)
            val esNuevoOMovido = antes == null || antes.categoriaId != p.categoriaId
            if (esNuevoOMovido) {
                return ResultadoGuardado.CategoriaEliminada
            }
        }
        // 4. Guardar el plato y sus alérgenos juntos, en una transacción (P125, P137).
        productoDao.guardarConAlergenos(p, alergenos)
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