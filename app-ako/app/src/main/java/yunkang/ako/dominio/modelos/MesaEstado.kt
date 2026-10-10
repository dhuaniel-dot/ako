package yunkang.ako.dominio.modelos

import yunkang.ako.datos.entidades.Mesa

// una casilla de la rejilla de 60 mesas. la monta el repositorio
// juntando cada mesa con su MesaConTotal, si la tiene
// sin comanda: comandaId y totalCentimos vacíos (mesa libre)
data class MesaEstado(
    val mesa: Mesa,
    val comandaId: Long?,
    val totalCentimos: Int?
)