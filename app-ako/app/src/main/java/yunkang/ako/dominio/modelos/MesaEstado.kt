package yunkang.ako.dominio.modelos

import yunkang.ako.datos.entidades.Mesa

// Una casilla de la rejilla de 60 mesas (S8 y S9). La monta el repositorio (S4)
// juntando cada mesa con su MesaConTotal, si la tiene (P123).
// Sin comanda: comandaId y totalCentimos vacíos (mesa libre, R3).
data class MesaEstado(
    val mesa: Mesa,
    val comandaId: Long?,
    val totalCentimos: Int?
)