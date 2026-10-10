package yunkang.ako.dominio.modelos

import java.time.LocalDate

// el Resumen de ingresos de un día (2g): sus comandas cobradas y cuánto suman
// lo monta ComandaRepository.resumenDelDia; no se guarda en la base de datos
data class ResumenIngresos(
    val dia: LocalDate,
    val comandas: List<ComandaConTotal>,
    val totalCentimos: Int
)
