# Sesión 04 — 2026-10-01

| Campo | Valor |
|---|---|
| **Fecha** | 2026-10-01 |
| **Sesión nº** | 04 |
| **Objetivo de la sesión** | Del spec: **Repositorios**: `CartaRepository`, `ComandaRepository`, `SeguridadRepository`, `PinStore`. Compila; cada método tiene un comentario de una línea con la regla que garantiza |
| **Tiempo dedicado** | 12:11 – 14:10, unas 2 h, sin pausas (confirmado por Daniel) |
| **Nivel / pieza** | Nivel 1 · capa de datos (repositorios) y seguridad (`PinStore`) |
| **Commit final** | `296078a` — S4: repositorios y PinStore (antes, intermedios `3f246d2` y `15647c6`) |

## Qué se hizo

- **Plan Mode (12:11–):** plan de 13 piezas en dos tandas con los nombres reales de los DAOs. Decisiones (detalle en `decisiones-code.md` 5.6): P127 → B (Daniel eligió B entre A/B/C: la carta pregunta a `ComandaRepository`), P128 → B (eligió A y cambió a B: `eliminar…` no devuelve nada), P129 → A (`withTransaction`), P130 → A (parámetro `aunqueCategoriaEliminada`), P131 → A (`totalDe` en SQL), P132 → C (`ComandaRepository` como interfaz, para que la prueba pueda fingirlo), P133 → B (`ComandaRepositoryReal` / `ComandaRepositoryFalso`).
- El repaso de lo no entendido en la S3 pasa al final de la sesión (Daniel). Al llegar, se dio el repaso 1 (palabras clave, tabla con ejemplos del bar) y Daniel pidió **pasar el repaso entero a los PDF del cierre** (palabras clave, `@Query` y lo que comprueba Room, `SUM` + `GROUP BY`, pruebas con JUnit).
- Pieza 1: `dominio/modelos/ResultadoGuardado.kt` (enum, P11). Pieza 2: `seguridad/PinStore.kt` (sal + hash en `SharedPreferences` privadas). Compilan.
- P134 → A (Daniel eligió A entre A/B/C): los métodos que pican el PIN cambian de hilo ellos mismos con `withContext(Dispatchers.Default)`.
- Pieza 3: `SeguridadRepository` (`hayPin`, `crearPin`, `comprobarPin`, `cambiarPin` con el actual primero, D18). Pieza 4: interfaz `ComandaRepository` (las dos preguntas de R6) y `ComandaRepositoryReal`. Piezas 5–7: `CartaRepository` (categorías con R16 y P48; lectura de platos; `guardarPlato` con las cuatro puertas R8 → R9 → cadena 3e (P62, P130) → guardar; eliminar/recuperar sin borrar) y `CategoriaDao.porId` [Claude]. Todo compila; solo hay `DELETE` en `borrarLinea` y en las marcas de alérgeno.
- Pieza 8: `test/…/datos/repositorios/DaosFalsos.kt`, `ComandaRepositoryFalso.kt` y `CartaRepositoryTest.kt` (P-C-09 completa: 1850 y 0 guardan; −100 lanza y el DAO no recibe nada). Commit intermedio `3f246d2`.
- Piezas 9–11: `ComandaRepository` crece (`mesasConEstado` R3 en dos pasos, `comandaPendiente`, `lineasDe`, `totalDe` con consulta nueva en SQL P131, `enviarCarrito` en `withTransaction` P129, `quitarLinea` R7 en transacción, `anular`, `cobrar`, ayudante `abierta()` R5, `resumenDelDia` con `totalDe` P135); `ComandaDao.totalDe` y `lineaPorId`; `dominio/modelos/ResumenIngresos.kt`. Commit intermedio `15647c6`.
- Pieza 12: `EntradaAko` reparte `comandaRepository` (el puesto), `cartaRepository` y `seguridadRepository` con `by lazy`; `PinStore` no se ofrece suelto [Claude]. La app arranca en el emulador; la base de datos tiene las 7 tablas y la precarga (60 mesas, 14 alérgenos, Otros y Bebidas).
- Pieza 13: tabla de dueños de R1–R16 (ninguna sin dueño; R9 y R11 las impone la base de datos) y **revisor independiente**: 0 Alta, 4 Media, 3 Baja. Daniel decidió M1 → A (P136, nota para la S8), M2 → A (P137, `ProductoDao.guardarConAlergenos`: plato y alérgenos en una transacción), M3 sí (`enviarCarrito` exige 1–99 por línea) y M4 sí (`crearPin` no pisa un PIN existente). Los Baja, a *Siguiente sesión*.
- [Claude, a petición de Daniel] `docs/guias/android-studio-basico.md`, apartado 10, ampliado: menús escondidos (☰), deshacer/rehacer, buscar, marcadores, plegar código sin teclado numérico, casos de uso «quiero… → hago…» y «Fix with AI» como botón que no se pulsa.
- `assembleDebug` y 15 pruebas de `test/` en verde. Nada a medias.

## Problemas y soluciones

| # | Qué falló | Cómo se resolvió | Justificación (por qué esta solución y no otra) | Fuente |
|---|---|---|---|---|
| 1 | Pieza 6: los seis métodos de lectura de platos quedaron pegados **dentro** de `recuperarCategoria` (antes de su `}`), y la `}` de ese método acabó al final de `alergenosDe`. **Compilaba sin error**: Kotlin admite funciones dentro de funciones (locales), pero así nadie de fuera puede llamarlas. Además se perdió la primera línea del comentario de la clase | Daniel pidió revisarlo; Claude lo vio leyendo el archivo (no por el compilador). Se mueve la `}` al final de `recuperarCategoria`, se quita la sobrante y se recupera la línea del comentario; Ctrl+Alt+L para la sangría | Que compile no basta: hay que leer dónde quedó cada cosa. La sangría torcida (métodos más metidos que los demás) era la pista. Para pegar «antes de la última `}`» conviene mirar que la `}` de encima cierra un método y no otra cosa | Propio |
| 2 | Pieza 8: al ejecutar las pruebas, «Unresolved reference 'ComandaRepositoryFalso'» (`CartaRepositoryTest.kt:23`) | Faltaba crear el archivo 9 (`ComandaRepositoryFalso.kt`); `find` en `test/` lo confirmó | «Unresolved reference» = Kotlin no encuentra ese nombre en ningún archivo del proyecto: o falta el archivo, o está en otro paquete, o el nombre está mal escrito. Se mira primero si existe | Propio |
| 3 | Pieza 11a: `quitarLinea`, `anular`, `cobrar` y `abierta` quedaron pegados **dentro** de `enviarCarrito`, y su `}` de cierre desapareció. Esta vez **no compilaba**: «Modifier 'override' is not applicable to 'local function'», «Unresolved reference 'abierta'», «Missing '}'» | Volver a poner la `}` que cierra `enviarCarrito` debajo de `comandaId` (línea 83) y Ctrl+Alt+L | Mismo origen que el problema 1 (pegar «antes de la última `}`» cuando la de encima cierra un método). Aquí el compilador lo cazó porque `override` no vale dentro de una función. Segunda vez en la sesión: propuesto dar el archivo entero en los cambios con métodos nuevos | Propio |

## Qué entendí y qué no

- Durante la sesión, bien: por qué un número repetido es una casilla y un precio negativo una excepción (tras corregirlo: el precio negativo **no** se guarda nunca); la libreta del PIN «ve el hash, pero no sirve porque no tiene el PIN»; `cambiarPin` con el actual mal «dice que no es correcto y no lo cambia»; la categoría por defecto «porque se puede cambiar el nombre» (falta: se reconoce por `esPorDefecto`); un plato de una categoría eliminada no es visible; eliminar un plato no cambia `linea_comanda` («nada»); mesa cobrada → libre; R9 y R11 las garantiza la base de datos; 2 cobradas y 1 anulada → 2 filas en el Resumen.
- Durante la sesión, mal o «no sé»: la pregunta de la interfaz (dijo «nada» y es «no compila»); qué columna dice que la mesa está ocupada («no sé»: ninguna, se deduce); envío de otra Agua a una mesa con 2 Aguas (dijo «3 líneas»: son 2, cada envío es una tanda).
- **Al cerrar (palabras de Daniel):** a) `PinStore`: «guarda el pin y el hash y sal» → **matiz importante: el PIN no se guarda nunca**, solo sal y hash; b) por qué `withContext` y no basta `suspend`: «no sé»; c) la interfaz y el actor: «para comprobar que todo esté bien» (falta: la carta solo puede hacer las preguntas del puesto, y el actor permite probar sin base de datos); d) `enviarCarrito`: «envía lo del carrito a las mesas» (falta: crea o reutiliza la comanda, congela nombre y precio, y la transacción hace que vaya entero o nada); e) por qué eliminar un plato no toca las comandas: «no sé»; f) qué demuestra la prueba de −100 €: «no sé».
- **Lo que más le cuesta (Daniel):** «en general me cuesta saber qué es un DAO y el código en general». Lo repasa con los PDF.
- **No entendí todavía:** qué es un DAO; `withContext` frente a `suspend`; para qué sirve el actor en la prueba; por qué eliminar no toca las comandas (R6); el repaso de la S3 (JUnit, `@Query`, `SUM` + `GROUP BY`, palabras clave), que va en los PDF. **Se repasa al abrir la S5, antes del primer bloque de código, empezando por qué es un DAO.**

## Para el vídeo

- **Uno toca a uno (P127, P132):** el encargado de la carta no tiene la llave del cajón de comandas; llama al departamento de comandas por un «puesto» (interfaz) que solo atiende dos preguntas. Así la carta no puede tocar una comanda ni por despiste (R6). En el servicio contesta el departamento real; en el ensayo (la prueba), un actor con guion (`ComandaRepositoryFalso`). Es la misma idea que los DAOs.
- **El doble toque en *Enviar* (P129):** toda la operación va en una transacción y Room hace una a la vez: si el camarero pulsa dos veces, la segunda ya ve la comanda de la primera y no salen dos comandas en la misma mesa (R1).
- **Preguntar o hacer, no las dos (P128):** un método mira qué mesas afecta; otro elimina. Como el camarero que o te lee la cuenta o te la cobra.
- **Casilla o excepción (P11):** lo que le puede pasar al usuario (número repetido) es una casilla de `ResultadoGuardado` y la pantalla avisa; lo que solo puede pasar por un fallo nuestro (precio negativo, carrito vacío, PIN de 3 cifras, tocar una comanda cerrada) para en seco con una excepción.
- **`suspend` no basta (P134):** `suspend` dice «puede esperar», no «hazlo en la cocina»; picar el PIN 100 000 veces se manda a la cocina con `withContext(Dispatchers.Default)` para que el camarero de la barra no se congele.
- **La mesa ocupada no se apunta, se deduce (R3, P123):** ninguna columna dice «ocupada»; el encargado mira si hay comanda abierta encima de la mesa.
- **R9 frente a R1:** el número de plato repetido lo frena la base de datos (`UNIQUE`); «una sola comanda pendiente por mesa» no se puede declarar en Room y la garantiza `enviarCarrito`.
- **Eliminar no borra (R5, R6):** se apaga `activo`; lo pedido sigue pedido y se cobra; la comanda anulada se queda como ticket tachado.
- **Las líneas son tandas (R2, R14):** 2 Aguas + 1 Agua = una comanda con 2 líneas, cada una con su precio congelado.
- **Un solo cobro para plato y alérgenos (P137):** guardar un plato sin sus alérgenos sería grave para un alérgico; van en una transacción.
- **El Resumen y el cambio de hora (revisor):** el día termina en «principio del día siguiente − 1 ms» en la zona del móvil, así acierta también los días de 23 o 25 horas; y usa `totalDe`, la misma suma que el recibo (P135).
- **Revisión independiente (P35):** otro agente lee el código sin saber cómo se escribió; 0 fallos graves, 4 medios que decidió Daniel.

## Pruebas

- Pruebas de código que pasan al terminar (1 oct): **P-C-01 a P-C-05 y P-C-09 completa** (15 pruebas en `test/`: las 12 de la S3 y las 3 de `CartaRepositoryTest`). P-C-09 apuntada con fecha en `spec+doc-pruebas.md`.
- Pruebas manuales: la S4 no tiene P-M. Comprobado a mano: la app arranca y la base de datos tiene las 7 tablas con la precarga.
- `estado-nivel.md`: ningún RF cambia (nota de la S4 en *Notas*).

## Uso de IA en esta sesión

- Plan Mode: Claude leyó los DAOs reales y propuso el plan; las decisiones P127–P137 se plantearon con 3 opciones (al menos una de fuente con enlace) y **eligió Daniel**, a veces pidiendo antes la diferencia entre opciones (P128, P130, P132, P135). El choque entre P127 B y P129 A lo detectó Claude y lo resolvió Daniel eligiendo la interfaz.
- Piezas 1–12 y 14 (arreglos del revisor): Claude compiló cada borrador en el proyecto y lo deshizo antes de dárselo a Daniel; **Daniel creó los archivos y pegó el código en Android Studio**, ejecutó las pruebas y la app; Claude comprobó leyendo el disco, compilando y pasando las pruebas por consola.
- Arreglos mecánicos de Claude: sangrías tras pegar (piezas 6 y 11a), una `}` pegada a `Int}` en `ComandaDao`, una línea en blanco en `CategoriaDao`. Daniel arregló él las llaves de los problemas 1 y 3 siguiendo las indicaciones.
- Revisión: un subagente `revisor` (P35) leyó el código sin contexto; Claude comprobó sus hallazgos y Daniel decidió los arreglos.
- Documentación: Claude amplió la guía de Android Studio con lo que Daniel preguntó (atajos y casos de uso) y escribió ficha, decisiones y notas; un subagente puso al día las guías S5–S13 con lo decidido.

## Siguiente sesión

- **Al abrir la S5, antes del primer bloque de código:** repasar **qué es un DAO** (lo que más le cuesta a Daniel), `withContext` frente a `suspend`, el actor de la prueba y R6 (eliminar no toca comandas), apoyándose en los PDF de la S4.
- S5 (Selector y PIN): `crearPin`, `comprobarPin` y `cambiarPin` ya son `suspend` y cambian de hilo solos (P134); el ViewModel solo los llama desde una corrutina. `crearPin` lanza excepción si ya hay PIN.
- Hallazgos *Baja* del revisor: (1) un doble toque en *Cobrar*/*Anular* lanzaría excepción en la segunda llamada y cerraría la app → en la S9, botón desactivado mientras cobra o el ViewModel captura la excepción; (2) `PinStore.guardar` usa `apply` (escribe en disco un instante después); valorar `edit(commit = true)` en la S5; (3) id inexistente: `eliminarPlato` no hace nada y `eliminarCategoria` lanza; unificar si molesta en la S6–S7.
- Nota para la S8 (P136): `visibles()` deja los platos de Otros los primeros; la carta monta sus secciones con `categorias()`.
