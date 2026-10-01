package yunkang.ako.datos.repositorios

// [Claude] El actor del puesto de comandas en las pruebas (P132, P133).
// No se usa en estas pruebas; existe porque CartaRepository lo pide al crearse.
class ComandaRepositoryFalso : ComandaRepository {
    override suspend fun mesasConPlatoPendiente(productoId: Long): List<Int> = TODO()
    override suspend fun mesasConCategoriaPendiente(categoriaId: Long): List<Int> = TODO()
}