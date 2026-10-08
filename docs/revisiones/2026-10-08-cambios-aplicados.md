# Cambios aplicados tras la revisión del 8 oct (después de la S13)

Fecha: 8 de octubre de 2026. Es un chat de aplicación, no una sesión de código, igual que el del 7 oct. Lo hizo Claude (Opus 5.5) siguiendo el guion `2026-10-08-plan-de-arreglos.md`, que escribió Fable 5.1 al terminar la revisión.

Daniel contestó al abrir: **1A, 2A, 3C, 4A, 5B, 6A, 7B, 8A, 9A**. La 10 (marcas `[Claude]`) se verá en la S13.5, y la 11 (orden S13.5 / S14 / memoria) la quitó: *«aquí lo que quiero es arreglar todo lo que encontró Fable y errores»*. La 12, **A**. Después decidió dos cosas nuevas que salieron al aplicar: **P209 B** (los avisos `SetTextI18n` de lint) y **P210 A** (dos líneas del mismo plato en 6b: se deja y se anota). Todo queda como **P199–P210** en `docs/decisiones-code.md`, apartado 5.18. Los dos informes de origen están en esta carpeta, cada uno con su apartado «Aplicado».

**Quién hizo qué:**
- **Daniel tecleó** (archivo entero, pegado en Android Studio) `PlatoActivity`, `CartaFragment`, `LineaAdapter`, `CategoriaAdapter`, `SelectorActivity`, `FichaPlatoFragment` y `Precarga`, y contestó la pregunta de comprensión de cada pieza.
- **Claude escribió** las líneas sueltas, los borrados, los comentarios y las cadenas de P202 (Daniel: *«aplica estos cambios»*), los cambios de las pruebas de Room y todos los documentos. Cada archivo de Daniel se comprobó con `/verificar`: igual al bloque dado, un solo `package` y compila. Antes de darle un archivo, Claude lo compiló en una copia.

**Estado al terminar:**
- `assembleDebug`, `testDebugUnitTest` (21) y `connectedDebugAndroidTest` (3) en verde.
- `lintDebug` con **35 avisos**, los de la S13. Relanzado desde cero, salieron 4 `SetTextI18n` que ya estaban; se arreglaron con P209 B.
- 14 pruebas en el emulador con la lista aprobada por Daniel (`docs/pruebas-pasadas/revision-08oct.md`, capturas en `docs/capturas/revision-08oct/`): todas pasan menos M1, que `adb` no consiguió provocar.
- **El emulador ya no tiene el montaje de la S13**: la instalación limpia de la prueba 13 lo borró (avisado y aprobado). Ako abre en 1b, sin PIN.

**Cómo revisar:** `git show --stat HEAD` da la lista; `git show HEAD` enseña cada cambio.

---

## A. Código de la app (`app-ako/app/src/main`)

| Archivo | Qué cambió | Por qué (decisión) |
|---|---|---|
| `ui/plato/PlatoActivity.kt` | `preguntarMover()` y `preguntarRecuperar()`: si `supportFragmentManager.isStateSaved`, no se abre la caja y se vuelve a encender *Guardar*. En `preguntarRecuperar`, después de contar los platos | **P199 A (M1).** Desde la S12 la espera de 3e incluye copiar la foto: Inicio en ese momento cerraba la app. [Claude] La comprobación va tras la última espera, no en la primera línea como decía el plan |
| `ui/pedido/CartaFragment.kt` | Campo `ultimaCarta`: si llega la misma carta, no se monta otra lista; `ultimaCarta = null` en `onViewCreated`. `categoriaActiva` se guarda en `onSaveInstanceState` y se lee en `onCreate`; `companion` con `CLAVE_CATEGORIA_ACTIVA` | **P200 A (M2)** y **H07.** [Claude] Sin el `null`, al volver de la ficha la carta salía vacía; leída en `onViewCreated`, la vuelta de la ficha resaltaría la primera categoría |
| `ui/pedido/FichaPlatoFragment.kt` | `alergenosAbiertos` a la caja fuerte, junto a la cantidad (`CLAVE_ALERGENOS_ABIERTOS`) | **H07** |
| `ui/selector/SelectorActivity.kt` | `portero = GuardaDobleToque()` y `if (!portero.permite()) return@…` en el oyente de la mesa tocada | **H02 (B3 del `revisor`)**: igual que `CuentaActivity` |
| `ui/cuenta/ComandaFragment.kt` | `if (viewModel.quitarLinea(linea.id)) cuenta.volverARejilla()` también en la rama que no es la última línea | **H09** (defensa) |
| `datos/dao/ComandaDao.kt` · `dominio/modelos/MesaConTotal.kt` | Fuera `m.numero AS numero` y `val numero` | **H10**: nadie lo leía |
| `ui/comun/LineaAdapter.kt` · `ui/panel/CategoriaAdapter.kt` | `contentDescription` con el nombre del plato (−, + y *Quitar*) y de la categoría (plegar o desplegar, y lápiz) | **P202 A (B4)**, RNF-13 |
| `res/values/strings.xml` · `values-en/strings.xml` | Seis cadenas de P202, en español e inglés; fuera `panel_desplegar_cd` (sin uso); `comun_numero` = «%1$d» (P209); comentarios al día | P202, P209, H04 |
| `ui/comun/MesaAdapter.kt` · `FichaPlatoFragment` · `PlatoActivity` · `ui/resumen/ResumenListaFragment.kt` | `getString(R.string.comun_numero, n)` en vez de `n.toString()` | **P209 B**: el aviso `SetTextI18n` de lint |
| `datos/Precarga.kt` | Todo dentro de `onCreate(db)`, con `ContentValues` y `db.insert(…, CONFLICT_ABORT, …)`: sin corrutina ni `cargar(db)` | **P203 B (H06; sustituye a P149 A)**: la precarga va en la misma transacción en la que Android crea la base; si se corta, la próxima vez se crea entera |
| `datos/dao/MesaDao.kt` · `datos/dao/PrecargadosDao.kt` | Fuera `insertarTodas` e `insertarAlergenos` (y su `import`) | P203: se quedaron sin uso |
| `ui/comun/Formato.kt` | Comentario del límite de los importes; referencia a `textos-ui.md` al día | **P201 C (B1)**, P204 |
| 15 archivos `.kt` y `.xml` más (`ComandaRepository`, `PedidoViewModel`, `CuentaViewModel`, `ResumenListaFragment`, `ComprobadorPin`, `PinDialog`, `FilaPlatoAdapter`, `MesaAdapter`, `RejillaMesasFragment`, `ReciboFragment`, `LineaVista`, `CategoriaAdapter`, `Calculadora`, cuatro layouts) | Comentarios en presente: fuera «Crece pieza a pieza», «en la S8/S9/S10», «hoy… en la S8»; la fecha de ejemplo de 2g en formato largo | **H04 (B5)**; los históricos («(S12)», «pieza 7») se quedan para la S13.5 |

## B. Pruebas (`app-ako/app/src/test` y `androidTest`)

| Archivo | Qué cambió | Por qué |
|---|---|---|
| `androidTest/…/CategoriaPorDefectoTest.kt` · `androidTest/…/repositorios/ComandaRepositoryTest.kt` | La base en memoria se construye con `.addCallback(Precarga(context))`, como la app, en vez de llamar a `Precarga(context).cargar(db)` | P203 B: `cargar` ya no existe; ahora las pruebas usan el mismo camino que la app |
| `test/…/repositorios/DaosFalsos.kt` | Fuera el doble de `insertarAlergenos` | P203 |

Las 21 de `test/` y las 3 de Room pasan.

## C. Configuración de Claude Code y memoria

Sin cambios en `CLAUDE.md`, en las skills ni en los hooks. En la memoria de Claude (fuera del repositorio): una nota nueva, «los chats de arreglos son solo para código y errores; nada de planificar la memoria ni la S13.5/S14» (Daniel, al quitar la pregunta 11).

## D. Documentación

| Archivo | Qué cambió | Por qué |
|---|---|---|
| `docs/textos-ui.md` (regenerado) · `docs/guias/textos-ui-borrador-s00.md` (el viejo, movido con `git mv`) | Las 143 claves de `strings.xml` con español, inglés, dónde se usan y su nota, por pantallas; cómo quedaron D1–D20; los textos que no son cadenas. El borrador lleva una nota de «superado» | **P204 A (C01)**. El borrador se conserva por su apartado 10 (textos del nivel 2 y 3) |
| `docs/spec-claude-code.md` | Tachaduras y notas con fecha: C03 (11 P-C), C04 (P98 B en vez del Scanner, tres sitios), la nota sobre el árbol del apartado 3 (C06, con C05), C07, C08, C09, C10, C11, C12, C13, C14 (dos sitios), C15, C16 (filas P-C-10 y P-C-11), P198 en la fila S13 y el límite de P201 en R8 | Coherencia; el spec nunca se reescribe |
| `docs/decisiones-code.md` | Apartado **5.18** (P199–P210 y «sin pregunta»); C25 (AGP 9.3.3), C26 (la tachadura C07 ya hecha), C27 (el portero en 2g y en 1d); cabecera de 4b | P199–P210 |
| `docs/para-el-project/01`, `02`, `03` y `LEEME.md` | 01: complemento S6–S13 (C17–C24, H11) y los cambios de código de hoy. 02: fila S13 (C02), la revisión del 8 oct, M2, H06, el límite B1, lint y P210; corregidas la fila H02 (M1) y la cifra de pruebas (C28). 03: C46–C53, P202, P201 y P204. LEEME: filas recontadas (C30) | Lo que tiene que llegar al doc |
| `docs/spec+doc-pruebas.md` | Cabecera (C31), *disponible* es nivel 2 (C32), P-M-11 esperado (C33), notas D1/D6/D7 (C34), los botones reales (C35), P-C-06 (C36); P-M-11, 15, 17, 19, 23 y 24 y P-C-06, 07 y 08 con «Revisión 08/10: Pasa» en *Observaciones* | C31–C36 y las pruebas de hoy |
| `docs/guias/juego-de-datos.md` | Fecha aaaa-mm-dd (C37) y el pie de 2g en dos etiquetas (C38) | Coherencia |
| `README.md` · `docs/guias/readme-borrador.md` | Nivel 1 terminado el 8 oct, el estado de las pruebas con `pruebas-pasadas/` y `capturas/`, las carpetas de `docs/` (sin `resumenes/`, que no se sube: está en `.gitignore`), `dominio/` y `EntradaAko` (C39–C42); el borrador, «superado» (C43) | Coherencia |
| `docs/guias/sesion-13.md` | Tachaduras P198 en la Release (C44) y el hueco de `Formato.fecha`, decidido (C45) | Coherencia |
| `docs/guias/strings-es-borrador.xml` · `LEEME.md` (raíz) | La ruta nueva del borrador de textos; la fila de `textos-ui.md` | P204 |
| `docs/pruebas-pasadas/revision-08oct.md` (nuevo) y su `LEEME.md` | La lista aprobada y lo que salió, prueba a prueba; en el índice, también la fila de `s13.md`, que faltaba | P176 |
| `docs/capturas/revision-08oct/` (nueva, 14 capturas y `LEEME.md`) y `docs/capturas/LEEME.md` | Las capturas de la pasada; **Daniel tiene que revisarlas** (P205 B) | P205 B |
| `docs/lecciones-claude.md` | Cuatro lecciones: el límite «de milisegundos» que se quedó viejo; `observe` + adaptador nuevo y dónde leer la caja fuerte en un Fragment de la pila; comprobar cada enlace antes de citarlo; relanzar lint desde cero antes de contar | Lo que costó hoy |
| `docs/diario/pendientes-de-entender.md` | Cuatro conceptos para la S14: `isStateSaved`, el tiempo de espera de `asLiveData`, el desbordamiento del `Int`, la precarga dentro de la transacción | P177 |
| `docs/revisiones/` | Este documento, el apartado «Aplicado» de los dos informes y `LEEME.md` | Como el 7 oct |

## E. Lo que queda abierto

- **P205 B:** Daniel revisa las capturas (`docs/capturas/accesibilidad/`, `ingles/` y `revision-08oct/`) y se pone la fecha en la fila S13 de `para-el-project/02`.
- **P228** (marcas `[Claude]`) y los comentarios históricos: en la S13.5.
- **M1** no se pudo provocar con `adb`: queda comprobado leyendo el código.
- La copia de seguridad de Ako que pidió Daniel se hace después del commit (zip de `C:\AKO` en su carpeta del Proyecto intermodular; no va al repositorio).
