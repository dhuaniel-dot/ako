package yunkang.ako.datos.repositorios

// El puesto de comandas: las preguntas que otros (la carta) pueden hacer sobre las comandas (P132).
// Lo cumplen ComandaRepositoryReal (la app) y ComandaRepositoryFalso (las pruebas).
// Crece pieza a pieza: hoy solo las dos preguntas de R6.
interface ComandaRepository {

    // R6: números de las mesas con comanda pendiente que llevan este plato.
    suspend fun mesasConPlatoPendiente(productoId: Long): List<Int>

    // R6: números de las mesas con comanda pendiente que llevan algún plato de esta categoría.
    suspend fun mesasConCategoriaPendiente(categoriaId: Long): List<Int>
}