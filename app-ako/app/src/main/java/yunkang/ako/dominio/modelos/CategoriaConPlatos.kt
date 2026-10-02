package yunkang.ako.dominio.modelos

import yunkang.ako.datos.entidades.Categoria
import yunkang.ako.datos.entidades.Producto

// [Claude] Lo que pinta una caja del Panel: una categoría y sus platos, ya en orden.
// Es una «caja de transporte» entre el repositorio y la pantalla (sin anotaciones de Room)
data class CategoriaConPlatos(val categoria: Categoria, val platos: List<Producto>)