# Cambios aplicados tras la revisión del 1 oct — para el chat que los revise

Fecha: 2 de octubre de 2026, 00:10–00:40. Los hizo Claude (Fable 5.1) **por orden de Daniel** («aplica los cambios que propones»), con sus respuestas a las 17 preguntas numeradas: 1A, 2A, 3A, 4A, 5A, 6A, 7B, 8A, 9A, 10B, 11A, 12 «se pregunta al cerrar», 13A, 14A, 15 → nivel 2, 16 → se ve al llegar, 17 → sin commit. Las decisiones están registradas como **P145–P159** en `docs/decisiones-code.md`, apartado 5.8. Los tres informes de origen están en esta misma carpeta.

**Estado al terminar:** `assembleDebug` y `testDebugUnitTest` en verde; **21 pruebas** de `test/` (P-C-01 a 05, 09, y las nuevas P-C-10 y P-C-11). **Sin commit** (17 → C). No se ha probado en el emulador (estaba apagado): el chat que revise debería ejecutar P-M-01, 02 y 03 (crear PIN, entrar en el Panel, cambiar PIN) y mirar que 1a y 1b se ven igual que antes.

**Cómo revisar:** `git status --short` da la lista; `git diff` enseña cada cambio. Lo que no convenza se deshace archivo a archivo con `git checkout -- <archivo>` (está permitido para archivos sueltos) o se corrige. Daniel tiene que **entender cada línea** (regla 2): cada punto de abajo lleva el porqué en una frase.

---

## A. Código de la app (`app-ako/app/src/main`)

| Archivo | Qué cambió | Por qué (decisión) |
|---|---|---|
| `AndroidManifest.xml` | `android:allowBackup="false"`; fuera `dataExtractionRules` y `fullBackupContent` y el `xmlns:tools` que no se usaba. Borrados `res/xml/backup_rules.xml` y `res/xml/data_extraction_rules.xml` | **P151 (1A).** Con la copia de Android activa, reinstalar en un móvil real restauraba el PIN y la base de datos: el que olvidara el PIN se quedaba fuera para siempre. Ahora reinstalar borra todo, como dicen 1b, el spec 8 y el Anexo II |
| `ui/comun/ComprobadorPin.kt` (nuevo) | Interfaz con `suspend fun comprobarPin(pin): Boolean` | **P145 (13A).** El diálogo del PIN deja de depender de `SelectorViewModel`; en la S8 `PedidoActivity` implementa la misma interfaz y reutiliza `PinDialog` sin tocarlo |
| `ui/selector/SelectorActivity.kt` | Implementa `ComprobadorPin` delegando en su ViewModel; los insets incluyen `Type.ime()` (el teclado) | P145; **H25**: con borde a borde, la pantalla no se encogía al salir el teclado y *Aceptar* podía quedar tapado |
| `ui/selector/PinDialog.kt` | Pide `requireActivity() as ComprobadorPin`; `dismissAllowingStateLoss()` en vez de `dismiss()` | P145; **P146 (2A)**: si se pulsa Home mientras se pica el PIN, `dismiss()` lanzaba `IllegalStateException` y cerraba la app |
| `ui/selector/CrearPinFragment.kt` | `commit(allowStateLoss = true)` al pasar a 1a; al crearse la vista, si ya hay PIN pasa a 1a | **P146.** Mismo cierre por Home; y si Android rehacía 1b justo al guardar, el segundo *Aceptar* hacía saltar el `check` de `crearPin` |
| `ui/panel/CambiarPinDialog.kt` | `dismissAllowingStateLoss()` | **P146** |
| `ui/panel/PanelActivity.kt` | Insets con `Type.ime()` | **H25** |
| `datos/dao/ComandaDao.kt` | `mesasConCategoriaPendiente`: `AND p.activo = 1`; `lineaPorId` devuelve `LineaComanda?` | **P147 (3A)**: el aviso 2e contaba platos ya eliminados (P55 decía solo activos). **P152**: un id que no existe ya no cierra la app |
| `datos/dao/CategoriaDao.kt` | `porId` devuelve `Categoria?` | **P152**: igual que `ProductoDao.porId` |
| `datos/repositorios/CartaRepository.kt` | `guardarCategoria`: `trim` y nombre no vacío; al editar conserva `esPorDefecto` **y `activo`** guardados. `guardarPlato`: carga el plato guardado siempre que `id != 0`, al editar conserva `activo`, `alergenos.distinct()`, `checkNotNull` si la categoría o el plato no existen. `eliminarCategoria`/`recuperarCategoria`: `?: return` | **P150 (4A)**: eliminar y recuperar solo por `eliminar…`/`recuperar…`, donde la pantalla avisa (R6). **H12, H13, P152** |
| `datos/repositorios/ComandaRepositoryReal.kt` | `enviarCarrito` recorre `carrito.lineas.toList()`; `quitarLinea` devuelve `false` si la línea ya no existe | **H11**: copia por si la pantalla toca el carrito mientras se envía. **P152**: doble toque en *Quitar* |
| `dominio/Carrito.kt` | `anadir` devuelve `false` y no añade nada si `cantidad < 1` | **P148 (5A)**: el mínimo de R4 no tenía dueño; por arriba sigue recortando a 99 (P74) |
| `dominio/Hash.kt` | `coincide` compara con `MessageDigest.isEqual`; `receta.clearPassword()` tras picar | **H14**: buena práctica OWASP, dos líneas; P-C-05 sigue igual |
| `seguridad/PinStore.kt` | `existe()` exige sal **y** hash; `coincide` devuelve `false` si la sal no es Base64 | **H15**: un archivo roto no deja fuera para siempre ni cierra la app |
| `datos/Precarga.kt` | `cargar` entero en `db.withTransaction { }`; comentario en `onCreate` sobre las pruebas de la S11; comentario «ticket» → «resguardo» | **P149 (6A)**: todo o nada. H10, H23 |
| `res/values/themes.xml` y `values-night/themes.xml` | `materialAlertDialogTheme` = `ThemeOverlay.AKO.Dialogo` (parent `ThemeOverlay.Material3.MaterialAlertDialog`) con `colorPrimary = ?attr/colorOnSurface` | **P153 (7B)**: el naranja como **texto** (Aceptar/Cancelar y etiqueta del campo en 1c/1e) daba 3,75:1; los botones rellenos siguen en `#C75000` |
| `res/layout/fragment_crear_pin.xml`, `fragment_selector.xml` | Envueltos en `ScrollView` con `fillViewport="true"` (el `ConstraintLayout` pasa a `wrap_content`) | **H25**: con letra grande o pantalla baja, 1a y 1b no cabían |
| `res/layout/activity_panel.xml` | `contentDescription="@string/comun_atras_cd"` en «← Atrás» | **H26**: el lector de pantalla lee «Atrás», no «flecha» |
| `res/values/strings.xml` | `translatable="false"` en `app_name` y las 17 cadenas de la precarga; cadena nueva `comun_atras_cd` | **P100** (decidido el 25 sep y sin aplicar); H26 |
| `res/values/colors.xml` | Fuera `black` (sin uso) | P115 |
| `datos/dao/MesaDao.kt` | Llave de cierre en su línea | Estilo (H24) |
| Borrados: `test/…/ExampleUnitTest.kt`, `androidTest/…/ExampleInstrumentedTest.kt`, `main/keepRules/rules.keep` | Restos de la plantilla del asistente | **P115**; las fichas contaban «15 pruebas» con la de `2 + 2 = 4` |

## B. Pruebas (`app-ako/app/src/test`)

| Archivo | Qué cambió | Por qué |
|---|---|---|
| `DaosFalsos.kt` | `CategoriaDaoFalso(activa)` configurable; `ProductoDaoFalso(guardado)` registra `productoRecibido` y `marcasRecibidas`, `actualizar` y `porId` ya no son `TODO()`; firmas nulables | Para poder probar la cadena 3e y afirmar «no guarda nada» (H19, H20) |
| `CartaRepositoryTest.kt` | P-C-09 afirma el precio recibido y que con −100 no llega ni plato ni marcas; **P-C-10** nueva: 5 pruebas de la cadena 3e (nuevo, movido, editado sin mover, `aunqueCategoriaEliminada`, `activo` conservado) | H19, H20, P150 |
| `CarritoTest.kt` | **P-C-11** nueva: cantidad < 1, `cambiarCantidad` fuera de rango y de un plato ausente, `quitar` dos veces | H21, P148 |
| `HashTest.kt` | `elHashNoContieneElPin` con sal fija | H22: sin azar (antes podía fallar una de cada 420 000 veces) |

## C. Configuración de Claude Code y skills

| Archivo | Qué cambió | Por qué |
|---|---|---|
| `.claude/settings.json` | `permissions.deny` con `git commit --amend`, `git restore`, `git checkout .`, `git push --force-with-lease`, `git rebase`, `git stash drop/clear`, `rm -fr`, `rm -r -f`, `rm -f -r` | **F04**: CLAUDE.md prometía que `--amend` estaba prohibido ahí y no lo estaba |
| `CLAUDE.md` | Regla 9: el coautor se pasa con un segundo `-m`; lista de prohibidos completa | **F05** |
| `.claude/skills/seguridad/SKILL.md` | 2.3: `git add -A` solo tras los puntos 1 y 2 (**P154, 9A**); frase del correo personal reescrita (F17) | Contradecía a tres skills |
| `.claude/skills/cerrar-sesion/SKILL.md` | Paso 7 con el comando completo (coautor con `-m`); paso 4: repasos al PDF (F06); campo «Contexto al cerrar» (P156); 8c: pregunta de repaso (P157) y «salvo que Daniel diga que se envíe sin esperar» (F16) | F05, F06, P156, P157, F16 |
| `.claude/skills/verificar/SKILL.md` | Paso 6 con el coautor; paso 1: comprobar paquete ↔ carpeta y una sola línea `package` antes de compilar (**P155, 10B**) | F05; siete pegados en el archivo equivocado en tres sesiones |
| `.claude/skills/relevo/SKILL.md` | 4b remite a `cerrar-sesion` 8c (F14); fuera la regla «seis piezas o tercera hora», se anota el % de contexto (**P156, 11A**) | F14, F08 |
| `.claude/skills/como-trabajamos/SKILL.md` | Lista de «por volver a preguntar» sin P46 y P64 (F12); nota de P156; regla P157 (preguntar al cerrar qué repaso quiere); historial completo movido a `docs/como-trabajamos-historial.md` (de 158 a 123 líneas) | F12, F08, F09, F14 |
| `.claude/skills/abrir-sesion/SKILL.md` | Umbrales sin «seis piezas» | P156 |
| `.claude/agents/revisor.md` | Ruta `app-ako/app/src/main` | F13 |
| `docs/lecciones-claude.md` | Fuera cuatro lecciones (una sola vez o duplicadas en una skill); nueva: respuestas tecleadas en un XML de Android Studio | F15 |
| `docs/plantilla-diario.md` | Campo «Contexto al cerrar» | P156 |

**Pendiente de pegar por Daniel (Claude no puede editar los hooks):** `.claude/hooks/comprobar-commit.sh`, bloques F02 (archivos nuevos sin añadir) y F03 (bloquear si falla python) en `2026-10-01-skills.md`. Hasta entonces, el hook sigue sin ver los archivos nuevos cuando `git add -A` y el commit van en el mismo comando.

## D. Documentación

| Archivo | Qué cambió | Por qué |
|---|---|---|
| `docs/decisiones-code.md` | P3 y P23 tachadas (F10, F11); apartado 5.8 con P145–P159 y las respuestas de Daniel; 13 filas nuevas en «Para el Project» (P103 + las de hoy) | Sin ellas la memoria no coincidiría con lo hecho |
| `docs/spec-claude-code.md` | Nota en la cabecera (**P158, 14A**: se edita a mano; manda `decisiones-code.md` si discrepan); R6, R10, P-C-05 y la fila S1 con tachaduras y lo vigente | Cuatro frases viejas que podían hacer rehacer P128/P120 |
| `docs/spec+doc-pruebas.md` | P-M-29 sin el paso de `etiqueta` (P09); P-C-09 corregida y con su nueva observación; filas **P-C-10 y P-C-11** | P09, P10, H19–H21 |
| `docs/guias/sesion-06.md` | Huecos 8 y 11 con tres opciones y fuente (P11); el hueco 8 queda resuelto por P147 y el 11 por P152 (confirmar en el Plan Mode) | P112 |
| `docs/guias/sesion-13.md` | Apartado 5 de notas: revisión local si pasa del 5 nov (P12), Scanner por la rama B (P98), P101 al nivel 2 (**P159**), `allowBackup` hecho | P12, P04, P159 |
| `docs/revisiones/` (nueva) | LEEME, los tres informes del 1 oct (cada uno con su apartado «Aplicado») y este documento | Daniel lo pidió |

## E. Lo que NO se hizo (y por qué)

- **H16** (la APK que se entrega es `debug`): no se cambia nada; se declara en la memoria («Para el Project»).
- **H09 / P01** quedó resuelto por P145 (no hace falta hueco en la S8).
- **P06** (hito del 8 nov y recortes): Daniel decidió verlo al llegar; la estimación queda en `2026-10-01-plan.md`.
- **P07 y P08**: las guías ya lo preveían; nada que cambiar.
- **Commit**: Daniel eligió no hacerlo hasta que otro chat lo revise. Cuando lo haga: `S5: revisión independiente del 1 oct y cambios aplicados` con el coautor por `-m`, y antes la skill `seguridad` (`git status --short` y el grep de claves ya pasados hoy: limpio).
- **Emulador**: no se ha probado la app en pantalla (estaba apagado). P-M-01, 02 y 03 deberían repetirse en el chat de revisión (cinco minutos) porque tocan 1b, 1c y 1e.

---

## F. Revisión de estos cambios (2 oct, chat de revisión con Opus 5.5)

- **`git diff` contra este documento:** los 44 archivos coinciden con lo descrito en A–D; nada de más ni de menos.
- **Compila y pruebas:** `assembleDebug` y `testDebugUnitTest` en verde, **21 pruebas** (P-C-01 a 05, 09, 10 y 11).
- **Emulador (ejecutado por Claude con `adb`, instalación limpia):** precarga completa dentro de la transacción (2 categorías, 1 plato, 14 alérgenos, 60 mesas); **P-M-01** (1234/1235 → aviso y campos vacíos; 1234/1234 → 1a), **P-M-02** (9999 → «PIN incorrecto» y el diálogo sigue; 1234 → Panel) y **P-M-03** (actual 0000 → aviso sin tocar los nuevos; 1234 → 5678; después 1234 no entra y 5678 sí; vuelta a 1234) pasan. 1a y 1b se ven igual que antes; con el teclado abierto, la barra inferior del Panel sube por encima (H25).
- **Hallazgo nuevo (Baja, P153 a medias):** en 1c y 1e los botones *Cancelar*/*Aceptar* ya salen en color de texto normal, pero la **etiqueta y el borde del campo enfocado siguen naranjas** (3,75:1). Causa: `DialogPinBinding.inflate(layoutInflater)` dentro de `onCreateDialog` usa el tema de la Activity, no el del diálogo. Arreglo posible: inflar con `LayoutInflater.from(constructor.context)` del `MaterialAlertDialogBuilder`. Se plantea a Daniel (P112) en la S6 o en la S13 con el contraste.
- **Comprensión (regla 2):** Daniel contestó bien el bloque 1 (pantallas del PIN y manifiesto). Los bloques 2 (datos), 3 (recursos y tema) y 4 (pruebas y configuración) quedan por explicar: se terminan al abrir la S6, antes de la primera pieza.
- **Uso de IA:** el código de A y B lo escribió Claude (Fable) por orden de Daniel, no lo tecleó Daniel; así consta en la memoria.
- **Sigue pendiente de pegar por Daniel:** los bloques F02 y F03 del hook `comprobar-commit.sh` (`2026-10-01-skills.md`).
