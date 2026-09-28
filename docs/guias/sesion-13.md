> **Guía de la sesión 13 — Cierre del nivel 1.** Escrita en la sesión 00 (noche del 24 al 25 sep 2026) [Claude] como plan de piezas, sin código: el código lo da Claude pieza a pieza en el chat (bloque arriba, explicación debajo, pregunta al final) y Daniel lo teclea en Android Studio. Objetivo del spec (apartado 11, S13): **Cierre del nivel 1: tema (paleta, naranja), `strings.xml` EN, Accessibility Scanner en las siete pantallas, pasada por las 29 manuales que faltaran, `estado-nivel.md` con todo el nivel 1 en *implementado*, etiqueta `v1-nivel1` en Git; APK debug generada en Android Studio y adjuntada a mano por Daniel a una Release de GitHub con esa etiqueta (P225, P248).** Entregable: **las 29 P-M y las 9 P-C anotadas en `spec+doc-pruebas.md` con fecha; la Release `v1-nivel1` con la APK se ve en GitHub.** Aquí termina el nivel 1. Manda el spec en sus apartados 10 (estilo visual y accesibilidad), 11 (S13 y *qué pasa después de la S13*), 13 (diario, estado y Git) y 15 (entregables de la memoria); RNF-11, 12, 13, 17 y 19; `docs/textos-ui.md` (c) (los `contentDescription`); y `docs/guias/juego-de-datos.md`, apartado 4 (la pasada final).
> **Prerrequisitos** (de S1 a S12): la app entera del nivel 1 sin nada provisional (el último provisional se retiró en la S12); `res/values/strings.xml` en español completo (sin `pendiente_sesion_posterior`, borrada en la S10); el tema de la S5 (`Theme.Ako` sobre Material 3, `colorPrimary` naranja); las 29 P-M pasadas al menos una vez según el calendario de la sesión 00 (P-M-08 en la S12; P-M-04 y 18 enteras en la S12; las demás en S5–S10); las seis P-C de `test/` en verde y P-C-06, 07 y 08 en verde o con la red de seguridad P117 aplicada (S11); la foto grande de P-M-08 en `/sdcard/Download/` del emulador (S12); los hallazgos *Media/Baja* del `revisor` de la S9 que quedaran en *Siguiente sesión*. **El emulador está en inglés** (comprobado el 24 sep, `juego-de-datos.md` apartado 1), salvo que la duda del idioma de la S10 lo cambiara.
> **Horas estimadas: 6–8 h** (10 piezas de 20–40 min; las piezas 9 y 10 son la pasada final, 2 h 30 min – 3 h según `juego-de-datos.md`; más apertura y cierre). **Pasa de 6 h: se corta con `relevo` al terminar la pieza 7** (app pulida, en inglés, revisada por el Scanner y el `revisor`); **el segundo día, piezas 8–12 de un tirón** (P-C, instalación limpia, pasada final, papeles y Release), porque la pasada vive en los datos del emulador. Chat con **Opus 5.5, esfuerzo alto, o Fable 5.1 por ser hito** (P32); sin Plan Mode (P25).

> **Decidido por Daniel el 25 sep:** **P98 → B**, sin Accessibility Scanner (comprobaciones de Android Studio, lint y revisión a mano; la memoria lo declara), y **P101: un botón para elegir modo día o modo noche** (tema claro y oscuro; funcionalidad nueva). Las piezas del Scanner y del tema se rehacen en el Plan Mode de la S13.

# Sesión 13 — guía

## 0. Al abrir

`abrir-sesion` lee la ficha de la S12 y Claude repasa, y lo dice en cinco líneas: **qué hallazgos *Media/Baja* del `revisor`** siguen abiertos en las fichas S9–S12 (los de accesibilidad y textos se hacen en las piezas 1–2; el resto va a la pieza 7); **qué P-M no han pasado nunca enteras** en `spec+doc-pruebas.md` (según el calendario, ninguna: si falta alguna, se dice ya); **cómo quedaron P-C-06, 07 y 08** en la S11; **en qué idioma está el emulador** (*Settings → System → Languages*); y **qué contestó Daniel** a las dudas de esta guía (Accessibility Scanner, idioma por defecto, precarga en inglés, tema claro).

**El orden de la sesión y por qué [Claude]:** primero se pule (tema, accesibilidad, inglés), después miran los de fuera (Accessibility Scanner y `revisor`), y **solo al final** la pasada de las 29 P-M desde instalación limpia. Así cada arreglo queda probado por la pasada final; al revés, un arreglo después de la pasada obligaría a repetir pruebas.

**Una trampa del inglés que hay que tener presente desde el principio [Claude].** Hasta hoy la app solo tenía `values/strings.xml` (español), así que salía en español aunque el emulador esté en inglés. **En cuanto exista `values-en/`, en un emulador en inglés la app sale en inglés.** Y hay algo más: la `Precarga` copia *Otros*, *Bebidas*, *Agua*, los 14 alérgenos y las 3 etiquetas **desde `strings.xml` a la base de datos**, en el idioma que tenga el móvil **al instalar**; cambiar el idioma después no los cambia. Por eso: (1) la duda de la precarga en inglés (recomendado no traducirla); (2) **antes de la instalación limpia de la pieza 8, el emulador se pone en español** y se queda así para la pasada final (sus textos esperados están en español).

**Las cuatro dudas de esta guía** (para Daniel; lo recomendado es lo que siguen las piezas):

1. **Accessibility Scanner sin Play Store** (piezas 5–6). A) Un segundo emulador Pixel 6 API 34 con imagen *Google Play* (descarga de 1,5–2 GB; Daniel inicia sesión en Play Store) · B) sin Scanner: el análisis de accesibilidad del editor de diseño de Android Studio, lint y revisión a mano, y la memoria lo dice · C) instalar el Scanner desde una web de terceros: **descartada** (fuente no fiable). **Recomendada: A**, con B si A falla: es lo que pide el spec y da capturas que defender.
2. **Idioma por defecto** (pieza 3). El spec (2) dice «ES (por defecto) y EN»; RNF-19 dice «con caída a inglés para cualquier otro idioma». A) `values/` español y `values-en/` inglés: un móvil en francés ve español; se corrige RNF-19 en el Project · B) `values/` inglés y `values-es/` español: cumple RNF-19, pero cambia de sitio el archivo de doce sesiones y un emulador en inglés lo vería todo en inglés, precarga incluida. **Recomendada: A** (manda el spec y no rompe nada de lo probado).
3. **La precarga en inglés** (piezas 3 y 8). A) No se traduce: sus claves llevan `translatable="false"` y la base de datos nace siempre en español, como el resto de la carta (spec 5.1) · B) Se traduce: una instalación en inglés crea *Other*, *Beverages*, *Water*… como datos, y los textos esperados de las P-M dejan de valer · C) Enseñar los alérgenos desde `strings.xml` por su número en vez de desde la tabla (más código). **Recomendada: A**, y el Anexo II dice que la carta está en español.
4. **Tema claro fijo** (pieza 1). La S5 usó `Theme.Material3.DayNight`: con el móvil en modo oscuro la app cambia de colores, y nadie lo ha probado. A) Fijar el tema claro (`Theme.Material3.Light.NoActionBar`) · B) Dejar *DayNight* y pasar el Scanner en los dos modos. **Recomendada: A** (una sola paleta que probar; el diseño solo define una).

**Lo provisional de esta sesión:** nada. Si se usa un segundo emulador para el Scanner (duda 1), se queda creado pero Ako se prueba en `Pixel_6_API_34`.

## 1. Piezas

Regla 12 de `CLAUDE.md`: explicación breve → código completo con su ruta → Daniel lo teclea → `/verificar` → pregunta. Rutas de Kotlin bajo `app-ako/app/src/main/java/yunkang/ako/`; recursos bajo `app-ako/app/src/main/res/`. En esta sesión casi todo son recursos (`colors.xml`, `themes.xml`, `strings.xml`, layouts): Claude busca en todo el proyecto y da la lista de cambios; Daniel los hace.

### Pieza 1 — Tema final: paleta neutra y un solo naranja (30 min)

- **Qué:** repasar el tema de la S5 con la app entera delante.
- **Archivos:** `res/values/colors.xml`, `res/values/themes.xml` (y `res/values-night/themes.xml` si existe: duda del tema claro); los layouts de los botones de acción si alguno no toma el color del tema.
- **Qué te explico antes:** spec 10 y RNF-17: **paleta neutra** (blanco, negro, gris oscuro) y **naranja como único acento** en los botones de acción (*Guardar*, *Enviar*, *Añadir*, *Cobrar*, *Dar la cuenta*, *Anular*); **nunca rojo ni verde de acento**, porque ya significan *mesa ocupada* y *mesa cobrada* (el rojo de la rejilla se queda: es un estado, y lleva el total escrito, RNF-13); **el contraste del texto del botón** (≥ 4,5:1): el blanco sobre un naranja claro no llega (un naranja vivo da 2–3:1); o se oscurece el naranja o el texto va en negro [Claude: Claude calcula el contraste del par real y propone el tono]; transiciones de serie y una sola animación del nivel 1 (la sacudida del PIN): Claude comprueba que no se declaró ninguna otra.
- **Qué comprobamos después:** capturas (botón *Take screenshot* del emulador, `android-studio-basico.md` apartado 6) de 1a, 3a, 5c y 6c: todos los botones de acción en el mismo naranja; ninguno rojo o verde; Claude da la cifra de contraste.
- **Pregunta:** ¿qué contraste necesita el texto de un botón y cómo sabemos que el nuestro lo cumple?

### Pieza 2 — Accesibilidad en el código: 12 sp, 48 dp y `contentDescription` (40 min)

- **Qué:** que el Scanner de la pieza 6 encuentre poco, porque lo de siempre ya está mirado.
- **Archivos:** los layouts de `res/layout/` y los adaptadores que ponen descripciones por código (`FilaPlatoAdapter`, `CategoriaAdapter`, `MesaAdapter`); `strings.xml` si falta alguna cadena de descripción (tabla (c) de `textos-ui.md`: `comun_sin_foto_cd`, `plato_elegir_foto_cd`, `comun_menos_cd`, `comun_mas_cd`, `categoria_titulo_nueva` y `categoria_titulo_editar` como descripción del `+` y del lápiz, `comun_mesa` + `comun_precio` en cada mesa).
- **Qué te explico antes:** las tres reglas de RNF-11 dichas otra vez: **ningún texto por debajo de 12 sp** y todos en `sp` (crecen con el ajuste de letra del usuario); **todo lo que se toca mide al menos 48 × 48 dp** (la yema de un dedo; el dibujo puede ser más pequeño si el área de toque llega); **toda imagen o botón sin texto lleva `contentDescription`** (lo que lee en voz alta el lector de pantalla). Cómo lo busca Claude: una pasada por los layouts y el informe de **lint** (`./gradlew.bat lintDebug`, viene con Android Studio: avisa de textos pequeños, descripciones que faltan y textos escritos a mano). Una prueba extra [Claude]: poner la letra del emulador al máximo (*Settings → Display → Font size*) y mirar que nada importante se corte en 1a, 5a, 6a y 6c.
- **Qué comprobamos después:** lint sin avisos de tamaño de letra, de descripción ni de texto escrito a mano; con la letra al máximo, los botones siguen legibles.
- **Pregunta:** ¿qué lee un lector de pantalla en la miniatura de Pollo asado, y en la de Entrecot?

### Pieza 3 — `values-en/strings.xml` (35 min)

- **Qué:** la app entera en inglés (RNF-19).
- **Archivo:** `res/values-en/strings.xml` — **todas** las claves de `values/strings.xml` en inglés, `<plurals>` con sus formas `one` y `other`; se crea con clic derecho en `res` → *New → Android Resource File* → calificador *Locale* → `en` (**comprobar en pantalla**), o desde el *Translations Editor* (abrir `strings.xml` → *Open editor*). Las claves que no se traducen llevan `translatable="false"` en `values/` [Claude]: `app_name`, los símbolos (`comun_menos`, `comun_mas`) y, si Daniel acepta la duda, las de la precarga.
- **Qué te explico antes:** **los calificadores**: `values/` es *«lo que se usa si no hay nada mejor»* y `values-en/` *«lo que se usa si el móvil está en inglés»*; Android elige solo, sin código; por eso todo texto tenía que estar en `strings.xml` desde la S5 (el `revisor` de la S9 lo vigiló). **Qué se traduce y qué no:** se traduce la interfaz; **no** se traducen los datos de la carta (nombres de platos y categorías, descripciones): `producto.nombre` es el nombre en español (spec 5.1) y las traducciones de la carta son el incremento 10. **El glosario [Claude]**, para que el vocabulario obligatorio no se pierda en inglés: Propietario *Owner* · Pedir *Order* · Cuenta *Bill* · comanda *order* · recibo *Receipt* · la carta *Menu* · eliminar / recuperar *Remove / Restore* (nunca *Delete*: no se borra nada) · plato *dish* · mesa *Table* · Resumen de ingresos *Income summary* · *Eliminado* → *Removed*. Claude da el archivo entero; Daniel lo lee y cambia lo que quiera.
- **Qué comprobamos después:** compila; lint sin «not translated» ni «extra translation» (`MissingTranslation`, `ExtraTranslation`).
- **Pregunta:** si pongo el móvil en francés, ¿en qué idioma sale la app y por qué?

### Pieza 4 — Ver la app en inglés y volver al español (30 min)

- **Qué:** comprobar el inglés en el emulador y dejarlo en español para la pasada.
- **Archivo:** ninguno (salvo arreglos de textos largos).
- **Qué te explico antes:** cómo se cambia el idioma del emulador: *Settings → System → Languages → System languages* → poner arriba *English (United States)* o *Español (España)*, añadiéndolo si no está (**comprobar en pantalla**); la app se recarga con el idioma nuevo; por qué la fecha de 2g («Thursday») y el formato de los precios dependen del idioma del móvil y no de `strings.xml` (S10); qué se ve y qué no se traduce (los datos de la carta y lo que copió la precarga).
- **Qué comprobamos después:** en inglés: 1a, 1b (con *Clear storage* para verla), 2a, 2b, 3a, 5a, 5b, 5c, 6a, 6b, 6c y 2g **sin ni un texto de interfaz en español** y sin textos cortados por ser más largos; capturas para la memoria (RNF-19). **Al terminar, el emulador vuelve a *Español (España)*** y se queda así.
- **Pregunta:** ¿por qué los alérgenos de la lista de 3a siguen en español con el móvil en inglés?

### Pieza 5 — Accessibility Scanner: prepararlo (30 min)

- **Qué:** tener el Scanner funcionando sobre Ako (duda 1: el emulador `Pixel_6_API_34` es *Google APIs*, **sin Play Store**, y el Scanner se instala desde Play Store).
- **Archivo:** ninguno de la app.
- **Qué te explico antes:** qué es el **Accessibility Scanner** (una app de Google que mira la pantalla como la miraría un lector y apunta sugerencias: descripciones que faltan, objetivos táctiles pequeños, contraste bajo de textos e imágenes, elementos que se tocan sin decir qué son); es la prueba de RNF-11 a RNF-13 que pide el spec (10) y que irá a la memoria, **no** una garantía total. Si Daniel eligió la **opción A** de la duda: *Device Manager* → *Create Virtual Device* → Pixel 6 → imagen **API 34 con Google Play** (se descarga, 1,5–2 GB: es la excepción consentida al «no abrir SDK Manager» de `android-studio-basico.md` apartado 9) → arrancarlo → **Daniel** inicia sesión en Play Store con su cuenta (Claude no toca contraseñas) → instalar *Accessibility Scanner* (de Google LLC) → *Settings → Accessibility → Accessibility Scanner* → activar → Run ▶ de Ako eligiendo ese emulador, crear el PIN 1234 y unos datos mínimos (modo rápido de `juego-de-datos.md`, apartado 2.9, puntos 1–11) para que las pantallas tengan contenido. Si eligió la **B**: el análisis de accesibilidad del editor de diseño de Android Studio (la 2026.1.3 instalada trae dentro el *Accessibility Test Framework*: comprobado en sus archivos el 25 sep, no en pantalla; sale en el panel *Problems* al abrir un layout en vista *Design*, **comprobar en pantalla**), más lint y la revisión a mano de la pieza 2. Con cualquiera de las dos, **no se da por hecho que el Scanner llegue a instalarse**: si A falla (riesgos, apartado 4), se pasa a B sin insistir.
- **Qué comprobamos después:** con A, el botón flotante del Scanner (una marca azul) aparece encima de Ako; con B, el panel de problemas del editor enseña la categoría de accesibilidad de un layout.
- **Pregunta:** ¿qué puede encontrar el Scanner que no encuentre lint leyendo los archivos?

### Pieza 6 — Accessibility Scanner en las vistas del nivel 1 (40 min)

- **Qué:** pasar el Scanner por todas las vistas del nivel 1 y arreglar lo que diga.
- **Archivos:** los que salgan; las capturas del Scanner en `docs/capturas/accesibilidad/` [Claude] (carpeta nueva con su `LEEME.md`, como manda la skill de carpetas del proyecto).
- **Qué te explico antes:** el spec dice «las siete pantallas», pero el nivel 1 tiene **seis Activities y quince vistas**; se pasa por **las quince** [Claude]: 1a, 1b, 1c, 1d, 1e, 2a, 2b, 2g, 3a (arriba y abajo), 5a, 5b, 5c, 6a, 6b, 6c, y una vez por `ConfirmacionDialog`. En cada una: botón del Scanner → *Snapshot* → captura → leer las sugerencias → **arreglar** (texto < 12 sp, objetivo < 48 dp, falta descripción, contraste < 4,5:1) o **justificar** por escrito si no aplica (por ejemplo, un aviso sobre el texto de un dato del restaurante). **Con la opción B** no hay botón del Scanner: se recorren los layouts de esas vistas en la vista *Design* con el panel *Problems* abierto, más el informe de lint, y la captura es la del panel; la tabla de la ficha dice «editor de diseño y lint», no «Scanner». Cada arreglo es una pieza mini con `/verificar`.
- **Qué comprobamos después:** ninguna sugerencia de *arreglar* pendiente; en la ficha, una tabla vista → sugerencias → qué se hizo, que es la evidencia de RNF-11 a 13 para la memoria.
- **Pregunta:** si el Scanner dice que la palabra *Eliminado* atenuada tiene poco contraste, ¿se arregla o se justifica, y por qué?

### Pieza 7 — La revisión del `revisor` (30 min + revisión)

- **Qué:** la tercera revisión independiente (P35), **antes** de la pasada final.
- **Archivos:** los que señale el `revisor` sobre `app-ako/app/src` (y sus pruebas en `test/` y `androidTest/`).
- **Qué te explico antes:** qué mira (reglas R1–R16 en su sitio, capas, vocabulario, textos fuera del código, pruebas no debilitadas, librerías fuera del spec, accesibilidad base) y por qué va **antes** de la pasada: lo que arregle queda probado en ella; los hallazgos **Alta** se arreglan ahora; los **Media/Baja** que no dé tiempo van a la lista de la **revisión del prototipo** (lo primero después de la S13), no a una sesión siguiente, porque no la hay.
- **Qué comprobamos después:** `assembleDebug` y `testDebugUnitTest` en verde; ningún *Alta* abierto. **Aquí se corta si la sesión va a durar dos días** (`relevo`).
- **Pregunta:** de lo que ha encontrado el `revisor`, ¿qué hallazgo explicarías en el vídeo y por qué?

### Pieza 8 — Las nueve P-C y la instalación limpia (25 min)

- **Qué:** las pruebas de código y el punto de partida de la pasada final.
- **Archivo:** ninguno.
- **Qué te explico antes:** por qué las P-C van **primero** (`juego-de-datos.md`, S13): `connectedDebugAndroidTest` suele desinstalar la app al terminar (lo que se viera en la S11), así que después de la pasada borraría los datos que se están mirando; si en la S11 se aplicó la red de seguridad P117, se lanza **una vez** más (10 min) y, si sigue sin arrancar, queda como estaba. **Instalación limpia** (`juego-de-datos.md`, apartado 1): *Clear storage* de Ako, o *Run ▶* si Gradle la desinstaló; el emulador en **español** (pieza 4) antes de abrirla, para que la precarga copie *Otros*, *Bebidas* y los alérgenos en español; la foto grande sigue en *Download* (instalar o limpiar la app no la toca).
- **Qué comprobamos después:** `testDebugUnitTest` (6) y `connectedDebugAndroidTest` (3) con *BUILD SUCCESSFUL*; la app abre en **1b** (la *Entrada* de P-M-01).
- **Pregunta:** ¿por qué la pasada final empieza desde cero y no con los datos que ya había?

### Pieza 9 — Pasada final, parte 1: bloques A, B y C (~1 h 15 min con las notas)

- **Qué:** P-M-01 → 29 → 02 → 03 → 05 → (eliminar Bebidas, P8) → 07 → 09 → (descripción) → 13 → (mesa 5) → 06 → 11 → (eliminar Postres) → (mesa 6) → 10 → 04 → 14, en el orden y con los pasos de `juego-de-datos.md`, apartados 2 y 4.
- **Archivo:** ninguno. Daniel apunta una línea por prueba (*Pasa/Falla* y lo raro que vea); Claude lo pasa al plan en la pieza 11.
- **Qué te explico antes:** qué significa esta pasada (**las 29 enteras, en una misma versión del código y el mismo día o dos**: es la prueba de que el nivel 1 funciona junto, no a trozos); qué se hace si una falla (`juego-de-datos.md`, apartado 4): se anota *Falla* con lo visto; si el arreglo toca código, al terminar se repite esa prueba y las que dependan de ella; **nunca se anota *Pasa* algo que no se ha visto**. Si se parte en dos días, se corta en 1a y sin carrito a medias.
- **Qué comprobamos después:** 13 pruebas con su línea; ninguna *Falla* sin anotar.
- **Pregunta:** ¿qué dato de los que acabas de crear necesitan las pruebas del bloque D?

### Pieza 10 — Pasada final, parte 2: bloques D, E y F (~1 h 30 min con las notas)

- **Qué:** P-M-17 → 18 → 19 → 20 → 21 → 16 → 22 → 23 → 24 → 25 → (anular mesa 6) → (mesa 4 · 42,00) → 15 → 26 → 27 → 28 → 12 → **08** → remate de 04 y 18 (paso 37).
- **Archivo:** ninguno.
- **Qué te explico antes:** el cuidado de la fecha en el bloque E (los pasos 30–35 el mismo día, sin cruzar la medianoche: P-M-12 cuenta los cobros de *hoy* y mira *ayer* vacío); por qué P-M-08 va la última (la foto es la última pieza, y P-M-04 y P-M-18 se anotan tras el remate).
- **Qué comprobamos después:** las 29 con su línea; ninguna *Falla* abierta.
- **Pregunta:** en P-M-12, ¿por qué el pie dice «2 comandas» si solo se ha cobrado la mesa 4?

### Pieza 11 — Los papeles: pruebas, estado del nivel y commits (30 min)

- **Qué:** dejarlo todo escrito y subido.
- **Archivos:** `docs/spec+doc-pruebas.md` (las 29 P-M y las 9 P-C con *Resultado*, *Observaciones* y *Fecha*: la fecha es la de la pasada final y *Observaciones* conserva el historial, «Parcial el … (S6); entera el … (S12); pasada final S13 el …»), `docs/estado-nivel.md` (los 29 RF del nivel 1 en `implementado`, con su sesión; recuento de arriba «29 implementados»), la ficha `sesion-13.md`, `decisiones-code.md` (lo lleva Claude en `cerrar-sesion`).
- **Qué te explico antes:** lo que sale de aquí para la memoria (spec 15): la tabla de pruebas con fecha (apartado 7), la tabla diseñado/implementado (apartados 5 y 7), las capturas del Scanner (RNF-11–13) y del inglés (RNF-19); los **dos commits** de siempre, `S13: cierre del nivel 1` y después `S13: ficha del diario`, con push (los hace Claude; el hook exige `S13:` y que no haya atribución).
- **Qué comprobamos después:** en GitHub se ven los dos commits; ninguna fila del nivel 1 en `estado-nivel.md` distinta de `implementado`.
- **Pregunta:** ¿qué fila de la memoria saldría mal si una P-M se hubiera anotado *Pasa* sin fecha?

### Pieza 12 — Etiqueta `v1-nivel1`, APK y Release de GitHub (35 min)

- **Qué:** marcar esta versión, generar la APK y publicarla (P225, P248).
- **Archivo:** ninguno del código. La APK en `app-ako/app/build/outputs/apk/debug/` y el texto de la Release, que da Claude.
- **Qué te explico antes:** **commit, etiqueta, Release y APK**: un commit es una foto del proyecto; una **etiqueta** es un nombre fijo pegado a una foto (`v1-nivel1`); una **Release** es la página de GitHub de esa etiqueta, donde se pueden adjuntar archivos; una **APK** es la app empaquetada para instalar en un móvil (la *debug* va firmada con la clave de pruebas de Android Studio, y para instalarla hay que permitir «apps de origen desconocido»: lo explicará el Anexo I). Los pasos: (1) **Claude** crea la etiqueta anotada `v1-nivel1` sobre el último commit (el de la ficha: así la Release lleva también el diario completo; el código es el mismo) y la sube con `git push origin v1-nivel1`; (2) **Daniel** genera la APK desde el menú **Build**, en el submenú de APK y *bundles*. Hay dos formas escritas de ese menú: **Build → Generate App Bundles or APKs → Generate APKs** (la que traen los archivos de configuración de menús del Android Studio 2026.1.3 instalado, leídos el 25 sep; no se ha visto en pantalla) y **Build → Generate Bundle(s) / APK(s) → Generate APK(s)** (la de la documentación web, que copia `android-studio-basico.md`, apartado 5); el spec (11) dice *Build → Build APK*, el nombre de versiones antiguas. **Comprobar en pantalla** cuál sale (y otra vez si Android Studio se actualizó por la duda de AGP de la S1); al terminar, el aviso trae el enlace a la carpeta; **no vale la APK de Run ▶**, que va marcada como «solo pruebas». Alternativa por consola (Claude): `./gradlew.bat assembleDebug`, misma carpeta. Se renombra `app-debug.apk` a `ako-v1-nivel1-debug.apk` [Claude]; (3) **comprobación [Claude]**: arrastrar esa APK a un emulador sin Ako (o tras desinstalarla) y ver que se instala y arranca en 1b: es lo que hará quien siga el Anexo I; (4) **Daniel**, en la web de GitHub: repositorio `ako` → *Releases* → *Draft a new release* → etiqueta `v1-nivel1` → título y descripción (el texto de Claude) → arrastrar la APK a la zona de adjuntos → *Publish release*. Publicar lo hace Daniel (P225): es su cuenta y su entrega.
- **Qué comprobamos después:** en GitHub, *Releases* enseña `v1-nivel1` con `ako-v1-nivel1-debug.apk` adjunta; la etiqueta apunta al último commit.
- **Pregunta:** ¿qué diferencia hay entre la etiqueta `v1-nivel1` y la Release `v1-nivel1`?

## 2. Pruebas que cierran la sesión

- **Las 29 P-M: Pasa**, todas con la fecha de la pasada final de la S13 e historial en *Observaciones* (pieza 11). Si alguna falla y no se puede arreglar hoy, la sesión **no se cierra** (`relevo`): el nivel 1 no está terminado.
- **Las 9 P-C** con fecha: las seis de `test/` **Pasa**; P-C-06, 07 y 08 **Pasa**, o *No ejecutada* con la red de seguridad P117 y la frase de la S11 (con la fecha del último intento).
- **Accesibilidad (RNF-11 a 13):** la tabla de la pieza 6 y las capturas en `docs/capturas/accesibilidad/`; si se usó la opción B de la duda, dicho así en la ficha.
- `estado-nivel.md`: **los 29 RF del nivel 1 en `implementado`**; recuento «29 implementados»; el nivel 2 y el 3 siguen en `diseñado`.
- **Revisión `revisor`** (P35): pieza 7.
- Cierre con `cerrar-sesion`: ficha `sesion-13.md`; `decisiones-code.md` con las decisiones de la sesión (tono del naranja y su contraste, tema claro o no, glosario inglés, precarga sin traducir, idioma por defecto, cómo se pasó el Scanner, carpeta de capturas, etiqueta sobre el commit de la ficha, nombre de la APK); **dos commits** (`S13: cierre del nivel 1` y `S13: ficha del diario`), push, etiqueta y Release (pieza 12). Recordar a Daniel subir la ficha al Project. **El primer mensaje del chat siguiente** es el de la revisión del prototipo (abajo), con **Opus 5.5, esfuerzo medio**.

### Lo que viene después de la S13 (spec 11, *qué pasa después de la S13*; P233)

1. **Revisión del prototipo con Daniel** (fila 43 del registro): se repasa lo hecho, con la lista de hallazgos *Media/Baja* del `revisor` que quedaran, y se deciden dos preguntas aplazadas: **P227 · `CLAUDE.md` público** (A: se queda en el repositorio, recomendado · B: se borra al final, sabiendo que sigue en el historial de Git) y **P228 · marcas `[Claude]`** (A: se mantienen hasta la pasada de apropiación, recomendado · B: se quitan al terminar el prototipo).
2. **Se completa la memoria (fase 7)** con lo que sale del código (P4): el diagrama de clases definitivo desde el código real, la estructura de carpetas, la tabla de pruebas, las fichas del diario y `estado-nivel.md`; el **Anexo I** instala la APK de esta Release; el **Anexo II** avisa de que olvidar el PIN obliga a reinstalar y se pierde todo, y de que la interfaz solo está en español e inglés.
3. **Pasada de apropiación (P230 → D):** Claude hace la lista de las partes que Daniel no sabría explicar (marcas `[Claude]`, lo más avanzado, lo que el diario dice que no entendió); Daniel elige; en cada una **Claude solo guía y Daniel teclea**; se repasan sus pruebas; commit; ficha «reescrito por Daniel». Coste aceptado: después se actualizan el apartado 7 de la memoria, la declaración de IA y las capturas del Anexo II.
4. **Solo entonces, y si queda tiempo, los incrementos del nivel 2** (fase 6b), en orden, cada uno con sus pruebas al final de las tablas, su redibujo y **su Release `v1-inc<N>` con APK**. **P3:** el incremento 1 solo entra si el nivel 1 está cerrado el **31 de octubre**; si no, todo el nivel 2 se declara *diseñado, no implementado*. Después, la memoria al día y el vídeo (fase 8).

Fechas que mandan (P2, P26): **hito interno del nivel 1, 8 de noviembre** · el crédito de la nube caduca el **5 de noviembre** · entrega, **semana del 23 de noviembre**.

## 3. Lo que tienes que saber defender

- **RNF-11 a RNF-13 con pruebas**: las capturas del Scanner (o del editor de diseño, si fue la opción B), qué sugirió y qué se arregló; y que ningún estado se dice solo con color (*Eliminado*, *Categoría eliminada*, el total escrito en la mesa roja).
- **Por qué un solo naranja y nada de rojo ni verde de acento** (spec 10), y cómo se comprobó el contraste del texto de los botones.
- **Español e inglés**: cómo elige Android el idioma (`values/`, `values-en/`), por qué todo texto vive en `strings.xml` desde la S5, y el límite honesto: los datos de la carta y los de la precarga están en español (las traducciones de la carta son el incremento 10).
- **La pasada final desde instalación limpia**: las 29 P-M enteras, en una sola versión del código, con fecha; y las 9 P-C (o la red de seguridad P117 contada tal cual).
- **Etiqueta, Release y APK**: qué es cada cosa y que la APK de la Release `v1-nivel1` es la que instala el Anexo I (P225, P248).

## 4. Riesgos típicos y qué hacer

- **Tras la pieza 3 la app sale en inglés**, o en la pasada final Otros se llama *Other*: el emulador está en inglés. Se pone en español (pieza 4); si la instalación limpia ya se hizo en inglés, *Clear storage* y volver a empezar la pasada, ya en español.
- **`lintDebug` falla con «"xxx" is not translated in "en"»**: falta esa clave en `values-en/`; se añade o, si no se traduce (símbolos, `app_name`, precarga), `translatable="false"` en `values/`.
- **La descarga de la imagen con Google Play no termina, falta espacio o Play Store no deja iniciar sesión**: no se insiste más de 30 min; se aplica la opción B de la duda del Scanner y la ficha lo dice. **Nunca** se instala el Scanner desde una web de terceros.
- **Android Studio ofrece actualizarse o un *Upgrade Assistant*** al abrir *Device Manager*: no (`android-studio-basico.md`, apartado 9); solo se descarga la imagen del emulador.
- **Una P-M falla en la pasada**: se anota *Falla* con lo visto; si el arreglo es pequeño, se hace y se repiten esa prueba y las que dependan de ella (`juego-de-datos.md`, apartado 4); si es grande, la sesión no se cierra (`relevo`) y la ficha dice qué falta.
- **La etiqueta quedó en un commit equivocado**: si aún no se subió, Claude la quita en local y la vuelve a crear; si ya se subió, se avisa a Daniel antes de nada (nada de `--force`, prohibido en `.claude/settings.json`).
- **«App not installed» al probar la APK**: casi siempre es que se cogió la APK de Run ▶ (marcada «solo pruebas», `testOnly`), que Android no instala fuera de `adb`: se genera otra vez desde el menú de la pieza 12. La de Run ▶ y la del menú van firmadas con la misma clave de pruebas, así que la firma no es el problema; aun así, para la comprobación de la pieza 12 se desinstala Ako antes, que es lo que verá quien siga el Anexo I.
