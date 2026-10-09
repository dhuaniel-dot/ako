> Nota (9 oct 2026): esto es un registro de la sesión 00 (24 sep 2026) y se conserva tal cual; algunas cosas pueden estar desactualizadas. Lo que vale hoy es CLAUDE.md y docs/decisiones-code.md.

# Ako — Análisis inicial de Claude Code

> Escrito la noche del **24 al 25 de septiembre de 2026** por Claude Code (modelo Fable 5.1), tras leer entera la carpeta `C:\AKO` (CLAUDE.md, LEEME.md, spec-claude-code.md, las siete fichas, los 21 wireframes con sus leyendas, requisitos, clases, plan de pruebas, estado del nivel, plantilla del diario) y comprobar qué hay instalado en el PC. **No he preguntado nada, no he escrito código, no he instalado nada y no he tocado ningún otro archivo.** Todo lo que propongo y no estaba pedido lleva **[Claude]**.
>
> **Cómo se lee:** A y B son el plan (lo primero). C es para que compruebes que lo he entendido. D son las contradicciones y huecos. E, F y G son propuestas (instalación, skills, chats y modelos). **H son las preguntas**, numeradas, al final: se contestan con la letra o con tus palabras. Dentro del texto, «→ P12» quiere decir «esto cambia según lo que contestes en la pregunta P12 de H».
>
> **Resumen en tres líneas:** el nivel 1 son **90–115 horas** de trabajo real para alguien que empieza de cero (13 sesiones, unas 6 semanas a 18 h/semana); el proyecto entero hasta la entrega son **145–180 horas**. Hay **36 preguntas** en H y **24 contradicciones, huecos o dudas** en D. El plan cabe en la semana del 23 de noviembre **solo si dedicas unas 18 horas por semana**; con 12–15 horas la fecha realista es el 6 de diciembre y el nivel 2 no entra.

---

## A. El plan de todo el proyecto

### A.1 El calendario que hay

| Dato | Valor |
|---|---|
| Hoy | Jueves 24 de septiembre de 2026 |
| Entrega prevista | Semana del 23 de noviembre (lunes 23) → **8 semanas y 4 días** (60 días) |
| Límite oficial | 6 de diciembre → 13 días más de colchón |
| Crédito de Claude Code en la nube | Hay que **reclamarlo antes del 7 de octubre** y **caduca el 4 de noviembre** (ver E.4) |

### A.2 Las fases, en orden

Las fases salen del spec (apartado 11, «qué pasa después de la S13») y de tu mensaje. Las horas son **horas de trabajo real tuyas** (con las explicaciones, los errores y las pausas para entender), estimadas para alguien que nunca ha programado. La columna «Depende de» dice qué preguntas de H cambian esa fase.

| # | Fase | Qué se hace | Horas | Semanas (si hay ~18 h/sem) | Depende de |
|---|---|---|---|---|---|
| 0 | **Cierre de este análisis** | Mañana: leer este archivo, discutir el plan hasta que lo puedas contar con tus palabras, contestar H, decidir instalación/skills/chats. **La S1 no empieza hasta cerrar esto** | 2–3 | 25–26 sep | Todo H |
| 1 | **Preparar el PC** (dentro de la S1, [Claude]) | Activar la virtualización en la BIOS, completar el asistente de Android Studio (SDK), crear el emulador, arreglar la identidad de Git, ajuste `attribution`. Es lo único del proyecto que **no puedo hacer yo**: son clics tuyos, guiados paso a paso | 3–4 | 26–27 sep | P5, P18–P23 |
| 2 | **Nivel 1 del prototipo: 13 sesiones** (detalle en B) | S1 proyecto y repositorio · S2 entidades · S3 DAOs y dominio puro · S4 repositorios · S5 selector y PIN · S6 Panel · S7 plato · S8 Pedir · S9 Cuenta · S10 Resumen de ingresos · S11 pruebas Room · S12 fotos · S13 cierre y Release `v1-nivel1` | **90–115** | 27 sep → **8 nov** | P1, P6–P17, P24, P25 |
| 3 | **Revisión del prototipo** contigo (P233 → paso 1) | Repasar lo hecho pantalla a pantalla; decidir P227 (`CLAUDE.md` público) y P228 (marcas `[Claude]`) | 3–4 | 8–9 nov | — |
| 4 | **Memoria** (fase 7, el trabajo escrito, 70 % de la nota) | Lo que depende del código (spec, apartado 15): diagrama de clases definitivo desde el código, estructura de carpetas, tabla de pruebas con resultados, diario fundido, tabla diseñado/implementado, declaración de uso de IA (P37), Anexos I y II. Se escribe en la app de Claude con tu skill `hacer-doc`; Claude Code solo genera los artefactos que salen del código | 25–35 | 9–20 nov | P4 |
| 5 | **Pasada de apropiación** (P230 → D) | Yo hago la lista de lo que no sabrías explicar; tú eliges; **tú tecleas y yo guío**; se repiten las pruebas de esa parte; commit y ficha «reescrito por Daniel». Después se actualizan el apartado 7, la declaración de IA y las capturas | 8–12 | 9–15 nov (en paralelo con la memoria, a ratos) | P30 |
| 6 | **Nivel 2, solo si queda tiempo** | Incrementos del 4.2 en orden, cada uno con pruebas, wireframes redibujados y Release `v1-inc<N>`. **Puerta [Claude]:** solo entra si el nivel 1 está cerrado el **1 de noviembre** y la memoria ya avanzada; el único candidato realista es el **incremento 1 (etiquetas y chips)**, 10–12 h | 0–12 | 2–8 nov como mucho | P1, P3 |
| 7 | **Vídeo** (10 min, 30 %, sin preguntas) | Guion = recorrido del nivel 1 (spec, apartado 4); ensayos; grabar el emulador y la voz; montaje; comprobar que **explicas el código sin leerlo** | 8–12 | 16–21 nov | — |
| 8 | **Entrega** | Última lectura, PDF, Release con APK comprobada, subir | 2–3 | 22–23 nov | P2 |
| | **Total** | | **145–180 h** | | |

### A.3 Lo que cabe y lo que no, según las horas que tengas (→ P1, P2)

| Horas reales por semana | Nivel 1 cerrado | Memoria + vídeo | Entrega | Nivel 2 |
|---|---|---|---|---|
| **18 o más** | ~8 nov | 9–21 nov | **23 nov** | Incremento 1 solo si el nivel 1 cierra el 1 nov |
| **12–15** | ~20 nov | 21 nov – 5 dic | **6 dic** (límite) | Ninguno; todo declarado *diseñado, no implementado* |
| **Menos de 10** | No cabe ni el 6 dic sin recortar | | | Hay que decidir recortes antes de empezar (ver A.5) |

**Mi lectura honesta:** 90–115 h para el nivel 1 son muchas horas para «trabajo a rachas». No es pesimismo: en tu primer proyecto, cada sesión lleva dentro aprender la herramienta (dónde está cada cosa en Android Studio, qué significa cada error de Gradle) además del código. Las tres primeras sesiones serán las más lentas por eso; a partir de la S5 el ritmo sube porque las herramientas ya no son nuevas.

### A.4 Reglas del plan (las que ya están decididas en el spec)

1. **El nivel 1 entero, probado y documentado antes de tocar el nivel 2** (spec 4, CLAUDE.md).
2. **Cada sesión cierra con:** pruebas que tocan pasando, ficha del diario, `estado-nivel.md`, commit `S<N>: <objetivo>` y push. Si no se llega, la ficha dice por qué.
3. **La foto es la última pieza del nivel 1** (S12): toda la app funciona sin fotos.
4. **Las tres pruebas de Room (S11) tienen red de seguridad:** si no arrancan, se documenta y R1, R7 y R16 quedan cubiertas por las manuales.
5. **Aprobar seguro antes que nota:** donde el plan choque con el calendario, se recorta nivel 2 primero, nunca memoria ni vídeo.

### A.5 Riesgos que veo, de mayor a menor

| Riesgo | Por qué importa | Qué propongo |
|---|---|---|
| **La virtualización está desactivada en tu BIOS** (comprobado esta noche, ver E.1) | Sin ella el emulador va tan lento que no se puede probar nada. Bloquea la S1 | Activar «SVM Mode» en la BIOS antes de la S1 (pasos en E.2). Es un cambio de un minuto, pero hay que reiniciar y entrar en la BIOS |
| **Horas por semana** | Es la variable que decide la fecha | Contestar P1 con sinceridad; revisar el calendario cada dos sesiones |
| **S6 y S8 son las sesiones difíciles** (listas anidadas en el Panel; la carta con secciones, ficha, carrito y Enviar) | Son donde un principiante se atasca | Plan Mode al empezar cada una (→ P25), dividir en piezas de 30–45 min, no abrir la siguiente sesión con la anterior a medias |
| **Memoria y vídeo se dejan para el final** | Son el 100 % de la nota; el código solo se evalúa a través de ellos | Fecha límite interna para cerrar el nivel 1: **8 nov**. Si el 1 nov no está en S11, se para el nivel 1 donde esté y se documenta |
| **El diario se rellena «después»** | Sin la ficha de cada sesión no hay apartado 7 ni declaración de IA creíble | La skill de sesión (F.1) lo hace obligatorio al abrir y al cerrar |
| **Cambios de diseño a mitad** | Cada P nueva cuesta horas | Las dudas de D se cierran mañana, de una vez; después, solo cambia el diseño lo que rompa una prueba |

---

## B. El plan del prototipo, sesión por sesión

Para cada sesión: **qué se construye** (del spec, apartado 11), **qué te explico** (herramientas y conceptos, antes del código), **qué tienes que saber defender** (lo que el vídeo o el corrector pueden pedirte) y **qué prueba la cierra**. Las horas son de trabajo tuyo. Todas las sesiones empiezan y terminan con el ritual del spec (ficha del diario, `estado-nivel.md`, commit y push).

### Preparación del PC (antes de la S1, dentro de su ficha) [Claude] — 3–4 h → P5, P18–P23

- **Qué se hace:** activar SVM en la BIOS · abrir Android Studio y completar su asistente (descarga el SDK: plataforma, herramientas, emulador) · crear el emulador Pixel 6 · configurar nombre y correo de Git para este repositorio · crear el ajuste `attribution` en tus ajustes de usuario de Claude Code.
- **Qué te explico:** qué es la BIOS y por qué el emulador la necesita; qué es el SDK y qué es un emulador; qué es Git (una máquina de fotos del proyecto) y qué es GitHub (dónde se guardan las fotos); qué es un commit.
- **Qué tienes que saber defender:** nada de código todavía; sí, en una frase, qué es cada herramienta.
- **Cierra con:** el emulador arranca y enseña la pantalla de inicio de Android.

### S1 — Proyecto y repositorio — 4–6 h

- **Qué se construye:** el proyecto de Android Studio **dentro de `C:\AKO`**, al lado de `docs/` (te explico cómo: el asistente crea una carpeta con el nombre del proyecto; la crearemos como subcarpeta, p. ej. `C:\AKO\app-ako\`, o bien se crea en otro sitio y se mueve su contenido; te propondré la opción con menos riesgo, → P22 para el nombre) · Kotlin DSL, `minSdk 26`, ViewBinding, catálogo de versiones con Room 2.8.5, Glide 5.0.9, KSP, corrutinas y las dependencias de pruebas · `.gitignore`, `README.md`, `git init`, repositorio público en GitHub, primer commit y push · comprobar que el commit **no lleva `Co-Authored-By`**.
- **Qué te explico:** qué es cada archivo que aparece (`build.gradle.kts`, `libs.versions.toml`, `AndroidManifest.xml`, `MainActivity`, `res/`); qué es Gradle (el que construye la app) y qué significa «sincronizar»; qué es una dependencia; cómo se lee un error de compilación; `git status`, `add`, `commit`, `push`, y qué ves en GitHub.
- **Qué tienes que saber defender:** la estructura de carpetas del proyecto (apartado 3 del spec) y por qué el repositorio es público desde el primer día (RNF-25).
- **Cierra con:** la app vacía arranca en el emulador; el repositorio se ve en GitHub; el primer commit sin atribución.

### S2 — 14 entidades, `Converters`, `AppDatabase`, `Precarga` — 5–7 h

- **Qué se construye:** las 14 clases `@Entity` con sus columnas en `snake_case`, índices únicos y claves foráneas (`RESTRICT` en todas salvo `linea_modificador → linea_comanda`, `CASCADE`) · los dos `enum` con `@TypeConverter` · `AppDatabase` con una sola instancia · `Precarga` (Otros con `esPorDefecto`, 60 mesas, 14 alérgenos, 3 etiquetas, 1 categoría y 1 plato de ejemplo → P7, P16).
- **Qué te explico:** qué es una tabla y una fila; qué es una clave primaria y una foránea; qué es un índice único; por qué los precios van en céntimos `Int`; qué hace Room con una anotación; qué es el Database Inspector de Android Studio y cómo mirar las tablas.
- **Qué tienes que saber defender:** las 14 tablas de memoria (por grupos, ver C.3), R11 (por qué solo una CASCADE), R16 (por qué `esPorDefecto` y no el nombre), R8 (por qué no hay `CHECK`).
- **Cierra con:** compila; el inspector enseña las 14 tablas con Otros, 60 mesas, 14 alérgenos, 3 etiquetas y el ejemplo.

### S3 — DAOs y dominio puro — 7–9 h

- **Qué se construye:** los 5 DAOs (`CategoriaDao`, `ProductoDao` con `visibles()` y `existeNumero`, `PrecargadosDao`, `MesaDao`, `ComandaDao` con `pendienteDeMesa`, `pendientesConTotal`, `pagadasEntre`, `mesasConProductoPendiente`…) · `Carrito`, `LineaCarrito`, `Calculadora`, `Validacion`, `Hash` en `dominio/` · **las seis pruebas de `test/`**: P-C-01 a P-C-05 y P-C-09.
- **Qué te explico:** qué es SQL y qué es una consulta parametrizada (`:parametro`), por qué nunca texto concatenado (RNF-08); qué es un JOIN (la consulta de plato visible); qué es una función pura y por qué se prueba sin emulador; qué es JUnit y cómo se lee un test que falla; qué es PBKDF2 con palabras sencillas (sal, iteraciones, hash).
- **Qué tienes que saber defender:** R15 (la consulta de visibles), R4 (Carrito y Validacion), R8 (P-C-09), cómo se guarda el PIN y por qué no se puede recuperar. **Estas seis pruebas son las que mejor demuestran comprensión en el vídeo**: pocas líneas, entrada y salida claras.
- **Cierra con:** compila y las seis pruebas de `test/` pasan en verde en Android Studio.

### S4 — Repositorios y `PinStore` — 6–8 h

- **Qué se construye:** `CartaRepository`, `ComandaRepository`, `SeguridadRepository`, `PinStore`. Cada método con un comentario de una línea con la regla que garantiza: `enviarCarrito` (R1, R2, R14, en `@Transaction`), `quitarLinea` (R7), `eliminarPlato`/`eliminarCategoria` (R6, devuelven las mesas), `guardarPlato` (R8, R9, cadena 3e vía `ResultadoGuardado`), `guardarCategoria` (R16), `hayPlatoVisible` (puerta de Pedir), `pagadasEntre` → `ResumenIngresos`.
- **Qué te explico:** qué es una capa y por qué las pantallas no ven los DAOs; qué es una transacción; qué es una corrutina y `Dispatchers.IO` (con la metáfora de «lo lento se hace en otra cola»); qué es `SharedPreferences`; qué es inyectar los DAOs por constructor (hace falta para P-C-09 con DAO falso, → D17).
- **Qué tienes que saber defender:** **la tabla «quién garantiza cada regla» (spec, apartado 6)** entera: es el argumento del vídeo (P128): R9 la impone la base de datos, R1 no puede y la impone el repositorio.
- **Cierra con:** compila; cada método lleva su comentario; las seis pruebas siguen en verde. **[Claude] propongo una revisión externa del código de S2–S4 con `/code-review` antes de seguir**: es la base de todo lo demás (→ P35).

### S5 — Selector y PIN (1a, 1b, 1c, 1e) + `ConfirmacionDialog` — 7–9 h

- **Qué se construye:** `SelectorActivity` con `SelectorFragment` (tres botones grandes), `CrearPinFragment` (dos campos, aviso, Aceptar apagado hasta 4+4 cifras), `PinDialog` (cuatro cifras ocultas, teclado numérico, «PIN incorrecto» con **sacudida** `ObjectAnimator`, intentos ilimitados), `CambiarPinDialog`, `SelectorViewModel`, el `ConfirmacionDialog` único, `strings.xml` en español, tema base con el naranja de acento, orientación vertical fija.
- **Qué te explico:** Activity, Fragment y ViewModel con palabras sencillas (la pantalla, un trozo de pantalla, la memoria de la pantalla que sobrevive al giro); ViewBinding; `LiveData` y «observar»; un `DialogFragment`; XML con `ConstraintLayout`; `sp` frente a `dp`; el editor de diseño de Android Studio.
- **Qué tienes que saber defender:** el flujo de la ficha 1 (primer arranque → 1b; después 1a), por qué un solo `ConfirmacionDialog` (P84, P121), por qué la sacudida es «una animación con función».
- **Cierra con:** P-M-01, P-M-02, P-M-03 pasan. P-M-13 y P-M-29 **solo en parte** (necesitan 1d, 6a y el Panel, que llegan en S6–S9; → D7, P6).

### S6 — Panel (2a, 2b, 2e) — 10–12 h — **la primera sesión difícil**

- **Qué se construye:** `PanelActivity` con `RecyclerView` exterior de categorías, cada caja con cabecera (nombre, lápiz), `RecyclerView` interior de **altura fija 270 dp** con `RecycledViewPool` compartido y `[+ Plato]` fuera del scroll interior; `CategoriaAdapter`, `FilaPlatoAdapter` (miniatura «?», número, nombre cortado, precio, *Eliminado*); `CategoriaBottomSheet` (nombre, foto → P13, interruptor solo al editar; Otros sin interruptor); `MesasAfectadasDialog` (R6 agrupado); *Categoría eliminada* con platos atenuados; `[Resumen de ingresos]`, `[Cambiar PIN]`, `[Terminar]`, `← Atrás`; `PanelViewModel`.
- **Qué te explico:** qué es un `RecyclerView` y un adaptador (la lista que recicla filas); por qué **nunca un `ScrollView` con listas dentro** (verificación 3); qué es una hoja inferior; cómo se pinta un estado sin depender del color (RNF-13); orden por `orden` con Otros la última.
- **Qué tienes que saber defender:** «se reutiliza el componente, no la pantalla» (`FilaPlatoAdapter` en 2a y 5a), P99 (altura fija), R6 extendido a categorías, R16 en pantalla.
- **Cierra con:** P-M-05 pasa. P-M-04 y P-M-06 **en parte** (P-M-04 pide una foto, S12; P-M-06 pide una comanda enviada desde Pedir, S8; → P6).

### S7 — Plato sin foto (3a, 3d, 3e) — 8–10 h

- **Qué se construye:** `PlatoActivity` con scroll único y barra fija con Guardar (apagado sin los cuatro obligatorios); número entero, precio en euros ↔ céntimos (0,00 admitido, el signo no se admite); categoría preelegida desde `[+ Plato]`; descripción; 14 alérgenos con casillas; interruptor *En la carta* (apagarlo elimina → 3d con R6); aviso «Si alguien tiene apuntado el N…» al cambiar el número; aviso de número repetido (R9, `existeNumero`); «Se perderán los cambios» al salir; **cadena 3e** (dos `ConfirmacionDialog` seguidos a partir de `ResultadoGuardado.CategoriaEliminada`); `PlatoViewModel`. La foto **no**: solo el hueco con «?».
- **Qué te explico:** validar un formulario (cuándo se enciende un botón); convertir «18,50» a 1850 sin coma flotante; `OnBackPressedCallback`; cómo una pantalla reacciona a un resultado del repositorio.
- **Qué tienes que saber defender:** la cadena 3e con las palabras de la ficha («ya es decisión del propietario»), R9 y R14 (editar un plato no cambia lo pedido).
- **Cierra con:** P-M-07, P-M-09, P-M-11 pasan; P-M-10 **en parte** (pide una comanda de la mesa 6; → P6).

### S8 — Pedir (1d, 5a, 5b, 5c, Enviar, salir con PIN, RF-38) — 12–15 h — **la sesión más larga**

- **Qué se construye:** `RejillaMesasFragment` en modo *elegir* (4 × 15, 73 dp, rojo con total a 12 sp; `MesaAdapter`; alojado en `SelectorActivity`); la puerta de Pedir con sus dos mensajes; `PedidoActivity` con «Mesa N» y *Salir*; `CartaFragment` (fila horizontal de categorías con foto redonda o círculo «?», la activa resaltada, Otros la última, solo categorías con algún plato visible; lista vertical por secciones con dos tipos de fila; tocar una categoría → `scrollToPositionWithOffset`); `FichaPlatoFragment` (foto o «?», número, nombre entero, precio, descripción, alérgenos desplegables o el aviso «pregunta al personal», cantidad 1–99 con −/+ bloqueados, «Añadir — precio»); pastilla del carrito (número y total, sin animación); `CarritoFragment` con `LineaAdapter` (+/−, Quitar, total, Enviar apagado si vacío); **Enviar** con confirmación «Mesa N · total» → `enviarCarrito`; *Salir* y Atrás del sistema → aviso RF-38 si hay carrito → `PinDialog` → 1a; el carrito vive en `PedidoViewModel` y se vacía al salir.
- **Qué te explico:** un `RecyclerView` horizontal; dos tipos de vista en una lista; `activityViewModels()` (los tres Fragments comparten el carrito); qué significa «el carrito vive en memoria» (RNF-24); el `OnBackPressedCallback`.
- **Qué tienes que saber defender:** todo el rasgo diferencial (ficha 5): R2 (cada envío crea líneas nuevas), R4, R14, R15 en la carta, por qué la pastilla y no una barra, P123 (el mensaje que no se puede provocar).
- **Cierra con:** P-M-14 a P-M-21 pasan. Además, con las comandas que ahora se pueden crear, se completan P-M-06 y P-M-10 (S6/S7) y P-M-13.

### S9 — Cuenta (6a, 6b, 6c) — 9–11 h

- **Qué se construye:** `CuentaActivity` sin PIN; `RejillaMesasFragment` en modo *gestionar* (blanca → snackbar «La mesa N no tiene comanda»; roja → 6b); `ComandaFragment` (líneas cantidad × nombre e importe, TOTAL calculado, Quitar por línea sin aviso salvo la última → aviso R7 → ANULADA y mesa libre, **Anular** con confirmación, **Dar la cuenta**); `ReciboFragment` (nombres y precios congelados, TOTAL, campo *Entregado* opcional → cambio con `Calculadora.cambio`, negativo si falta, **Cobrar** con confirmación → PAGADA con `fechaCierre`, vuelve a 6a y la mesa queda blanca); `CuentaViewModel`.
- **Qué te explico:** un Fragment con un parámetro (`soloLectura`, `modo`); cómo se recalcula el total al quitar una línea; qué es un estado en la base de datos (PENDIENTE/PAGADA/ANULADA) y por qué no hay «borrar».
- **Qué tienes que saber defender:** R3 (ocupación calculada), R7, R10, R14 en el recibo, por qué el verde no está (nivel 3) y por qué el Resumen no vive aquí (RNF-10). Es **el final del guion del vídeo**.
- **Cierra con:** P-M-22 a P-M-28 pasan; P-M-29 se completa.

### S10 — Resumen de ingresos (2g) — 4–5 h

- **Qué se construye:** `ResumenIngresosActivity` con `DatePickerDialog` (hoy por defecto), lista de comandas PAGADAS del día (mesa, hora, importe) con `pagadasEntre(inicio, fin)` en la zona horaria del dispositivo, pie con cuántas y el total, «No se cobró ninguna comanda ese día», tocar una → `ReciboFragment` en solo lectura; `ResumenIngresosViewModel`.
- **Qué te explico:** fechas como instante (`Long`) y `java.time` (API 26+); calcular las 0:00 y las 23:59:59.999 de un día; un selector de fecha estándar.
- **Qué tienes que saber defender:** «es una consulta, no una tabla» (ficha 6), por qué nunca «las mesas en verde», y que es el caso de uso que justifica R14.
- **Cierra con:** P-M-12 pasa.

### S11 — Pruebas de Room en `androidTest/` — 4–6 h (con red de seguridad)

- **Qué se construye:** P-C-06 (una comanda abierta por mesa, dos envíos → una comanda con dos líneas congeladas), P-C-07 (quitar la última línea → ANULADA con `fechaCierre`, mesa libre), P-C-08 (una sola categoría por defecto, no se elimina, no se crea otra), con `Room.inMemoryDatabaseBuilder` y la precarga ejecutada.
- **Qué te explico:** la diferencia entre `test/` (PC) y `androidTest/` (emulador); una base de datos en memoria; por qué se descartó Room KMP (P124).
- **Qué tienes que saber defender:** que R1, R7 y R16 tienen prueba de código **y** manual, y qué dice la ficha si las tres no arrancan (P117).
- **Cierra con:** P-C-06, 07, 08 pasan, o la ficha dice por qué no y la memoria lo repetirá tal cual.

### S12 — Fotos — 5–7 h

- **Qué se construye:** `ImageStore.guardar(uri)` (`inSampleSize`, lado mayor ~1080 px, JPEG calidad ~85, archivo en `filesDir/fotos/`, devuelve la ruta, en `Dispatchers.IO`), `borrar`; `PickVisualMedia` en 3a (y en 2b → P13); Glide en `FilaPlatoAdapter` (Panel y carta), en la fila de categorías y en 5b, con placeholder neutro y «?» de error; `contentDescription` «Sin foto».
- **Qué te explico:** qué es un bitmap y por qué una foto de cámara no cabe en una fila de base de datos (verificación 6); qué es Glide y su caché; el selector de fotos del sistema (sin permisos); cómo meter una foto grande en la galería del emulador para P-M-08.
- **Qué tienes que saber defender:** «archivo, nunca BLOB», por qué la foto es la última pieza (toda la app funciona sin fotos), sin permiso `INTERNET`.
- **Cierra con:** P-M-08 pasa; P-M-04 se completa.

### S13 — Cierre del nivel 1 — 6–8 h

- **Qué se construye:** tema definitivo (paleta neutra, naranja, sin rojo ni verde de acento), `strings.xml` **EN** completo, `contentDescription` en todo, **Accessibility Scanner** en las siete pantallas (texto ≥ 12 sp, 48 dp, contraste), pasada por las 29 manuales que faltaran y las 9 de código con **fecha** en `spec+doc-pruebas.md`, `estado-nivel.md` con los 29 RF en *implementado*, etiqueta `v1-nivel1`, **APK debug** (Build → Build APK) y **Release en GitHub** con la APK adjuntada a mano por ti.
- **Qué te explico:** qué es una etiqueta de Git y una Release; qué es una APK; instalar y usar el Accessibility Scanner en el emulador; cómo cambiar el idioma del emulador para ver el EN.
- **Qué tienes que saber defender:** RNF-11 a RNF-13 con las capturas del Scanner; que la APK de la Release es la que instala el Anexo I.
- **Cierra con:** las 29 P-M y las 9 P-C anotadas con fecha; la Release `v1-nivel1` visible en GitHub. **Aquí termina el nivel 1.**

---

## C. Lo que he entendido, con mis palabras

### C.1 La app

Ako es una app Android en Kotlin para **un solo móvil del restaurante, en vertical**. Se deja en la mesa para que el cliente pida solo, o la lleva el camarero. Lo diferencial es **la carta** (pantalla 5): pensada para que cualquiera pueda pedir, con fotos, alérgenos, filtros, idiomas y letra grande; en el nivel 1 entran fotos y alérgenos, y filtros, idiomas y Agrandar son incrementos. Tres roles desde una pantalla de tres botones: **Propietario** (crea la carta y ve el Resumen de ingresos, detrás de un PIN de 4 cifras que no se puede recuperar), **Pedir** (elige mesa → carta → carrito → Enviar; salir pide el PIN) y **Cuenta** (rejilla de 60 mesas, ver lo pedido, quitar líneas, anular, recibo con calculadora de cambio, cobrar; sin PIN). Todo local con Room, sin red, sin permiso `INTERNET`. La arquitectura cliente-servidor está **diseñada** pero **no se programa**: es un recorte de alcance declarado. Es un trabajo académico: memoria (70 %) y vídeo de 10 minutos (30 %); la normativa permite la IA como apoyo, no como sustituto, y por eso cada pieza se explica antes de escribirla y el diario dice quién hizo qué.

### C.2 El nivel 1

Es **lo único obligatorio** y es **el guion del vídeo**: un propietario nuevo crea el PIN → crea una categoría → crea un plato → entra en Pedir y pide → entra en Cuenta, ve lo pedido y el total → quita una línea → «ha pagado con esto» y sale el cambio → cobra → mira el Resumen de ingresos. Son **29 RF** (RF-01 a 06, 08 a 12, 20, 24 a 26, 28 a 30, 36 a 38, 40 a 46 y 50) repartidos en siete pantallas de las fichas, de las que en el nivel 1 se programan **cinco** (1, 2 con 2g, 3, 5 y 6; la 7 y la 8 son nivel 2 enteras; la 4 no existe). Fuera del nivel 1 pero con la tabla ya creada: etiquetas y chips, kiosco, Agrandar, modificadores, nutrición, vista previa, resumen por periodos, orden y selección masiva, Cuenta completa, idiomas, foto nutricional y agotado temporal (12 incrementos, en ese orden). Nivel 3: verde con Liberar, «lo pedido por la mesa». Se prueba con 29 manuales (una por RF) y 9 de código (6 puras en el PC, 3 con Room en el emulador).

### C.3 Las 14 tablas

Las agrupo en tres bloques para recordarlas:

- **La carta (10):** `categoria` (nombre único, `orden`, `activo`, `esPorDefecto`; Otros es la única por defecto y siempre la última) · `producto` (número entero único, precio en céntimos, `activo`, `disponible` siempre `true` en nivel 1, `imagen` e `imagenNutricional` como rutas) · `producto_nutricion` (1:1, siete `Int?` en kJ y mg; sin fila = sin datos) · `alergeno` (14 fijas) · `producto_alergeno` (N:M, PK compuesta) · `etiqueta` (nombre único, 3 precargadas) · `producto_etiqueta` (N:M) · `idioma` (código de dos letras único; el español no está) · `producto_traduccion` (PK compuesta producto+idioma; sin fila = pendiente) · `modificador` (AÑADIR/QUITAR, QUITAR a 0).
- **La sala (1):** `mesa` (60 filas fijas, número único, sin `activo` ni estado: la ocupación se calcula).
- **Los pedidos (3):** `comanda` (mesa, estado PENDIENTE/PAGADA/ANULADA como texto, `fechaCreacion`, `fechaCierre?`) · `linea_comanda` (cantidad, **precio y nombre congelados**) · `linea_modificador` (nombre y precio congelados, cantidad; la **única CASCADE**, desde `linea_comanda`).

Reglas de datos: importes en céntimos `Int`; nada se borra salvo relaciones y líneas de comanda abierta (`activo = false` es *eliminar*; `disponible = false` es *desactivar*, otra cosa); RESTRICT en todas las FK salvo una; lo calculable no se guarda (total, ocupación, kcal, traducido/pendiente); nada de `CHECK` en las entidades; fechas como `Long`; precarga en `onCreate` con Otros antes que cualquier plato.

### C.4 Las 15 reglas y quién las garantiza

Son R1 a R16 **sin R13** (no existe: 15 reglas). Las agrupo por quién las cumple:

- **La base de datos:** R9 (número único, `UNIQUE`), R11 (RESTRICT y la única CASCADE).
- **`ComandaRepository`:** R1 (una comanda abierta por mesa: busca `pendienteDeMesa`; Room no puede con un índice parcial), R2 (Enviar crea o amplía; cada envío, líneas nuevas), R3 (ocupación = hay PENDIENTE), R7 (sin líneas → ANULADA con fecha), R10 (total calculado con `Calculadora`), R14 (congela nombre y precio), R8 al congelar el precio.
- **`CartaRepository`:** R6 (avisar de mesas afectadas al eliminar plato o categoría, sin borrar líneas, aviso agrupado), R8 (con `Validacion.precioValido`, prueba P-C-09), R9 antes de guardar (`existeNumero`), R12 (nivel 2), R15 (plato visible = activo y disponible y categoría activa, en la consulta `visibles()`), R16 (una sola por defecto, no se elimina, se renombra).
- **Dominio puro (`Carrito`, `Validacion`):** R4 (carrito no vacío, 1–99 por línea, modificadores 1–9).
- **Todos los repositorios por omisión:** R5 (no existe método que borre una entidad ni toque una comanda cerrada).

Lo que hay que saber decir en el vídeo (P128): **R9 la impone la base de datos y R1 no puede**; saber dónde vive cada garantía es lo que distingue entender el modelo de copiarlo.

### C.5 Las capas

`es.daniel.ako/` con cinco capas y una regla: **cada capa habla solo con la de abajo**.

1. **`datos/entidades`** — las 14 `@Entity`, dos `enum`, `Converters`. Describen; no piensan.
2. **`datos/dao`** — cinco DAOs, la única capa que sabe SQL, siempre parametrizado.
3. **`datos/repositorios`** — `CartaRepository`, `ComandaRepository`, `SeguridadRepository`: garantizan las reglas y devuelven modelos listos para la pantalla; no saben quién los llama. `AppDatabase` y `Precarga` también en `datos/`.
4. **`dominio/`** — solo lo puro: `Carrito`, `LineaCarrito`, `Calculadora`, `Validacion`, `Hash`, y `modelos/` (`MesaEstado`, `ResumenIngresos`, `ComandaConTotal`, `CategoriaConPlatos`, `ResultadoGuardado`). Nada de aquí toca Android: por eso se prueba en el PC.
5. **`seguridad/PinStore`** (sal + hash en `SharedPreferences`, nunca el PIN; llama a `Hash`) e **`imagenes/ImageStore`** (redimensionar, JPEG, archivo privado).
6. **`ui/`** — una Activity por pantalla (Selector, Panel, Plato, ResumenIngresos, Pedido, Cuenta), sus vistas como Fragments, **un ViewModel por Activity** compartido por sus Fragments (`activityViewModels()`), el carrito en `PedidoViewModel`; en `ui/comun/` lo reutilizado: `RejillaMesasFragment` (dos modos), `ReciboFragment` (con `soloLectura`), `ConfirmacionDialog` (uno para todo) y los cuatro adaptadores (`FilaPlatoAdapter`, `CategoriaAdapter`, `MesaAdapter`, `LineaAdapter`).

Stack fijo: Kotlin, XML con `ConstraintLayout` y ViewBinding (nada de Compose), Room 2.8.x con KSP, MVVM sencillo con corrutinas y `LiveData`, Glide 5.0.x, `minSdk 26`, Gradle Kotlin DSL con catálogo de versiones, emulador Pixel 6. Sin librerías fuera de la tabla del apartado 2. Textos siempre en `strings.xml` (ES y EN), nombres de negocio en español, paleta neutra con naranja de acento, sin gestos, todo con botón, texto ≥ 12 sp, objetivos ≥ 48 dp.

---

## D. Contradicciones, huecos y dudas

No resuelvo ninguna: las apunto con el documento y el apartado, y donde hace falta decidir hay una pregunta en H. Están ordenadas de más a menos importantes para el código.

| # | Qué he visto | Dónde | Pregunta |
|---|---|---|---|
| D1 | **La categoría y el plato de ejemplo de la precarga no tienen nombre, número, precio ni descripción en ningún documento.** Hacen falta para escribir `Precarga` en la S2 y para P-M-29 y P-M-14 | `spec-claude-code.md` 5.1 (Precarga); `spec+doc-clases.md` 3.2; RF-50; P-M-29 | P7 |
| D2 | **El juego de datos de las pruebas ignora la categoría de ejemplo.** P-M-04 espera las cajas «Carnes, Postres, Otros» y P-M-17 espera en la carta solo «Carnes y Otros», pero la categoría de ejemplo con su plato visible estaría ahí también, salvo que se elimine antes. P-M-14 sí la tiene en cuenta («el plato de ejemplo eliminado») | `spec+doc-pruebas.md` 2 (juego de datos), P-M-04, P-M-05, P-M-17 | P8 |
| D3 | **Varias pruebas que «cierran» una sesión necesitan pantallas de sesiones posteriores.** S5: P-M-13 pide 1d (S8) y 6a (S9); P-M-29 pide Panel, Cuenta y 1d. S6: P-M-04 pide «Entrecot con foto» (S12) y P-M-06 pide una comanda enviada desde Pedir (S8). S7: P-M-10 pide una comanda de la mesa 6 (S8). El spec dice «platos creados a mano en el inspector si hace falta», pero no dice nada de comandas ni de fotos | `spec-claude-code.md` 11 (S5, S6, S7); `spec+doc-pruebas.md` 3 | P6 |
| D4 | **«reactivar» frente a «recuperar»** en el segundo aviso de la cadena 3e: el spec (6) dice *«¿Quieres reactivar <categoría>?»* y el wireframe `dialogo-3e-2-reactivar` dice *«¿Quieres reactivar Carnes?»* con botón *Reactivar*; la ficha 3 y P-M-11 dicen *«¿Quieres recuperar…?»*, y el vocabulario de `CLAUDE.md` es *recuperar*. Hay que elegir el literal de `strings.xml` | `spec-claude-code.md` 6 (cadena 3e); `docs/wireframes/dialogo-3e-2-reactivar.png` y su leyenda; `spec+doc-pantallas.md` pantalla 3, 3e; P-M-11 | P9 |
| D5 | **Precio negativo: dos mecanismos para la misma regla.** El spec (5.1) y P-C-09 dicen que un precio negativo **lanza `IllegalArgumentException`**; el spec (6) da `ResultadoGuardado` con cuatro valores (Ok, NumeroRepetido, NombreRepetido, CategoriaEliminada); pero `spec+doc-clases.md` 3.2 dice que `ResultadoGuardado` también dice «precio negativo (R8)» | `spec-claude-code.md` 5.1 y 6; `spec+doc-clases.md` 3.2 (`ResultadoGuardado`); P-C-09 | P11 |
| D6 | **Foto de categoría en el nivel 1.** RF-05 (nivel 1) dice «nombre, foto, activo» y el wireframe 2b dibuja «Foto (opcional) + Elegir»; pero la S12 (Fotos) solo nombra «ImageStore, selector de fotos, Glide en Panel, 5a y 5b», P-M-08 solo prueba la foto del plato y P-M-05 crea las categorías «sin foto». No queda escrito en qué sesión entra la foto de categoría ni qué hace el botón *Elegir* de 2b en la S6 | RF-05; `02b-editar-categoria.png`; `spec-claude-code.md` 11 (S12); P-M-05, P-M-08 | P13 |
| D7 | **Wireframe con vocabulario viejo:** el fondo de `dialogo-1e-cambiar-pin.png` dice «Resumen por día» y «Categoría desactivada» (anteriores a P149 y P153). El documento de wireframes dice que solo se redibujaron `02a`, `02b` y `05a`. Además, la barra de `02g-resumen-ingresos.png` dice solo «Resumen» | `docs/wireframes/dialogo-1e-cambiar-pin.png`; `spec+doc-wireframes.md` cabecera | P10 |
| D8 | **La puerta de Pedir es un aviso de un solo botón, pero se dibuja con la caja de `ConfirmacionDialog`**, que el spec define con «botón afirmativo, botón negativo». Falta decir si el diálogo admite un solo botón o si la puerta es otra cosa (snackbar) | `spec-claude-code.md` 7 (ConfirmacionDialog); `spec+doc-wireframes.md` 2 (último párrafo); ficha 1, validaciones («Se pulsa Pedir y no hay ningún plato VISIBLE») | P15 |
| D9 | **Cómo se hace la precarga técnicamente.** `Precarga` es un `RoomDatabase.Callback` y `onCreate` recibe la base de datos «cruda», no los DAOs; el diagrama de clases dice que `PrecargadosDao` tiene «inserción para la precarga», y RNF-08 prohíbe SQL concatenado. Hay dos formas (lanzar una corrutina que use los DAOs desde el callback, o `execSQL` en el callback) y ningún documento elige | `spec+doc-clases.md` 3.2 (`Precarga`, `PrecargadosDao`); `spec-claude-code.md` 5.1 (Precarga); RNF-08 | P16 |
| D10 | **P-C-09 con «un DAO falso»** exige que `CartaRepository` reciba sus DAOs por constructor (para poder pasarle uno falso en `test/`). Es una decisión de diseño que ningún documento escribe, y afecta a cómo se construyen los tres repositorios y quién los crea (¿`AppDatabase`? ¿una clase `Ako : Application`?) | `spec-claude-code.md` 12 (P-C-09); `spec+doc-pruebas.md` 4 | P17 (parte) |
| D11 | **`MesaConTotal` no existe en la lista de modelos.** `ComandaDao.pendientesConTotal()` devuelve `List<MesaConTotal>` en el diagrama, pero los modelos del spec son `MesaEstado · ResumenIngresos · ComandaConTotal · CategoriaConPlatos · ResultadoGuardado`. Hace falta saber cómo se llama y dónde vive la proyección del DAO | `spec+doc-clases.md` 4 (`clases-acceso.puml`, ComandaDao); `spec-claude-code.md` 3 (`dominio/modelos/`) | Lo decido en S3 y lo marco [Claude], salvo que digas otra cosa |
| D12 | **El emulador del spec es «Pixel 6, API 34»** y el `targetSdk` «el último estable» (con Android Studio Quail y AGP 9.4 será 36 o 37). Hay que elegir qué imagen de sistema se descarga | `spec-claude-code.md` 2 (Dispositivo, Android mínimo); nota a RNF-20 | P20 |
| D13 | **Las dependencias base no están en la tabla del stack.** La tabla del apartado 2 lista Room, Glide, KSP y las de pruebas, y dice «sin librerías que no estén en esta tabla»; pero la app necesita `appcompat`, `material` (Material 3, apartado 10), `constraintlayout`, `activity-ktx`/`fragment-ktx`, `lifecycle-viewmodel-ktx` y `lifecycle-livedata-ktx` (corrutinas y LiveData, apartado 2) y `kotlinx-coroutines-android`. Doy por hecho que cuentan como «del spec», pero conviene dejarlo escrito | `spec-claude-code.md` 2 (tabla y frase «Sin librerías…») | P17 |
| D14 | **El stack real de septiembre de 2026 difiere en detalles del spec (no en fondo):** Room estable es **2.8.5** (9 sep 2026, exige Kotlin 2.0+ y `minSdk` 23); Glide estable es **5.0.9** con procesador KSP (`minSdk` 23); Android Studio estable es **Quail 4** (1 sep 2026) y el instalado es **2026.1.3 (Quail 3)**; con **AGP 9.x** Kotlin viene **integrado** (el plugin `org.jetbrains.kotlin.android` ya no se aplica), `buildConfig` viene apagado y `targetSdk` hay que ponerlo a mano. Nada de esto contradice el diseño; cambia lo que verás en el asistente de la S1 | `spec-claude-code.md` 2; comprobado en developer.android.com y github.com/bumptech/glide el 24 sep 2026 | P19 |
| D15 | **Resaltado del plato al volver al Panel:** el spec (10) y la ficha 2 (párrafo de nivel) lo ponen en **nivel 3**, pero la ficha 3 («Al guardar y volver: el Panel… resalta el plato un segundo») y la ficha 2 (validaciones, «Al volver de crear o editar un plato… se resalta un segundo») lo cuentan sin marca de nivel | `spec+doc-pantallas.md` pantalla 3 validaciones; pantalla 2 validaciones; `spec-claude-code.md` 10 | P14 |
| D16 | **Ficha 3 dice «Volver a activar un plato… es la forma de gestionar un “agotado” temporal».** Es un resto anterior a P153: el agotado es `disponible` (incremento 12), no `activo` | `spec+doc-pantallas.md` pantalla 3, validaciones («Volver a activar un plato») | Solo aviso: no afecta al nivel 1 |
| D17 | **P-C-05, alcance:** el spec (12) dice «`Hash.pbkdf2()` y `PinStore.coincide()` con un `SharedPreferences` falso **o solo `Hash`**»; el plan de pruebas (4) solo cita `Hash`. Un `SharedPreferences` falso es trabajo extra sin valor | `spec-claude-code.md` 12; `spec+doc-pruebas.md` 4 (P-C-05) | P12 |
| D18 | **Cobrar y el verde.** La ficha 6 (flujo y validaciones) dice que al cobrar «la cuadrícula se pone verde»; el spec (4.1) dice que en el nivel 1 «vuelve a 6a y la mesa queda blanca». No es contradicción (el verde es nivel 3 y la ficha describe la app completa), pero al programar la S9 hay que recordar la versión del spec. La leyenda de `06c` ya lo dice | `spec+doc-pantallas.md` pantalla 6 (flujo, validaciones «Cobrar»); `spec-claude-code.md` 4.1 | Solo aviso |
| D19 | **`SeguridadRepository` vive en `datos/repositorios/` «porque usan Room»**, pero no usa Room: usa `PinStore`. La justificación de P237 no le cuadra, aunque la ubicación no molesta | `spec-claude-code.md` 3 (P237); `spec+doc-clases.md` 1 (P237) | Solo aviso |
| D20 | **Requisitos lista 11 incrementos** en «Cómo se lee» («1 etiquetas… 11 foto nutricional»), pero hay **12** (el agotado temporal, RF-53) en el spec y en `estado-nivel.md` | `spec+doc-requisitos.md`, «Cómo se lee», viñeta «Orden de los incrementos» | Solo aviso: hay que corregir el documento del Project |
| D21 | **`CLAUDE.md` empieza con una línea residual** de una instrucción de copia («# Ako` hacia abajo) al archivo `CLAUDE.md`…: la copia ya está hecha en `code\CLAUDE.md`») y menciona una carpeta `code\` que no existe en `C:\AKO`. La leo en cada sesión: conviene quitarla | `CLAUDE.md`, línea 1 | Solo aviso: no la toco sin que lo digas |
| D22 | **El ajuste `attribution` de Claude Code.** El spec (11, S1) manda crear `{"attribution": {"commit": "", "pr": ""}}` en tus ajustes de usuario. He comprobado en la documentación oficial (24 sep 2026) que el ajuste existe con esos campos; lo que no está documentado es si la cadena vacía basta para quitar la línea. Esta noche, además, Claude Code me ha indicado que la regla de tu `CLAUDE.md` tiene prioridad sobre su atribución. Hay que **comprobarlo en el primer commit** de la S1 | `spec-claude-code.md` 11 (S1) y 13; `CLAUDE.md` regla 9 | P23 |
| D23 | **El archivo `PRIMER-MENSAJE-para-Claude-Code.md` tiene siete partes (A–G, preguntas en G)**; el mensaje que me has enviado tiene ocho (chats y modelos en G, preguntas en H). El archivo de la carpeta se quedó atrás; este análisis sigue el mensaje enviado | `PRIMER-MENSAJE-para-Claude-Code.md`, punto 2 | Solo aviso |
| D24 | **La plantilla del diario manda apuntar las fuentes «al mismo tiempo» en `ref+doc-bibliografia.md`**, que vive en el Project de Claude, donde yo no llego. Hace falta decidir quién lo copia y cuándo (lo natural: tú, al subir la ficha al Project) | `docs/plantilla-diario.md` 1 (Las fuentes) | Solo aviso |

Dos cosas que **no** son problema pero conviene tener presentes: las reglas van de R1 a R16 sin R13 (son 15; no hay que buscar la R13), y la pantalla 4 no existe (el número queda libre).

---

## E. Propuesta: qué instalar, cómo trabajar, cómo usar la nube

### E.1 Lo que he encontrado en tu PC esta noche (solo leyendo; no he cambiado nada)

| Qué | Estado | Qué significa |
|---|---|---|
| Windows 11 Home 26200 · Ryzen 5 5600G (6 núcleos, 12 hilos) · **16 GB de RAM** · RTX 4060 · **287 GB libres** en C: | Bien | Sobra para Android Studio y el emulador |
| **Virtualización (SVM) en la BIOS: desactivada** («Se habilitó la virtualización en el firmware: No») | **Bloquea** | El emulador necesita aceleración por hardware; sin SVM no arranca o va inservible. Hay que activarlo en la BIOS (E.2, paso 1) |
| **Android Studio 2026.1.3 (Quail 3)** en `C:\Program Files\Android\Android Studio` | Instalado, **sin SDK** y **sin emulador** creado | Alguien abrió el instalador pero el asistente de primer arranque no descargó el SDK (no hay `AppData\Local\Android\Sdk`). Se completa en la S1. La versión estable actual es Quail 4 (1 sep 2026) |
| **Git 2.55** | Instalado | Vale |
| **GitHub CLI (`gh`)**, con sesión iniciada en la cuenta **`dhuaniel-dot`** | Instalado y conectado | Podemos crear el repositorio desde la consola sin pasar por la web |
| **Identidad global de Git: nombre `Yunkang46`, correo `(correo de la otra cuenta)`** | Es otra cuenta | Los commits saldrían con ese nombre y no con el tuyo. Hay que configurar tu nombre y tu correo (E.2, paso 5) |
| JDK 25 (Temurin) en Program Files | Instalado | **No hace falta**: Android Studio trae su propio Java (JBR 21). Ojo con que Gradle no lo coja por error |
| `claude` en consola | No está en el PATH | Da igual: usas la app de escritorio |
| Ajustes de usuario de Claude Code (`C:\Users\dhuan\.claude\settings.json`) | Existen, **sin `attribution`** | Se añade en la S1 (→ P23) |
| Plan de Claude: **Pro** (según la app esta noche) | | Importa para el crédito de la nube (E.4) y para los límites (G) |

### E.2 Qué instalar y en qué orden (→ P18–P23)

Cada paso te lo guiaré clic a clic el día que toque; aquí solo el orden y por qué.

1. **BIOS → SVM Mode = Enabled** (placa Gigabyte A520M H). Reiniciar, pulsar `Supr` al arrancar, buscar «SVM Mode» (suele estar en *Tweaker → Advanced CPU Settings*, o en *Settings → AMD CBS*), ponerlo en *Enabled*, `F10` para guardar. Comprobación: Administrador de tareas → Rendimiento → CPU → «Virtualización: Habilitado». **Solo tú puedes hacerlo** (yo no llego a la BIOS). No cambia nada más del PC.
2. **Android Studio**: abrirlo y dejar que el asistente descargue el SDK (unos 3–4 GB): plataforma Android 36 (o la que proponga), *Build-Tools*, *Platform-Tools* (trae `adb`), *Emulator* y, si lo ofrece, el *Android Emulator hypervisor driver* para AMD. Antes o después, actualizar a **Quail 4** desde *Help → Check for Updates* y **no volver a actualizar hasta la entrega** (→ P19).
3. **Emulador (AVD)**: *Device Manager → Create → Pixel 6 → imagen «Google APIs» API 34* (→ P20; sin Play Store, que estorba para el inspector de base de datos). Arrancarlo una vez.
4. **Repositorio**: en la S1, `git init` en `C:\AKO`, `.gitignore` de Android, y el repositorio público en GitHub con `gh repo create` (→ P22).
5. **Identidad de Git solo para este repositorio** (`git config user.name` / `user.email` **sin `--global`**), con tu nombre y el correo de tu cuenta `dhuaniel-dot` (→ P21). Así no toco la configuración de la otra persona que use el PC.
6. **`attribution` en tus ajustes de usuario de Claude Code** y reinicio de la app (→ P23).
7. **[Claude] Opcional, para que Gradle vaya más rápido:** excluir `C:\AKO` y `C:\Users\dhuan\.gradle` del análisis de Windows Defender (→ P27). Android Studio a veces lo propone él mismo.

No hace falta instalar Java, Gradle ni Kotlin aparte: los trae Android Studio.

### E.3 Cómo sería una sesión (→ P24, P25)

- **Dónde:** app de escritorio de Claude → pestaña Código → sesión **Local** con carpeta `C:\AKO`. Android Studio abierto al lado con el emulador. Un chat por sesión (G).
- **Reparto:** yo explico, escribo el código por piezas pequeñas, compilo por consola (`gradlew.bat`) y paso las pruebas de `test/`; **tú** ejecutas la app desde Android Studio (botón Run), miras el emulador, haces las pruebas manuales con el dedo, usas el inspector de base de datos y me dices qué ves. Así aprendes la herramienta que saldrá en el vídeo. Si algo se atasca, puedo instalar y arrancar la app yo por `adb` como respaldo.
- **Ritual de apertura (5 min):** leo la última ficha y `estado-nivel.md`; creo `docs/diario/sesion-NN.md` con fecha, objetivo y hora; compruebo `git status`.
- **Durante:** cada pieza = explicación en lenguaje llano → código → «¿lo has entendido?» → compila/prueba → siguiente. Cada error va a la tabla de problemas **en el momento**.
- **Ritual de cierre (15–20 min):** pruebas de la sesión con fecha en `spec+doc-pruebas.md`; ficha completa (tiempo real, qué entendí y qué no, uso de IA); `estado-nivel.md`; `git add -A && git commit -m "S<N>: <objetivo>"` y `git push`; identificador del commit en la ficha. La skill de F.1 hace esto sin que se olvide nada.
- **Plan Mode** (el modo en que solo leo y escribo un plan antes de tocar código) al empezar las sesiones grandes: S4, S6, S8, S9 (→ P25).
- **Una sesión puede durar dos o tres días**: se cierra el chat cada día con el estado escrito en la ficha («Qué se dejó a medias»), aunque el commit de cierre llegue al final.

### E.4 La nube de Claude Code y el crédito (→ P26)

Lo que he comprobado el 24 de septiembre de 2026:

- Las **sesiones en la nube** salieron de prueba hoy mismo. **Clonan un repositorio de GitHub** (no leen tu PC), pueden ejecutar comandos, Gradle y pruebas JUnit, y **no tienen emulador**. Se pueden crear desde la app de escritorio eligiendo *Cloud* en lugar de *Local*.
- **Crédito de lanzamiento:** una sola vez, **100 $ en Pro y 250 $ en Max**; se reclama con `/claim-credit` o desde el enlace de Anthropic **antes del 7 de octubre**, es **solo para sesiones en la nube** (no toca tus límites locales) y **caduca el 4 de noviembre**. Tu app dice que el plan es **Pro**; si tu cifra de 250 $ es la buena, sería Max (→ P26). Lo que cueste la nube después del crédito no está publicado: se mira antes de usarla sin crédito.
- **Para qué la usaría yo (antes del 4 de noviembre):** (a) **revisión independiente** del código de cada sesión desde la S4, en una sesión que no sabe cómo se escribió el código (encuentra lo que la sesión original ya no ve); (b) generar el **diagrama de clases definitivo** (PlantUML) desde el código real para la memoria; (c) borradores de documentación que salen del código (estructura de carpetas, fusión de fichas del diario); (d) pasar las seis pruebas de `test/` en la nube como comprobación extra. **Para qué no:** las sesiones de construcción (necesitan el emulador y a ti delante) ni la apropiación.
- **Requisito:** que exista el repositorio (S1) y que el trabajo esté subido con `push`.

---

## F. Skills y plugins

Lo que existe hoy y lo que compensa para este proyecto. Miré el catálogo oficial de plugins (`claude-plugins-official`, ya registrado en tu instalación), tus plugins activos y la documentación (24 sep 2026). **No hay ningún plugin oficial de Android, Kotlin o Gradle**; hay uno de servidor de lenguaje Kotlin y varios de la comunidad orientados a Compose y a librerías que el spec prohíbe.

| # | Qué | Qué haría | Qué nos ahorra | Qué cuesta | Recomendación |
|---|---|---|---|---|---|
| F.1 | **Crear la skill `sesion-ako`** (dos entradas: `/abrir-sesion` y `/cerrar-sesion`), guardada en `C:\AKO\.claude\skills\` y subida al repositorio | Abrir: leer última ficha y `estado-nivel.md`, crear `sesion-NN.md` desde la plantilla con fecha, número, objetivo y hora. Cerrar: comprobar pruebas, ficha completa, `estado-nivel.md`, columnas de resultado, commit `S<N>: …` sin atribución, push, hash en la ficha, y una lista de lo que falta si algo no cuadra | 15–20 min por sesión y, sobre todo, **que no se olvide ninguna pieza del ritual** (el diario es la prueba de integridad académica) | 30–45 min en la S1: la escribo yo, la lees tú | **Sí, en la S1** (→ P28) |
| F.2 | **Un hook pequeño** (`PreToolUse` sobre `git commit`) | Bloquea el commit si el mensaje lleva `Co-Authored-By` o si no existe la ficha `sesion-NN.md` de la sesión | Garantiza P226 y la ficha aunque se me olvide: un hook es código que se ejecuta, no una instrucción que puedo saltarme | 15 min en la S1; un script de PowerShell de diez líneas | **Sí, en la S1** (→ P29) |
| F.3 | **Skill `verificar`** (+ opcional un hook `Stop` que pase las pruebas de `test/` si cambió un `.kt`) | Tras un cambio de código: compilar, pasar las pruebas, leer el diff, comprobar que ninguna prueba se ha debilitado, informar con la evidencia | Evita el «parece que funciona» y da material para la columna *Resultado* | 30 min en la S3, cuando existan las primeras pruebas; el hook añade 1–2 min por parada, por eso opcional | **Sí, en la S3** (skill); hook, probar y quitar si molesta |
| F.4 | **Plugin oficial `explanatory-output-style`** y **`learning-output-style`** | Estilos de salida: *explanatory* añade explicaciones de por qué se hace cada cosa; *learning* te pide a ti escribir pequeños trozos con guía | *Explanatory* encaja con la regla 1 de `CLAUDE.md`; *learning* es exactamente la **pasada de apropiación** (tú tecleas, yo guío) | Instalar desde el catálogo (un comando); se activa y desactiva | **Explanatory en las sesiones (probarlo en S1); learning en la apropiación** (→ P30) |
| F.5 | **Built-in `/code-review`** (ya lo tienes) | Revisión del código cambiado buscando errores de corrección | Una segunda mirada antes de construir encima | Consume límite; ~10 min por uso | **Al cerrar S4, S9 y S13** (→ P35) |
| F.6 | **Plugin oficial `kotlin-lsp`** | Un servidor de lenguaje Kotlin: me da tipos y firmas reales, menos errores «a ciegas» | Menos ciclos de compilar-fallar-corregir | Instalar el binario `kotlin-lsp` de JetBrains (aún joven, en Windows puede fallar); configuración | **No de entrada.** Se prueba en la S3 solo si los errores de tipos se repiten (→ P31) |
| F.7 | Plugins oficiales `commit-commands`, `github`, `gitkraken` | Comandos de commit y acceso a GitHub | Nada que `gh` y F.1 no hagan ya | Ruido | **No** |
| F.8 | Skills de la comunidad para Android (p. ej. `android-skills`, «kotlin-development-assistant») | Recetas de arquitectura, Compose, Hilt, Retrofit… | Nada para este proyecto: el stack está fijado y ellas empujan a Compose y a librerías prohibidas | Riesgo de contradecir el spec | **No** |
| F.9 | Plugin `context7` (documentación actualizada de librerías por MCP) | Consultar la documentación de Room/Glide/Android al día | Ya lo cubro con búsqueda web cuando dudo | Un servidor MCP más | **No** |
| F.10 | Plugin de Claude Code para JetBrains (funciona en Android Studio) | Claude Code dentro de Android Studio, con el diff y los errores del IDE a la vista | Menos cambiar de ventana | Otra integración que aprender, cuando ya tienes la app de escritorio | **No por ahora**; se puede probar más adelante (→ P36) |
| F.11 | **Tus skills ya existentes** (`preguntas-numeradas`, `elegir-modelo`, `guardar-en-carpeta-proyecto`, `cierre-de-fase`, `hacer-doc`, `wireframes-movil`) | Son de tu app de Claude y las veo también aquí | `preguntas-numeradas` ya rige el formato de H; `guardar-en-carpeta-proyecto` dice dónde va cada archivo; `wireframes-movil` servirá si un incremento obliga a redibujar | Nada | Se usan como están |

---

## G. Chats y modelos

### G.1 Cuándo cambiar de chat

- **Regla base: un chat por sesión de código.** La sesión es la unidad que ya tiene principio (ficha) y fin (commit); el chat nuevo empieza leyendo esa ficha. Si una sesión ocupa dos o tres días, **se cierra el chat cada día** siempre que lo hecho esté escrito en la ficha («Qué se dejó a medias y en qué estado exacto»); si no está escrito, se escribe antes de cerrar.
- **Señales de que toca cerrar aunque la sesión no haya terminado:** (1) la app enseña el contexto por encima del **60 %** (esta noche estamos en el 27 % de un contexto de 1 millón, con la compactación automática al 97 %); (2) empiezo a repetir cosas o a olvidar decisiones de hace un rato; (3) aparece el aviso de compactación; (4) llevamos más de dos o tres horas de conversación; (5) hay que cambiar de modelo (el cambio solo tiene sentido al abrir chat). **Un chat largo consume más que un modelo caro**: cada mensaje reenvía toda la conversación.
- **No usar `/compact` en las sesiones de código** (resume la conversación y pierde detalle que luego hay que reexplicar). Mejor cerrar bien y abrir nuevo.

### G.2 Cómo se pasa el contexto de un chat al siguiente

Lo que se queda escrito fuera del chat, siempre, es lo que el spec ya manda: **la ficha del diario** (sobre todo «Qué se dejó a medias», «No entendí todavía» y «Siguiente sesión»), **`estado-nivel.md`**, **el commit** y **`CLAUDE.md`** (que leo solo). Con eso el chat siguiente no pierde nada. Añado dos cosas [Claude]:

1. **Un archivo `docs/diario/EN-CURSO.md`** (no se sube al Project; sí al repositorio) para cuando un chat se cierra **a mitad de sesión**: qué pieza estaba a medias, qué compila y qué no, qué archivo estaba abierto. Se vacía al cerrar la sesión. Lo mantiene la skill de F.1.
2. **El primer mensaje de cada chat nuevo**, siempre igual (te lo daré para copiar):
   > «Sesión S<N> de Ako. Lee `CLAUDE.md`, la última ficha de `docs/diario/`, `docs/estado-nivel.md` y, si existe, `docs/diario/EN-CURSO.md`. Después el apartado de la S<N> en `docs/spec-claude-code.md` y la ficha y wireframes de la pantalla que toque. Abre la sesión con `/abrir-sesion` y dime en tres líneas por dónde empezamos.»

### G.3 Qué modelo y qué esfuerzo en cada tipo de trabajo

Modelos disponibles hoy en tu plan (comprobado en la ayuda oficial de Claude Code el 24 sep 2026): **Fable 5.1** (el más capaz, es con el que has mandado este mensaje; tiene **su propio límite semanal aparte**, y esta noche estaba al 53 %), **Opus 5.5**, **Sonnet 5** y **Haiku 4.5**. La ayuda oficial dice que Opus «consume bastante más límite» que Sonnet y recomienda ajustar el modelo a la tarea. Los límites del plan Pro son una ventana de 5 horas y otra semanal, compartidas entre la app de Claude y Claude Code. Sobre Fable no hay cifras publicadas; por lo que se ve en tu cuenta, es el que antes se agota.

| Fase o bloque | Modelo | Esfuerzo | Por qué | Chat nuevo |
|---|---|---|---|---|
| Este análisis y la discusión de mañana | **Fable 5.1** | Alto | Es un plan que se arrastra dos meses: un error aquí sale caro | Este mismo chat mañana; nuevo al empezar la S1 |
| S1 (proyecto, herramientas, Git) | **Opus 5.5** | Medio | Mucha explicación de herramientas y errores de entorno; Sonnet se queda corto guiando a un principiante | Sí |
| S2–S4 (entidades, DAOs, dominio, repositorios) | **Opus 5.5** | **Alto en S4** (reglas), medio en S2–S3 | Es la base; un error en el modelo de datos se arrastra hasta la memoria | Uno por sesión |
| Revisión externa de S2–S4 | **Fable 5.1** (o `/code-review` con Opus si Fable va justo) | Alto | Auditoría: es donde el modelo fuerte rinde | Sí, chat aparte (o sesión en la nube) |
| S5, S7, S10, S12 (pantallas medianas) | **Opus 5.5** | Medio | Código guiado sobre algo ya decidido, pero con explicación continua | Uno por sesión |
| S6, S8, S9 (pantallas difíciles) | **Opus 5.5** | Alto | Listas anidadas, carrito compartido, estados de comanda: donde un principiante se atasca | Uno por sesión; Plan Mode al abrir |
| Bloques mecánicos dentro de una sesión: `strings.xml` EN, rellenar columnas de pruebas, `estado-nivel.md`, dar formato a la ficha | **Sonnet 5** | Bajo | Copiar y ordenar; no hay decisión | Solo si el bloque es largo (p. ej. el EN de S13, chat propio) |
| Corregir un error que se resiste (el mismo fallo dos veces) | Subir un escalón: Opus alto; si sigue, **Fable** en chat nuevo con el error y el diff pegados | Alto | Ante la duda, empezar barato y subir si falla dos veces | Sí: un chat limpio con solo el error ve más que el chat cargado |
| S11 (pruebas Room) y S13 (cierre) | **Opus 5.5** | Medio | Trabajo conocido, mucha comprobación | Uno por sesión |
| Revisión final del nivel 1 (antes de la Release) | **Fable 5.1** | Alto | Es lo que verá el corrector | Sí |
| Memoria (fase 7): estructura y revisión final | **Fable 5.1** en la app de Claude (`hacer-doc`) | Alto / máximo en la revisión final | Entregable que ve otra persona | Según tu skill `cierre-de-fase` |
| Memoria: artefactos desde el código (diagrama, tablas) | **Opus 5.5** (o sesión en la nube) | Medio | Generar a partir de algo decidido | Sí |
| Pasada de apropiación | **Opus 5.5** con `learning-output-style` | Medio | Tú tecleas; el modelo guía y comprueba | Un chat por parte reescrita |
| Vídeo: guion y ensayo de explicaciones | **Sonnet 5** para el guion; **Opus** para ensayar preguntas del corrector | Medio | Redactar a partir de lo hecho es mecánico; el ensayo es donde conviene rigor | Sí |

Reglas: **el modelo se cambia al abrir un chat, nunca a mitad**; un bloque, un modelo; **fast mode no** (más consumo por lo mismo). Si el límite semanal va justo, se baja Sonnet en los bloques mecánicos antes que recortar los de decisión. Cada vez que empiece una sesión te diré el modelo y el esfuerzo en una línea.

---

## H. Preguntas

Contesta con la letra o con tus palabras, en el orden que quieras y en varios mensajes si hace falta (`P3 B` vale). La opción con **[Claude]** es la que recomiendo, con el motivo. La numeración P1–P36 es **local a este archivo** (→ P34).

### Plan y calendario

**P1 · horas por semana** · ¿Cuántas horas reales por semana puedes dedicar de aquí a la entrega?
- A) Menos de 10
- B) Entre 10 y 15
- C) 18 o más **[Claude]** es lo que exige llegar al 23 de noviembre con el nivel 1 entero; con B la fecha realista es el 6 de diciembre

**P2 · fecha objetivo** · ¿Con qué fecha planificamos?
- A) Semana del 23 de noviembre, con el 6 de diciembre como colchón que no se cuenta **[Claude]** planificar contra la fecha temprana deja margen para lo que siempre pasa
- B) Directamente el 6 de diciembre

**P3 · puerta del nivel 2** · ¿Cuándo entra un incremento del nivel 2?
- A) Solo si el nivel 1 está cerrado el 1 de noviembre y la memoria avanzada; el único candidato es el incremento 1 (etiquetas y chips) **[Claude]** aprobar seguro antes que nota
- B) Ninguno, pase lo que pase: todo declarado *diseñado, no implementado*
- C) Al menos el incremento 1 aunque cueste memoria

**P4 · dónde se escribe la memoria** · ¿Cuánto hay escrito ya y dónde se escribe la fase 7?
- A) En la app de Claude con `hacer-doc`; Claude Code solo genera lo que sale del código (diagrama, tablas, diario fundido) **[Claude]** es el sitio donde están tus skills y el Project
- B) En Claude Code
- C) Ya hay capítulos escritos de las fases 1–5 (dime cuáles): eso acorta la fase 7

**P5 · preparación del PC** · ¿Cómo se registra la preparación (BIOS, SDK, emulador, Git)?
- A) Dentro de la ficha de la S1, que puede ocupar dos días **[Claude]** el spec numera 13 sesiones de código; una ficha 00 sin código descuadra la numeración
- B) Ficha aparte `sesion-00.md`

### Diseño y huecos (de D)

**P6 · pruebas con prerrequisitos** · Para P-M-13, P-M-29, P-M-04, P-M-06 y P-M-10 (D3), ¿qué se hace en la sesión que las «cierra»?
- A) Se ejecuta la parte posible, se anota «parcial» en Observaciones con la fecha, y se repite entera cuando exista el prerrequisito; en la S13 pasan todas de una vez **[Claude]** es lo honesto y no cuesta nada
- B) Crear a mano comandas y fotos en el inspector para pasarlas enteras en su sesión
- C) Mover esas pruebas a la S13

**P7 · datos de ejemplo** · ¿Qué precarga como «1 categoría y 1 plato de ejemplo» (D1)?
- A) Categoría **Ejemplo** con el plato **1 · Plato de ejemplo · 1,00 €**, sin descripción ni alérgenos **[Claude]** deja claro que es un marcador y no confunde con el juego de datos
- B) Algo realista (p. ej. **Bebidas** con **1 · Agua · 1,50 €**)
- C) Otros nombres, los dices tú

**P8 · categoría de ejemplo en las pruebas** · ¿Cómo encaja la categoría de ejemplo en el juego de datos (D2)?
- A) Se elimina (interruptor apagado) al montar el juego de datos y se añade esa línea a la entrada de P-M-04 y P-M-17 **[Claude]** no cambia ningún resultado esperado
- B) Se deja y se ajustan los resultados esperados de P-M-04 y P-M-17

**P9 · reactivar o recuperar** · Literal del segundo aviso de la cadena 3e (D4):
- A) «¿Quieres recuperar Carnes? Volverán a la carta sus 8 platos» y botón *Recuperar*; se anota la corrección del spec y del wireframe **[Claude]** es el vocabulario obligatorio de `CLAUDE.md`
- B) «¿Quieres reactivar…?» como el spec y el wireframe

**P10 · wireframe 1e** · El fondo de `dialogo-1e-cambiar-pin.png` con vocabulario viejo (D7):
- A) Se deja; se programa el diálogo (que está bien) y la memoria lo anota en una línea **[Claude]** el diálogo es lo que importa; redibujar cuesta y no cambia el código
- B) Redibujarlo (con `wireframes-movil`) antes de la S5

**P11 · precio negativo** · ¿Excepción o valor de `ResultadoGuardado` (D5)?
- A) Excepción `IllegalArgumentException` desde `Validacion`, como el spec 5.1 y P-C-09; `ResultadoGuardado` con sus cuatro valores **[Claude]** la interfaz no deja teclear el signo, así que en la app nunca se ve; la excepción protege el repositorio
- B) Valor `PrecioNegativo` en `ResultadoGuardado`

**P12 · alcance de P-C-05** · (D17)
- A) Solo `Hash` (`pbkdf2` y `coincide`) **[Claude]** `PinStore` ya lo cubren P-M-01 y P-M-02
- B) También `PinStore` con un `SharedPreferences` falso

**P13 · foto de categoría** · (D6)
- A) Entra en la S12 con la del plato; en la S6 el botón *Elegir* de 2b existe pero está apagado hasta la S12 **[Claude]** RF-05 la pide y, una vez existe `ImageStore`, cuesta media hora
- B) Se queda fuera del nivel 1 y se declara

**P14 · resaltado al volver al Panel** · (D15)
- A) Nivel 3: no se hace en la S7 **[Claude]** es lo que dice el spec, que manda sobre las fichas
- B) Hacerlo en la S7 (es barato)

**P15 · puerta de Pedir** · Cómo se enseña «La carta está vacía» / «Todas las categorías están eliminadas» (D8):
- A) `ConfirmacionDialog` con el botón negativo opcional (un solo botón *Aceptar*) **[Claude]** un solo componente, como quiere P121
- B) Un snackbar

**P16 · técnica de la precarga** · (D9)
- A) `Precarga.onCreate` lanza una corrutina que obtiene la base de datos y usa los DAOs (`insertarTodas`, `insertarAlergenos`…) **[Claude]** cumple «solo los DAOs saben SQL» y se prueba en P-C-08
- B) `execSQL` directo en el callback

**P17 · dependencias base y constructores** · (D13, D10)
- A) Confirmar que `appcompat`, `material`, `constraintlayout`, `activity-ktx`, `fragment-ktx`, `lifecycle-viewmodel-ktx`, `lifecycle-livedata-ktx` y `kotlinx-coroutines-android` cuentan como «del spec», y que los repositorios reciben sus DAOs por constructor **[Claude]** sin ellas no hay ViewModel, LiveData ni Material 3; sin constructor no hay P-C-09
- B) Las revisamos una a una en la S1

### Tu PC e instalación

**P18 · virtualización en la BIOS** · (E.1)
- A) La activas tú antes de la S1 con mis pasos (reinicio, `Supr`, SVM Mode → Enabled, `F10`) **[Claude]** es un minuto y sin ella no hay emulador
- B) Prefieres que alguien te ayude; lo dejamos para ese día
- C) Buscar otra salida (no la hay: no tienes móvil físico)

**P19 · versión de Android Studio** ·
- A) Actualizar a Quail 4 antes de crear el proyecto y no volver a actualizar hasta la entrega **[Claude]** empezar en la estable actual evita arrastrar avisos; congelar evita sorpresas a mitad
- B) Quedarse en 2026.1.3

**P20 · imagen del emulador** · (D12)
- A) Pixel 6, API 34, «Google APIs» (sin Play) **[Claude]** es lo que dice el spec y la imagen más ligera con inspector de base de datos
- B) Pixel 6, API 36 (la del `targetSdk`)

**P21 · identidad de Git** · Los commits saldrían como `Yunkang46` (E.1):
- A) Configurar tu nombre y correo **solo en el repositorio `C:\AKO`** (sin `--global`), con la cuenta `dhuaniel-dot` **[Claude]** no toca la configuración de nadie más que use el PC
- B) Cambiar la configuración global a la tuya
- C) Dejarla como está

**P22 · nombre del repositorio y de la carpeta del proyecto** ·
- A) Repositorio `ako` (público), proyecto de Android Studio en `C:\AKO\app-ako\` **[Claude]** corto, y la subcarpeta deja `docs/` y `CLAUDE.md` limpios en la raíz
- B) Repositorio `ako-android`, proyecto directamente en la raíz `C:\AKO\`
- C) Otro nombre

**P23 · atribución de los commits** · (D22)
- A) Crear el ajuste `attribution` en tus ajustes de usuario como dice el spec y comprobar el primer commit; si sale la línea, buscar el ajuste correcto ese día **[Claude]** cinturón y tirantes; se verifica con hechos
- B) Fiarse solo de la regla 9 de `CLAUDE.md`

### Cómo trabajar

**P24 · quién ejecuta la app** ·
- A) Tú desde Android Studio (Run) y las pruebas manuales; yo compilo y paso las pruebas de código por consola; `adb` solo de respaldo **[Claude]** aprendes la herramienta que saldrá en el vídeo
- B) Yo todo por consola y `adb`; tú solo miras el emulador

**P25 · Plan Mode por sesión** ·
- A) Al abrir S4, S6, S8 y S9 **[Claude]** son las que tienen varias piezas encadenadas; en las demás sobra
- B) En todas las sesiones
- C) En ninguna

**P26 · nube y crédito** · (E.4)
- A) Reclamar el crédito antes del 7 de octubre y usar la nube solo para revisiones y documentación desde la S4 hasta el 4 de noviembre **[Claude]** es gratis hasta esa fecha y no toca tus límites locales. Dime si tu plan es Pro (100 $) o Max (250 $)
- B) No usar la nube
- C) Usarla también para ejecutar sesiones sin emulador (S3, S4, S11 no)

**P27 · exclusiones de Windows Defender** · (E.2, paso 7)
- A) Sí, `C:\AKO` y `C:\Users\dhuan\.gradle` **[Claude]** Gradle compila bastante más rápido; es reversible
- B) No

### Skills y plugins

**P28 · skill de sesión** · (F.1)
- A) Crearla en la S1 **[Claude]** el ritual empieza en la S1 y es lo que más se olvida
- B) Más adelante, cuando el ritual ya lo tengamos hecho a mano dos veces
- C) No

**P29 · hook del commit** · (F.2)
- A) Sí, en la S1 **[Claude]** garantiza P226 y la ficha sin depender de que yo me acuerde
- B) Sin hooks

**P30 · estilos de salida** · (F.4)
- A) *Explanatory* en las sesiones y *learning* en la apropiación **[Claude]** casan con la regla 1 de `CLAUDE.md` y con P230
- B) Estilo normal siempre
- C) Solo *learning* en la apropiación

**P31 · servidor de lenguaje Kotlin** · (F.6)
- A) No de entrada; se prueba en la S3 si se repiten errores de tipos **[Claude]** herramienta joven en Windows; primero ver si hace falta
- B) Instalarlo en la S1

### Chats y modelos

**P32 · modelos** · (G.3)
- A) Opus 5.5 para las sesiones, Sonnet 5 para lo mecánico, Fable 5.1 solo en hitos (plan, revisión de S2–S4, revisión final, memoria) **[Claude]** Fable tiene su propio límite y es el que antes se agota; Opus guía bien a un principiante
- B) Fable siempre
- C) Sonnet siempre

**P33 · un chat por sesión** · (G.1)
- A) Sí, y cerrar cada día con el estado escrito en la ficha o en `EN-CURSO.md` **[Claude]** el chat largo consume y olvida
- B) Un chat por semana

**P34 · numeración de estas preguntas** ·
- A) P1–P36 se quedan como numeración local de este archivo; si alguna pasa al registro del Project, se renumera allí desde la P249 **[Claude]** tu registro va por la P248 y no conviene mezclar
- B) Renumerarlas ya desde la P249

**P35 · revisión externa del código** · (F.5)
- A) `/code-review` (o una sesión en la nube) al cerrar S4, S9 y S13 **[Claude]** tres momentos: la base, el final del guion del vídeo y la entrega
- B) Nunca

**P36 · Claude Code dentro de Android Studio** · (F.10)
- A) Seguir con la app de escritorio **[Claude]** una herramienta nueva a la vez
- B) Probar el plugin de JetBrains en la S2

---

*Fin del análisis. Siguiente paso, mañana y en este orden: (1) lees esto; (2) discutimos A hasta que lo cuentes con tus palabras; (3) contestas H y me preguntas lo que haga falta; (4) decidimos E, F y G. La S1 empieza después.*
