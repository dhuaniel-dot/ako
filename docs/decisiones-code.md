> **Registro de decisiones tomadas en Claude Code (fase 6).** Nace en la sesión 00 (24 sep 2026) con las respuestas de Daniel a las preguntas de `ANALISIS-INICIAL.md` (numeradas P1–P39 **en ese archivo**; si alguna pasa al registro del Project, se renumera allí desde la P249). Cada sesión añade abajo las decisiones nuevas que no estén en el spec, con fecha y marca **[Claude]** si las propuso Claude. **Es el documento que lee un chat nuevo para no volver a preguntar lo ya decidido.** Manda el spec; esto lo completa donde el spec callaba o se contradecía.

# Decisiones de Claude Code — Ako

## 1. Plan y calendario

| Decisión | Qué se decidió | Consecuencia para el trabajo |
|---|---|---|
| P1 · horas por semana | Indefinido: Daniel trabaja a rachas y aprieta cerca del límite | El plan se mide por **hitos**, no por semanas. Se revisa cada dos sesiones |
| P2 · fecha objetivo | Semana del 23 de noviembre; el 6 de diciembre es colchón que no se cuenta | Hito interno: **nivel 1 cerrado el 8 de noviembre** |
| P3 · nivel 2 | Solo el incremento 1 (etiquetas y chips), y solo si el nivel 1 está cerrado el **31 de octubre** | Si no, todo el nivel 2 se declara *diseñado, no implementado* |
| P4 · memoria | Se escribe en la app de Claude (Project «Línea 2 Ako»); Claude Code solo genera lo que sale del código | Al cerrar la fase 6: diagrama de clases desde el código, tabla de pruebas, fichas del diario, `estado-nivel.md` |
| P5 · sesión 00 | La preparación del PC y las bases tienen ficha propia: `docs/diario/sesion-00.md` | La S1 empieza con el proyecto de Android Studio |
| P25 · Plan Mode | **[Claude]** Al abrir S4, S6, S8 y S9 | `abrir-sesion` lo propone en esas cuatro |

## 2. Diseño (huecos del spec cerrados)

| Decisión | Qué se decidió | Dónde se aplica |
|---|---|---|
| P6 · pruebas con prerrequisito | Una P-M que necesita algo de una sesión posterior se ejecuta en lo posible, se anota **Parcial** con fecha, y se repite entera cuando exista el prerrequisito; en la S13 pasan todas | `spec+doc-pruebas.md`, columnas de resultado; `cerrar-sesion` |
| P7 · precarga de ejemplo | Categoría **Bebidas** (activa, `orden` = 1, sin foto) con el plato **1 · Agua · 1,50 €** (sin descripción, sin alérgenos) | `Precarga` (S2); P-M-29 y P-M-14 |
| P8 · Bebidas en el juego de datos | **[Claude]** Al montar el juego de datos de las pruebas, Bebidas se **elimina** (interruptor apagado); se añade esa línea a la *Entrada* de P-M-04 y P-M-17 | `spec+doc-pruebas.md` (al ejecutar) |
| P9 · cadena 3e, segundo aviso | Literal: **«¿Quieres recuperar Carnes? Volverán a la carta sus 8 platos.»**, botón **Recuperar**. Sustituye a «reactivar» del spec 6 y del wireframe `dialogo-3e-2` | `strings.xml` (S7); anotar la corrección para el Project |
| P10 · wireframe 1e | Se deja con su vocabulario viejo; una línea en la memoria | — |
| P11 · precio negativo | `Validacion.precioValido()` lanza `IllegalArgumentException`; `ResultadoGuardado` tiene solo Ok · NumeroRepetido · NombreRepetido · CategoriaEliminada | `dominio/Validacion` (S3), repositorios (S4), P-C-09 |
| P12 · P-C-05 | Prueba solo `Hash.pbkdf2()` y `Hash.coincide()`; `PinStore` no se prueba en `test/` | S3 |
| P13 · foto de categoría | Entra en la **S12** con la del plato; en la S6 el botón *Elegir* de 2b existe pero está desactivado. Solo se ve pequeña y redonda en la fila de categorías de la carta (5a) | S6, S12 |
| P14 · resaltado al volver al Panel | Nivel 3: no se hace | S7 |
| P15 · puerta de Pedir | `ConfirmacionDialog` admite un solo botón (el negativo es opcional); los dos mensajes de la puerta lo usan así | S5 (`ConfirmacionDialog`), S8 |
| P16 · técnica de la precarga | **[Claude]** `Precarga` es un `RoomDatabase.Callback` cuyo `onCreate` lanza una corrutina (`CoroutineScope(Dispatchers.IO)`) que obtiene la instancia de `AppDatabase` y usa los DAOs (`insertarTodas`, `insertarAlergenos`, `insertarEtiquetas`, `insertar`). Nada de `execSQL` | S2; P-C-08 |
| P17 · dependencias base | Cuentan como «del spec»: `core-ktx`, `appcompat`, `material`, `constraintlayout`, `recyclerview`, `activity-ktx`, `fragment-ktx`, `lifecycle-viewmodel-ktx`, `lifecycle-livedata-ktx`, `kotlinx-coroutines-android` | S1 (`libs.versions.toml`) |
| P17 · constructores | Los tres repositorios reciben sus DAOs (y `PinStore`) **por constructor**; los crea una clase `Application` **[Claude]**, llamada **`EntradaAko`** (P117) que también guarda la única instancia de `AppDatabase` | S2 (`Ako`), S4; P-C-09 con DAO falso |
| D11 · `MesaConTotal` | **[Claude]** La proyección del DAO para la rejilla se llama `MesaConTotal` y vive en `dominio/modelos/` junto a `MesaEstado` | S3 |

## 3. PC, herramientas y repositorio

| Decisión | Qué se decidió |
|---|---|
| P18 · BIOS | SVM activado (24 sep). Emulador con controlador AEHD 2.2 |
| P19 · Android Studio | **[Claude]** Se queda la 2026.1.3 instalada; no se actualiza hasta la entrega salvo que Android Studio lo exija |
| P20 · emulador | `Pixel_6_API_34` (Google APIs, x86_64), ya creado. SDK en **`C:\Android\Sdk`** (no en `AppData`: la app de Claude redirige lo que escribe ahí a su propia carpeta) |
| P21 · Git | Identidad solo para `C:\AKO` por `includeIf`: **Yunkang Daniel <correo anónimo de GitHub, `…@users.noreply.github.com`>** |
| P22 · repositorio | GitHub **`ako`**, público, cuenta `dhuaniel-dot`; el proyecto de Android Studio en **`C:\AKO\app-ako\`**; raíz del repositorio `C:\AKO` |
| P23 · atribución | `attribution.commit = ""` y `attribution.pr = ""` en `C:\Users\dhuan\.claude\settings.json` (hecho; requiere reiniciar la app). Comprobar en el primer commit de la S1. El hook `comprobar-commit.sh` bloquea además cualquier `Co-Authored-By` |
| P27 · Defender | Exclusiones: `C:\AKO`, `C:\Android\Sdk`, `C:\Users\dhuan\.gradle` (hecho) |
| P26 · nube | Crédito de 250 $ reclamado (caduca el **5 de noviembre**). Solo para revisiones independientes y documentación desde la S4; nunca para construir |

## 4. Forma de trabajo (detalle en `.claude/skills/como-trabajamos/SKILL.md`)

| Decisión | Qué se decidió |
|---|---|
| P24 / P36 | **Daniel maneja Android Studio y teclea o pega el código**; Claude explica, da el código (bloque arriba, explicación debajo) y comprueba leyendo el archivo y compilando. Si el tiempo aprieta, Daniel puede pasar a que Claude escriba los archivos |
| P30 / P37 | Nivel de detalle «corta el pan, pon la carne, cierra» (regla 12 de `CLAUDE.md`) |
| P28 / P29 | Skills `abrir-sesion`, `cerrar-sesion`, `relevo`, `verificar`, `como-trabajamos`; hook `comprobar-commit.sh`; subagente `revisor` |
| P31 | Sin servidor de lenguaje Kotlin de entrada |
| P32 | Opus 5.5 en las sesiones (alto en S4, S6, S8, S9), Sonnet 5 en lo mecánico, Fable 5.1 en hitos o cuando Daniel lo decida |
| P33 | Un chat por sesión; se cambia cuando se hace largo, **terminando antes la pieza en curso**; umbrales 60 / 75 / 85 % de contexto; en cada cambio Claude entrega el primer mensaje del chat siguiente |
| P35 | Revisión independiente (`revisor` o sesión en la nube) al cerrar S4, S9 y S13 |
| P38 | Sin plugin de estilo *explanatory*; la regla 12 lo cubre. *Learning* se valora en la apropiación |

## 4b. Textos de la interfaz (inventario en `docs/textos-ui.md`, sesión 00)

El inventario recoge 141 cadenas de nivel 1 y 20 discrepancias entre fuentes (apartado (a) del inventario). Resueltas en la sesión 00 así; las dos que dependen de Daniel están en el registro de preguntas:

| Discrepancia | Decisión | Motivo |
|---|---|---|
| D1 aviso de 1b | «Si lo olvidas, no se puede recuperar: habrá que reinstalar la app.» (versión del wireframe) **[Claude]** | Contiene la corta de la ficha y añade la consecuencia que el Anexo II también avisa |
| D5 título de 2g | «Resumen de ingresos» **[Claude]** | Vocabulario obligatorio de `CLAUDE.md` |
| D6 pie de 2g | Dos etiquetas como el wireframe («Comandas cobradas» / «Total del día») **[Claude]** | Manda el wireframe en disposición; P-M-12 describe, no dicta |
| D7 pastilla del carrito | «Carrito · N · total» como el wireframe **[Claude]** | Idem |
| D8 título de 1c | «Introduce el PIN» **[Claude]** | Es el literal del wireframe |
| D13 botón de línea | «Quitar» **[Claude]** | Idem |
| D14 punto final del aviso 3e-2 | Con punto **[Claude]** | Ficha y wireframe coinciden |
| D15 aviso R6 con una o varias mesas | `<plurals>`: «…está en una comanda pendiente de la mesa %1$s.» / «…está en comandas pendientes de las mesas %1$s.» **[Claude]** | Hace falta el singular para P-M-10 |
| D16 aviso RF-38 | Título «¿Salir sin enviar?», cuerpo `<plurals>` «Hay %1$d plato en el carrito sin enviar. Se perderá.» / «Hay %1$d platos… Se perderán.», botones «Cancelar» / «Salir» **[Claude]** | Ninguna fuente da el literal; sigue el patrón de las otras cajas |
| D18 diálogo 1e | Los tres campos a la vez como el wireframe; al aceptar se comprueba primero el actual y, si falla, «PIN incorrecto» sin tocar el nuevo **[Claude]** | Cumple el wireframe (disposición) y la ficha (comportamiento) |
| D19 «ALÉRGENOS» / «Alérgenos» | Dos cadenas: una para la sección de 3a (mayúsculas por `textAllCaps`) y otra para el desplegable de 5b **[Claude]** | Son dos sitios |
| D20 «Otros» en el aviso 3e-1 | «¿Muevo el plato a %2$s?» con el nombre real de la categoría por defecto **[Claude]** | R16: la categoría por defecto se puede renombrar y nunca se reconoce por el nombre |
| D2, D3, D10, D17 | Ya decididas (P9, P10, snackbar «La mesa %1$d no tiene comanda») | — |
| D9/D11/D12 botones de los diálogos (**P40 → A**) | Los del wireframe: «Cancelar / Cobrar», «Cancelar / Anular», «No / Sí, mover», «Cancelar / Eliminar», «Cancelar / Quitar y anular». En las pruebas, «Sí/No» se lee como «botón afirmativo/negativo» | El botón dice lo que hace; manda el wireframe en lo visible |
| D4 interruptor de 2b (**P41 → A**) | «En la carta», igual que en el plato; apagarlo = eliminar | Mismo interruptor, mismo nombre; vocabulario obligatorio |

## 4c. Stack y repositorio (verificado compilando, sesión 00)

| Decisión | Qué se decidió | Motivo |
|---|---|---|
| Versiones **[Claude]** | Las de `docs/guias/stack-verificado/`: AGP 9.4.1, Gradle 9.6.0, Kotlin 2.2.10 (integrado), KSP 2.3.12, Room 2.8.5, Glide 5.0.9, `compileSdk`/`targetSdk` 37, `minSdk` 26 | Compiladas y probadas el 24 sep con este SDK; Glide 5.0.9 exige `compileSdk 37`; 37 es «el último estable» que pide el spec. Alternativa comprobada: `compileSdk 36` + Glide 5.0.7 + core-ktx 1.18.0 |
| Java de Gradle **[Claude]** | `C:\Users\dhuan\.gradle\gradle.properties` con `org.gradle.java.home` apuntando al JBR de Android Studio (hecho) | Evita que Gradle coja el JDK 25 del PATH; no hay que tocar `JAVA_HOME` en cada comando |
| Dos commits al cerrar **[Claude]** | Commit de código `S<N>: <objetivo>` (su hash va en *Commit final* de la ficha) y después `S<N>: ficha del diario` | La plantilla del diario (apartado 1) dice «el último commit» y el spec (13) «un commit»; con uno solo la ficha con el hash quedaría sin subir. **Anotar para el Project** al cerrar la fase 6 |
| Commits intermedios | Tras cada pieza completa que compila y pasa sus pruebas, `S<N>: <pieza>` sin push (spec 13); el hook exige el formato `S<N>:` en todo commit | Una sesión de tres horas no puede acabar sin nada guardado |
| Git dentro de `app-ako/` | **Nunca**: casilla «Create Git repository» desmarcada en el asistente; el único repositorio es `C:\AKO` | Un `.git` anidado dejaría la carpeta del código vacía en GitHub |
| `attribution` | Ya hecho en la sesión 00 (spec 11 lo situaba en la S1); se comprueba con el primer commit | — |

## 5. Decisiones de sesiones posteriores

*(Cada sesión añade aquí las suyas: fecha · decisión · consecuencia · [Claude] si procede.)*

### 5.1 Sesión 00, noche del 24 al 25 sep — guías S6–S13, juego de datos y revisión [Claude]

Daniel dormía: lo que hacía falta para seguir lo decidió Claude y va aquí, **cambiable**; lo que es de Daniel está en 5.2 como *pendiente de Daniel*. Las guías ya están escritas con la opción recomendada de cada duda; si Daniel elige otra, se ajusta la guía en el Plan Mode de esa sesión.

| Fecha · decisión [Claude] | Consecuencia |
|---|---|
| 25 sep · **Calendario de las 29 P-M** (aplica P6): primera ejecución, estado (*Pasa* / *Parcial* y qué falta) y sesión en que se repite entera, para cada una | `docs/guias/juego-de-datos.md` (apartado 5) y apartado 2 de cada guía S5–S13 |
| 25 sep · **Regla de `estado-nivel.md`**: un RF pasa a *implementado, no probado* si su P-M queda *Parcial*, y a *implementado* cuando pasa entera | `cerrar-sesion`, apartado 2 de cada guía |
| 25 sep · **P8 aplicado**: Bebidas se elimina justo después de P-M-29 y P-M-05. En P-M-04 el Panel la enseña la primera, *Categoría eliminada*, con Agua atenuado; en P-M-14 no hace falta eliminar Agua (ya no es visible); en P-M-17 no sale. Se anota en *Entrada*/*Observaciones* **al ejecutarlas** | `juego-de-datos.md` 2.7 |
| 25 sep · `pendiente_sesion_posterior` (caja provisional de la S5) se usa también en el Panel (Resumen de ingresos hasta la S10; `[+ Plato]` y tocar un plato hasta la S7) y **se borra en la S10** | `sesion-05.md`, `sesion-06.md`, `sesion-10.md` |
| 25 sep · `Precarga(context)` con `suspend fun cargar(db: AppDatabase)` desde la S2; `onCreate` solo lanza la corrutina que la llama. Compatible con P16; lo necesitan las pruebas de Room (S11) | `sesion-02.md` pieza 9, `sesion-11.md` pieza 3 |
| 25 sep · `MesaEstado` y `ComandaConTotal` (`comandaId`, `mesaNumero`, `fechaCierre`, `totalCentimos`) nacen en la S3; `PinStore` usa `Hash.generarSal()` de la S3 | `sesion-03.md`, `sesion-04.md`, `sesion-10.md` |
| 25 sep · Métodos de repositorio que añaden las guías, cada uno en su sesión: `recuperarPlato`, `recuperarCategoria` (S4), `categoriasConPlatos` y `platosAfectadosPorCategoria` (S6), `hayPlatoExistente` (S8) | Anotar para el diagrama de clases de la fase 7 |
| 25 sep · **Comprobado contra developer.android.com** lo que citan `sesion-01.md` y `android-studio-basico.md`; corregidos menús (Auto Import, *Clean and Assemble Project with Tests*, captura del emulador, Database Inspector, Device Manager, *Clear storage*) y quitadas las marcas «comprobar en pantalla» confirmadas | Las dos guías llevan al final la lista de páginas consultadas |
| 25 sep · **Corrige la fila «Git dentro de `app-ako/`» del apartado 4c**: la documentación de Android Studio no lista una casilla «Create Git repository» en el asistente (es de IntelliJ); el riesgo real es el menú *VCS → Enable Version Control Integration*, que crearía el repositorio dentro de `app-ako/`. La regla no cambia: nunca Git dentro de `app-ako/` | `sesion-01.md` («Tres cosas que no hay que hacer») y `android-studio-basico.md` apartado 8 ya lo dicen así |
| 25 sep · **Compatibilidad Android Studio / AGP**: la 2026.1.3 instalada admite AGP hasta 9.3 (developer.android.com/build/releases/about-agp); `stack-verificado` usa 9.4.1. Compilado esta noche el mismo stack con **AGP 9.3.3**: `assembleDebug` y `testDebugUnitTest` en verde | Pregunta P42; `sesion-01.md` apartado 4 lo avisa y no se sustituye nada hasta que Daniel conteste |
| 26 sep · **Fable de guardia (*advisor*)** (Daniel): apagado por defecto, en todos los proyectos; Claude propone activarlo cuando algo se complica y Daniel escribe `/advisor fable` (y `/advisor off` al resolverse). No se sube el esfuerzo alto por defecto ni se crea un subagente que edite código (Daniel teclea, P24/P36) | Regla en `~/.claude/CLAUDE.md`; `como-trabajamos`; `cerrar-sesion` paso 8b |
| 25 sep · Las guías S6–S13 usan nombres nuevos marcados [Claude] (p. ej. `Formato`, `LineaVista`, `ModoRejilla`, `CategoriaFilaAdapter`, `ComandaCobradaAdapter`, claves de `strings.xml` nuevas como `enviar_hecho`, `carrito_tope_99`, `foto_error`) | Cada uno depende de su pregunta de 5.2; al cerrar cada sesión se anota aquí el nombre definitivo |

### 5.2 Preguntas de la noche del 24 al 25 sep — contestadas por Daniel el 25 sep

Numeradas desde la **P42** siguiendo la serie de este archivo (ojo: las P40 y P41 de aquí no son las P40 y P41 del registro del Project; ver P102). Formato: contexto · opciones · **recomendada**. **Las guías ya siguen la recomendada**; basta contestar las que Daniel quiera cambiar, y las de cada sesión se confirman en su Plan Mode o al abrirla.

**25 sep (antes de dormir), Daniel contestó 31 y fijó una regla:** *«en casos de dudas sobre cosas técnicas de código elegiré tu recomendación»*. Las técnicas que no contestó se marcan así. **Más tarde el mismo 25 sep contestó el resto: ya no queda ninguna pendiente** (P102 sin contestar se queda con la recomendada).

**Antes de la S1**

- **P42 · AGP y Android Studio** · La 2026.1.3 instalada solo admite AGP ≤ 9.3; el stack verificado usa 9.4.1 y la sincronización fallaría · A) actualizar Android Studio a Quail 4 (2026.1.4) (P19 lo permite «si Android Studio lo exige») · B) `agp = "9.3.3"` en `libs.versions.toml` (compilado esta noche en verde) · **Recomendada: B** (mantiene P19 y está probado con este PC) → **Daniel: B** (25 sep)
- **P43 · Retocar `stack-verificado` ya** · Falta `testInstrumentationRunner`, `room-testing` está en `testImplementation` (el spec lo quiere en `androidTest`) y `AkoGlideModule.kt` dice `package yunkang.ako` aunque va en `imagenes/` · A) corregirlo en la S11 en `app-ako/` · B) corregir `stack-verificado/` antes de la S1 (junto con P42) · **Recomendada: B** (tres líneas que no cambian lo que compila hoy) → **Daniel: B** (25 sep)

**S2–S5 (guías ya escritas)**

- **P44 · `orden` de Otros en la precarga** (S2) · A) 0 (no se usa: R16 va por `esPorDefecto`) · B) un número muy alto · C) 1, como Bebidas · **Recomendada: A** (la primera categoría nueva recibe 2, sin repetidos) → **Daniel: A** (25 sep)
- **P45 · Formato de las guías S2 y S3** · están en párrafos, no en viñetas; algunas piezas de menos de 20 min · A) dejarlas · B) pasarlas al formato de la S4 sin tocar contenido · **Recomendada: A** → **Daniel: B** (25 sep)
- **P46 · Orden de comprobaciones en 1e** (S5 pieza 11, D18) · hoy la pantalla compara los dos nuevos antes de mirar el actual · A) dejarlo · B) al Aceptar, primero `comprobarPin(actual)` (si falla, «PIN incorrecto» y sacudida), después comparar los nuevos, después `cambiarPin` · C) `cambiarPin` con tres valores y resultado de tres casos · **Recomendada: B** (D18 al pie de la letra, un método de una línea) → **Recomendada, por la regla de Daniel** (25 sep: «en dudas técnicas de código, tu recomendación») [Claude]

**S6 — Panel (se confirman en su Plan Mode)**

- **P47 · Quién construye `CategoriaConPlatos`** · A) método nuevo del repositorio `categoriasConPlatos()` · B) el ViewModel · C) `@Relation` de Room · **Recomendada: A** → **Daniel: A** (25 sep)
- **P48 · Dónde se aplica «Otros la última»** · A) en `categorias()` del repositorio · B) en el SQL · C) en el adaptador (spec 6) · **Recomendada: A** (la carta lo hereda) → **Daniel: A** (25 sep)
- **P49 · Cómo se refrescan las listas al volver** · A) `cargar()` en `onResume` y recarga tras cada escritura · B) DAOs con `LiveData`/`Flow` · **Recomendada: A** (no toca los DAOs de la S3; S8–S10 lo dan por hecho) → **Recomendada, por la regla de Daniel** (25 sep: «en dudas técnicas de código, tu recomendación») [Claude]
- **P50 · `DiffUtil` o `notifyDataSetChanged`** · A) `notifyDataSetChanged` en las dos listas · B) `ListAdapter` en las dos · C) `ListAdapter` fuera, `notifyDataSetChanged` dentro · **Recomendada: C** (con B, Flan no se atenúa al eliminar Postres) → **Daniel: C** (25 sep)
- **P51 · Los 270 dp** · A) la lista interior mide 270 dp (spec 7) · B) la caja mide ~270 dp con cuatro platos a la vista (ficha 2, wireframe, P99) · **Recomendada: B** → **Daniel: B** (25 sep)
- **P52 · Un adaptador de categorías o dos** · A) dos: `CategoriaAdapter` (cajas, 2a) y `CategoriaFilaAdapter` (fila de la carta, 5a) · B) uno con un modo · **Recomendada: A** (más claro; la fila de plato sí se comparte) → **Daniel: A** (25 sep)
- **P53 · `MesasAfectadasDialog`** · A) clase pequeña que monta el texto y abre la misma caja de `ConfirmacionDialog` · B) sin clase, `ConfirmacionDialog` directo (ficha 3) · C) diálogo propio · **Recomendada: A** → **Daniel: A** (25 sep)
- **P54 · Texto del aviso 2e** · P-M-06 pide «Flan (mesa 5)» y `mesasConCategoriaPendiente` solo da mesas · A) método nuevo `platosAfectadosPorCategoria` (sin SQL nuevo) + `<plurals>` y claves nuevas · B) consulta nueva con JOIN · C) texto solo con mesas · **Recomendada: A** → **Daniel: A** (25 sep)
- **P55 · ¿2e cuenta los platos ya eliminados?** · A) solo los activos (lo que de verdad se elimina) · B) todos · **Recomendada: A** → **Daniel: A** (25 sep)
- **P56 · Plegar la caja tocando el nombre** (ficha 2 y leyenda 02a, no en spec 4.1) · A) se hace, con icono visible · B) no se hace y se declara · C) se hace al final y se corta si falta tiempo · **Recomendada: C** → **Daniel: A** (25 sep)
- **P57 · *Cancelar* en el aviso 2e** · A) vuelve a la hoja sin guardar, con el interruptor como lo dejó · B) cierra la hoja · **Recomendada: A** → **Daniel: A** (25 sep)
- **P58 · `Formato.kt` en `ui/comun/`** (archivo no previsto en el spec) · `precio` devuelve «18,50» (el «€» lo pone `comun_precio`; negativos con «−» U+2212), `centimosDesde` (acepta coma y punto), `lista`, `fecha`, `hora` · A) aceptarlo · B) otra ubicación o nombres · **Recomendada: A** → **Daniel: A** (25 sep)
- **P59 · Empezar las pruebas de cada sesión con instalación limpia** (*Clear storage*) · A) siempre · B) conservar los datos de la sesión anterior · **Recomendada: A** (P-M-05, 07 y 11 no se pueden repetir sobre datos viejos) → **Daniel: A** (25 sep)

**S7 — Plato**

- **P60 · Interruptor *En la carta* al crear un plato** · A) visible y encendido también al crear (wireframe 03a) · B) solo al editar, como en 2b · **Recomendada: A** → **Daniel: A** (25 sep)
- **P61 · Categorías del desplegable** · A) todas, sin marcar las eliminadas · B) todas, marcando las eliminadas (cadena nueva) · C) solo activas y la ya elegida · **Recomendada: A** (la cadena 3e avisa al guardar) → **Daniel: A** (25 sep)
- **P62 · Cuándo salta la cadena 3e** · A) al crear un plato o moverlo a una categoría eliminada · B) siempre que se guarde en una eliminada · **Recomendada: A** → **Daniel: A** (25 sep)
- **P63 · «sus 1 platos»** · A) `recuperar_categoria_cuerpo` pasa a `<plurals>` (como D15) · B) dejarlo · **Recomendada: A** → **Daniel: A** (25 sep)
- **P64 · Qué devuelve `ConfirmacionDialog`** · A) qué botón se pulsó; cerrar sin contestar no hace nada · B) dos claves por caja · **Recomendada: A** → **Recomendada, por la regla de Daniel** (25 sep: «en dudas técnicas de código, tu recomendación») [Claude]
- **P65 · Nombres nuevos de la S7** (`PlatoViewModel.hayCambios`, `platosQueVuelven()`, `leerFormulario()`, `EXTRA_PRODUCTO_ID`…) · A) aceptarlos · B) renombrar en la sesión · **Recomendada: A** → **Daniel: A** (25 sep)
- **P66 · Casillas de alérgenos si Android recrea la pantalla** · A) los marcados viven en el ViewModel · B) id estable en cada casilla · **Recomendada: A** → **Daniel: A** (25 sep)

**S8 — Pedir (se confirman en su Plan Mode)**

- **P67 · Componentes compartidos (`RejillaMesasFragment` en 1d/6a, `ReciboFragment` en 6c/2g)** · A) el Fragment pinta lo que le da la Activity y avisa del toque con `setFragmentResult` · B) el Fragment mira su modo y pide el ViewModel que toca · C) una interfaz común en los dos ViewModels · **Recomendada: A** → **Daniel: A** (25 sep)
- **P68 · `LineaAdapter` para dos tipos de línea** (carrito y comanda) · A) modelo común `LineaVista` y un modo (carrito · solo Quitar · solo lectura) · B) dos adaptadores · **Recomendada: A** → **Daniel: A** (25 sep)
- **P69 · Secciones de la carta** · A) `ConcatAdapter`: una cabecera y un `FilaPlatoAdapter` por sección · B) un adaptador con dos tipos de fila · **Recomendada: A** (la fila de plato entra sin tocarla) → **Daniel: A** (25 sep)
- **P70 · Puerta de Pedir con dos mensajes** · A) `hayPlatoExistente()` nuevo en el repositorio · B) un `enum` con los tres casos · C) siempre «Todas las categorías están eliminadas» · **Recomendada: A** → **Daniel: A** (25 sep)
- **P71 · Comprobar el PIN al salir de Pedir** (`PinDialog` nació para el Selector) · A) interfaz `ComprobadorPin` que implementan las dos Activities · B) `PinDialog` con su propio ViewModel · C) el diálogo devuelve el PIN y la Activity comprueba · **Recomendada: A** → **Daniel: A** (25 sep)
- **P72 · Tras Enviar** · A) carrito vacío, vuelta a 5a y snackbar «Pedido enviado a la mesa N» (cadena nueva) · B) igual sin mensaje · C) salir de Pedir · **Recomendada: A** → **Daniel: A** (25 sep)
- **P73 · La pastilla con el carrito vacío** · A) visible, «Carrito · 0 · 0,00 €» · B) oculta · C) visible y desactivada · **Recomendada: A** → **Daniel: A** (25 sep)
- **P74 · Aviso al pasar de 99** (ficha 5 lo pide, sin texto) · A) snackbar «Máximo 99 unidades por plato»; `Carrito.anadir` devuelve `Boolean` y P-C-03 comprueba ese valor · B) mismo texto sin tocar `Carrito` · C) sin aviso · **Recomendada: A** → **Daniel: A** (25 sep)
- **P75 · Resaltar la categoría también al deslizar** · A) sí (primera pieza que se recorta si falta tiempo) · B) solo al tocar · **Recomendada: A** → **Daniel: A** (25 sep)
- **P76 · Pasar la mesa a Pedir** · A) id y número por `Intent` y `iniciar(mesaId, mesaNumero)` · B) solo el id y una consulta · **Recomendada: A** → **Daniel: A** (25 sep)

**S9 — Cuenta (se confirman en su Plan Mode)**

- **P77 · Quién abre el recibo** · A) `CuentaActivity` (6b solo avisa del toque, como la rejilla) · B) `ComandaFragment` · **Recomendada: A** → **Recomendada, por la regla de Daniel** (25 sep: «en dudas técnicas de código, tu recomendación») [Claude]
- **P78 · Saber que es la última línea** (aviso R7 antes de quitar) · A) el ViewModel mira si la lista tiene una fila · B) método nuevo del repositorio · **Recomendada: A** → **Recomendada, por la regla de Daniel** (25 sep: «en dudas técnicas de código, tu recomendación») [Claude]
- **P79 · *Cambio* con el campo vacío o mal escrito** · A) en blanco y Cobrar activo · B) una raya «—» (cadena nueva) · **Recomendada: A** → **Recomendada, por la regla de Daniel** (25 sep: «en dudas técnicas de código, tu recomendación») [Claude]
- **P80 · Cómo se llaman `quitarLinea`, `anular` y `cobrar`** · A) `suspend` desde `lifecycleScope`, como `comprobarPin` en la S5 · B) `viewModelScope` y un evento `LiveData` · **Recomendada: A** → **Recomendada, por la regla de Daniel** (25 sep: «en dudas técnicas de código, tu recomendación») [Claude]
- **P81 · 6b sin argumentos** · A) lee la comanda del `CuentaViewModel` compartido · B) `comandaId` en `arguments` · **Recomendada: A** → **Recomendada, por la regla de Daniel** (25 sep: «en dudas técnicas de código, tu recomendación») [Claude]
- **P82 · Hallazgos del `revisor` de la S4** que tocan `quitarLinea`/`anular`/`cobrar` · A) cerrarlos al empezar la S9 · B) dejarlos para la S13 · **Recomendada: A** → **Recomendada, por la regla de Daniel** (25 sep: «en dudas técnicas de código, tu recomendación») [Claude]
- **P83 · Dónde va P-M-29 en la S9** (necesita la app recién instalada) · A) instalación limpia al empezar las pruebas, P-M-29 en su sitio y después el montaje completo (`juego-de-datos.md`) · B) al final, tras *Clear storage* (`sesion-09.md`) · **Recomendada: A** (con los datos de la S8 la mesa 4 sigue abierta y P-M-20 no encuentra «mesa 4 sin comanda») → **Daniel: A** (25 sep)

**S10 — Resumen de ingresos**

- **P84 · Idioma del nombre del día** («Jueves») · A) el del dispositivo · B) español fijo · **Recomendada: A** → **Daniel: A** (25 sep)
- **P85 · Calendario** · A) `DatePickerDialog` desde la Activity · B) un `DialogFragment` propio (guía oficial) · **Recomendada: A** → **Recomendada, por la regla de Daniel** (25 sep: «en dudas técnicas de código, tu recomendación») [Claude]
- **P86 · Lista de 2g** · A) `ComandaCobradaAdapter` nuevo en `ui/resumen/` · B) otro modo de `LineaAdapter` · **Recomendada: A** (una comanda no es una línea) → **Recomendada, por la regla de Daniel** (25 sep: «en dudas técnicas de código, tu recomendación») [Claude]
- **P87 · Dónde se abre el recibo en 2g** · A) contenedor encima de la lista, misma Activity, con Atrás · B) la lista como Fragment nuevo · **Recomendada: A** → **Recomendada, por la regla de Daniel** (25 sep: «en dudas técnicas de código, tu recomendación») [Claude]

- **P103 · Idioma del emulador desde la S10** · el emulador está en inglés y hasta la S13 la app solo tiene español: 2g diría «Thursday, 17/09/2026» · A) poner el emulador en *Español (España)* desde la S10 (la S13 lo hace igualmente) · B) fecha con `Locale("es","ES")` fijo (rompe el inglés de la S13) · C) dejarlo y anotarlo · **Recomendada: A** → **Daniel: A** (25 sep)

**Juego de datos y pruebas**

- **P88 · P-M-08 en la pasada final** · A) la última y después un vistazo de 2 min al Panel y a 5b para anotar P-M-04 y P-M-18 · B) antes de P-M-04 · **Recomendada: A** → **Daniel: A** (25 sep)
- **P89 · La mesa 6 nunca se cierra** (P-M-15 pide «resto sin comanda») · A) anularla antes de P-M-15 · B) cobrarla y cambiar P-M-12 a 3 comandas · **Recomendada: A** (no cambia ningún resultado esperado) → **Daniel: A** (25 sep)
- **P90 · Descripción de Entrecot** (P-M-18 la pide y nadie la crea) · A) paso a mano «Lomo de vaca a la plancha con patatas.» · B) añadirla al paso 3 de P-M-07 · **Recomendada: A** → **Daniel: A** (25 sep)
- **P91 · P-M-07 paso 4 sin precio** (Guardar no se enciende y no salta «número repetido») · A) teclear ya 11,00 en el paso 4 · B) aceptarlo · **Recomendada: A** → **Daniel: A** (25 sep)
- **P92 · Cómo se anota una prueba repetida** · A) *Resultado* y *Fecha* de la última; historial en una línea de *Observaciones* · B) solo la última · **Recomendada: A** → **Daniel: A** (25 sep)
- **P93 · Foto de categoría sin prueba manual** · A) comprobación a mano apuntada en la ficha de la S12 · B) P-M-30 nueva · **Recomendada: A** (una P-M por RF) → **Daniel: A** (25 sep)

**S12 — Fotos**

- **P94 · Cuándo se copia la foto** · A) al elegirla (salir sin guardar la borra) · B) al guardar · **Recomendada: A** (el selector solo presta la foto un rato) → **Recomendada, por la regla de Daniel** (25 sep: «en dudas técnicas de código, tu recomendación») [Claude]
- **P95 · Fotos giradas (EXIF)** · A) no tratarlo y declararlo · B) enderezar con `android.media.ExifInterface`, que ya trae Android (~15 min) · **Recomendada: B** (la foto de P-M-08 saldría tumbada) → **Daniel: B** (25 sep)
- **P96 · Foto que no se puede usar** · A) snackbar «No se ha podido usar esa foto» (`foto_error`, nueva) · B) nada · **Recomendada: A** → **Daniel: A** (25 sep)
- **P97 · Hueco de la foto en 2b** · A) redondo, como en 5a (spec 9, P13) · B) cuadrado (wireframe 02b) · **Recomendada: A** → **Daniel: A** (25 sep)

**S13 — Cierre**

- **P98 · Accessibility Scanner sin Play Store** (el emulador es *Google APIs*) · A) segundo emulador con imagen *Google Play* solo para el Scanner (1,5–2 GB; iniciar sesión en Play Store) · B) sin Scanner: comprobación de accesibilidad de Android Studio + lint + revisión a mano, y la memoria lo dice · **Recomendada: A, y B si A falla** → **Daniel: B** (25 sep): «creo que no hace falta; al final simplemente tenemos que hacer un prototipo, una APK y ya». Sin Scanner: comprobaciones de Android Studio + lint + revisión a mano; la memoria lo declara (el spec 10 y la S13 pedían el Scanner)
- **P99 · Idioma por defecto** · A) `values/` español y `values-en/` inglés (un móvil en francés vería español; se corrige RNF-19 en el Project) · B) `values/` inglés y `values-es/` español · **Recomendada: A** (manda el spec) → **Daniel: A** (25 sep)
- **P100 · La precarga en inglés** · A) sus cadenas `translatable="false"` (la carta es un dato en español) · B) traducirlas · **Recomendada: A** → **Daniel: A** (25 sep)
- **P101 · Tema claro fijo** · A) `Theme.Material3.Light` · B) DayNight y pasar el Scanner también en oscuro · **Recomendada: A** → **Daniel (25 sep): ni A ni B — «me gustaría un botón para elegir modo día o modo noche»**. Funcionalidad nueva, fuera del diseño: tema claro y oscuro con un botón para cambiarlo. Se programa en la S13 con el tema; dónde va el botón, cómo se recuerda la elección y el contraste en oscuro se deciden al abrir la S13 [Claude]. Anotar para el Project (requisitos y memoria)

**Registro**

- **P102 · Choque de números con el Project** · las P40 y P41 de este archivo no son las del registro del Project (la ficha 1 y las clases citan «P41» para salir de Pedir con PIN) · A) desde ahora, citar las de este archivo como «CC-P41» en las guías · B) dejarlo con esta nota · **Recomendada: B** (el encabezado ya dice que se renumeran al pasar al Project) → Sin contestar: queda la **recomendada (B)** (25 sep)

### 5.3 Sesión 1 — 28 sep 2026

| Fecha · decisión | Consecuencia |
|---|---|
| 28 sep · **P104 · Package name → C: `yunkang.ako`** (Daniel, en el asistente de proyecto nuevo). Sustituye a `es.daniel.ako` del spec (P138 del Project) | Cambiado en `CLAUDE.md`, spec, `spec+doc-clases.md`, guías S1–S13, `stack-verificado/` (`namespace`, `applicationId`, `AkoGlideModule.kt`), `verificar`, plantilla del README. `ANALISIS-INICIAL.md` se deja como estaba (histórico). **Anotar para el Project** (P138) |
| 28 sep · **`minSdk 26` confirmado** (Daniel, al crear el proyecto): se queda como dice el spec. Motivo: PBKDF2 con SHA-256 (`PBKDF2WithHmacSHA256`) viene en Android desde la API 26, sin librerías extra (el spec las prohíbe); y llega al ~98,4 % de los móviles (dato del asistente de Android Studio, 28 sep 2026) | Ninguna: es lo que ya tenía el spec. Sirve para defenderlo en el vídeo |
| 28 sep · **P105 · Nombre visible de la app → B: `AKO`** en mayúsculas (Daniel). Es el `app_name` (debajo del icono y en el Selector 1a); el nombre del proyecto en los documentos sigue siendo Ako | `strings-es-borrador.xml` y `textos-ui.md` con `AKO`; el asistente ya generó `app_name` = `AKO`. **Anotar para el Project** (wireframe 01a dice «Ako») |
| 28 sep · **P106 → A**: los archivos de Gradle de `stack-verificado/` los copia Claude por consola (configuración, no código de negocio); Daniel los lee con la explicación y sincroniza | Ficha S1, *Uso de IA* |
| 28 sep · **P107 sustituida** (Daniel): en cada cambio de chat, dos resúmenes en PDF (sencillo y normal) por Gmail, preguntando antes qué le costó y qué curiosidades tiene | `como-trabajamos`, `relevo` 4b, `cerrar-sesion` 8c |
| 29 sep · **Respuesta del profesor sobre el uso de la IA** (literal): «Buenos días Yunkang, sí en ese caso puedes usarlo sin problema. La IA es una herramienta más no es malo usarla. Lo que sí sería malo es que diseñara y ejecutara el proyecto por si solo, no es problema que genere código sino cómo lo hace y lo más importante que dependas de ella. En cuanto a que escriba las partes repetitivas sin problema como dices para generar datos para la base. Para la declaración de código utiliza getter y setters del IDE no hace falta IA. Espero haberte resuelto. Un saludo» | Se sigue con la línea 2 y la forma de trabajar actual. **Permitido:** preparación, documentación, explicar, ejemplos, comprobar, revisar, y que Claude escriba las partes repetitivas (p. ej. datos de la base de datos). **Límite:** que la IA no diseñe ni ejecute el proyecto sola y que Daniel no dependa de ella. Getters/setters: sugerencia del profesor, no obligación (en Kotlin las propiedades ya los traen) |
| 29 sep · **P109 → B**: en el README el nombre es «Yunkang Daniel Huzhou» | README |
| 29 sep · **P110 → A**: el apartado «Uso de IA» del README dice que el uso se consultó con el profesor y lo aprobó | README |
| 29 sep · **P111 · Coautor en los commits** (Daniel, tras la respuesta del profesor): los commits de Claude Code llevan `Co-Authored-By: Claude`. **Sustituye a P226** | `attribution` en `.claude/settings.json` del repositorio (manda sobre el ajuste de usuario, que sigue vacío para los demás proyectos); hook sin el bloqueo; `CLAUDE.md` regla 9, spec apartado 13, `cerrar-sesion`, `como-trabajamos`. Los commits anteriores al 29 sep no lo llevan. **Anotar para el Project** (P226) |
| 29 sep · **P112 · Quién decide lo técnico** (Daniel): **sustituye la regla del 25 sep** («en dudas técnicas, tu recomendación»). Toda decisión que cambie cómo funciona el código se le plantea con **mínimo 3 opciones**, al menos una de Claude y al menos una sacada de una fuente de internet (con enlace), y **elige Daniel**. Lo mecánico lo hace Claude y lo cuenta | `como-trabajamos`. Las P46, P49, P64, P77–P81, P85–P87 y P94 («por la regla de Daniel») se vuelven a preguntar así en su sesión. Fuentes a la bibliografía de la memoria |
| 29 sep · **Hook: el autor tiene que ser Daniel** (Daniel: sí; lo pegó él porque los permisos de la app no dejan a Claude editar los hooks). Paso 5 de `comprobar-commit.sh`: bloquea el commit si `user.name` no es «Yunkang Daniel» o el correo no es el anónimo de GitHub. Probado: pasa con su identidad y bloquea con «Claude» | Evita que un chat sin la identidad configurada (p. ej. en la nube) firme como Claude, como pasó con `87d352e`, `74e521b` y `ebd1cd9` |
| 29 sep · **Somos un equipo** (Daniel): si algo se tuerce, Claude para, lo dice y se replanifica juntos (Fable como asesor si hace falta) | `como-trabajamos` y `~/.claude/CLAUDE.md` |

### 5.4 Sesión 2 — 30 sep 2026

| Fecha · decisión | Consecuencia |
|---|---|
| 30 sep · **P113 · Cómo se guardan los enums → B** (Daniel eligió B entre A/B/C). A) `Converters.kt` propio que guarda el nombre (Claude, la guía) · **B) sin `Converters`: el conversor de enums que Room trae desde la 2.3.0 guarda el nombre como texto** (fuente: notas de versión de Room, *Built-in Enum Support*, https://developer.android.com/jetpack/androidx/releases/room#2.3.0, consultado el 30 sep 2026) · C) conversor propio que guarda el número (`ordinal`) (Claude). Motivo de Daniel: «es algo que te viene gratis; como ir a la ciudad en bici o en coche costando lo mismo». En la base de datos se lee igual que con A (`PAGADA`) | **Cambia el spec**: sin `Converters` ni `@TypeConverters` en `AppDatabase`. Corregidos spec (apartados 3, 5 y 11), guías S2, S4, S6 y `juego-de-datos.md`. **Anotar para el Project** (spec y diagrama de clases) y la fuente a la bibliografía |
| 30 sep · ~~P114 · Tablas de la S2 → A: las 14~~ **sustituida por P115** | — |
| 30 sep · **P115 · Solo el nivel 1 en la base de datos → C** (Daniel eligió C entre A/B/C: A) las 14 tablas del spec · B) fuera solo lo de modificadores · C) nada del nivel 2). Regla de Daniel: *«si empiezo algo, lo termino»*; nada del nivel 2 a medias. Fuera: `TipoModificador`, `modificador`, `linea_modificador`, `producto_nutricion`, `idioma`, `producto_traduccion`, `producto_etiqueta`, `etiqueta`, las columnas `producto.disponible` y `producto.imagenNutricional`, y las 3 etiquetas de la precarga. Quedan **7 tablas**: `categoria`, `producto`, `alergeno`, `producto_alergeno`, `mesa`, `comanda`, `linea_comanda`. Consecuencia: **no queda ninguna CASCADE** en el nivel 1 (la única era `linea_modificador → linea_comanda`, R11); todo es RESTRICT | Cada incremento del nivel 2 que se haga añade **entero** lo suyo: tablas, columnas, precarga y función, subiendo `version` de `AppDatabase` (en desarrollo, desinstalar y reinstalar; https://developer.android.com/training/data-storage/room/migrating-db-versions). RF-50 pierde las 3 etiquetas. Cambia el spec: a «Para el Project» |
| 30 sep · **Plan de Daniel: prototipo, doc y después extras** — *«yo quiero hacer el prototipo y ya; luego, si me sobra tiempo después de terminar el doc, vemos qué podemos añadir y modifico el doc con lo añadido»*. Orden: 1) nivel 1 completo; 2) el doc (memoria); 3) solo entonces, si sobra tiempo, incrementos del nivel 2 enteros, y el doc se actualiza con ellos | **Sustituye a P3** (incremento 1 si el nivel 1 estaba cerrado el 31 oct): el nivel 2 ya no se mira hasta terminar el doc |
| 30 sep · **P116 · Una sola base de datos → B** (Daniel eligió B entre A/B/C): A) portero `obtener()` con `@Volatile` y `synchronized` dentro de `AppDatabase` (codelab oficial «Persist data with Room», https://developer.android.com/codelabs/basic-android-kotlin-compose-persisting-data-room) · **B) solo la clase `Application` con `val db by lazy { Room.databaseBuilder(…).build() }`** (Claude; `lazy` es seguro con varios hilos: https://kotlinlang.org/docs/delegated-properties.html#lazy-properties) · C) un `object` aparte rellenado al arrancar (Claude). La documentación de Room solo pide una instancia única (https://developer.android.com/training/data-storage/room) | `AppDatabase` sin `companion object`; `Precarga` obtiene la base de datos de `EntradaAko`. Guías S2 y S4 ajustadas |
| 30 sep · **P117 · Nombre de la clase `Application` → `EntradaAko`** (nombre propio de Daniel, frente a A `AccesoBaseDatos`, B `AkoApplication` —convención del codelab, `InventoryApplication`— y C `Central`). Motivo: `Ako` a secas se confundía con la app entera | `EntradaAko.kt` en la raíz del paquete y `android:name=".EntradaAko"` en el manifiesto. Guías S2–S13 renombradas (no se tocan `Theme.Ako`, `AkoGlideModule` ni el nombre del proyecto) |
| 30 sep · **P118 · Cuándo se abre la base de datos → A** (Daniel eligió A entre A/B/C): **A) `EntradaAko.onCreate` la abre por detrás al arrancar la app (`openHelper.writableDatabase` en una corrutina), y así salta la precarga en el primer arranque** (fuente: https://discuss.kotlinlang.org/t/room-oncreate-isnt-called-after-databasebuilder-build/15124; Room llama a `Callback.onCreate` «cuando la base de datos se crea por primera vez», y no la abre hasta el primer uso) · B) línea provisional en `MainActivity` hasta la S5 (Claude) · C) no forzar: la precarga salta con la primera pantalla que lea (Claude) | `EntradaAko` con `onCreate`. Los DAOs nacen en la S2 solo con los `@Insert` de la precarga (`CategoriaDao.insertar`, `ProductoDao.insertar`, `MesaDao.insertarTodas`, `PrecargadosDao.insertarAlergenos`); la S3 les añade el resto; `ComandaDao` nace en la S3 |
| 30 sep · Aviso para leer documentación: la página oficial de conversores (`training/data-storage/room/referencing-data`) ya enseña **Room 3** (`@ColumnTypeConverter`); el proyecto usa Room 2.8.5 (`@TypeConverter`) | Ninguna; evita confusiones al consultar |

## 6. Para el Project (lo que Daniel corrige en el doc)

Regla de Daniel (30 sep 2026): **lo que se decide en Claude Code tiene prioridad sobre el Project «Proyecto Intermodular» de la app de Claude** (donde se planificó todo y se escribe el doc): el doc explica el prototipo, así que manda lo que se construye aquí. Daniel le pide al chat del Project que lea esta lista y corrija el doc en consecuencia; si algo del spec no hace falta o cambia, se sigue lo decidido aquí y se apunta en esta lista para que Daniel lo corrija después en el Project «Línea 2 Ako». Se tacha cuando Daniel diga que ya está en el doc. (Los «Anotar para el Project» sueltos de los apartados anteriores se pasan aquí al cerrar la sesión.)

| Fecha | Qué cambió | Dónde tocarlo en el doc |
|---|---|---|
| 30 sep 2026 | **Plan: prototipo → doc → extras** (sustituye a P3): el nivel 2 solo si sobra tiempo después de terminar el doc; el diseño del nivel 2 se conserva | Planificación y alcance de la memoria (apartados 5–7), Gantt |
| 30 sep 2026 | **P115: la base de datos solo tiene lo del nivel 1** (7 tablas; sin ninguna CASCADE; sin `etiqueta`, `modificador`, `linea_modificador`, `producto_nutricion`, `idioma`, `producto_traduccion`, `producto_etiqueta`; sin `producto.disponible` ni `producto.imagenNutricional`); RF-50 sin las 3 etiquetas; cada incremento del nivel 2 añade entero lo suyo | Spec (modelo de datos, «las 14 tablas se crean desde el primer día», orden de construcción), `spec+doc-clases.md` (diagrama de clases y de la BD), `spec+doc-requisitos.md` (RF-50), `spec+doc-pantallas.md`, `spec+doc-pruebas.md` (P-M-29: se quita el paso «3 filas en `etiqueta`»; P-C-08), memoria (qué está diseñado y no implementado) |
| 30 sep 2026 | P116 y P117: la única base de datos la guarda la clase `Application` **`EntradaAko`** con `by lazy`; `AppDatabase` sin `obtener()` | Diagrama de clases (`Ako` → `EntradaAko`, sin `AppDatabase.obtener`), bibliografía (codelab y documentación de Kotlin `lazy`) |
| 30 sep 2026 | P113: sin `Converters`; los enums los guarda Room con su conversor incorporado (nombre como texto) | Spec (estructura, reglas de las entidades, orden de construcción S2), diagrama de clases, bibliografía (notas de versión de Room 2.3.0) |
