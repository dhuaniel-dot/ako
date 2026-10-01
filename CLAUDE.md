# Ako — app Android de pedidos para bares y restaurantes

Proyecto Intermodular de DAM. **Es mi primer proyecto de programación**: nunca he usado Android Studio ni Git antes de este repositorio. Es un trabajo académico evaluable, no un producto. **Criterio: aprobar seguro antes que nota.** Entrega: semana del 23 de noviembre de 2026.

## El objetivo es el prototipo (Daniel, 30 sep 2026)

1. **Se construye solo el nivel 1.** Orden fijado: **prototipo (nivel 1) → el doc (memoria) → y solo si sobra tiempo, extras del nivel 2**, cada uno entero, actualizando el doc después. Hasta terminar el doc no se propone nada del nivel 2 (sustituye a P3).
2. **Nada de código «por si acaso».** Cada clase, tabla, columna, cadena o dependencia la usa algo del nivel 1 hoy. La base de datos tiene **7 tablas** (P115); lo del nivel 2 llega entero con su incremento.
3. **Lo que se empieza se termina.** Si surge una idea a mitad de una pieza, se apunta y se sigue con la pieza.
4. **El diseño del nivel 2 no se borra** de ningún documento: se conserva para después.
5. **Lo decidido aquí manda sobre el spec y sobre el Project «Proyecto Intermodular» de la app de Claude** (donde se planificó todo y se escribe el doc): el doc explica el prototipo. Cada cambio va a «Para el Project» (`docs/decisiones-code.md`, apartado 6); Daniel le pide al chat del Project que lo lea y corrija el doc.

## Lee esto antes de cada sesión

1. **`docs/spec-claude-code.md`** es el diseño completo y el orden de construcción. Manda sobre cualquier idea tuya. Léelo entero la primera vez; después, el apartado de la sesión.
2. **`docs/diario/`** tiene una ficha por sesión. Lee la última antes de empezar: ahí dice qué quedó a medias y qué no entendí.
3. **`docs/estado-nivel.md`** es la tabla diseñado/implementado. Se actualiza al cerrar cada sesión.
4. Las fichas de pantalla (`docs/spec+doc-pantallas.md`), los wireframes (`docs/wireframes/`) y el plan de pruebas (`docs/spec+doc-pruebas.md`) están en el repositorio. Cuando una pantalla se programa, se mira su ficha y su wireframe, no se inventa.

## Cómo trabajamos — reglas, no sugerencias

1. **Nunca generes una funcionalidad completa de golpe.** Divide en piezas pequeñas y explica en lenguaje sencillo qué hace cada una y por qué **antes** de escribir el código.
2. **Tengo que entender cada línea que se quede en el proyecto.** Si algo es complejo, simplifícalo o coméntalo, aunque sea menos elegante. Prefiero código repetido y claro a código listo que no sé explicar.
3. Cuando termines una pieza, **pregúntame si la he entendido** antes de seguir.
4. **No implementes nada que no te haya pedido** ni que no esté en el spec. Si crees que falta algo, dímelo y decido yo.
5. Ante una decisión de diseño que el spec no cubra, **pregunta**. No asumas.
6. Cuando aparezca un error, documenta **qué pasó, por qué y cómo se resolvió** en el momento: va a la ficha del diario.
7. **Nombres y comentarios en español** para el código de negocio (`Comanda`, `guardarPlato`, `precioCentimos`), salvo convenciones estándar de Android/Kotlin (`onCreate`, `ViewModel`, `Dao`).
8. **Explica también las herramientas.** La primera vez que haga falta algo de Android Studio, del emulador o de Git, dime dónde hacer clic o qué escribir, con el nivel de detalle de la regla 12. No lo he visto nunca.
9. **Los commits de Claude Code llevan la línea `Co-Authored-By: Claude`** (P111, 29 sep 2026, tras el visto bueno del profesor; sustituye a P226): se pasa en el commit con un segundo `-m` (ver `cerrar-sesion`, paso 7); el ajuste `attribution` de `.claude/settings.json` solo la añade cuando Claude Code arma el commit con su propio formato (revisión del 1 oct, F05). El autor del commit sigue siendo Daniel. El uso de IA se declara además en la memoria y en cada ficha del diario.
10. **Cada sesión tiene un objetivo de una línea** (del orden de construcción del spec) y termina con: las pruebas que tocan pasando, la ficha del diario rellena, `docs/estado-nivel.md` actualizado y un commit con push. Si no se llega, se dice en la ficha por qué.
11. Marca lo que propones tú y yo no había pedido con **[Claude]**, también en los comentarios del código.
12. **Nivel de detalle y formato de cada pieza.** Los pasos, al nivel de «corta el pan, pon la carne, cierra»: ni un solo «haz el bocadillo» ni el paso a paso del cuchillo. Cuando haya código: **arriba el bloque de código limpio y completo, con la ruta del archivo y dónde va; debajo la explicación en lenguaje llano; al final la pregunta de comprensión.** Yo tecleo o pego el código en Android Studio; tú lo compruebas leyendo el archivo y compilando (`/verificar`). Si me pasas demasiado o demasiado poco, te lo diré y ajustas. La forma de trabajar completa, que iremos afinando, está en `.claude/skills/como-trabajamos/SKILL.md` (se lee al abrir cada sesión).

**Por qué:** la normativa del módulo permite la IA como apoyo y prohíbe que sustituya mi trabajo; el uso indebido es suspenso. Tengo que defender este código en un vídeo de 10 minutos, y la memoria declara cómo se usó la IA: **lo escrito tiene que coincidir con lo hecho.**

## Stack — fijo

Kotlin · Android nativo · XML con Activities y Fragments (**no** Compose) · Room · MVVM sencillo (ViewModel + repositorio) · Glide · `minSdk 26`. Detalles y versiones en el spec. **Sin librerías que no estén en el spec.**

## Comandos y mapa (para Claude Code) [Claude]

- Proyecto de Android Studio: `app-ako/` (paquete `yunkang.ako`: `datos/` entidades, dao, repositorios · `dominio/` puro y `modelos/` · `seguridad/` · `imagenes/` · `ui/`). Documentación en `docs/`; lo decidido fuera del spec en `docs/decisiones-code.md`; los textos en `docs/textos-ui.md`; una guía por sesión en `docs/guias/`.
- Compilar: `cd /c/AKO/app-ako && ./gradlew.bat compileDebugKotlin` (recursos XML: `assembleDebug`). Pruebas de código: `./gradlew.bat testDebugUnitTest`. Pruebas de Room: `connectedDebugAndroidTest` con el emulador encendido. Gradle ya usa el Java de Android Studio (`~/.gradle/gradle.properties`).
- Emulador: `C:\Android\Sdk\emulator\emulator.exe -avd Pixel_6_API_34`; instalar y ver: `./gradlew.bat installDebug`, `adb exec-out screencap -p > cap.png`. SDK en `C:\Android\Sdk`.
- Git: raíz del repositorio `C:\AKO`, solo `main`; commits `S<N>: …` (el hook lo exige); nada de `--amend`, `reset --hard`, `push --force`, `restore`, `rebase` ni `stash drop` (prohibidos en `.claude/settings.json`).
- Lecciones de sesiones anteriores (se leen siempre): @docs/lecciones-claude.md

## Vocabulario obligatorio

| Se dice | Nunca |
|---|---|
| **Cuenta** (el tercer rol) | Recibir |
| **recibo** | ticket, factura, precuenta |
| un plato, una categoría, una etiqueta o un idioma se **elimina** (sale de la carta; `activo = false`; la fila no se borra) y se **recupera** | borrar, dar de baja |
| un plato se **desactiva** solo cuando está **agotado** (`disponible = false`, nivel 2, incremento 12) y se **activa** al reponerlo | usar *desactivar* para *eliminar* |
| **plato visible** = activo y con su categoría activa | — |
| **plato existente** = toda fila de `producto` | — |
| **Resumen de ingresos** (la pantalla 2g) | Resumen por día, Resumen del día |
| **modo camarero** = Pedir sin fijar pantalla | cualquier otro uso |
| **la carta** = la pantalla 5, única | carta compacta, catálogo compacto |

## Lo que NO se hace nunca

- Subir a GitHub una clave de API, un token, una contraseña, un keystore o un archivo con secretos (`.env`, `claude_desktop_config.json`, `.mcp.json`, `local.properties`). Antes de cada commit se aplica la skill `seguridad` (`.claude/skills/seguridad/SKILL.md`).
- Guardar el PIN en claro. Se guarda sal + hash PBKDF2.
- Borrar filas de `producto`, `categoria`, `modificador`, `etiqueta` o `idioma`: se marcan `activo = false`.
- Borrar líneas de una comanda automáticamente al eliminar o desactivar un plato.
- Crear una comanda vacía: nace con el primer *Enviar*.
- Guardar un dato que se pueda calcular (total de la comanda, ocupación de la mesa, kcal).
- Reconocer la categoría por defecto por su nombre: se usa `esPorDefecto`.
- Programar una segunda carta o un campo para teclear el número de plato.
- Gestos ocultos: todo lo que se puede hacer tiene un botón visible.
- Empezar un incremento del nivel 2 antes de terminar, probar y documentar el nivel 1 **y de terminar el doc**; o meter código de nivel 2 «por si acaso» (P115).
- Autenticación con servidores, pagos reales, notificaciones push, traducción automática.
- Cambiar el alcance, el modelo de datos o una regla R1-R16 sin decírmelo antes.
