> **Instrucciones para Claude Code: el diseño completo y el orden de construcción del prototipo. Va al repositorio como `docs/spec-claude-code.md` (P134 → A); las reglas de trabajo van aparte en `CLAUDE.md` (`spec-claude-md.md`). DERIVADO de `spec+doc-diseno-app.md`, `spec+doc-pantallas.md`, `spec+doc-clases.md`, `spec+doc-pruebas.md` y `spec+doc-wireframes.md`: se regenera, nunca se edita a mano. Manda junto con el spec principal; si discrepan, es un error y se pregunta.**
> **Versión de la fase 5b — 24 de septiembre de 2026.** Es la definitiva del 17 de septiembre (fase 2d, bloque 10, P133-P137) con las filas del registro `proceso-pendiente-cierre.md` que le tocaban ya aplicadas (3, 5, 7, 34, 35, 36, 38, 39, 43, 48, 51 y 53) y una pasada entera de vocabulario: **eliminar ≠ desactivar** y `producto.disponible` con el incremento 12 (P153), **Resumen de ingresos** (P149), **categoría sin foto con «?»** (P224), **53 RF**, clases `ResumenIngresos…` (P238), repositorios en `datos/repositorios/` y `Hash` en `dominio/` (P237), commits sin atribución (P226), APK en una Release de GitHub (P225, P248), pasada de apropiación (P230, P233), revisión del prototipo tras la S13 (P227, P228) y `§` → *apartado* (P145).
> Decisiones del bloque 10: dos archivos (P134); orden de construcción por capas y luego pantallas en el orden del vídeo (P135, delegada a Claude); solo `main`, commit y push por sesión (P136); `docs/estado-nivel.md` desde el primer día (P137). Lo marcado **[Claude]** lo decidió Claude por la regla de Daniel *"lo que no vaya al documento ni a la presentación se hace de la mejor forma para Claude Code"* (P112) y se puede cambiar.

# Ako — spec para Claude Code

## 1. Qué es esto y cómo se usa

App Android nativa de pedidos para bares y restaurantes, en **un móvil del restaurante, en vertical**, que se deja en la mesa (kiosco) o lleva el camarero. Rasgo diferencial: **una carta digital pensada para que pueda pedir cualquiera** (fotos, alérgenos, filtros, idiomas, Agrandar); todo eso vive en el rol **Pedir**, que no se negocia. Nombre: **Ako** (P97, confirmado en la 5b, P183); paquete `yunkang.ako` (P138; cambiado desde `es.daniel.ako` por Daniel en la S1, 28 sep 2026, CC-P104).

**Arquitectura diseñada:** cliente-servidor en red local. **Lo que se programa:** prototipo **monodispositivo**, Room local, tres roles, sin cocina (recorte de alcance declarado).

**Lo que va al repositorio en `docs/`** [Claude; ampliado en la 5b, P246 delegada], para que Claude Code no trabaje de memoria: este documento · `spec+doc-pantallas.md` (fichas) · `wireframes/` (los 21 PNG y `spec+doc-wireframes.md`, que lleva las 21 leyendas y enlaza cada PNG) · `spec+doc-pruebas.md` (plan de pruebas, con columnas de resultado por rellenar) · **`spec+doc-requisitos.md`** (los 53 RF y 25 RNF: de ahí sale `estado-nivel.md`) · **`spec+doc-clases.md`** (borrador del diagrama de clases: los nombres de clases y métodos que usan este spec y el plan de pruebas) · `plantilla-diario.md` (`proceso-plantilla-diario-desarrollo.md`, apartados 1 y 2) · `diario/` (una ficha por sesión) · `estado-nivel.md` (tabla diseñado/implementado, generada en la 5b con las 53 filas en *diseñado*, P247). En la raíz del repositorio, `CLAUDE.md`. El spec principal (`spec+doc-diseno-app.md`) y el E-R **no** van (P134): lo que Claude Code necesita de ellos está aquí, y una sola fuente evita contradicciones.

## 2. Stack y configuración del proyecto

| Qué | Valor | Quién |
|---|---|---|
| Lenguaje | **Kotlin** | Daniel (RNF-20) |
| Interfaz | **XML** con `ConstraintLayout`, Activities y Fragments; **ViewBinding**. Nada de Compose | Daniel; ViewBinding [Claude] |
| Persistencia | **Room** (`androidx.room`, 2.8.x, la estable en el momento de crear el proyecto), sobre SQLite | Daniel |
| Arquitectura | **MVVM sencillo**: Activity/Fragment → ViewModel → Repositorio → DAO → Room. Corrutinas con `viewModelScope`; el estado hacia la pantalla con `LiveData` | Daniel; corrutinas y LiveData [Claude] |
| Imágenes | **Glide** (versión del README del repositorio oficial, línea 5.0.x; anotaciones con **KSP**). Archivos en el almacenamiento privado; en la base de datos solo la ruta | Daniel; versión y KSP [Claude] |
| Android mínimo | **API 26 (Android 8.0)**, definitivo (verificación 2: `PBKDF2withHmacSHA256` existe desde API 26; Room 2.8 exige 23; Glide 14). `targetSdk`: el último estable que ofrezca Android Studio | Daniel (P63, P126) |
| Build | Gradle con **Kotlin DSL** y **catálogo de versiones** (`libs.versions.toml`); **KSP** para Room y Glide, nunca kapt | [Claude] |
| Orientación | **Vertical fija** (`android:screenOrientation="portrait"` en todas las Activities); scroll vertical | Daniel (RNF-15) |
| Idioma de la interfaz | `strings.xml` en **ES** (por defecto) y **EN**; todo texto visible sale de `strings.xml`, nunca escrito en el código | Daniel (RNF-19) |
| Dispositivo | **Emulador** de móvil (p. ej. Pixel 6, API 34). No hay dispositivo físico | Daniel |
| Sin conexión | La app no declara el permiso `INTERNET`. Nada usa red | Daniel (RNF-22) |
| Dependencias de pruebas | `junit:junit` en `test/`; `androidx.room:room-testing`, `androidx.test.ext:junit`, `androidx.test:runner` en `androidTest/` (`testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"`) | Daniel (P117) |

**Sin librerías que no estén en esta tabla.** Si una tarea parece pedir otra, se pregunta primero.

## 3. Estructura del código — paquetes y capas

Sale del borrador del diagrama de clases (bloque 7, `spec+doc-clases.md`): 58 clases en cinco capas. **Los nombres son borrador**: si en la fase 6 uno cambia, se cambia también en `spec+doc-pruebas.md`, y el diagrama definitivo de la fase 7 se genera desde el código real. Lo que no cambia es la estructura: **cada capa habla solo con la de abajo**. Las pantallas no ven los repositorios; los ViewModels no ven los DAOs.

```
yunkang.ako/                    [Claude] el paquete raíz; cambiar por el que Daniel prefiera
├── datos/
│   ├── entidades/     14 @Entity + 2 enum (EstadoComanda, TipoModificador) + Converters
│   ├── dao/           CategoriaDao · ProductoDao · PrecargadosDao · MesaDao · ComandaDao
│   ├── repositorios/  CartaRepository · ComandaRepository · SeguridadRepository  (usan Room: por eso viven en datos/, P237)
│   ├── AppDatabase    @Database con las 14 entidades y los 5 DAOs (única instancia)
│   └── Precarga       RoomDatabase.Callback: Otros, 60 mesas, 14 alérgenos, 3 etiquetas, 1 categoría + 1 plato de ejemplo
├── dominio/           SOLO lo puro: nada de aquí toca Android, Room, SharedPreferences ni archivos (P237)
│   ├── Carrito, LineaCarrito, Calculadora (importe, total, cambio: funciones puras)
│   ├── Validacion     precio ≥ 0, cantidad 1-99, PIN de 4 cifras (funciones puras, R4 y R8)
│   ├── Hash           PBKDF2 puro (javax.crypto), separado de PinStore para poder probarlo sin emulador (P-C-05)
│   └── modelos/       MesaEstado · ResumenIngresos · ComandaConTotal · CategoriaConPlatos · ResultadoGuardado
├── seguridad/
│   └── PinStore       SharedPreferences privadas: sal + hash; nunca el PIN. Llama a dominio/Hash
├── imagenes/
│   └── ImageStore     guardar (redimensionar ~1080 px + JPEG + archivo privado → ruta), borrar
└── ui/
    ├── selector/      SelectorActivity · SelectorFragment (1a) · CrearPinFragment (1b) · PinDialog (1c) · SelectorViewModel
    ├── panel/         PanelActivity (2a) · CategoriaBottomSheet (2b) · CambiarPinDialog (1e) · MesasAfectadasDialog (2e/3d) · PanelViewModel
    ├── plato/         PlatoActivity (3a) · PlatoViewModel
    ├── resumen/       ResumenIngresosActivity (2g) · ResumenIngresosViewModel
    ├── pedido/        PedidoActivity · CartaFragment (5a) · FichaPlatoFragment (5b) · CarritoFragment (5c) · PedidoViewModel
    ├── cuenta/        CuentaActivity · ComandaFragment (6b) · CuentaViewModel
    └── comun/         RejillaMesasFragment (1d elegir · 6a gestionar) · ReciboFragment (6c · 2g solo lectura) · ConfirmacionDialog
                       · adaptadores: FilaPlatoAdapter (2a, 5a) · CategoriaAdapter (2a cajas · 5a fila) · MesaAdapter (1d, 6a) · LineaAdapter (5c, 6b, 6c, 2g)
```

**Responsabilidad de cada capa, en una línea:**

| Capa | Hace | No hace |
|---|---|---|
| Entidades | Describen las 14 tablas con tipos Kotlin | Lógica |
| DAOs | La única capa que sabe SQL: consultas parametrizadas de Room (`@Query`, `@Insert`…), nunca SQL concatenado (RNF-08) | Reglas de negocio |
| Repositorios | **Garantizan las reglas R1-R16** de su parte y devuelven modelos listos para la pantalla | Saber qué pantalla los llama |
| Dominio puro (`Carrito`, `Calculadora`, `Validacion`, `Hash`) | Cálculos sin Android: es lo que prueban las P-C en `test/` | Tocar Room, SharedPreferences ni archivos |
| ViewModels | Estado de una pantalla; llaman al repositorio en corrutinas; **el carrito vive en `PedidoViewModel`** | Tocar la base de datos directamente |
| Activities y Fragments | Pintar y recoger toques; `ViewBinding`; una Activity por pantalla de las fichas, sus vistas como Fragments | Lógica ni cálculos |

**Un ViewModel por Activity** (P108); los Fragments de una Activity comparten su ViewModel (`activityViewModels()`).

**Por qué los repositorios están en `datos/` y `Hash` en `dominio/` (P237 → B, delegada a Claude [Claude]):** así es verdad, y no solo una intención, que **todo lo de `dominio/` se prueba en el PC sin emulador** (las seis P-C de `test/`): los repositorios usan Room, luego no son dominio puro; `Hash` no usa nada de Android, luego sí lo es. `PinStore` (que sí usa `SharedPreferences`) se queda solo en `seguridad/`. `spec+doc-clases.md` lo describe así también.

## 4. Contrato del prototipo — niveles

**El nivel 1 es el prototipo y es lo único obligatorio.** Se termina entero, se prueba (29 pruebas manuales + 9 de código) y se documenta antes de tocar el nivel 2. Después se completa la memoria. Solo entonces, si queda tiempo, entran **incrementos del nivel 2 en este orden**, cada uno terminado y probado antes del siguiente. Lo que no entre se declara en `docs/estado-nivel.md` y en la memoria como *diseñado, no implementado*. **Las 14 tablas se crean desde el primer día** aunque el nivel 1 no use todas.

**El nivel 1 es el guion del vídeo:** un propietario nuevo crea el PIN → crea una categoría → crea un plato → entra en Pedir y pide → entra en Cuenta, ve lo pedido y el total → quita una línea → "ha pagado con esto" y sale el cambio → cobra → mira el Resumen de ingresos.

### 4.1 Nivel 1 — obligatorio

| Pantalla | Qué entra |
|---|---|
| **1 Selector** | Tres botones grandes apilados con el nombre *Ako* encima (1a) · crear PIN en el primer arranque (1b, con el aviso *"si lo olvidas, no se puede recuperar"*) · introducir PIN (1c, cuatro cifras ocultas, teclado numérico, intentos ilimitados, **sacudida** del campo al fallar) · cambiar PIN (1e) · elegir mesa al entrar en Pedir (1d, rejilla en modo *elegir*: todas elegibles, se ven colores y totales) · **salir de Pedir pide el PIN**, también con el Atrás del sistema · **aviso previo si hay platos en el carrito sin enviar** (RF-38, con `ConfirmacionDialog`) |
| **2 Panel** | Cajas por categoría **de altura fija ~270 dp** (4 platos visibles) con scroll propio, `[+ Plato]` fijo abajo en cada caja; `[+]` categoría arriba; lápiz de categoría; tocar plato = editar · crear y editar categoría en hoja inferior (2b: nombre, foto; al editar, el interruptor *En la carta*: apagarlo **elimina** la categoría) · **aviso agrupado de mesas afectadas al eliminar una categoría (R6, diálogo 2e)** · fila de plato: **miniatura (o "?") · número · nombre · precio · la palabra *Eliminado*** · categoría eliminada marcada *Categoría eliminada*, con sus platos atenuados (se ve siempre: el Panel es el único sitio desde donde se recupera) · *Otros* siempre la última, sin flechas ni interruptor (R16) · `[Resumen de ingresos]` · `[Cambiar PIN]` · `[Terminar]` (cierra el Panel; no hay Guardar global) · flecha `←` con la palabra *Atrás* |
| **2g Resumen de ingresos** | Selector de fecha estándar de Android (por defecto hoy) · lista de comandas **PAGADAS** ese día con **mesa, hora e importe** · abajo, **cuántas comandas y el total** · tocar una abre su **recibo en solo lectura** (`ReciboFragment` sin Cobrar). Nunca "las mesas en verde" |
| **3 Plato** | Formulario con scroll único: nombre, número (entero), precio (en euros, se guarda en céntimos; 0,00 se admite), categoría (viene elegida desde el `[+ Plato]` de la caja), descripción, interruptor *En la carta* (apagarlo **elimina** el plato); **Guardar no se activa** sin los cuatro obligatorios · marcar alérgenos entre los 14 (no se crean) · aviso *"Si alguien tiene apuntado el N…"* al cambiar el número · aviso de número repetido (R9) · aviso *"Se perderán los cambios"* al salir sin guardar · aviso de mesas afectadas al eliminar (R6, 3d) · **cadena de dos avisos al guardar en una categoría eliminada (3e)** · **foto: elegir de la galería, redimensionar, comprimir, Glide — la última pieza del nivel 1** |
| **5 Pedir** | Barra superior con **"Mesa N"** y *Salir* · **fila de categorías fija bajo la barra, con scroll horizontal** (foto redonda pequeña + nombre; **sin foto, un círculo con «?»** en el hueco de la foto, P224; la activa resaltada; *Otros* la última; solo categorías con algún plato visible) · **una sola lista por secciones**; tocar una categoría salta a su sección · puerta de Pedir con sus dos mensajes (*"La carta está vacía"* / *"Todas las categorías están eliminadas"*, mirando platos **visibles**) · ficha 5b (foto grande o "?", número, nombre entero, precio, descripción, **alérgenos desplegables** con el aviso *"El restaurante no ha indicado alérgenos para este plato. Pregunta al personal."* si no hay, cantidad 1-99 con −/+ bloqueados en los límites, botón *"Añadir — precio"*) · **carrito como pastilla abajo a la derecha** (número de platos y total; sin animación) · carrito 5c (líneas, +/−, Quitar, total) · **Enviar** con confirmación *"Mesa N · total"* (R2, R4) |
| **6 Cuenta** | Sin PIN · rejilla de 60 mesas en modo *gestionar* (blanco / **rojo con el total**) · mesa blanca → snackbar *"La mesa N no tiene comanda"* y nada más · 6b: líneas (cantidad × nombre, importe) y **TOTAL** calculado; **Quitar línea** (sin aviso, salvo la última: aviso R7, que anula la comanda) · **Anular** con confirmación · **Dar la cuenta** → 6c recibo (nombres y precios congelados) · **calculadora de cambio** (campo *Entregado* opcional → cambio, en negativo si falta; no guarda nada) · **Cobrar** con confirmación → PAGADA con `fechaCierre`, vuelve a 6a y la mesa queda blanca (el verde es nivel 3) · botón `[Imprimir]` **no** entra (incremento 9) |
| Transversal | 14 tablas Room desde el primer día · precarga · tema visual (paleta neutra, naranja de acento) · transiciones por defecto · accesibilidad base (`sp`, 48 dp, `contentDescription`, texto ≥ 12 sp) · repositorio GitHub público · diario por sesión · `estado-nivel.md` · pruebas |

> **En el nivel 1, tocar una mesa blanca en Cuenta solo informa.** Las comandas nacen desde Pedir. Añadir platos desde Cuenta y abrir la carta desde una mesa: incremento 9.

### 4.2 Nivel 2 — incrementos, en este orden

| # | Incremento | Qué incluye |
|---|---|---|
| 1 | **Etiquetas y chips** | Pantalla 7 entera (lista simple con `[+]`; crear pide solo el nombre; renombrar, eliminar y recuperar) · marcar etiquetas en el plato (3) · `[Etiquetar]` en masa (2c: se elige la etiqueta y salen los platos con casillas) · **chips de filtro en una fila propia bajo la barra** de la carta (5a); todos los chips activos deben cumplirse; sección vacía desaparece; *"Ningún plato cumple los filtros"* con botón para quitarlos |
| 2 | **Modo kiosco** | Interruptor *"Se la doy al cliente"* en 1a · `startLockTask()` (screen pinning, sin administrador) · aviso si el usuario rechaza el diálogo del sistema · **modo camarero** (interruptor apagado): sin fijar pantalla, salida libre y **cambiar de mesa sin salir** — cómo, se decide entonces (no será un ▾ en la barra, P89) |
| 3 | **Modo Agrandar** | Tema alternativo con tamaños mayores y más contraste; se apaga al salir de Pedir |
| 4 | **Modificadores** | Mini-formulario (3b: nombre, tipo, precio; QUITAR siempre a 0 y sin campo) · en la ficha 5b (quitar arriba, añadir abajo, cantidad 1-9) · `linea_modificador` con nombre y precio congelados · líneas idénticas (mismos modificadores) se suman en el carrito |
| 5 | **Tabla nutricional** | 7 campos en mg (3c) · kcal calculadas desde kJ (÷ 4,184) · desplegable en 5b con el título **"Valores por ración"** (P125) · sin fila si no se rellena nada |
| 6 | **Vista previa** (2f) | La pantalla 5 sin carrito, Añadir ni Enviar |
| 7 | **Resumen de ingresos por periodos** (P102, P149) | Desde 2g: años → meses → calendario con el total de cada día → día |
| 8 | **Orden ▲▼ y `[Eliminar y recuperar]` en masa** | Intercambio de `orden`; flechas bloqueadas en los extremos y en la penúltima (la última es *Otros*) · 2d con el aviso agrupado R6 |
| 9 | **Cuenta completa** | Añadir platos y cambiar cantidades desde 6b · **abrir la carta (pantalla 5) en modo camarero desde `[+ Añadir platos]` y desde una mesa blanca**, ya fijada a la mesa: carrito, Enviar (R2) y vuelta a 6b · botón `[Imprimir]` que avisa de que no hay impresora |
| 10 | **Idiomas y traducciones** | Pantalla 8 entera (español fijo arriba *(base)*; código de dos letras mayúsculas, único, *ES* rechazado) · editor que **lee la carta, no la copia**: dos listas (traducidos / pendientes), contador *"N de M"*, autoguardado al salir del campo con snackbar *"Guardado"* · selector de idioma en la carta (5d) · R12 · avisos al cambiar el código o eliminar un idioma con traducciones |
| 11 | **Foto de la tabla nutricional** (`imagenNutricional`) | Segundo flujo de foto. Si hay campos y foto, se enseñan los campos |
| 12 | **Agotado temporal** (`producto.disponible`, P153) | Interruptor *Desactivar / Activar* en el formulario del plato (3a) y en su fila del Panel; estado **Desactivado** (distinto de *Eliminado*: el plato vuelve con su número, alérgenos, etiquetas y modificadores intactos) · el plato sale de la carta del cliente sin salir del Panel · aviso R6 al desactivar · `disponible` entra en la definición de plato visible (R15). **En el nivel 1 la columna existe desde el primer día y vale siempre `true`** |

### 4.3 Nivel 3 — solo si sobra tiempo

Modo de alcance · pago mixto real · color verde con *Liberar mesa* y *Empezar comanda nueva* · línea de resumen del Panel · resaltado del plato al volver al Panel · *"Lo pedido por la mesa"* (5e) · animaciones más allá de las tres básicas.

## 5. Modelo de datos — 14 tablas

Tipos Kotlin: `Long` para claves, `Int` para céntimos, cantidades y números de plato y mesa, `Long` (milisegundos desde época) para fechas, `?` donde es nulable. Nombres de columna en `snake_case` como el E-R (`@ColumnInfo(name = "precio_centimos")`), propiedades en `camelCase`.

| # | Tabla / entidad | Campos | Restricciones que **sí** declara Room |
|---|---|---|---|
| 1 | `categoria` / `Categoria` | id, nombre, imagen?, orden: Int, activo, esPorDefecto | `@Index(nombre, unique = true)` |
| 2 | `producto` / `Producto` | id, categoriaId, numero: Int, nombre, descripcion?, precioCentimos: Int, imagen?, imagenNutricional?, activo, **disponible** (`true` por defecto; solo lo cambia el incremento 12) | `@Index(numero, unique = true)`; FK → categoria RESTRICT |
| 3 | `producto_nutricion` / `ProductoNutricion` | productoId (**PK y FK**, 1 a 1), energiaKj?, grasasMg?, grasasSaturadasMg?, hidratosMg?, azucaresMg?, proteinasMg?, salMg? — todos `Int?` | FK → producto RESTRICT |
| 4 | `idioma` / `Idioma` | id, codigo, nombre, activo | `@Index(codigo, unique = true)` |
| 5 | `producto_traduccion` / `ProductoTraduccion` | productoId, idiomaId, nombre | **PK compuesta** (productoId, idiomaId); FKs RESTRICT |
| 6 | `modificador` / `Modificador` | id, productoId, nombre, precioCentimos: Int, tipo: TipoModificador, activo | FK → producto RESTRICT |
| 7 | `alergeno` / `Alergeno` | id, nombre | — (14 filas precargadas, no editables) |
| 8 | `producto_alergeno` / `ProductoAlergeno` | productoId, alergenoId | **PK compuesta**; FKs RESTRICT |
| 9 | `etiqueta` / `Etiqueta` | id, nombre, activo | `@Index(nombre, unique = true)` |
| 10 | `producto_etiqueta` / `ProductoEtiqueta` | productoId, etiquetaId | **PK compuesta**; FKs RESTRICT |
| 11 | `mesa` / `Mesa` | id, numero: Int | `@Index(numero, unique = true)` (P70). **60 filas fijas.** Sin `activo` ni `estado` |
| 12 | `comanda` / `Comanda` | id, mesaId, estado: EstadoComanda, fechaCreacion: Long, fechaCierre: Long? | FK → mesa RESTRICT |
| 13 | `linea_comanda` / `LineaComanda` | id, comandaId, productoId, cantidad: Int, precioUnitarioCentimos: Int, nombreProducto | FK → comanda RESTRICT; FK → producto RESTRICT |
| 14 | `linea_modificador` / `LineaModificador` | id, lineaComandaId, modificadorId, precioCentimos: Int, nombreModificador, cantidad: Int (= 1 en los QUITAR, P71) | FK → linea_comanda **CASCADE** (la única); FK → modificador RESTRICT |

`EstadoComanda` = PENDIENTE / PAGADA / ANULADA · `TipoModificador` = AÑADIR / QUITAR. Se guardan como texto con `@TypeConverter` (`Converters`), para que en la base de datos se lean.

### 5.1 Reglas de datos que no se negocian

- **Todo importe en céntimos y todo valor nutricional en miligramos, como `Int`.** La coma flotante acumula error al sumar. La interfaz convierte (18,50 € ↔ 1850).
- **`producto.numero` y `mesa.numero` son enteros y únicos.** Si fueran texto, `"7"` y `"07"` convivirían.
- **Ningún precio negativo (R8).** Room no sabe declarar `CHECK` (verificación 9): **lo garantiza el código**, en `Validacion.precioValido()` llamada por `CartaRepository.guardarPlato` y `guardarModificador` y por `ComandaRepository.enviarCarrito` al congelar precios. Un precio negativo lanza `IllegalArgumentException`; la interfaz además no admite el signo. Los QUITAR se guardan a 0. **No escribir `CHECK` en ningún `@Entity` ni en un `Callback`**: Room compara el esquema al abrir y no lo entendería.
- **Las entidades no se borran: `activo = false`** en `producto`, `categoria`, `modificador`, `etiqueta` e `idioma`. `mesa` no lleva `activo`. **Eso es *eliminar*** (y *recuperar* es volver a `activo = true`). **Desactivar es otra cosa** (P153): `producto.disponible = false`, el agotado temporal, **solo en platos**, nivel 2 (incremento 12); en el nivel 1 la columna vale siempre `true` y ninguna pantalla la toca.
- **Lo que sí se borra:** filas de relación (desmarcar una etiqueta, vaciar una traducción) y **líneas de una comanda todavía abierta**. **Una comanda cerrada (PAGADA o ANULADA) es intocable.** Una comanda anulada por R7 queda ANULADA con cero líneas en el histórico.
- **`onDelete = RESTRICT` en todas las claves foráneas, salvo `linea_modificador → linea_comanda`, que es `CASCADE`.**
- **Las tres tablas N:M llevan clave primaria compuesta.**
- **`linea_comanda` congela precio y nombre del producto; `linea_modificador` congela nombre y precio del modificador (R14).** Cambiar la carta no reescribe lo ya pedido ni lo cobrado.
- **Lo que se puede calcular no se guarda:** total de la comanda (R10), ocupación de la mesa (R3), kcal, traducido/pendiente (= hay fila), con/sin nutrición (= hay fila; **si no se rellena ningún campo no se crea la fila**).
- **`producto.nombre` es el nombre base en español.** El español no está en `idioma`.
- **`categoria.orden`**: al crear, máximo + 1. **La categoría con `esPorDefecto = true` va siempre la última**, pase lo que pase con su `orden`, y **es exactamente una** (R16): se reconoce por la columna, nunca por el nombre.
- **Fechas como instante (`Long`)**: el Resumen de ingresos consulta las PAGADAS con `fechaCierre` entre las 0:00:00 y las 23:59:59.999 del día elegido, en la zona horaria del dispositivo.
- **Precarga** (`Precarga.onCreate`): categoría *Otros* con `esPorDefecto = true` · 60 mesas (1-60) · 14 alérgenos (nombres del anexo II del Reglamento 1169/2011) · 3 etiquetas (Vegano, Vegetariano, Pescetariano) · 1 categoría y 1 plato de ejemplo. **Otros se crea antes que cualquier plato.**

## 6. Reglas de negocio — quién garantiza cada una

| # | Regla | Dónde vive en el código |
|---|---|---|
| R1 | Una mesa tiene como máximo una comanda no cerrada | `ComandaRepository.enviarCarrito`: busca `pendienteDeMesa`; si existe, añade líneas; si no, crea. **La base de datos no puede**: Room no declara índices únicos parciales |
| R2 | *Enviar* crea la comanda si no hay ninguna abierta y añade líneas si la hay. Cada envío crea líneas nuevas (no suma a las existentes: precios congelados distintos) | `ComandaRepository.enviarCarrito`, en una `@Transaction` |
| R3 | Mesa ocupada = tiene comanda PENDIENTE. No hay campo | `ComandaDao.pendientesConTotal` + `ComandaRepository.mesasConEstado` |
| R4 | No se envía un carrito vacío; 1-99 unidades por línea; modificadores de añadir 1-9 | `Carrito` y `Validacion`; la interfaz bloquea los botones en los límites |
| R5 | Nada se borra salvo relaciones y líneas de comanda abierta; comanda cerrada intocable | Repositorios: no existe ningún método que borre una entidad ni que toque una comanda cerrada |
| R6 | Al eliminar un plato **o una categoría** (y, en el incremento 12, al desactivar un plato), avisar de qué mesas lo tienen PENDIENTE; **no borrar ninguna línea**; varios a la vez → un aviso agrupado | `CartaRepository.eliminarPlato / eliminarCategoria` (incremento 12: también `desactivarPlato`) devuelven la lista de mesas; la pantalla enseña `MesasAfectadasDialog` **antes** de confirmar |
| R7 | Comanda sin líneas → ANULADA con `fechaCierre`, mesa libre | `ComandaRepository.quitarLinea` (devuelve `true` si quedó anulada); la pantalla avisa antes de quitar la última |
| R8 | Ningún precio negativo | **`Validacion` + repositorios** (ver 5.1). Prueba P-C-09 |
| R9 | Dos platos no tienen el mismo número a la vez | **La base de datos** (`UNIQUE` sobre entero) y `ProductoDao.existeNumero` para avisar antes de guardar |
| R10 | La comanda no guarda su total | `Calculadora.total(lineas)`; `ComandaRepository.totalDe` |
| R11 | RESTRICT en todo salvo `linea_modificador → linea_comanda` CASCADE | **La base de datos** (`@ForeignKey`) |
| R12 | Sin traducción → nombre en español | `CartaRepository` (incremento 10) |
| R14 | Las líneas congelan nombre y precio | `ComandaRepository.enviarCarrito` copia `nombre` y `precioCentimos` a cada `LineaComanda` |
| R15 | Plato visible = en la carta (`activo`) **y** no agotado (`disponible`; nivel 1: siempre `true`) **y** categoría activa | `ProductoDao.visibles()` (`activo = 1 AND disponible = 1`, JOIN con `categoria.activo = 1`); el Panel usa `porCategoria` y los ve todos |
| R16 | Exactamente una categoría por defecto; no se elimina; siempre la última; se puede renombrar | `Precarga` (la crea), `CartaRepository.guardarCategoria` (rechaza una segunda por defecto y rechaza eliminarla), `CategoriaAdapter` (la pinta la última, sin flechas ni interruptor) |

> **Para explicarlo en el vídeo (P128):** R9 la impone la base de datos (un `UNIQUE`) y R1 no puede (haría falta un índice único parcial, que Room no declara): saber dónde vive cada garantía es lo que distingue entender el modelo de copiarlo.

**Más lógica que vive en los repositorios:**

- **Puerta de Pedir:** `CartaRepository.hayPlatoVisible()`. Si no hay platos existentes → *"La carta está vacía"* (no se puede provocar con la precarga: se conserva por la app diseñada); si los hay pero ninguno visible → *"Todas las categorías están eliminadas"*.
- **Cadena 3e** al guardar un plato en una categoría eliminada: `guardarPlato` devuelve `ResultadoGuardado.CategoriaEliminada`; la pantalla pregunta *"¿Muevo el plato a Otros?"* y, si no, *"¿Quieres reactivar <categoría>? Volverán a la carta sus N platos"*; si tampoco, se guarda ahí, invisible.
- **`ResultadoGuardado`**: Ok · NumeroRepetido (R9) · NombreRepetido (categoría/etiqueta) · CategoriaEliminada.
- **Resumen de ingresos:** `ComandaDao.pagadasEntre(inicio, fin)` → `ResumenIngresos` (comandas con mesa, hora, total; cuántas; total).

## 7. Pantallas — lo que hay que tener delante al programar cada una

Las fichas completas (flujo, validaciones, casos límite, errores) están en `docs/spec+doc-pantallas.md` y los dibujos en `docs/wireframes/`. **Se programa lo que dice la ficha y se dibuja lo que enseña el wireframe**; si discrepan, manda el wireframe en disposición y la ficha en comportamiento, y se pregunta. Aquí solo lo que no está en ninguno de los dos o que Claude Code tiende a hacer mal:

- **Diálogos, hojas y pantallas (P84):** una pregunta → `ConfirmacionDialog` centrado; un formulario corto → hoja inferior (`BottomSheetDialogFragment`; 2b lleva nombre y el selector de foto, y sigue siendo hoja: así está dibujada y así lo dice su ficha); con foto grande, lista larga o sin nada detrás → pantalla completa.
- **`ConfirmacionDialog` es uno solo** (título, texto, botón afirmativo, botón negativo) y sirve para Enviar, Cobrar, Anular, la última línea (R7), la cadena 3e, *"Se perderán los cambios"* y el aviso de platos sin enviar (RF-38). No se crea un diálogo por caso.
- **`RejillaMesasFragment` es uno con dos modos** (`elegir` / `gestionar`): **4 columnas × 15 filas**, cuadrículas de ~73 dp, número de mesa y, si tiene comanda, fondo rojo con el total a **12 sp** (P130). `GridLayoutManager(4)` con scroll.
- **Panel (2a) [Claude]:** `RecyclerView` exterior (una fila por categoría) cuyo elemento contiene la cabecera de la caja, un **`RecyclerView` interior con altura fija de 270 dp** y el botón `[+ Plato]` fuera del scroll interior. **Nunca un `ScrollView` con listas dentro** (verificación 3). Compartir un `RecycledViewPool` entre las listas interiores.
- **Carta (5a):** `RecyclerView` horizontal para la fila de categorías (fija bajo la barra, fuera del scroll) y `RecyclerView` vertical con dos tipos de elemento (cabecera de sección y `FilaPlatoAdapter`); tocar una categoría → `scrollToPositionWithOffset` a su cabecera y se resalta.
- **Fila de plato compartida** (`FilaPlatoAdapter`): miniatura (~44 dp, Glide, o "?") · número · nombre (cortado con "…") · precio · en el Panel, además, *Eliminado*. **Es el argumento *"se reutiliza el componente, no la pantalla"***: el Panel y la carta son dos disposiciones distintas con la misma fila.
- **Salir de Pedir:** el botón *Salir* y el Atrás del sistema (`OnBackPressedCallback`) hacen lo mismo: aviso de platos sin enviar (si hay) → `PinDialog` → volver a 1a. Al salir, el carrito se vacía (vive en memoria, RNF-24).
- **Textos:** todos en `strings.xml`; los mensajes están literalmente en las fichas y en el plan de pruebas (P-M-nn, columna *Resultado esperado*): se copian de ahí.
- **Atrás:** flecha `←` con la palabra *Atrás* arriba a la izquierda donde la ficha la pide; sin flecha *adelante* (Android no la tiene, verificación 4).

## 8. Seguridad

| Qué | Cómo |
|---|---|
| **PIN del Propietario** | 4 dígitos. `Hash.pbkdf2(pin, sal)` con **`PBKDF2withHmacSHA256`**, sal de **16 bytes** de `SecureRandom`, **100 000 iteraciones**, clave de **256 bits** (P126; RFC 8018 pide ≥ 64 bits de sal y ≥ 1 000 iteraciones). `PinStore` guarda `sal` y `hash` en Base64 en `SharedPreferences` privadas (`MODE_PRIVATE`), **nunca el PIN**. `coincide(pin)` recalcula y compara. Intentos ilimitados (recorte declarado) |
| **Consultas** | Room parametriza todo (`@Query` con `:parametro`). Nunca `rawQuery` con texto concatenado (RNF-08) |
| **Resumen de ingresos** | Solo desde el Panel, detrás del PIN. Cuenta no lo enseña (RNF-10) |
| **Modo kiosco** | Incremento 2: `startLockTask()` (screen pinning, sin administrador); el sistema muestra un diálogo y el usuario puede salir por el sistema. La protección real combina el PIN de la app con la opción del dispositivo *"solicitar PIN antes de dejar de fijar"* (Anexo I) |
| **Lo que no hay, a propósito** | Código de recuperación (olvidar el PIN obliga a reinstalar: **se pierde la carta, las comandas y las fotos**, porque todo está en el almacenamiento privado, que se borra al desinstalar) · bloqueo tras intentos fallidos · cierre automático del Panel al pasar a segundo plano |

## 9. Imágenes

- **Archivo, nunca BLOB** (verificación 6: Android lee las filas por una ventana de 2 MB). En `imagen` se guarda la **ruta** del archivo dentro de `filesDir/fotos/`.
- **`ImageStore.guardar(uri)`**: decodifica con `inSampleSize` (guía *Loading Large Bitmaps Efficiently*), redimensiona el lado mayor a **~1080 px**, comprime a **JPEG (calidad ~85)**, escribe el archivo y devuelve la ruta. Se hace en `Dispatchers.IO`.
- **Elegir la foto:** `ActivityResultContracts.PickVisualMedia` (selector de fotos del sistema, sin permisos de almacenamiento).
- **Cargar con Glide** en cada `ImageView` (`Glide.with(view).load(File(ruta)).placeholder(neutro).error(interrogacion).into(view)`): Glide redimensiona al tamaño real de la vista y cachea. Sin permiso `INTERNET`.
- **Sin foto**: el **"?"** con `contentDescription = "Sin foto"`, en el plato (miniatura y ficha) **y en la categoría** (círculo con «?» en la fila de categorías de la carta y en la hoja 2b; P224, que sustituye al *"solo el nombre"* de P92). **Toda la app funciona sin fotos**: por eso la foto es la última pieza del nivel 1 (sesión 12).

## 10. Estilo visual y accesibilidad base

- **Paleta neutra** (blanco, negro, gris oscuro) con **naranja como único acento** para los botones de acción (Guardar, Enviar, Añadir, Cobrar, Dar la cuenta, Anular). **No usar rojo ni verde** de acento: ya significan *mesa ocupada* y *mesa cobrada*. Tema Material 3 con `colorPrimary` naranja [Claude].
- **Transiciones entre pantallas: las de por defecto.** No declarar ninguna.
- **Tres animaciones con función:** sacudida del campo de PIN al fallar (nivel 1, `ObjectAnimator` de `translationX`) · resaltado del plato al volver al Panel (nivel 3) · snackbar *"Guardado"* en el editor de traducciones (incremento 10).
- **Accesibilidad que entra siempre** (RNF-11 a RNF-13): textos en `sp` y **ningún texto por debajo de 12 sp** · objetivos táctiles **≥ 48 × 48 dp** · `contentDescription` en todo elemento interactivo e imagen · contraste ≥ 4,5:1 en texto y 3:1 en el resto · **ningún estado se comunica solo con color** (la palabra *Eliminado*, texto en todos los botones) · **nada de gestos**: todo tiene botón (Android exige alternativa a todo gesto; el diseño los elimina, P127). Al terminar el nivel 1 se pasa el **Accessibility Scanner** por las siete pantallas (sesión 13).

## 11. Orden de construcción — sesiones (P135 → A [Claude])

Cada sesión tiene un objetivo de una línea, un entregable comprobable y termina con su ficha de diario, `estado-nivel.md` actualizado y un commit con push. **Una sesión puede ocupar más de una jornada; nunca se abre la siguiente con la anterior a medias sin dejarlo escrito.** El orden es primero la base (para que las reglas queden escritas y probadas antes de tocar una pantalla) y después las pantallas en el orden del vídeo.

| S | Objetivo | Entregable comprobable | Pruebas |
|---|---|---|---|
| **1** | **Proyecto y repositorio.** Android Studio: proyecto vacío (Kotlin DSL, `minSdk 26`, ViewBinding, catálogo de versiones con Room, Glide, KSP, corrutinas, pruebas); primer arranque del emulador; Git: `init`, `.gitignore`, `README.md`, repositorio público en GitHub, primer `commit` y `push`; carpeta `docs/` con este spec, `CLAUDE.md`, fichas, wireframes, plan de pruebas, plantilla del diario y `estado-nivel.md`. **Antes del primer commit (P226):** Claude Code guía a Daniel para crear o editar `C:\Users\dhuan\.claude\settings.json` (ajustes de **usuario**; NO el `.claude/settings.json` del repositorio, que se subiría a GitHub) con `{"attribution": {"commit": "", "pr": ""}}` y reiniciar Claude Code | La app vacía arranca en el emulador; el repositorio se ve en GitHub; **el primer commit no lleva la línea `Co-Authored-By`** | — |
| **2** | **14 entidades, `Converters`, `AppDatabase`, `Precarga`** | Compila; al arrancar, el inspector de base de datos enseña las 14 tablas con *Otros*, 60 mesas, 14 alérgenos, 3 etiquetas y el ejemplo | — |
| **3** | **DAOs y dominio puro**: los 5 DAOs con sus consultas; `Carrito`, `LineaCarrito`, `Calculadora`, `Validacion`, `Hash` | Compila; las cinco puras pasan | **P-C-01 a P-C-05 y P-C-09** en `test/` |
| **4** | **Repositorios**: `CartaRepository`, `ComandaRepository`, `SeguridadRepository`, `PinStore` | Compila; cada método tiene un comentario de una línea con la regla que garantiza | — |
| **5** | **Selector y PIN** (1a, 1b, 1c, 1e) + `ConfirmacionDialog` | P-M-01, 02, 03, 13 pasan | P-M-01, 02, 03, 13, 29 |
| **6** | **Panel** (2a, 2b, 2e) con `CategoriaAdapter`, `FilaPlatoAdapter`, `MesasAfectadasDialog` | P-M-04, 05, 06 pasan (con platos creados a mano en el inspector si hace falta) | P-M-04, 05, 06 |
| **7** | **Plato sin foto** (3a, 3d, 3e) | P-M-07, 09, 10, 11 pasan | P-M-07, 09, 10, 11 |
| **8** | **Pedir** (1d con `RejillaMesasFragment`, 5a, 5b, 5c, Enviar, salir con PIN y aviso RF-38) | P-M-14 a P-M-21 pasan | P-M-14 a 21 |
| **9** | **Cuenta** (6a, 6b con Quitar y R7, 6c con `ReciboFragment`, cambio, Cobrar, Anular) | P-M-22 a P-M-28 pasan | P-M-22 a 28 |
| **10** | **Resumen de ingresos** (2g) | P-M-12 pasa | P-M-12 |
| **11** | **Pruebas de Room** en `androidTest/` (P117, con red de seguridad: si no arrancan en una sesión, se documenta y R1, R7 y R16 quedan cubiertas por las manuales) | P-C-06, 07, 08 pasan, o la ficha dice por qué no | P-C-06 a 08 |
| **12** | **Fotos** (`ImageStore`, selector de fotos, Glide en Panel, 5a y 5b) | P-M-08 pasa | P-M-08 |
| **13** | **Cierre del nivel 1**: tema (paleta, naranja), `strings.xml` EN, Accessibility Scanner en las siete pantallas, pasada por las 29 manuales que faltaran, `estado-nivel.md` con todo el nivel 1 en *implementado*, etiqueta `v1-nivel1` en Git; **APK debug generada en Android Studio (Build → Build APK) y adjuntada a mano por Daniel a una Release de GitHub con esa etiqueta (P225, P248)** | Las 29 P-M y las 9 P-C anotadas en `spec+doc-pruebas.md` con fecha; la Release `v1-nivel1` con la APK se ve en GitHub | Todas |

**Qué pasa después de la S13, en este orden (P233 → A):**

1. **Revisión del prototipo con Daniel** (fila 43 del registro): se repasa lo hecho y ahí se deciden dos preguntas aplazadas: **P227 · `CLAUDE.md` público** (A se queda en el repositorio, recomendado / B se borra al final, sabiendo que sigue en el historial de Git) y **P228 · marcas `[Claude]`** (A se mantienen hasta la pasada de apropiación, recomendado / B se quitan al terminar el prototipo).
2. **Se completa la memoria** (fase 7).
3. **Pasada de apropiación (P230 → D):** Claude Code hace la lista de las partes del código que Daniel no sabría explicar o que están por encima de su nivel (marcas `[Claude]`, lo más avanzado, lo que el diario dice que no entendió); Daniel elige cuáles; en cada una **Claude Code solo guía y Daniel teclea**; se pasan otra vez las pruebas de esa parte; commit; ficha del diario *"reescrito por Daniel"*. **Coste aceptado:** después hay que actualizar el apartado 7 de la memoria, la declaración de IA y las capturas del Anexo II.
4. **Solo entonces, y si queda tiempo,** los incrementos del 4.2 (fase 6b), cada uno como una sesión propia con sus pruebas añadidas al final de las tablas (código libre siguiente), su redibujo de wireframes y figura del diagrama afectados, y **una Release `v1-inc<N>` con su APK**. Después, actualizar la memoria y grabar el vídeo (fase 8).

**Ritual de sesión [Claude]:**

- *Al empezar:* leer `docs/diario/` (la última ficha) y `estado-nivel.md`; escribir la ficha nueva con fecha, número, objetivo y hora; `git pull` no hace falta (una sola máquina), pero se comprueba `git status` limpio.
- *Durante:* piezas pequeñas, explicación antes del código, pregunta de comprensión después; cada error va a la tabla de problemas en el momento.
- *Al terminar:* pruebas de la sesión; ficha completa (tiempo real, qué entendí y qué no, uso de IA); `estado-nivel.md`; `git add -A && git commit -m "S<N>: <objetivo>"` y `git push`; anotar el identificador corto del commit en la ficha.

## 12. Pruebas

Plan completo en `docs/spec+doc-pruebas.md` (29 manuales P-M-01…29, una por RF de nivel 1; tabla de 8 columnas con *Resultado*, *Observaciones* y *Fecha* que se rellenan al ejecutar). **Pruebas de código: nueve.**

| Código | Qué | Clase · función | Carpeta |
|---|---|---|---|
| P-C-01 | Importe de una línea (1850 × 3 = 5550) | `LineaCarrito.importe()` / `Calculadora.importe` | `test/` |
| P-C-02 | Total del carrito, vacío y con líneas (4200; `numPlatos` 3) | `Carrito.total()`, `estaVacio()`, `numPlatos()` | `test/` |
| P-C-03 | Líneas idénticas se suman; tope 99 | `Carrito.anadir()`, `cambiarCantidad()` | `test/` |
| P-C-04 | Cambio: 800 / −200 / 0 | `Calculadora.cambio(total, entregado)` (función pura; `CuentaViewModel` la llama) | `test/` |
| P-C-05 | Hash del PIN: coincide consigo mismo, no con 1235, distinto con otra sal, no contiene "1234" | `Hash.pbkdf2()` (puro) y `PinStore.coincide()` con un `SharedPreferences` falso o solo `Hash` | `test/` |
| **P-C-09** | **Precio negativo rechazado** (R8): `precioValido(-1)` falso; `guardarPlato` con −100 lanza excepción; 0 se admite | `Validacion.precioValido()`; `CartaRepository.guardarPlato` con un DAO falso | `test/` |
| P-C-06 | Una sola comanda abierta por mesa; dos envíos → una comanda con dos líneas, nombre y precio copiados | `ComandaRepository.enviarCarrito` con Room en memoria | `androidTest/` |
| P-C-07 | Quitar la última línea → ANULADA con `fechaCierre`, mesa libre | `ComandaRepository.quitarLinea` | `androidTest/` |
| P-C-08 | Exactamente una categoría por defecto; no se elimina; no se crea otra | `Precarga`, `CategoriaDao.porDefecto`, `CartaRepository` | `androidTest/` |

Las de `test/` corren en el PC sin emulador (por eso `Calculadora`, `Validacion` y `Hash` son funciones puras separadas de los ViewModels y de `PinStore`). Las tres de `androidTest/` usan `Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)` con la precarga ejecutada; necesitan el emulador. **Red de seguridad (P117):** si en la sesión 11 no arrancan, se documenta en la ficha, R1, R7 y R16 quedan cubiertas por P-M-20, P-M-24 y P-M-05, y la memoria lo dice tal cual. La documentación de Room (sep 2026) ofrece además pruebas JVM vía Room KMP; **descartado (P124)**: exige estructura multiplataforma.

**Sin prueba, a propósito:** R11 (CASCADE) y R12 solo actúan en nivel 2. RNF-11/13 se comprueban con el Accessibility Scanner (S13).

## 13. Diario, estado del nivel y Git

- **Diario de desarrollo:** una ficha por sesión en `docs/diario/sesion-NN.md` con la plantilla de `docs/plantilla-diario.md` (fecha · sesión · objetivo · tiempo real · nivel/pieza · commit · qué se hizo · problemas con solución **y justificación** · qué entendí y qué no · pruebas · uso de IA · siguiente sesión). El guion lo marca como *(Importante)*. Se rellena en dos momentos: al empezar y al terminar; los problemas, en el momento. Al cerrar la fase 6 las fichas se suben al Project y se funden en `diario+doc-decisiones.md`.
- **`docs/estado-nivel.md`** (P137): tabla con una fila por función (los 53 RF de `spec+doc-requisitos.md`, agrupados por pantalla e incremento) y tres columnas: *Nivel* · *Estado* (`diseñado` / `en curso` / `implementado` / `implementado, no probado`) · *Sesión*. Se actualiza al cerrar cada sesión. Es la fuente de la tabla diseñado/implementado del apartado 5 y del 7 de la memoria.
- **Git (P136):** una sola rama **`main`**; commits dentro de la sesión cuando una pieza compila y pasa sus pruebas; **un commit al cerrar cada sesión** con mensaje `S<N>: <objetivo>`, seguido de `push`. Sin ramas ni *pull requests*. Etiqueta `v1-nivel1` al cerrar la S13 y `v1-inc<N>` por incremento; **cada etiqueta lleva una Release en GitHub con la APK debug adjuntada a mano por Daniel (P225, P248)**. `.gitignore` de Android Studio (no se suben `build/`, `.idea/` local ni `local.properties`). **Los commits no llevan la línea `Co-Authored-By`** (P226: ajuste de usuario `attribution` en `C:\Users\dhuan\.claude\settings.json`, hecho en la S1); **el uso de IA se declara en la memoria y en cada ficha del diario, no en el commit**.
- **Uso de IA:** cada ficha dice qué pidió Daniel, qué generó Claude Code y qué revisó o cambió Daniel. La frase de la memoria (P37) se redacta en la fase 7 con las fichas delante; **lo escrito coincide con lo hecho**. Los códigos P-C significan *"C de Claude"* (P115) y la memoria lo explica junto a esa frase. **Como los commits no llevan atribución (P226), la frase de la memoria y las fichas del diario son la única prueba del reparto Daniel / Claude Code: tienen que decir, sin rebajarlo, que Claude Code generó el código y qué hizo Daniel** (pedir, entender, revisar, probar, y lo que reescriba en la pasada de apropiación, P230).

## 14. Lo que NO hay que hacer

Está en `CLAUDE.md`, que Claude Code lee en cada sesión. Repetido aquí lo que más cuesta recordar a mitad de una tarea: **no** guardar el PIN en claro · **no** borrar entidades con `activo` · **no** borrar líneas al eliminar o desactivar un plato · **no** crear comandas vacías · **no** guardar lo que se calcula · **no** reconocer *Otros* por su nombre · **no** programar una segunda carta ni un campo de número · **no** usar gestos · **no** escribir `CHECK` en las entidades · **no** meter librerías fuera del apartado 2 · **no** empezar el nivel 2 con el nivel 1 abierto · **no** cambiar alcance, modelo ni reglas sin decirlo.

## 15. Entregables de la memoria que dependen del código

| Entregable | Cuándo | De dónde sale |
|---|---|---|
| **Diagrama de clases definitivo** (apartado 7) | Fase 7, desde el código real; sustituye al borrador del bloque 7 | Los paquetes del apartado 3 |
| **Estructura de carpetas** documentada | Fase 7 | Apartado 3 |
| **Pruebas ejecutadas y resultados** (apartado 7) | Se rellenan en las sesiones; se pasan a la memoria en la fase 7 (tabla apaisada) | `docs/spec+doc-pruebas.md` |
| **Diario de desarrollo** con problemas, soluciones y justificación *(Importante)* | Una ficha por sesión | `docs/diario/` |
| **Tabla diseñado/implementado** (apartados 5 y 7) | Fase 7 | `docs/estado-nivel.md` |
| **Repositorio público en GitHub** | Desde la S1 | — |
| **Anexo I — manual de instalación** | Fase 7, con la app lista: instalar el APK; preparar el móvil en modo kiosco (fijar pantalla y *"solicitar PIN antes de dejar de fijar"*) | — |
| **Anexo II — manual de usuario** | Fase 7: **advertir de que olvidar el PIN obliga a reinstalar y se pierde todo** (carta, comandas y fotos), y de que la interfaz solo está en español e inglés | — |

## 16. Verificaciones ya hechas (no volver a preguntarlas)

Las nueve verificaciones del diseño se hicieron en el bloque 9 contra documentación oficial, con URL y fecha, en `ref+doc-verificaciones.md` (Project). Lo que afecta al código ya está incorporado arriba: API 26 definitivo (apartado 2) · R8 en el código, no `CHECK` (apartados 5.1 y 6) · PBKDF2-SHA256 con sus parámetros (apartado 8) · listas anidadas con altura fija (apartado 7) · sin navegación adelante (apartado 7) · gestos con alternativa → sin gestos (apartado 10) · archivos, no BLOB (apartado 9) · guía de bitmaps y Glide sin conexión (apartado 9) · texto ≥ 12 sp (apartado 10). Si Claude Code duda de alguna, la respuesta es la de arriba.
