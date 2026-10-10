package yunkang.ako.datos.repositorios

import yunkang.ako.datos.entidades.LineaComanda
import yunkang.ako.dominio.modelos.MesaEstado
import yunkang.ako.dominio.Carrito
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import yunkang.ako.dominio.modelos.ResumenIngresos

// [Claude] el actor del puesto de comandas en las pruebas
// no se usa en estas pruebas; existe porque CartaRepository lo pide al crearse
class ComandaRepositoryFalso : ComandaRepository {
    override suspend fun mesasConPlatoPendiente(productoId: Long): List<Int> = TODO()
    override suspend fun mesasConCategoriaPendiente(categoriaId: Long): List<Int> = TODO()
    override fun mesasConEstado(): Flow<List<MesaEstado>> = TODO()
    override suspend fun lineasDe(comandaId: Long): List<LineaComanda> = TODO()
    override suspend fun totalDe(comandaId: Long): Int = TODO()
    override suspend fun enviarCarrito(carrito: Carrito): Long = TODO()
    override suspend fun quitarLinea(lineaId: Long): Boolean = TODO()
    override suspend fun anular(comandaId: Long): Unit = TODO()
    override suspend fun cobrar(comandaId: Long): Unit = TODO()
    override suspend fun resumenDelDia(dia: LocalDate): ResumenIngresos = TODO()
}