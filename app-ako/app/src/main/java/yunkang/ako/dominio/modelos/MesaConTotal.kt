package yunkang.ako.dominio.modelos

// Una mesa ocupada con lo que lleva gastado. La rellena la base de datos
// (ComandaDao.pendientesConTotal): solo salen las mesas con comanda PENDIENTE.
data class MesaConTotal(
    val mesaId: Long,
    val comandaId: Long,
    val totalCentimos: Int
)