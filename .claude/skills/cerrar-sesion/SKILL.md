---
name: cerrar-sesion
description: 'Cierra una sesión de código de Ako con el ritual completo: pruebas con fecha, ficha del diario, decisiones-code, estado-nivel, dos commits con coautor Claude (P111), push y el primer mensaje del chat siguiente.'
when_to_use: 'Cuando Daniel diga «cerramos la sesión» o «hemos terminado», o escriba /cerrar-sesion. Solo si el objetivo está cumplido y probado; si falta algo, usa relevo.'
---

# Cerrar sesión

Ritual de cierre del spec (`docs/spec-claude-code.md`, apartados 11 y 13). **Solo se cierra una sesión cuyo objetivo está cumplido y probado.** Si falta algo, no se cierra: se usa `relevo` y la ficha dice qué falta y por qué.

## Pasos

1. **Pruebas de código.** Si existe el proyecto (`app-ako/`), desde `C:\AKO\app-ako`: `./gradlew.bat testDebugUnitTest` (Git Bash) o `.\gradlew.bat testDebugUnitTest` (PowerShell); y `connectedDebugAndroidTest` con el emulador encendido si la sesión tiene pruebas de `androidTest/`. Todas en verde, o la sesión no se cierra. (Gradle ya usa el Java de Android Studio por `~/.gradle/gradle.properties`.)
2. **Revisión independiente (solo en S4, S9 y S13).** Lanzar el subagente `revisor` sobre `app-ako/app/src`. Sus hallazgos *Alta* se arreglan antes del commit y van a la tabla de problemas de la ficha; los *Media* y *Baja* se anotan en *Siguiente sesión* si no da tiempo.
3. **Pruebas manuales.** Listar las P-M que cierran la sesión (tabla del apartado 11 del spec). Preguntar a Daniel el resultado de cada una con preguntas numeradas (Pasa / Falla / Parcial, y qué vio). Anotar **Resultado**, **Observaciones** y **Fecha** en `docs/spec+doc-pruebas.md`. Una prueba que necesita algo de una sesión posterior se anota *Parcial* con lo comprobado y se repite entera cuando exista el prerrequisito (P6).
4. **Ficha del diario** (`docs/diario/sesion-NN.md`), todo lo que quedó en plantilla:
   - **Tiempo dedicado**: preguntar hora de fin y pausas; minutos reales, incluidos los de leer errores y equivocarse.
   - **Qué se hizo**: lo que existe ahora y no existía al empezar. Si hay `docs/diario/EN-CURSO.md`, **pasar aquí sus decisiones y lo no entendido antes de borrarlo** (paso 6).
   - **Problemas y soluciones**: cada fila con *justificación* (por qué esta solución y no otra) y *fuente*.
   - **Qué entendí y qué no**: **con las palabras de Daniel**. Preguntarle una a una las piezas de la sesión: qué sabría explicar sin leerlo y qué no todavía. Lo que no entendió se vuelve a explicar en la sesión siguiente antes de escribir más código encima.
   - **Pruebas**: códigos que pasan.
   - **Uso de IA**: una línea por pieza: qué pidió Daniel, qué generó Claude, qué revisó, tecleó o cambió Daniel. Sin rebajarlo.
   - **Siguiente sesión**: una línea.
5. **`docs/decisiones-code.md`**: añadir en el apartado 5 las decisiones nuevas de la sesión que no estén en el spec (fecha · decisión · consecuencia · [Claude] si procede), incluidas las que estaban en EN-CURSO.md. **`docs/estado-nivel.md`**: cambiar el *Estado* de los RF de la sesión (`implementado` o `implementado, no probado`), poner la *Sesión* y actualizar el recuento de arriba.
6. Borrar `docs/diario/EN-CURSO.md` si existe (ya volcado en los pasos 4 y 5). **Revisar la forma de trabajar** como en `relevo` (paso 3b): si Daniel dijo algo sobre cómo trabajar o algo fue mal, actualizar `como-trabajamos` con línea fechada y decírselo. Si Claude se equivocó en algo que puede repetirse (una orden que no funciona en este PC, una trampa de Gradle, una lectura errónea del spec), una línea en `docs/lecciones-claude.md`; y borrar de ahí lo que ya no haga falta.
7. **Commit y push, en dos commits:**
   - `git add -A` · `git commit -m "S<N>: <objetivo de una línea>"` · `git push` → es **el commit de código** de la sesión.
   - Anotar su identificador corto (7 caracteres) en *Commit final* de la ficha · `git add -A` · `git commit -m "S<N>: ficha del diario"` · `git push`.
   El mensaje lleva la línea `Co-Authored-By: Claude` (P111; la añade el ajuste `attribution`) y empieza por `S<N>:` (el hook lo exige). *Nota:* la plantilla del diario habla de «un commit» y del «último commit»; en Ako se hace así (decisión de la sesión 00, anotada en `decisiones-code.md`) para que la ficha lleve el hash del código y el repositorio quede limpio.
8. **Decir a Daniel en tres líneas:** qué se cerró, qué pruebas quedaron *Parcial*, y recordarle que el chat del Project lee directamente la carpeta `C:\AKO` (ficha, `decisiones-code.md` §6 «Para el Project» y fuentes nuevas): **no hace falta subir nada a mano** (Daniel, 30 sep).
8b. **Fable de guardia:** si durante la sesión se usó `/advisor fable`, recordar a Daniel `/advisor off` antes del chat siguiente (regla de `~/.claude/CLAUDE.md`: apagado por defecto, Claude lo propone cuando algo se complica y Daniel lo activa).
8c. **Resumen para leer en el móvil (Daniel, 28 sep).** Antes de entregar el mensaje del chat nuevo: **preguntar antes a Daniel qué le ha costado entender y qué curiosidades quiere saber** (lo incluye en los dos), y escribir dos resúmenes de **lo hecho y explicado en este chat** (longitud la que pida el contenido): uno **sencillo**, coloquial y con ejemplos de la vida real, y otro **normal**, con los términos técnicos (sirve para la memoria y el vídeo). Guardarlos como PDF en `docs/resumenes/AAAA-MM-DD-S<N>-sencillo.pdf` y `…-normal.pdf` (fuente en Markdown al lado; **la carpeta está en `.gitignore`: no se suben a GitHub**, son para que Daniel estudie, 30 sep) y **enviarlos a Daniel por Gmail** con los dos resúmenes **dentro del cuerpo del correo en HTML** (los PDF adjuntos no caben en la llamada de Gmail: 30 sep): enseñarle asunto y adjuntos y **esperar su «sí» antes de enviar** (enviar un correo siempre se confirma).
9. **Entregar el primer mensaje del chat siguiente, listo para copiar** (cerrar una sesión es también cambiar de chat; regla de Daniel: siempre que se cambia de chat, el mensaje lo escribe Claude). Modelo:

   > Sesión S<N+1> de Ako: <objetivo de una línea de la tabla del apartado 11 del spec>. Lee `CLAUDE.md`, `.claude/skills/como-trabajamos/SKILL.md`, `docs/decisiones-code.md`, `docs/diario/sesion-NN.md` (la última) y `docs/estado-nivel.md`; después el apartado de la S<N+1> en `docs/spec-claude-code.md` y la ficha y los wireframes de <pantalla>. Abre la sesión y dime en tres líneas por dónde empezamos.

   Debajo, en una línea: **modelo y esfuerzo recomendados** para ese chat (Opus 5.5 medio por defecto; alto en S4, S6, S8 y S9; Plan Mode al abrir en esas cuatro) y si conviene Fable por ser un hito.

## Lo que no se hace

- No se reescribe una ficha cerrada: si algo de una sesión anterior estaba mal, se dice en la ficha de la sesión en que se descubre.
- No se cierra con pruebas en rojo ni con el objetivo a medias.
- No se usa `git commit --amend` ni se cambia de rama: solo `main`.
