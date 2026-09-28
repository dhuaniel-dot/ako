---
name: relevo
description: 'Cambio de chat a mitad de una sesión de Ako: termina la pieza en curso, guarda el estado exacto en EN-CURSO.md, commit de trabajo si compila, revisa la forma de trabajar y entrega el primer mensaje del chat nuevo.'
when_to_use: 'Cuando Daniel diga «cambiamos de chat», «esto se hace largo» o «lo dejamos por hoy» sin cerrar la sesión, escriba /relevo, o el contexto del chat pase del 60 % (aviso), 75 % (obligatorio) u 85 % (emergencia).'
---

# Relevo (cambiar de chat sin perder nada)

Un chat largo consume más y olvida. Se cambia de chat cuando se hace largo, cuando toca cambiar de modelo o al terminar el día con la sesión abierta. **Regla de Daniel: primero se termina la pieza que está en curso, y después se cambia.**

## Cuándo (umbrales de contexto)

Claude mira el uso de contexto del chat al terminar cada pieza (en la app de escritorio, la herramienta de uso de la sesión; si no está disponible, pide a Daniel que escriba `/context` o que mire el anillo de porcentaje junto al modelo y diga la cifra). Cuanto más lleno, peor recuerda lo del principio y más cuesta cada mensaje; la compactación automática salta al 97 %, y para entonces ya se ha perdido calidad.

| Contexto | Qué se hace |
|---|---|
| Menos del 60 % | Nada; se sigue |
| **60–75 %** | Claude avisa; se termina la pieza en curso y se hace el relevo normal (pasos de abajo) |
| **75–85 %** | Relevo obligatorio al terminar la pieza; no se empieza ninguna nueva |
| **Más del 85 %** | **Relevo de emergencia:** no se termina la pieza. Se escribe EN-CURSO.md **ya**, con el estado exacto aunque no compile (qué archivo, qué líneas están puestas, qué faltaba), y se entrega el mensaje del chat nuevo. Sin commit |

**Red de seguridad si no se puede medir el contexto:** relevo obligatorio tras **seis piezas** o al empezar la **tercera hora** de chat, lo que llegue antes. Si Daniel pide el cambio antes de esos umbrales (cansancio, fin del día), se hace el relevo normal.

## Pasos

1. **Terminar la pieza en curso.** Nunca se deja código a medio escribir: o la pieza compila, o se retira lo que no compila y se apunta qué faltaba. Comprobar con `verificar`.
2. **Escribir `docs/diario/EN-CURSO.md`** con la plantilla de `docs/plantilla-en-curso.md` (se sobrescribe si ya existía). Lo que no puede faltar: la pieza en curso y su estado, la salida de `git status --short`, el hash del último commit, **el bloque de código exacto que se le dio a Daniel para la pieza en curso** (es lo que `verificar` compara en el chat nuevo), las **decisiones tomadas en este chat que no están en ningún documento** (con [Claude] si las propuso Claude), lo que Daniel dijo que no entendió, los tres pasos siguientes y el modelo recomendado.
3. **Ficha del diario:** pasar a la tabla *Problemas y soluciones* cualquier error del día que aún no esté, con su justificación. En `docs/estado-nivel.md`, los RF de la pieza abierta pasan a `en curso` (es el único momento en que ese estado tiene sentido).
3b. **Revisar la forma de trabajar.** Repasar el chat buscando lo que Daniel haya dicho sobre cómo quiere trabajar («mucha chapa», «explica más», «demasiados pasos», «a partir de ahora…», «prefiero…») y lo que haya funcionado mal o bien. Si hay algo, actualizar `.claude/skills/como-trabajamos/SKILL.md` con una línea fechada en su historial y decírselo a Daniel en una frase. Si no hay nada, decir «forma de trabajar: sin cambios».
4. **Commit de trabajo** (solo si compila): `git add -A` · `git commit -m "S<N>: WIP <pieza>"` · `git push`. Si no compila, no se hace commit y EN-CURSO.md lo dice.
4b. **Resumen para leer en el móvil (Daniel, 28 sep).** Antes de entregar el mensaje del chat nuevo: **preguntar antes a Daniel qué le ha costado entender y qué curiosidades quiere saber** (lo incluye en los dos), y escribir dos resúmenes de **lo hecho y explicado en este chat** (longitud la que pida el contenido): uno **sencillo**, coloquial y con ejemplos de la vida real, y otro **normal**, con los términos técnicos (sirve para la memoria y el vídeo). Guardarlos como PDF en `docs/resumenes/AAAA-MM-DD-S<N>-sencillo.pdf` y `…-normal.pdf` (fuente en Markdown al lado) y **enviarlos a Daniel por Gmail** (dhuaniel@gmail.com) como adjuntos: enseñarle asunto y adjuntos y **esperar su «sí» antes de enviar** (enviar un correo siempre se confirma).
5. **Entregar a Daniel, siempre y sin que lo pida, el primer mensaje del chat nuevo, listo para copiar.** Se rellena con los datos reales (sesión, pieza, pantalla), no con los huecos:

   > Sesión S<N> de Ako, continuación: <objetivo de la sesión>. Estábamos en <pieza en curso, estado>. Lee `CLAUDE.md`, `.claude/skills/como-trabajamos/SKILL.md`, `docs/decisiones-code.md`, `docs/diario/sesion-NN.md`, `docs/diario/EN-CURSO.md` y `docs/estado-nivel.md`; después el apartado de la S<N> en `docs/spec-claude-code.md` y la ficha y los wireframes de <pantalla>. Abre la sesión y dime en tres líneas por dónde seguimos.

   Debajo, en una línea: **modelo y esfuerzo recomendados** para el chat nuevo. Y recordarle que **el modelo se elige al abrir el chat nuevo**, no a mitad.

## Qué pasa con EN-CURSO.md

Al abrir el chat siguiente sobre `C:\AKO`, el hook `al-empezar.sh` lo mete en el contexto y `abrir-sesion` lo lee; `cerrar-sesion` lo vuelca en la ficha y en `decisiones-code.md` y lo borra. Se sube al repositorio (es parte del rastro de trabajo), no al Project de Claude.
