> **Guía de la sesión 11 — Pruebas de Room en `androidTest/`.** Escrita en la sesión 00 (noche del 24 al 25 sep 2026) [Claude] como plan de piezas, sin código: el código lo da Claude pieza a pieza en el chat (bloque arriba, explicación debajo, pregunta al final) y Daniel lo teclea en Android Studio. Objetivo del spec (apartado 11, S11): **Pruebas de Room en `androidTest/` (P117, con red de seguridad: si no arrancan en una sesión, se documenta y R1, R7 y R16 quedan cubiertas por las manuales); P-C-06, 07, 08 pasan, o la ficha dice por qué no.** Entregable: tres pruebas nuevas en `app-ako/app/src/androidTest/` que abren **una base de datos en memoria con la precarga ejecutada** (`Room.inMemoryDatabaseBuilder`, spec 12) y pasan en el emulador; o, si no arrancan, la ficha con el porqué (P117). Manda el spec (apartado 12: tabla de las nueve P-C y red de seguridad) y el plan de pruebas (apartado 4: P-C-06, 07 y 08 con su *Entrada* y su *Resultado esperado*, y la nota P124). No hay pantalla ni wireframe.
> **Prerrequisitos** (de S1 a S10): `class Precarga(context: Context)` como `RoomDatabase.Callback` con la carga en `suspend fun cargar(db: AppDatabase)` y un `onCreate` que solo lanza una corrutina (`CoroutineScope(Dispatchers.IO)`, P16) que llama a `cargar(AppDatabase.obtener(context))`, y `AppDatabase.obtener(context)` con la única instancia (S2, piezas 7 y 9); `CategoriaDao.porDefecto()` y `todas()`, `MesaDao.todas()`, `ComandaDao.porId`, `pendienteDeMesa`, `lineasDe`, `contarLineas` (S3); `CartaRepository` con cuatro DAOs por constructor (`guardarCategoria`, `eliminarCategoria`) y `ComandaRepository(mesaDao, comandaDao, db)` con `enviarCarrito(mesaId, carrito): Long` dentro de `withTransaction`, `quitarLinea(lineaId): Boolean` y `mesasConEstado()` (S4, opción A de su Plan Mode; P17); `Carrito(mesaId)` con `anadir(producto, cantidad)` (S3); P-C-01 a 05 y P-C-09 en verde. **La red de seguridad ya está puesta:** P-M-20 (R1) y P-M-24 (R7) pasaron enteras en la S9 y la S10, y P-M-05 (R16) en la S8. Si la S4 o la S8 cambiaron algún nombre, manda lo que haya en el código.
> **Horas estimadas: 4–6 h** (7 piezas de 20–40 min más apertura, cierre y el tiempo que se lleve el primer arranque de las pruebas en el emulador). Chat con **Opus 5.5, esfuerzo medio**; sin Plan Mode (P25).

# Sesión 11 — guía

## 0. Al abrir

`abrir-sesion` lee la ficha de la S10 y, antes de la primera pieza, Claude mira **cuatro cosas del código real** y las dice en tres líneas:

1. **Cómo quedó `Precarga`** (`datos/Precarga.kt`): la guía de la S2 (pieza 8) la construye ya en dos partes, `cargar(db)` y un `onCreate` que solo lanza la corrutina; se comprueba que el código real es así o si la corrutina de `onCreate` hace todo el trabajo dentro. Y cómo se llama la función de la única instancia (`AppDatabase.obtener`).
2. **Los constructores reales de los repositorios** (S4): qué recibe `ComandaRepository` (opción A: `mesaDao`, `comandaDao` y la `AppDatabase`; opción B: sin la base) y los cuatro DAOs de `CartaRepository`.
3. **`app-ako/app/build.gradle.kts`**: si tiene `testInstrumentationRunner` y dónde está `room-testing` (pieza 1).
4. **Si existe `app-ako/app/src/androidTest/java/yunkang/ako/ExampleInstrumentedTest.kt`**, la prueba de ejemplo que crea el asistente de Android Studio en la S1. Si sigue ahí, se usa en la pieza 2.

**Dos avisos antes de empezar [Claude]:**

- **El emulador tiene que estar encendido** durante toda la sesión: las pruebas de `androidTest/` corren dentro de él.
- **`connectedDebugAndroidTest` suele desinstalar la app al terminar** (es lo que hace Gradle por defecto; lanzadas desde el triángulo verde de Android Studio, la app se queda; **se comprueba en la pieza 7**, y la ficha dice lo que pasó). Desinstalar borra el PIN, el juego de datos y las comandas del emulador. Por eso, durante la sesión se lanzan desde Android Studio, y por Gradle solo una vez, al final (pieza 7). No pasa nada: la S12 empieza con instalación limpia y su montaje mínimo (`docs/guias/juego-de-datos.md`, apartado 3).

**Por qué la `Precarga` está partida en dos [Claude].** Las tres pruebas tienen que abrir una base de datos **vacía, en memoria y con la precarga hecha** (spec 12): sin *Otros* ni las 60 mesas no hay nada que probar. Una `Precarga` cuyo `onCreate` lo hiciera todo dentro de su corrutina (lo literal de P16) no se podría usar en una prueba por dos motivos:

1. **Pediría la instancia única.** La corrutina obtiene la base de datos con `AppDatabase.obtener(context)`, que es la del archivo `ako.db` de la app. En una prueba, eso metería *Otros* y las mesas en la base de datos real del emulador, no en la de memoria que la prueba está mirando.
2. **No avisaría de cuándo termina.** `onCreate` lanza la corrutina y sigue. La prueba no sabe si *Otros* ya existe cuando pregunta por él: a veces sale verde y a veces rojo, según quién llegue antes. Una prueba así (*intermitente*) no sirve.

**La solución [Claude], que la guía de la S2 (pieza 8) ya aplica desde el principio:** separar *qué se inserta* de *cuándo se inserta*. Todo el trabajo vive en `suspend fun cargar(db: AppDatabase)` de la misma clase `Precarga`, que usa los DAOs **de la base de datos que recibe**; `onCreate` solo lanza la corrutina, que llama a `cargar(AppDatabase.obtener(context))`. La app se comporta igual (P16 intacto: `Callback`, corrutina en `Dispatchers.IO`, DAOs, nada de `execSQL`), y la prueba abre su base de datos en memoria **sin** el `Callback` y llama ella misma a `cargar(db)` dentro de `runBlocking`: sabe qué base de datos se llena y espera a que termine. **La pieza 3 lo comprueba** en el código real; solo si la S2 lo dejó todo dentro de `onCreate`, se separa ahí (mover el cuerpo de la corrutina a `cargar` y cambiar dos líneas de `onCreate`).

**Lo provisional de esta sesión:** nada. La prueba de ejemplo de la pieza 2 se borra en la pieza 7.

## 1. Piezas

Regla 12 de `CLAUDE.md`: explicación breve → código completo con su ruta → Daniel lo teclea → `/verificar` → pregunta. Rutas de Kotlin de la app bajo `app-ako/app/src/main/java/yunkang/ako/`; **las pruebas de esta sesión, bajo `app-ako/app/src/androidTest/java/yunkang/ako/`**. Ninguna prueba se debilita para que pase (el `revisor` lo mira en la S13): si una sale roja, se arregla el código o se entiende por qué, nunca se cambia el valor esperado.

### Pieza 1 — Gradle: el lanzador de pruebas y `room-testing` (20 min)

- **Qué:** las dos líneas que faltan para que Android sepa ejecutar pruebas dentro del emulador.
- **Archivo:** `app-ako/app/build.gradle.kts` — en `defaultConfig`, `testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"` (lo exige el spec, apartado 2); en `dependencies`, `testImplementation(libs.androidx.room.testing)` pasa a ser `androidTestImplementation(libs.androidx.room.testing)`. `libs.versions.toml` no se toca: las tres dependencias de pruebas del spec (`room-testing`, `androidx.test.ext:junit`, `androidx.test:runner`) ya están en el catálogo desde la S1. **Desde el 25 sep (P43 → B) `stack-verificado/` ya trae las dos líneas bien**, así que lo normal es que la pieza se reduzca a comprobarlas, sincronizar y ver el verde; solo si el archivo real de `app-ako/` no las tiene se escriben aquí.
- **Qué te explico antes:** qué es el *instrumentation runner* (*«el encargado que, dentro del emulador, arranca la app en modo prueba y va ejecutando cada `@Test`»*); por qué cada carpeta tiene su propia lista de herramientas (`testImplementation` sirve a `test/`, `androidTestImplementation` a `androidTest/`: `room-testing` en la lista equivocada no llega nunca a las pruebas que la usarían); por qué faltaba: el archivo de `docs/guias/stack-verificado/` que se copió en la S1 no traía el *runner* y tenía `room-testing` en `test/` (hueco visto en la sesión 00; la corrección se hace aquí, en `app-ako/`, y se anota al cerrar). Un apunte honesto [Claude]: `Room.inMemoryDatabaseBuilder` viene con `room-runtime`, y `room-testing` sirve sobre todo para probar migraciones, que el nivel 1 no tiene; se deja en `androidTest/` porque el spec (2, P117) la fija ahí.
- **Qué comprobamos después:** *File → Sync Project with Gradle Files* sin error; `assembleDebug` y `testDebugUnitTest` siguen en verde (las de `test/`: P-C-01 a 05 y 09).
- **Pregunta:** ¿por qué `room-testing` no servía de nada puesta en `testImplementation`?

### Pieza 2 — `test/` frente a `androidTest/`, y la primera prueba en el emulador (30 min)

- **Qué:** comprobar que el emulador ejecuta pruebas **antes** de escribir las de verdad. Es el punto de control de la red de seguridad (P117).
- **Archivo:** `androidTest/java/yunkang/ako/ExampleInstrumentedTest.kt` si el asistente de la S1 lo dejó (comprueba que el paquete de la app es `yunkang.ako`); si se borró, `ArranqueTest.kt` [Claude] con esa misma comprobación y nada más. Se borra en la pieza 7.
- **Qué te explico antes:** **`test/` frente a `androidTest/`**: `test/` es *probar la receta en la mesa de tu cocina* (corre en el PC, en segundos, sin Android: por eso ahí solo viven las puras); `androidTest/` es *probarla en el restaurante abierto*: Gradle construye un segundo APK con las pruebas (`yunkang.ako.test`), lo instala en el emulador junto a la app y el *runner* las ejecuta dentro del proceso de la app, con Android de verdad (SQLite, `Context`, archivos). **`@RunWith(AndroidJUnit4::class)`** encima de la clase: *«esta clase se ejecuta con el lanzador de Android»*. **Cómo se lanzan:** con el emulador encendido, el triángulo verde junto a la clase o a un `@Test` (*Run*), o clic derecho en la carpeta `androidTest/java` → *Run 'All Tests'* (el texto exacto varía: **comprobar en pantalla**); la primera vez tarda un par de minutos (compila e instala dos APK). Por consola, desde `app-ako/`: `./gradlew.bat connectedDebugAndroidTest` (lo lanza Claude; deja un informe HTML en `app/build/reports/androidTests/connected/`, **comprobar la ruta exacta**, y suele desinstalar la app al terminar).
- **Qué comprobamos después:** barra verde en la pestaña *Run*. Si sale un error del *runner*, se vuelve a la pieza 1. **Límite de tiempo [Claude]:** si al terminar esta pieza y **una hora más** de arreglos el emulador no ejecuta ninguna prueba, se para y se aplica la red de seguridad (apartado 2); no se gasta la sesión entera peleando con la herramienta.
- **Pregunta:** ¿por qué una prueba de `androidTest/` necesita el emulador encendido y una de `test/` no?

### Pieza 3 — `Precarga`: la carga en una función que se puede llamar (30 min)

- **Qué:** comprobar (y, solo si hace falta, hacer) la separación explicada en el apartado 0.
- **Archivo:** `datos/Precarga.kt` — `suspend fun cargar(db: AppDatabase)` [Claude, S2 pieza 8] con todo el trabajo, en este orden (**Otros primero**, 60 mesas, 14 alérgenos, Bebidas y Agua, P7; sin etiquetas, P115), usando `db.categoriaDao()`, `db.mesaDao()`, `db.precargadosDao()` y `db.productoDao()` de la base que recibe; `onCreate` solo lanza la corrutina, que llama a `cargar(AppDatabase.obtener(context))`. Si el código real ya es así, no se toca nada; si la S2 lo dejó todo dentro de `onCreate`, se separa aquí.
- **Qué te explico antes:** los dos problemas del apartado 0 con el dibujo de dos bases de datos (la del archivo y la de memoria) y un reloj; por qué la app no nota nada (la precarga sigue ocurriendo una vez, al crear el archivo, en otra cola); qué significa que `cargar` sea `suspend` (*«quien la llame puede esperarla»*: la corrutina de `onCreate` no espera a nadie, la prueba sí); por qué **no** se añade el `Callback` a la base de datos de la prueba (volvería a lanzar la carga por su cuenta, contra la instancia única).
- **Qué comprobamos después:** si no se tocó `Precarga`, basta con que Claude lo confirme leyendo el archivo. Si se tocó: compila; las de `test/` en verde; **desinstalar la app**, *Run ▶*, crear el PIN y mirar el Database Inspector: las mismas filas que en la S2 (Otros y Bebidas, Agua, 60 mesas, 14 alérgenos; sin etiquetas, P115). *(Esto borra los datos del emulador: si se quiere conservar el juego de datos, se hace esta comprobación al final de la sesión, justo antes de la pieza 7, que los borra igualmente.)*
- **Pregunta:** si `onCreate` llamara a `cargar` directamente, sin corrutina, ¿qué pasaría y por qué P16 la lanza aparte?

### Pieza 4 — P-C-08: una sola categoría por defecto (35 min)

- **Qué:** la primera prueba de verdad, y la que prueba la propia precarga.
- **Archivo:** `androidTest/java/yunkang/ako/datos/CategoriaPorDefectoTest.kt` [Claude, nombre] — `@RunWith(AndroidJUnit4::class)`; `@Before`: `Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()` **sin** `addCallback`, `runBlocking { Precarga(context).cargar(db) }` y un `CartaRepository` con los cuatro DAOs de **esa** base (el constructor de la S4: P17 hace posible esto); `@After`: `db.close()`. El `context` sale de `ApplicationProvider.getApplicationContext()`, que llega con `androidx.test.ext:junit` (sin librería nueva). Un `@Test` con las tres partes del plan: (1) tras la precarga hay **una** categoría con `esPorDefecto = true` y está activa; (2) `eliminarCategoria` con su `id` → al volver a leerla con `porDefecto()`, **sigue activa**; (3) `guardarCategoria` de una categoría *Varios* con `esPorDefecto = true` → **sigue habiendo una sola** con `esPorDefecto = true`.
- **Qué te explico antes:** **la base de datos en memoria** (*«una pizarra que se borra al cerrar»*: no toca `ako.db` ni los datos del emulador); **`@Before` y `@After`** se ejecutan antes y después de **cada** `@Test`: cada prueba empieza con su pizarra limpia y no depende de otra; **`runBlocking`** = *«espera aquí a que termine»*: en la app no se usa nunca (congelaría la pantalla), en una prueba es justo lo que queremos; la prueba **cuenta** `esPorDefecto`, nunca busca «Otros» (R16); la parte (3) comprueba el recuento y no el `ResultadoGuardado`, así vale tanto si la S4 decidió **rechazar** una segunda por defecto como si la **normaliza** a `false` (spec 6: «la rechaza o normaliza»).
- **Qué comprobamos después:** verde. Y el truco [Claude]: **romperla a propósito** (comentar en `guardarCategoria` la línea que normaliza o rechaza, lanzar, ver el rojo con su mensaje, y dejarla como estaba). Una prueba que nunca has visto en rojo no sabes si prueba algo.
- **Pregunta:** ¿por qué P-C-08 no puede ir en `test/` con un DAO falso, como P-C-09?

### Pieza 5 — P-C-06: una sola comanda abierta por mesa (40 min)

- **Qué:** R1, R2 y R14 en una prueba: dos envíos a la mesa 4 acaban en una comanda con dos líneas congeladas.
- **Archivo:** `androidTest/java/yunkang/ako/datos/repositorios/ComandaRepositoryTest.kt` [Claude, nombre] — el mismo `@Before`/`@After` que la pieza 4 (**repetido a propósito**, regla 2: dos clases que se leen solas), más la preparación [Claude]: Carnes con `categoriaDao().insertar`, **12 · Entrecot · 1850** en Carnes y **32 · Helado · 500** en la categoría por defecto con `productoDao().insertar` (los del juego de datos del plan de pruebas); la mesa 4 se busca **por su número** en `mesaDao().todas()` (nunca se da por hecho que su `id` es 4); `ComandaRepository` con el constructor real de la S4. El `@Test`: carrito con 2 × Entrecot → `enviarCarrito` → `id1`; otro carrito con 1 × Helado → `enviarCarrito` → `id2`. Se comprueba: `id1 == id2`; `pendienteDeMesa` de la mesa 4 devuelve ese mismo `id`; `contarLineas` = 2; las líneas llevan `nombreProducto` «Entrecot» y «Helado», `precioUnitarioCentimos` 1850 y 500 y cantidades 2 y 1.
- **Qué te explico antes:** R1 y por qué **la base de datos no puede** garantizarla (haría falta un índice único parcial, que Room no declara: P128) — esta prueba es la demostración en código de que la garantiza `enviarCarrito`; R2 (el segundo envío añade líneas, no suma a las de antes) y R14 (el nombre y el precio se **copian**); por qué se prepara con los DAOs y no con la pantalla (la prueba mira `enviarCarrito`, no el formulario); las claves foráneas: Carnes tiene que existir **antes** que Entrecot, o Room se niega (R11); `withTransaction` funciona igual en memoria.
- **Qué comprobamos después:** verde. Cambiar a propósito el 2 esperado de `contarLineas` por un 3, ver cómo cuenta el fallo («expected 3 but was 2»), y volver a dejarlo.
- **Pregunta:** si `enviarCarrito` creara una comanda nueva en cada envío, ¿qué comprobaciones de esta prueba fallarían?

### Pieza 6 — P-C-07: quitar la última línea anula la comanda (25 min)

- **Qué:** R7 y R3: sin líneas, la comanda queda ANULADA y la mesa libre.
- **Archivo:** el mismo `ComandaRepositoryTest.kt`, un segundo `@Test` — enviar 1 × Entrecot a la mesa 4; `lineasDe` da la única línea; `quitarLinea` con su `id` devuelve **`true`**; `comandaDao().porId` → estado **ANULADA** y `fechaCierre` distinta de `null`; `contarLineas` = 0; en `mesasConEstado()` la mesa 4 sale **sin `comandaId`**.
- **Qué te explico antes:** por qué este `@Test` no aprovecha la comanda de P-C-06 (cada `@Test` tiene su base limpia y JUnit no promete el orden); R7 (la comanda no se borra: queda ANULADA con cero líneas en el histórico, R5) y R3 (nadie escribe «libre» en ningún sitio: la mesa está libre porque la consulta ya no encuentra una PENDIENTE); qué hacía la pantalla de la S9 con ese `true` (volver a 6a).
- **Qué comprobamos después:** las dos de la clase en verde, y las tres de `androidTest/` juntas (clic derecho en la carpeta → *Run*).
- **Pregunta:** después de esta prueba, ¿la comanda sigue existiendo en la base de datos? ¿Por qué no se borra?

### Pieza 7 — Las nueve de una vez, y dejarlo escrito (25 min)

- **Qué:** pasar todas las pruebas de código como en el cierre y anotar.
- **Archivos:** se borra `ExampleInstrumentedTest.kt` (o `ArranqueTest.kt`): en `androidTest/` quedan solo las tres del plan; `docs/spec+doc-pruebas.md` (P-C-06, 07 y 08: *Resultado*, *Observaciones*, *Fecha*; lo escribe Claude en el cierre).
- **Qué te explico antes:** qué hace cada orden (`testDebugUnitTest` = las seis P-C de `test/` en el PC; `connectedDebugAndroidTest` = las tres de `androidTest/` en el emulador) y cómo leer el informe HTML; por qué esta es la última pieza (Gradle suele desinstalar la app al terminar: se perderían los datos del emulador, y la S12 empieza de todas formas con instalación limpia, `juego-de-datos.md`); **por qué se descartó Room KMP (P124)**: la documentación de Room recomienda hoy probar la base de datos en el PC con Kotlin Multiplatform, pero exigiría rehacer el proyecto con estructura multiplataforma para ahorrarse arrancar el emulador tres veces.
- **Qué comprobamos después:** las dos órdenes con *BUILD SUCCESSFUL*; el informe dice 3 pruebas y 0 fallos. Después, *Run ▶* para volver a tener la app instalada (se crea el PIN 1234 de nuevo).
- **Pregunta:** ¿qué reglas quedan ahora cubiertas dos veces, por una prueba manual y por una de código, y qué gana cada lado?

## 2. Pruebas que cierran la sesión

- **P-C-06, P-C-07 y P-C-08: Pasa**, con fecha en `spec+doc-pruebas.md` (Observaciones: «Room en memoria con `Precarga.cargar`; lanzadas con `connectedDebugAndroidTest`»).
- **P-C-01 a P-C-05 y P-C-09** siguen en verde.
- **Sin pruebas manuales** en esta sesión. `estado-nivel.md`: **ningún RF cambia** (las P-C no son RF); la ficha dice que R1, R7 y R16 tienen ya prueba manual **y** de código (tabla de cobertura del plan, apartado 5).
- Cierre con `cerrar-sesion` (sin `revisor`: toca en la S13): ficha `sesion-11.md`; `decisiones-code.md` con `Precarga.cargar(db)` (si se hizo aquí), el *runner* y `room-testing` corregidos en `app-ako/` (y qué se hace con `stack-verificado/`, duda para Daniel), los nombres de las dos clases de prueba, la preparación con DAOs y la mesa buscada por número; **dos commits**: `S11: pruebas de Room en androidTest` y después `S11: ficha del diario`, con push.

### Si no arrancan: la red de seguridad (P117)

Si se llega al límite de la pieza 2 sin que el emulador ejecute ninguna prueba, o si las tres escritas no pasan por un fallo de la herramienta (no del código de la app), **la sesión se cierra igual**: el objetivo del spec incluye «o la ficha dice por qué no». Es la única excepción al «todas en verde» del paso 1 de `cerrar-sesion`, y se dice así en la ficha. Lo que se escribe [Claude]:

- **Ficha, *Problemas y soluciones*:** una fila por intento, con el **error copiado tal cual** (de la pestaña *Run* o del informe HTML), qué se cambió y por qué no bastó.
- **Ficha, *Qué se hizo*:** si las pruebas llegaron a escribirse (se quedan en el repositorio si compilan: escritas y no ejecutadas, dicho así) o no; y la frase: «Se aplica P117 el <fecha>: las pruebas de Room no arrancan en el emulador. R1, R7 y R16 quedan cubiertas por P-M-20 (paso 4) y P-M-28 (paso 4), por P-M-24, y por P-M-05 (paso 5) y P-M-04» (tabla de cobertura del plan, apartado 5), con las fechas en que pasaron esas manuales.
- **`spec+doc-pruebas.md`:** en P-C-06, 07 y 08, *Resultado* «No ejecutada» [Claude], *Observaciones* «Red de seguridad P117: no arrancan en el emulador (ficha S11); R1, R7 y R16 cubiertas por P-M-20, P-M-24 y P-M-05», *Fecha* la del día.
- **Siguiente sesión:** «La S13 vuelve a lanzar `connectedDebugAndroidTest` una vez (10 min); si sigue sin arrancar, se queda como está». La memoria (fase 7) lo repite tal cual: **lo escrito coincide con lo hecho**.

## 3. Lo que tienes que saber defender

- **`test/` frente a `androidTest/`**: dónde corre cada una y por qué las seis puras van en el PC y las tres de Room en el emulador (spec 12; separar calcular de guardar, P237).
- **Qué es una base de datos en memoria** y por qué cada prueba empieza con la suya, limpia y con la precarga hecha (`@Before`), y la cierra al acabar (`@After`).
- **R1 tiene doble garantía de prueba**: P-M-20 con el dedo y P-C-06 en código; y la base de datos **no** puede imponerla (P128). Lo mismo R7 (P-M-24 y P-C-07) y R16 (P-M-05 y P-C-08).
- **Por qué la `Precarga` está partida en dos**: con todo dentro de `onCreate`, pediría la instancia única y no avisaría de cuándo termina; así, *qué se carga* es una función que la prueba puede llamar y esperar, y la app hace lo mismo (P16).
- **Las dos alternativas descartadas**: Room KMP para probar en el PC (P124: cambio de arquitectura entero) y, si hubiera hecho falta, la red de seguridad P117 contada tal cual.

## 4. Riesgos típicos y qué hacer

- **«Unable to find instrumentation info for: ComponentInfo{yunkang.ako.test/…}»** o **«No tests found»** al lanzar: falta `testInstrumentationRunner` en `defaultConfig` o no se sincronizó Gradle (pieza 1).
- **«No connected devices!»** o *No target device found*: el emulador no está encendido o no terminó de arrancar; `C:\Android\Sdk\emulator\emulator.exe -avd Pixel_6_API_34` y esperar a la pantalla de inicio.
- **Una prueba sale verde unas veces y roja otras**: se añadió `addCallback(Precarga(...))` a la base de datos en memoria y la carga corre por su cuenta; se quita el `Callback` y se llama a `cargar(db)` dentro de `runBlocking`.
- **«FOREIGN KEY constraint failed»** o **«UNIQUE constraint failed: producto.numero»** en el `@Before`: se insertó Entrecot antes que Carnes, o dos platos con el mismo número; la preparación sigue el orden del juego de datos.
- **«Unresolved reference: ApplicationProvider»**: el import es `androidx.test.core.app.ApplicationProvider`; si aun así no aparece, `InstrumentationRegistry.getInstrumentation().targetContext` hace lo mismo con `androidx.test:runner`, que ya está (ninguna librería nueva).
- **Tras `connectedDebugAndroidTest`, la app ha desaparecido del emulador** o pide crear el PIN: es normal (Gradle desinstala al terminar); se rehace el juego de datos en la S12 con `docs/guias/juego-de-datos.md`.
- **La prueba pasa también con el código roto**: se ha comprobado lo que no toca (por ejemplo, el `ResultadoGuardado` en vez del recuento); el truco de la pieza 4 (romper a propósito, ver el rojo, deshacer) lo destapa.
