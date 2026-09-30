> **Guía de la sesión 5 — Selector y PIN (1a, 1b, 1c, 1e) + `ConfirmacionDialog`.** Escrita en la sesión 00 (24 sep 2026) [Claude] como plan de piezas, sin código: el código lo da Claude pieza a pieza en el chat y Daniel lo teclea en Android Studio. Objetivo del spec (apartado 11, S5): **Selector y PIN (1a, 1b, 1c, 1e) + `ConfirmacionDialog`; P-M-01, 02, 03, 13 pasan.** Es la primera sesión con pantalla: se programa lo que dice la ficha 1 de `spec+doc-pantallas.md` y se dibuja lo que enseñan `01a-selector`, `01b-crear-pin`, `dialogo-1c-introducir-pin` y `dialogo-1e-cambiar-pin`.
> **Prerrequisitos** (de S1 a S4): proyecto con ViewBinding y el catálogo de versiones; las 7 tablas con precarga (P115); `Validacion.pinValido`; `SeguridadRepository`, `CartaRepository` (`hayPlatoVisible`) y `PinStore`; `Ako : Application` con los tres repositorios; `docs/textos-ui.md` con las cadenas de la pantalla 1 (apartado 1), las comunes (apartado 8) y los diálogos (apartado 7), y `docs/guias/strings-es-borrador.xml` con **las claves reales** (el inventario tiene alias: `app_nombre` → `app_name`, `panel_btn_cambiar_pin` → `pin_cambiar_titulo`).
> **Horas estimadas: 7–9 h** (12 piezas de 20–40 min más aperturas y cierre). Chat con **Opus 5.5, esfuerzo medio**; sin Plan Mode (P25).

# Sesión 5 — guía

## 0. Al abrir

`abrir-sesion` lee la ficha de la S4 (los hallazgos *Media/Baja* del `revisor` que quedaron en *Siguiente sesión* se hacen aquí si tocan al PIN; si no, esperan). Antes de la primera pieza, Daniel tiene abiertos los cuatro PNG de la pantalla 1 y la ficha 1. Lo que la S1 dejó de la plantilla (`MainActivity.kt`, `activity_main.xml`) se **borra en la pieza 3**: la app arranca en `SelectorActivity`.

**Lo que en esta sesión es provisional [Claude]** y se sustituye después: un `PanelActivity` mínimo con los botones *Cambiar PIN* y *Terminar* (para que P-M-03 pase entera; el Panel real es la S6), y una caja «Esta parte llega en una sesión posterior» detrás de *Pedir* (1d, S8) y *Cuenta* (6a, S9). Por eso **P-M-13 y P-M-29 se anotan como *Parcial*** con fecha (decisión P6) y se repiten enteras en la S9 (calendario de pruebas común de las guías).

## 1. Piezas

Regla 12 de `CLAUDE.md`: explicación breve → código completo con su ruta → Daniel lo teclea → `/verificar` → pregunta. Rutas de Kotlin bajo `app-ako/app/src/main/java/yunkang/ako/`; recursos bajo `app-ako/app/src/main/res/`. **Ningún texto visible se escribe en el código ni en el XML de diseño: todo sale de `strings.xml`** (RNF-19).

### Pieza 1 — `strings.xml` de la pantalla 1 (25 min)

- **Qué:** todas las cadenas que usa esta sesión, con las claves del borrador `docs/guias/strings-es-borrador.xml` (son las reales; el inventario tiene alias).
- **Archivo:** `res/values/strings.xml` — apartado 1 de `textos-ui.md` (`app_name`, `selector_subtitulo`, `selector_btn_propietario`, `selector_btn_pedir`, `selector_btn_cuenta`, `pin_crear_barra`, `pin_crear_titulo`, `pin_crear_subtitulo`, `pin_hint_pin`, `pin_hint_repite`, `pin_crear_aviso`, `pin_no_coinciden`, `pin_incorrecto`, `pin_introducir_titulo`, `pin_cambiar_titulo` (título de 1e y también texto del botón *Cambiar PIN* del Panel: una sola clave), `pin_hint_actual`, `pin_hint_nuevo`, `pin_hint_repite_nuevo`) y las comunes que hacen falta (`comun_aceptar`, `comun_cancelar`, `comun_atras`); más `panel_btn_terminar` (del apartado 2, se queda) y una provisional [Claude]: `pendiente_sesion_posterior` (no está en el borrador; se borra en la S10, cuando el botón *Resumen de ingresos* del Panel, que la usa desde la S6, deja de necesitarla).
- **Qué te explico antes:** qué es un recurso y por qué el texto no va en el código (idioma EN en la S13, RNF-19; el `revisor` lo comprueba); cómo se nombra una clave (`pantalla_elemento`); qué es `%1$d`/`%1$s` (aquí todavía no hace falta).
- **Qué comprobamos después:** compila; Android Studio no marca ninguna clave duplicada; los literales coinciden letra a letra con el inventario (Claude los compara).
- **Pregunta:** si en la S13 hay que poner la app en inglés, ¿qué archivos se tocan y cuáles no?

### Pieza 2 — Tema Material 3, colores y orientación (30 min)

- **Qué:** la paleta neutra con el naranja como único acento y la app en vertical.
- **Archivos:** `res/values/colors.xml` (naranja de acento, blanco, negro, gris oscuro; **sin rojo ni verde de acento**: ya significan mesa ocupada y mesa cobrada), `res/values/themes.xml` (`Theme.Ako` con padre `Theme.Material3.DayNight.NoActionBar` y `colorPrimary` naranja [Claude]) y `AndroidManifest.xml` (`android:screenOrientation="portrait"` en **cada** `<activity>`, RNF-15; `android:theme="@style/Theme.Ako"`).
- **Qué te explico antes:** qué es un tema (los colores y formas que heredan todos los botones sin repetirlos); qué es `colorPrimary` en Material 3 y qué botones lo cogen solos (los `MaterialButton` rellenos: *Pedir* en 1a, *Aceptar* en 1b); por qué la orientación va en el manifiesto y no en el código; qué es *DayNight* y por qué no nos preocupa (paleta neutra).
- **Qué comprobamos después:** la app arranca; girar el emulador (`Ctrl+F11`) **no** gira la pantalla; un botón relleno sale naranja.
- **Pregunta:** ¿por qué el rojo y el verde no pueden ser color de acento en esta app?

### Pieza 3 — `SelectorActivity` y su contenedor (30 min)

- **Qué:** la pantalla 1 como Activity vacía que decide qué vista enseñar.
- **Archivos:** `ui/selector/SelectorActivity.kt`, `res/layout/activity_selector.xml` (un `FragmentContainerView` a toda pantalla), `AndroidManifest.xml` (LAUNCHER pasa a `SelectorActivity`; se borran `MainActivity.kt` y `activity_main.xml`).
- **Qué te explico antes:** Activity = la pantalla; Fragment = un trozo de pantalla que se cambia sin cambiar de Activity (1a y 1b viven en la misma); ViewBinding (`ActivitySelectorBinding.inflate`: la clase que Android Studio genera desde el XML para no buscar vistas por `id`); `supportFragmentManager` y `replace`; **el flujo de la ficha 1:** si no hay PIN → 1b, si hay → 1a. En esta pieza solo el contenedor; la decisión se conecta en la pieza 5.
- **Qué comprobamos después:** compila; la app arranca en una pantalla en blanco sin error en Logcat; `MainActivity` ya no existe.
- **Pregunta:** ¿por qué 1a y 1b son dos Fragments de una Activity y no dos Activities?

### Pieza 4 — `SelectorViewModel` (30 min)

- **Qué:** la memoria de la pantalla 1 y su puente a los repositorios.
- **Archivo:** `ui/selector/SelectorViewModel.kt` — `class SelectorViewModel(app: Application) : AndroidViewModel(app)` [Claude] (así llega a `Ako` con `getApplication<Ako>()` sin *factory*); `hayPin: LiveData<Boolean>`, `fun crearPin(pin: String)`, `suspend fun comprobarPin(pin: String): Boolean` [Claude], `suspend fun hayPlatoVisible(): Boolean`. `mesas` (para 1d) se añade en la S8.
- **Qué te explico antes:** ViewModel = *«la memoria de la pantalla que sobrevive a que Android la destruya y la vuelva a crear»*; `LiveData` = un valor que la pantalla **observa** y que le avisa cuando cambia; `viewModelScope.launch` = *«lanza esto en otra cola y no bloquees la pantalla»*; **un ViewModel por Activity** (P108) que comparten sus Fragments (`activityViewModels()`); por qué el ViewModel no toca `PinStore` ni un DAO (spec 3); por qué `comprobarPin` es `suspend`: PBKDF2 con 100 000 iteraciones tarda décimas de segundo y no puede correr en el hilo de la pantalla.
- **Qué comprobamos después:** compila; el ViewModel importa `Ako` y los repositorios, ningún DAO.
- **Pregunta:** si Android destruye y recrea la Activity, ¿qué se pierde y qué no? ¿Dónde vive `hayPin`?

### Pieza 5 — `SelectorFragment` (1a) (35 min)

- **Qué:** el nombre *Ako*, el subtítulo y los tres botones grandes apilados.
- **Archivos:** `ui/selector/SelectorFragment.kt`, `res/layout/fragment_selector.xml` (`ConstraintLayout`; `app_name` grande, `selector_subtitulo`; tres `MaterialButton` a todo lo ancho, altura ≥ 48 dp y en el dibujo bastante más; *Pedir* relleno de acento, *Propietario* y *Cuenta* con borde, como el wireframe 01a); `SelectorActivity` decide en `onCreate`: `hayPin` → 1a, si no → 1b.
- **Qué te explico antes:** `ConstraintLayout` con palabras sencillas (cada vista se ata a otra o al borde); `dp` (tamaño físico) frente a `sp` (texto, crece con el ajuste del usuario: RNF-11); por qué los botones son grandes (P101: los pulsa gente que nunca ha visto la app); el editor de diseño de Android Studio: vista *Design* y vista *Code*; `viewLifecycleOwner` al observar desde un Fragment. Los tres `onClick` quedan vacíos hasta la pieza 12.
- **Qué comprobamos después:** en el emulador (con un PIN metido a mano en la pieza 7 o saltando la comprobación provisionalmente) se ve 1a como el wireframe; ningún texto por debajo de 12 sp.
- **Pregunta:** ¿qué diferencia hay entre `dp` y `sp`, y por qué el texto va siempre en `sp`?

### Pieza 6 — `CrearPinFragment` (1b), diseño (30 min)

- **Qué:** la pantalla del primer arranque, tal como el wireframe 01b.
- **Archivos:** `ui/selector/CrearPinFragment.kt` (solo inflar), `res/layout/fragment_crear_pin.xml` — barra con `pin_crear_barra` **sin flecha Atrás**; `pin_crear_titulo`, `pin_crear_subtitulo`; dos `TextInputLayout` con `TextInputEditText` (`inputType="numberPassword"`, `maxLength="4"`, hints `pin_hint_pin` y `pin_hint_repite`); caja gris con `pin_crear_aviso`; botón `comun_aceptar` grande, **desactivado** al empezar.
- **Qué te explico antes:** `inputType="numberPassword"` = teclado numérico y cifras ocultas en una sola línea; `maxLength` en XML frente a comprobar en código (las dos cosas: la interfaz no deja y `Validacion` no admite); por qué 1b no tiene Atrás (es obligatoria y no hay nada detrás: el Atrás del sistema cierra la app, que es lo correcto); `TextInputLayout` para enseñar el error debajo del campo.
- **Qué comprobamos después:** se ve 1b como el dibujo; el teclado que sale es numérico; el botón está gris.
- **Pregunta:** ¿por qué 1b no tiene botón Atrás ni Cancelar?

### Pieza 7 — `CrearPinFragment` (1b), comportamiento (35 min)

- **Qué:** Aceptar se enciende con 4 + 4 cifras; si no coinciden, aviso y campos vacíos; si coinciden, se guarda y se pasa a 1a.
- **Archivo:** `ui/selector/CrearPinFragment.kt` — `doAfterTextChanged` en los dos campos (core-ktx) → `botón.isEnabled = los dos tienen 4 cifras`; al pulsar: distintos → `pin_no_coinciden` como error del primer campo y vaciar los dos; iguales → `viewModel.crearPin(pin)` y `replace` por `SelectorFragment`.
- **Qué te explico antes:** qué es un *listener* (*«avísame cuando cambie el texto»*); dónde vive cada comprobación (coinciden → pantalla; 4 cifras → pantalla **y** `Validacion`; guardar → repositorio → `PinStore`); qué pasa en `crearPin` de punta a punta (sal nueva, hash, archivo privado).
- **Qué comprobamos después:** **P-M-01 pasa**: 1234/1235 → aviso y campos vacíos; 1234/1234 → 1a. Para repetirla: en el emulador, *Settings → Apps → All apps → Ako → Storage & cache → Clear storage → Delete* (o por consola `adb shell pm clear yunkang.ako`, o desinstalar), y se explica por qué eso borra el PIN.
- **Pregunta:** ¿dónde queda guardado el PIN 1234 después de aceptar, y qué hay exactamente en ese archivo?

### Pieza 8 — `PinDialog` (1c), diseño y resultado (35 min)

- **Qué:** el diálogo «Introduce el PIN» con un campo y Cancelar / Aceptar.
- **Archivos:** `ui/selector/PinDialog.kt` (`DialogFragment` sobre `MaterialAlertDialogBuilder`), `res/layout/dialog_pin.xml` (un `TextInputLayout` con `numberPassword`, `maxLength` 4); título `pin_introducir_titulo` (D8); el resultado sale con `setFragmentResult` bajo una clave (`PinDialog.CLAVE_RESULTADO`) [Claude] para que lo escuche la Activity que lo abrió (1a hoy; `PedidoActivity` al salir de Pedir en la S8).
- **Qué te explico antes:** qué es un `DialogFragment` y por qué no un `AlertDialog` suelto (sobrevive a que Android recree la pantalla); *Fragment Result*: *«el diálogo deja un sobre con la respuesta y quien lo abrió lo recoge»*; por qué el diálogo **no decide nada**: pregunta al ViewModel (`comprobarPin`) dentro de `lifecycleScope.launch` y devuelve solo «correcto»; Cancelar cierra y no deja sobre.
- **Qué comprobamos después:** desde 1a, *Propietario* abre el diálogo (conexión provisional); el teclado es numérico; Cancelar vuelve a 1a.
- **Pregunta:** ¿quién comprueba si el PIN es correcto: el diálogo, el ViewModel, el repositorio o `PinStore`? ¿Y quién decide qué pantalla abrir después?

### Pieza 9 — `PinDialog`, fallo con sacudida (30 min)

- **Qué:** con un PIN incorrecto, «PIN incorrecto», el campo se vacía, **se sacude** y el diálogo sigue abierto.
- **Archivo:** `ui/selector/PinDialog.kt` — `ObjectAnimator.ofFloat(campo, "translationX", 0f, 20f, -20f, 15f, -15f, 0f)` de unos 400 ms; el botón Aceptar se reengancha en `onStart` para que **no cierre** el diálogo (el de serie cierra siempre) [Claude].
- **Qué te explico antes:** qué es `translationX` (mover la vista sin cambiar su sitio en el diseño); por qué esta es *«una animación con función»* (spec 10: dice «no» sin depender del color, RNF-13) y las otras dos que hay en la app; la trampa del `AlertDialog`: el botón positivo cierra el diálogo salvo que se sustituya después de `show()`; intentos ilimitados = recorte declarado (spec 8).
- **Qué comprobamos después:** **P-M-02 pasa** menos el último paso: 9999 → aviso, campo vacío, sacudida, sigue abierto; 1234 → cierra (el Panel llega en la pieza 10).
- **Pregunta:** ¿por qué la sacudida cuenta como accesibilidad y no como adorno?

### Pieza 10 — `PanelActivity` provisional y `PanelViewModel` mínimo (25 min) [Claude]

- **Qué:** el sitio al que llega el Propietario, con solo lo que P-M-03 necesita.
- **Archivos:** `ui/panel/PanelActivity.kt`, `res/layout/activity_panel.xml` (barra con `comun_atras`, botones `pin_cambiar_titulo` y `panel_btn_terminar`), `ui/panel/PanelViewModel.kt` (`AndroidViewModel` con solo `suspend fun cambiarPin(actual, nuevo): Boolean`; el resto llega en la S6), manifiesto (portrait).
- **Qué te explico antes:** qué es un *placeholder* y por qué se declara (la ficha de la S5 lo dice; se sustituye en la S6, no se «arregla»); *Terminar* y *Atrás* hacen lo mismo aquí: `finish()` y vuelta a 1a **sin PIN** (ficha 1: volver desde Propietario no pide PIN); un segundo ViewModel porque es otra Activity (P108).
- **Qué comprobamos después:** *Propietario* → 1234 → se abre el Panel provisional; *Terminar* y el Atrás del sistema vuelven a 1a. **P-M-02 entera.**
- **Pregunta:** ¿por qué salir del Panel no pide el PIN y salir de Pedir sí?

### Pieza 11 — `CambiarPinDialog` (1e) (40 min)

- **Qué:** tres campos a la vez (actual, nuevo, repite), como el wireframe 1e (D18).
- **Archivos:** `ui/panel/CambiarPinDialog.kt`, `res/layout/dialog_cambiar_pin.xml` (tres `TextInputLayout` con `numberPassword` y hints `pin_hint_actual`, `pin_hint_nuevo`, `pin_hint_repite_nuevo`; título `pin_cambiar_titulo`); Aceptar apagado hasta 4 + 4 + 4 cifras; al aceptar: nuevo ≠ repite → `pin_no_coinciden` y se vacían los dos nuevos; si coinciden → `viewModel.cambiarPin` y, si devuelve `false`, `pin_incorrecto` en el campo actual **sin tocar los nuevos** y el diálogo sigue abierto; si `true`, cierra.
- **Qué te explico antes:** el orden de la ficha («primero el actual; si es incorrecto, no se llega a pedir el nuevo») se cumple **en el repositorio** (S4, `cambiarPin` mira el actual antes de nada), no en la pantalla; qué se reutiliza de `PinDialog` (misma trampa del botón, misma sacudida en el campo del actual [Claude], mismo `numberPassword`) y por qué aun así son dos clases (un campo frente a tres, y viven en paquetes distintos: `PinDialog` lo usará Pedir).
- **Qué comprobamos después:** **P-M-03 pasa entera** (paso 2: actual 0000 **y el nuevo 5678 tecleado dos veces**, porque Aceptar está apagado hasta 4 + 4 + 4 cifras, D18; como lo escribe `juego-de-datos.md` → «PIN incorrecto» en el actual y el nuevo no se guarda; 1234 → 5678 dos veces → después 1234 falla y 5678 abre; volver a 1234).
- **Pregunta:** si el PIN actual está mal y el nuevo bien, ¿qué queda guardado? ¿Y quién lo decide?

### Pieza 12 — `ConfirmacionDialog` y los tres botones conectados (40 min)

- **Qué:** la única caja de confirmación de toda la app y las salidas provisionales de *Pedir* y *Cuenta*.
- **Archivos:** `ui/comun/ConfirmacionDialog.kt` (`DialogFragment`; `companion fun nueva(titulo: String, texto: String, afirmativo: String, negativo: String? = null, clave: String)`; resultado con `setFragmentResult` [Claude]; **el botón negativo es opcional**, P15); `SelectorFragment.kt` — *Propietario* → `PinDialog` → `PanelActivity`; *Pedir* y *Cuenta* → `ConfirmacionDialog` con `pendiente_sesion_posterior` y solo `comun_aceptar` (en la S8 *Pedir* pasa a `hayPlatoVisible` + 1d; en la S9 *Cuenta* pasa a 6a).
- **Qué te explico antes:** por qué **una sola** clase para Enviar, Cobrar, Anular, R7, la cadena 3e, «Se perderán los cambios», RF-38 y la puerta de Pedir (P84, P121: se reutiliza el componente, no la pantalla; y **los botones dicen lo que hacen**, P40: «Cobrar», «Eliminar», no «Sí»); los textos entran por `arguments` (un `Bundle`), nunca por constructor: si Android recrea el diálogo, el constructor no se vuelve a llamar y los argumentos sí se conservan; qué significa «solo botón afirmativo» (la puerta de Pedir solo informa).
- **Qué comprobamos después:** los tres botones hacen algo; la caja provisional sale con un solo botón y cierra; **P-M-13 Parcial** (Propietario → 1c → cancelar vuelve a 1a; Cuenta y Pedir enseñan la caja provisional en vez de 6a/1d).
- **Pregunta:** ¿por qué el aviso de platos sin enviar (RF-38) no es una clase nueva?

## 2. Pruebas que cierran la sesión

- **P-M-01, P-M-02, P-M-03: pasan enteras** (piezas 7, 10 y 11). Se anotan con fecha en `spec+doc-pruebas.md`.
- **P-M-13: Parcial** (P6): Propietario → 1c → cancelar sí; Cuenta y Pedir llevan a la caja provisional. Se repite entera en la S9.
- **P-M-29: Parcial** (P6): ~~solo el paso 5 (tabla `etiqueta` con 3 filas en el inspector)~~ (P115: sin tabla `etiqueta`, el paso 5 de P-M-29 se quita [Claude]) y que la app arranca con el PIN creado; Panel, formulario, Cuenta y 1d se comprueban en S6–S9. Se repite entera en la S9.
- `estado-nivel.md`: **RF-01, RF-02, RF-03 → implementado (S5)**; **RF-24 → implementado, no probado** (hasta P-M-13 entera).
- Cierre con `cerrar-sesion` (sin `revisor`: toca en S9): ficha `sesion-05.md`, `decisiones-code.md` (los provisionales [Claude] y `AndroidViewModel`), dos commits (`S5: selector y PIN` y `S5: ficha del diario`) y push.

## 3. Lo que tienes que saber defender

- **El flujo de la ficha 1:** primer arranque → 1b obligatoria y sin Atrás; después siempre 1a; Propietario pide el PIN, Cuenta no, y salir de Pedir lo pedirá (S8). Y por qué el Resumen de ingresos vivirá detrás del PIN (RNF-10).
- **Activity, Fragment, ViewModel y LiveData con tus palabras**: la pantalla, el trozo de pantalla, la memoria que sobrevive, el valor que avisa. Y que el ViewModel no toca ningún DAO.
- **Por qué un solo `ConfirmacionDialog`** con botón negativo opcional (P84, P15, P121) y por qué sus botones dicen lo que hacen (P40).
- **La sacudida es una animación con función** (spec 10, RNF-13): comunica el fallo sin depender del color; intentos ilimitados es un recorte declarado.
- **De dónde sale cada texto** (`strings.xml`, RNF-19) y qué hace falta para el inglés en la S13.

## 4. Riesgos típicos y qué hacer

- **`LiveData` observado con el dueño equivocado:** en un Fragment se observa con `viewLifecycleOwner`, nunca con `this`; si no, al cambiar de Fragment el observador viejo sigue vivo y pinta dos veces.
- **Diálogo que se recrea** (Android destruye la Activity en segundo plano): un `DialogFragment` con datos en el constructor vuelve vacío; los datos van en `arguments`. Y el resultado por `setFragmentResult`, no por una referencia al Fragment que lo abrió.
- **Corrutina en el hilo principal:** `comprobarPin` con PBKDF2 congela la pantalla si no va en `suspend` desde `lifecycleScope`/`viewModelScope`; el síntoma es un parpadeo de medio segundo al pulsar Aceptar o el aviso *«Skipped frames»* en Logcat.
- **El `AlertDialog` cierra al pulsar Aceptar** aunque el PIN esté mal: se sustituye el `onClick` del botón positivo después de `show()` (piezas 9 y 11).
- **`SelectorActivity` decide 1a/1b con un valor todavía no cargado:** si `hayPin` llega por `LiveData`, la decisión va en el observador, no en `onCreate` a pelo; y se comprueba `savedInstanceState == null` para no apilar un Fragment encima de otro al recrear.
