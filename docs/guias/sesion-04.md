> **Guía de la sesión 4 — Repositorios y `PinStore`.** Escrita en la sesión 00 (24 sep 2026) [Claude] como plan de piezas: qué se construye, en qué orden y qué hay que entender en cada trozo. No lleva código: el código lo da Claude pieza a pieza en el chat (bloque arriba, explicación debajo, pregunta al final) y Daniel lo teclea en Android Studio. Objetivo del spec (apartado 11, S4): **`CartaRepository`, `ComandaRepository`, `SeguridadRepository`, `PinStore`; compila; cada método tiene un comentario de una línea con la regla que garantiza.** Entregable: la app compila y las seis pruebas de `test/` siguen en verde, más la mitad nueva de P-C-09 (con DAO falso).
> **Prerrequisitos** (de S2 y S3): las 7 entidades del nivel 1 (P115), `AppDatabase` y `Precarga`; los 5 DAOs con las consultas del diagrama (`pendienteDeMesa`, `pendientesConTotal`, `visibles`, `existeNumero`, `existeNombre`, `mesasConProductoPendiente`, `mesasConCategoriaPendiente`, `pagadasEntre`…); `Carrito`, `LineaCarrito`, `Calculadora`, `Validacion` (con `precioValido`, `cantidadValida`, `pinValido`), `Hash`; `MesaEstado` y `MesaConTotal` en `dominio/modelos/`; P-C-01 a P-C-05 y la mitad pura de P-C-09 en verde. Si `Ako : Application` ya existe desde la S2 con la base de datos (P17), en la pieza 11 solo se le añaden los repositorios.
> **Horas estimadas: 6–8 h** (12 piezas de 20–40 min más aperturas, cierre y revisión). Chat con **Opus 5.5, esfuerzo alto**.

# Sesión 4 — guía

## 0. Al abrir: Plan Mode (P25)

Esta sesión se abre en **Plan Mode**: Claude lee los DAOs reales de la S3 (los nombres pueden haber cambiado respecto al diagrama) y escribe el plan de las 12 piezas de abajo con los nombres reales antes de tocar código. En ese plan se cierran **cuatro huecos que el diagrama de clases deja abiertos** (todos [Claude], Daniel decide):

1. **`CartaRepository` necesita también `ComandaDao`** para R6: las consultas `mesasConProductoPendiente` y `mesasConCategoriaPendiente` viven en `ComandaDao`, no en los DAOs de la carta. Se le pasa por constructor como un cuarto DAO.
2. **R6 en dos pasos.** La pantalla enseña las mesas afectadas *antes* de confirmar (spec 6), así que hace falta un método de solo lectura (`mesasAfectadasPorPlato(id)` / `mesasAfectadasPorCategoria(id)`) y otro que elimine (`eliminarPlato(id)` / `eliminarCategoria(id)`), que devuelve la misma lista para cuadrar con el diagrama.
3. **La transacción de `enviarCarrito`.** Room solo pone `@Transaction` en los DAOs; en el repositorio se usa `withTransaction` de `AppDatabase`. Opción A: `ComandaRepository` recibe también la `AppDatabase` por constructor. Opción B: un método `@Transaction` en `ComandaDao` que solo inserta comanda y líneas juntas, sin decidir nada. Recomendada A: la regla se lee entera en un sitio.
4. **La tercera salida de la cadena 3e** («se guarda ahí, invisible»): `guardarPlato` recibe un parámetro `aunqueCategoriaEliminada: Boolean = false`; la pantalla lo pone a `true` solo cuando el Propietario ha dicho que no a las dos preguntas.

Con el plan aprobado se sale de Plan Mode y se empieza por la pieza 1.

## 1. Piezas

Cada pieza sigue la regla 12 de `CLAUDE.md`: explicación breve → código completo con su ruta → Daniel lo teclea → `/verificar` → pregunta de comprensión. **Todas las rutas cuelgan de `app-ako/app/src/main/java/yunkang/ako/`** salvo que se diga otra cosa. **Cada método público lleva encima un comentario de una línea con la regla que garantiza**, por ejemplo `// R7: una comanda sin líneas queda ANULADA con fechaCierre y la mesa libre`; si un método no garantiza ninguna, el comentario dice qué devuelve y para qué pantalla.

### Pieza 1 — `ResultadoGuardado` (20 min)

- **Qué:** el tipo con el que los repositorios responden «se guardó» o «no, por esto».
- **Archivo:** `dominio/modelos/ResultadoGuardado.kt` — `enum class ResultadoGuardado { Ok, NumeroRepetido, NombreRepetido, CategoriaEliminada }` (P11).
- **Qué te explico antes:** qué es un `enum` (una lista cerrada de valores con nombre); por qué el repositorio contesta con una palabra en vez de con `true/false` (la pantalla tiene que saber *qué* aviso enseñar); por qué el precio negativo **no** está aquí (P11: es un error de programación, lanza excepción; los otros tres son situaciones normales que la pantalla resuelve preguntando).
- **Qué comprobamos después:** compila. Nada más: todavía no lo usa nadie.
- **Pregunta:** ¿por qué un número repetido es un valor de `ResultadoGuardado` y un precio negativo es una excepción?

### Pieza 2 — `PinStore` (30 min)

- **Qué:** el archivo privado donde viven la sal y el hash del PIN. Nunca el PIN.
- **Archivo:** `seguridad/PinStore.kt` — constructor con `Context`; `existe(): Boolean`, `guardar(pin: String)`, `coincide(pin: String): Boolean`; sal de 16 bytes de `Hash.generarSal()` (S3, `SecureRandom`; no se repite aquí [Claude]); hash por `Hash.pbkdf2` (S3); guardado en Base64 en `SharedPreferences` con `MODE_PRIVATE`.
- **Qué te explico antes:** qué es `SharedPreferences` (un archivo pequeño de pares clave-valor, privado de la app, que se borra al desinstalar); qué es la sal y por qué se genera al azar una vez; por qué guardamos bytes como texto (Base64); por qué `coincide` **recalcula** el hash con la sal guardada y compara, en vez de «desencriptar» (no se puede: es la gracia).
- **Qué comprobamos después:** compila; en el archivo no hay ninguna línea que guarde `pin` tal cual (Claude lo comprueba con una búsqueda); `Hash` sigue sin importar nada de Android.
- **Pregunta:** si alguien copia el archivo de preferencias, ¿qué ve y por qué no le sirve para entrar?

### Pieza 3 — `SeguridadRepository` (25 min)

- **Qué:** la puerta única al PIN para los ViewModels.
- **Archivo:** `datos/repositorios/SeguridadRepository.kt` — `class SeguridadRepository(private val pinStore: PinStore)`; `hayPin()`, `crearPin(pin)`, `comprobarPin(pin): Boolean`, `cambiarPin(actual: String, nuevo: String): Boolean`.
- **Qué te explico antes:** qué es una capa y por qué el ViewModel habla con el repositorio y no con `PinStore` (spec 3: cada capa solo ve la de abajo); qué es recibir las herramientas por constructor (P17): *«al crearlo le das lo que necesita; en una prueba le das uno falso»*; por qué `cambiarPin` comprueba el actual **antes** de mirar el nuevo (D18) y devuelve `false` sin tocar nada si falla; `Validacion.pinValido` se llama antes de guardar (4 cifras), aunque la pantalla ya lo impida.
- **Qué comprobamos después:** compila; los cuatro métodos llevan su comentario (este repositorio no garantiza ninguna R: el comentario dice qué pantalla lo usa: 1b, 1c, 1e y la salida de Pedir).
- **Pregunta:** ¿qué pasa en `cambiarPin` si el PIN actual es incorrecto, y por qué no se llega a mirar el nuevo?

### Pieza 4 — `CartaRepository`, categorías (35 min)

- **Qué:** leer, guardar y eliminar categorías con R16 y R6.
- **Archivo:** `datos/repositorios/CartaRepository.kt` — `class CartaRepository(categoriaDao, productoDao, precargadosDao, comandaDao)`; `categorias()`, `guardarCategoria(c: Categoria): ResultadoGuardado`, `mesasAfectadasPorCategoria(id): List<Int>`, `eliminarCategoria(id): List<Int>`, `recuperarCategoria(id)` [Claude: no está en el diagrama; *recuperar* = volver a `activo = true`, spec 5.1].
- **Qué te explico antes:** qué es `suspend` y por qué todo lo que toca la base de datos lo lleva (*«lo lento se hace en otra cola y avisa al terminar»*); R16 en código: `guardarCategoria` **normaliza** `esPorDefecto = false` en cualquier categoría nueva (así nunca hay dos, P-C-08) y `eliminarCategoria` **no hace nada** si la categoría es la de por defecto; `orden` = máximo + 1 al crear; `existeNombre` → `NombreRepetido`; eliminar es `activo = false`, nunca un `delete` (R5).
- **Qué comprobamos después:** compila; cada método con su comentario (`// R16 …`, `// R6 …`, `// R5 …`).
- **Pregunta:** ¿cómo sabe el repositorio cuál es la categoría por defecto, y por qué no puede mirar si se llama «Otros»?

### Pieza 5 — `CartaRepository`, lectura de platos (25 min)

- **Qué:** las consultas que usan el Panel, la carta y la puerta de Pedir.
- **Archivo:** el mismo — `platosDe(categoriaId)`, `platosVisibles()`, `hayPlatoVisible(): Boolean`, `plato(id)`, `alergenos()`, `alergenosDe(productoId)`.
- **Qué te explico antes:** la diferencia entre **plato existente** (toda fila) y **plato visible** (activo y categoría activa: R15; `disponible` se suma con el incremento 12, P115; que ya vive en la consulta `visibles()` de la S3); el Panel usa `platosDe` y lo ve todo, la carta usa `platosVisibles`; `hayPlatoVisible` es la puerta de Pedir (spec 6); en la S8 se le suma `hayPlatoExistente` [Claude, hueco 4 de su Plan Mode] para elegir entre *«La carta está vacía»* y *«Todas las categorías están eliminadas»*.
- **Qué comprobamos después:** compila; el repositorio no repite la condición de visible (la delega en el DAO).
- **Pregunta:** un plato activo en una categoría eliminada, ¿es visible? ¿Dónde está escrita esa regla?

### Pieza 6 — `CartaRepository`, guardar y eliminar platos (40 min)

- **Qué:** el método más cargado de reglas de la carta.
- **Archivo:** el mismo — `guardarPlato(p: Producto, alergenos: List<Long>, aunqueCategoriaEliminada: Boolean = false): ResultadoGuardado`, `mesasAfectadasPorPlato(id): List<Int>`, `eliminarPlato(id): List<Int>`, `recuperarPlato(id)` [Claude, como `recuperarCategoria`].
- **Qué te explico antes:** el **orden** de las comprobaciones y por qué importa: primero `Validacion.precioValido` (R8, lanza), después `existeNumero` (R9 → `NumeroRepetido`), después la categoría (`CategoriaEliminada` si está eliminada y no se fuerza), y solo entonces insertar o actualizar y `guardarAlergenos`; cómo R9 vive **dos veces** (el `UNIQUE` de la base de datos la impone; `existeNumero` solo sirve para avisar antes, P128); `eliminarPlato` marca `activo = false` y **no toca ninguna línea de comanda** (R6): lo pedido sigue pedido.
- **Qué comprobamos después:** compila; comentarios `// R8`, `// R9`, `// R6`, `// R5`; Claude comprueba que no hay ningún `delete` sobre `producto`.
- **Pregunta:** si se elimina un plato que está en una comanda pendiente de la mesa 6, ¿qué cambia en `linea_comanda`? (Nada. ¿Por qué?)

### Pieza 7 — P-C-09 con un DAO falso (35 min)

- **Qué:** la mitad de P-C-09 que faltaba: `guardarPlato` con −100 lanza excepción **y el DAO no recibe nada**.
- **Archivos:** `app-ako/app/src/test/java/yunkang/ako/datos/repositorios/DaosFalsos.kt` [Claude] (clases `ProductoDaoFalso`, `CategoriaDaoFalso`, `PrecargadosDaoFalso`, `ComandaDaoFalso` que implementan las interfaces; todos los métodos devuelven vacío o `TODO()` salvo `insertar`, que apunta `seLlamo = true`) y `CartaRepositoryTest.kt` (tres casos: 1850 guarda, 0 guarda, −100 lanza `IllegalArgumentException` y `seLlamo` sigue en `false`).
- **Qué te explico antes:** qué es un doble de pruebas (*«un actor que hace de DAO y solo apunta si le llaman»*); por qué esto solo es posible porque el repositorio recibe los DAOs por constructor (P17); por qué la validación va **la primera** en `guardarPlato` (si fuera después de `existeNumero`, el falso tendría que saber contestar); `runBlocking` para llamar a un `suspend` desde una prueba [Claude]; cómo se lee un test rojo en Android Studio.
- **Qué comprobamos después:** las pruebas de `test/` —las seis P-C (P-C-01 a 05 y P-C-09, esta con sus dos mitades: `Validacion` y el DAO falso)— en verde desde Android Studio (clic derecho en la carpeta `test` → *Run Tests*).
- **Pregunta:** ¿qué demostraría la prueba si `guardarPlato` validara el precio *después* de insertar?

### Pieza 8 — `ComandaRepository`, lectura (30 min)

- **Qué:** lo que necesitan la rejilla de mesas (1d y 6a) y la comanda (6b).
- **Archivo:** `datos/repositorios/ComandaRepository.kt` — `class ComandaRepository(mesaDao, comandaDao, db: AppDatabase)` (opción A del Plan Mode); `mesasConEstado(): List<MesaEstado>`, `comandaPendiente(mesaId): Comanda?`, `lineasDe(comandaId)`, `totalDe(comandaId): Int`.
- **Qué te explico antes:** R3 en código: `mesasConEstado` junta las 60 mesas con `pendientesConTotal` y construye un `MesaEstado` por mesa; **ninguna columna dice si la mesa está ocupada**; R10: `totalDe` suma con `Calculadora.total` sobre las líneas, la comanda no tiene columna `total`.
- **Qué comprobamos después:** compila; comentarios `// R3` y `// R10`.
- **Pregunta:** ¿qué tabla o columna dice que la mesa 4 está ocupada? (Ninguna. ¿Cómo se sabe entonces?)

### Pieza 9 — `enviarCarrito` (40 min)

- **Qué:** el método que junta más reglas de toda la app.
- **Archivo:** el mismo — `enviarCarrito(mesaId: Long, carrito: Carrito): Long` dentro de `db.withTransaction { }`.
- **Qué te explico antes:** qué es una transacción (*«o se hace todo o no se hace nada»*: sin ella podría quedar una comanda sin líneas); R4: carrito vacío → excepción, nunca una comanda vacía; R1/R2: busca `pendienteDeMesa`; si hay, añade líneas; si no, crea la comanda; cada envío crea líneas **nuevas** (no suma a las existentes); R14: copia `nombre` y `precioCentimos` del producto a cada `LineaComanda` (cambiar la carta mañana no cambia lo pedido hoy); R8: `Validacion.precioValido` al congelar cada precio; por qué **la base de datos no puede** garantizar R1 (Room no declara índices únicos parciales, P128).
- **Qué comprobamos después:** compila; comentarios `// R1, R2`, `// R4`, `// R8`, `// R14`; el método devuelve el `id` de la comanda.
- **Pregunta:** ¿por qué R9 la impone la base de datos y R1 la tiene que imponer este método?

### Pieza 10 — `quitarLinea`, `anular`, `cobrar`, `resumenDelDia` (40 min)

- **Qué:** el ciclo de vida de una comanda y la consulta del Resumen de ingresos.
- **Archivos:** el mismo, más `dominio/modelos/ResumenIngresos.kt` (`dia`, `comandas: List<ComandaConTotal>`, `numComandas`, `totalCentimos`, del diagrama; `ComandaConTotal` ya existe desde la S3, pieza 6) — `quitarLinea(lineaId): Boolean`, `anular(comandaId)`, `cobrar(comandaId)`, `resumenDelDia(dia: LocalDate): ResumenIngresos`.
- **Qué te explico antes:** R5 primero: los tres métodos **se niegan** si la comanda no está PENDIENTE (una comanda cerrada es intocable); R7: tras borrar la línea, si `contarLineas` = 0 → estado ANULADA con `fechaCierre` y devuelve `true` (la pantalla ya avisó antes); `cobrar` = PAGADA + `fechaCierre = ahora`; el día como instante: `LocalDate` → milisegundos de las 0:00:00 a las 23:59:59.999 en la zona del dispositivo (spec 5.1) → `pagadasEntre`; el total de cada comanda se **calcula** (R10) y con eso se monta `ResumenIngresos` (comandas con mesa, hora y total; cuántas; total). La fila lleva el **número** de mesa, no su `id`: se saca de `mesaDao.todas()` sin suponer que `id` = número [Claude] (la S10 lo necesita así).
- **Qué comprobamos después:** compila; comentarios `// R5`, `// R7`, `// R10`; no existe ningún método que borre una comanda ni una entidad.
- **Pregunta:** si quitas la única línea de una comanda, ¿qué queda en la base de datos y por qué no se borra la comanda?

### Pieza 11 — `Ako : Application` (25 min)

- **Qué:** el objeto que vive toda la ejecución y reparte la base de datos y los repositorios.
- **Archivos:** `Ako.kt` (raíz del paquete; existe desde la S2, pieza 7, con `val db by lazy { AppDatabase.obtener(this) }` y ya registrado en `AndroidManifest.xml` con `android:name=".Ako"`) — se le añaden `val pinStore by lazy`, `val cartaRepository by lazy`, `val comandaRepository by lazy`, `val seguridadRepository by lazy`, que usan `db`.
- **Qué te explico antes:** qué es `Application` (se crea antes que cualquier pantalla y no muere hasta que muere la app); por qué la base de datos se abre **una sola vez** (spec 3: única instancia); qué es `by lazy` (se crea la primera vez que alguien lo pide); cómo un ViewModel llegará a esto en la S5 (`application as Ako`).
- **Qué comprobamos después:** compila; la app arranca en el emulador sin error en Logcat; el inspector de base de datos sigue enseñando las 7 tablas con la precarga (P115).
- **Pregunta:** ¿cuántas instancias de `AppDatabase` hay en la app, quién la guarda y quién la pide?

### Pieza 12 — Pasada de comentarios, pruebas y cierre (30 min + revisión)

- **Qué:** comprobar el entregable de la sesión y cerrar.
- **Archivos:** los tres repositorios y `PinStore`.
- **Qué te explico antes:** Claude lista los métodos públicos y la R que cada uno declara, y la cuadra con la tabla del spec (apartado 6): **ninguna R1–R16 del nivel 1 puede quedar sin dueño**; qué hace el subagente `revisor` (ojos nuevos: lee el código sin saber cómo se escribió y solo devuelve hallazgos verificados, P35) y por qué se pasa aquí y no en la S5 (todo lo demás se construye encima).
- **Qué comprobamos después:** `assembleDebug` en verde; las seis P-C de `test/` (P-C-01 a 05 y 09) en verde; el `revisor` sobre `app-ako/app/src`: los hallazgos *Alta* se arreglan antes del commit, los *Media/Baja* van a *Siguiente sesión* de la ficha.
- **Pregunta:** de las 16 reglas, ¿cuáles garantiza la base de datos sola y cuáles el código? (R9 y R11 la base; el resto el código.)

## 2. Pruebas que cierran la sesión

- **P-C-01 a P-C-05** siguen en verde (nada de la S3 ha cambiado).
- **P-C-09 completa:** `precioValido` (S3) **y** `guardarPlato` con DAO falso (pieza 7): 1850 y 0 guardan, −100 lanza y no guarda. Se anota con fecha en `spec+doc-pruebas.md`.
- **Sin pruebas manuales**: no hay pantalla. P-C-06, 07 y 08 (Room en memoria) son de la S11.
- `estado-nivel.md`: ningún RF pasa a *implementado* (los repositorios no son un RF); se anota en la ficha que R1–R16 tienen dueño en el código.
- Cierre con `cerrar-sesion`: revisión del `revisor`, ficha `sesion-04.md`, `decisiones-code.md` con las cuatro decisiones del Plan Mode, y **dos commits**: `S4: repositorios y PinStore` y después `S4: ficha del diario` (decisión de la sesión 00), con push.

## 3. Lo que tienes que saber defender

- **La tabla «quién garantiza cada regla»** (spec 6) entera, y en especial: R9 la impone la base de datos con un `UNIQUE`; R1 no puede (Room no declara índices únicos parciales) y la impone `enviarCarrito`. Es el argumento del vídeo (P128).
- **Por qué los repositorios reciben los DAOs por constructor** y qué permite eso: P-C-09 prueba `guardarPlato` sin emulador con un DAO falso.
- **Eliminar no borra:** `activo = false` en categorías y platos (R5), y al eliminar **no se toca ninguna línea** de comanda (R6); *recuperar* es volver a `activo = true`. Y desactivar es otra cosa (nivel 2).
- **Lo que se calcula no se guarda:** ocupación de la mesa (R3) y total de la comanda (R10) salen de consultas y de `Calculadora`; las líneas, en cambio, **congelan** nombre y precio (R14).
- **El PIN nunca se guarda:** `PinStore` guarda sal + hash PBKDF2 y `coincide` recalcula; olvidarlo obliga a reinstalar (recorte declarado).

## 4. Riesgos típicos y qué hacer

- **Llamar a un DAO `suspend` desde código normal** («Suspend function can only be called from a coroutine»): todos los métodos del repositorio que tocan la base de datos son `suspend`; los llamará el ViewModel desde `viewModelScope` (S5). En las pruebas, `runBlocking`.
- **Consultas Room en el hilo principal** («Cannot access database on the main thread»): no se arregla con `allowMainThreadQueries()`; se arregla haciendo `suspend` el DAO (Room cambia de hilo solo).
- **`withTransaction` que no compila:** viene con `room-runtime` 2.8 (antes estaba en `room-ktx`); si falta el import, es `androidx.room.withTransaction`. Si se eligió la opción B, la transacción es un método `@Transaction` del DAO.
- **Tentación de un `@Delete`** para «limpiar» pruebas o para eliminar: no existe ningún `delete` de entidad en toda la app; solo `borrarLinea` (línea de comanda abierta) y, en el nivel 2, filas de relación.
- **Comentario de regla que no coincide con el código** (dice `// R7` y la comanda no cambia de estado): el `revisor` lo caza; mejor comprobarlo método a método en la pieza 12 antes de lanzarlo.
