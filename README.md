# Ako

Ako es una app Android para tomar pedidos en un bar o un restaurante. Es mi Proyecto Intermodular del ciclo de DAM, curso 2026-2027. La hice yo, Yunkang Daniel Huzhou, con Claude Code de apoyo; abajo explico cómo.

Funciona en un solo móvil, en vertical y sin internet (la app no pide el permiso de Internet). Al abrirla hay tres botones:

Propietario. Pide un PIN de cuatro cifras. Desde ahí se hace la carta (categorías y platos, con su precio, sus alérgenos y su foto) y se mira el resumen de ingresos de cada día.

Pedir. Eliges la mesa, miras la carta, añades platos al carrito y envías la comanda. Para salir hay que poner el PIN.

Cuenta. La rejilla de las 60 mesas. En cada mesa roja se ve lo pedido con su total; se pueden quitar líneas, anular, sacar el recibo, calcular el cambio y cobrar.

Es un prototipo de clase, no un producto. El nivel 1 del diseño está terminado y probado (8 de octubre de 2026, sesión 13). El nivel 2 y la versión cliente-servidor en red local están diseñados pero no programados. En docs/estado-nivel.md está qué hay hecho y qué no.

## Con qué está hecho

Kotlin. Android nativo con vistas XML (Activities y Fragments, ViewBinding; nada de Compose). Room sobre SQLite. MVVM sencillo: ViewModel y repositorio, con corrutinas y LiveData. Glide para las fotos. minSdk 26 (Android 8.0), compileSdk y targetSdk 37. Las versiones de las librerías y de los plugins están en app-ako/gradle/libs.versions.toml; los SDK, en app-ako/app/build.gradle.kts.

## Cómo abrirlo

1. Android Studio que admita la versión del plugin de Android (AGP) que marca libs.versions.toml (yo usé Android Studio 2026.1), con la plataforma 37 del SDK y una imagen de emulador de API 26 o superior.
2. File → Open → la carpeta app-ako (no la raíz del repositorio).
3. Esperar a que termine de sincronizar Gradle.
4. Un emulador Pixel 6 con API 34, o cualquier Android 8.0 o superior.
5. Run. La primera vez pide crear el PIN del Propietario. Si se olvida no se puede recuperar: hay que reinstalar y se pierde todo (la carta, las comandas y las fotos).

## Pruebas

Desde la carpeta app-ako (en Windows con PowerShell o Git Bash; en macOS o Linux, ./gradlew):

- ./gradlew.bat testDebugUnitTest pasa las pruebas de código sin emulador (app/src/test).
- ./gradlew.bat connectedDebugAndroidTest pasa las de la base de datos (Room en memoria, app/src/androidTest); hace falta el emulador encendido.

Las pruebas manuales están en docs/spec+doc-pruebas.md: 29 casos, uno por requisito del nivel 1, todos pasados el 8 de octubre de 2026. El detalle de cada pasada está en docs/pruebas-pasadas y las capturas del emulador en docs/capturas. Cómo montar los datos para repetirlas: docs/guias/juego-de-datos.md.

Hay 40 pruebas de código, 29 en test y 11 en androidTest, y todas pasan (9 de octubre de 2026).

## Cómo está organizado

app-ako es el proyecto de Android Studio. El paquete yunkang.ako va por capas: datos (tablas, consultas y repositorios), dominio (los cálculos que se prueban sin emulador, el carrito y los modelos que llegan a las pantallas), seguridad (el PIN, guardado como hash con sal, nunca en claro), imagenes (las fotos) y ui (las pantallas). En la raíz del paquete está EntradaAko, la Application: crea la base de datos y los repositorios.

docs es el diseño y el seguimiento: spec-claude-code.md (el diseño y el orden de construcción), las fichas de pantalla, los wireframes, los requisitos, el diagrama de clases, el plan de pruebas, estado-nivel.md, decisiones-code.md, textos-ui.md (los textos de la interfaz en español e inglés), guias (una por sesión), diario (una ficha por sesión), pruebas-pasadas, capturas, revisiones (las revisiones con IA y lo que se aplicó), para-el-project (lo que paso a la memoria) y lecciones-claude.md.

CLAUDE.md y .claude son las reglas y las herramientas con las que trabajé con Claude Code.

## Cómo usé la IA

El código lo hice con Claude Code (Anthropic) de apoyo, con las reglas de CLAUDE.md: yo pedía cada pieza, Claude la explicaba y me daba el código, y yo lo tecleaba en Android Studio (o lo revisaba, cuando en algún tramo lo escribió él directamente; cada ficha del diario dice cuál de las dos cosas pasó), lo probaba y contestaba preguntas de comprensión antes de seguir. En cada ficha de docs/diario está qué pedí yo, qué generó la IA y qué revisé, tecleé o cambié. Desde el 29 de septiembre de 2026 los commits en los que trabajó Claude Code llevan la línea Co-Authored-By: Claude. Lo que propuso la IA sin que yo lo pidiera lleva la marca [Claude].

Lo consulté con el profesor del módulo antes de empezar a programar y lo aprobó (29 de septiembre de 2026).

## Licencia

Trabajo académico. Solo para la evaluación.
