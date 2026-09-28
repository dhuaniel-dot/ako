> **Borrador del `README.md` de la raíz del repositorio** [Claude]. Escrito en la sesión 00 (noche del 24 al 25 sep 2026) a partir de `docs/plantilla-readme.md`, con las versiones reales de `docs/guias/stack-verificado/` y lo decidido en `docs/decisiones-code.md`. **En la S1 (guía `sesion-01.md`, apartado 5, paso 7) Daniel lo lee, cambia lo que quiera y lo pega** en `C:\AKO\README.md`; Claude hace el commit `S1: README`. Regla 2 de `CLAUDE.md`: tienes que entender cada línea.
>
> **Qué cambia respecto a la plantilla** [Claude]: SDK de compilación 37 (no 36: lo exige Glide 5.0.9, apartado 4c de `decisiones-code.md`); ruta real del proyecto (`app-ako/`); comandos de prueba con `gradlew.bat` y la carpeta desde la que se lanzan; la carpeta `docs/guias/`; el diario explicado como la prueba del uso de la IA (spec 13, P226: los commits no llevan atribución); la versión de Android Studio sin número fijo, porque depende de la duda de AGP de la S1 (`sesion-01.md`, apartado 4: AGP 9.4.1 con Android Studio 2026.1.4, o AGP 9.3.3 con la 2026.1.3 instalada); y lo que en la S1 todavía no existe (las pruebas, sus resultados, el código) dicho como lo que se va construyendo o «se anota al ejecutarlo», para que el README no prometa nada falso el día que se pega. **Antes de pegarlo, comprueba tres cosas:** tu nombre completo tal como quieres que salga (la plantilla dice «Yunkang Daniel Hu Zhou»; en Git firmas como «Yunkang Daniel»), el curso y el enlace del repositorio.
>
> Todo lo que va entre las dos líneas de abajo es el archivo. Lo de antes no se pega.

---

```markdown
# Ako — app Android de pedidos para bares y restaurantes

Proyecto Intermodular del ciclo de Desarrollo de Aplicaciones Multiplataforma (DAM), curso 2026-2027. Autor: Yunkang Daniel Hu Zhou.

**Qué es:** una app para un móvil del restaurante, en vertical, con tres roles desde una pantalla de tres botones:

- **Propietario** (detrás de un PIN de cuatro cifras): crea la carta —categorías y platos con precio, alérgenos y foto— y consulta el Resumen de ingresos de cada día.
- **Pedir**: se elige la mesa, se mira la carta, se añaden platos al carrito y se envía la comanda. Salir pide el PIN.
- **Cuenta**: la rejilla de las 60 mesas, lo pedido por cada una con su total, quitar líneas, anular, el recibo con la calculadora de cambio y cobrar.

Funciona **sin conexión y en un solo dispositivo** (la app no pide el permiso de Internet). Es un **prototipo académico**: el nivel 1 del diseño, que se construye por sesiones. La arquitectura cliente-servidor en red local está diseñada, no programada. Qué funciones están ya implementadas y cuáles solo diseñadas se ve en `docs/estado-nivel.md`.

## Stack

Kotlin · Android nativo con vistas XML (Activities y Fragments, ViewBinding; sin Compose) · Room sobre SQLite · MVVM sencillo (ViewModel + repositorio, corrutinas y LiveData) · Glide para las fotos · `minSdk 26` (Android 8.0), `compileSdk` y `targetSdk` 37. Las versiones de las librerías y de los plugins de Gradle están en un solo sitio, `app-ako/gradle/libs.versions.toml`; `minSdk`, `compileSdk` y `targetSdk`, en `app-ako/app/build.gradle.kts`.

## Cómo abrirlo

1. Android Studio compatible con la versión del plugin de Android (AGP) que fija `app-ako/gradle/libs.versions.toml` (tabla de compatibilidad de developer.android.com; el proyecto se desarrolló con Android Studio 2026.1), con el SDK de Android (plataforma 37 para compilar; una imagen del emulador de API 26 o superior).
2. *File → Open* → la carpeta `app-ako/` (no la raíz del repositorio).
3. Esperar a que termine la sincronización de Gradle.
4. Emulador: Pixel 6 con API 34 (o cualquier Android 8.0 o superior).
5. *Run ▶*. En el primer arranque la app pide crear el PIN del Propietario: **si se olvida, no se puede recuperar** y hay que reinstalar (se pierden la carta, las comandas y las fotos).

## Cómo pasar las pruebas

Desde la carpeta `app-ako/` (en Windows, con PowerShell o Git Bash; en macOS o Linux, `./gradlew` en vez de `./gradlew.bat`):

- **Pruebas de código sin emulador** (JUnit, carpeta `app/src/test/`): `./gradlew.bat testDebugUnitTest`
- **Pruebas de la base de datos** (Room en memoria, carpeta `app/src/androidTest/`, con el emulador encendido): `./gradlew.bat connectedDebugAndroidTest`
- **Pruebas manuales**: `docs/spec+doc-pruebas.md` — 29 casos, uno por requisito del nivel 1; el resultado y la fecha de cada uno se anotan al ejecutarlo. El orden para prepararlas está en `docs/guias/juego-de-datos.md`.

El plan tiene nueve pruebas de código (seis en `test/` y tres en `androidTest/`); cuáles existen y han pasado en cada momento, con su fecha, también está en `docs/spec+doc-pruebas.md`.

## Estructura

- `app-ako/` — el proyecto de Android Studio. Paquete `yunkang.ako`, por capas: `datos/` (tablas, consultas y repositorios), `dominio/` (cálculos puros que se prueban sin emulador), `seguridad/` (el PIN, guardado como hash con sal, nunca en claro), `imagenes/` (fotos) y `ui/` (pantallas).
- `docs/` — el diseño y el seguimiento: `spec-claude-code.md` (diseño y orden de construcción), fichas de pantalla, wireframes, requisitos, diagrama de clases, plan de pruebas, `estado-nivel.md` (qué está hecho), `decisiones-code.md`, `guias/` (una guía por sesión) y `diario/` (una ficha por sesión de trabajo).
- `CLAUDE.md` y `.claude/` — las reglas y las herramientas de trabajo con Claude Code.

## Uso de IA

El código se construye con **Claude Code** (Anthropic) como apoyo, siguiendo las reglas de `CLAUDE.md`: el autor pide cada pieza, Claude Code la explica y da el código, y el autor lo teclea (o lo revisa, si en algún tramo Claude Code lo escribe directamente: cada ficha dice cuál de las dos), lo prueba y responde preguntas de comprensión antes de seguir. **Qué pidió el autor, qué generó la IA y qué revisó, tecleó o cambió el autor está en cada ficha de `docs/diario/`**: los commits no llevan la marca de la IA, así que el diario es el registro de ese reparto. Lo propuesto por la IA y no pedido lleva la marca `[Claude]`.

## Licencia

Trabajo académico. Sin licencia de uso más allá de la evaluación.
```

---

**Si quieres cambiar algo y no sabes cómo decirlo en el README, dilo con tus palabras en el chat de la S1 y Claude te propone la frase.**
