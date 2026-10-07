# Cambios aplicados tras la revisión del 6 oct — para la S9 y para el chat que los revise

Fecha: 7 de octubre de 2026 (chat de aplicación, no es sesión de código: la S9 empieza en otro chat). Los hizo Claude (Fable 5.1) **por orden de Daniel** («aplicamos cambios y las cosas ahora en este chat»), con sus respuestas a las 12 preguntas: 1B, 2B, 3A, 4B, 5A, 6 opción propia (Claude pasa las pruebas con la lista aprobada antes), 7A, 8C con partición por documentos, 9A, 10B, 11C, 12A. Las decisiones están registradas como **P171–P182** en `docs/decisiones-code.md`, apartado 5.12. Los cinco informes de origen están en esta misma carpeta, cada uno con su apartado «Aplicado».

**Estado al terminar el código:** `assembleDebug` y `testDebugUnitTest` en verde; **21 pruebas** de `test/` (P-C-01 a 05, 09, 10 y 11); `lintDebug` con 23 avisos (los 22 de la revisión más `DataExtractionRules`, explicado en P173; ninguno de accesibilidad ni de textos a mano). **Emulador** (`Pixel_6_API_34`, con los datos de la pasada de la S8, **sin borrarlos**: `ako.db` y `pin.xml` siguen con fecha del 6 oct): el doble toque en «12 · Entrecot», en «+ Plato» y en el lápiz abre una sola pantalla u hoja (H01); **P-M-02** y **P-M-16** pasan (anotado en el plan); la hoja 2b de Carnes se abre con su nombre y su interruptor (H04); el formulario del plato sobrevive a `am kill` con lo tecleado, la categoría, Gluten desmarcado y el aviso «¿Salir sin guardar?» (H10); 1c en modo noche con los botones en color de texto (H06). **Commit:** `S8: revisión del 6 oct aplicada` (P182 A), al final del chat.

**Cómo revisar:** `git show --stat HEAD` da la lista; `git show HEAD` enseña cada cambio. Daniel tiene que **entender cada línea** (regla 2): el código se explicó por bloques con una pregunta de comprensión cada uno en el mismo chat (queda en el «Uso de IA» de la ficha S9), y cada punto de abajo lleva el porqué en una frase.

---

## A. Código de la app (`app-ako/app/src/main`)

| Archivo | Qué cambió | Por qué (decisión) |
|---|---|---|
| `ui/comun/GuardaDobleToque.kt` (nuevo) | El «portero que mira el reloj»: `permite()` apunta `SystemClock.elapsedRealtime()` y devuelve `false` si el toque anterior fue hace menos de 500 ms | **P171 (1B), H01.** Dos toques rápidos en un plato del Panel abrían dos `PlatoActivity` y la segunda podía pisar lo guardado en la primera. Daniel eligió la guarda de «último toque» (receta de Stack Overflow) en vez de `singleTop` |
| `ui/panel/PanelActivity.kt` | Un `portero` para la pantalla; pasan por él el lápiz, «+ Plato», tocar un plato, el «+» de la barra, *Resumen de ingresos* y *Cambiar PIN* | **P171.** Una guarda por pantalla, compartida por sus botones, en vez de una por botón [Claude]: menos código que entender |
| `ui/selector/SelectorFragment.kt` | `portero` en *Propietario* y en la caja provisional de *Cuenta* (*Pedir* ya se apaga mientras pregunta, P142) | **P171** |
| `ui/pedido/PedidoActivity.kt` | `portero` al principio de `intentarSalir()` (cubre *Salir*, el Atrás del sistema y el aviso RF-38) | **P171** |
| `ui/pedido/PedidoViewModel.kt` | `enviar()`: `enviarCarrito` y el carrito nuevo dentro de `try { … } finally { enviando = false }` | **H03** (mecánico): si `enviarCarrito` fallara a mitad, la bandera no se quedaría encendida y *Enviar* seguiría funcionando |
| `ui/panel/CategoriaBottomSheet.kt` | Campo `categoria`; al editar, `observe` de `categoriasConPlatos` con `viewLifecycleOwner` que pinta **una sola vez** cuando llega la categoría; `actualizarGuardar()` (Guardar apagado al editar hasta que llega) | **P172 (2B), H04.** Antes miraba el tablón una vez al abrirse: si Android rehacía la hoja antes de que llegara la lista, se abría como «Nueva categoría». Ahora espera a la lista |
| `AndroidManifest.xml` | `xmlns:tools`; en `<application>`, `android:dataExtractionRules="@xml/reglas_extraccion"` y `tools:targetApi="31"`; comentario del Panel sin «provisional en la S5» | **P173 (3A), H05**: en Android 12+ algunos fabricantes pasan los datos de móvil a móvil aunque `allowBackup` esté apagado. **H09** (mecánico) |
| `res/xml/reglas_extraccion.xml` (nuevo) | `<cloud-backup>` y `<device-transfer>` excluyen los cajones enteros `sharedpref` (el PIN) y `database` (`ako.db`) con `path="."` | **P173.** Todo lo que guarda la app; así no hay que enumerar `ako.db-wal` ni `-shm` [Claude]. Lint sigue avisando (`DataExtractionRules` pide `fullBackupContent` para Android < 12): no hace falta, `allowBackup="false"` ya apaga todo ahí |
| `ui/plato/PlatoViewModel.kt` | Constructor `PlatoViewModel(cartaRepository, cajaFuerte: SavedStateHandle)`; `categoriaElegidaId`, `formularioRelleno` y `hayCambios` leen y escriben en la caja fuerte; `alergenosMarcados` pasa a `List<Long>` guardada como `LongArray`, con `marcarAlergeno(id, marcado)`; la `Factory` usa `createSavedStateHandle()` | **P174 (4B), H10.** La libreta sobrevive a recrear la pantalla pero no a que Android mate la app; la caja fuerte sí. Comprobado con `am kill` |
| `ui/plato/PlatoActivity.kt` | `rellenar` marca los alérgenos del plato con `marcarAlergeno(id, true)`; la casilla llama a `marcarAlergeno(alergeno.id, marcada)` | **P174** (el conjunto ya no se toca desde fuera) |
| `res/values-night/themes.xml` | Solo `Base.Theme.AKO`; fuera las tres copias de `ThemeOverlay.AKO.Dialogo`, `Widget.AKO.CampoTexto` y `ThemeOverlay.AKO.CampoTexto` (viven en `values/`) | **P175 (5A), H06.** Eran copias exactas: cambiar una y olvidar la otra rompería el modo noche. Comprobado 1c en noche |
| `res/values/strings.xml` | `mesa_ocupada_cd` = «%1$s, %2$s» | **P175, H07**: la única frase de interfaz que no salía de `strings.xml` (RNF-19) |
| `ui/comun/MesaAdapter.kt` | `tarjeta.contentDescription = contexto.getString(R.string.mesa_ocupada_cd, textoMesa, total)` | **H07** |

Finales de línea: los archivos tocados se guardaron con CRLF como el resto del proyecto (Android Studio); el diff real son unas 160 líneas nuevas y 80 quitadas.

## B. Pruebas (`app-ako/app/src/test`)

Sin cambios: las 21 pruebas siguen iguales y en verde. `PlatoViewModel` y `CategoriaBottomSheet` no tienen pruebas de código (no las pide el plan).

## C. Configuración de Claude Code y skills

| Archivo | Qué cambió | Por qué |
|---|---|---|
| `CLAUDE.md` | «Para el Project» apunta a `docs/para-el-project/` (un documento por área) | **P178** |
| `.claude/skills/como-trabajamos/SKILL.md` | Dos reglas nuevas con las palabras de Daniel: las pruebas manuales las pasa Claude con la lista aprobada antes (**P176**); lista viva de pendientes de entender con 10 min al abrir (**P177**); «Para el Project» por áreas (**P178**); línea en el historial | F01/C01, R01, R03 |
| `.claude/skills/cerrar-sesion/SKILL.md` | Paso 3 (la lista de pruebas con pasos → visto bueno → Claude con `adb`), paso 4 (`pendientes-de-entender.md`), paso 5 (filas nuevas a `docs/para-el-project/`), paso 8 | P176, P177, P178 |
| `.claude/skills/abrir-sesion/SKILL.md` | Paso 4b: diez minutos de «explícamelo tú» sobre dos conceptos de la lista | **P177** |
| `docs/lecciones-claude.md` | Las seis lecciones de pegado fundidas en dos («archivo entero» y «trozo suelto»); fuera `local.properties` (S1) y el hook con `-F -` (ya no pueden repetirse); dos nuevas del 7 oct (heredocs largos; `uiautomator` y el `contentDescription` de las mesas) | **F02**: de 25 lecciones a 20 (de 32 a 30 líneas) |

**Hooks:** nada que pegar. F04 (patrones `password *=` y `storePassword` en el hook) queda como opcional para la S13.

## D. Documentación

| Archivo | Qué cambió | Por qué |
|---|---|---|
| `docs/decisiones-code.md` | Apartado **5.12** con P171–P182 y la fila «Sin pregunta»; el apartado 6 queda como regla e índice de `docs/para-el-project/` | P171–P182, **P178** |
| `docs/para-el-project/` (nueva) | `LEEME.md` y tres documentos por área (`01-diagrama-de-clases-y-bd.md`, 24 filas · `02-memoria.md`, 20 · `03-pantallas-textos-y-requisitos.md`, 13): las 45 filas de antes movidas tal cual, más las de esta revisión (P171–P174, P176, P177, H02, H03, H07, H08, H11, F05/R04, H16 y la propia revisión) | **P178** (R03) con el matiz de Daniel: él elige qué documento lleva al Project |
| `docs/diario/pendientes-de-entender.md` (nueva) | 15 conceptos de las fichas S3–S8 y dos de esta revisión, con una pista de la vida real y la columna «explicado el» | **P177** (R01, el único Alta) |
| `docs/guias/sesion-09.md` | Apartado 0 reescrito: los **ocho huecos abiertos** (1–8) con tres opciones, fuente y recomendación, listos para contestar por lotes; cerrados el 9 (P83), 10 (**P171**), 11 (P167), 13 (**P180**) y el 12 fundido con el 2; prerrequisitos con el portero y la caja fuerte; piezas 2, 3, 7, 9, 10, 12, 15, 16 y 17–19, cierre, «defender» y «riesgos» al día; sin relevo previsto (**P179 A**, **P181 C**) | P01, P02, P06, F01 |
| `docs/guias/sesion-10.md`, `sesion-11.md`, `sesion-12.md`, `sesion-13.md`, `juego-de-datos.md` | S10: quién pasa las pruebas (P176). S11: Claude lanza `ArranqueTest` antes de la sesión (P05). S12: `android:id` del hueco de `activity_plato.xml` (P03). S13: H06/H07 hechos, H05 resuelto, C07 (tachadura del árbol del spec), F04 opcional, P181 C; piezas 9–10 con P176. Juego de datos: 3.0 y 4 con P176 | P176, P03, P05, C07, F04, P181 |
| `docs/spec+doc-pruebas.md` | Línea «Cómo se lee» (**C01/P176**); P8 en las *Entradas* de P-M-04 y P-M-17 (**C05**); P-M-02 y P-M-16 repetidas el 07/10/2026: Pasa | C01, C05 |
| `docs/textos-ui.md` | Anular: `comun_cancelar` / `comanda_btn_anular` (fuera `comun_si`) | **C06** |
| `README.md` | Once pruebas de código (ocho en `test/`, tres de Room) | **C02** |
| `LEEME.md` | Nota de cabecera; el hook y el coautor; la skill `seguridad`; 7 tablas y P158; las fichas S00–S08, `pendientes-de-entender.md`, `para-el-project/` y `revisiones/` | **C03** |
| `docs/revisiones/` | Este documento y el apartado «Aplicado» al final de los cinco informes | Daniel lo pidió (como el 2 oct) |

## E. Lo que NO se hizo (y por qué)

- **C04** (la viñeta P167 de la ficha S8 cita tres nombres del plan que no existen): la ficha S8 está cerrada; va como nota en la ficha S9 al abrirla (el primer mensaje de la S9 lo dice).
- **C07** (árbol de paquetes del spec 3, de la S3): tachadura en la S13 (anotado en su guía); el diagrama definitivo sale del código.
- **P180 B** (la rejilla de Cuenta tapada): se programa en la S9 (pieza 16); hoy no existe `CuentaActivity`.
- **P181 → C**: ningún cambio de calendario ni recortes.
- **F04** (patrones de contraseña en el hook): opcional, para la S13, y lo pega Daniel.
- **H16** (APK `debug`): solo se declara (fila en `02-memoria.md`).
- **Ficha del diario:** este chat no abre sesión; el «Uso de IA» de lo hecho hoy (código escrito por Claude por orden de Daniel y explicado por bloques) se escribe en la ficha S9 al abrirla, como se hizo el 2 oct con la S6.

---

## F. Comprensión (regla 2) y commit

- Daniel pidió (7 oct, comiendo) no contestar las preguntas en el chat: los cinco bloques, con su código, explicación, pregunta **y respuesta**, van en el PDF `docs/resumenes/2026-10-07-revision-6-oct-bloques.pdf` (fuera de Git), enviado por Gmail. Lo que no quede claro va a `pendientes-de-entender.md` y a los diez minutos de «explícamelo tú» al abrir la S9 (P177).
- Commit `S8: revisión del 6 oct aplicada` con el coautor, y push (P182 A); el identificador se anota en la ficha S9 al abrirla.
