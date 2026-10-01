# Sesión 05 — 2026-10-01

| Campo | Valor |
|---|---|
| **Fecha** | 2026-10-01 |
| **Sesión nº** | 05 |
| **Objetivo de la sesión** | Del spec: **Selector y PIN** (1a, 1b, 1c, 1e) + `ConfirmacionDialog`; P-M-01, 02, 03 y 13 pasan |
| **Tiempo dedicado** | 16:43 – 18:05, 1 h 22 min. Daniel pidió cerrar rápido y no detalló pausas: se apuntan como ninguna [Claude] |
| **Nivel / pieza** | Nivel 1 · pantalla 1 (1a, 1b, 1c, 1e), Panel provisional y `ConfirmacionDialog` |
| **Commit final** | `e32acd2` — S5: selector y PIN (1a, 1b, 1c, 1e) y ConfirmacionDialog (antes, intermedios `639a5ac`, `786eccd`, `ddb3621`, `966793c`, `658a005`, `a72888e`) |

## Qué se hizo

- Al abrir (16:43): Daniel decide **hacer los repasos de la S4 (DAO, `withContext` frente a `suspend`, el actor de la prueba, R6) al final de la sesión**, no al principio. Se empieza directamente por la pieza 1.
- Pieza 1: 21 cadenas de la pantalla 1 en `strings.xml` (comunes, 1a, 1b, 1c, 1e, `panel_btn_terminar` y la provisional `pendiente_sesion_posterior` [Claude]). Literales iguales al borrador, sin claves repetidas; los recursos compilan. Daniel, a la pregunta del inglés: «string» (bien: solo se añade otro `strings.xml`; diseños y Kotlin no se tocan).
- Pieza 2: P138 → A (`#C75000`, texto blanco); `acento` en `colors.xml` y `colorPrimary`/`colorOnPrimary` en los dos `themes.xml` (día y noche). Compila. Daniel, a la pregunta: «esos son los colores del estado de las mesas» (bien).
- Pieza 3: `ui/selector/SelectorActivity.kt` (borde a borde, P139), `activity_selector.xml` (`FragmentContainerView` `contenedor`), manifiesto con `SelectorActivity` como LAUNCHER y `portrait`; `MainActivity.kt` y `activity_main.xml` borrados. Compila y la app arranca en blanco sin error.
- Pieza 4: P140 → B (fábrica `viewModelFactory`), P141 → A (`hayPin()` normal). `ui/selector/SelectorViewModel.kt` solo con `hayPin` y su `Factory` [Claude: `crearPin` y `comprobarPin` en sus piezas]. Compila.
- Pieza 5: `fragment_selector.xml` (ConstraintLayout, nombre 48 sp, subtítulo, tres `MaterialButton` de 72 dp; *Pedir* relleno naranja, los otros con borde), `SelectorFragment` (solo infla) y `SelectorActivity` monta 1a con `savedInstanceState == null`. En el emulador sale como el wireframe 01a. Daniel, a «dp frente a sp»: «uno cambia y otro no» (bien a medias: falta que `sp` crece con el tamaño de letra del usuario, RNF-11).
- Pieza 6: `fragment_crear_pin.xml` (barra sin Atrás, dos `TextInputLayout` `numberPassword` con `maxLength` 4, aviso en caja gris, *Aceptar* apagado), `CrearPinFragment` (solo infla) y `SelectorActivity` con el ViewModel (`by viewModels { SelectorViewModel.Factory }`) decide 1a/1b con `hayPin()`. En el emulador sale 1b como el wireframe 01b; teclado numérico. Daniel, a «¿por qué 1b no tiene Atrás?»: «no hay nada a lo que volver» (bien).
- Pieza 7 (decisiones): P142 → A (`suspend fun crearPin`; la pantalla apaga *Aceptar*, espera y pasa a 1a). P143 → A (se deja `apply` en `PinStore`; Daniel pidió antes ventajas y pegas de A y B). Borrador probado por Claude en el emulador (1234/1235 → aviso y campos vacíos; 1234/1234 → 1a), deshecho y datos de la app borrados (`pm clear`) para que Daniel haga P-M-01.
- Pieza 7: `SelectorViewModel.crearPin` (`suspend`) y `CrearPinFragment` con su comportamiento (Aceptar con 4 + 4, «Los PIN no coinciden» y campos vacíos, guardar y pasar a 1a). **P-M-01 pasa** (Daniel, emulador). Claude comprobó que el archivo `pin` solo tiene `sal` y `hash`.
- Pieza 7, pregunta «¿dónde queda el PIN y qué hay?»: «en el archivo propio pin se guardan sal y hash» (bien).
- Pieza 8 [Claude: «sigue abierto con PIN incorrecto» se adelanta de la 9 a la 8, porque el Aceptar de serie cerraría el diálogo antes de terminar la comprobación]: `dialog_pin.xml`, `PinDialog` (`DialogFragment` + `MaterialAlertDialogBuilder`, Aceptar reenganchado en `setOnShowListener`, sobre `CLAVE_RESULTADO`), `SelectorViewModel.comprobarPin` y *Propietario* abre 1c. Compila. Daniel, a «¿quién comprueba y quién decide después?»: «el encargado; la libreta decide qué va después» (lo primero bien —el repositorio, con `PinStore`—; lo segundo no: decide la Activity que abrió el diálogo al recoger el sobre).
- Pieza 9: sacudida con `ObjectAnimator` (`translationX`, 400 ms) en `PinDialog` al fallar. Compila. Daniel preguntó qué es «sacudida» (explicado: el «no» con la cabeza, como la pantalla de bloqueo del móvil); a «¿por qué es accesibilidad?»: «porque ese gesto ya se entiende como que está mal» (bien; falta: sin depender del color ni de leer, RNF-13).
- Pieza 10 [Claude: `PanelViewModel` pasa a la pieza 11]: `ui/panel/PanelActivity.kt` y `activity_panel.xml` provisionales (Atrás, Cambiar PIN sin conectar, Terminar), manifiesto con `PanelActivity` (`exported=false`, vertical) y el buzón de `SelectorActivity` (`setFragmentResultListener`) que abre el Panel. Compila. Daniel, a «¿por qué salir del Panel no pide PIN y salir de Pedir sí?»: «porque está el cliente» (bien).
- **P-M-02 pasa** (Daniel, emulador; Panel provisional).
- Pieza 11 (decisión): P46 vuelta a preguntar (P112) → B (primero el PIN actual, luego que coincidan los nuevos, luego guardar). Borrador probado por Claude en el emulador (0000 → «PIN incorrecto» en el actual sin tocar los nuevos; 1234 → cambia; 5678 abre el Panel), deshecho y datos de la app borrados (`pm clear`).
- Pieza 11: `dialog_cambiar_pin.xml`, `ui/panel/PanelViewModel.kt` (`comprobarPin`, `cambiarPin`, fábrica P140), `ui/panel/CambiarPinDialog.kt` (orden P46 B con `else if`, sacudida en el actual) y *Cambiar PIN* conectado en el Panel. **P-M-03 pasa** (Daniel). Daniel, a «actual mal y nuevo bien, ¿qué queda guardado y quién lo decide?»: «nada, porque como está mal no pasa la primera» (bien: queda el PIN viejo; lo decide el orden de la pantalla y, por debajo, `cambiarPin` del repositorio).
- Pieza 12: P144 → A. `ui/comun/ConfirmacionDialog.kt` (`nueva(titulo, texto, afirmativo, negativo = null, clave)`, textos en `arguments`, sobre con `RESPUESTA_AFIRMATIVA`) y *Pedir*/*Cuenta* abren la caja provisional. Compila. **P-M-13 Parcial** (Daniel). Daniel, a «¿por qué «¿Salir sin enviar?» no necesita clase nueva?»: «no sé» (es la misma caja con otros textos y el negativo, P84).
- **P-M-03 repetida entera** tras la pieza 12 (Claude vio que tras su `pm clear` no había PIN guardado, así que la primera vez no pudo hacerse sobre esos datos); pasa. Claude comprobó que el archivo `pin` se reescribió durante la prueba.
- Repasos al final (DAO, `suspend`/`withContext`, actor, R6): Claude los escribió en el chat con 4 preguntas; Daniel decidió que **los repasos van siempre al PDF** y que solo los pedirá en el chat si quiere (`como-trabajamos` actualizado). Las 4 preguntas quedan sin contestar: van al PDF.
- **Existe al terminar:** `ui/selector/` (`SelectorActivity`, `SelectorFragment`, `CrearPinFragment`, `PinDialog`, `SelectorViewModel`), `ui/panel/` (`PanelActivity` provisional, `PanelViewModel`, `CambiarPinDialog`), `ui/comun/ConfirmacionDialog`; 6 diseños XML; tema con el naranja; 21 cadenas nuevas; `MainActivity` borrada. Commits intermedios `639a5ac`, `786eccd`, `ddb3621`, `966793c`, `658a005`, `a72888e`.
- **A medias:** nada. Provisional y declarado [Claude]: el Panel (lo sustituye la S6) y la caja «Esta parte llega en una sesión posterior» detrás de *Pedir* (S8) y *Cuenta* (S9).
- **Al cerrar:** Daniel pidió terminar rápido («ponlo todo»): Claude rellenó la ficha con lo que Daniel dijo durante la sesión y envió los PDF sin esperar confirmación, por indicación expresa de Daniel.

## Problemas y soluciones

| # | Qué falló | Cómo se resolvió | Justificación (por qué esta solución y no otra) | Fuente |
|---|---|---|---|---|
| 1 | Pieza 2: la respuesta a la pregunta de comprensión («eso es por nuestro diseño…») quedó escrita **dentro** de `values-night/themes.xml`, después de `</resources>` (se tecleó en Android Studio en vez de en el chat). Un XML con texto suelto tras la etiqueta de cierre no compila | Claude lo vio leyendo el archivo y quitó el texto sobrante (arreglo mecánico); `assembleDebug` en verde | Leer el archivo entero tras cada «ya está» (S4) lo caza antes que el compilador; el texto fuera de `</resources>` rompe el XML | Propio |
| 2 | Al contestar P139, la «a» volvió a quedar escrita al final de `values-night/themes.xml` (Android Studio tenía el foco y guarda solo): `mergeDebugResources` falló con «El contenido no está permitido en la sección final» | Claude la quitó y avisó a Daniel de escribir las respuestas en el chat de Claude, no en Android Studio | Segunda vez con el mismo archivo abierto: la causa es dónde está el cursor, no el código | Propio |
| 3 | Pieza 6: el archivo 3 (`SelectorActivity` entera) se pegó en `SelectorFragment.kt`. «Redeclaration» (dos clases `SelectorActivity`) y «Unresolved reference 'SelectorFragment'» (la clase del Fragment desapareció) | Daniel vuelve a poner el Fragment en `SelectorFragment.kt` y la Activity en `SelectorActivity.kt` | «Redeclaration» = el mismo nombre de clase en dos archivos del mismo paquete; mirar la pestaña abierta antes de Ctrl+A | Propio |
| 4 | Pieza 10: «Unresolved reference 'PanelActivity'» en `SelectorActivity.kt:13`. El paquete `panel` se creó **dentro** de `selector` (`yunkang.ako.ui.selector.panel`): Android Studio enseña `ui` y `selector` juntos como «ui.selector» (paquetes intermedios compactados) y el clic derecho sobre esa fila crea la carpeta dentro de `selector` | Claude movió el archivo a `ui/panel/` y cambió su línea `package` (arreglo mecánico); compila. Ahora que `ui` tiene dos carpetas, Android Studio ya las enseña por separado | `Unresolved reference` con el archivo existiendo = está en otro paquete; se mira la línea `package` del archivo. Alternativa descartada: *Refactor → Move* desde Android Studio (más pasos para lo mismo). Consecuencia: Android Studio ya había añadido a Git la copia de la ruta equivocada y se coló en el commit `ddb3621`; se quitó con otro commit (`966793c`), porque `--amend` está prohibido. Al mover un archivo a mano, mirar `git status` antes del commit (`AD` = añadido y luego borrado) | Propio |

## Qué entendí y qué no

- Pieza 3, «¿por qué 1a y 1b son dos Fragments de una Activity?»: «no sé». Explicado con la entrada del bar (cerradura una vez / tres puertas cada día; misma memoria, Atrás sin volver a 1b). Comprobación: «tras crear el PIN, Atrás en 1a ¿vuelve a 1b?» → «no» (bien: sale de la app).
- Pieza 4: «¿qué se pierde si Android rehace la pantalla?» → «la pantalla» (bien); «¿por qué la pantalla no pregunta al repositorio?» → «no sé». Explicado con el camarero de paso y la libreta del encargado de sala; comprobación «si el camarero pide comprobar el PIN y lo cambian, ¿qué pasa con la respuesta?» → «se pierde» (bien).
- **Entendí (por sus respuestas en la sesión):** el texto va en `strings.xml` y el inglés es otro archivo («string»); rojo y verde reservados para las mesas; Atrás tras crear el PIN sale de la app; al rehacer la pantalla se pierde la pantalla, no la libreta, y una respuesta pedida por la pantalla se perdería; 1b sin Atrás porque «no hay nada a lo que volver»; dónde queda el PIN («sal y hash» en el archivo `pin`); la sacudida «se entiende como que está mal»; salir del Panel no pide PIN y salir de Pedir sí «porque está el cliente»; actual mal → «no pasa la primera», queda el PIN viejo.
- **A medias:** `dp`/`sp` («uno cambia y otro no»: falta que `sp` sigue el tamaño de letra del usuario); quién comprueba el PIN (bien: el encargado) y quién decide adónde ir (dijo «la libreta»: es la Activity que recoge el sobre).
- **No entendí todavía:** por qué 1a y 1b son Fragments de una Activity (explicado en el chat); por qué la pantalla no habla con el repositorio (explicado); por qué «¿Salir sin enviar?» no es una clase nueva («no sé»); y los repasos de la S4 (DAO, `suspend` frente a `withContext`, el actor, R6). **Todo va a los PDF de la S5** (regla nueva: los repasos van al PDF; en el chat solo si Daniel lo pide). Daniel no contestó a la lista de 12 conceptos del cierre (pidió terminar rápido).

## Para el vídeo

- **La pizarra del menú (RNF-19):** ningún texto está escrito en el código; las pantallas dicen la clave y Android busca el texto en `strings.xml`. Para el inglés, otra pizarra con las mismas claves.
- **El naranja con número (P138):** `#C75000` porque el blanco encima tiene contraste 4,6:1 (WCAG pide 4,5:1); el rojo y el verde ya significan mesa ocupada y cobrada.
- **La entrada del bar:** una Activity con dos Fragments que se turnan (la cerradura el primer día, las tres puertas cada día); comparten libreta y Atrás no vuelve a la cerradura.
- **La libreta que sobrevive (P140):** el ViewModel recibe al encargado por constructor y Android lo construye con una «receta» (fábrica). La pantalla es un camarero de paso: si pidiera ella, la respuesta se perdería al cambiarla.
- **Esperar sin congelar (P134, P142):** la pantalla espera con `lifecycleScope.launch`; el encargado pica el PIN «en la cocina» (`withContext`); el botón se apaga para que un doble toque no haga nada.
- **El portero que solo dice «correcto»:** `PinDialog` no decide adónde ir; deja un sobre y la pantalla que lo abrió decide (el Panel hoy, salir de Pedir en la S8).
- **La sacudida es accesibilidad (RNF-13):** dice «no» sin color ni texto.
- **Demuestra quién eres antes de cambiar nada (P46 B, OWASP):** al cambiar el PIN se comprueba primero el actual; con `else if` el orden del código es el orden de las reglas.
- **Un formulario en blanco para todos los «¿seguro?» (P84, P144):** `ConfirmacionDialog` con la ficha grapada (`arguments`), que sobrevive a que Android rehaga la pantalla.
- **Dos cerrojos:** `maxLength="4"` en la pantalla y `require` en el repositorio.
- **De borde a borde (P139):** desde Android 15 la app se dibuja bajo la hora; se aparta con el margen que dicen las barras del sistema.

## Pruebas

- Pruebas de código que pasan al terminar (1 oct, `testDebugUnitTest`): las 15 de `test/` (P-C-01 a P-C-05 y P-C-09). La S5 no añade pruebas de código.
- Pruebas manuales (apuntadas en `spec+doc-pruebas.md`): **P-M-01 Pasa**, **P-M-02 Pasa**, **P-M-03 Pasa** (repetida entera tras la pieza 12), **P-M-13 Parcial** (Pedir y Cuenta con caja provisional; entera en la S9), **P-M-29 Parcial** (arranque limpio y PIN; Panel, formulario, Cuenta y 1d en S6–S9; el paso `etiqueta` se quita por P115).

## Uso de IA en esta sesión

- Decisiones: P138, P139, P140, P141, P142, P143, P144 y P46 (vuelta a preguntar) se plantearon con 3 opciones (al menos una con fuente enlazada) y **eligió Daniel**; en P143 y P144 pidió antes ventajas y pegas. Los cambios de orden de piezas los propuso Claude y van marcados [Claude].
- Piezas 1–12: Claude dio cada bloque tras **compilarlo y probarlo en el emulador** dentro del proyecto (y deshacerlo); **Daniel creó cada archivo y carpeta y pegó el código en Android Studio**, ejecutó la app y las P-M; Claude comprobó leyendo el disco, compilando y, en las P-M, con capturas o archivos del emulador.
- Arreglos mecánicos de Claude: texto suelto en `themes.xml (night)` (problemas 1 y 2), mover `PanelActivity` al paquete bueno (problema 4) y quitar la copia vieja con un commit. Daniel arregló el problema 3 siguiendo las indicaciones.
- Documentación: Claude escribió ficha, decisiones, `estado-nivel.md`, plan de pruebas, lecciones y `como-trabajamos`; un subagente puso al día las guías S6–S13. Al cierre, Daniel pidió que Claude lo rellenara todo y enviara los PDF sin esperar su confirmación.

## Siguiente sesión

- S6 (Panel, 2a/2b/2e) en Plan Mode: el `PanelActivity` provisional se sustituye; `PanelViewModel` crece (recibirá también `cartaRepository`, P140); `ConfirmacionDialog` ya devuelve afirmativo/negativo (P144) para el aviso 2e. Pendientes del revisor de la S4: doble toque en *Cobrar*/*Anular* (S9) e id inexistente al eliminar (S6).
