package yunkang.ako.dominio.modelos

// La respuesta de un repositorio al guardar: "hecho" o "no, por esto" (P11).
// El precio negativo no está aquí: es un fallo de programación y lanza una excepción (R8).
enum class ResultadoGuardado {
    Ok,                  // se guardó
    NumeroRepetido,      // otro plato ya tiene ese número (R9)
    NombreRepetido,      // otra categoría ya se llama así, sin mirar mayúsculas (P124)
    CategoriaEliminada   // la categoría elegida está eliminada: la pantalla abre la cadena 3e
}