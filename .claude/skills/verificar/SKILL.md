---
name: verificar
description: 'Comprueba con hechos el código que Daniel tecleó en Android Studio: lee el archivo en disco, lo compara con el bloque dado, compila, pasa las pruebas y mira el emulador si hace falta; informa de qué falla y dónde.'
when_to_use: 'Cada vez que Daniel diga «ya está», «lo he puesto», «mira si está bien» o mande una captura de código, o escriba /verificar.'
---

# Verificar

Daniel pone el código en Android Studio; Claude comprueba **con hechos**, no mirando una captura: leyendo el archivo y compilando. Nunca se dice «parece bien» sin haber compilado.

## 0. Antes de leer: que el archivo esté guardado

Android Studio no siempre escribe en disco al instante. Pedir a Daniel: **«Ctrl+S en Android Studio (o File → Save All) y dime ya»**. Comprobar que la fecha de modificación del archivo es posterior al «ya está» (`ls -l --time-style=full-iso <archivo>`); si no, no está guardado.

## 1. Qué cambió y si está donde debe

- `git status --short` en `C:\AKO` da los archivos tocados (`??` = nuevos).
- Leer cada archivo cambiado que forme parte de la pieza y **compararlo con el bloque que se le dio a Daniel**. Diferencias típicas: una llave o un paréntesis de menos, un `import` que Android Studio no añadió (por eso en la S1 se pone *Settings → Editor → General → Auto Import → Insert imports on paste: Always*), un nombre con una letra cambiada, código pegado en el archivo equivocado.
- **Paquete ↔ carpeta:** la línea `package yunkang.ako.xxx` tiene que coincidir con la ruta del archivo. Kotlin compila aunque no coincida, pero rompe las capas; se corrige antes de seguir.
- Un archivo `.sh` de `.claude/hooks/` tocado desde Android Studio puede quedar con finales CRLF y bash lo rompe (`$'\r': command not found`): `.gitattributes` lo fuerza a LF; si pasa, `dos2unix` o `sed -i 's/\r$//'`.

## 2. Compilar

Desde `C:\AKO\app-ako`. Gradle usa el Java de Android Studio porque `C:\Users\dhuan\.gradle\gradle.properties` lleva `org.gradle.java.home` (hecho en la sesión 00); no hace falta tocar `JAVA_HOME`.

- Git Bash: `cd /c/AKO/app-ako && ./gradlew.bat compileDebugKotlin`
- PowerShell: `cd C:\AKO\app-ako; .\gradlew.bat compileDebugKotlin`

Si la pieza toca recursos XML: `assembleDebug` (más lento, comprueba también los layouts). Si la pieza es una prueba de `test/`: `compileDebugUnitTestKotlin` (el `compileDebugKotlin` no compila `src/test`). La primera compilación de la sesión tarda (Gradle arranca); las siguientes, segundos.

## 3. Pruebas de código

Cuando existan (desde la S3): `./gradlew.bat testDebugUnitTest`. Si la sesión tiene pruebas de Room: `connectedDebugAndroidTest` con el emulador encendido.

## 4. Comprobar en el emulador (cuando la pieza es una pantalla)

Claude puede mirar el emulador sin que Daniel haga captura:

- Encender si hace falta: `C:\Android\Sdk\emulator\emulator.exe -avd Pixel_6_API_34` y esperar a `adb shell getprop sys.boot_completed` = 1.
- Instalar y arrancar: `./gradlew.bat installDebug` y `adb shell am start -n yunkang.ako/.ui.selector.SelectorActivity` (la Activity que toque).
- Ver la pantalla: `adb exec-out screencap -p > cap.png` y leer `cap.png`.
- Ver la base de datos (S2 en adelante, sustituye al inspector para Claude): `adb exec-out run-as yunkang.ako cat databases/ako.db > ako.db` y leerla con Python (`sqlite3`): tablas, filas de la precarga.

Daniel sigue haciendo las pruebas manuales con el dedo; esto es para que Claude confirme lo que Daniel cuenta.

## 5. Informar, en este orden y sin más

- `OK` y qué se comprobó, o
- cada error como `archivo:línea — qué pasa — qué cambiar` (una línea por error). Si el arreglo es más de una línea, va **primero el bloque de código corregido** (solo el trozo que cambia, con la ruta) y **debajo** la explicación, como en todas las piezas.
- Si una prueba falla: qué esperaba y qué obtuvo. **Nunca arreglar una prueba para que pase**: se arregla el código o, si la prueba está mal, se dice y se decide con Daniel.
- Si Gradle da un error de entorno (Java, SDK, red) y no de código, decirlo aparte: no es un problema de Daniel.

## 6. Después del OK

- Si la pieza está completa y sus pruebas pasan: **commit intermedio** `git add -A && git commit -m "S<N>: <pieza>"` (sin push hasta el cierre). Es lo que pide el spec, apartado 13; el hook comprueba el formato.
- Un error que costó más de un intento va a la tabla *Problemas y soluciones* de la ficha en ese momento: síntoma copiado, arreglo, justificación, fuente.
