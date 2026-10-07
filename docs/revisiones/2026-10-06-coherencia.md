# Revisión de coherencia entre documentos y código — 6 oct 2026 (tras la S8)

Revisor: Claude (Fable 5.1). Comparados entre sí y con el código real de `app-ako/app/src`: `CLAUDE.md`, el spec, `decisiones-code.md` (incluida la tabla «Para el Project»), `estado-nivel.md`, `spec+doc-pruebas.md`, `spec+doc-pantallas.md`, `spec+doc-requisitos.md` (lista de RNF), `textos-ui.md`, `README.md`, `LEEME.md`, las fichas S00–S08, las guías S9–S13 y `juego-de-datos.md`. **No se ha cambiado nada.**

**Recuento:** 0 Alta · **1 Media** · **6 Baja** · 9 comprobaciones en las que todo coincide. Casi todo es mecánico (lo hace Claude en el cierre de la S9 cuando Daniel diga); la Media es una decisión de Daniel y se trata en el informe de forma de trabajar (F01).

**Regla aplicada:** manda `decisiones-code.md` donde discrepe con el spec (P158); las fichas cerradas **no se reescriben** (plantilla del diario): lo que esté mal en una ficha se dice en la siguiente.

---

## MEDIA

### C01 · El plan de pruebas dice que las pruebas manuales «las ejecuta Daniel con el dedo»; desde la S6 las ejecuta Claude con `adb` — **Media** · decisión (ver F01 en el informe de forma de trabajar)
- **Dónde:** `docs/spec+doc-pruebas.md:11` («Prueba manual (P-M-nn): la ejecuta Daniel con el dedo en el emulador») frente a las *Observaciones* de P-M-04, 05, 06, 09, 10, 11, 14–21 y 29 («Ejecutada por Claude con `adb` a petición de Daniel»), las fichas S6 (pieza 21), S7 (pieza 14) y S8 (piezas 23–24), y `como-trabajamos` (2 y 5 oct: «las pruebas esas hazlas tú»).
- **Qué pasa:** la definición y la práctica no coinciden. La práctica está **declarada con honradez** en cada prueba y en cada ficha (bien), pero la frase del plan es la que la memoria copiará en el apartado 7, y un corrector que lea las dos cosas verá la contradicción.
- **Por qué importa:** la normativa permite la IA como apoyo y prohíbe que «ejecute el proyecto por sí sola»; probar la app es parte del proyecto. No es un problema de integridad (todo está escrito tal cual pasó), es un problema de **cómo se cuenta** y de **cuánto hace Daniel con sus manos**.
- **Cómo lo he comprobado:** leyendo la columna *Observaciones* de las 29 P-M y las tres fichas; de las 20 pruebas con resultado, Daniel ejecutó con el dedo P-M-01, 02, 03 y 13 (S5); las 16 restantes las ejecutó Claude con `adb` y Daniel revisó las capturas.
- **Arreglo del documento (mecánico):** cambiar la línea 11 del plan por «la ejecuta Daniel con el dedo en el emulador o, a petición suya, Claude con `adb`; la columna *Observaciones* dice quién en cada caso» y añadir una fila a «Para el Project». **Quién las ejecuta a partir de ahora** (sobre todo la pasada final de la S13) es la decisión F01.

## BAJA (mecánico)

| # | Dónde | Qué no coincide | Arreglo |
|---|---|---|---|
| C02 | `README.md:34` | «El plan tiene nueve pruebas de código (seis en `test/` y tres en `androidTest/`)»; desde el 1 oct son **once** (ocho en `test/`, tres de Room), como dicen el spec 12, el plan y las fichas | Corregir la frase; comprobar que el README se lee en GitHub con el recuento bueno |
| C03 | `LEEME.md` (raíz) | Dice que el hook «bloquea commits… con atribución» (al revés desde P111); la fila de `.claude/` no nombra la skill `seguridad`; la fila del spec habla de «14 tablas, 15 reglas» (son 7 tablas, P115); `docs/diario/` «vacía hasta la S1» (hay 9 fichas); «commits sin atribución» en la fila de `CLAUDE.md`. Es el índice que lee un chat o una persona que entra en la carpeta | Una pasada de 10 minutos al cerrar la S9 (el archivo dice «Se edita»); o marcar arriba «índice de la fase 5b, superado por `CLAUDE.md` y `decisiones-code.md`» |
| C04 | `docs/diario/sesion-08.md`, viñeta de P167 | Dice que la consecuencia son «consultas nuevas `ComandaDao.pendientesConTotalObservadas`, `ProductoDao.visiblesObservados`, `ComandaRepository.mesasConEstadoObservadas`»; **esos nombres no existen**: más abajo la misma ficha (pieza 6a) y `decisiones-code.md` 5.11 dicen lo que se hizo de verdad (se cambiaron a `Flow` con el mismo nombre: `pendientesConTotal`, `visibles`, `mesasConEstado`; solo `MesaDao.todasObservadas` es nueva). El código coincide con lo segundo | La ficha S8 está cerrada: una línea en la ficha S9 («la viñeta P167 de la S8 cita nombres del plan, no del código»). El diagrama de la fase 7 sale del código, así que no afecta a la memoria |
| C05 | `docs/spec+doc-pruebas.md`, P-M-04 y P-M-17, columna *Entrada* | P8 (`decisiones-code.md`) y `juego-de-datos.md` 2.7 dicen que al ejecutarlas por primera vez se añade a la *Entrada* la línea «Bebidas eliminada (P8)…»; se añadió a *Observaciones*, no a *Entrada* (en P-M-17 dice «*Entrada* con P8», pero la celda *Entrada* no lo lleva) | Añadir la frase a las dos celdas *Entrada* en el cierre de la S9 |
| C06 | `docs/textos-ui.md:164` | Dice «botón negativo de Anular → `comun_no` y afirmativo → `comun_si` según P-M-25 (D11)»; P40 → A (sesión 00) fijó «Cancelar / Anular», y `strings-es-borrador.xml` y la guía S9 (pieza 1) ya lo dicen bien (`comun_cancelar` / `comanda_btn_anular`). `comun_si` no existe en `strings.xml` ni debe existir (P40: el botón dice lo que hace) | Corregir la línea para que la S9 no la copie por error |
| C07 | `docs/spec-claude-code.md`, apartado 3 (árbol de paquetes) | El árbol es de antes de la S4: `ui/comun/` lista `CategoriaAdapter (2a cajas · 5a fila)` (son dos adaptadores, P52; `CategoriaAdapter` vive en `ui/panel/` y `CategoriaFilaAdapter` en `ui/pedido/`); `MesasAfectadasDialog` sigue en `panel/` (está en `comun/`, P163); faltan `Formato`, `LineaVista`, `ComprobadorPin`, `CabeceraAdapter`, `MesaConTotal`, `PlatoConMesas`, `ComandaRepositoryReal`, `EntradaAko`; `RejillaMesasFragment (1d elegir · 6a gestionar)` ya no tiene modos (P168) | No hace falta tocarlo ahora: el spec se edita a mano solo donde confunde (P158) y el diagrama definitivo se genera desde el código (spec 15). Propuesta: una tachadura con «ver el código y `decisiones-code.md` 5.6–5.11» encima del árbol en la S13 |

---

## Comprobado y coincide (no hace falta volver a mirarlo)

1. **`estado-nivel.md`:** el recuento de cabecera (13 implementados, 7 implementados no probados) coincide fila a fila con la tabla, y cada estado coincide con el *Resultado* de su P-M en el plan de pruebas (Pasa → implementado; Parcial → implementado, no probado; sin ejecutar → diseñado).
2. **`spec+doc-pruebas.md` frente al código:** 21 pruebas en `test/` como dice el apartado 4; P-C-09, 10 y 11 prueban lo que dicen sus filas (he leído las aserciones); P-C-06, 07 y 08 sin ejecutar, como corresponde (S11).
3. **`decisiones-code.md` 5.11 (S8) frente al código:** `MesaDao.todasObservadas`, `pendientesConTotal`/`mesasConEstado`/`visibles` como `Flow`, `cartaVisible`, `RejillaMesasFragment.nueva(titulo)` sin `ModoRejilla`, `PedidoViewModel.ficha`/`cargarFicha`/`DatosFicha`, `LineaVista`/`LineaAdapter` sin modo, `enviar(): Boolean` con `enviando`, cadenas `carrito_tope_99` y `enviar_hecho`, colores `mesa_ocupada`/`sobre_mesa_ocupada`: todo existe con esos nombres.
4. **Guías S9–S13 frente al código:** todos los nombres que citan en sus prerrequisitos (constructores de los repositorios, `Formato.precio` solo ≥ 0, `ComprobadorPin`, `ConfirmacionDialog.nueva(...)`, `RESPUESTA_AFIRMATIVA`, `SelectorViewModel.mesas`, `rejillaVisible()`, `item_linea.xml` con sus ids, `AkoGlideModule` en `imagenes/`, el `ImageView` de `sheet_categoria.xml` sin id) coinciden. La única omisión está en el informe del plan (P03: el hueco de la foto de `activity_plato.xml` tampoco tiene id).
5. **«Para el Project»:** las decisiones de la S6, S7 y S8 que cambian el diseño (P160, P49 B, P161, P162–P165, P166–P170, el TOTAL fijo de 5c, las cadenas nuevas) tienen su fila. Lo que no está son nombres internos [Claude] (`Formato`, `PlatoConMesas`, `CategoriaConPlatos`…), que la memoria recogerá del diagrama generado desde el código.
6. **Vocabulario:** «eliminar»/«recuperar», «Cuenta», «recibo», «Resumen de ingresos», «plato visible»/«existente» se usan igual en código, fichas, plan y guías.
7. **Plan de pruebas y `juego-de-datos.md`:** el orden, los *Parcial* y «se repite entera en» de la tabla resumen (apartado 5) coinciden con la columna *Observaciones* del plan y con `estado-nivel.md` tras la S8.
8. **Hitos:** P2 (8 nov y semana del 23 nov) iguales en `CLAUDE.md`, `decisiones-code.md`, la guía S13 y las fichas.
9. **README y `docs/`:** la estructura de carpetas que describe el README coincide con la real (`datos/`, `dominio/`, `seguridad/`, `imagenes/`, `ui/`); el apartado «Uso de IA» coincide con lo que dicen las fichas (quién teclea, cuándo escribe Claude, coautor desde el 29 sep).

---

## Resumen para Daniel (5 líneas)

1. Los cuatro documentos que mandan (código, `decisiones-code.md`, `estado-nivel.md`, plan de pruebas) **dicen lo mismo** en lo que importa: recuentos, estados, nombres de la S8 y resultados de las pruebas.
2. Lo único de peso es cómo se cuenta **quién ejecuta las pruebas manuales**: el plan dice «Daniel con el dedo» y desde la S6 las hace Claude con `adb` (declarado en cada una). Se arregla con una frase en el plan, y tú decides quién pasa la pasada final (F01).
3. Cinco arreglos mecánicos de 10 minutos para el cierre de la S9: el README (9 → 11 pruebas), el LEEME de la raíz (habla del hook y de las 14 tablas de antes), la línea de P8 en dos *Entradas* del plan, una línea de `textos-ui.md` que aún dice `comun_si`, y una nota en la ficha S9 sobre tres nombres de la ficha S8 que no existen en el código.
4. El árbol de paquetes del spec (apartado 3) es de la S3 y no se corresponde ya con el código; no hace falta tocarlo porque el diagrama definitivo sale del código, pero conviene una tachadura que lo diga (S13).
5. Nada de esto afecta a lo que un corrector vería en la app; afecta a lo que leerá en la memoria si se copia sin repasar.

---

## Aplicado (7 oct 2026)

Decidido por Daniel el 7 oct y aplicado el mismo día; detalle en `2026-10-06-cambios-aplicados.md`.

- **C01 → P176 (opción de Daniel):** la línea «Cómo se lee» del plan dice «Daniel con el dedo o, con la lista de pruebas revisada y aprobada antes por él, Claude con `adb`»; la regla está en `como-trabajamos` y en `cerrar-sesion` 3.
- **C02:** README con once pruebas de código. **C03:** LEEME al día (hook y coautor, `seguridad`, 7 tablas y P158, fichas S00–S08, carpetas nuevas). **C05:** P8 en las *Entradas* de P-M-04 y P-M-17. **C06:** `textos-ui.md` con `comun_cancelar` / `comanda_btn_anular`.
- **C04:** la ficha S8 no se toca; nota en la ficha S9 al abrirla (lo dice el primer mensaje de la S9 y el apartado 0 de su guía).
- **C07:** tachadura sobre el árbol del spec 3 en la S13 (anotado en la guía S13, apartado 5).
