# Sesión 11 — 2026-10-08

| Campo | Valor |
|---|---|
| **Fecha** | 2026-10-08 |
| **Sesión nº** | 11 |
| **Objetivo de la sesión** | Del spec: **Pruebas de Room en `androidTest/`** (P117, con red de seguridad); P-C-06, 07 y 08 pasan, o la ficha dice por qué no |
| **Tiempo dedicado** | 12:26 – 13:05, **unos 40 min** (sin pausas; Daniel: «esto es corto porque todas las pruebas y todo lo has hecho tú») |
| **Nivel / pieza** | Nivel 1 · pruebas de código de Room (`androidTest/`): P-C-06, 07 y 08 |
| **Commit final** | `d4ad522` — S11: pruebas de Room en androidTest (P-C-06, 07 y 08 pasan) |
| **Contexto al cerrar** | menos del 20 % (Daniel: «ni siquiera el 20»); toda la sesión en un solo chat |

## Qué se hizo

- Al abrir (12:26): Claude comprobó el código real (guía, apartado 0): `Precarga` partida en dos (`cargar(db)` en `withTransaction`), `ComandaRepositoryReal(db, mesaDao, comandaDao)` y `CartaRepository(categoriaDao, productoDao, precargadosDao, comandaRepository)` como dice la guía; `build.gradle.kts` ya tenía `testInstrumentationRunner` y `room-testing` en `androidTestImplementation` (**pieza 1: comprobada, sin cambios**). La prueba previa de P05 (Claude lanza `ArranqueTest` antes de la sesión) **no se hizo**: se hace dentro de la pieza 2.
- Pieza 2: Daniel creó `androidTest/java` (*New → Directory → Gradle Source Sets*); no la veía en la vista Android porque las carpetas de código vacías no salen: el paquete `yunkang/ako` lo creó Claude [Claude]. Daniel pegó `ArranqueTest.kt` y la lanzó con ▶ desde Android Studio: **verde a la primera** (la herramienta funciona; la app sigue instalada tras lanzarla desde Android Studio). A «¿por qué `androidTest/` necesita el emulador?»: «porque corre dentro de Android» (bien; Claude añadió que `test/` corre en el Java del PC). A «¿cómo sabrías si el rojo es de la herramienta o del código?»: «porque saldría el error de Android» (a medias; Claude: `ArranqueTest` no toca nada de Ako, así que un rojo ahí solo puede ser de la herramienta).
- Pieza 3: `Precarga` confirmada leyendo el archivo, sin tocarla. A «¿y si `onCreate` llamara a `cargar` sin corrutina?»: «la app se congelaría» (bien; Claude añadió que ni compilaría: `cargar` es `suspend`).
- Pieza 4 (P-C-08): antes del bloque, la base en memoria («una pizarra») y `@Before`/`@After` («mantel limpio antes de cada cliente»); a «¿el segundo `@Test` vería los cambios del primero?»: «no, cada test empieza con su pizarra limpia» (bien). Daniel creó el paquete `datos (androidTest)` y pegó `CategoriaPorDefectoTest.kt`: **verde**. A «¿por qué no en `test/` con un DAO falso?»: «porque necesita Room de verdad» (bien; Claude: prueba la precarga y la consulta SQL de `porDefecto()`, no un falso). **Rota a propósito por Daniel** (quitó `esPorDefecto = false` al crear en `guardarCategoria`): rojo «expected 1 but was 2», y lo dejó como estaba (comprobado con `git status`). Daniel: *«haz ese tipo de cosas tú»* → a `como-trabajamos`: desde ahora las roturas a propósito las hace Claude.
- Pieza 5 (P-C-06): R1 (la base no puede imponerla: haría falta un índice único parcial), R2 y R14; preparación con DAOs (Carnes antes que Entrecot) y la mesa 4 buscada por número. Daniel pegó `ComandaRepositoryTest.kt` (paquete equivocado, problema 1); tras moverlo, **verde** (Claude con `am instrument`). A «¿qué fallaría si cada envío creara una comanda?»: «`id1 == id2` y `contarLineas`» (bien; Claude añadió que la prueba se para en el primer `assert` que falla). **Rota a propósito por Claude:** 2 → 3 en `contarLineas`: «expected:<3> but was:<2>»; restaurada y otra vez OK.
- Pieza 6 (P-C-07): R7 y R3; `.first()` sobre el `Flow` («una foto del grifo»). Daniel sustituyó `ComandaRepositoryTest.kt` entero y lanzó las de la carpeta: verdes (Claude, con `am instrument`: **OK (4 tests)**, con `ArranqueTest`). A «¿la comanda sigue existiendo?»: «sigue existiendo, queda ANULADA» (bien: R7, se queda en el histórico).
- Pieza 7: Claude borró `ArranqueTest.kt` (`git rm`) y lanzó las dos órdenes: `testDebugUnitTest` **BUILD SUCCESSFUL, 21 pruebas, 0 fallos**; `connectedDebugAndroidTest` **BUILD SUCCESSFUL, 3 pruebas, 0 fallos** (informe en `app-ako/app/build/reports/androidTests/connected/debug/index.html`). **Se confirma que `connectedDebugAndroidTest` desinstala la app al terminar** (`pm list packages` vacío): se borraron el PIN, el juego de datos y las comandas del emulador; Claude la reinstaló con `installDebug` (sin PIN: la S12 empieza con instalación limpia, `juego-de-datos.md` 3).

## Problemas y soluciones

| # | Qué falló | Cómo se resolvió | Justificación (por qué esta solución y no otra) | Fuente |
|---|---|---|---|---|
| 1 | Pieza 5: `ComandaRepositoryTest.kt` quedó en `androidTest/java/yunkang/ako/repositorios/` con `package yunkang.ako.repositorios` (el paquete se creó como `repositorios` colgando de `yunkang.ako`, no de `datos`); Daniel vio verde, pero en ese paquete `ComandaRepositoryReal` no se resuelve sin `import`, así que lo que se lanzó no pudo ser esa clase | Claude lo vio en el `ls` tras el «ya está» (lección del 30 sep): `git mv` a `datos/repositorios/`, `package yunkang.ako.datos.repositorios`, y lanzada por Claude con `installDebug` + `installDebugAndroidTest` + `am instrument`: **OK (1 test)** | `git mv` y no mover a mano, para que no quede un `AD` en el commit (lección del 1 oct); `am instrument` en vez de `connectedDebugAndroidTest`, para no desinstalar la app | Propio |

## Qué entendí y qué no

- Daniel, al cerrar: *«en general todo igual; lo que me costó más es lo de crear lo de test y el porqué»*; y *«estas preguntas ya son irrelevantes cuando lo repasemos todo en la S14»* (a `como-trabajamos`). Lo que sigue sale de sus respuestas a las preguntas de cada pieza.
- **Entendí:** `androidTest/` corre dentro de Android y por eso necesita el emulador; llamar a `cargar` sin corrutina congelaría la app; cada `@Test` empieza con su pizarra limpia; P-C-08 necesita Room de verdad; si cada envío creara una comanda fallarían `id1 == id2` y `contarLineas`; tras quitar la última línea la comanda sigue existiendo, ANULADA; R1, R7 y R16 quedan cubiertas dos veces.
- **No entendí todavía:** crear la carpeta y los paquetes de `androidTest/` y **por qué** van ahí (lo que más le costó); por qué un rojo de `ArranqueTest` solo podría ser de la herramienta (respuesta a medias). A `pendientes-de-entender.md` y a los PDF.

## Para el vídeo

- **`test/` frente a `androidTest/`**: probar la receta en la cocina de casa (el PC, segundos, sin Android) o en el restaurante abierto (el emulador, con SQLite de verdad); spec 12, P237 (separar calcular de guardar).
- **La base de datos en memoria**: una pizarra que se borra al cerrar; cada `@Test` empieza con la suya y con la precarga hecha (`@Before`, «mantel limpio antes de cada cliente»).
- **Por qué `Precarga` está partida en dos**: si todo fuera en `onCreate`, llenaría la despensa del bar y no avisaría de cuándo acaba; con `cargar(db)` la prueba elige la despensa y espera (P16, P149).
- **R1 con doble garantía**: la base no puede imponer «una comanda pendiente por mesa» (no hay índice único parcial en Room); lo garantiza `enviarCarrito`, y lo prueban P-M-20 con el dedo y P-C-06 en código. Igual R7 (P-M-24 y P-C-07) y R16 (P-M-05 y P-C-08).
- **Romper la prueba a propósito**: una prueba que nunca has visto en rojo no sabes si prueba algo («expected 1 but was 2» en P-C-08; «expected:<3> but was:<2>» en P-C-06).
- **`.first()` sobre un `Flow`**: una foto del grifo; la rejilla se queda escuchando, la prueba solo necesita el momento (P167 A).
- **Room KMP descartado** (P124 del Project): probar en el PC exigiría rehacer el proyecto como multiplataforma.

## Pruebas

- Pruebas de código que pasan al terminar: las **once**: P-C-01 a 05, 09, 10 y 11 en `test/` (`testDebugUnitTest`, 21 pruebas, 0 fallos) y **P-C-06, 07 y 08** en `androidTest/` (`connectedDebugAndroidTest`, 3 pruebas, 0 fallos), con fecha 2026-10-08 en `spec+doc-pruebas.md`. **No se aplicó la red de seguridad P117.** R1, R7 y R16 tienen ya prueba manual (P-M-20, P-M-24, P-M-05) **y** de código.
- Pruebas manuales: ninguna (la S11 no tiene P-M). Ningún RF cambia en `estado-nivel.md`.

## Uso de IA en esta sesión

- Apertura y pieza 1: Claude leyó el código real y comprobó `build.gradle.kts` (sin cambios); lanzó el emulador.
- Pieza 2: Claude dio la explicación y `ArranqueTest.kt`; Daniel creó la carpeta `androidTest/java` (Claude creó el paquete vacío `yunkang/ako`, que no se veía), tecleó/pegó la prueba y la lanzó desde Android Studio.
- Pieza 3: Claude confirmó `Precarga` leyendo el archivo y lo explicó; Daniel no tecleó nada.
- Pieza 4: Claude generó `CategoriaPorDefectoTest.kt`; Daniel creó el paquete, la pegó, la lanzó y la rompió a propósito él mismo (y la restauró).
- Pieza 5: Claude generó `ComandaRepositoryTest.kt` (P-C-06); Daniel la pegó en un paquete equivocado; Claude la movió (`git mv`) y corrigió el `package`, la lanzó por consola y la rompió a propósito (a petición de Daniel).
- Pieza 6: Claude generó el archivo entero con P-C-07; Daniel lo sustituyó y lanzó las de la carpeta; Claude las relanzó por consola.
- Pieza 7: Claude borró `ArranqueTest.kt`, lanzó `testDebugUnitTest` y `connectedDebugAndroidTest`, leyó el informe y reinstaló la app. El cierre (ficha, plan de pruebas, decisiones, guías S12–S13 y juego de datos) lo escribió Claude.

## Siguiente sesión

- S12 (**Fotos**: `ImageStore`, selector de fotos, Glide en Panel, 5a y 5b; P-M-08 pasa): la app está instalada **sin PIN** (la desinstaló `connectedDebugAndroidTest`): se empieza con instalación limpia y el montaje mínimo de `juego-de-datos.md` (apartado 3). Antes de pruebas con horas, poner la hora de Madrid (`lecciones-claude.md`).
