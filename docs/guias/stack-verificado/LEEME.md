# docs/guias/stack-verificado/ — los archivos de Gradle del stack, comprobados compilando

Creada el 24 de septiembre de 2026 (sesión 00) [Claude]. Se edita solo si cambia una versión.

Son los archivos de configuración de un proyecto de prueba con **este mismo SDK y este mismo PC**, con los que `assembleDebug` y `testDebugUnitTest` pasan (BUILD SUCCESSFUL). En la S1, después de que el asistente de Android Studio cree `app-ako/`, se sustituyen los suyos por estos (misma ruta relativa dentro de `app-ako/`).

| Archivo | Va en |
|---|---|
| `gradle/libs.versions.toml` | `app-ako/gradle/libs.versions.toml` (el catálogo de versiones: aquí están todas) |
| `settings.gradle.kts` | `app-ako/settings.gradle.kts` |
| `build.gradle.kts` | `app-ako/build.gradle.kts` (raíz del proyecto) |
| `app/build.gradle.kts` | `app-ako/app/build.gradle.kts` |
| `gradle.properties` | `app-ako/gradle.properties` |
| `gradle/wrapper/gradle-wrapper.properties` | `app-ako/gradle/wrapper/gradle-wrapper.properties` |
| `app/AkoGlideModule.kt` | `app-ako/app/src/main/java/yunkang/ako/imagenes/AkoGlideModule.kt` (Glide con KSP exige esta clase; llega en la S12 con `ImageStore`, pero puede estar desde la S1) |

Versiones (24 sep 2026; AGP cambiado el 25 sep): AGP **9.3.3** · Gradle 9.6.0 · Kotlin 2.2.10 (el que trae AGP 9; no se declara) · KSP 2.3.12 · Room 2.8.5 · Glide 5.0.9 · core-ktx 1.19.1 · appcompat 1.8.0 · material 1.14.0 · constraintlayout 2.2.2 · recyclerview 1.4.0 · activity-ktx 1.13.0 · fragment-ktx 1.9.1 · lifecycle 2.11.0 · coroutines 1.10.2 · junit 4.13.2 · test-ext-junit 1.3.0 · test-runner 1.7.0. `compileSdk 37` y `targetSdk 37` (el último estable, como pide el spec; la plataforma 37 ya está en `C:\Android\Sdk`), `minSdk 26`. El único cambio respecto a lo compilado es `targetSdk` 36 → 37, que no afecta a la compilación.

**Cambios del 25 sep (P42 y P43, decididos por Daniel) [Claude]:** AGP 9.4.1 → **9.3.3**, porque el Android Studio instalado (2026.1.3) solo admite AGP hasta 9.3 (https://developer.android.com/build/releases/about-agp); en `app/build.gradle.kts`, `testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"` (lo exige el spec 2) y `room-testing` pasa de `testImplementation` a `androidTestImplementation`; `AkoGlideModule.kt` lleva ya `package yunkang.ako.imagenes`. Recompilado el 25 sep con un proyecto de prueba: `assembleDebug`, `testDebugUnitTest` y `assembleDebugAndroidTest` en verde.

Por qué 37 y no 36: Glide 5.0.9 y core-ktx 1.19 exigen `compileSdk 37` (fallan en `checkDebugAarMetadata` con 36). La alternativa comprobada, por si hiciera falta volver atrás: `compileSdk 36` con Glide **5.0.7** y core-ktx **1.18.0**.
