# Revisión de la forma de trabajar — 6 oct 2026 (tras la S8)

Revisor: Claude (Fable 5.1). Leído: `CLAUDE.md` (proyecto y global), `docs/lecciones-claude.md`, las seis skills, `.claude/settings.json`, `.claude/agents/revisor.md`, los dos hooks, `.gitignore` y `.gitattributes`, las fichas S00–S08 (apartados «Uso de IA», «Problemas» y «Qué entendí»), `decisiones-code.md` 5.3 (respuesta del profesor) y el historial de Git (autores, coautor, secretos). **No se ha editado nada** (los hooks no los puede tocar Claude; aquí no hace falta tocarlos).

**Recuento:** 0 Alta · **1 Media** · **4 Baja** · 8 comprobaciones sin hallazgo. La revisión del 1 oct (F01–F17) está aplicada entera (lo he comprobado archivo a archivo: `git add -A` tras `seguridad` 2, hook con F02 y F03 probado el 2 oct, `deny` completo, comando de commit con el coautor, repasos al PDF, P155 en `verificar`, P156 en `relevo`, historial fuera de `como-trabajamos`, lecciones podadas). Nada de aquello se ha roto.

---

## MEDIA

### F01 · Desde la S6 las pruebas manuales las ejecuta Claude con `adb`; a Daniel le queda cada vez menos «hecho con sus manos» que contar — **Media** · decisión (es la cara de trabajo de C01, en el informe de coherencia)
- **Dónde:** fichas S6 (pieza 21), S7 (pieza 14), S8 (piezas 23–24); `como-trabajamos` (2 oct: «puede pedir que Claude ejecute las pruebas manuales»; 5 oct: «las pruebas esas hazlas tú»); plan de pruebas, columna *Observaciones* (16 de las 20 P-M con resultado las ejecutó Claude).
- **Qué pasa:** la práctica es legítima (Daniel lo pidió, está escrito en cada prueba, Daniel revisa las capturas) y eficiente (la S8 hizo en 2 h 50 lo que la guía estimaba en 9 h). Pero el profesor dibujó la línea en «lo malo sería que diseñara y ejecutara el proyecto por sí sola… lo más importante que dependas de ella», y **probar la app es la parte que un alumno sí puede hacer sin saber programar**. Si en la memoria queda «Claude escribió el código (Daniel lo tecleó), Claude probó la app, Daniel miró capturas», el reparto se inclina.
- **Por qué importa:** es la frase de uso de IA de la memoria (P37) y el vídeo; «lo escrito tiene que coincidir con lo hecho» se cumple hoy, pero lo hecho debería incluir una parte de pruebas con el dedo de Daniel, sobre todo la **pasada final de la S13**, que es la prueba de que el nivel 1 funciona y la que verá el corrector.
- **Cómo lo he comprobado:** contando en el plan de pruebas quién ejecutó cada P-M (Daniel: 01, 02, 03 y 13 en la S5; Claude: las demás) y leyendo el «Uso de IA» de las fichas.
- **Opciones:**
  - **A)** **La pasada final de la S13 la pasa Daniel con el dedo** (las 29, en dos tandas, como ya prevé la guía S13 en las piezas 9–10: «Daniel apunta una línea por prueba; Claude lo pasa al plan»), y Claude solo prepara el emulador y anota. En S9–S12 se sigue como ahora (Claude con `adb`) para no perder velocidad, pero **cada sesión Daniel pasa con el dedo las 2–3 pruebas del guion del vídeo** de esa sesión (en la S9: P-M-23, 24 y 28) y lo dice la ficha. [Claude]
  - **B)** Seguir como ahora hasta el final y declararlo tal cual en el plan y en la memoria («pruebas automatizadas con `adb` por la IA a petición del autor; el autor revisó las capturas»), apoyándose en que la automatización de pruebas con `adb` es una práctica estándar de Android (https://developer.android.com/tools/adb). Pega: la memoria tendrá que explicar por qué el alumno no tocó la app en 25 de 29 pruebas.
  - **C)** Mixto al revés: Daniel pasa todas las P-M nuevas de cada sesión con el dedo (7 en la S9, 1 en la S10, 1 en la S12) y Claude repite las antiguas con `adb`. Pega: la S9 ya es la sesión más larga.
- **Recomendación:** **A.** Es la que menos tiempo quita (unos 20 min por sesión y una tarde en la S13) y la que mejor defiende el vídeo: Daniel habrá hecho con sus manos justo las escenas que va a grabar. Fuente externa: no aplica (es una decisión de reparto de trabajo); la fuente es la normativa del módulo y la respuesta del profesor del 29 sep (`decisiones-code.md` 5.3).

## BAJA

| # | Dónde | Qué pasa | Arreglo |
|---|---|---|---|
| F02 | `docs/lecciones-claude.md` | Dice «Corto a propósito» y tiene ya 25 lecciones (el 1 oct eran 15): seis hablan de lo mismo (pegar en el archivo o sitio equivocado: 30 sep, 1 oct, 2 oct, 5 oct ×2, 6 oct ×2) y la del 6 oct («Ctrl+A y después Ctrl+V», «desde entonces no volvió a pasar») ya las resume. Cada chat las carga enteras | Mecánico, al cerrar la S9: fundir las seis de pegado en dos (una para «archivo entero: Ctrl+Mayús+N → nombre → Ctrl+A → Ctrl+V», otra para «trozo suelto solo si no tiene vecino parecido ni va en el borde de un bloque») y quitar las que ya no pueden repetirse (hook con `-F -`, `local.properties` de la S1, AGP) |
| F03 | Fichas S6–S8, «Uso de IA»; `README.md` apartado «Uso de IA» | Cada vez hay más código que escribe Claude directamente y está **bien declarado**: iconos e `ic_desplegado` (S6), la pieza 20 (S6), siete arreglos «Claude dejó el archivo como el bloque» (S7–S8), la preparación de datos con `sqlite3`, el ayudante de Python de las pruebas. El README ya lo cubre («o lo revisa, si en algún tramo Claude Code lo escribe directamente: cada ficha dice cuál de las dos») | Nada que cambiar; que la memoria recoja la lista tal cual (es honradez, no un problema). Y que la **pasada de apropiación** (P230) empiece por esos trozos |
| F04 | `.claude/hooks/comprobar-commit.sh` | Funciona (lo probé leyendo: formato `S<N>:`, ficha, claves en lo cambiado y en los archivos nuevos, autor Daniel con correo anónimo, bloqueo si falta `python`). Dos límites que conviene tener escritos: solo vigila los commits que lanza Claude (no los de Daniel a mano, ya dicho en `seguridad` 2.4) y busca claves con **forma** de clave (`sk-ant-`, `ghp_`, `AIza`, `AKIA`, `PRIVATE KEY`): una contraseña normal escrita a mano no la vería | Nada que pegar hoy. Si Daniel quiere, en la S13 se puede añadir al patrón `password *=` y `storePassword` como hace la skill `seguridad` 2.2 (bloque para que lo pegue Daniel) |
| F05 | Historial de Git (`git log --author=Claude`) | Tres commits del 29 sep (`87d352e`, `74e521b`, `ebd1cd9`) tienen **autor «Claude»** y GitHub muestra a «claude» como colaborador del repositorio público. Está explicado en la ficha S1 (problema 2) y el hook lo impide desde entonces (comprobado: los 111 commits posteriores son de Daniel y, desde el 29 sep, todos llevan `Co-Authored-By: Claude`) | Nada en Git (reescribir el historial está prohibido). Una frase en la memoria y en el README («tres commits del 29 sep salieron con la identidad de la herramienta por un error de configuración, corregido ese mismo día con un hook») para que el corrector no lo descubra solo |

---

## Comprobado y bien

1. **Secretos:** ningún patrón de clave en los archivos ni en el historial; correos anonimizados (`…@users.noreply.github.com`); `local.properties`, `.idea/`, `*.jks`, `.env`, `settings.local.json` y `docs/resumenes/` fuera del repositorio (`git ls-files` limpio); el nombre de usuario de GitHub `dhuaniel-dot` aparece en documentos, pero es público por definición.
2. **Commits:** 111 en total; autor «Yunkang Daniel» con correo anónimo desde el 29 sep; coautor Claude en todos desde entonces (salvo `0f58b2b`, declarado en la ficha S3); mensajes `S<N>:` siempre; dos commits por cierre (código y ficha); sin `--amend`, `force` ni ramas. El árbol de trabajo está limpio y al día con `origin/main`.
3. **`settings.json`:** `deny` completo (los del 1 oct incluidos); hooks `SessionStart` y `PreToolUse` para `Bash|PowerShell`; `attribution.commit` puesto.
4. **`al-empezar.sh`:** funciona (al abrir este chat metió el «Siguiente sesión» de la ficha S8).
5. **Skills:** `abrir-sesion`, `cerrar-sesion`, `relevo`, `verificar`, `seguridad` y `como-trabajamos` se citan entre sí sin contradicciones (la de `git add -A` del 1 oct está resuelta); los umbrales 60/75/85 % son los mismos en las tres que los nombran; `cerrar-sesion` 5b (poner al día las guías comparando con el código) se ha cumplido en S6, S7 y S8 (las guías S9–S13 llevan sus «[Claude, puesta al día del 6 oct]»).
6. **`revisor.md`:** ruta corregida (F13), reglas P115 incluidas, solo lee. Se usa en S9 y S13 como dice P35.
7. **`como-trabajamos`:** la marcha vigente (calidad), «Ahora tú», la regla de los tres caminos (P112) y los cambios del 5 y 6 oct están escritos con fecha; el historial largo vive aparte (F14).
8. **Fichas del diario:** las nueve siguen la plantilla, con «Para el vídeo» desde la S00, «Contexto al cerrar» desde la S6, problemas con justificación y fuente, y «Qué entendí» con las palabras de Daniel. Es el mejor activo del proyecto para la memoria.

---

## Resumen para Daniel (5 líneas)

1. Las herramientas (skills, hooks, `settings.json`, `.gitignore`) están bien y no hay secretos ni datos personales en el repositorio; el hook hace lo que promete.
2. Lo único de peso es **quién prueba la app**: desde la S6 lo hace Claude con `adb` (bien declarado), y conviene que la pasada final de la S13 y las 2–3 pruebas del guion del vídeo de cada sesión las hagas tú con el dedo (F01).
3. Cada vez hay más trozos que escribe Claude directamente; todos están declarados, así que no hay problema de honradez, pero son los primeros candidatos de la pasada de apropiación.
4. Las lecciones han crecido de 15 a 25 en cinco días; seis dicen lo mismo sobre pegar código: se funden en dos al cerrar la S9.
5. GitHub enseña a «claude» como colaborador por tres commits del 29 sep: está explicado en la ficha S1; que lo diga también la memoria antes de que lo vea el corrector.

---

## Aplicado (7 oct 2026)

Decidido por Daniel el 7 oct y aplicado el mismo día; detalle en `2026-10-06-cambios-aplicados.md`.

- **F01 → opción de Daniel (P176):** ninguna de las tres. *«Creo que está bien que lo hagas tú porque son pruebas manuales repetitivas; la cosa es que me tendrías que hacer una lista de las pruebas, la reviso, le doy el visto bueno y lo haces»*. Desde ahora: lista con pasos → visto bueno de Daniel → Claude con `adb` → Daniel revisa las capturas. En `como-trabajamos`, `cerrar-sesion` 3, el plan de pruebas, las guías S9, S10 y S13 y `juego-de-datos.md`; fila en `02-memoria.md`.
- **F02:** las seis lecciones de pegado fundidas en dos; fuera las de `local.properties` (S1) y el hook con `-F -`; dos lecciones nuevas del 7 oct.
- **F03:** nada que cambiar (la memoria recoge la lista; la apropiación empieza por esos trozos).
- **F04:** nada hoy; opcional para la S13 (anotado en su guía).
- **F05:** fila en `02-memoria.md` (la frase para la memoria y el README sobre los tres commits del 29 sep).
