> **Guía de la sesión 3 — DAOs y dominio puro.** Escrita en la sesión 00 (24 sep 2026) [Claude]. Plan de piezas, no código. Objetivo del spec (apartado 11, S3): los 5 DAOs con sus consultas; `Carrito`, `LineaCarrito`, `Calculadora`, `Validacion`, `Hash`; compila y **las seis pruebas puras pasan** (P-C-01 a P-C-05 y P-C-09) en `test/`. Entregable: compila y esas seis pruebas en verde (apartado 2).
> **Prerrequisitos:** S2 cerrada.
> **Horas estimadas: 7–9 h.** Chat con **Opus 5.5, esfuerzo medio**.

# Sesión 3 — guía

## 0. Al abrir (5 min)

- Chat nuevo sobre `C:\AKO`. «Empecemos la S3».
- Tener delante: `docs/spec-claude-code.md` apartados 3, 6 y 12; `docs/spec+doc-clases.md` 3.2 y 3.3 (los nombres de métodos); `docs/spec+doc-pruebas.md` apartado 4; `docs/decisiones-code.md` (P11, P12, D11).
- Conceptos al abrir, sin código: qué es SQL (la lengua para pedir cosas a la base de datos) y qué es una consulta parametrizada (`:parametro`: el dato entra por una puerta aparte, nunca pegado al texto, RNF-08); qué es un JOIN (cruzar dos tablas); qué es una función pura (entra un dato, sale un dato, no toca nada más) y por qué se prueba sin emulador; qué es JUnit (un programa que ejecuta tus pruebas y pinta verde o rojo).

## 1. Piezas

Cada pieza sigue la regla 12 de `CLAUDE.md`: explicación breve → código completo con su ruta → Daniel lo teclea → `/verificar` → pregunta de comprensión. **Todas las rutas cuelgan de `app-ako/app/src/main/java/yunkang/ako/`** salvo que se diga otra cosa; los `…Test.kt` van en `app/src/test/java/yunkang/ako/`, como en la pieza 2 [Claude].

**Parte A: dominio puro y sus pruebas (primero, porque no dependen de nada).**

### Pieza 1 — `LineaCarrito` y `Carrito` (35 min)

- **Qué:** las dos clases del carrito [Claude]; R4 y R10.
- **Archivos:** `dominio/LineaCarrito.kt` (`producto`, `cantidad`, `importe()` = precio × cantidad) y `dominio/Carrito.kt` (`mesaId`, `lineas`; `anadir` suma en la misma línea si el plato ya está y **topa en 99**; `cambiarCantidad` (1–99); `quitar`; `numPlatos`; `total`; `estaVacio`).
- **Qué te explico antes:** el carrito vive en memoria y no se guarda (R10); «líneas idénticas se suman» (ficha 5).
- **Qué comprobamos después:** compila [Claude].
- **Pregunta:** ¿por qué `Carrito` no importa nada de Room ni de Android?

### Pieza 2 — P-C-01, P-C-02, P-C-03 (30 min)

- **Qué:** las tres pruebas del carrito [Claude].
- **Archivo:** `app/src/test/java/yunkang/ako/dominio/CarritoTest.kt` — importe 1850 × 3 = 5550; carrito vacío (total 0, `estaVacio`, `numPlatos` 0) y con 2 × 1850 + 1 × 500 = 4200 y `numPlatos` 3; añadir Entrecot × 2 y × 1 → una línea con 3; `cambiarCantidad` a 99 y añadir 1 → 99.
- **Qué te explico antes:** anatomía de una prueba (preparar → actuar → comprobar con `assertEquals`); cómo se ejecuta con el triángulo verde.
- **Qué comprobamos después:** tres verdes.
- **Pregunta:** si cambio el tope a 98, ¿qué prueba se pone roja y por qué?

### Pieza 3 — `Calculadora` y P-C-04 (20 min)

- **Qué:** las cuentas de importe, total y cambio, y su prueba [Claude].
- **Archivos:** `dominio/Calculadora.kt` — `importe(precio, cantidad)`, `total(lineas)`, `cambio(totalCentimos, entregadoCentimos)` = entregado − total, puede ser negativo, no lanza nada. `CalculadoraTest.kt`: 5000 → 800; 4000 → −200; 4200 → 0.
- **Qué te explico antes:** qué hace cada una de las tres funciones [Claude].
- **Qué comprobamos después:** P-C-04 en verde [Claude].
- **Pregunta:** ¿por qué el cambio negativo no es un error?

### Pieza 4 — `Validacion` y P-C-09 (25 min)

- **Qué:** las validaciones del dominio y la primera parte de P-C-09 [Claude] (decisión P11). La parte de P-C-09 que usa `CartaRepository` con un DAO falso se completa en la S4.
- **Archivos:** `dominio/Validacion.kt` — `precioValido(centimos)` (lanza `IllegalArgumentException` si es negativo; 0 vale), `cantidadValida(n)` (1–99), `pinValido(pin)` (cuatro cifras). `ValidacionTest.kt`: 1850 y 0 válidos; −1 lanza (`assertThrows`).
- **Qué te explico antes:** R8 vive aquí porque Room no declara `CHECK` (verificación 9); qué es una excepción (parar en seco con un mensaje).
- **Qué comprobamos después:** P-C-09, parte de `Validacion`, en verde [Claude].
- **Pregunta:** ¿dónde más se llama a `precioValido` (spec 5.1)?

### Pieza 5 — `Hash` y P-C-05 (35 min)

- **Qué:** el hash del PIN y su prueba (decisión P12) [Claude].
- **Archivos:** `dominio/Hash.kt` — `generarSal()` (16 bytes, `SecureRandom`), `pbkdf2(pin, sal)` (`PBKDF2withHmacSHA256`, 100 000 iteraciones, 256 bits, `javax.crypto`), `coincide(pin, sal, hash)`. `HashTest.kt`: «1234» con la misma sal coincide; «1235» no; «1234» con otra sal da otro hash; el resultado en Base64 no contiene «1234».
- **Qué te explico antes:** sal, iteraciones y hash con una metáfora (moler el PIN con un puñado de arena distinto cada vez, cien mil vueltas: se puede comprobar, no deshacer); el límite honesto (10 000 combinaciones; la protección real es que el archivo es privado).
- **Qué comprobamos después:** verde; el test tarda unos segundos por las 100 000 iteraciones y está bien que tarde.
- **Pregunta:** ¿por qué `Hash` está en `dominio/` y `PinStore` en `seguridad/` (P237)?

**Parte B: los DAOs.**

### Pieza 6 — Modelos de consulta (20 min)

- **Qué:** tres modelos que juntan datos de varias tablas [Claude].
- **Archivos:** `dominio/modelos/MesaConTotal.kt` (`mesaId`, `numero`, `comandaId`, `totalCentimos`; decisión D11), `MesaEstado.kt` (`mesa: Mesa`, `comandaId: Long?`, `totalCentimos: Int?`, del diagrama: una celda de la rejilla; la construye `ComandaRepository.mesasConEstado` en la S4 y la usan la S8 y la S9) y `ComandaConTotal.kt` (`comandaId`, `mesaNumero`, `fechaCierre`, `totalCentimos` [Claude]: una fila del Resumen de ingresos con mesa, hora e importe; la rellena `resumenDelDia` en la S4 y la pinta la S10).
- **Qué te explico antes:** un DAO puede devolver «una fila inventada» que junta datos de varias tablas; `MesaEstado` y `ComandaConTotal` son modelos para la pantalla que monta el repositorio.
- **Qué comprobamos después:** compila [Claude].
- **Pregunta:** ¿quién rellena `MesaConTotal` y quién monta `MesaEstado`? [Claude]

### Pieza 7 — `CategoriaDao` y `PrecargadosDao` (25 min)

- **Qué:** las consultas de categorías y de los datos precargados [Claude].
- **Archivos:** `datos/dao/CategoriaDao.kt` — `insertar`, `actualizar`, `todas()` (ordenadas por `orden`; la regla «Otros la última» la aplica el repositorio, no el SQL), `porDefecto()`, `existeNombre(nombre, exceptoId)`. `PrecargadosDao.kt`: `alergenos()`, `etiquetas()`, `insertarAlergenos`, `insertarEtiquetas`. Todas `suspend` salvo lo que devuelva `LiveData`/`Flow` [Claude: `suspend` para todo en S3; cómo se refrescan las listas se decide en el Plan Mode de la S6, con la primera lista (hueco 3)].
- **Qué te explico antes:** `@Query` con `:parametro`; `suspend` = «puede tardar, se llama desde una corrutina».
- **Qué comprobamos después:** compila (Room valida el SQL al compilar: un error de columna sale aquí, no en el móvil).
- **Pregunta:** ¿qué comprueba Room al compilar que otras librerías no comprueban?

### Pieza 8 — `ProductoDao` (35 min)

- **Qué:** las consultas de platos, con la de plato visible (R15) y la de número repetido (R9) [Claude].
- **Archivo:** `datos/dao/ProductoDao.kt` [Claude: la ruta, como en la pieza 7] — `insertar`, `actualizar`, `porId`, `porCategoria(categoriaId)` (por número), **`visibles()`** (`activo = 1 AND disponible = 1` JOIN `categoria.activo = 1`, R15), `hayAlgunoVisible()`, **`existeNumero(numero, exceptoId)`** (R9), `alergenosDe(productoId)`, `guardarAlergenos(productoId, ids)` (`@Transaction`: borrar las relaciones del plato e insertar las nuevas).
- **Qué te explico antes:** el JOIN de visibles con un dibujo de dos tablas; por qué el Panel usa `porCategoria` (ve todos) y la carta `visibles()`.
- **Qué comprobamos después:** compila [Claude].
- **Pregunta:** ¿un plato activo de una categoría eliminada aparece en `visibles()`?

### Pieza 9 — `MesaDao` y `ComandaDao` (40 min)

- **Qué:** las consultas de mesas y comandas (R1, R3, R6, R10 y el Resumen de ingresos) [Claude].
- **Archivos:** `MesaDao.kt` — `todas()`, `insertarTodas`. `ComandaDao.kt`: `insertar`, `actualizar`, `porId`, **`pendienteDeMesa(mesaId)`** (R1), **`pendientesConTotal()`** (→ `MesaConTotal`: JOIN con `SUM(cantidad × precioUnitarioCentimos)`, R3 y R10), `lineasDe`, `insertarLineas`, `borrarLinea`, `contarLineas`, **`pagadasEntre(inicio, fin)`** (Resumen de ingresos), `mesasConProductoPendiente(productoId)`, `mesasConCategoriaPendiente(categoriaId)` (R6).
- **Qué te explico antes:** `SUM` y `GROUP BY` con el ejemplo de la rejilla; por qué el total no se guarda.
- **Qué comprobamos después:** compila; Room valida cada consulta.
- **Pregunta:** ¿qué devuelve `pendienteDeMesa` para una mesa con una comanda PAGADA?

### Pieza 10 — Cierre (20 min [Claude])

- **Qué:** commit intermedio tras la parte A (`S3: dominio puro y seis pruebas`), commit de cierre `S3: DAOs y dominio puro` y después `S3: ficha del diario`, con push (dos commits al cerrar, `decisiones-code.md` 4c).
- **Archivo:** la ficha del diario [Claude], con «qué entendí».
- **Qué te explico antes:** qué guarda cada commit (el intermedio, el de cierre y el de la ficha) [Claude].
- **Qué comprobamos después:** los commits están hechos y subidos con push [Claude].
- **Pregunta:** ¿por qué la ficha del diario va en un commit aparte del código? [Claude]

## 2. Pruebas que cierran la sesión

**P-C-01, P-C-02, P-C-03, P-C-04, P-C-05, P-C-09** (esta última en su parte de `Validacion`; la de `CartaRepository` con DAO falso, en la S4) en verde, con fecha en `spec+doc-pruebas.md`.

## 3. Lo que tienes que saber defender (es la sesión más útil para el vídeo)

- Qué es una consulta parametrizada y por qué nunca hay SQL concatenado (RNF-08).
- La consulta de plato visible (R15) explicada en una frase.
- R9 vive en la base de datos (`UNIQUE`) y `existeNumero` solo avisa antes; R1 no puede vivir en la base de datos.
- Por qué `Carrito`, `Calculadora`, `Validacion` y `Hash` son «puras» y se prueban en el PC en segundos.
- Cómo se guarda el PIN (sal + PBKDF2) y por qué no se puede recuperar.

## 4. Riesgos típicos y qué hacer

- **Room: «Not sure how to convert a Cursor to this method's return type»:** los nombres de las columnas del `SELECT` no coinciden con los campos del modelo (`MesaConTotal`); se usan alias `AS`.
- **Un test no aparece con el triángulo verde:** falta `@Test` o la clase no está en `src/test`.
- **`assertThrows` no compila:** JUnit 4.13 lo tiene; comprobar el `import org.junit.Assert.assertThrows`.
- **El test de `Hash` tarda:** normal (100 000 iteraciones); no bajar las iteraciones para que vaya rápido.
