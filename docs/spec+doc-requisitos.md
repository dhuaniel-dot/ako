> **Requisitos funcionales y no funcionales del proyecto. Es un `spec` (define qué se construye, junto con `spec+doc-diseno-app.md` y las fichas) y un `doc` (alimenta el apartado 7 de la memoria, *"requisitos funcionales y no funcionales detallados"*, y el plan de pruebas). Se reescribe entero en cada cierre de fase.**
> Creado el **17 de septiembre de 2026 — fase 2d, bloque 2** (P60-P65). Extraído de las siete fichas de `spec+doc-pantallas.md`, de las 15 reglas y de los apartados 2, 6, 7, 8, 9, 14 y 15 del spec. Motivos en `diario-2d-requisitos.md`.
> **Última actualización: 17 de septiembre de 2026, en el cierre de la fase 2d.** Se fundieron aquí los cambios del bloque 8 (`spec+doc-requisitos-cambios-b8.md`: RF-46 partido, RF-51 y RF-52 nuevos, RF-20 reescrito, diez filas renumeradas de incremento, recuento corregido y nota a RF-25) y se aplicaron las filas **7, 8, 35, 46, 50 y 61** del registro `proceso-pendiente-cierre.md`, que tocan **RNF-21, RNF-03, RF-25, RNF-07, RNF-12 y RNF-20**. Motivos en `diario-2d-pruebas.md` y `diario-2d-verificaciones.md`.

# Requisitos

## Cómo se lee

- **Requisito funcional (RF):** algo que la app hace y que un usuario puede pedirle (*"el usuario puede…"*). Una fila por función (P60); cada RF tendrá su caso de prueba en el plan de pruebas.
- **Requisito no funcional (RNF):** una condición sobre cómo tiene que ser la app, que afecta a todas las funciones a la vez.
- **Nivel:** el del contrato del prototipo (spec, apartado 16). `1` = nivel 1, obligatorio; `2 · inc. n` = nivel 2, incremento *n*; `3` = nivel 3.
- **Orden de los incrementos del nivel 2:** 1 etiquetas y chips · 2 kiosco · 3 Agrandar · 4 modificadores · 5 nutrición · 6 vista previa · 7 Resumen de ingresos por periodos · 8 orden y selección masiva · 9 Cuenta completa · 10 idiomas · 11 foto nutricional.
- **Obj.:** objetivo específico al que sirve (bloque 1 de la 2d): **OE2** diseñar la app completa, **OE3** implementar el prototipo. Todo lo de nivel 1 es OE3; lo de niveles 2 y 3 es OE2.
- **Vocabulario:** el que fija el spec, apartado 0. *Cuenta* es el rol; *recibo* es el documento; un plato se *elimina* (sale de la carta) o se *desactiva* (agotado, nivel 2); *plato visible* = en la carta, disponible y con categoría activa; *modo camarero* = Pedir sin fijar la pantalla; el informe de lo cobrado es el *Resumen de ingresos*.
- **Orden de las filas:** por rol y, dentro de cada rol, por pantalla. Los dos requisitos nuevos del bloque 8 van junto al requisito del que salen (RF-52 tras RF-20, RF-51 tras RF-46), no al final.

## Requisitos funcionales

| Código | Requisito | Pantalla · vista | Reglas | Nivel | Obj. |
|---|---|---|---|---|---|
| **Propietario** | | | | | |
| RF-01 | Crear el PIN en el primer arranque, escrito dos veces, con aviso de que no se recupera | 1 · 1b | — | 1 | OE3 |
| RF-02 | Entrar en Propietario introduciendo el PIN; error con sacudida e intentos ilimitados | 1 · 1c | — | 1 | OE3 |
| RF-03 | Cambiar el PIN (actual + nuevo dos veces) | 1 · 1e | — | 1 | OE3 |
| RF-04 | Ver la carta en modo edición: cajas por categoría con sus platos, incluidos los **eliminados** marcados con la palabra *Eliminado* | 2 · 2a | R5, R15 | 1 | OE3 |
| RF-05 | Crear y editar una categoría (nombre, foto, activo); la categoría por defecto sin interruptor ni flechas | 2 · 2b | R16 | 1 | OE3 |
| RF-06 | **Eliminar** una categoría avisando de las mesas con sus platos en comandas pendientes, sin borrar líneas | 2 · 2b, 2e | R6, R15 | 1 | OE3 |
| RF-07 | Ordenar las categorías con flechas ▲▼; extremos y penúltima ▼ bloqueados | 2 · 2a | R16 | 2 · inc. 8 | OE2 |
| RF-08 | Crear y editar un plato: nombre, número, precio y categoría obligatorios; descripción y activo | 3 · 3a | R8, R9, R16 | 1 | OE3 |
| RF-09 | Añadir foto al plato: elegir, redimensionar (~1080px), comprimir a JPEG, guardar como archivo | 3 · 3a | — | 1 (última pieza) | OE3 |
| RF-10 | Marcar los alérgenos del plato entre los 14 legales | 3 · 3a | — | 1 | OE3 |
| RF-11 | **Eliminar** un plato avisando de las mesas afectadas, sin borrar líneas | 3 · 3d | R6 | 1 | OE3 |
| RF-12 | Al guardar un plato en una categoría **eliminada**, cadena de dos avisos (mover a la categoría por defecto / recuperarla con contador) | 3 · 3e | R15, R16 | 1 | OE3 |
| RF-13 | Añadir modificadores al plato (nombre, tipo AÑADIR/QUITAR, precio; QUITAR siempre a 0) | 3 · 3b | R8 | 2 · inc. 4 | OE2 |
| RF-14 | Marcar etiquetas del plato entre las activas | 3 · 3a | — | 2 · inc. 1 | OE2 |
| RF-15 | Rellenar la tabla nutricional (7 apartados en kJ y mg; kcal calculadas) | 3 · 3c | — | 2 · inc. 5 | OE2 |
| RF-16 | Subir una foto de la tabla nutricional como atajo | 3 · 3c | — | 2 · inc. 11 | OE2 |
| RF-17 | Etiquetar varios platos a la vez eligiendo primero la etiqueta | 2 · 2c | — | 2 · inc. 1 | OE2 |
| RF-18 | **Eliminar y recuperar** varios platos a la vez con un único aviso agrupado | 2 · 2d, 2e | R6 | 2 · inc. 8 | OE2 |
| RF-19 | Vista previa de la carta sin carrito, Añadir ni Enviar | 2 · 2f | — | 2 · inc. 6 | OE2 |
| RF-20 | Resumen de ingresos: selector de fecha (por defecto, hoy); lista de las comandas PAGADAS con `fecha_cierre` en ese día, con mesa, hora e importe; abajo, cuántas comandas y el total; tocar una abre su recibo en solo lectura | 2 · 2g | R10, R14 | 1 | OE3 |
| RF-52 | Resumen de ingresos por periodos: navegación años → meses → calendario con el total cobrado de cada día → día (que abre el Resumen de ingresos) | 2 · 2g | R10, R14 | 2 · inc. 7 | OE2 |
| RF-53 | **Desactivar y activar un plato** (agotado temporal, `producto.disponible`): sale de la carta del cliente, sigue visible en el Panel con la palabra *Desactivado* y vuelve con todo intacto | 3 · 3a · 2 · 2a | R6, R15 | 2 · inc. 12 | OE2 |
| RF-21 | Gestionar etiquetas: crear (solo nombre, único), renombrar, **eliminar** con contador de platos | 7 | R5 | 2 · inc. 1 | OE2 |
| RF-22 | Gestionar idiomas: crear (nombre + código de dos letras, único, *ES* rechazado), editar, **eliminar**; avisos con contador | 8 · 8a, 8b, 8d | R5 | 2 · inc. 10 | OE2 |
| RF-23 | Traducir nombres de platos: dos listas, autoguardado con snackbar, copiar el nombre español, vaciar = pendiente | 8 · 8c | R12 | 2 · inc. 10 | OE2 |
| RF-55 | Traducir también los nombres de las categorías, como los de los platos (tabla `categoria_traduccion`); sin traducción, nombre en español | 8 · 8c | R12 | 2 · inc. 10 | OE2 |
| **Cliente (Pedir)** | | | | | |
| RF-24 | Elegir rol en el selector: Propietario, Pedir, Cuenta | 1 · 1a | — | 1 | OE3 |
| RF-25 | Puerta de Pedir: no entrar sin ningún plato visible, con dos mensajes según el motivo | 1 | R15 | 1 | OE3 |
| RF-26 | Elegir la mesa en la rejilla en modo *elegir* (colores y totales visibles, toda mesa elegible) | 1 · 1d | R2, R3 | 1 | OE3 |
| RF-27 | Interruptor *"Se la doy al cliente"*: fijar pantalla (kiosco) o modo camarero con cambio de mesa sin salir | 1 · 1d | — | 2 · inc. 2 | OE2 |
| RF-28 | Salir de Pedir con botón visible o Atrás, pidiendo el PIN | 1 · 1c | — | 1 | OE3 |
| RF-29 | Consultar la carta: categorías + una lista por secciones; tocar una categoría salta a su sección; solo platos visibles | 5 · 5a | R15 | 1 | OE3 |
| RF-30 | Ver la ficha del plato: foto (o "?"), número, nombre, precio, descripción, alérgenos desplegables (o aviso *"pregunta al personal"*) | 5 · 5b | — | 1 | OE3 |
| RF-31 | Filtrar por etiquetas con chips apilables (un plato cumple todos los activos) | 5 · 5a | — | 2 · inc. 1 | OE2 |
| RF-32 | Ver la información nutricional en desplegable (campos si los hay; si no, la foto) | 5 · 5b | — | 2 · inc. 5 | OE2 |
| RF-33 | Elegir modificadores en la ficha; los de añadir con cantidad 1-9 | 5 · 5b | — | 2 · inc. 4 | OE2 |
| RF-34 | Cambiar el idioma de la carta; sin traducción, nombre en español | 5 · 5d | R12 | 2 · inc. 10 | OE2 |
| RF-54 | Selector de idioma de toda la app en 1a, al lado del icono de modo claro/oscuro: «Como el móvil», «Español», «English» (`AppCompatDelegate.setApplicationLocales`, guía oficial de idiomas por app); al cambiar a inglés, la interfaz y los platos y categorías traducidos | 1 · 1a | R12 | 2 · inc. 10 | OE2 |
| RF-35 | Activar el Modo Agrandar (magnificación) | 5 · 5a | — | 2 · inc. 3 | OE2 |
| RF-36 | Añadir al carrito con cantidad 1-99; +/−, quitar; líneas idénticas se suman | 5 · 5c | R4 | 1 | OE3 |
| RF-37 | Enviar con confirmación (mesa y total): crea la comanda o añade líneas; congela nombre y precio | 5 · 5c | R1, R2, R4, R14 | 1 | OE3 |
| RF-38 | Avisar al salir con platos sin enviar | 5 / 1 | — | 1 | OE3 |
| RF-39 | Ver lo pedido por la mesa, solo lectura | 5 · 5e | — | 3 | OE2 |
| **Camarero (Cuenta)** | | | | | |
| RF-40 | Ver la rejilla de 60 mesas en modo *gestionar*: blanco / rojo con total / verde con lo cobrado | 6 · 6a | R3, R10 | 1 (verde: 3) | OE3 |
| RF-41 | Ver la comanda de una mesa: líneas, modificadores y total calculado | 6 · 6b | R10 | 1 | OE3 |
| RF-42 | Anular la comanda con confirmación → ANULADA con `fecha_cierre`, mesa a blanco | 6 · 6b | R5, R7 | 1 | OE3 |
| RF-43 | Mostrar el recibo en pantalla con los nombres congelados en español | 6 · 6c | R14 | 1 | OE3 |
| RF-44 | Calcular el cambio desde un importe entregado opcional (negativo si falta) | 6 · 6c | — | 1 | OE3 |
| RF-45 | Cobrar con confirmación → PAGADA con `fecha_cierre` | 6 · 6c | — | 1 | OE3 |
| RF-46 | Quitar una línea de la comanda desde Cuenta; aviso al quitar la última (la comanda pasa a ANULADA); los modificadores se van con la línea | 6 · 6b | R5, R7, R11 | 1 | OE3 |
| RF-51 | Añadir líneas y cambiar cantidades desde Cuenta (`[+ Añadir platos]` abre la carta en modo camarero fijada a la mesa; +/− en las líneas) | 6 · 6b | R2, R4, R5 | 2 · inc. 9 | OE2 |
| RF-47 | Abrir la carta (pantalla 5) en modo camarero fijada a la mesa desde 6b, una mesa blanca o una verde; Enviar añade y vuelve a 6b | 6 → 5 | R2 | 2 · inc. 9 | OE2 |
| RF-48 | Botón *Imprimir* que informa de que no hay impresora | 6 · 6c | — | 2 · inc. 9 | OE2 |
| RF-49 | Mesa verde: Liberar o Empezar comanda nueva | 6 · 6a | — | 3 | OE2 |
| **Sistema** | | | | | |
| RF-50 | Precargar al instalar: categoría por defecto, 60 mesas, 14 alérgenos, 3 etiquetas, 1 categoría y 1 plato de ejemplo | — | R16 | 1 | OE3 |

**Recuento: 55 RF. Nivel 1: 29 · nivel 2: 24 · nivel 3: 2.** *(RF-54 y RF-55, selector de idioma en 1a y categorías traducidas, entran en el incremento 10 el 10 de octubre de 2026, pedidos por Daniel en la S15.)* *(RF-53, el agotado temporal del incremento 12, entra el 17 de septiembre por la noche con P153.)*

- **Nivel 1 (29):** RF-01 a RF-06, RF-08 a RF-12, RF-20, RF-24 a RF-26, RF-28 a RF-30, RF-36 a RF-38, RF-40 a RF-46, RF-50.
- **Nivel 3 (2):** RF-39 y RF-49.
- **Nivel 2 (24):** el resto. *(Decía "21": resto de antes de RF-53; corregido en la 5b, 24 sep.)*

La línea *"Nivel 1: 30 · nivel 2: 17 · nivel 3: 3"* que llevaba este documento desde el bloque 2 estaba mal contada (eran 28 · 20 · 2 antes de los cambios del bloque 8). Queda corregida aquí.

### Nota a RF-25 — el primer mensaje no se puede provocar en el prototipo

Con la precarga (RF-50 crea un plato de ejemplo) y con R5 (los platos no se borran de la base de datos, se marcan), en el prototipo **nunca hay cero platos existentes**, así que el primer mensaje de RF-25, *"La carta está vacía"*, no puede aparecer. **El requisito se conserva**: en la app diseñada (cliente-servidor) el arranque sin datos sí es posible, y el mensaje forma parte del diseño. La prueba **P-M-14** cubre solo el segundo mensaje (todos los platos existentes están ocultos, es decir, ninguno es **plato visible**), y la memoria lo justifica en una línea. Decidido en P123, bloque 8.

## Requisitos no funcionales

| Código | Categoría | Requisito | Fuente | Nivel |
|---|---|---|---|---|
| RNF-01 | Integridad | Las entidades no se borran: `activo` en `producto`, `categoria`, `modificador`, `etiqueta` e `idioma`; solo se borran relaciones y líneas de comandas abiertas; una comanda cerrada es intocable | R5 | 1 |
| RNF-02 | Integridad | `onDelete = RESTRICT` en todas las claves foráneas salvo `linea_modificador → linea_comanda` (`CASCADE`) | R11 | 1 |
| RNF-03 | Integridad | Importes en céntimos enteros; ningún precio negativo en los cuatro campos de precio. **Esta regla (R8) la garantiza el código, no la base de datos:** Room no declara `CHECK` ni índices parciales (verificación 9), así que la imponen los **repositorios** con una **validación pura**, cubierta por la prueba de código **P-C-09** | R8, verificación 9 | 1 |
| RNF-04 | Integridad | Las líneas congelan nombre y precio del producto y del modificador en el momento de pedir | R14 | 1 |
| RNF-05 | Integridad | Lo que se puede calcular no se guarda: ocupación de la mesa, total de la comanda, kcal, traducido/pendiente, con/sin nutrición | R3, R10, spec, apartado 4 | 1 |
| RNF-06 | Integridad | Unicidad: `producto.numero`, **`mesa.numero`** (P70), `categoria.nombre`, `etiqueta.nombre`, `idioma.codigo`; clave primaria compuesta en las tres N:M; `producto_nutricion` 1:1 con `producto`. Las mesas son 60 filas fijas precargadas: el `UNIQUE` no lo necesita el usuario, impide que una precarga mal hecha o una migración futura creen dos mesas con el mismo número | R9, spec, apartado 5 | 1 |
| RNF-07 | Seguridad | PIN de 4 dígitos guardado como hash **`PBKDF2withHmacSHA256`**, con **sal de 16 bytes** generada con `SecureRandom`, **100 000 iteraciones** y clave de **256 bits**, en las preferencias privadas de la app; **nunca en claro** | spec, apartado 9, P126 | 1 |
| RNF-08 | Seguridad | Todas las consultas parametrizadas (Room); nunca SQL concatenado | spec, apartado 9 | 1 |
| RNF-09 | Seguridad | Modo kiosco por screen pinning, sin permisos de administrador; salida con PIN | spec, apartado 9 | 2 · inc. 2 |
| RNF-10 | Seguridad | El Resumen de ingresos solo es accesible tras el PIN (vive en el Panel, no en Cuenta) | spec, apartado 9 | 1 |
| RNF-11 | Accesibilidad | Textos en `sp`, objetivos táctiles ≥ 48dp × 48dp, `contentDescription` en todo elemento interactivo, contraste ≥ 4,5:1 en texto y 3:1 en el resto | spec, apartado 8 | 1 |
| RNF-12 | Accesibilidad | Ninguna función depende de un gesto oculto: todo tiene botón visible (sin pulsado largo, sin deslizar, sin arrastrar). La **guía de accesibilidad de Android** no prohíbe los gestos: **exige que todo gesto tenga una alternativa accesible**. Esta app cumple por la vía más simple: no usa gestos ocultos, así que no necesita alternativa | Guía de accesibilidad de Android, spec, apartado 6 | 1 |
| RNF-13 | Accesibilidad | Ningún estado se comunica solo con color: *Eliminado*, *Eliminada*, *Desactivado* (nivel 2), *(base)*, texto en todos los botones | spec, apartado 8, apartado 14 | 1 |
| RNF-14 | Accesibilidad | Modo Agrandar: tema alternativo con tamaños mayores y más contraste | spec, apartado 8 | 2 · inc. 3 |
| RNF-15 | Interfaz | Orientación vertical fija y scroll vertical; desviación del guion justificada en la memoria (dispositivo fijado en la mesa) | spec, apartado 6 | 1 |
| RNF-16 | Interfaz | Adaptación a distintos tamaños de pantalla con layouts flexibles (`ConstraintLayout`, medidas en `dp` y `sp`) | spec, apartado 6 | 1 |
| RNF-17 | Interfaz | Paleta neutra con naranja como único acento; transiciones por defecto de Android; tres animaciones funcionales (sacudida del PIN, resaltado de fila, snackbar *Guardado*) | spec, apartado 14 | 1 (resaltado: 3; snackbar: 2) |
| RNF-18 | Rendimiento | Fotos como archivo (solo la ruta en la base de datos), redimensionadas a ~1080px y comprimidas a JPEG al guardar; carga con Glide, con caché y placeholder | spec, apartado 15 | 1 |
| RNF-19 | Localización | Interfaz en ES y EN (`strings.xml`), con caída a inglés para cualquier otro idioma; nombres de plato traducidos por tabla | spec, apartado 7 | 1 (tabla: 2 · inc. 10) |
| RNF-20 | Plataforma | Android nativo en Kotlin, XML + Activities/Fragments, Room, MVVM sencillo (ver la nota de stack, abajo) | spec, apartado 2 | 1 |
| RNF-21 | Plataforma | Versión mínima de Android: **8.0 (API 26), definitivo** — verificado el 17 de septiembre de 2026 contra la documentación oficial: `PBKDF2withHmacSHA256` está disponible desde API 26, Room 2.8 exige API 23 y Glide exige API 14, así que manda el PIN | Verificación 2, P63 | 1 |
| RNF-22 | Plataforma | Funciona sin conexión a internet: base de datos, fotos y carga de imágenes son locales (P65) | spec, apartado 15 | 1 |
| RNF-23 | Alcance | Monodispositivo, sin sincronización; la arquitectura cliente-servidor solo se diseña | spec, apartado 2 | 1 |
| RNF-24 | Fiabilidad | Las traducciones se guardan campo a campo, sin botón global; el carrito vive en memoria y se pierde si Android cierra la app (límite declarado) | spec, apartado 6, ficha 8 | 1 |
| RNF-25 | Mantenibilidad | Repositorio público en GitHub desde el primer día de la fase 6; componentes reutilizados (fila de plato, rejilla de mesas, desplegable) | spec, apartado 12, apartado 6 | 1 |

**Recuento: 25 RNF.**

### Nota a RNF-20 — decisiones de stack [Claude]

Sobre la base que fija RNF-20 (Kotlin, XML + Activities/Fragments, Room, MVVM sencillo), estas piezas concretas las propuso Claude y las aceptó Daniel; van marcadas **[Claude]** porque la memoria declara el uso de IA:

- **ViewBinding** para acceder a las vistas, en lugar de `findViewById` o DataBinding. **[Claude]**
- **Corrutinas + LiveData** para la capa asíncrona y la observación desde las Activities. **[Claude]**
- **Kotlin DSL** en los scripts de Gradle, con **catálogo de versiones** (`libs.versions.toml`). **[Claude]**
- **KSP** (no kapt) para el procesador de anotaciones de Room. **[Claude]**
- **Glide 5.0.x** para cargar las fotos desde archivo. **[Claude]**
- **Emulador Pixel 6 / API 34** como dispositivo de pruebas (no hay dispositivo físico). **[Claude]**
- **Material 3** como base de tema, con el **naranja** de acento de la paleta. **[Claude]**

**Sin límites de longitud** en nombres, descripciones ni códigos más allá de los que fijan las reglas (código de idioma: dos letras): la interfaz corta los textos largos con "…" (P64).
