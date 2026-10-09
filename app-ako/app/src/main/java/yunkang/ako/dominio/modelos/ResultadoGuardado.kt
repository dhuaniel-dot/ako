package yunkang.ako.dominio.modelos

// La respuesta de un repositorio al guardar: "hecho" o "no, por esto".
// El precio negativo no está aquí: es un fallo de programación y lanza una excepción.
// NumeroRepetido: otro plato ya tiene ese número.
// NombreRepetido: otra categoría ya se llama así, sin mirar mayúsculas.
// CategoriaEliminada: la categoría elegida está eliminada; la pantalla abre la cadena 3e.
enum class ResultadoGuardado {
    Ok,
    NumeroRepetido,
    NombreRepetido,
    CategoriaEliminada
}