# Sesión 00 — 2026-09-24

| Campo | Valor |
|---|---|
| **Fecha** | 2026-09-24 (y la noche del 24 al 25 para el análisis) |
| **Sesión nº** | 00 (bases: análisis, PC y herramientas; sin código) [Claude] |
| **Objetivo de la sesión** | Leer todo el diseño, escribir `ANALISIS-INICIAL.md`, dejar el PC listo (virtualización, SDK, emulador, Git) y crear las skills y el hook del ritual de sesión |
| **Tiempo dedicado** | Claude solo (noche): ~1 h de lectura y escritura. Daniel + Claude (tarde): ~2 h, a rachas. *(Daniel: corrige si no cuadra)* |
| **Nivel / pieza** | Bases del proyecto (no toca ningún RF) |
| **Commit final** | — (sin repositorio todavía: se crea en la S1. **Sesión cerrada**; la siguiente es la S1) |

## Qué se hizo

- `ANALISIS-INICIAL.md`: plan de fases (A), plan sesión a sesión (B), lo entendido (C), 24 contradicciones y huecos (D), instalación (E), skills (F), chats y modelos (G) y 36 preguntas (H).
- Preguntas contestadas por Daniel (resumen de las decisiones que afectan al código): P6 pruebas con prerrequisito → *Parcial* y repetir · P7 precarga de ejemplo → categoría **Bebidas** con **1 · Agua · 1,50 €** · P8 → Bebidas se elimina al montar el juego de datos [Claude] · P9 → «recuperar» (no «reactivar») en la cadena 3e · P11 → precio negativo lanza excepción · P12 → P-C-05 solo `Hash` · P13 → foto de categoría en S12 · P14 → resaltado nivel 3 · P15 → puerta de Pedir con `ConfirmacionDialog` de un botón · P16 → precarga con corrutina y DAOs [Claude] · P17 → dependencias base y DAOs por constructor · P22 → repositorio `ako`, proyecto en `C:\AKO\app-ako\`.
- PC: virtualización SVM activada en la BIOS (Daniel) · SDK de Android, emulador e imagen Pixel 6 API 34 instalados en `C:\Android\Sdk` y emulador `Pixel_6_API_34` creado y arrancado (62 s el primer arranque) · controlador AEHD instalado · exclusiones de Windows Defender para `C:\AKO`, `C:\Android\Sdk` y `.gradle` · identidad de Git para `C:\AKO`: Yunkang Daniel <correo anónimo de GitHub, `…@users.noreply.github.com`> (por `includeIf`, sin tocar la global) · ajuste `attribution` vacío en los ajustes de usuario de Claude Code (P226) · crédito de 250 $ para sesiones en la nube reclamado (caduca el 5 nov).
- Regla 12 añadida a `CLAUDE.md` (nivel de detalle «corta el pan, pon la carne, cierra»; código arriba y explicación debajo; Daniel teclea) [decidida por Daniel, P30/P37].
- Skills del repositorio en `.claude/skills/`: `abrir-sesion`, `cerrar-sesion`, `relevo` (con umbrales de contexto 60/75/85 % y relevo de emergencia), `verificar` y `como-trabajamos` (la forma de trabajar con las palabras de Daniel, documento vivo). Hook `.claude/hooks/comprobar-commit.sh` (bloquea commits con atribución o sin ficha de sesión), configurado en `.claude/settings.json`.
- Análisis profundo por la tarde (Daniel: «gasto ahora los máximos tokens para gastar menos en el futuro»): **stack verificado compilando** un proyecto de prueba (`docs/guias/stack-verificado/`, AGP 9.4.1, Gradle 9.6.0, Room 2.8.5, Glide 5.0.9, `compileSdk 37`) · **inventario de los 141 textos** del nivel 1 (`docs/textos-ui.md`, 20 discrepancias, 18 resueltas [Claude], 2 decididas por Daniel: P40 y P41) · **revisión independiente** de skills, hook y CLAUDE.md (8 hallazgos de peso aplicados: hook reescrito y probado con 11 casos, comandos de Gradle corregidos, `~/.gradle/gradle.properties` con el Java de Android Studio, EN-CURSO con plantilla, commits intermedios, comprobación por `adb`, aviso de no crear Git dentro de `app-ako/`) · **búsqueda en la documentación oficial** de Claude Code (comandos destructivos prohibidos en `settings.json`, hook de arranque `al-empezar.sh`, atajos de la app) · `docs/decisiones-code.md`, `docs/guias/sesion-01.md`, `docs/plantilla-en-curso.md`, `docs/plantilla-readme.md`, `.gitignore`, `.gitattributes`, subagente `revisor`.
- Mientras Daniel estaba en el trabajo (sin preguntas): guías de piezas `sesion-02.md` a `sesion-05.md`, `android-studio-basico.md` (la herramienta explicada para quien nunca la ha abierto) y `strings-es-borrador.xml` (129 cadenas + 2 plurals del nivel 1). Las propuestas [Claude] de diseño que salieron al planificar la S4 (qué DAOs recibe `CartaRepository`, R6 en dos pasos, cómo se hace la transacción de `enviarCarrito`, la tercera salida de la cadena 3e) se deciden con Daniel en el Plan Mode de la S4.
- Noche del 24 al 25 sep (Daniel dormido; sin preguntas) [Claude]: **guías de piezas `sesion-06.md` a `sesion-13.md`** (S6 Panel 21 piezas, S7 Plato 14, S8 Pedir 24, S9 Cuenta 20, S10 Resumen 9, S11 Room 7, S12 Fotos 11, S13 Cierre 12; S6, S8 y S9 con la lista de huecos para su Plan Mode) · **`docs/guias/juego-de-datos.md`** (montaje del juego de datos en 37 pasos desde instalación limpia con P8, orden de las 29 P-M sesión por sesión con *Parcial* y repetición según P6, pasada final de la S13) · **`docs/guias/readme-borrador.md`** (para pegar en la S1) · **revisión con ojos nuevos** de las 13 guías por tres subagentes que no las escribieron (unos 60 arreglos aplicados: claves de `strings.xml`, calendario de pruebas, nombres entre sesiones, `Precarga.cargar` desde la S2, `MesaEstado` y `ComandaConTotal` en la S3, P-M-15 en la S8…) · **`sesion-01.md` y `android-studio-basico.md` comprobadas contra developer.android.com** (menús corregidos, marcas «comprobar en pantalla» quitadas donde se confirmaron) · hallazgo: **Android Studio 2026.1.3 no admite AGP 9.4.1**; el stack compila con AGP 9.3.3 (probado) → pregunta P42, a contestar antes de la S1 · **62 preguntas** (P42–P103) en `decisiones-code.md`, apartado 5, **contestadas por Daniel el 25 sep** (las técnicas de código, con la recomendación de Claude por regla suya), y las decisiones [Claude] de la noche. Aplicado ya: `stack-verificado/` con AGP 9.3.3, el lanzador de pruebas y `room-testing` en `androidTest` (recompilado en verde, P42 y P43); guías S2 y S3 pasadas al formato de la S4 (P45); notas en S6 (plegar la caja, P56) y S13 (sin Scanner, P98; botón modo día/noche, P101). Nuevo fuera del diseño: el botón de modo día/noche (anotar en el Project). La noche se cortó una vez por el límite de uso (23:00 → 00:10) y se retomó sin perder nada.
- Nada a medias.

## Problemas y soluciones

| # | Qué falló | Cómo se resolvió | Justificación (por qué esta solución y no otra) | Fuente |
|---|---|---|---|---|
| 1 | El emulador no podía usar aceleración: Windows decía «virtualización en el firmware: No» | Daniel activó *SVM Mode* en la BIOS de la placa Gigabyte A520M H y reinició | Sin virtualización por hardware el emulador x86_64 no arranca o va inservible; es un ajuste del firmware, no hay alternativa por software | Propio; comprobado con `systeminfo` |
| 2 | El SDK descargado por Claude Code a `AppData\Local\Android\Sdk` no aparecía para otros programas: la app de Claude redirige lo que escribe en `AppData` a su propia carpeta (`Packages\Claude_…\LocalCache`) | Se movió el SDK a `C:\Android\Sdk`, fuera de `AppData` | Android Studio y Gradle tienen que ver el SDK con su ruta real; una carpeta fuera de `AppData` no se redirige. Se descartó reinstalar desde Android Studio porque los 5,8 GB ya estaban bajados | Propio; verificado listando `Packages\Claude_pzs8sxrjxfjjc\LocalCache\Local\Android` |
| 3 | `emulator -accel-check`: «Android Emulator hypervisor driver is not installed» (Windows 11 Home no trae Hyper-V) | Se instaló el paquete `extras;google;Android_Emulator_Hypervisor_Driver` con `sdkmanager` y su `silent_install.bat` como administrador | En Windows Home el emulador acelera con el controlador AEHD de Google; la alternativa (activar *Windows Hypervisor Platform*) exige otro reinicio | README del paquete AEHD; `accel-check` después: «AEHD (version 2.2) is installed and usable» |
| 4 | Los commits saldrían como `Yunkang46 <(correo de la otra cuenta)>` (identidad global de Git) | `includeIf "gitdir/i:C:/AKO/"` en `~/.gitconfig` apuntando a `~/.gitconfig-ako` con Yunkang Daniel <correo anónimo de GitHub, `…@users.noreply.github.com`> | Solo cambia la identidad dentro de `C:\AKO`; la global queda intacta por si otro proyecto la usa | Documentación de Git, `git config` includeIf |

## Qué entendí y qué no

- **Entendí:** *(Daniel: en tus palabras, qué sabrías explicar de lo de hoy: qué es la virtualización y por qué la necesita el emulador, qué es el SDK, qué es un commit, qué es un hook)*.
- **No entendí todavía:** *(Daniel)*.

## Para el vídeo

*(Añadido el 30 sep 2026 al crearse este apartado en la plantilla; ideas que salieron en la sesión 00.)*

- **Dónde vive cada regla (P128 del spec):** R9 (dos platos no comparten número) la impone la base de datos con un `UNIQUE`, como el registro civil con el DNI; R1 (una sola comanda abierta por mesa) no puede vivir ahí (Room no declara índices parciales) y la vigila el repositorio, como el camarero que mira si la mesa ya tiene comanda. Saber dónde está cada garantía es lo que distingue entender el modelo de copiarlo.
- **Archivo, nunca BLOB (verificación 6):** las fotos se guardan como archivos y en la base de datos solo va la ruta, como un álbum que guarda las fotos y una agenda que solo apunta dónde están. Por eso la foto es la última pieza del nivel 1: toda la app funciona sin ella.
- **Se reutiliza el componente, no la pantalla:** la misma fila de plato sirve en el Panel y en la carta, la misma rejilla en Elegir mesa y en Cuenta, el mismo recibo al cobrar y en el Resumen de ingresos; como una bandeja que vale en barra y en mesa.
- **Importes en céntimos enteros (spec 5.1):** se cuenta en monedas, no en euros con coma, porque la coma flotante acumula «céntimos fantasma» al sumar.
- **Cómo se controló el uso de la IA (decisiones P24, P28, P29 de la sesión 00):** el código lo teclea Daniel; cada pieza se explica antes y se pregunta después; un hook (un portero automático) no deja pasar un commit sin su ficha del diario; lo que propone Claude y no se pidió lleva [Claude]; el stack se comprobó compilando antes de la S1 («probar la receta antes de la cena»). Vale también para el apartado de metodología y la declaración de IA de la memoria.

## Pruebas

- Ninguna de código ni manual: no hay código. El emulador arranca y `adb devices` lo ve (`emulator-5554 device`).

## Uso de IA en esta sesión

- Daniel pidió el análisis completo de la documentación; Claude Code lo leyó todo y escribió `ANALISIS-INICIAL.md` sin preguntar (sesión desatendida). Daniel lo leyó y contestó las preguntas.
- Claude Code descargó e instaló el SDK, el emulador y el controlador AEHD por consola; Daniel activó la BIOS y aceptó los permisos de administrador.
- Claude Code escribió las cuatro skills, el hook y esta ficha; Daniel decidió cuáles se creaban (P28, P29) y cómo se trabaja (P24, P30, P33, P36).

## Siguiente sesión

- S1: abrir Android Studio (SDK en `C:\Android\Sdk`), crear el proyecto `app-ako` dentro de `C:\AKO` **sin marcar «Create Git repository»**, sustituir los archivos de Gradle por los de `docs/guias/stack-verificado/`, `git init` en `C:\AKO`, `gh auth setup-git`, repositorio público `ako` en GitHub, primer commit sin atribución y push. Guía completa: `docs/guias/sesion-01.md`. Antes: reiniciar la app de Claude (ajuste `attribution`) y abrir el chat con `C:\AKO` como carpeta del proyecto y Opus 5.5.
