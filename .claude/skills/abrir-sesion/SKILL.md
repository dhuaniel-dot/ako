---
name: abrir-sesion
description: 'Abre una sesión de código de Ako: lee el contexto (como-trabajamos, decisiones, última ficha, EN-CURSO), crea o retoma la ficha del diario y propone el primer paso.'
when_to_use: 'Al empezar cada sesión, cuando Daniel diga «empecemos», «vamos con la S3», «abrimos sesión» o escriba /abrir-sesion.'
---

# Abrir sesión

Ritual de apertura del spec (`docs/spec-claude-code.md`, apartado 11) y de la plantilla del diario (`docs/plantilla-diario.md`). Dura cinco minutos y no se salta.

**Requisito:** el chat tiene que estar abierto con **`C:\AKO` como carpeta del proyecto** (no como carpeta adicional): si no, los hooks de `.claude/settings.json` no se ejecutan y esta skill ni se ve. Si `git rev-parse --show-toplevel` no da `C:/AKO`, decírselo a Daniel antes de nada.

## Pasos

1. **Averiguar qué sesión es.** Mirar el último `docs/diario/sesion-NN.md`. La sesión anterior está **cerrada** si no existe `docs/diario/EN-CURSO.md` y su ficha tiene *Commit final* anotado (o dice por qué no lo hay, como la sesión 00); entonces la nueva es NN+1. Si existe `EN-CURSO.md`, la sesión anterior sigue abierta: se retoma esa misma ficha. Si hay duda, preguntar a Daniel con una pregunta numerada.
2. **Leer, en este orden:** `CLAUDE.md` · `.claude/skills/como-trabajamos/SKILL.md` (la forma de trabajar, con sus últimos cambios y la marcha vigente) · `docs/decisiones-code.md` (lo ya decidido: no se vuelve a preguntar) · la última ficha entera · `docs/estado-nivel.md` (solo las filas de la sesión) · `docs/diario/EN-CURSO.md` si existe · el apartado de la sesión en `docs/spec-claude-code.md` (tabla del apartado 11) · la guía de la sesión en `docs/guias/` si la hay · la ficha de la pantalla que toque en `docs/spec+doc-pantallas.md`, sus wireframes en `docs/wireframes/` y sus textos en `docs/textos-ui.md`.
3. **Comprobar el repositorio:** `git status` y rama `main`. Si hay cambios sin commit **y existe `EN-CURSO.md`**, es un relevo de emergencia: se sigue desde ahí. Si hay cambios sin commit y **no** existe, preguntar a Daniel qué son; **nunca** `git checkout`, `stash` ni `reset` sin que él lo diga (y los destructivos están prohibidos en `settings.json`).
4. **Ficha del diario.** Si es sesión nueva: copiar el bloque del apartado 2 de `docs/plantilla-diario.md` a `docs/diario/sesion-NN.md` y rellenar solo **Fecha**, **Sesión nº**, **Objetivo de la sesión** (la línea de la tabla del apartado 11 del spec) y la hora de inicio en *Tiempo dedicado*. El resto se deja con la plantilla. Si se retoma una sesión abierta: añadir bajo la tabla una línea `Día 2 (AAAA-MM-DD): retomada a las HH:MM` y no borrar nada.
4b. **Diez minutos de «explícamelo tú»** (P177, 7 oct): Daniel elige **dos** conceptos de `docs/diario/pendientes-de-entender.md` y los explica con sus palabras; Claude corrige en dos frases, apunta la respuesta en *Qué entendí* de la ficha y pone la fecha en la lista si quedó claro (si no, sigue pendiente). Sin repaso largo: el PDF explica, el chat comprueba.
5. **Decir a Daniel, en cinco líneas como máximo:** el objetivo de la sesión, qué quedó a medias (si algo), la primera pieza que vamos a hacer y en qué archivo va, y confirmar que el modelo con el que se abrió el chat es el recomendado para esta sesión (si no lo es, decirlo: se cambia cerrando y abriendo otro chat, no a mitad). En S4, S6, S8 y S9 proponer además Plan Mode antes de la primera pieza. Terminar con la línea **«Ahora tú: …»** (ver `como-trabajamos`).

## Durante la sesión

La forma de trabajar (piezas, formato código-arriba, quién teclea, «Ahora tú», marcha vigente) está en `como-trabajamos`, leída en el paso 2, y las reglas en `CLAUDE.md`; no se repiten aquí para que vivan en un solo sitio. Lo que sí es de esta skill: **al terminar cada pieza, mirar el uso de contexto del chat** y aplicar los umbrales de `relevo` (aviso al 60 %, relevo al 75 %, emergencia al 85 %; el % se anota en la ficha al cerrar, P156), y anotar cada error en la tabla *Problemas y soluciones* de la ficha en el momento.
