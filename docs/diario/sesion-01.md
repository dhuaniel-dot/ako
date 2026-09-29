# Sesión 01 — 2026-09-28

| Campo | Valor |
|---|---|
| **Fecha** | 2026-09-28 |
| **Sesión nº** | 01 |
| **Objetivo de la sesión** | Proyecto y repositorio: proyecto vacío en Android Studio dentro de `C:\AKO\app-ako`, primer arranque en el emulador, Git y repositorio público en GitHub, primer commit sin atribución |
| **Tiempo dedicado** | ~3 h 20 min en total, **aproximado** (Daniel no está seguro: el día 1 se paró a esperar la respuesta del profesor). Día 1 (28 sep): 19:48 – ~22:30, incluido redactar el correo al profesor. Día 2 (29 sep): 14:54 – ~15:40 |
| **Nivel / pieza** | Bases del proyecto (no toca ningún RF) |
| **Commit final** | `a047fb6` — S1: proyecto y repositorio |

## Qué se hizo

- Día 1 (28 sep, 19:48 – ~22:30): asistente de Android Studio (Custom, SDK en `C:\Android\Sdk`) · proyecto `app-ako` creado por Daniel (Empty Views Activity, paquete `yunkang.ako` **P104**, nombre visible `AKO` **P105**, API 26) · «Hello World!» en el emulador · archivos de Gradle del `stack-verificado` copiados por Claude (**P106 → A**) y `AkoGlideModule.kt`: `assembleDebug` y `testDebugUnitTest` en verde, sincronizado y ejecutado en Android Studio · `git init` en `C:\AKO`, commit `9dbb69a` sin atribución (autor comprobado) · repositorio público https://github.com/dhuaniel-dot/ako creado y subido.
- Daniel escribió al profesor para confirmar el uso de la IA; la sesión se para hasta su respuesta (ver `EN-CURSO.md`).
- A medias: captura de GitHub, README (`S1: README`) y cierre.
- Día 2 (29 sep): retomada a las 14:54. El profesor contestó que el uso de la IA está permitido tal como se planteó (respuesta literal en `decisiones-code.md` 5.3); se sigue con la línea 2.
- Día 2, además: correos personales fuera de los documentos y revisión de claves en todo el historial (limpio) (P108, skill `seguridad`) · commits con coautor Claude (P111) · README con nombre «Huzhou» y el visto bueno del profesor (P109, P110) · regla P112 (Daniel elige las decisiones de código entre mínimo 3 opciones, una con fuente de internet) · regla «somos un equipo» · hook: el autor del commit tiene que ser Daniel (bloque pegado por Daniel, probado).
- Nada a medias.

## Problemas y soluciones

| # | Qué falló | Cómo se resolvió | Justificación (por qué esta solución y no otra) | Fuente |
|---|---|---|---|---|
| 1 | El asistente de Android Studio proponía el SDK en `C:\Users\dhuan\AppData\Local\Android\Sdk` (600 MB de descarga) | Instalación *Custom* y ruta cambiada a `C:\Android\Sdk` con el botón de carpeta: «An existing Android SDK was detected», 50,4 MB | Lo que se escribe en `AppData` desde la app de Claude acaba en otra carpeta (sesión 00, problema 2); el SDK ya estaba en `C:\Android\Sdk` | Propio; `sesion-01.md` apartado 1 |
| 2 | En GitHub salía «claude» como colaborador: 3 commits de otro chat (`87d352e`, `74e521b`, `ebd1cd9`) se hicieron con la identidad de Git «Claude <noreply@anthropic.com>» | No se reescribe el historial (`push --force` prohibido); se añadió al hook un paso que bloquea el commit si el autor no es Daniel con el correo anónimo de GitHub. Probado: pasa con su identidad, bloquea con «Claude» | Arreglar el origen (la identidad) en vez de tapar el síntoma; reescribir el historial rompe la regla de no forzar | Propio |
| 3 | Los permisos de la app no dejaron a Claude editar el hook (protección contra que la IA cambie sus propias reglas) | Daniel pegó el bloque en el Bloc de notas; primero lo escribió en la terminal de PowerShell por error («no se reconoce como nombre de un cmdlet»): el código de un archivo no se teclea en la terminal | Es la protección correcta; no se buscó forma de saltársela | Propio |
| 4 | Daniel eligió el paquete `yunkang.ako` y la documentación decía `es.daniel.ako`: al copiar `stack-verificado/` el `namespace` no habría coincidido con el código | Claude cambió el nombre en `CLAUDE.md`, spec, guías, `stack-verificado/` y `verificar` antes de copiar (P104) | Mejor cambiar la documentación que el proyecto recién creado: la decisión es de Daniel | Propio |

## Qué entendí y qué no

- **Entendí** (palabras de Daniel, 29 sep): **commit**: «es como está el código en ese momento»; **push**: «envías el código a un sitio» · **emulador**: «imito en mi ordenador lo que sería un móvil» · **package name**: «es como un identificador, y por eso no se cambia» · **README**: «algo que se pone para que se lea antes de usar la aplicación o instalarla» (le falta: es la portada del repositorio en GitHub).
- **No entendí todavía:** **Gradle** («no sé») · **catálogo de versiones**: lo confunde con las versiones del proyecto (es la lista de librerías con su versión) · **por qué `minSdk 26`**: «por las funciones, no estoy seguro» (es por el hash PBKDF2 con SHA-256 del PIN, que viene desde la API 26). En general, dice Daniel, le ha costado entender cómo funciona todo porque nunca había usado Android Studio. **Repasar estos tres al empezar la S2**, antes de escribir código.

## Pruebas

- Pruebas de código: ninguna P-C todavía; `testDebugUnitTest` en verde el 29 sep (solo la prueba de ejemplo de la plantilla).
- Pruebas manuales: la S1 no tiene. Entregables del spec comprobados: la app vacía arranca en el emulador (28 sep) · el repositorio se ve en GitHub (captura del 29 sep) · el primer commit (`9dbb69a`) no lleva la línea de coautor.

## Uso de IA en esta sesión

- Daniel manejó Android Studio (asistente, proyecto, Sync, Run) guiado por Claude con capturas; decidió el paquete, el nombre visible y quién copiaba Gradle.
- Claude copió por consola los archivos de Gradle del `stack-verificado` (P106 → A) y `AkoGlideModule.kt`, los explicó y compiló; hizo `git init`, el commit y la creación del repositorio (con el «sí» de Daniel).
- Claude corrigió la documentación por P104/P105 y ayudó a redactar el correo al profesor sobre el uso de la IA (lo escribió y envió Daniel).
- Día 2: Claude cambió reglas y documentos por las decisiones de Daniel (P108–P112), revisó claves en todo el historial, escribió el README desde el borrador (Daniel lo leyó y lo aprobó) y escribió el bloque del hook, que Daniel pegó y Claude probó.

## Siguiente sesión

- S2 (entidades Room y precarga): empezar repasando Gradle, el catálogo de versiones y el porqué de `minSdk 26`; las decisiones de código, con 3 opciones (P112).
