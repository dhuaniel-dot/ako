package yunkang.ako.datos.repositorios

import yunkang.ako.datos.entidades.Comanda
import yunkang.ako.datos.entidades.LineaComanda
import yunkang.ako.dominio.modelos.MesaEstado
import yunkang.ako.dominio.Carrito
import java.time.LocalDate
import yunkang.ako.dominio.modelos.ResumenIngresos

// El puesto de comandas: las preguntas que otros pueden hacer sobre las comandas (P132).
// Lo cumplen ComandaRepositoryReal (la app) y ComandaRepositoryFalso (las pruebas).
// Crece pieza a pieza.
interface ComandaRepository {

    // R6: números de las mesas con comanda pendiente que llevan este plato.
    suspend fun mesasConPlatoPendiente(productoId: Long): List<Int>

    // R6: números de las mesas con comanda pendiente que llevan algún plato de esta categoría.
    suspend fun mesasConCategoriaPendiente(categoriaId: Long): List<Int>

    // R3: las 60 mesas, cada una libre u ocupada con su total (rejilla 1d y 6a).
    suspend fun mesasConEstado(): List<MesaEstado>

    // R1: la comanda pendiente de una mesa, o vacío si la mesa está libre.
    suspend fun comandaPendiente(mesaId: Long): Comanda?

    // Las líneas de una comanda, en el orden en que se pidieron (6b, 6c y 2g).
    suspend fun lineasDe(comandaId: Long): List<LineaComanda>

    // R10: el total de una comanda, calculado (no se guarda).
    suspend fun totalDe(comandaId: Long): Int

    // R1, R2, R4, R8, R14: guarda lo pedido en la comanda de la mesa (la crea si no hay); devuelve su id.
    suspend fun enviarCarrito(carrito: Carrito): Long

    // R5, R7: quita una línea de una comanda abierta; si era la última, la comanda queda ANULADA (true).
    suspend fun quitarLinea(lineaId: Long): Boolean

    // R5: anula una comanda abierta; la mesa queda libre.
    suspend fun anular(comandaId: Long)

    // R5: cobra una comanda abierta (PAGADA); la mesa queda libre.
    suspend fun cobrar(comandaId: Long)

    // R10: las comandas cobradas en un día, cada una con su total calculado (Resumen de ingresos, 2g).
    suspend fun resumenDelDia(dia: LocalDate): ResumenIngresos
}