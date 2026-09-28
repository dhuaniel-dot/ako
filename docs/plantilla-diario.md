> **Plantilla del diario de desarrollo: una ficha por sesión de código.** Copia de `proceso-plantilla-diario-desarrollo.md` (Project), apartados 1 y 2, preparada en la fase 5b (24 sep 2026) para `docs/plantilla-diario.md`. Alimenta el apartado 7 de la memoria (*"diario de desarrollo… problemas encontrados y soluciones dadas con su correspondiente justificación (Importante)"*), el Gantt real del apartado 6 y la declaración de uso de IA.
> **Claude Code:** al empezar cada sesión copia el bloque del apartado 2 a `docs/diario/sesion-NN.md` y rellena los campos del arranque; el resto, al cerrar. La ficha se escribe en español y con las palabras de Daniel donde diga *qué entendí*.

# Plantilla del diario de desarrollo

## 1. Cómo se usa

- **Una ficha por sesión de código**, en un archivo propio del repositorio: `docs/diario/sesion-NN.md` (NN = 01, 02…), como dice `spec-claude-code.md`, apartado 13. *(Cuando la ficha se sube al Project de Claude al cerrar la sesión, allí se llama `diario-6-sesion-NN.md`.)* Nunca se reescribe una ficha cerrada; si algo de una sesión anterior resulta estar mal, se dice en la sesión en que se descubre.
- **Se rellena en dos momentos, no al final:** al **empezar** la sesión, *fecha*, *sesión nº*, *objetivo* y la hora de inicio; al **terminar**, todo lo demás. Un problema que se resuelve a mitad de sesión se apunta en el momento, aunque sea en dos palabras: *qué falló* se olvida en cuanto deja de fallar.
- **Cada fila de problema lleva su justificación**, que es lo que el guion marca como *(Importante)*: no basta *"lo arreglé cambiando X"*; hay que decir **por qué X era el arreglo** y qué se descartó.
- **El tiempo se apunta en minutos reales**, incluidos los que se van en buscar, en leer un error o en equivocarse. Es el dato del Gantt real; sin él el apartado 6 se inventa.
- ***Qué entendí y qué no*** es la defensa de la integridad académica: el vídeo se graba sin preguntas, pero el corrector lee el diario. Lo que no se entiende se apunta y se vuelve a ello en la sesión siguiente antes de escribir más código encima.
- **Las fuentes** van con enlace y fecha de consulta, y **al mismo tiempo** a `ref+doc-bibliografia.md` con su nivel A/B/C (la mantiene Claude).
- **El commit** es el identificador corto (7 caracteres) del último `commit` de la sesión, que es lo que permite volver a ese estado del código. Si una sesión no termina en commit, se dice por qué.
- **La ficha se cierra en el chat de esa sesión** y se sube al Project; en el cierre de la fase 6 las fichas se funden en `diario+doc-decisiones.md` y son la base del apartado 7.

## 2. La ficha

```markdown
# Sesión NN — <fecha>

| Campo | Valor |
|---|---|
| **Fecha** | AAAA-MM-DD |
| **Sesión nº** | NN |
| **Objetivo de la sesión** | Qué tenía que quedar hecho al terminar (una línea; sale de `spec-claude-code.md`, orden de construcción) |
| **Tiempo dedicado** | HH:MM (inicio – fin, descontando pausas) |
| **Nivel / pieza** | Nivel 1 · <pantalla o capa> (p. ej. *entidades Room*, *pantalla 6c*) |
| **Commit final** | `abc1234` — mensaje del commit |

## Qué se hizo

- Lista de lo que existe al terminar y no existía al empezar (clases, pantallas, pruebas que pasan).
- Qué se dejó a medias y en qué estado exacto (para retomarlo sin adivinar).

## Problemas y soluciones

| # | Qué falló | Cómo se resolvió | Justificación (por qué esta solución y no otra) | Fuente |
|---|---|---|---|---|
| 1 | El síntoma tal cual: mensaje de error copiado, o qué hacía la app en vez de lo esperado | Lo que se cambió, en concreto | Por qué era el arreglo correcto; qué otra opción había y por qué se descartó | Enlace y fecha, o "propio" |
| 2 | | | | |

## Qué entendí y qué no

- **Entendí:** lo que ahora podría explicar sin leerlo (una línea por concepto).
- **No entendí todavía:** lo que funciona pero no sabría explicar. Se vuelve a ello en la sesión siguiente.

## Pruebas

- Pruebas de código que pasan al terminar: P-C-nn, …
- Pruebas manuales ejecutadas en esta sesión: P-M-nn (resultado y fecha ya apuntados en `spec+doc-pruebas.md`).

## Uso de IA en esta sesión

- Qué pidió Daniel a Claude Code, qué generó, qué revisó o cambió Daniel a mano. Una línea por pieza. (Alimenta la frase de P37; **lo escrito coincide con lo hecho**.)

## Siguiente sesión

- Con qué se empieza, en una línea.
```
