# Sesión 01 — 2026-09-28

| Campo | Valor |
|---|---|
| **Fecha** | 2026-09-28 |
| **Sesión nº** | 01 |
| **Objetivo de la sesión** | Proyecto y repositorio: proyecto vacío en Android Studio dentro de `C:\AKO\app-ako`, primer arranque en el emulador, Git y repositorio público en GitHub, primer commit sin atribución |
| **Tiempo dedicado** | 19:48 – … (descontando pausas) |
| **Nivel / pieza** | Bases del proyecto (no toca ningún RF) |
| **Commit final** | `abc1234` — mensaje del commit |

## Qué se hizo

- Día 1 (28 sep, 19:48 – ~22:30): asistente de Android Studio (Custom, SDK en `C:\Android\Sdk`) · proyecto `app-ako` creado por Daniel (Empty Views Activity, paquete `yunkang.ako` **P104**, nombre visible `AKO` **P105**, API 26) · «Hello World!» en el emulador · archivos de Gradle del `stack-verificado` copiados por Claude (**P106 → A**) y `AkoGlideModule.kt`: `assembleDebug` y `testDebugUnitTest` en verde, sincronizado y ejecutado en Android Studio · `git init` en `C:\AKO`, commit `9dbb69a` sin atribución (autor comprobado) · repositorio público https://github.com/dhuaniel-dot/ako creado y subido.
- Daniel escribió al profesor para confirmar el uso de la IA; la sesión se para hasta su respuesta (ver `EN-CURSO.md`).
- A medias: captura de GitHub, README (`S1: README`) y cierre.
- Día 2 (29 sep): retomada a las 14:54. El profesor contestó que el uso de la IA está permitido tal como se planteó (respuesta literal en `decisiones-code.md` 5.3); se sigue con la línea 2.

## Problemas y soluciones

| # | Qué falló | Cómo se resolvió | Justificación (por qué esta solución y no otra) | Fuente |
|---|---|---|---|---|
| 1 | El asistente de Android Studio proponía el SDK en `C:\Users\dhuan\AppData\Local\Android\Sdk` (600 MB de descarga) | Instalación *Custom* y ruta cambiada a `C:\Android\Sdk` con el botón de carpeta: «An existing Android SDK was detected», 50,4 MB | Lo que se escribe en `AppData` desde la app de Claude acaba en otra carpeta (sesión 00, problema 2); el SDK ya estaba en `C:\Android\Sdk` | Propio; `sesion-01.md` apartado 1 |
| 2 | Daniel eligió el paquete `yunkang.ako` y la documentación decía `es.daniel.ako`: al copiar `stack-verificado/` el `namespace` no habría coincidido con el código | Claude cambió el nombre en `CLAUDE.md`, spec, guías, `stack-verificado/` y `verificar` antes de copiar (P104) | Mejor cambiar la documentación que el proyecto recién creado: la decisión es de Daniel | Propio |

## Qué entendí y qué no

- **Entendí:** por qué `minSdk 26`: el hash PBKDF2 con SHA-256 del PIN viene en Android desde la API 26 sin librerías extra, y llega al ~98,4 % de los móviles; es la versión mínima (funciona también en las más nuevas), no confundir con `targetSdk` 37. *(Daniel: añade el resto con tus palabras al cerrar)*
- **No entendí todavía:** lo que funciona pero no sabría explicar. Se vuelve a ello en la sesión siguiente.

## Pruebas

- Pruebas de código que pasan al terminar: P-C-nn, …
- Pruebas manuales ejecutadas en esta sesión: P-M-nn (resultado y fecha ya apuntados en `spec+doc-pruebas.md`).

## Uso de IA en esta sesión

- Daniel manejó Android Studio (asistente, proyecto, Sync, Run) guiado por Claude con capturas; decidió el paquete, el nombre visible y quién copiaba Gradle.
- Claude copió por consola los archivos de Gradle del `stack-verificado` (P106 → A) y `AkoGlideModule.kt`, los explicó y compiló; hizo `git init`, el commit y la creación del repositorio (con el «sí» de Daniel).
- Claude corrigió la documentación por P104/P105 y ayudó a redactar el correo al profesor sobre el uso de la IA (lo escribió y envió Daniel).

## Siguiente sesión

- Con qué se empieza, en una línea.
