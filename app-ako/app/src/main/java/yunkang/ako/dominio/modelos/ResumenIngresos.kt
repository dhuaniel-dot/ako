package yunkang.ako.dominio.modelos

import java.time.LocalDate

// El Resumen de ingresos de un día (2g): sus comandas cobradas, cuántas son y cuánto suman (R10).
// Lo monta ComandaRepository.resumenDelDia; no se guarda en la base de datos.
data class ResumenIngresos(
    val dia: LocalDate,
    val comandas: List<ComandaConTotal>,
    val numComandas: Int,
    val totalCentimos: Int
)
