package yunkang.ako.dominio.modelos

// una fila del Resumen de ingresos: mesa, hora de cobro e importe
// la monta el repositorio con las comandas pagadas de un día
data class ComandaConTotal(
    val comandaId: Long,
    val mesaNumero: Int,
    val fechaCierre: Long,
    val totalCentimos: Int
)