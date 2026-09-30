package yunkang.ako.dominio.modelos

// Una fila del Resumen de ingresos (S10): mesa, hora de cobro e importe.
// La monta el repositorio (S4) con las comandas PAGADAS de un día (P126).
data class ComandaConTotal(
    val comandaId: Long,
    val mesaNumero: Int,
    val fechaCierre: Long,
    val totalCentimos: Int
)