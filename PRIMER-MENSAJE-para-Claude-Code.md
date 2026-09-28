# Primer mensaje para Claude Code

> Preparado en la fase 5b (24 de septiembre de 2026). **Cómo se usa:** abre Claude Code en tu PC (app de escritorio de Claude → pestaña *Código* → sesión **Local** → elige como carpeta del proyecto **`C:\AKO`**; o la consola dentro de esa carpeta) y pega **todo lo que hay debajo de la raya**, tal cual. **Antes de enviar, en el desplegable de permisos de la sesión elige el modo que acepta las ediciones de archivos sin preguntar** ("aceptar ediciones" o equivalente): el mensaje le pide crear un archivo, y si la sesión está en el modo que pide permiso para cada escritura se quedará parada esperando tu clic mientras duermes. No hace falta el modo que salta todas las confirmaciones. Está escrito en primera persona, con tu voz. Lo que decidas cambiar, cámbialo antes de pegar. `C:\AKO` es la carpeta del prototipo (decidida el 24 sep): aquí está la documentación y aquí se crearán el proyecto de Android Studio y el repositorio Git. Está fuera de OneDrive, que es lo recomendable para un proyecto de Android Studio (los `build/` generan miles de archivos que OneDrive intentaría sincronizar).
> Por qué en local y no en la nube (decidido en la 5b, P245 → A): la documentación oficial de Claude Code (code.claude.com/docs/en/claude-code-on-the-web, consultada el 24 sep 2026) dice que las sesiones en la nube **clonan un repositorio de GitHub**, no leen una carpeta del PC; y Android Studio y el emulador solo existen en tu PC. La nube puede entrar después, cuando exista el repositorio (S1), para tareas que no necesiten el emulador.

---

Hola. Vamos a construir juntos el prototipo de **Ako**, mi Proyecto Intermodular del ciclo de DAM (Desarrollo de Aplicaciones Multiplataforma). Antes de que hagas nada, lee esto entero.

## Qué es la app

**Ako** es una app Android nativa (Kotlin) de pedidos para bares y restaurantes. Corre en **un móvil del restaurante, en vertical**, que se deja en la mesa para que el cliente pida solo (modo kiosco) o lleva el camarero. Su rasgo diferencial es **una carta digital pensada para que pueda pedir cualquiera**: fotos, alérgenos, filtros, idiomas, letra grande. Tres roles: **Propietario** (crea la carta y ve el Resumen de ingresos, detrás de un PIN), **Pedir** (la carta: se elige mesa, se añaden platos al carrito y se envía la comanda) y **Cuenta** (ver lo pedido por cada mesa, quitar líneas, calcular el cambio y cobrar). Room local, sin conexión, monodispositivo. La arquitectura completa está diseñada como cliente-servidor en red local, pero **lo que se programa es el prototipo monodispositivo**: eso es un recorte de alcance declarado, no un olvido.

Es un **trabajo académico evaluable**, no un producto: trabajo escrito (70 %) y un vídeo de 10 minutos que es la defensa (30 %), sin preguntas en vivo. Entrega prevista: **la semana del 23 de noviembre de 2026** (límite oficial, 6 de diciembre). Criterio: **aprobar seguro antes que nota.** Donde nota y riesgo choquen, gana el riesgo.

## Quién soy y de dónde parto

**Es mi primer proyecto de programación.** Nunca he programado, nunca he abierto Android Studio, nunca he usado Git ni GitHub. **Empiezo de cero**, y necesito que lo tengas presente en cada paso: explícame las herramientas (dónde hacer clic, qué escribir, qué significa cada mensaje) y no solo el código. Si algo no lo entiendo, te lo diré y te pediré que me lo expliques otra vez; si a la tercera sigo sin entenderlo, explícamelo desde cero, más simple, sin dar nada por sabido.

Trabajo a rachas y me centro en una cosa cada vez. Una sesión puede ocupar más de un día. Contesto mejor a **preguntas numeradas con opciones (A, B, C) y tu recomendación marcada**, y muchas veces contesto solo con la letra.

Hay una regla que no se negocia, porque la normativa de mi centro la impone: **la IA puede apoyar, pero no puede sustituir mi trabajo, y el uso indebido es suspenso.** Eso significa: no me generes una funcionalidad entera de golpe; divide en piezas pequeñas, explícame cada una **antes** de escribir el código, y pregúntame si la he entendido antes de seguir. Tengo que poder explicar en el vídeo cualquier parte del código sin leerla. Todo lo que propongas tú y yo no haya pedido, márcalo con **[Claude]**, también en los comentarios del código. Las reglas completas están en `CLAUDE.md`.

## Dónde está toda la información

**Todo lo que necesitas para construir el prototipo está en esta carpeta.** Es el resultado de tres semanas de diseño (con Claude, en la app de Claude) y **está al día a 24 de septiembre de 2026**. Esta carpeta, `C:\AKO`, es **la carpeta del prototipo**: va a ser la raíz del repositorio Git, y el proyecto de Android Studio se creará aquí dentro, al lado de `docs/` y `CLAUDE.md`. Cómo se crea el proyecto de Android Studio dentro de una carpeta que ya tiene archivos es una de las cosas que tienes que explicarme en la sesión 1.

| Archivo | Qué es | Cuándo leerlo |
|---|---|---|
| `CLAUDE.md` | Las reglas de trabajo entre tú y yo, el stack fijo y el vocabulario obligatorio. Lo lees en cada sesión | **Primero** |
| `LEEME.md` | El índice de esta carpeta, con el orden de lectura | Segundo |
| `docs/spec-claude-code.md` | **El documento principal para ti**: qué se construye y en qué orden. Stack, estructura de paquetes, contrato de niveles (el nivel 1 es lo único obligatorio y es el guion del vídeo; el nivel 2 son 12 incrementos que entran solo si da tiempo), las 14 tablas Room, las 15 reglas de negocio y quién garantiza cada una en el código, seguridad (PIN con PBKDF2), imágenes, estilo, **las 13 sesiones del orden de construcción**, pruebas, diario y Git | **Tercero, entero** |
| `docs/spec+doc-pantallas.md` | Las fichas de las siete pantallas (1, 2, 3, 5, 6, 7 y 8; no hay pantalla 4): flujo, validaciones, casos límite, mensajes literales. Se programa lo que dice la ficha | Antes de programar cada pantalla |
| `docs/wireframes/` | Los 21 dibujos del nivel 1 (PNG) y `spec+doc-wireframes.md`, que lleva las 21 leyendas numeradas. Se dibuja lo que enseña el wireframe; si ficha y wireframe discrepan, manda el wireframe en disposición y la ficha en comportamiento, y me preguntas | Antes de programar cada pantalla |
| `docs/spec+doc-requisitos.md` | Los 53 requisitos funcionales (29 de nivel 1, 22 de nivel 2, 2 de nivel 3) y los 25 no funcionales | Al hacer el plan |
| `docs/spec+doc-clases.md` | Borrador del diagrama de clases (58 clases en cinco capas). **Los nombres son borrador**: si en el código uno cambia, se cambia también en el plan de pruebas; el diagrama definitivo saldrá del código real al final | Al hacer el plan y al empezar cada capa |
| `docs/spec+doc-pruebas.md` | El plan de pruebas: 29 manuales (P-M-01…29, una por RF de nivel 1) y 9 de código (P-C-01…09, JUnit). Las columnas de resultado están vacías: se rellenan al ejecutarlas | Al cerrar cada sesión |
| `docs/estado-nivel.md` | Tabla diseñado / implementado con los 53 RF, todos en *diseñado*. Se actualiza al cerrar cada sesión | Al cerrar cada sesión |
| `docs/plantilla-diario.md` | La plantilla de la ficha de diario. **Una ficha por sesión en `docs/diario/sesion-NN.md`**, rellenada al empezar y al terminar, con los problemas y su justificación en el momento | Al empezar y cerrar cada sesión |
| `docs/diario/` | Vacía: aquí van las fichas | — |
| `PRIMER-MENSAJE-para-Claude-Code.md` | Este mensaje | — |

El diseño lo decidí yo, pregunta a pregunta, y cada decisión tiene su número (P1, P2… hasta P248) y su motivo registrado. **No cambies el alcance, el modelo de datos ni una regla R1-R16 sin decírmelo antes.** Si encuentras una contradicción entre dos documentos, o algo que el diseño no cubre, **no lo resuelvas tú: pregúntamelo.**

## Qué quiero que hagas ahora — y una condición: voy a dejar el ordenador encendido y me voy a dormir

**No voy a estar delante para contestarte.** Así que en esta primera sesión **no me hagas ninguna pregunta**: ni en el chat ni con la herramienta de preguntas interactivas (la que me hace elegir entre respuestas ya hechas). Si la usas, la sesión se queda parada toda la noche esperando mi clic. **Cada pregunta que te surja la apuntas en el archivo y sigues.** Haz todo el análisis tú solo, déjalo escrito, y para. Mañana lo leo, contesto y seguimos.

**Lo primero que quiero ver mañana es el plan de todo el proyecto: las fases y el orden.** Para hacerlo bien tienes que leer antes toda la documentación, pero eso no se ve; lo que se ve es el archivo, y **el plan va al principio del archivo**.

1. **Lee todo, entero y con calma**: `CLAUDE.md`, `LEEME.md` y `docs/spec-claude-code.md` completos; después el resto de `docs/` (fichas, wireframes con sus leyendas, requisitos, clases, plan de pruebas, estado del nivel, plantilla del diario). No te saltes nada: el diseño está repartido entre los documentos y las reglas cruzan de uno a otro.
2. **Escribe un solo archivo, `C:\AKO\ANALISIS-INICIAL.md`**, en español, con estas siete partes, **en este orden** (el plan primero, las preguntas las últimas):
   - **A. El plan de todo el proyecto**, de aquí a la entrega (semana del 23 de noviembre de 2026): **las fases, en orden, y qué se hace en cada una** — nivel 1 del prototipo (13 sesiones), memoria (el trabajo escrito), pasada de apropiación, nivel 2 solo si queda tiempo, vídeo y entrega. Con una estimación de tiempo realista para alguien que empieza de cero, por fase, y **marcando qué partes del plan cambian según lo que yo conteste en G**.
   - **B. El plan del prototipo en detalle, sesión por sesión**: para cada una de las 13 sesiones, qué se construye, qué me vas a explicar, qué me tendré que aprender para poder defenderlo, y qué prueba la cierra.
   - **C. Lo que has entendido**, con tus palabras: qué es la app, qué es el nivel 1, cómo son las 14 tablas y las 15 reglas, cómo están repartidas las capas. Es para que yo compruebe mañana que lo has entendido bien.
   - **D. Contradicciones, huecos y dudas** que hayas encontrado entre documentos o cosas que el diseño no cubre. Cada una con el documento y el apartado donde está. **No las resuelvas tú**: solo apúntalas.
   - **E. Propuesta** de qué hay que instalar en mi PC (Android Studio, emulador, Git… con versiones y en qué orden), de cómo vamos a trabajar sesión a sesión (en local en mi PC; la sesión en la nube de Claude Code solo clona repositorios de GitHub, así que como mucho entra cuando exista el repositorio y para tareas sin emulador), y de cómo aprovechar los 250 $ de crédito de Claude Code en la nube que tengo. Solo propuesta: lo decidimos mañana.
   - **F. Skills y plugins.** Dime si para este proyecto nos conviene **crear una skill o un plugin de Claude Code** (por ejemplo, para el ritual de cada sesión: ficha del diario, `estado-nivel.md`, commit) o **instalar alguno que ya exista** (de Android, Kotlin, Gradle, Git…). Para cada uno: qué haría, qué nos ahorra, qué cuesta ponerlo, y tu recomendación. Yo no sé cuáles existen ni cuándo compensan: dímelo tú, aunque yo no lo haya pedido. Solo propuesta: lo decidimos mañana.
   - **G. Todas las preguntas que necesitas hacerme, al final del archivo**, numeradas (P1, P2…), cada una con opciones A/B/C en su propia línea y tu recomendación marcada con el motivo en una línea. Sobre el plan, sobre el diseño, sobre mi PC, sobre mi nivel, sobre cómo trabajar, sobre las partes E y F. Las contesto mañana, en el chat, con la letra o con mis palabras.
3. **Lo que NO haces esta noche:** no me preguntas nada, no escribes ni una línea de código, no instalas nada, no creas el proyecto de Android Studio, no inicializas Git, no creas ni cambias ningún archivo que no sea `ANALISIS-INICIAL.md`.
4. Cuando termines el archivo, **para** y escribe en el chat un resumen de tres líneas: cuánto tiempo estimas para todo el proyecto y para el nivel 1, cuántas preguntas tienes en G y cuántas contradicciones has visto en D.

**Mañana, en este orden y sin saltarse ninguno:** (1) leo el archivo; (2) **discutimos el plan del proyecto** — las fases, el orden, los tiempos — hasta que yo lo tenga claro y pueda contarlo con mis palabras; (3) **contesto tus preguntas y te hago las mías**, las que hagan falta; (4) **decidimos juntos qué hacemos y cómo** (instalación, forma de trabajo, skills). **La sesión 1 no empieza hasta que hayamos cerrado esos cuatro puntos.** Cuando empiece, será por el principio y explicándome cada paso: qué instalo, cómo creo el proyecto de Android Studio en esta carpeta, qué es cada cosa que aparece en pantalla, qué escribo y por qué.

Esta noche: lee y escribe el archivo. Nada más.
