package yunkang.ako.datos.repositorios

import yunkang.ako.datos.entidades.LineaComanda
import yunkang.ako.dominio.modelos.MesaEstado
import yunkang.ako.dominio.Carrito
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import yunkang.ako.dominio.modelos.ResumenIngresos

// el puesto de comandas: las preguntas que otros pueden hacer sobre las comandas
// lo cumplen ComandaRepositoryReal (la app) y ComandaRepositoryFalso (las pruebas)
interface ComandaRepository {

    // para el aviso al eliminar un plato: números de las mesas con comanda pendiente que llevan este plato
    suspend fun mesasConPlatoPendiente(productoId: Long): List<Int>

    // para el aviso al eliminar una categoría: números de las mesas con comanda pendiente
    // que llevan algún plato de esta categoría
    suspend fun mesasConCategoriaPendiente(categoriaId: Long): List<Int>

    // las 60 mesas, cada una libre u ocupada (con comanda pendiente) con su total (rejilla 1d y 6a); al día solas
    fun mesasConEstado(): Flow<List<MesaEstado>>

    // las líneas de una comanda, en el orden en que se pidieron (6b, 6c y 2g)
    suspend fun lineasDe(comandaId: Long): List<LineaComanda>

    // el total de una comanda, calculado (no se guarda)
    suspend fun totalDe(comandaId: Long): Int

    // guarda lo pedido en la comanda de la mesa (la crea si no hay; una sola abierta por mesa); devuelve su id
    // cada línea, de 1 a 99 unidades, sin precio negativo y con el nombre y el precio congelados al enviar
    suspend fun enviarCarrito(carrito: Carrito): Long

    // quita una línea de una comanda abierta; si era la última, la comanda queda anulada (true)
    suspend fun quitarLinea(lineaId: Long): Boolean

    // anula una comanda abierta; la mesa queda libre
    suspend fun anular(comandaId: Long)

    // cobra una comanda abierta (pagada); la mesa queda libre
    suspend fun cobrar(comandaId: Long)

    // las comandas cobradas en un día, cada una con su total calculado (Resumen de ingresos, 2g)
    suspend fun resumenDelDia(dia: LocalDate): ResumenIngresos
}