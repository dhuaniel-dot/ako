package yunkang.ako.datos.repositorios

import yunkang.ako.datos.dao.ComandaDao

// La versión de verdad del puesto de comandas: pregunta a la base de datos (P132, P133).
// Recibe su DAO por constructor (P17); en la tanda 2 se le suman mesaDao y la base de datos.
class ComandaRepositoryReal(private val comandaDao: ComandaDao) : ComandaRepository {

    // R6: mesas que salen en el aviso al eliminar un plato (solo se miran, no se tocan).
    override suspend fun mesasConPlatoPendiente(productoId: Long): List<Int> =
        comandaDao.mesasConProductoPendiente(productoId)

    // R6: mesas que salen en el aviso al eliminar una categoría (solo se miran, no se tocan).
    override suspend fun mesasConCategoriaPendiente(categoriaId: Long): List<Int> =
        comandaDao.mesasConCategoriaPendiente(categoriaId)
}