# Sesión 13 — 2026-10-08

| Campo | Valor |
|---|---|
| **Fecha** | 2026-10-08 |
| **Sesión nº** | 13 |
| **Objetivo de la sesión** | Del spec: **Cierre del nivel 1**: tema (paleta, naranja), `strings.xml` EN, accesibilidad (sin Scanner, P98 B), revisión del `revisor`, pasada por las 29 manuales, `estado-nivel.md` con todo el nivel 1 en *implementado*; APK en el ordenador, sin push, etiqueta ni Release (P198) |
| **Tiempo dedicado** | 14:35 – HH:MM (inicio – fin, descontando pausas) |
| **Nivel / pieza** | Nivel 1 · <pantalla o capa> (p. ej. *entidades Room*, *pantalla 6c*) |
| **Commit final** | `abc1234` — mensaje del commit |
| **Contexto al cerrar** | NN % (el anillo junto al modelo; P156) |

## Qué se hizo

- **APK (pieza 12), solo cuando Daniel lo diga** (Daniel, 14:40): *«la apk todavía no, yo diré cuándo quiero que la hagas… tengo que decirlo explícitamente para que la hagas»*. Claude no genera ni prepara la APK por su cuenta.
- **Pieza 1 (tema), 14:36–14:50:** Claude repasó `colors.xml`, `themes.xml` y `values-night/themes.xml` y sacó capturas con `adb` en modo claro y en modo noche (1a, 1d, 2a, 2b, 3a, 5a, 5b, 5c, 6a, 6b, 6c y los diálogos de Enviar, Cobrar, «¿Salir sin enviar?» y PIN). **Bien:** todos los botones de acción en `#C75000` con letras blancas (4,6:1, P138); ni rojo ni verde de acento (el rojo, solo en la mesa ocupada con su total); en modo noche los diálogos (P153) y el campo enfocado (P160) en color de texto; una sola animación (la sacudida del PIN, en 1c y 1e). **Mal (de la S12):** el «+ Elegir» de 2b con letras naranjas (unos 4,2:1 de día y 3,6:1 de noche) y el «+» de 3a naranja sobre un círculo transparente encima de la foto (1,4:1 sobre el gris). **Decisiones de Daniel:** 1 → A (el «+» de 3a en un círculo relleno `colorSurface` con el «+» en `colorOnSurface`; frente a B, círculo naranja como el botón flotante de Material 3, https://m3.material.io/components/floating-action-button/overview, y C, justificarlo como texto grande, WCAG 1.4.3, https://www.w3.org/WAI/WCAG21/Understanding/contrast-minimum.html) y 2 → A (*Anular* de 6b se queda en color de texto, como el wireframe 06b, y se apunta para el Project que el spec 10 lo pone naranja). **Arreglo, escrito por Claude** (Daniel: *«las cosas de una línea modifícalas tú»*, a `como-trabajamos`): `android:textColor="?attr/colorOnSurface"` en `botonElegirFoto` de `sheet_categoria.xml`, y `textColor` + `app:backgroundTint="?attr/colorSurface"` en el de `activity_plato.xml`. `installDebug` en verde; capturas de 3a y 2b de día y de noche: se leen bien. El emulador quedó en modo claro y con una comanda pendiente en la mesa 4 (42,00 €) de las capturas (la pasada final empieza desde instalación limpia).
- Lista de lo que existe al terminar y no existía al empezar (clases, pantallas, pruebas que pasan).
- Qué se dejó a medias y en qué estado exacto (para retomarlo sin adivinar).

## Problemas y soluciones

| # | Qué falló | Cómo se resolvió | Justificación (por qué esta solución y no otra) | Fuente |
|---|---|---|---|---|
| 1 | El síntoma tal cual: mensaje de error copiado, o qué hacía la app en vez de lo esperado | Lo que se cambió, en concreto | Por qué era el arreglo correcto; qué otra opción había y por qué se descartó | Enlace y fecha, o "propio" |
| 2 | | | | |

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
