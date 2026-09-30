# Sesión 03 — 2026-09-30

| Campo | Valor |
|---|---|
| **Fecha** | 2026-09-30 |
| **Sesión nº** | 03 |
| **Objetivo de la sesión** | Del spec: **DAOs y dominio puro**: los 5 DAOs con sus consultas; `Carrito`, `LineaCarrito`, `Calculadora`, `Validacion`, `Hash`. Compila y pasan P-C-01 a P-C-05 y P-C-09 (parte de `Validacion`) en `test/` |
| **Tiempo dedicado** | 20:41 – 22:30, unas 1 h 50 min, sin pausas (confirmado por Daniel) |
| **Nivel / pieza** | Nivel 1 · capa de datos (DAOs) y dominio puro |
| **Commit final** | `a8edbf7` — S3: DAOs y dominio puro (antes, intermedio `0f58b2b` — S3: dominio puro y seis pruebas) |

## Qué se hizo

- Repaso de corrutina y `suspend` (pendiente de la S2) y de por qué `insertar` devuelve el `id`.
- **Dominio puro** (`dominio/`, todos `object` salvo el carrito): `LineaCarrito` (P119, `var cantidad`), `Carrito` (suma líneas idénticas, tope 99 con aviso `false`, P74), `Calculadora` (`importe` y `cambio`, P120), `Validacion` (`MAXIMO_POR_PLATO`, `precioValido`, `cantidadValida`, `pinValido`; P11, P121), `Hash` (sal + PBKDF2WithHmacSHA256, 100 000 vueltas, 256 bits, textos Base64; P122).
- **Pruebas en `test/`**: `CarritoTest` (P-C-01, 02, 03), `CalculadoraTest` (P-C-04), `HashTest` (P-C-05, 4 pruebas), `ValidacionTest` (P-C-09 parte pura + 2 [Claude]): 12 en verde con la de ejemplo. Commit intermedio `0f58b2b`.
- **Modelos de consulta** (`dominio/modelos/`, sin Room): `MesaConTotal`, `MesaEstado`, `ComandaConTotal` (P123, P126).
- **DAOs**: `CategoriaDao` (+ `actualizar`, `todas`, `porDefecto`, `existeNombre` con `COLLATE NOCASE`, P124), `PrecargadosDao` (+ `alergenos`), `ProductoDao` (`porId`, `porCategoria`, `visibles` con JOIN R15, `hayAlgunoVisible`, `existeNumero` R9, `alergenosDe`, `guardarAlergenos` con `@Transaction`, P125), `MesaDao` (+ `todas`), `ComandaDao` nuevo (12 consultas: R1, R3/R10 con `SUM`/`GROUP BY`, R6, R7, Resumen de ingresos) y enchufado a `AppDatabase`.
- Forma de trabajar: apartado «Para el vídeo» en cada ficha y en la plantilla; archivos nuevos sin línea `package` y con la forma corta de crearlos.
- Idea de Daniel apuntada para el nivel 2: dividir la cuenta.
- Nada a medias.

## Problemas y soluciones

| # | Qué falló | Cómo se resolvió | Justificación (por qué esta solución y no otra) | Fuente |
|---|---|---|---|---|
| 1 | Pieza 1: `Carrito.kt` y `LineaCarrito.kt` quedaron en `dominio/modelos/` y con dos líneas `package` (la que pone Android Studio, `…dominio.modelos`, y la del bloque, `…dominio`) | Claude movió los dos archivos a `dominio/` y quitó la línea `package` de Android Studio; compila | Android Studio junta en una línea (`dominio.modelos`) los paquetes que no tienen archivos propios (*Compact Middle Packages*): clic derecho sobre esa línea crea el archivo en el paquete de más adentro. Se desmarca en ⋮ → *Appearance* → *Compact Middle Packages* | Propio |
| 2 | Pieza 2: `CarritoTest` no compilaba: «Syntax error: Expecting a top level declaration» e «imports are only allowed in the beginning of file» (línea 3) | Claude quitó la primera línea `package` repetida; las 3 pruebas pasan | Al crear un *Kotlin File* en un paquete, Android Studio ya escribe la línea `package`; al pegar el bloque (que trae la suya) quedan dos. Mismo origen que el problema 1. Para las siguientes: Ctrl+A antes de pegar, así el bloque sustituye todo. **Volvió a pasar en la pieza 3** (`Calculadora.kt` y `CalculadoraTest.kt`): esta vez lo arregló Daniel borrando las líneas repetidas (Ctrl+Y); según Daniel, al pegar salía un aviso («mirror») | Propio |
| 3 | El commit intermedio `0f58b2b` salió **sin** la línea `Co-Authored-By: Claude` (P111). Antes, el hook bloqueó el primer intento («tiene que empezar por S<N>:») porque el mensaje iba por `-F -` (heredoc) y el hook no lo lee | Se dejó así (`--amend` está prohibido) y se anota aquí; los siguientes commits llevan la línea con un `-m` aparte | El ajuste `attribution` solo la añade cuando el commit lo arma Claude Code con su formato; con `git commit -m` a mano hay que escribirla. Reescribir el commit exigiría `--amend`/`push --force`, prohibidos | Propio |
| 4 | Al plantear P125, Claude dio como pega de la opción B que `withTransaction` necesitaba `room-ktx`, «fuera del spec»; era falso: `room-ktx` ya está en `build.gradle.kts` desde la S1 (lo vio el agente que repasó las guías al cerrar) | Corregido en `decisiones-code.md` (P125) y dicho a Daniel; A se mantiene salvo que Daniel quiera revisarla en la S4 | Una opción no puede llevar una pega inventada (P112 exige ventajas y pegas reales); antes de decir que algo falta, mirar `build.gradle.kts` | Propio |

## Qué entendí y qué no

- **Entendí:** **`suspend` y corrutina** (repaso al abrir, palabras de Daniel): «`suspend` es como señalar que algo tarda y solo puede entrar la corrutina; la corrutina es como una aparición que se ocupa de las cosas que tardan y se lo da al hilo principal: el hilo principal habla con los clientes y mientras ha invocado algo para que le dé la comida a ese cliente». Matiz: `suspend` dice «*puede* tardar», no «tarda mucho»; y para crear la corrutina desde un botón hace falta `launch` (S4–S5).
- **Carrito** (a): «un sitio donde añadimos los platos; hoy hemos puesto el límite, que es 99; y con *lazy* se nos puede complicar, así que hay que tener anotaciones para la sesión 8». Matiz: no es `lazy` (eso fue la base de datos en la S2) sino **`var`** (el renglón «a lápiz», P119); la nota para la S8 es correcta.
- **Calculadora** (c): «así es menos lío que tener que hacerlo como dos calculadoras».
- **`Validacion`** (d): «es como un check que hace que veamos que las cosas estén bien». Falta el porqué: Room no admite `CHECK`, por eso lo hace el código.
- **PIN** (e): «hemos troceado el PIN y ya no podemos rehacerlo» (picado = hash; sin llave para volver atrás).
- **Modelo de consulta** (f): «un módulo que consulta el estado de la mesa». Matiz: no consulta nada; es la ficha que rellenan la base de datos (`MesaConTotal`) o el repositorio (`MesaEstado`).
- **JOIN y transacción** (h): «junta un plato con una categoría; la transacción era lo de poner los alérgenos y cambiarlos luego». Falta: la transacción hace los dos pasos juntos o ninguno.
- Durante la sesión, bien: el cambio negativo («lo que hay que pedir de más», tras corregirlo), `pendienteDeMesa` con una comanda PAGADA («nada, porque ya está pagada»), el tope a 98 pone roja la prueba 3, `visibles()` no enseña platos de una categoría eliminada.
- **No entendí todavía** (palabras de Daniel): **las pruebas con JUnit** (b: «ni idea; creo que no me has explicado lo de los test»), **`@Query` y lo que comprueba Room** (g: «era algo de las tablas, no me acuerdo»), **`SUM` + `GROUP BY`** (i: «ni idea») y, en general, **las palabras clave del código** (`val`/`var`, `fun`, `object`, `suspend`, `@Query`…). Pide que los PDF lo expliquen mucho y simple, con ejemplos de la vida real. **Se repasan al abrir la S4, antes de escribir código encima** (la S4 usa DAOs y pruebas).

## Para el vídeo

- **Corrutina y `suspend`:** el hilo principal es el camarero de la barra, que no puede quedarse parado; la corrutina es el ayudante que va a la cocina (base de datos); `suspend` es el cartel «puede tardar: solo personal de cocina».
- **El carrito no se guarda** (R10): es la libreta del camarero hasta *Enviar*; líneas idénticas se suman; tope de 99 por plato (R4), escrito en un solo sitio (`Validacion.MAXIMO_POR_PLATO`, P121).
- **Renglón «a lápiz» (P119):** se eligió `var cantidad` por sencillez; el riesgo es un despiste al programar, no un ataque (el carrito vive en memoria y solo lo toca nuestro código).
- **Dominio puro y pruebas en el PC:** `Carrito`, `Calculadora`, `Validacion` y `Hash` no usan nada de Android, así que JUnit las examina en el PC en segundos, sin emulador. Una prueba = preparar, actuar, comprobar (`assertEquals`).
- **Calculadora (P120):** la multiplicación precio × cantidad está escrita una sola vez y la usan el carrito y el recibo; el cambio negativo no es un error, significa «faltan X €».
- **Validación del precio (R8, P11):** Room no admite reglas `CHECK`, así que `Validacion.precioValido` para en seco (excepción) antes de guardar un precio negativo.
- **El PIN (P122):** no se cifra, se pica (hash): picadora + especias al azar (sal) + 100 000 vueltas (PBKDF2). No hay llave para volver atrás: se comprueba picando otra vez. Límite honesto: 10 000 combinaciones; la protección real es que el archivo es privado. `minSdk 26` porque desde ahí Android trae `PBKDF2WithHmacSHA256` y `java.util.Base64`.
- **El total no se guarda, se calcula** (regla del proyecto): la rejilla junta mesas y totales en dos pasos sencillos (P123).
- **SQL y consultas parametrizadas (RNF-08):** `@Query` es la comanda en el idioma de la base de datos; `:nombre` es una casilla de una comanda impresa: el dato entra aparte y nadie puede reescribir la orden (inyección SQL). **Room revisa cada consulta al compilar** (tablas, columnas, que el SQL esté bien escrito y que encaje con lo que devuelve la función): el error sale en Android Studio, no en el móvil.
- **Nombre de categoría repetido (P124):** «Carnes» = «carnes» con `COLLATE NOCASE`; límite honesto: las letras con tilde o ñ no las pliega.
- **El JOIN y los platos visibles (R15):** cruzar la lista de platos con la de categorías y quedarse con los que tienen las dos cosas «en la carta»; la misma tabla con dos gafas: la carta ve `visibles()`, el Panel ve todo con `porCategoria()` para poder recuperar lo eliminado.
- **Transacción (P125):** guardar los alérgenos son dos pasos (borrar marcas viejas y poner las nuevas); con `@Transaction` van juntos, como un pago con tarjeta: o se cobra entero o nada. Sin ella, un fallo a mitad dejaría un plato sin alérgenos, grave para un alérgico.
- **`SUM` + `GROUP BY` (R3, R10):** el total de la mesa no se guarda: el camarero junta los renglones de la comanda y suma cantidad × precio cada vez. Los alias `AS` ponen a cada columna el nombre de la casilla del modelo.
- **Dónde se hace cada cuenta (P126):** el Resumen de ingresos sigue el diagrama y suma en Kotlin con `Calculadora.importe`; con 50–150 comandas al día, una consulta por comanda no se nota. Elegir por claridad, no por una velocidad que no importa.
- **R1 en una consulta:** `pendienteDeMesa` solo mira comandas PENDIENTE; una mesa con la cuenta pagada está libre.

## Pruebas

- Pruebas de código que pasan al terminar (30 sep): **P-C-01, P-C-02, P-C-03, P-C-04, P-C-05 y P-C-09** (parte de `Validacion`; la del repositorio con DAO falso, en la S4). Apuntadas con fecha en `spec+doc-pruebas.md`.
- Pruebas manuales: la S3 no tiene P-M. `estado-nivel.md`: ningún RF cambia de estado (la S3 es capa de datos y dominio; la lógica del carrito de RF-36 existe, su pantalla es de la S8).

## Uso de IA en esta sesión

- Repaso de `suspend`: Claude explicó con el ejemplo del bar; Daniel lo resumió con sus palabras.
- Decisiones P119–P126: Claude dio 3 opciones cada una (al menos una de fuente oficial con enlace) y **eligió Daniel**, con su motivo (en `decisiones-code.md` 5.5). La idea de dividir la cuenta es de Daniel.
- Piezas 1–9 (dominio, pruebas, modelos, DAOs): Claude dio cada bloque explicado; **Daniel creó los archivos y pegó el código en Android Studio**, y ejecutó las pruebas; Claude comprobó leyendo el disco, compilando y pasando las pruebas por consola. Antes de dar `ProductoDao` y `ComandaDao`, Claude compiló el borrador en el proyecto y lo deshizo, para comprobar que Room aceptaba las consultas.
- Arreglos mecánicos de Claude: mover `Carrito` y `LineaCarrito` de `modelos/` a `dominio/`, quitar líneas `package` repetidas (piezas 1–2; en la 3 y la 4 las quitó Daniel), quitar una `b` colada en `Calculadora.kt`. Daniel corrigió los bloques cruzados de `CategoriaDao`/`PrecargadosDao` y el `import` que faltaba en `MesaDao`.
- Otro chat de Claude (abierto por Daniel) actualizó en paralelo las guías S4–S13 con P119–P123; no forma parte de los commits de esta sesión.

## Siguiente sesión

- Al abrir la S4, antes del código: repasar **pruebas con JUnit**, **`@Query`** y lo que comprueba Room, **`SUM` + `GROUP BY`** y las palabras clave del código (lo que Daniel no entendió en la S3).
- S4 (repositorios y `PinStore`), en Plan Mode y con esfuerzo alto: cerrar los 5 huecos de la guía con 3 opciones cada uno; aviso de P119 para la S8 (el renglón cambiado del carrito es el mismo objeto). Recomendado partirla en dos tandas (piezas 1–7 y 8–12).
- **Idea de Daniel para el nivel 2 (30 sep): dividir la cuenta** — botón *Dividir* al cobrar; se eligen platos y se pasan «al otro lado» (una mesa con 3 aguas: una a otra cuenta) y se calcula lo de cada parte. Apuntada en `decisiones-code.md` (5.5 y «Para el Project») para que el Project la diseñe como nivel 2.
