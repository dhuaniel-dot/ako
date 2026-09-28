> **Guía de la sesión 2 — 14 entidades, `Converters`, `AppDatabase`, `Precarga`.** Escrita en la sesión 00 (24 sep 2026) [Claude]. Es un plan de piezas, no código: el código se escribe pieza a pieza en la sesión, explicado antes y tecleado por Daniel. Objetivo del spec (apartado 11, S2): **compila y, al arrancar, el inspector de base de datos enseña las 14 tablas con Otros, 60 mesas, 14 alérgenos, 3 etiquetas y el ejemplo (Bebidas / Agua, P7).** Entregable: el inspector con las 14 tablas y la precarga (apartado 2).
> **Prerrequisitos:** S1 cerrada (proyecto compila y arranca; catálogo de versiones de `stack-verificado/`).
> **Horas estimadas: 5–7 h.** Chat con **Opus 5.5, esfuerzo medio**.

# Sesión 2 — guía

## 0. Al abrir (5 min)

- Chat nuevo sobre `C:\AKO`. «Empecemos la S2».
- Tener delante: `docs/spec-claude-code.md` apartado 5 (la tabla de las 14 tablas y las reglas 5.1) y `docs/decisiones-code.md` (P7, P16, P17).
- Conceptos que explico al abrir, en cinco minutos y sin código: qué es una tabla y una fila; qué es la clave primaria (el número de carné de una fila); qué es una clave foránea (una fila que apunta a otra) y qué significa `RESTRICT` (no se puede borrar lo apuntado) frente a `CASCADE` (si se borra lo apuntado, se borra lo que apunta); qué es un índice único; qué hace Room con una anotación (`@Entity` = «esta clase es una tabla»).

## 1. Piezas

Cada pieza sigue la regla 12 de `CLAUDE.md`: explicación breve → código completo con su ruta → Daniel lo teclea → `/verificar` → pregunta de comprensión. **Todas las rutas cuelgan de `app-ako/app/src/main/java/yunkang/ako/`** salvo que se diga otra cosa.

### Pieza 1 — Paquetes vacíos (10 min)

- **Qué:** crear las carpetas de código del apartado 3 del spec dentro de `yunkang.ako`: `datos/entidades`, `datos/dao`, `datos/repositorios`, `dominio/modelos`, `seguridad`, `imagenes`, `ui`.
- **Archivos:** ninguno, solo paquetes [Claude]. En Android Studio: clic derecho sobre `yunkang.ako` → New → Package.
- **Qué te explico antes:** un paquete es una carpeta con nombre; el nombre de arriba de cada archivo (`package …`) tiene que coincidir con la carpeta.
- **Qué comprobamos después:** las siete carpetas están bajo `yunkang.ako` [Claude].
- **Pregunta:** ¿por qué `dominio/` no puede tener nada de Android dentro?

### Pieza 2 — Las dos enumeraciones y `Converters` (20 min)

- **Qué:** las dos listas cerradas de valores y cómo se guardan en la base de datos [Claude].
- **Archivos:** `datos/entidades/EstadoComanda.kt` (PENDIENTE, PAGADA, ANULADA), `datos/entidades/TipoModificador.kt` (AÑADIR, QUITAR), `datos/entidades/Converters.kt` (dos `@TypeConverter` por enum: a texto y desde texto).
- **Qué te explico antes:** un `enum` es una lista cerrada de valores; SQLite no sabe qué es un enum, así que se guarda como texto para que en la base de datos se lea.
- **Qué comprobamos después:** compila.
- **Pregunta:** ¿qué pasaría si se guardara como número?

### Pieza 3 — `Categoria` y `Producto` (35 min)

- **Qué:** las tablas `categoria` y `producto` [Claude].
- **Archivos:** `datos/entidades/Categoria.kt` (`id`, `nombre`, `imagen?`, `orden: Int`, `activo`, `esPorDefecto`; `@Index(nombre, unique = true)`) y `Producto.kt` (`id`, `categoriaId`, `numero: Int`, `nombre`, `descripcion?`, `precioCentimos: Int`, `imagen?`, `imagenNutricional?`, `activo`, `disponible = true`; `@Index(numero, unique = true)`; FK → `categoria` RESTRICT). Columnas en `snake_case` con `@ColumnInfo`.
- **Qué te explico antes:** `data class` = una fila con sus campos; `?` = puede estar vacío; `precioCentimos` en `Int` y por qué (la coma flotante acumula error); `disponible` existe desde hoy y vale siempre `true` en nivel 1 (P153).
- **Qué comprobamos después:** compila.
- **Pregunta:** ¿cuál es la diferencia entre `activo = false` y `disponible = false`?

### Pieza 4 — Las tablas de la carta que el nivel 1 no usa (30 min)

- **Qué:** seis tablas de la carta que se crean hoy aunque el nivel 1 no las use [Claude].
- **Archivos:** `ProductoNutricion.kt` (PK = `productoId`, 1:1, siete `Int?`), `Modificador.kt`, `Etiqueta.kt` (`nombre` único), `ProductoEtiqueta.kt` (PK compuesta), `Idioma.kt` (`codigo` único), `ProductoTraduccion.kt` (PK compuesta). Todas con sus FK `RESTRICT`.
- **Qué te explico antes:** «las 14 tablas se crean desde el primer día aunque el nivel 1 no use todas» (spec 4); PK compuesta = dos columnas juntas son el carné.
- **Qué comprobamos después:** compila.
- **Pregunta:** ¿por qué `producto_traduccion` no tiene `id` propio?

### Pieza 5 — `Alergeno` y `ProductoAlergeno` (15 min)

- **Qué:** los alérgenos y su tabla puente con los platos [Claude].
- **Archivos:** `Alergeno.kt` (`id`, `nombre`), `ProductoAlergeno.kt` (PK compuesta `productoId` + `alergenoId`, FKs RESTRICT).
- **Qué te explico antes:** relación N:M = una tabla puente.
- **Qué comprobamos después:** compila [Claude].
- **Pregunta:** ¿qué se borra cuando desmarco un alérgeno de un plato, y por qué eso sí está permitido (R5)?

### Pieza 6 — `Mesa`, `Comanda`, `LineaComanda`, `LineaModificador` (35 min)

- **Qué:** las tablas de la sala y de los pedidos [Claude].
- **Archivos:** `Mesa.kt` (`id`, `numero: Int` único; sin `activo` ni estado), `Comanda.kt` (`mesaId`, `estado: EstadoComanda`, `fechaCreacion: Long`, `fechaCierre: Long?`; FK RESTRICT), `LineaComanda.kt` (`comandaId`, `productoId`, `cantidad: Int`, `precioUnitarioCentimos: Int`, `nombreProducto`; dos FK RESTRICT), `LineaModificador.kt` (`lineaComandaId`, `modificadorId`, `precioCentimos: Int`, `nombreModificador`, `cantidad: Int`; FK → `linea_comanda` **CASCADE**, la única; FK → `modificador` RESTRICT).
- **Qué te explico antes:** R3 (la mesa no tiene estado: se calcula), R14 (la línea congela nombre y precio), R11 (por qué esa única CASCADE), fechas como instante `Long`.
- **Qué comprobamos después:** compila [Claude].
- **Pregunta:** ¿por qué `Mesa` no tiene columna `ocupada`?

### Pieza 7 — `AppDatabase` (25 min)

- **Qué:** la clase que junta las 14 entidades y da la única instancia de la base de datos [Claude].
- **Archivo:** `datos/AppDatabase.kt` — `@Database(entities = [las 14], version = 1, exportSchema = false)`, `@TypeConverters(Converters::class)`, cinco funciones abstractas para los DAOs (`categoriaDao()`, `productoDao()`, `precargadosDao()`, `mesaDao()`, `comandaDao()`, como en `spec+doc-clases.md`; **los DAOs todavía no existen**: en esta sesión se declara solo `abstract class AppDatabase : RoomDatabase()` con los DAOs comentados, o se crean cinco interfaces `@Dao` vacías que la S3 rellenará [Claude: lo segundo, para que el inspector funcione hoy]); `companion object` con la única instancia y la función que la da, `obtener(context)` [Claude] (`Room.databaseBuilder(...).addCallback(Precarga(...)).build()`), base de datos `ako.db`. Ese nombre lo usan la pieza 8 y la S11.
- **Qué te explico antes:** «una única instancia» (abrir la base de datos es caro); qué es `RoomDatabase.Callback`.
- **Qué comprobamos después:** compila (Room genera `AppDatabase_Impl` con KSP; si falla, el error dice qué entidad está mal).
- **Pregunta:** ¿dónde vive la base de datos en el móvil y qué pasa al desinstalar?

### Pieza 8 — `Ako : Application` y las cadenas de la precarga (25 min)

- **Qué:** las cadenas que necesita la precarga y el objeto que guarda la base de datos [Claude, decisión P17].
- **Archivos:** primero, en `res/values/strings.xml`, el bloque *precarga* de `docs/guias/strings-es-borrador.xml` (`alergeno_01_gluten` … `alergeno_14_moluscos`, `etiqueta_vegano`, `etiqueta_vegetariano`, `etiqueta_pescetariano`, `precarga_categoria_por_defecto`, `precarga_categoria_ejemplo`, `precarga_plato_ejemplo`): los usa la pieza 9 para no escribir ningún nombre en el código (RNF-19). Después, `Ako.kt` en la raíz del paquete: `class Ako : Application()` con `val db by lazy { AppDatabase.obtener(this) }` y, más adelante (S4), los repositorios. Registrarla en `AndroidManifest.xml` (`android:name=".Ako"`).
- **Qué te explico antes:** qué es un recurso y por qué la clave lleva el número de orden; `Application` es el objeto que vive mientras la app vive; es el sitio natural para «lo único».
- **Qué comprobamos después:** la app arranca.
- **Pregunta:** ¿por qué la base de datos se guarda en `Ako` y no en una pantalla? [Claude]

### Pieza 9 — `Precarga` (40 min)

- **Qué:** la precarga (decisión P16). `cargar` inserta: **Otros** (`esPorDefecto = true`, `orden` alto o el criterio de «siempre la última» lo aplica el código, no el `orden`), **60 mesas** (1–60), **14 alérgenos** (nombres del inventario `docs/textos-ui.md`, apartado 9, en ese orden; los nombres salen de `strings.xml`, pieza 8 [Claude]), **3 etiquetas** (Vegano, Vegetariano, Pescetariano), categoría **Bebidas** (`orden` = 1) con el plato **1 · Agua · 1,50 €** (150 céntimos). Otros se inserta **antes** que cualquier plato.
- **Archivo:** `datos/Precarga.kt` — `class Precarga(context: Context) : RoomDatabase.Callback()` en dos partes [Claude, para que la S11 no tenga que rehacerla]: `suspend fun cargar(db: AppDatabase)` hace todo el trabajo con los DAOs **de la base de datos que recibe** (por ahora, `@Insert` mínimos en los DAOs vacíos de la pieza 7), y `onCreate` solo lanza una corrutina en `Dispatchers.IO` que llama a `cargar(AppDatabase.obtener(context))` (P16 intacto: `Callback`, corrutina, DAOs, nada de `execSQL`).
- **Qué te explico antes:** qué es una corrutina en dos frases (trabajo que se hace aparte sin bloquear la pantalla); por qué la precarga no puede usar SQL escrito a mano (RNF-08); por qué `cargar` recibe la base de datos en vez de pedir la instancia única (en la S11 las pruebas de Room la llaman con una base de datos en memoria, sin el `Callback`, y esperan a que termine; `sesion-11.md`, pieza 3).
- **Qué comprobamos después:** desinstalar la app del emulador (para que `onCreate` vuelva a saltar), Run ▶, y **Database Inspector** (Daniel) o `adb exec-out run-as yunkang.ako cat databases/ako.db` (Claude, `verificar` apartado 4): 14 tablas, 1 + 1 categorías, 1 plato, 60 mesas, 14 alérgenos, 3 etiquetas.
- **Pregunta:** ¿por qué hay que desinstalar la app para volver a ver la precarga?

### Pieza 10 — Cierre (20 min [Claude])

- **Qué:** commit intermedio tras la pieza 7 (`S2: entidades y AppDatabase`), commit de cierre `S2: 14 entidades, Converters, AppDatabase y Precarga` y después `S2: ficha del diario`, con push (dos commits al cerrar, `decisiones-code.md` 4c).
- **Archivo:** la ficha del diario [Claude], con «qué entendí» en tus palabras.
- **Qué te explico antes:** qué guarda cada commit (el intermedio, el de cierre y el de la ficha) [Claude].
- **Qué comprobamos después:** los commits están hechos y subidos con push [Claude].
- **Pregunta:** ¿por qué la ficha del diario va en un commit aparte del código? [Claude]

## 2. Pruebas que cierran la sesión

Ninguna P-M ni P-C: el entregable es el inspector con las 14 tablas y la precarga. `estado-nivel.md`: RF-50 pasa a `implementado, no probado` (P-M-29 se ejecuta entera en la S9).

## 3. Lo que tienes que saber defender

- Las 14 tablas por grupos (carta 10, sala 1, pedidos 3) y qué guarda cada una.
- R11: RESTRICT en todo salvo `linea_modificador → linea_comanda`, y por qué.
- R16: la categoría por defecto se reconoce por `esPorDefecto`, nunca por el nombre.
- Por qué no hay `CHECK` en ninguna entidad (Room no lo declara; R8 vive en `Validacion`, S3).
- Qué es la precarga y por qué Otros se crea antes que cualquier plato.

## 4. Riesgos típicos y qué hacer

- **KSP falla con un mensaje largo:** casi siempre es una FK que apunta a una columna que no existe o un `@ColumnInfo` mal escrito; el error nombra la entidad.
- **La precarga no aparece:** `onCreate` solo salta la primera vez que se crea el archivo; desinstalar la app del emulador (mantener pulsado el icono → App info → Uninstall, o `adb uninstall yunkang.ako`).
- **«Cannot access database on the main thread»:** algo llamó a Room desde la pantalla; en esta sesión solo la corrutina de `Precarga` toca la base de datos.
- **Nombres:** todo en español salvo lo estándar de Android (`onCreate`, `Dao`, `Database`).
