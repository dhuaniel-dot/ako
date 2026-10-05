# Sesión 07 — 2026-10-05

| Campo | Valor |
|---|---|
| **Fecha** | 2026-10-05 |
| **Sesión nº** | 07 |
| **Objetivo de la sesión** | Del spec: **Plato sin foto (3a, 3d, 3e)**; P-M-07 pasa y P-M-09, 10 y 11 quedan *Parcial* (guía S7, P6) |
| **Tiempo dedicado** | 11:35 – HH:MM (descontando pausas) |
| **Nivel / pieza** | Nivel 1 · <pantalla o capa> (p. ej. *entidades Room*, *pantalla 6c*) |
| **Commit final** | `abc1234` — mensaje del commit |
| **Contexto al cerrar** | NN % (el anillo junto al modelo; P156) |

## Qué se hizo

- Al abrir (11:35): comprobado en el código que `ConfirmacionDialog` manda `RESPUESTA_AFIRMATIVA = false` con el botón negativo (P144 A); la cadena 3e no lo toca.
- Preparación (apartado 0 de la guía), **hecha por Claude a petición de Daniel** («haz tú lo de la preparación»): app desinstalada (**se borran los datos del emulador**), `installDebug`, PIN 1234, Carnes y Postres creadas desde el `+` del Panel y Bebidas eliminada desde su lápiz (sin aviso: no tiene platos en mesas). En la base de datos: Otros (por defecto, `orden` 0), Bebidas (`activo` 0), Carnes (2), Postres (3) y Agua.
- Pieza 1: cadenas de la pantalla 3 en `strings.xml` (`comun_no`, `plato_*`, `carta_btn_salir`, `mesas_afectadas_titulo`, `<plurals>` `mesas_afectadas_cuerpo` (D15) y `recuperar_categoria_cuerpo` (P63 A), `categoria_eliminada_*`, `recuperar_categoria_*`). [Claude] Singular del segundo aviso 3e: «Volverá a la carta 1 plato.». Queda para la pieza 12: con 0 platos saldría «sus 0 platos» (el español no tiene `zero` en `<plurals>`). Compila (`assembleDebug`). Daniel, a «¿por qué no se escribe "Otros" en el aviso 3e-1?»: «porque dije que no se puede renombrar» (al revés: **sí** se puede renombrar, R16; por eso el nombre se lee de la base de datos buscando `esPorDefecto`).
- Pieza 2: borrador de `Formato.centimosDesde` probado por Claude con una prueba provisional de 20 casos (y el camino de ida y vuelta con `precio`) y deshecho. Tecleado por Daniel; no compilaba (problema 1) y Claude lo arregló. Compila. Daniel, a «¿qué da "7,5" y por qué no 705?»: «porque 05 son 5 céntimos y 50 son 50 céntimos» (bien: «5» se rellena a «50» y da 750).

## Problemas y soluciones

| # | Qué falló | Cómo se resolvió | Justificación (por qué esta solución y no otra) | Fuente |
|---|---|---|---|---|
| 1 | Pieza 2: `compileDebugKotlin` falla («Compilation error») | La palabra «porque» de la respuesta a la pregunta de comprensión se escribió dentro de `Formato.kt` (Android Studio tenía el foco), en la línea `if (partes.size > 2) return null`; Claude la quitó | Es la misma lección del 1 oct (respuestas escritas en un XML): por eso se mira `git diff` antes de compilar tras cada «ya está». Se quita la palabra y nada más; el resto del bloque coincidía | Propio |

## Qué entendí y qué no

- **Entendí:** lo que ahora podría explicar sin leerlo (una línea por concepto).
- **No entendí todavía:** lo que funciona pero no sabría explicar. Se vuelve a ello en la sesión siguiente.

## Para el vídeo

- Lo que se ha explicado en la sesión y se podría contar en el vídeo de 10 minutos: una línea por idea, con la comparación de la vida real si la hubo y la decisión (Pnnn) que la respalda. Sirve para montar el guion al final.

## Pruebas

- Pruebas de código que pasan al terminar: P-C-nn, …
- Pruebas manuales ejecutadas en esta sesión: P-M-nn (resultado y fecha ya apuntados en `spec+doc-pruebas.md`).

## Uso de IA en esta sesión

- Qué pidió Daniel a Claude Code, qué generó, qué revisó o cambió Daniel a mano. Una línea por pieza. (Alimenta la frase de P37; **lo escrito coincide con lo hecho**.)

## Siguiente sesión

- Con qué se empieza, en una línea.
