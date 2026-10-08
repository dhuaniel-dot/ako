# Revisión de coherencia entre documentos y código — 8 oct 2026 (tras la S13, nivel 1 cerrado)

Revisor: Claude (Fable 5.1), a petición de Daniel, con cuatro ayudantes en paralelo (uno por bloque de documentos) y comprobación propia por muestreo de cada afirmación (líneas de los documentos leídas una a una antes de darlas por buenas). Comparados con el código real de `app-ako/app/src` (main, test, androidTest, recursos, manifiesto y Gradle): el spec (`spec-claude-code.md`), `spec+doc-clases.md`, `spec+doc-pantallas.md` y las leyendas de los wireframes, `spec+doc-pruebas.md`, `textos-ui.md`, `decisiones-code.md`, `estado-nivel.md`, los tres documentos de `para-el-project/` y su `LEEME.md`, `README.md`, `readme-borrador.md`, `juego-de-datos.md`, la guía S13 y la ficha S13. **No se ha cambiado ningún archivo de código ni de documentación**: los únicos archivos nuevos son los dos informes de hoy en esta carpeta.

**Recuento:** 0 Alta · **2 Media** (C01, C02) · **51 Baja** agrupadas por documento (C03–C53; casi todas mecánicas: una tachadura, una nota o una fila) · 12 comprobaciones en las que todo coincide. Lo mecánico lo hace Claude cuando Daniel diga (son documentos, no código); **tres decisiones son de Daniel**: C01 y C02 (tocan lo que llega a la memoria) y C46 (qué hace Pedir si Android mata la app).

**Regla aplicada:** manda `decisiones-code.md` donde discrepe con el spec (P158); el spec se corrige con tachaduras y notas, nunca reescribiéndolo; las fichas cerradas **no se reescriben** (plantilla del diario). Lo que ya está apuntado en `para-el-project/` se da por **declarado** y no se repite como hallazgo.

---

## MEDIA

### C01 · `textos-ui.md`, el inventario de textos que alimentará el doc, se quedó en el borrador de la sesión 00: 24 claves que no existen, 23 cadenas del código sin fila, 5 textos distintos y 5 dudas (D4, D5, D14, D15, D16) que figuran «sin resolver» y están resueltas en el código — **Media** · decisión
- **Dónde:** `docs/textos-ui.md` entero frente a `app-ako/app/src/main/res/values/strings.xml` (133 `<string>` + 4 `<plurals>` = 137 claves; 26 `translatable="false"`; 111 traducibles) y `values-en/strings.xml` (111 claves, exactamente las traducibles).
- **Qué no coincide (comprobado clave a clave):**
  - Claves del documento que **no existen** en `strings.xml` (en el código se reutiliza otra, como dicen los propios comentarios de `strings.xml`): `app_nombre` (l.14; es `app_name`) · `panel_nueva_categoria_cd` (l.46; el «+» usa `categoria_titulo_nueva`, `activity_panel.xml:63`) · `panel_editar_categoria_cd` (l.48; el lápiz usa `categoria_titulo_editar`, `item_categoria_caja.xml:66`) · `panel_btn_cambiar_pin` (l.52; es `pin_cambiar_titulo`) · `categoria_activa` (l.59; el interruptor es `plato_en_la_carta`, P41) · `categoria_eliminada_titulo` (l.107; el título 3e-1 es `panel_categoria_eliminada`, `PlatoActivity.kt:351`) · `plato_salir_btn_salir`, `enviar_btn_enviar`, `salir_sin_enviar_btn_salir` (l.104, 133, 136; el afirmativo reutiliza `carta_btn_salir` y `carrito_btn_enviar`) · `comun_si` (l.198; C06 del 6 oct quedó a medias: la l.165 ya dice que no existe) · `alergeno_gluten` … `alergeno_moluscos` (tabla 9, l.219–232; en el código van numeradas `alergeno_01_gluten` … `alergeno_14_moluscos`, `Precarga.kt:56–69`).
  - Cadenas del código **sin fila**: `comun_atras_cd` «Atrás» (H26), `mesa_ocupada_cd` «%1$s, %2$s» (H07), `panel_plegar_cd` / `panel_desplegar_cd` (P56 A), `categoria_eliminar_plato_mesas` (plurals) y `comun_y` (los monta `MesasAfectadasDialog.kt:20–33`), `carrito_tope_99` (P74 A), `enviar_hecho` (P72 A).
  - Textos **distintos**: `resumen_titulo` «Resumen» (l.72) → «Resumen de ingresos» (D5) · `recuperar_categoria_cuerpo` «Volverán a la carta sus %1$d platos.» (l.111) → «Platos que volverán a la carta: %1$d» (P164 B) · `salir_sin_enviar_cuerpo` (l.135) → plurals «Hay %1$d plato en el carrito sin enviar. Se perderá.» / «Hay %1$d platos…» (D16) · `categoria_eliminar_cuerpo` y `mesas_afectadas_cuerpo` (l.62, 106) dadas como cadena → son `<plurals>` con singular fijado (D15) · `comun_precio` «espacio fino» (l.203) → es espacio **duro** (`&#160;`, U+00A0).
  - Columna «dónde se usa» con errores: `comun_no` (l.199) no se usa en Anular; `comun_cancelar` (l.196) también en 2e y Anular; `comun_mesa` (l.202) no en la confirmación de Enviar (es `enviar_cuerpo`) y sí en las celdas de 1d/6a (`MesaAdapter.kt:47,65`).
  - Recuento previsto (apartado b, l.296–308): «121 cadenas + un plurals, 17 de precarga» → real: 137 claves (133 + 4), 111 traducibles, 26 `translatable="false"` (9 de símbolos y formatos + 17 de precarga).
- **Por qué importa:** es la fuente de la tabla de textos y del apartado de localización de la memoria (RNF-19); copiado tal cual, el doc diría cosas que la app no dice.
- **Opciones:**
  - **A)** Reescribir `textos-ui.md` **desde `strings.xml`** (una fila por clave real, con su texto ES, su EN y dónde se usa; las dudas D4–D16 pasan a «resueltas» con su decisión) y mover el borrador actual a `docs/guias/` junto a `strings-es-borrador.xml` [Claude]. Pega: ~140 filas; lo escribe Claude y Daniel lo revisa.
  - **B)** Corregir solo las 23 líneas listadas arriba y añadir una tabla «cómo quedó» con las cifras reales; el resto del documento se queda como está.
  - **C)** Declararlo superado: una nota en la cabecera («el inventario vivo es `strings.xml` + `values-en/strings.xml`; este documento es el borrador de la sesión 00») y nada más; la memoria se escribe mirando los XML.
  - **Recomendación: A.** Es el documento que la memoria copia y hoy cuesta más corregirlo que regenerarlo; además deja a la vista las 111 traducciones.

### C02 · `para-el-project/02-memoria.md` no tiene fila de la S13: la memoria no sabrá que la pasada final de las 29 pruebas manuales la pasó **y anotó** Claude con `adb` sin que Daniel revisara las capturas — **Media** · decisión
- **Dónde:** `docs/para-el-project/02-memoria.md`, última fila «S12, fotos» (8 oct). La ficha S13 (piezas 9–11 y «Uso de IA») y `decisiones-code.md` 5.17 («Pasada final hecha por Claude con `adb` y anotada por Claude») lo dicen; el canal hacia el doc, no.
- **Qué falta en esa fila:** la pasada final (29 P-M, 8 oct, lista aprobada por Daniel con «dale», ejecutada y anotada por Claude, capturas sin revisar por Daniel); las 11 P-C con fecha; RNF-19 hecho (`values-en`, glosario); la revisión de accesibilidad con la opción B de P98 y su tabla; que la S13 cierra sin APK ni Release (P198 ya tiene fila). También: `02-memoria.md` l.14 dice «las pruebas de código son 14 del plan más 2 nuevas» mezclando `@Test` y P-C (hoy: 11 P-C = 24 `@Test`, 21 + 3).
- **Por qué importa:** es la regla de oro del proyecto («lo escrito tiene que coincidir con lo hecho») aplicada a la declaración de IA del apartado 7: si el doc dice que Daniel pasó las 29 pruebas, no será verdad.
- **Opciones:** **A)** una fila «S13» en `02-memoria.md` con los cinco puntos de arriba, redactada por Claude ahora y revisada por Daniel [Claude] · **B)** además, que Daniel revise las capturas de `docs/capturas/` y de la pasada antes de llevarlo al Project, y la fila diga «capturas revisadas por Daniel el …» · **C)** dejarlo para el cierre de la S14. **Recomendación: A ahora y B cuando Daniel tenga media hora** (las capturas están en el ordenador).

## BAJA (mecánico, por documento)

### `docs/spec-claude-code.md` (último cambio: commit `fb469fa`, S6; nada de la S7 a la S13 está tachado)

| # | Línea | Dice | Hay | Arreglo (tachadura + nota) |
|---|---|---|---|---|
| C03 | 84 y 257 | «29 pruebas manuales + 9 de código», «las 29 P-M y las 9 P-C» | 11 P-C (la l.274 ya dice «once») | «9» → «11» |
| C04 | 237, 257 y 290 | «Accessibility Scanner por las siete pantallas (sesión 13)» | No se usó: P98 → B (editor de diseño + lint + revisión a mano), ficha S13 piezas 5–6 | Tachar y poner «P98 B» |
| C05 | 48 | `Calculadora (importe, total, cambio)` | Solo `importe` y `cambio` (`Calculadora.kt:9,13`, P120); la l.184 ya lo dice | Tachar `total` |
| C06 | 44, 51, 58, 60, 63–64 (árbol del apartado 3) | `ComandaRepository` como clase; `modelos/` sin `PlatoConMesas` ni `MesaConTotal`; `MesasAfectadasDialog` en `panel/`; `resumen/` sin `ResumenListaFragment` ni `ComandaCobradaAdapter`; `CategoriaAdapter (2a cajas · 5a fila)` en `comun/`; faltan `LineaVista`, `GuardaDobleToque`, `ComprobadorPin`, `Formato`, `CabeceraAdapter`, `CategoriaFilaAdapter`, `EntradaAko`, `AkoGlideModule` | Es el C07 del 6 oct: `decisiones-code.md` 5.12 (l.372) y la guía S13 decían que **en la S13 se pondría la tachadura** «ver el código y `decisiones-code.md` 5.6–5.12» sobre el árbol; **no se hizo** | Hacer ahora esa tachadura (una nota sobre el árbol; el diagrama definitivo sale del código en la fase 7) |
| C07 | 80 | «las seis P-C de `test/`» | Son ocho (21 `@Test`) | «ocho» |
| C08 | 131–137 (tabla 5) | No nombra los índices sobre las claves foráneas | `Producto.kt:22`, `Comanda.kt:21`, `LineaComanda.kt:28–29`, `ProductoAlergeno.kt:26` los declaran (Room los exige); ya en `para-el-project/01` | Nota «+ `@Index` en cada FK» |
| C09 | 176 (R2) | «en una `@Transaction`» | Es `db.withTransaction { }` en `ComandaRepositoryReal.kt:56` (P129) | Precisar |
| C10 | 188 (R15) | «el Panel usa `porCategoria` y los ve todos» | El Panel usa `ProductoDao.todosObservados()` vía `CartaRepository.categoriasConPlatos()` (P49 B); `porCategoria` solo lo usa `platosDe` para 2e y el contador de 3e | Corregir el nombre |
| C11 | 189 (R16) | «`guardarCategoria` rechaza… eliminarla; `CategoriaAdapter` la pinta la última, sin flechas ni interruptor» | Rechazar eliminarla está en `eliminarCategoria` (`CartaRepository.kt:91`); «la última» lo hace el repositorio (`sortedBy { it.esPorDefecto }`, l.28/35/111); el interruptor oculto está en `CategoriaBottomSheet.kt:83`; `CategoriaAdapter` no distingue la por defecto | Reescribir la celda con los tres sitios |
| C12 | 196 | «¿Quieres reactivar <categoría>? Volverán a la carta sus N platos» | «¿Quieres recuperar %1$s?» / «Platos que volverán a la carta: %1$d» (P9, P164) | Tachar |
| C13 | 206 | «`RejillaMesasFragment` es uno con dos modos (`elegir` / `gestionar`)» | No hay modo: `nueva(titulo: Int)` (P168); la rejilla pinta lo que le dan y quien la aloja decide | Tachar «dos modos» → «dos títulos» |
| C14 | 93 y 207 | Cajas «de altura fija ~270 dp» | `alto_lista_caja = 224dp` (cuatro filas de 56 dp; la caja entera ~340 dp, P161; ya en 03) | Nota |
| C15 | 234 | Botones de acción naranjas: «… Dar la cuenta, Anular» | *Anular* de 6b es texto en color de texto (S13 pieza 1, 2 → A; ya en 03) | Tachar «Anular» |
| C16 | 276–286 (tabla P-C) | 9 filas | Faltan P-C-10 (`CartaRepositoryTest`, cadena 3e, 5 pruebas) y P-C-11 (`CarritoTest`, 2 pruebas) aunque la l.274 dice «once» | Dos filas |

Comprobado y correcto en el spec: apartado 2 entero (stack, versiones, `portrait` en las 6 Activities, sin `INTERNET`, dependencias de prueba), apartado 5 (7 tablas, columnas, PK compuesta, FK RESTRICT, UNIQUE, enums como texto sin `Converters`), 5.1, R1, R3–R11 y R14 con el método que nombran, apartados 8 y 9, P-C-01…09 de la tabla 12, apartado 13 (`attribution`).

### `docs/spec+doc-clases.md` (borrador congelado; el definitivo saldrá del código en la fase 7). Lo que **no** está ya en `para-el-project/01`

| # | Dónde | Dice | Hay |
|---|---|---|---|
| C17 | l.82 y `.puml` | `enviarCarrito(mesaId, carrito)` | `enviarCarrito(carrito: Carrito): Long`; la mesa va dentro de `Carrito(mesaId)` |
| C18 | l.97 | `SelectorViewModel.hayPlatoVisible` | `motivoPuertaCerrada(): Int?` sobre `hayPlatoVisible()` + `hayPlatoExistente()` (`ProductoDao.hayAlguno()`) |
| C19 | `.puml` Carrito | `anadir(p, cantidad)` sin retorno | Devuelve `Boolean` (false si topa en 99 o cantidad < 1; P74, P148) |
| C20 | l.98 | `PanelViewModel` | Faltan `estaPlegada`/`alternarPlegado` (P56 A), `platosAfectados`, `recuperarCategoria`, `comprobarPin` |
| C21 | CartaRepository / modelos | — | Faltan `platosAfectadosPorCategoria(): List<PlatoConMesas>`, `PlatoConMesas`, `MesaConTotal` y `AkoGlideModule` (no aparecen en ningún documento) |
| C22 | l.85 | `ResultadoGuardado` con «precio negativo (R8)» | No existe: R8 lanza `IllegalArgumentException` (`ResultadoGuardado.kt:4`) |
| C23 | `.puml` pantallas | `CartaFragment ..> PinDialog`; `PanelActivity ..> MesasAfectadasDialog` | Abren `PedidoActivity.pedirPin()` y `CategoriaBottomSheet` |
| C24 | l.35/42 | «6 Activities, 8 Fragments, 5 diálogos, 4 adaptadores; 58 clases» | 6 Activities, 9 Fragments, 5 diálogos/hojas, 7 adaptadores; 74 tipos de primer nivel |

Arreglo: una sola fila «complemento S6–S13» en `para-el-project/01` con C17–C24 (el borrador no se edita: lo sustituye el diagrama de la fase 7).

### `docs/decisiones-code.md`

| # | Línea | Dice | Hay | Arreglo |
|---|---|---|---|---|
| C25 | 87 (4c) | «AGP 9.4.1 … (integrado)» | `agp = "9.3.3"` (P42 → B, 25 sep; `stack-verificado/LEEME.md` ya corregido). Lo demás de la fila (Gradle 9.6.0, Kotlin 2.2.10, KSP 2.3.12, Room 2.8.5, Glide 5.0.9, SDK 37/26) coincide | Tachar «9.4.1» → «9.3.3 (P42)» |
| C26 | 372 (5.12) | «C07 (árbol del spec 3): tachadura en la S13» | No se hizo (C06 de arriba) | Hacerla y anotar la fecha |
| C27 | 358 (P171) | Portero «una por pantalla (`PanelActivity`, `SelectorFragment`, `PedidoActivity`)… la S9 la usa en 6b y 6c» | También `CuentaActivity.portero`, `ReciboFragment` y `ResumenListaFragment` (2g) | Añadir «y 2g» |

Verificado y correcto en 5.6–5.17: todas las decisiones que nombran un archivo, método o número (P127–P197 y la 5.17 entera: `alpha 0.8f`, `maxLines="2"` en 7 layouts, autosizing 24→12 sp, `minHeight="48dp"`, las 8 cadenas `translatable="false"` nombradas, 111 claves en `values-en`, `FormatStyle.FULL`, `centimosDesde` con tope de 7 cifras, 500 ms del portero, `reglas_extraccion.xml`, `SavedStateHandle`, etc.).

### `docs/para-el-project/`

| # | Documento | Qué | Arreglo |
|---|---|---|---|
| C28 | `02-memoria.md` | Falta la fila S13 (es C02) y la l.14 mezcla `@Test` con P-C | Ver C02 |
| C29 | `01-diagrama-de-clases-y-bd.md` | Faltan C17–C24 | Una fila «complemento S6–S13» |
| C30 | `LEEME.md` l.9–11 | «Filas (7 oct): 24 / 20 / 13» | Hoy 29 / 25 / 20; o quitar la columna o decir «se cuentan al cerrar» |

`03-pantallas-textos-y-requisitos.md`: las cuatro filas S13 que `decisiones-code.md` 5.17 manda «a Para el Project» **están** (*Anular* no naranja, autosizing y `maxLines`, inglés y fecha larga, eliminados al 80 %). ✓

### `docs/spec+doc-pruebas.md` y `docs/guias/juego-de-datos.md` (ninguno de código: las 21 + 3 `@Test`, los nombres de clases y métodos y las 40 fechas coinciden con `s13.md` y la ficha S13)

| # | Línea | Dice | Hay | Arreglo |
|---|---|---|---|---|
| C31 | 1–2 (cabecera) | «Las columnas *Resultado*… van en blanco a propósito», «siguen vacías» | Las 40 filas llevan `2026-10-08` | Nota «Actualizado el 8 oct 2026 (S13): pasada final, las 40 con resultado» |
| C32 | 16 | «*plato visible* = en la carta, **disponible** y con su categoría activa (R15)» | `disponible` es nivel 2 (inc. 12); `Producto.kt` no tiene esa columna; `CLAUDE.md`: visible = activo y categoría activa | «(disponible: nivel 2)» |
| C33 | 58 (P-M-11, esperado) | «Volverán a la carta sus N platos»; botones «Rechazar / Aceptar / No / Sí» | «Platos que volverán a la carta: %1$d» (P164 B); botones No / Sí, mover / Recuperar | Actualizar el esperado (las *Observaciones* ya lo dicen) |
| C34 | 48, 59, 71 (P-M-01, 12, 19) | Citas cortas: «Si lo olvidas, no se puede recuperar»; «2 comandas · 47,00 €»; «2 · 37,00 €» | «…: habrá que reinstalar la app.» (D1); dos etiquetas «Comandas cobradas» / «Total del día» (D6); «Carrito · 2 · 37,00 €» (D7) | Notas «(D1)», «(D6)», «(D7)» |
| C35 | 80–85 y P-M-06/10 | «quitar», «cancelar / aceptar», «No / Sí» | Botones reales Quitar, Cancelar / Quitar y anular, Cancelar / Anular, Cancelar / Cobrar, Cancelar / Eliminar (P40); las *Observaciones* lo anotan caso a caso | Una nota en §1: «los botones reales están en la tabla 2.0 de `juego-de-datos.md`» |
| C36 | 109 (P-C-06) | `ComandaRepository.enviarCarrito()` | La clase probada es `ComandaRepositoryReal` (la interfaz es `ComandaRepository`) | Opcional |
| C37 | `juego-de-datos.md` 154 (§3.0) | «*Fecha* en formato dd/mm/aaaa» | El plan usa `2026-10-08` en las 40 filas | «aaaa-mm-dd» |
| C38 | `juego-de-datos.md` 100 (paso 35) | «pie 2 comandas · 47,00 €» | Dos etiquetas (D6) | Como C34 |

### `README.md` y `docs/guias/readme-borrador.md` (stack, paquetes, comandos y manifiesto coinciden)

| # | Línea | Dice | Hay | Arreglo |
|---|---|---|---|---|
| C39 | `README.md` 11 | «el nivel 1 del diseño, que se construye por sesiones» | 29/29 RF implementados y probados el 8 oct 2026 (S13) | «Nivel 1 terminado y probado el 8 oct 2026 (S13); el nivel 2 está diseñado, no programado» |
| C40 | `README.md` 31, 33 | «se anotan al ejecutarlo», «en cada momento» | Las 29 P-M y las 11 P-C pasaron el 8 oct; el detalle vive en `docs/pruebas-pasadas/` y las capturas en `docs/capturas/`, que el README no nombra | Una frase con el estado final y las dos carpetas |
| C41 | `README.md` 38 | Lista de `docs/` | Faltan `textos-ui.md`, `pruebas-pasadas/`, `capturas/`, `para-el-project/`, `revisiones/`, `resumenes/`, `lecciones-claude.md` | Añadir al menos `pruebas-pasadas/` y `capturas/` |
| C42 | `README.md` 37 | `dominio/` «cálculos puros» | También `modelos/` y `Carrito`; `EntradaAko` en la raíz del paquete sin mención | Menor |
| C43 | `readme-borrador.md` 3, 42, 52, 54 | «nueve pruebas (seis + tres)», AGP 9.4.1 como duda, «(P110)», «(P111)» | Superado por `README.md` desde el 7 oct (C02 de aquel informe) | Nota al principio «Superado por `README.md`; no se actualiza» |

### `docs/guias/sesion-13.md`

| # | Línea | Dice | Hay | Arreglo |
|---|---|---|---|---|
| C44 | 1, 122, 146 | «la Release `v1-nivel1` con la APK se ve en GitHub», «en GitHub se ven los dos commits», «el Anexo I instala la APK de esta Release» | P198: sin push, etiqueta ni Release (solo la l.31 y la pieza 12 se actualizaron) | Tachadura con «P198» |
| C45 | 30 | Hueco de `Formato.fecha` «Elige Daniel» | Resuelto en la S13 (→ B, 5.17) | «decidido, 5.17» |

### Fichas de pantalla (`spec+doc-pantallas.md`) y leyendas de los wireframes

Las 14 vistas del nivel 1 (1a, 1b, 1c, 1d, 2a, 2b/2e, 2g y su recibo, 3a y sus avisos, 5a, 5b, 5c, 6a, 6b, 6c) y los siete diálogos dibujados **hacen lo que dicen su ficha y su wireframe** (flujo, botones, textos, validaciones, casos límite; comprobado vista a vista contra el código y `strings.xml`). Lo que no coincide:

**No declarado en ningún sitio (nuevo):**

| # | Vista · ficha | Dice | Hace | Propuesta |
|---|---|---|---|---|
| C46 | Pedir · ficha 1, l.131 | «Android cierra la app → Arranca de nuevo en el selector; el carrito se pierde» | Si Android mata el proceso con Pedir delante, al volver **se reabre Pedir** (Android restaura la carta, la ficha o el carrito que hubiera; `PedidoActivity.kt:53–63`) con la misma mesa y el **carrito vacío** (vive solo en la libreta, `PedidoViewModel.kt:55–71`). El carrito sí se pierde (eso coincide). En Cuenta y 2g este caso se trató (P193 A); en Pedir no | **Decisión de Daniel:** A) corregir la ficha y la memoria («vuelve a la carta de la misma mesa con el carrito vacío») [Claude] · B) hacer lo que dice la ficha: si la libreta renace en blanco, `finish()` en `PedidoActivity`, como P193 A (3 líneas) · C) dejarlo y no decir nada. Recomendada: **A** (el comportamiento actual es el que menos sorprende al cliente: sigue en su mesa) |
| C47 | Diálogos · fichas 1, 2, 3, 5 | Dan el cuerpo pero no el título ni el botón afirmativo de: puerta de Pedir («No se puede pedir»), Enviar («¿Enviar el pedido?»), salir del plato con cambios («¿Salir sin guardar?» · Salir), aviso 2e («¿Eliminar la categoría?») y el título «Editar plato» de 3a; `textos-ui.md` los marca «[no fijado — propuesta]» (l.19, 61, 102–104, 131, 182–184) | Son los de la app (`strings.xml` 74, 89, 106–109, 152, 175) y los que se ven en el vídeo | Fila en `para-el-project/03`: las fichas adoptan estos literales |
| C48 | 2a · ficha 2, l.713 y leyenda 02a #1 | «`[Terminar]` y `← Atrás` conviven: hacen cosas distintas» | Los dos hacen `finish()` (`PanelActivity.kt:91–93`): en el nivel 1 son necesariamente iguales (solo existen en 2a y «un paso atrás» desde 2a es el selector) | Nota en la ficha 2: «en el prototipo hacen lo mismo; la diferencia es para cuando haya Terminar en subpantallas» |

**Decidido en `decisiones-code.md` pero sin nota en la ficha ni fila en `para-el-project/03`** (el código hace lo decidido; falta que el doc lo sepa):

| # | Vista · ficha | Dice la ficha | Hace el código (decisión) | Propuesta |
|---|---|---|---|---|
| C49 | 3a · ficha 3, l.529 y leyenda 3e-1 | «Guardar en una categoría eliminada → dispara 3e» | Solo al **crear o mover** el plato a una categoría eliminada (`CartaRepository.kt:148–158`, `esNuevoOMovido`); editar uno que ya vive en ella se guarda sin aviso (**P62 → A**, l.154) | Fila en 03: ficha 3 · 3e y wireframe 3e-1 «al crear o mover» |
| C50 | 3a (3d) y 2b (2e) · ficha 3, l.469 y 487; ficha 2, l.671 | «Apagarlo elimina el plato y dispara 3d» (siempre confirmación) | El aviso R6 **solo sale si hay mesas afectadas**; sin ninguna, se elimina directamente (`PlatoActivity.kt:131–147`, `CategoriaBottomSheet.kt:106–123`; 5.10, «3d solo con mesas afectadas», l.341) | Fila en 03: «si ninguna mesa lo tiene pendiente, se elimina sin aviso» |
| C51 | 3a · ficha 3, l.535 | «Al guardar y volver… resalta el plato un segundo» (sin marca de nivel) | No hay resaltado (P14: nivel 3; la ficha 2 l.652 sí lo marca) | Nota «nivel 3, no implementado» |
| C52 | 1e · ficha 1, l.58 y 122; leyenda 1e #1 | «PIN actual, y después el nuevo dos veces»; «no se llega a pedir el nuevo» | Los tres campos a la vez; al Aceptar: actual → coinciden los nuevos → guardar (D18, P46 B; la fila del 1 oct en 03 ya lo cubre) | Al corregir la ficha 1, cambiar también «y después» |
| C53 | 1b · ficha 1, l.55 | «Si lo olvidas, no se puede recuperar» | «…: habrá que reinstalar la app.» (D1) | Añadir a la fila de textos de 03 |

Ya declarado y cubierto por `para-el-project/03`, verificado en el código (coincide): pastilla «Carrito · N · total» (D7), TOTAL fijo encima de Enviar y de Dar la cuenta, «€» fuera de la caja y Cambio en blanco (P187 A), título «Resumen de ingresos» y pie en dos etiquetas (D5/D6), fecha larga de 2g, caja de 2a ≈340 dp (P161), *Anular* en color de texto, atenuado al 80 %, «¿Quieres recuperar X? Platos que volverán a la carta: N» (P9/P164 B), singular de 3d (D15), nombre repetido sin mayúsculas (P124). La fila «Volver a activar un plato… agotado temporal» (ficha 3, l.531) es vocabulario del nivel 2 (activar tras agotado), no un error.

## Comprobado y coincide (no hace falta volver a mirarlo)

1. `values-en/strings.xml`: 111 claves = las 111 traducibles de `values/`; marcadores y `plurals` iguales; las 137 claves de ES se usan al menos una vez en `src/main` (ninguna huérfana).
2. `estado-nivel.md`: los 29 RF del nivel 1 tienen código (lista RF → clase en el apartado 5 del informe del ayudante, reutilizada aquí: RF-01 `CrearPinFragment` · 02 `PinDialog` · 03 `CambiarPinDialog` · 04 `CategoriaAdapter` + `FilaPlatoAdapter` · 05 `CategoriaBottomSheet` · 06 `MesasAfectadasDialog.abrirCategoria` · 08 `PlatoActivity` · 09 `ImageStore` · 10 `pintarAlergenos` · 11 `abrirPlato` · 12 `preguntarMover/preguntarRecuperar` · 20 `ui/resumen/` · 24 `SelectorFragment` · 25 `motivoPuertaCerrada` · 26 `abrirElegirMesa` · 28 `guardianSalir` + `PinDialog` · 29 `CartaFragment.saltarA` · 30 `FichaPlatoFragment` · 36 `Carrito` · 37 `enviar` · 38 `intentarSalir` · 40 `RejillaMesasFragment` en Cuenta · 41 `ComandaFragment` · 42 `anular` · 43 `ReciboFragment` · 44 `cambio` · 45 `cobrar` · 46 `quitarLinea` (R7) · 50 `Precarga`); RF-50 describe exactamente `Precarga.cargar`; las notas S4–S12 coinciden.
3. `spec+doc-pruebas.md`: los 24 `@Test` (21 + 3) y los nombres de clases y funciones existen y prueban lo que dice el plan; ninguna prueba debilitada; las 40 fechas y resultados cuadran con `pruebas-pasadas/s13.md` y la ficha S13 (incluidas las horas 16:40 y 16:45).
4. `juego-de-datos.md` §2: los ~26 literales entre comillas y la tabla de botones reales (2.0) coinciden con `strings.xml` y con los diálogos del código.
5. Ficha S13: cada número verificado (alpha 0,8; 7 layouts con `maxLines`; autosizing solo en `item_mesa.xml`; 8 + 1 + 14 + 3 = 26 `translatable="false"`; 111 claves EN; 21 + 3 `@Test`; `minHeight="48dp"`; `importantForAccessibility="no"` en la flecha de 5b).
6. `decisiones-code.md` 5.6–5.17: cada archivo, método y número que nombra sigue siendo verdad en el código (salvo C25–C27).
7. `CLAUDE.md`, «Comandos y mapa»: paquetes y comandos correctos.
8. `README.md`: stack, versiones, paquetes, comandos, manifiesto sin `INTERNET`, carpetas de `docs/` que cita (todas existen).
9. `para-el-project/03`: las filas de la S13 están.
10. Lo del 6 oct (`2026-10-06-coherencia.md`): C02, C03, C05 y C06 aplicados; C04 (ficha S8) se dijo en la S9; **C07 sigue pendiente** (es C06 de hoy).
11. Las 29 P-M y las 11 P-C anotadas con fecha 2026-10-08, como pide el spec 11 para la S13.

## Resumen para Daniel (5 líneas)

- El código y los documentos **dicen lo mismo en lo que importa**: reglas, tablas, pruebas, estado del nivel, decisiones. Ningún documento afirma que la app haga algo que no hace.
- Lo que está viejo son **inventarios y cabeceras**: `textos-ui.md` (el borrador de la sesión 00, con 24 claves que ya no existen), el árbol de paquetes del spec (la tachadura prometida para la S13 no se puso), la cabecera del plan de pruebas («las columnas siguen vacías») y el README («se construye por sesiones»).
- Lo único que afecta a la **memoria** es C02: falta la fila S13 en `para-el-project/02-memoria.md` con la pasada final hecha y anotada por Claude. Es una fila; la escribo cuando digas.
- Casi todo es mecánico y lo hago yo en el chat de arreglos (son documentos): unas 50 líneas con tachadura, nota o fila nueva. Las decisiones tuyas son tres: C01 (qué hacer con `textos-ui.md`: A regenerarlo desde `strings.xml`, B corregir las 23 líneas, C declararlo borrador), C02 (A fila S13 ahora, B además revisar tú las capturas) y C46 (si Android mata la app en Pedir: A corregir la ficha, B hacer que vuelva al selector, C nada).
- Las 14 vistas del nivel 1 hacen lo que dicen sus fichas y sus dibujos. Las fichas cerradas del diario no se tocan; la guía S13 sí (tachaduras P198).

## Aplicado

**8 oct 2026, el mismo día, en un chat con Opus** (detalle en `2026-10-08-cambios-aplicados.md`; decisiones en `decisiones-code.md` 5.18):

- **C01 → P204 A:** `docs/textos-ui.md` regenerado desde los dos `strings.xml` (143 claves, con inglés y dónde se usan, y cómo quedaron D1–D20); el borrador, a `docs/guias/textos-ui-borrador-s00.md`.
- **C02 → P205 B:** fila S13 en `para-el-project/02-memoria.md`; **pendiente que Daniel revise las capturas** y se ponga la fecha.
- **C03–C16:** tachaduras y notas en el spec, incluida la del árbol del apartado 3 (C06, la C07 del 6 oct). Además, P198 en la fila S13 del spec y el límite de P201 en R8.
- **C17–C24:** una fila «complemento S6–S13» en `para-el-project/01` (con H11).
- **C25–C27:** en `decisiones-code.md` (AGP 9.3.3, C07 hecho, el portero en 2g y en 1d).
- **C28–C30:** la cifra de pruebas en `02` y las filas del `LEEME` de `para-el-project`, recontadas (30 / 30 / 29).
- **C31–C36:** en `spec+doc-pruebas.md` (C36 también).
- **C37–C38:** en `juego-de-datos.md`.
- **C39–C43:** en `README.md` y `readme-borrador.md`.
- **C44–C45:** en la guía S13. La respuesta a la pregunta 11 del plan (el orden S13.5 / S14 / memoria) **no se apuntó**: Daniel la quitó de este chat.
- **C46 → P206 A**, y **C47–C53**, en `para-el-project/03`.
