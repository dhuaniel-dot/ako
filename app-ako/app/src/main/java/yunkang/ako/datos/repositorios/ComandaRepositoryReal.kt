package yunkang.ako.datos.repositorios

import yunkang.ako.datos.dao.ComandaDao
import yunkang.ako.datos.dao.MesaDao
import yunkang.ako.datos.entidades.Comanda
import yunkang.ako.datos.entidades.LineaComanda
import yunkang.ako.dominio.modelos.MesaEstado
import androidx.room.withTransaction
import yunkang.ako.datos.AppDatabase
import yunkang.ako.datos.entidades.EstadoComanda
import yunkang.ako.dominio.Carrito
import yunkang.ako.dominio.Validacion
import java.time.LocalDate
import java.time.ZoneId
import yunkang.ako.dominio.modelos.ComandaConTotal
import yunkang.ako.dominio.modelos.ResumenIngresos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

// La versión de verdad del puesto de comandas: pregunta a la base de datos.
// Recibe sus herramientas por constructor; la base de datos, para la transacción de Enviar.
class ComandaRepositoryReal(
    private val db: AppDatabase,
    private val mesaDao: MesaDao,
    private val comandaDao: ComandaDao
) : ComandaRepository {

    // R6: mesas que salen en el aviso al eliminar un plato (solo se miran, no se tocan).
    override suspend fun mesasConPlatoPendiente(productoId: Long): List<Int> =
        comandaDao.mesasConProductoPendiente(productoId)

    // R6: mesas que salen en el aviso al eliminar una categoría (solo se miran, no se tocan).
    override suspend fun mesasConCategoriaPendiente(categoriaId: Long): List<Int> =
        comandaDao.mesasConCategoriaPendiente(categoriaId)

    // R3: ninguna columna dice "ocupada"; ocupada = tiene comanda pendiente.
    // La base de datos da solo las ocupadas, y aquí se pegan a las 60 mesas.
    // combine junta los dos grifos; si cambia cualquiera de los dos, se vuelve a montar la rejilla
    override fun mesasConEstado(): Flow<List<MesaEstado>> =
        combine(mesaDao.todasObservadas(), comandaDao.pendientesConTotal()) { mesas, ocupadas ->
            mesas.map { mesa ->
                val ocupada = ocupadas.find { it.mesaId == mesa.id }
                MesaEstado(mesa, ocupada?.comandaId, ocupada?.totalCentimos)
            }
        }

    // Las líneas de una comanda, en el orden en que se pidieron (6b, 6c y 2g).
    override suspend fun lineasDe(comandaId: Long): List<LineaComanda> =
        comandaDao.lineasDe(comandaId)

    // R10: el total se suma en SQL cada vez; la comanda no tiene columna "total".
    override suspend fun totalDe(comandaId: Long): Int =
        comandaDao.totalDe(comandaId)

    // R1, R2, R4, R8, R14: todo el envío en una transacción: se guarda entero o nada.
    override suspend fun enviarCarrito(carrito: Carrito): Long = db.withTransaction {
        // R4: nunca una comanda vacía (la pantalla ya lo impide; aquí se asegura).
        require(!carrito.estaVacio()) { "No se envía un carrito vacío" }
        // R1, R2: si la mesa ya tiene comanda pendiente, se añade a ella; si no, se crea.
        val pendiente = comandaDao.pendienteDeMesa(carrito.mesaId)
        val comandaId = if (pendiente != null) {
            pendiente.id
        } else {
            comandaDao.insertar(
                Comanda(
                    mesaId = carrito.mesaId,
                    estado = EstadoComanda.PENDIENTE,
                    fechaCreacion = System.currentTimeMillis(),
                    fechaCierre = null
                )
            )
        }
        // R14: cada línea copia nombre y precio del plato (congelados); R8: nunca un precio negativo.
        // [Claude] toList(): se trabaja sobre una copia por si la pantalla toca el carrito mientras se envía
        val lineas = carrito.lineas.toList().map { linea ->
            Validacion.precioValido(linea.producto.precioCentimos)
            // R4: cada línea, de 1 a 99 unidades.
            require(Validacion.cantidadValida(linea.cantidad)) { "Cantidad fuera de 1-99: ${linea.cantidad}" }
            LineaComanda(
                comandaId = comandaId,
                productoId = linea.producto.id,
                cantidad = linea.cantidad,
                precioUnitarioCentimos = linea.producto.precioCentimos,
                nombreProducto = linea.producto.nombre
            )
        }
        // R2: cada envío crea líneas nuevas (no suma a las que ya había).
        comandaDao.insertarLineas(lineas)
        comandaId
    }

    // R5, R7: quita una línea de una comanda abierta; si era la última, la comanda queda ANULADA (true).
    override suspend fun quitarLinea(lineaId: Long): Boolean = db.withTransaction {
        // Si la línea ya no existe (doble toque en Quitar), no se hace nada y no se anula
        val linea = comandaDao.lineaPorId(lineaId) ?: return@withTransaction false
        val comanda = abierta(linea.comandaId)
        comandaDao.borrarLinea(lineaId)
        if (comandaDao.contarLineas(comanda.id) == 0) {
            comandaDao.actualizar(
                comanda.copy(estado = EstadoComanda.ANULADA, fechaCierre = System.currentTimeMillis())
            )
            true
        } else {
            false
        }
    }

    // R5: anula una comanda abierta (ANULADA, con hora de cierre); la mesa queda libre (R3).
    override suspend fun anular(comandaId: Long) {
        val comanda = abierta(comandaId)
        comandaDao.actualizar(
            comanda.copy(estado = EstadoComanda.ANULADA, fechaCierre = System.currentTimeMillis())
        )
    }

    // R5: cobra una comanda abierta (PAGADA, con hora de cierre); la mesa queda libre (R3).
    override suspend fun cobrar(comandaId: Long) {
        val comanda = abierta(comandaId)
        comandaDao.actualizar(
            comanda.copy(estado = EstadoComanda.PAGADA, fechaCierre = System.currentTimeMillis())
        )
    }

    // R10: las comandas cobradas en un día, cada una con su total calculado.
    override suspend fun resumenDelDia(dia: LocalDate): ResumenIngresos {
        // El día va de las 0:00:00,000 a las 23:59:59,999 en la hora del móvil.
        val zona = ZoneId.systemDefault()
        val inicio = dia.atStartOfDay(zona).toInstant().toEpochMilli()
        val fin = dia.plusDays(1).atStartOfDay(zona).toInstant().toEpochMilli() - 1
        val mesas = mesaDao.todas()
        val filas = comandaDao.pagadasEntre(inicio, fin).map { comanda ->
            // La fila lleva el número de la mesa (el que ve el camarero), no su id.
            val mesa = mesas.first { it.id == comanda.mesaId }
            ComandaConTotal(
                comandaId = comanda.id,
                mesaNumero = mesa.numero,
                fechaCierre = comanda.fechaCierre ?: error("Una comanda PAGADA siempre tiene hora de cierre"),
                totalCentimos = comandaDao.totalDe(comanda.id)
            )
        }
        return ResumenIngresos(
            dia = dia,
            comandas = filas,
            totalCentimos = filas.sumOf { it.totalCentimos }
        )
    }

    // [Claude] R5: saca una comanda y exige que siga abierta; una comanda cerrada es intocable.
    private suspend fun abierta(comandaId: Long): Comanda {
        val comanda = comandaDao.porId(comandaId)
        check(comanda != null && comanda.estado == EstadoComanda.PENDIENTE) {
            "La comanda $comandaId no está abierta"
        }
        return comanda
    }
}