package yunkang.ako.datos.repositorios

import yunkang.ako.datos.entidades.LineaComanda
import yunkang.ako.dominio.modelos.MesaEstado
import yunkang.ako.dominio.Carrito
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import yunkang.ako.dominio.modelos.ResumenIngresos

// El puesto de comandas: las preguntas que otros pueden hacer sobre las comandas.
// Lo cumplen ComandaRepositoryReal (la app) y ComandaRepositoryFalso (las pruebas).
interface ComandaRepository {

    // Para el aviso al eliminar un plato: números de las mesas con comanda pendiente que llevan este plato.
    suspend fun mesasConPlatoPendiente(productoId: Long): List<Int>

    // Para el aviso al eliminar una categoría: números de las mesas con comanda pendiente
    // que llevan algún plato de esta categoría.
    suspend fun mesasConCategoriaPendiente(categoriaId: Long): List<Int>

    // Las 60 mesas, cada una libre u ocupada (con comanda pendiente) con su total (rejilla 1d y 6a); al día solas.
    fun mesasConEstado(): Flow<List<MesaEstado>>

    // Las líneas de una comanda, en el orden en que se pidieron (6b, 6c y 2g).
    suspend fun lineasDe(comandaId: Long): List<LineaComanda>

    // El total de una comanda, calculado (no se guarda).
    suspend fun totalDe(comandaId: Long): Int

    // Guarda lo pedido en la comanda de la mesa (la crea si no hay; una sola abierta por mesa); devuelve su id.
    // Cada línea, de 1 a 99 unidades, sin precio negativo y con el nombre y el precio congelados al enviar.
    suspend fun enviarCarrito(carrito: Carrito): Long

    // Quita una línea de una comanda abierta; si era la última, la comanda queda ANULADA (true).
    suspend fun quitarLinea(lineaId: Long): Boolean

    // Anula una comanda abierta; la mesa queda libre.
    suspend fun anular(comandaId: Long)

    // Cobra una comanda abierta (PAGADA); la mesa queda libre.
    suspend fun cobrar(comandaId: Long)

    // Las comandas cobradas en un día, cada una con su total calculado (Resumen de ingresos, 2g).
    suspend fun resumenDelDia(dia: LocalDate): ResumenIngresos
}