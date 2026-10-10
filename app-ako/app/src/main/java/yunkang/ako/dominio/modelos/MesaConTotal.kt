package yunkang.ako.dominio.modelos

// una mesa ocupada con lo que lleva gastado. la rellena la base de datos
// (ComandaDao.pendientesConTotal): solo salen las mesas con comanda pendiente
data class MesaConTotal(
    val mesaId: Long,
    val comandaId: Long,
    val totalCentimos: Int
)