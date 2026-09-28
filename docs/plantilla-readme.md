> **Plantilla del `README.md` de la raíz del repositorio** [Claude]. En la S1 Claude la rellena, Daniel la lee y la cambia a su gusto (regla 2 de `CLAUDE.md`: tiene que entender cada línea). Va en la raíz `C:\AKO\README.md`.

```markdown
# Ako — app Android de pedidos para bares y restaurantes

Proyecto Intermodular del ciclo de Desarrollo de Aplicaciones Multiplataforma (DAM), curso 2026-2027. Autor: Yunkang Daniel Hu Zhou.

**Qué es:** una app para un móvil del restaurante, en vertical, con tres roles: **Propietario** (crea la carta y ve el Resumen de ingresos, detrás de un PIN), **Pedir** (la carta: se elige mesa, se añaden platos al carrito y se envía la comanda) y **Cuenta** (ver lo pedido por cada mesa, quitar líneas, calcular el cambio y cobrar). Funciona sin conexión, en un solo dispositivo. Es un **prototipo académico** (nivel 1 del diseño); la arquitectura cliente-servidor está diseñada pero no programada.

## Stack

Kotlin · Android nativo (XML, Activities y Fragments, ViewBinding) · Room · MVVM sencillo (ViewModel + repositorio, corrutinas y LiveData) · Glide · `minSdk 26`. Versiones en `app-ako/gradle/libs.versions.toml`.

## Cómo abrirlo

1. Android Studio (2026.1 o posterior) con el SDK de Android (API 34 y 36).
2. *File → Open* → la carpeta `app-ako/`.
3. Emulador: Pixel 6, API 34 (o cualquier Android 8.0 o superior).
4. *Run ▶*.

## Cómo pasar las pruebas

- De código (JUnit, sin emulador): `./gradlew.bat testDebugUnitTest` desde `app-ako/`.
- De Room (con el emulador encendido): `./gradlew.bat connectedDebugAndroidTest`.
- Manuales: `docs/spec+doc-pruebas.md` (29 casos, con resultado y fecha).

## Estructura

- `app-ako/` — el proyecto de Android Studio (`yunkang.ako`: `datos/`, `dominio/`, `seguridad/`, `imagenes/`, `ui/`).
- `docs/` — el diseño: `spec-claude-code.md` (diseño y orden de construcción), fichas de pantalla, wireframes, requisitos, diagrama de clases, plan de pruebas, `estado-nivel.md` y el diario de desarrollo (`docs/diario/`, una ficha por sesión).
- `CLAUDE.md` — las reglas de trabajo con Claude Code.

## Uso de IA

El código se ha construido con Claude Code como apoyo, siguiendo las reglas de `CLAUDE.md`. Qué pidió el autor, qué generó la IA y qué revisó, tecleó o cambió el autor está en cada ficha de `docs/diario/`. Lo propuesto por la IA y no pedido lleva la marca `[Claude]`.

## Licencia

Trabajo académico. Sin licencia de uso más allá de la evaluación.
```
