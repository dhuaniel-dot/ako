package yunkang.ako.dominio.modelos

// la respuesta de un repositorio al guardar: "hecho" o "no, por esto"
// el precio negativo no está aquí: es un fallo de programación y lanza una excepción
// NombreRepetido: sin mirar mayúsculas
// CategoriaEliminada: la categoría elegida está eliminada; la pantalla abre la cadena 3e
enum class ResultadoGuardado {
    Ok,
    NumeroRepetido,
    NombreRepetido,
    CategoriaEliminada
}