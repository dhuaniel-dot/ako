package yunkang.ako.dominio.modelos

// Una fila del Resumen de ingresos: mesa, hora de cobro e importe.
// La monta el repositorio con las comandas PAGADAS de un día.
data class ComandaConTotal(
    val comandaId: Long,
    val mesaNumero: Int,
    val fechaCierre: Long,
    val totalCentimos: Int
)