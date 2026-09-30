# Sesión 02 — 2026-09-30

| Campo | Valor |
|---|---|
| **Fecha** | 2026-09-30 |
| **Sesión nº** | 02 |
| **Objetivo de la sesión** | Del spec: 14 entidades, `Converters`, `AppDatabase`, `Precarga`; compila y el inspector enseña las 14 tablas con *Otros*, 60 mesas, 14 alérgenos, 3 etiquetas y el ejemplo. **Cambiado en la sesión** (P113, P115): **7 entidades**, sin `Converters` ni etiquetas; el inspector enseña las 7 tablas con Otros, 60 mesas, 14 alérgenos y Bebidas / 1 · Agua · 1,50 € (P7) |
| **Tiempo dedicado** | 11:16 – ~13:35, unas 2 h 20 min, sin pausas (confirmado por Daniel) |
| **Nivel / pieza** | Nivel 1 · capa de datos (entidades Room y precarga, RF-50) |
| **Commit final** | `d23b193` — S2: 7 entidades, AppDatabase, EntradaAko y Precarga |

## Qué se hizo

- Repaso de lo no entendido en la S1 (Gradle, catálogo de versiones, `minSdk 26`) y conceptos de base de datos (tabla, fila, clave primaria, clave foránea, RESTRICT/CASCADE, índice único, `@Entity`).
- Paquetes `datos/{entidades,dao,repositorios}`, `dominio/modelos`, `seguridad`, `ui`.
- `EstadoComanda` (enum; Room lo guarda como texto, P113) y las **7 entidades** del nivel 1: `Categoria`, `Producto`, `Alergeno`, `ProductoAlergeno` (PK compuesta), `Mesa`, `Comanda`, `LineaComanda`. Todas las FK RESTRICT; ninguna CASCADE (P115).
- `AppDatabase` (7 entidades, versión 1) con 4 DAOs que solo tienen lo que usa la precarga: `CategoriaDao.insertar`, `ProductoDao.insertar`, `MesaDao.insertarTodas`, `PrecargadosDao.insertarAlergenos`.
- `EntradaAko : Application` (P116, P117) con la única base de datos (`by lazy`) y `onCreate` que la abre al arrancar (P118); registrada en el manifiesto.
- `strings.xml` con los 14 alérgenos, Otros, Bebidas y Agua. `Precarga` (`Callback` + corrutina + `cargar(db)`).
- **Objetivo comprobado** (30 sep, 13:27): app desinstalada y reinstalada; `categoria` 2 filas (Otros, orden 0, por defecto; Bebidas, orden 1), `producto` 1 (1 · Agua · 150, `categoria_id` 2), `mesa` 60 (1–60), `alergeno` 14 en orden; resto vacías. Visto por Daniel en el Database Inspector y por Claude sacando `ako.db` con `adb`.
- Documentación: P113–P118, reglas «el prototipo primero», «nada de código por si acaso» y «lo decidido aquí manda» en `CLAUDE.md`, `como-trabajamos` y `decisiones-code.md` (lista «Para el Project»); spec y guías S2–S13 ajustados a 7 tablas y `EntradaAko`.
- Nada a medias.

## Problemas y soluciones

| # | Qué falló | Cómo se resolvió | Justificación (por qué esta solución y no otra) | Fuente |
|---|---|---|---|---|
| 1 | Pieza 1: los paquetes salían fuera de `yunkang.ako` (`java/dato/dao`, `java/datos/repositorios`), uno con mayúscula (`Datos`) y `entidades` suelto en vez de dentro de `datos` | Claude quitó las carpetas vacías (`rmdir`, que solo borra carpetas vacías) y Daniel las volvió a crear con clic derecho **sobre `yunkang.ako`** y el nombre con punto (`datos.entidades`) | Clic derecho sobre `java` crea el paquete en la raíz; el paquete tiene que coincidir con la línea `package yunkang.ako.…` de cada archivo. Rehacerlas vacías era más rápido que moverlas | Propio |
| 2 | Pieza 5: `Comanda` y `LineaComanda` subrayados en rojo; `Comanda::class` sin resolver | El archivo se creó como **`Comanda.java`** (Java Class) con código Kotlin dentro; se renombró a `Comanda.kt` (Shift + F6) | Android Studio decide el lenguaje por la extensión: con `.java` el código Kotlin no se entiende y la clase no existe para Kotlin. Renombrar conserva el contenido; borrar y rehacer también valía, pero es más lento | Propio |
| 3 | Al renombrar, `LineaComanda.kt` pasó a `LineaComanda.kt.kt` con la clase `` `LineaComanda.kt` `` («Name contains illegal characters») | Claude devolvió el nombre a la clase y al archivo (`sed` + `mv`) | Shift + F6 renombra lo que está seleccionado o donde está el cursor, y en Kotlin renombrar la clase renombra también su archivo: había que tener seleccionado `Comanda.java` en el panel. Arreglo mecánico, sin cambiar código | Propio |
| 4 | Pieza 8a: los DAOs quedaron en `datos/dao/datos/` y `CategoriaDao` en un archivo `dao.kt` (icono de archivo Kotlin en vez de interfaz) | Claude movió los cuatro a `datos/dao/` y renombró `dao.kt` a `CategoriaDao.kt` (`mv`, `rmdir`); compila | Al crear, el paquete seleccionado era otro y el nombre del archivo se escribió como «dao». El `package` de dentro ya era el correcto, así que bastaba mover y renombrar | Propio |

## Qué entendí y qué no

- **Entendí** (repaso de la S1, 30 sep, palabras de Daniel): **Gradle**: «es quien hace que todo lo que hemos escrito de la app funcione, en el sentido de darle significado a las cosas y de compilar» (faltaba: también descarga las librerías y fabrica el APK) · **catálogo de versiones**: la versión de Room se cambia «en lo de librerías de Gradle» (`libs.versions.toml`; faltaba el porqué: un solo sitio para todo el proyecto) · **`minSdk 26`**: «no estaría la función del PIN, ya que fue en la 26 donde se implementó». · **RESTRICT**: «no se puede [borrar Bebidas] ya que [Agua] sigue apuntando» · **índice único** (número de plato): «para que se pueda identificar y no se confundan».
- **`dominio`**, primer intento: «un espacio en blanco donde actuar con más libertad». Tras el ejemplo de la libreta de reglas eligió bien (B): reglas y cuentas sin nada de Android, que se prueban en el ordenador sin emulador. Dudaba si «sin nada de Android» quería decir «sin código»: es código Kotlin normal que no usa ninguna clase de Android.
- **enum**: «si tenemos una errata, lo señala» (el compilador avisa antes de ejecutar; con texto libre no) · **`?`**: «hace que esa parte esté vacía; nombre no lo lleva porque no puede haber un plato sin nombre» · **clave foránea**: un plato con `categoriaId = 99` inexistente «no se podrá» guardar.
- **CASCADE**, primer intento: creía que al borrar una comanda se borra todo, y al borrar un plato sus modificadores. Corregido: la única CASCADE es `linea_modificador → linea_comanda` (al quitar una línea se van los extras elegidos en esa línea); comandas y platos no se borran nunca.
- **Opciones A/B de la base de datos única y `EntradaAko`**: tras varias explicaciones lo resumió así: «Ako guarda el acceso a la base de datos, y en la otra Ako le dice a AppDatabase que mire dentro de su caja fuerte, que dentro está el acceso». Lo que más le llamó la atención de la sesión: **`lazy`**.
- **Línea de comanda congelada (R14)**: «los cambios futuros no afectan a los precios guardados»; no supo decir la casilla (`precio_unitario_centimos` de `linea_comanda`). · **Manifiesto sin `EntradaAko`**: «daría error, no podría acceder a la base de datos» (matiz: compila, falla al usarla). · **Precarga con 80 mesas**: pensaba «descargarla otra vez»; es desinstalar para que `onCreate` vuelva a saltar.
- Valoración de Daniel: «ni fácil ni difícil».
- **No entendí todavía:** nada señalado por Daniel. Para repasar al abrir la S3 (dos minutos): la corrutina (trabajo por detrás) y `suspend`, que la S3 usa en todos los DAOs; y por qué `insertar` devuelve el `id`.

## Para el vídeo

*(Añadido el 30 sep 2026, después del cierre, a petición de Daniel: el apartado nació en la S3. Única excepción a «no se reescribe una ficha cerrada».)*

- **Las 7 tablas y no 14 (P115):** el prototipo es el nivel 1 y no se escribe código «por si acaso»; es como no comprar el lavavajillas hasta que haga falta. Las tablas del nivel 2 llegan enteras con su incremento.
- **Todo RESTRICT, ninguna CASCADE (R11, P115):** en Ako nada se borra, se *elimina* con `activo = false`; RESTRICT es el «no te dejo tirar esta ficha mientras otra apunte a ella».
- **Enum frente a texto libre (P113):** los tres sellos de goma de la caja (PENDIENTE, PAGADA, ANULADA); con enum, una errata como `PAGDA` la marca el compilador antes de ejecutar, y no la descubre el cliente. Room lo guarda como texto él solo desde la 2.3.0 (bici o coche costando lo mismo).
- **Precios en céntimos `Int`:** un ordenador hace 0,1 + 0,2 = 0,30000000000000004; con céntimos, 150 + 250 es siempre 400.
- **La línea congela nombre y precio (R14):** como un recibo ya impreso: si mañana el Agua sube a 1,80 €, el de ayer sigue diciendo 1,50 € (`precio_unitario_centimos`).
- **Lo calculable no se guarda (R3, R10):** la mesa no tiene casilla «ocupada» (una pizarra que miente el día que se olvida borrarla); está ocupada si tiene una comanda PENDIENTE.
- **Tabla puente (`producto_alergeno`):** muchos platos con muchos alérgenos; fichitas «plato + alérgeno» cuyo carné es la pareja.
- **Una sola base de datos con `by lazy` en `EntradaAko` (P116, P117):** la llave del almacén cuelga del local (la `Application`, que vive mientras la app), no del bolsillo de un camarero (una pantalla); `lazy` = «si no hay llave se hace una; si hay, se da esa», seguro aunque la pidan dos a la vez. Lo que más le llamó la atención a Daniel.
- **La precarga al arrancar (P16, P118):** la inauguración del bar, una sola vez; Room no sube la persiana hasta el primer cliente, así que `EntradaAko` la abre al arrancar y el local está montado antes de que entre nadie.

## Pruebas

- Pruebas de código: la S2 no tiene P-C; `testDebugUnitTest` en verde el 30 sep (solo la de ejemplo de la plantilla).
- Pruebas manuales: la S2 no tiene P-M. Entregable del spec comprobado: el inspector con las 7 tablas y la precarga (30 sep). RF-50 queda *implementado, no probado* hasta P-M-29 (S9); su paso de la tabla `etiqueta` se quita (P115).

## Uso de IA en esta sesión

- Repaso de la S1 y conceptos de base de datos: Claude explicó con ejemplos; Daniel contestó con sus palabras.
- Pieza 1 (paquetes): los creó Daniel en Android Studio; Claude quitó las carpetas vacías mal puestas y comprobó el disco.
- Pieza 2, decisión P113: Claude buscó las opciones (una en las notas de versión oficiales de Room) y Daniel eligió B con su propio argumento; Claude actualizó spec y guías.
- Alcance (P115 y reglas del prototipo): lo decidió Daniel; Claude propuso las opciones y un subagente de Claude ajustó spec y guías a 7 tablas (Claude lo revisó).
- Piezas 2 a 8 (enum, 7 entidades, `AppDatabase`, `EntradaAko`, `strings.xml`, DAOs, `Precarga`): Claude dio cada bloque explicado; **Daniel los creó y pegó en Android Studio**; Claude los comprobó leyendo el disco y compilando. Claude hizo los arreglos mecánicos de nombres y carpetas (problemas 3 y 4).
- Decisiones P116 (una sola base de datos), P117 (nombre `EntradaAko`, propuesto por Daniel) y P118 (abrir al arrancar): Claude dio 3 opciones cada una, con fuente oficial; eligió Daniel.
- Prueba final: Daniel ejecutó la app y abrió el Database Inspector; Claude desinstaló la app con `adb` y comprobó la base de datos por fuera.

## Siguiente sesión

- S3 (DAOs y dominio puro): repasar en dos minutos corrutina y `suspend`; los DAOs ya existen con sus `@Insert` de la precarga (P118) y se les añaden las consultas; `ComandaDao` nace en la S3.
