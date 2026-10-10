package yunkang.ako.datos.entidades

// los tres estados posibles de una comanda. Room lo guarda como texto
// pendiente: abierta, la mesa está comiendo (sale en rojo)
// pagada: cobrada, cuenta en el Resumen de ingresos
// anulada: cancelada, no se cobra
enum class EstadoComanda {
    PENDIENTE,
    PAGADA,
    ANULADA
}