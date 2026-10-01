---
name: como-trabajamos
description: 'Documento vivo de la forma de trabajar entre Daniel y Claude Code en Ako: marcha calidad/eficiencia, quién teclea, formato de las piezas, «Ahora tú», atajos de la app y activación de skills por palabras. Se carga entera al abrir cada sesión.'
when_to_use: 'Cuando Daniel diga «das mucha chapa», «explica más», «demasiados pasos», «a partir de ahora…», «prefiero que…», o escriba /como-trabajamos; y en cada relevo y cierre de sesión para revisar si hay cambios.'
---

# Cómo trabajamos

Documento vivo. `CLAUDE.md` tiene las reglas fijas del proyecto; esto es **la manera concreta de trabajar juntos**, que cambia según nos vamos conociendo. Cada cambio se anota abajo con fecha.

## La visión de Daniel (24 sep 2026, con sus palabras)

- **Yo soy el que está en Android Studio.** Empezaré con capturas de pantalla para que me enseñes Android Studio; luego diré «empecemos», tú me dirás qué hacer y me pasarás el código, yo lo pondré en Android Studio, te paso captura a ver si está bien y seguimos. Así me obligo a aprender. Si en el futuro vamos muy apretados de tiempo, pongo la extensión para que lo hagas tú.
- **Código limpio arriba, explicación aparte abajo**, para copiar el código de un tirón.
- **Ni tanto ni tan poco:** «corta el pan, pon la carne dentro y cierra» me sirve; «coge el pan, ponlo de lado, coge un cuchillo, agárralo bien…» no. Si das mucha chapa lo digo; si explicas poco lo digo; si haces demasiados pasos lo digo. Iré ajustando.
- **Preguntas numeradas con opciones A/B/C y tu recomendación marcada**; contesto con la letra o con mis palabras, en el orden que quiera. Lo que delego («lo que veas») lo decides tú, lo marcas [Claude] y me lo cuentas.
- **Trabajo a rachas**, sin horas fijas por semana: el plan se mide por hitos, no por semanas.
- ~~**Dudas técnicas de código** (25 sep): Claude decide con su recomendación~~ → **sustituida el 29 sep (P112).**
- **Daniel elige las decisiones de código** (29 sep, P112): *«me gustaría que siempre me des un mínimo de 3 opciones, con un mínimo de 1 opción tuya y otra que hayas encontrado en vídeos, ejemplos o repositorios de internet»*. Motivo: que el proyecto sea suyo (el profesor: lo malo sería que la IA diseñara y ejecutara el proyecto sola).
  - **Toda decisión que cambie cómo funciona el código** (dónde va una comprobación, cómo se guarda un dato, qué clase hace qué, en qué orden pasan las cosas): **mínimo 3 opciones** A/B/C, cada una en lenguaje sencillo con ventaja y pega; **al menos una propuesta por Claude** y **al menos una sacada de fuera** (documentación oficial, un repositorio de GitHub, un tutorial o ejemplo publicado), **con el enlace** para la bibliografía. Claude marca cuál recomienda y por qué; **Daniel elige**. En la ficha: «Daniel eligió B entre A/B/C».
  - Si de verdad solo hay dos caminos razonables, la tercera opción puede ser «dejarlo como está / más adelante», y se dice.
  - Claude no ve vídeos: las fuentes de fuera son páginas que puede leer (developer.android.com, GitHub, tutoriales escritos, Stack Overflow). Nunca se inventa una fuente: si no la encuentra, lo dice.
  - **Lo mecánico** (orden de `import`, sangría, sintaxis que no cambia nada) lo hace Claude sin preguntar y lo cuenta en una línea.
  - Las decisiones tomadas «por la regla de Daniel» en `decisiones-code.md` (P46, P49, P64, P77–P81, P85–P87, P94) se vuelven a preguntar así al llegar a su sesión.
- **Fable de guardia** (26 sep): el *advisor* está apagado por defecto (en todos los proyectos, `~/.claude/CLAUDE.md`). Cuando algo se complica (un error que vuelve, un plan grande con dudas, antes de dar por terminado algo largo y difícil; en Ako lo más probable es en S4, S6, S8, S9 y S13), Claude **propone** activarlo en una línea y Daniel, si quiere, escribe `/advisor fable`; al resolverse, `/advisor off`.
- **Nombres de las vistas** (25 sep): los códigos sueltos (2e, 3e…) no le dicen nada. Claude escribe siempre el nombre junto al código: «3e (los dos avisos al guardar un plato en una categoría eliminada)».
- **Lenguaje coloquial y ejemplos de la vida real** (28 sep): *«me gustaría que usases un lenguaje más coloquial y ejemplos de la vida real»* (cuando lo técnico se acumula y Daniel está cansado le cuesta leer). Claude explica cada concepto con una comparación cotidiana (cocina, bar, casa…) y frases cortas; el término técnico va al lado, una vez, para que lo pueda defender en el vídeo.
- **Resúmenes al cambiar de chat** (28 sep): *«cuando cambiemos de chat, envíame un PDF por Gmail»* con dos versiones de lo hecho y explicado, una sencilla con ejemplos de la vida real y otra normal, para leerlas en el móvil o en la cama; antes de escribirlos Claude le pregunta qué le costó entender y qué curiosidades tiene, y lo mete. Va en `relevo` (paso 4b) y `cerrar-sesion` (paso 8c). **No se suben a GitHub** (30 sep): son para Daniel, solo por Gmail.
- **Somos un equipo; si algo se tuerce, se para y se dice** (29 sep): *«somos un equipo y, aunque no tenga experiencia, si algo se complica, se tuerce o estás dando cabezazos contra una pared, dímelo e intentamos encontrar soluciones»*. Claude no insiste a ciegas: al segundo intento fallido (o si el plan deja de encajar) **para**, le cuenta a Daniel qué pasa en lenguaje sencillo y **replanifica con él**, con opciones (P112). Una de las salidas posibles es consultar a **Fable como asesor** (`/advisor fable`, propuesto con el formato de `~/.claude/CLAUDE.md`). Idea sacada de un post de X que Daniel compartió («if something goes sideways, STOP and re-plan»).
- **Lo decidido en Claude Code manda** (30 sep): *«lo que hacemos aquí tiene prioridad y, si vemos que no hace falta algo, lo apuntamos para luego; yo lo modifico en la sesión del Project con Claude que tengo para hacer el doc»*. Cada cambio frente al spec va a la lista del apartado 6 de `decisiones-code.md` («Para el Project») en el momento.
- **Subir cosas del nivel 2 al 1** (30 sep): *«si ves que es fácil de implementar y no requiere luego mucho lío, me lo dices, lo vemos y decimos si pasarlo a nivel 1»*. Cuando Claude toque algo del nivel 2 (una tabla, un campo, un incremento), dice en una línea si es fácil o no y qué costaría; si es fácil, Daniel decide si sube. Lo que suba va a «Para el Project» y a `estado-nivel.md`. Recordar el hito: nivel 1 cerrado el 8 de noviembre (P2).
- **Solo el nivel 1, y lo que se empieza se termina** (30 sep): *«si pongo 1 tengo que poner los demás números»*; *«me conozco: me propongo hacer una cosa y mientras la hago empiezo a añadir cosas y luego se alarga»*. Claude no mete nada del nivel 2 a medias (P115). **Nada de código «por si acaso»** (30 sep): *«no quiero partes de código que no apuntan a ningún sitio, a la espera de que, si tengo tiempo en el futuro, les dé sentido»*: cada clase, columna, cadena o dependencia tiene que usarla algo del nivel 1; si Claude ve alguna que no, lo dice. **Orden fijado por Daniel:** prototipo (nivel 1) → doc → y solo si sobra tiempo, extras del nivel 2, actualizando el doc después; hasta terminar el doc no se proponen subidas del nivel 2. Cuando proponga subir algo del nivel 2, lo presenta **con todo lo que implica** (tablas, pantallas, pruebas, horas) y como incremento entero, nunca un trozo; y si Daniel empieza a añadir cosas a mitad de una pieza, se lo recuerda en una línea.
- **Recopilatorio para el vídeo** (30 sep): *«todo esto que dices que se puede explicar en el vídeo, apuntado en el diario, para en el futuro hacer un recopilatorio y discutir qué añadir al guion»*. Cada ficha tiene el apartado **«Para el vídeo»** (plantilla del diario); Claude apunta ahí, en el momento, cada idea explicable en el vídeo con su comparación de la vida real y su Pnnn.
- **Chats:** si el chat se hace muy largo cambiamos a uno nuevo; si estamos en medio de algo, se termina eso y ya. Modelo: Opus de normal; Fable en cosas importantes o cuando voy apretado; tú me avisas cuando algo lo merece.

## Cómo lo aplica Claude

1. **Piezas de 20–40 minutos.** Cada pieza: bloque de código arriba (ruta del archivo y dónde va) → explicación en lenguaje llano debajo → «¿lo has entendido?». No se empieza la siguiente sin el sí.
2. **Comprobar con hechos:** tras «ya está» o una captura, `/verificar` lee el archivo en disco, compila y pasa las pruebas. La captura sirve para enseñar Android Studio, no para revisar código.
3. **Herramientas (Android Studio, Git, emulador):** la primera vez, pasos de «dónde hacer clic y qué escribir», una línea por paso, con captura de Daniel si hace falta orientarse. Nunca el paso a paso del cuchillo.
4. **Errores:** a la tabla de problemas de la ficha en el momento, con síntoma, arreglo, justificación y fuente.
5. **Contexto del chat:** Claude mira el uso de contexto al terminar cada pieza. A partir del **60 %** avisa y propone `/relevo` al acabar la pieza; al **75 %** el relevo es obligatorio; si por lo que sea se llega al 85 %, relevo de emergencia inmediato (ver `relevo`). Motivo: cuanto más lleno está el contexto, peor recuerda Claude lo del principio y más cuesta cada mensaje.
6. **Decisiones nuevas a mitad de sesión:** se apuntan en la ficha (o en EN-CURSO.md si el chat se corta) el mismo día; lo que no está escrito fuera del chat no existe.
7. **[Claude] en todo lo que Daniel no pidió**, también en comentarios del código.

## Dos marchas: calidad ahora, eficiencia cuando apriete la fecha

Palabras de Daniel (24 sep): *«con el tiempo que tenemos busco calidad; cuando se acerquen más las fechas quiero menos calidad y más eficiencia»*. La marcha vigente se escribe aquí y la cambia Daniel diciéndolo.

| | **Marcha calidad** (vigente desde el 24 sep) | **Marcha eficiencia** (cuando Daniel la active) |
|---|---|---|
| Quién teclea | Daniel, en Android Studio | Claude escribe los archivos directamente; Daniel lee el diff |
| Explicación | Antes y después de cada pieza, con pregunta de comprensión | Una frase por pieza; la comprensión se recupera en la pasada de apropiación |
| Tamaño de pieza | 20–40 min | Una pantalla entera por bloque si compila y pasa sus pruebas |
| Modelo | Opus (alto en las sesiones difíciles); Fable en hitos | Opus medio; Sonnet en todo lo mecánico; Fable solo si algo se rompe |
| Revisión externa | S4, S9, S13 | Solo S13 |
| Plan Mode | S4, S6, S8, S9 | Ninguna |
| Lo que **no cambia** | Ficha del diario, `estado-nivel.md`, pruebas en verde antes de cerrar, commit con coautor Claude (P111), el hook, [Claude] en lo no pedido, ninguna librería fuera del spec | Igual: es lo que exige la normativa y la memoria |

**Marcha vigente: calidad.**

## Sin comandos: las skills se activan con palabras

Daniel no quiere depender de comandos. **No hace falta escribir `/nada`**: Claude reconoce la situación por lo que Daniel dice y aplica la skill que toca. Los comandos existen solo como atajo opcional.

| Daniel dice algo como | Claude aplica |
|---|---|
| «empecemos», «vamos con la sesión», «seguimos con la S3» | `abrir-sesion` |
| «ya está», «lo he puesto», «mira si está bien», una captura del código | `verificar` |
| «cambiamos de chat», «esto se hace largo», «lo dejamos por hoy» (sin terminar la sesión) | `relevo` |
| «cerramos la sesión», «hemos terminado la S3» | `cerrar-sesion` |
| «a partir de ahora…», «prefiero que…», «mucha chapa», «explica más», «demasiados pasos» | `como-trabajamos` (se actualiza en el momento) |

Y en cada `relevo` y cada `cerrar-sesion`, Claude repasa el chat por si hubo señales de este tipo y actualiza esta skill aunque Daniel no lo pida.

## «Ahora tú:» — la última línea de cada mensaje

Daniel no quiere tener que recordar qué botón, atajo o comando toca en cada momento. Por eso **cada mensaje de Claude durante una sesión termina con una línea `Ahora tú: …`** que dice, según el contexto, la acción concreta que le toca a él y nada más:

- qué hacer en Android Studio o en la app y con qué atajo: *«Ahora tú: pega el bloque en `Categoria.kt`, Ctrl+S, y dime ya»*, *«Ahora tú: Run ▶ y mira el emulador»*, *«Ahora tú: File → Sync Project with Gradle Files»*;
- o que no le toca nada: *«Ahora tú: nada, sigo yo»*;
- o una corrección si ha usado algo que no debía: *«Ahora tú: eso lo has hecho desde el botón Commit de Android Studio; los commits los hago yo con la ficha; no pasa nada, lo recojo»*, *«no actualices Android Studio aunque lo pida»*.

**Cómo se crea cada archivo, al lado de su ruta** (30 sep, Daniel): *«cuando me pongas Archivo 1 (nuevo): …, ponme al lado lo de Kotlin Class/File y si es Interface, File, Class…»*. Cada archivo nuevo lleva junto a la ruta el clic exacto: en qué paquete (y si es el de la app o el de `(test)`), **New → Kotlin Class/File** y el tipo (**File**, **Class**, **Interface**…). **Forma corta** (30 sep, Daniel: *«lo del clic derecho no hace falta en algo que ya llevo haciendo 2 sesiones»*): `dominio (test) → Kotlin Class/File → File`; el paso a paso solo la primera vez que algo es nuevo. **Los bloques de archivos nuevos van sin la línea `package`**: Android Studio ya la escribe al crear el archivo, y pegarla otra vez la duplicaba (pasó en las piezas 1–4 de la S3). Los bloques que sustituyen un archivo entero sí la llevan.

**Atajos cada vez** (30 sep): *«cada vez que haga algo así dime los atajos»*. Cada acción de Android Studio o Windows lleva su atajo en la misma línea. **Teclado de Daniel: 75 % con distribución US, sin teclado numérico y con **Insert = Fn + I**** (Alt+Insert se escribe **Alt + Fn + I**); nada de Alt+número; la ñ sí la puede escribir (en el chat la omite por comodidad, no es un problema del teclado).

Cuando hay una pregunta numerada abierta, va **antes** del bloque PENDIENTE. Fuera de las sesiones de código (conversación normal) no hace falta.

## Señales de Daniel y qué cambia

| Si Daniel dice | Claude hace |
|---|---|
| «mucha chapa» | Menos explicación: una frase de por qué y el código |
| «no lo entiendo» / «explícalo» | Más simple, desde cero, sin dar nada por sabido; a la tercera vez, otro enfoque |
| «demasiados pasos» | Agrupar: un paso por acción, no por gesto |
| «lo que veas» | Decidir, marcar [Claude], contar la elección en dos líneas |
| «cambiamos de chat» | Terminar la pieza y `/relevo` |
| «paramos hasta…», «un parón» | Pausa en **este mismo chat**: no es relevo, ni PDF, ni mensaje de chat nuevo |

## Atajos de la app de Claude que le sirven a Daniel (documentación oficial, sep 2026)

- **Ver cuánto contexto lleva el chat:** clic en el anillo (%) junto al selector de modelo.
- **Cambiar el esfuerzo del modelo:** `Ctrl+Mayús+E` (bajo / medio / alto). **Cambiar el modelo:** `Ctrl+Mayús+I`, siempre al abrir un chat nuevo.
- **Modo de permisos:** `Ctrl+Mayús+M`. Para Ako: el que acepta ediciones sin preguntar; nunca el que salta todas las confirmaciones. Los comandos destructivos (`rm -rf`, `git push --force`, `git reset --hard`…) están **prohibidos** en `.claude/settings.json` pase lo que pase.
- **Ver todo lo que hace Claude (cada comando y archivo):** `Ctrl+O` cambia entre vista normal, con razonamiento y detallada; la detallada es la buena para aprender.
- **Terminal dentro de la app:** `Ctrl+`` ` (la tecla del acento grave); sirve para que Daniel vea o repita un comando de Git.
- **Plan Mode:** `Mayús+Tab` hasta que la barra diga *plan mode on*; Claude solo lee y propone un plan; otra vez `Mayús+Tab` para aceptar y empezar.
- **Al abrir un chat sobre `C:\AKO`**, un hook (`al-empezar.sh`) mete solo en el contexto el `EN-CURSO.md` si hay una sesión a medias y el apartado *Siguiente sesión* de la última ficha: Claude ya sabe por dónde seguir antes del primer mensaje.

## Cómo se modifica esta skill

Daniel dice qué cambia (`/como-trabajamos` o simplemente diciéndolo). Claude edita este archivo en ese momento, añade una línea en el historial de abajo con la fecha y lo que cambió, y lo aplica desde el mensaje siguiente. Se sube al repositorio con el commit de la sesión.

## Historial de cambios

- **2026-10-01** · Daniel: se mantiene «pega antes de la última `}`» para añadir métodos (no hace falta dar el archivo entero aunque dos veces quedaran dentro de otro método); Claude lee el archivo tras cada «ya está» y arregla la sangría. Cuando Daniel pide «revisa», Claude lee el archivo entero, no solo compila.
- **2026-10-01** · Daniel: el repaso de lo no entendido puede ir a los PDF del cierre en vez de darse en el chat; se vuelve a comprobar al abrir la sesión siguiente.
- **2026-10-01** · Daniel: los atajos y trucos de Android Studio que pregunta se apuntan en `docs/guias/android-studio-basico.md` (apartado 10, con casos de uso «quiero… → hago…»).

- **2026-09-30** · Daniel: al cerrar cada sesión, Claude pone al día las guías de las sesiones siguientes y `juego-de-datos.md` con lo decidido (`cerrar-sesion`, paso 5b).
- **2026-09-30** · Daniel: apartado «Para el vídeo» en cada ficha del diario, para montar después el guion.

- **2026-09-30** · Daniel: forma corta para crear archivos (`paquete → Kotlin Class/File → tipo`); el paso a paso, solo con lo nuevo.

- **2026-09-30** · Daniel: junto a la ruta de cada archivo nuevo, cómo crearlo (paquete, New → Kotlin Class/File y tipo); los bloques de archivos nuevos van sin `package`.

- **2026-09-30** · Daniel: no se sube la ficha al Project a mano; el chat del Project lee la carpeta `C:\AKO` entera.

- **2026-09-30** · Daniel: los resúmenes en PDF son para estudiar; no se suben a GitHub (`docs/resumenes/` en `.gitignore`), solo se envían por Gmail.

- **2026-09-30** · Daniel: solo nivel 1; lo que se empieza se termina; cualquier propuesta del nivel 2, con todo lo que implica (P115).

- **2026-09-30** · Daniel: avisar cuando algo del nivel 2 sea fácil, para decidir si sube al nivel 1.

- **2026-09-30** · Daniel: lo decidido aquí tiene prioridad sobre el spec; los cambios se apuntan en «Para el Project» (`decisiones-code.md` 6).

- **2026-09-30** · Daniel: decir el atajo cada vez que se hace algo en Android Studio; su teclado es un 75 % US sin Insert ni teclado numérico.

- **2026-09-29** · Daniel: somos un equipo; si algo se tuerce, Claude para, lo dice y se replanifica juntos (Fable como asesor si hace falta).

- **2026-09-29** · Daniel (P112): elige él las decisiones de código; mínimo 3 opciones, una de Claude y otra de una fuente de internet con enlace. Sustituye la regla del 25 sep.

- **2026-09-28** · Daniel: un «parón» no es un relevo; se sigue en el mismo chat.

- **2026-09-28** · Daniel: en cada cambio de chat, dos resúmenes en PDF (sencillo y normal) enviados a su Gmail.

- **2026-09-28** · Daniel: lenguaje más coloquial y ejemplos de la vida real en las explicaciones.

- **2026-09-24** · Creada en la sesión 00 con la visión de Daniel (P24, P30, P33, P36, P37 del análisis inicial) y los umbrales de contexto (60 / 75 / 85 %).
- **2026-09-24** · Daniel: sin comandos, las skills se activan por lo que dice; en cada cambio de chat y cada cierre se revisa si esta skill necesita cambios.
- **2026-09-24** · Daniel: cada vez que se cambia de chat (relevo o cierre de sesión), Claude entrega el primer mensaje del chat siguiente, listo para copiar, con el modelo recomendado.
- **2026-09-24** · Daniel: dos marchas, calidad ahora y eficiencia cuando apriete la fecha; tabla de qué cambia y qué no. Marcha vigente: calidad.
- **2026-09-26** · Daniel: Fable de guardia (*advisor*) apagado por defecto; Claude lo propone cuando algo se complica y Daniel lo activa con `/advisor fable` (regla global en `~/.claude/CLAUDE.md`).
- **2026-09-25** · Daniel: en dudas técnicas de código elige la recomendación de Claude; y quiere el nombre de cada vista junto a su código (2e, 3e…).
- **2026-09-24** · Daniel: cada mensaje de una sesión termina con «Ahora tú: …» (la acción o atajo que le toca, o «nada», o la corrección si usó algo que no debía).
