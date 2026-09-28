# EN CURSO — Sesión 01 — 2026-09-28, noche

**Objetivo de la sesión:** Proyecto y repositorio: proyecto vacío en Android Studio dentro de `C:\AKO\app-ako`, primer arranque en el emulador, Git y repositorio público en GitHub, primer commit sin atribución
**Motivo del relevo:** pausa decidida por Daniel: espera la respuesta del profesor sobre el uso de la IA antes de seguir. Según lo que diga: seguir igual, cambiar la forma de usar la IA o valorar pasar a la línea 1 (también por tiempo)

## Pieza en curso
- Qué es: guía `sesion-01.md`, apartado 5, pasos 6 y 7 (captura de GitHub y README)
- Estado: compila (proyecto sin cambios desde el commit `9dbb69a`)
- Qué falta exactamente: captura de https://github.com/dhuaniel-dot/ako (entregable); README desde `docs/guias/readme-borrador.md` (revisar nombre: Daniel firma «Yunkang Daniel Huzhou», la plantilla dice «Hu Zhou»; paquete ya es `yunkang.ako`) y commit `S1: README`; después `cerrar-sesion`

## Terminado hoy
- Asistente de Android Studio con el SDK en `C:\Android\Sdk` (Custom)
- Proyecto `app-ako` (Empty Views Activity, `yunkang.ako`, API 26, Kotlin DSL); «Hello World!» en el emulador
- Archivos de Gradle de `stack-verificado/` copiados por Claude (P106 → A) y `AkoGlideModule.kt`; `assembleDebug` + `testDebugUnitTest` en verde; sincronizado y ejecutado en Android Studio
- `git init` en `C:\AKO`, primer commit `9dbb69a` (autor Yunkang Daniel, sin atribución comprobado con `git log`), repositorio público creado y subido

## Último commit
`9dbb69a` — S1: proyecto Android Studio, SDK y emulador

## El bloque de código de la pieza en curso, tal como se le dio a Daniel
Ninguno (la S1 no tiene código tecleado por Daniel).

## Decisiones tomadas en este chat que no están en ningún documento
- En `decisiones-code.md` 5.3 ya están P104, `minSdk 26` y P105. Faltan por pasar al cerrar: **P106 → A** (Claude copia los archivos de Gradle por consola: configuración, no código de negocio) y **P107** (sustituida: en cada cambio de chat, dos PDF por Gmail; ya en `como-trabajamos`, `relevo` 4b y `cerrar-sesion` 8c).
- `rootProject.name = "AKO"` en `stack-verificado/settings.gradle.kts` [Claude] (coincide con lo que generó el asistente).
- **Consulta al profesor sobre la IA** (28 sep, enviada por Daniel): pregunta si se permite Claude Code para preparación, documentación y como ayuda al programar (explicar cada fase antes, ejemplos, comprobar que compila), declarándolo en el repositorio y la memoria. **Guardar la respuesta literal** en `decisiones-code.md` y en la ficha.

## Lo que Daniel dijo que no entendió todavía
- La explicación técnica de los archivos de Gradle «a medias»: se repitió con el ejemplo del bar y le sirvió. Pide lenguaje coloquial y ejemplos de la vida real (ya en `como-trabajamos`).

## Los tres pasos siguientes, en orden
1. Leer la respuesta del profesor y decidir con Daniel: seguir igual / ajustar el uso de la IA (reflejarlo en `CLAUDE.md`, `como-trabajamos` y README) / valorar la línea 1 (comparar alcance y tiempo). **El uso de la IA no se esconde**: se ajusta a los límites que diga el profesor y se declara.
2. Si se sigue: captura de GitHub y README (`S1: README`).
3. `cerrar-sesion` de la S1 (con los dos PDF por Gmail, preguntando antes a Daniel qué le costó y qué curiosidades tiene).

**Modelo y esfuerzo para el chat siguiente:** Opus 5.5, esfuerzo medio
