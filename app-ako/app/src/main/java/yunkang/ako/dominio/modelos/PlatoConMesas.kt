package yunkang.ako.dominio.modelos

// [Claude] un plato que está en comandas pendientes y los números de esas mesas (para el aviso 2e)
// los números son mesa.numero (lo que ve el camarero), no el id
data class PlatoConMesas(val nombre: String, val mesas: List<Int>)
