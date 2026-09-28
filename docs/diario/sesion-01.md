# Sesión 01 — 2026-09-28

| Campo | Valor |
|---|---|
| **Fecha** | 2026-09-28 |
| **Sesión nº** | 01 |
| **Objetivo de la sesión** | Proyecto y repositorio: proyecto vacío en Android Studio dentro de `C:\AKO\app-ako`, primer arranque en el emulador, Git y repositorio público en GitHub, primer commit sin atribución |
| **Tiempo dedicado** | 19:48 – … (descontando pausas) |
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

- **Entendí:** por qué `minSdk 26`: el hash PBKDF2 con SHA-256 del PIN viene en Android desde la API 26 sin librerías extra, y llega al ~98,4 % de los móviles; es la versión mínima (funciona también en las más nuevas), no confundir con `targetSdk` 37. *(Daniel: añade el resto con tus palabras al cerrar)*
- **No entendí todavía:** lo que funciona pero no sabría explicar. Se vuelve a ello en la sesión siguiente.

## Pruebas

- Pruebas de código que pasan al terminar: P-C-nn, …
- Pruebas manuales ejecutadas en esta sesión: P-M-nn (resultado y fecha ya apuntados en `spec+doc-pruebas.md`).

## Uso de IA en esta sesión

- Qué pidió Daniel a Claude Code, qué generó, qué revisó o cambió Daniel a mano. Una línea por pieza. (Alimenta la frase de P37; **lo escrito coincide con lo hecho**.)

## Siguiente sesión

- Con qué se empieza, en una línea.
