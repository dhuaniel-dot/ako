package yunkang.ako.datos.entidades

// Los tres estados posibles de una comanda. Room lo guarda como texto.
// PENDIENTE: abierta, la mesa está comiendo (sale en rojo).
// PAGADA: cobrada, cuenta en el Resumen de ingresos.
// ANULADA: cancelada, no se cobra.
enum class EstadoComanda {
    PENDIENTE,
    PAGADA,
    ANULADA
}