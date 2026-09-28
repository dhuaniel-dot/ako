> **Guía de la sesión 1 — Proyecto y repositorio.** Escrita en la sesión 00 (24 sep 2026) [Claude] para que la S1 no se pierda en versiones ni en clics. Se sigue paso a paso; lo que salga distinto en pantalla se apunta en la ficha `sesion-01.md`. Objetivo del spec (apartado 11, S1): proyecto vacío en Android Studio dentro de `C:\AKO`, primer arranque en el emulador, Git y repositorio público en GitHub, primer commit sin atribución.

# Sesión 1 — guía

## 0. Antes de empezar (2 min)

- Reiniciar la app de Claude (para que cargue el ajuste `attribution`).
- Abrir un chat nuevo sobre `C:\AKO` con **Opus 5.5, esfuerzo medio**, y escribir «empecemos la S1».
- Tener a mano: cuenta de GitHub `dhuaniel-dot` iniciada en el navegador.

## 1. Android Studio: primer arranque y SDK (10 min)

1. Abrir **Android Studio** (menú Inicio).
2. Si pregunta por importar ajustes: *Do not import settings* → OK.
3. Asistente de bienvenida (*Setup Wizard*) → *Next*. En **Install Type** elegir **Custom** → *Next*.
4. En **SDK Components Setup**: en *Android SDK Location* escribir **`C:\Android\Sdk`**. Dejar marcados *Android SDK* y *Android Virtual Device*; *Next*. Como el SDK ya está, no descargará casi nada.
5. Aceptar licencias → *Finish*. Si pide instalar «Android Emulator hypervisor driver», ya está instalado: aceptar o saltar.
6. Pantalla de bienvenida (*Welcome to Android Studio*) con *New Project*: parar aquí y mandar captura.

- [Claude] La documentación oficial no describe en texto las pantallas del asistente (Install Type, SDK Components Setup): solo dice «sigue el asistente e instala los paquetes del SDK recomendados». Si los nombres salen distintos, lo que importa es una cosa: que la ruta del SDK sea `C:\Android\Sdk`. Si el asistente no deja elegirla, se cambia después en **File → Settings → Appearance & Behavior → System Settings → Android SDK** → *Android SDK Location*.
- [Claude] Mientras descarga pueden salir dos ventanas de Windows: **Control de cuentas de usuario** para *Windows Command Processor* → **Sí**; y **Alerta de seguridad de Windows** sobre `adb.exe` → **Permitir acceso**. Son normales.

## 2. Crear el proyecto dentro de `C:\AKO` (10 min)

1. *New Project* → plantilla **Empty Views Activity** (la que **no** dice Compose) → *Next*.
2. Rellenar:
   - **Name:** `Ako`
   - **Package name:** `yunkang.ako`
   - **Save location:** `C:\AKO\app-ako`
   - **Language:** Kotlin
   - **Minimum SDK:** API 26 («Android 8.0») *(la documentación lo llama* Minimum API level*)*
   - **Build configuration language:** Kotlin DSL (build.gradle.kts) *(es la opción por defecto desde Android Studio Giraffe)*
   - Si sale una casilla **Use legacy android.support libraries**: dejarla **desmarcada**.
3. *Finish*. Esperar a que termine «Gradle sync» (barra de progreso abajo; la primera vez, varios minutos). Captura cuando termine.
4. Si la sincronización falla: copiar el texto del error (pestaña *Build*, abajo, subpestaña *Sync*) y pegarlo en el chat. No tocar nada más.

- [Claude] El asistente ya genera `gradle/libs.versions.toml` (el catálogo de versiones es lo normal en proyectos nuevos desde Android Studio Iguana) y, con AGP 9, Kotlin va integrado en el plugin de Android: no hace falta el plugin `org.jetbrains.kotlin.android`. Por eso en el apartado 4 los archivos de Gradle se **sustituyen enteros**, no se añaden trozos.

## 3. Comprobar que arranca en el emulador (5 min)

1. Arriba, en el desplegable de dispositivos, elegir **Pixel_6_API_34** (ya existe). Si no aparece: **View → Tool Windows → Device Manager** (también icono del móvil en la barra lateral derecha) → debería listarlo; si no, **+** → *Create Virtual Device* → Pixel 6 → imagen API 34 «Google APIs».
2. Botón verde **Run ▶** (o `Mayús+F10`). El emulador se abre **dentro de Android Studio** (panel *Running Devices*) y al cabo de un minuto sale la app con «Hello World!».
3. Captura. Con eso, el entregable «la app vacía arranca en el emulador» está hecho.

## 4. Dependencias del stack (15 min, Claude da los archivos)

Sustituir los archivos de Gradle que generó el asistente por los de `docs/guias/stack-verificado/` (su LEEME dice cuál va dónde): `gradle/libs.versions.toml`, `settings.gradle.kts`, `build.gradle.kts`, `app/build.gradle.kts`, `gradle.properties` y `gradle/wrapper/gradle-wrapper.properties`. Daniel los copia (marcha calidad) o los copia Claude por consola si Daniel lo prefiere: son configuración, no código de negocio.

Después: **File → Sync Project with Gradle Files** (o **Sync Now** en la franja de aviso que sale arriba del editor) y `Run ▶` otra vez. Si compila y arranca, las versiones quedan fijadas hasta la entrega.

- [Claude] **Versión de AGP (decidido, P42 → B, 25 sep):** según la tabla oficial de compatibilidad (https://developer.android.com/build/releases/about-agp), Android Studio **2026.1.3 (Quail 3)**, el instalado, admite AGP **hasta 9.3**. Por eso `stack-verificado` usa **AGP 9.3.3** (antes 9.4.1, que pedía Quail 4), compilado el 25 sep con todo el stack y Gradle 9.6.0: `assembleDebug`, `testDebugUnitTest` y la APK de pruebas del emulador en verde. Android Studio no se actualiza (P19). Si aun así al sincronizar sale un error de «AGP version … not supported / incompatible» o Android Studio pide actualizarse: **parar y avisar a Claude**. No actualizar por cuenta propia.

## 5. Git y GitHub (15 min)

Claude lo hace por consola desde `C:\AKO` y va diciendo qué hace cada comando:

1. `git init` en `C:\AKO` (la raíz: `docs/`, `CLAUDE.md`, `.claude/`, `app-ako/`). El `.gitignore` ya existe.
2. `git status` para ver qué se va a subir (no debe aparecer `build/`, `.idea/` ni `local.properties`).
3. `git add -A` · `git commit -m "S1: proyecto Android Studio, SDK y emulador"` → el hook comprueba que existe `docs/diario/sesion-01.md` (la crea `abrir-sesion`) y que no hay atribución.
4. `git log -1` → comprobar que el autor es **Yunkang Daniel <dhuaniel@gmail.com>** y que **no hay línea `Co-Authored-By`**. Si la hubiera: ajuste `attribution` mal cargado → reiniciar la app y repetir.
5. `gh repo create ako --public --source=. --remote=origin --push` → crea el repositorio en `github.com/dhuaniel-dot/ako` y sube.
6. Abrir en el navegador `https://github.com/dhuaniel-dot/ako` → captura: entregable «el repositorio se ve en GitHub».
7. `README.md`: el borrador ya está en `docs/guias/readme-borrador.md` [Claude]; Daniel lo lee, lo cambia a su gusto y lo pega en `C:\AKO\README.md`; commit `S1: README`.

## 6. Cierre (10 min)

`cerrar-sesion`: ficha `sesion-01.md` completa (tiempo real, qué se hizo, problemas con justificación, qué entendí con tus palabras: qué es Gradle, qué es una dependencia, qué es un commit y un push, qué es el emulador), `estado-nivel.md` sin cambios (la S1 no implementa ningún RF), push, hash en la ficha. Claude entrega el primer mensaje del chat de la S2.

## 7. Lo que hay que saber defender de la S1

- Por qué el proyecto está dentro de `C:\AKO` al lado de `docs/` (una sola raíz de repositorio; la documentación viaja con el código).
- Qué hace Gradle y qué es el catálogo de versiones (`libs.versions.toml`): un único sitio donde están las versiones.
- Por qué `minSdk 26` (PBKDF2 con SHA-256 existe desde API 26) y qué es `targetSdk`.
- Qué es un commit, qué es `push`, y por qué el repositorio es público desde el primer día (RNF-25).

## Versiones verificadas

Comprobadas el 24 de septiembre de 2026 compilando un proyecto de prueba con este SDK y este PC (`assembleDebug` y `testDebugUnitTest` en verde). Los archivos exactos están en `docs/guias/stack-verificado/` con su LEEME: AGP 9.3.3 (P42; recompilado el 25 sep) · Gradle 9.6.0 · Kotlin 2.2.10 (integrado en AGP 9; no se declara ningún plugin de Kotlin) · KSP 2.3.12 · Room 2.8.5 · Glide 5.0.9 · lifecycle 2.11.0 · coroutines 1.10.2 · `compileSdk` y `targetSdk` 37 · `minSdk` 26. Trampas ya resueltas: Glide 5.0.9 exige `compileSdk 37`; Glide con KSP exige una clase `@GlideModule`; con `exportSchema = false` Room no avisa del esquema. La primera compilación tarda un minuto (descarga ~1 GB en `C:\Users\dhuan\.gradle`); las siguientes, segundos. [Claude] Compiladas por consola, no abiertas en el IDE: ver el aviso de compatibilidad AGP 9.4 / Quail 3 del apartado 4.

## Tres cosas que NO hay que hacer en la S1 (las encontró la revisión independiente)

1. **Que Android Studio no cree un Git dentro de `app-ako/`.** El repositorio Git es `C:\AKO`; si Android Studio creara otro dentro de `app-ako/`, en GitHub la carpeta del código aparecería vacía. Comprobación: no debe existir `C:\AKO\app-ako\.git`.
   - En el asistente de proyecto nuevo de Android Studio, la documentación oficial no lista ninguna casilla «Create Git repository» (esa casilla es del asistente de IntelliJ). Si aun así sale: **desmarcada**.
   - «Enable Version Control Integration» **no es un aviso que salga solo**: es una opción del menú **VCS**. **No pulsarla**, ni ninguna que diga **Create Git Repository**: las dos crean el repositorio en la carpeta del proyecto, o sea, en `app-ako/`.
   - [Claude] Después del `git init` del apartado 5, Android Studio puede avisar abajo a la derecha de que ha encontrado un repositorio Git que no tiene registrado (*unregistered VCS root*). Si la carpeta que nombra es **`C:\AKO`**: **Add root** está bien (solo le dice que use el que ya existe; no crea nada) y así funciona el panel Git de solo mirar. Si nombra otra carpeta: **Ignore** y avisar a Claude.
2. **No hacer commits desde el botón Commit de Android Studio**: los hace Claude con la ficha del diario y el hook.
3. **No actualizar Android Studio** aunque lo pida (decisión P19).

Y dos ajustes de un minuto, la primera vez que se abra el proyecto:
- *File → Settings → Editor → General → Auto Import*: en el bloque **Kotlin** marcar **Add unambiguous imports on the fly** (viene desmarcada). En el bloque **Java**, comprobar que **Insert imports on paste** está en **Always** (es lo que viene): aunque esté en el bloque Java, también manda al pegar Kotlin; el bloque Kotlin no tiene esa opción. Así, al pegar un bloque, Android Studio añade los `import` solo.
- Antes del primer `push`, Claude ejecuta `gh auth setup-git` (una vez; usa la sesión de GitHub que ya tiene `gh`) para que Git no abra una ventana de usuario y contraseña.

## Comprobado contra la documentación oficial (25 sep 2026) [Claude]

- https://developer.android.com/studio/install — el primer arranque pregunta si importar ajustes (→ OK) y luego se sigue el *Setup Wizard*, que descarga los componentes del SDK. **No** describe en texto Install Type, SDK Components Setup ni las licencias (solo un vídeo): esos nombres quedan sin confirmar.
- https://developer.android.com/codelabs/basic-android-kotlin-compose-install-android-studio — en Windows, ventana de Control de cuentas para *Windows Command Processor* (Sí) y alerta de seguridad de `adb.exe` (Permitir acceso); al final, *Finish* y la pantalla *Welcome to Android Studio*.
- https://developer.android.com/studio/intro/studio-config — cambiar la ruta del SDK: *Settings → Appearance & Behavior → System Settings → Android SDK → Android SDK Location*.
- https://developer.android.com/studio/projects/create-project (actualizada el 2 sep 2026) — campos *Name*, *Package name*, *Save location*, *Language*, *Minimum API level* y la casilla *Use legacy android.support libraries*; *Finish*. No menciona ninguna casilla de Git ni el campo *Build configuration language*.
- https://developer.android.com/codelabs/camerax-getting-started — la plantilla con vistas se llama **Empty Views Activity** (la *Empty Activity* es la de Compose).
- https://developer.android.com/build/migrate-to-kotlin-dsl — Kotlin DSL (`build.gradle.kts`) por defecto en proyectos nuevos desde Giraffe.
- https://android-developers.googleblog.com/2024/02/android-studio-iguana-is-stable.html — «los proyectos nuevos usan catálogos de versiones» (`libs.versions.toml`).
- https://developer.android.com/build/migrate-to-built-in-kotlin — AGP 9 trae Kotlin integrado y activado; el plugin `org.jetbrains.kotlin.android` ya no se aplica.
- https://developer.android.com/build/releases/about-agp — tabla de compatibilidad: Quail 3 (2026.1.3) admite AGP 7.1–9.3; Quail 4 (2026.1.4), 7.1–9.4.
- https://developer.android.com/build — *Sync Now* en la franja de aviso y *File → Sync Project with Gradle Files*; https://developer.android.com/studio/run — la ventana *Build* tiene una pestaña *Sync* con lo que hace la sincronización.
- https://developer.android.com/studio/run/managing-avds — *View → Tool Windows → Device Manager*, **+** → *Create Virtual Device*.
- https://developer.android.com/studio/run/emulator-launch-separate-window — el emulador se abre dentro de Android Studio por defecto (panel *Running Devices*).
- https://www.jetbrains.com/help/idea/enabling-version-control.html y https://www.jetbrains.com/help/idea/set-up-a-git-repository.html — *VCS → Enable Version Control Integration* crea el repositorio en la raíz del proyecto; aviso de repositorios sin registrar con *Add roots* / *Ignore*.
- https://www.jetbrains.com/help/idea/settings-auto-import.html y el código de IntelliJ (github.com/JetBrains/intellij-community: `KotlinCodeInsightWorkspaceSettingsProvider.kt`, `KotlinCopyPasteReferenceProcessor.kt`, `CodeInsightSettings.java`) — el bloque Kotlin de *Auto Import* solo tiene *Add unambiguous imports on the fly* (desmarcada por defecto) y *Optimize imports on the fly*; *Insert imports on paste* está en el bloque Java, viene en *Always* y Kotlin la respeta al pegar.
