> **Guía básica de Android Studio para Daniel.** Escrita el 24 sep 2026 [Claude]. Para leer una vez entera y después consultar por apartados. Android Studio 2026.1.3 (Quail 3) en Windows 11, proyecto `C:\AKO\app-ako`, SDK en `C:\Android\Sdk`, emulador `Pixel_6_API_34`. El IDE está en inglés: los nombres de menús van en inglés tal cual salen, con la explicación al lado. Lo marcado con **(comprobar en pantalla)** puede variar un poco en esta versión.

# Android Studio — lo básico

## 1. La ventana: qué es cada zona

1. **Izquierda — Project tool window** (`Alt+1`): el árbol de carpetas y archivos del proyecto.
2. Arriba de ese panel hay un desplegable que dice **Android** o **Project**. Elegir **Project**: muestra las carpetas reales del disco, iguales que en el Explorador de Windows. La vista *Android* las reordena y despista.
3. **Centro — el editor**: cada archivo abierto es una pestaña. Se cierra con la `x` de la pestaña o `Ctrl+F4`.
4. **Paneles de herramientas** (*tool windows*): se abren con los iconos de los bordes de la ventana (la *tool window bar*). Si uno no se ve: **View → Tool Windows →** su nombre. Los que se usan:
   - **Build**: resultado de compilar. Aquí salen los errores de compilación (y, en su pestaña *Sync*, los de sincronizar).
   - **Logcat**: mensajes que escribe la app mientras corre.
   - **Run** (`Mayús+F10` lo abre al ejecutar): lo que hace la app al arrancar y el resultado de las pruebas.
   - **Terminal**: consola de comandos (no hace falta usarla).
   - **App Inspection**: dentro está **Database Inspector**, para ver la base de datos.
   - **Running Devices**: la pantalla del emulador, que por defecto va dentro de Android Studio (no en una ventana aparte).
5. **Arriba — barra principal**: desplegable de dispositivos (debe decir `Pixel_6_API_34`), botón verde **Run ▶**, botón **Stop ■** rojo.
6. **Icono del elefante** con una flecha (**Sync Project with Gradle Files**, también en **File → Sync Project with Gradle Files**): vuelve a leer los archivos de Gradle. Se usa cuando Claude cambia dependencias.
7. **Abajo a la derecha — barra de progreso**: si se mueve, Android Studio está trabajando (sincronizando, indexando, compilando). Esperar a que desaparezca antes de hacer nada.
8. **Panel Git** (`Alt+9`, se llama *Git* o *Version Control*): solo para mirar, ver apartado 8.

## 2. Dónde está cada cosa del proyecto

Con la vista **Project** activa, desplegar `app-ako` → `app` → `src`:

| Ruta | Qué hay |
|---|---|
| `app/src/main/java/yunkang/ako/` | El código Kotlin de la app. Aquí van las clases. |
| `app/src/main/res/layout/` | Las pantallas, en XML (`activity_main.xml`, etc.). |
| `app/src/main/res/values/strings.xml` | Los textos de la app, uno por línea. |
| `app/src/main/AndroidManifest.xml` | La ficha de la app: nombre, pantallas, permisos. |
| `app/build.gradle.kts` | Configuración del módulo: qué librerías usa la app. |
| `gradle/libs.versions.toml` | Lista de versiones de las librerías. |
| `app/src/test/` | Pruebas que corren en el PC (rápidas). |
| `app/src/androidTest/` | Pruebas que corren en el emulador (lentas). |

- Hay un segundo `build.gradle.kts` en la raíz del proyecto. Casi nunca se toca.
- Las carpetas `build` y `.gradle` las genera Android Studio. No se tocan (apartado 9).

## 3. Crear cosas

### Un paquete (carpeta de código)

1. Clic derecho sobre `yunkang.ako` (o sobre `java` si no existe aún) → **New** → **Package**.
2. Escribir el nombre completo que diga Claude, por ejemplo `yunkang.ako.datos` → Enter.
3. Aparece la carpeta nueva en el árbol.

### Una clase o archivo Kotlin

1. Clic derecho sobre el paquete donde va → **New** → **Kotlin Class/File**.
2. Escribir el nombre que diga Claude (por ejemplo `Mesa`) y elegir **Class** o **File** según diga.
3. Se abre el archivo vacío en el editor con la primera línea `package yunkang.ako.datos` ya puesta.

### Un layout XML (pantalla)

1. Clic derecho sobre `res/layout` → **New** → **Layout Resource File**.
2. **File name**: el que diga Claude (por ejemplo `activity_mesas`), todo en minúsculas y con guion bajo. **Root element**: el que diga Claude o dejar el que sale.
3. **OK**. Se abre en modo diseño; **arriba a la derecha** del editor hay tres botones **Code / Split / Design**: elegir **Code** para pegar XML. (Atajo para cambiar entre ellos: `Alt+Mayús+→` / `Alt+Mayús+←`.)

### Por qué el nombre del paquete tiene que coincidir con la carpeta

- La línea `package yunkang.ako.datos` de arriba del archivo tiene que ser exactamente la carpeta donde está el archivo (`yunkang/ako/datos`).
- Si no coinciden, el archivo compila raro o no encuentra las otras clases. Si Android Studio lo subraya, `Alt+Enter` sobre esa línea → **Change file's package to '…'** (arregla la línea y deja el archivo donde está: es la buena si el archivo se creó en la carpeta correcta). La otra opción, **Move file to …**, mueve el archivo de carpeta: no usarla sin preguntar a Claude.
- Por eso el archivo se crea con clic derecho **desde la carpeta correcta**: así la línea `package` sale bien sola.

## 4. Pegar código que te da Claude

### Sustituir un archivo entero

1. Abrir el archivo (doble clic en el árbol).
2. Clic dentro del editor → `Ctrl+A` (seleccionar todo) → `Ctrl+V` (pegar lo copiado del chat).
3. `Ctrl+S`. Android Studio guarda solo casi siempre, pero `Ctrl+S` no hace daño. **File → Save All** guarda todo.

### Pegar un trozo en un sitio concreto

1. Abrir el archivo.
2. Poner el cursor donde diga Claude («al final del archivo», «dentro de la clase, después de la línea X»). `Ctrl+End` va al final del archivo; `Ctrl+G` va a un número de línea.
3. Enter para hacer sitio, `Ctrl+V`, `Ctrl+S`.

### Los imports

- Las líneas `import ...` de arriba del archivo dicen de dónde vienen las clases que usa el código. Sin ellas, los nombres salen en rojo.
- Al pegar, Android Studio suele añadirlas solo. Comprobar mirando arriba del archivo: si han aparecido líneas `import` nuevas, bien.
- Para que lo haga siempre, una sola vez: **File → Settings** (`Ctrl+Alt+S`) **→ Editor → General → Auto Import**:
  - Bloque **Kotlin**: marcar **Add unambiguous imports on the fly** (viene desmarcada). Este bloque solo tiene esa casilla y *Optimize imports on the fly* (esa, dejarla como esté).
  - Bloque **Java**: **Insert imports on paste** en **Always** (es lo que viene). Está en el bloque Java, pero también es la que manda al pegar código Kotlin.
  - **OK**.
- Si aun así queda algo en rojo: clic sobre la palabra roja → `Alt+Enter` → elegir **Import** (o el nombre de la clase). Si ofrece varias, decírselo a Claude.

### Colores del editor

- **Subrayado rojo**: error. No compila hasta arreglarlo. Pasar el ratón por encima muestra el mensaje; copiarlo al chat si no está claro.
- **Subrayado amarillo o texto gris**: aviso. Compila igual. Se ignora salvo que Claude diga otra cosa.
- **Franja roja en el margen derecho**: hay errores más abajo en el archivo; clic en la marca lleva a ellos.

## 5. Compilar y ejecutar

1. Si Claude ha cambiado `build.gradle.kts` o `libs.versions.toml`, aparece una franja arriba del editor con **Sync Now**: clic. O el icono del elefante. Esperar la barra de progreso.
2. Comprobar que el desplegable de dispositivos dice `Pixel_6_API_34`. Si no, desplegarlo y elegirlo.
3. **Run ▶** (`Mayús+F10`). Compila, abre el emulador si no estaba abierto e instala la app. Primera vez del día: uno o dos minutos.
4. Si el emulador no aparece en el desplegable: **View → Tool Windows → Device Manager** (o icono del móvil en la barra lateral derecha). Debe listar `Pixel_6_API_34`; pulsar su **▶** (*Launch*) para arrancarlo y volver a Run.
5. **Build → Clean and Assemble Project with Tests**: compila todo desde cero. Es el antiguo *Rebuild Project* (se renombró en Android Studio Meerkat). Se usa cuando algo falla sin motivo aparente y Claude lo pide.
6. [Claude] **La APK para la Release de GitHub (S13)**: **Build → Generate App Bundles or APKs → Generate APKs** (así lo nombran los archivos de la 2026.1.3 instalada) o **Build → Generate Bundle(s) / APK(s) → Generate APK(s)** (así lo escribe la web oficial) (**comprobar en pantalla** cuál sale; en versiones antiguas y en guías viejas: *Build Bundle(s) / APK(s) → Build APK(s)*). Al terminar sale un aviso con un enlace para abrir la carpeta: `app-ako/app/build/outputs/apk/`. No vale la APK que genera **Run ▶**: esa va marcada como «solo pruebas» y solo se instala por `adb`.

### Leer un error de compilación

1. Abrir la pestaña **Build** (abajo).
2. Buscar la primera línea roja que empieza por `e:`. Tiene esta forma: `e: file:///.../Mesa.kt:12:5 Unresolved reference: nombre`.
3. Eso dice: archivo `Mesa.kt`, línea 12, columna 5, y el mensaje.
4. Seleccionar esa línea (y las dos o tres siguientes) → `Ctrl+C` → pegar en el chat. Claude dice qué cambiar.

## 6. Ver la app y la base de datos

### Logcat

1. Con la app corriendo, pestaña **Logcat** (abajo).
2. En la caja de filtro de arriba escribir `package:mine` → solo salen mensajes de Ako.
3. Se puede añadir una palabra: `package:mine tag:Ako` si Claude lo pide.
4. Las líneas rojas son errores; si la app se cierra sola, copiar la primera línea roja que diga `FATAL EXCEPTION` y las diez siguientes al chat.
5. [Claude] Para ver solo los cierres: `package:mine is:crash`.

### Database Inspector (ver las tablas de Room)

1. La app tiene que estar corriendo en el emulador.
2. **View → Tool Windows → App Inspection** (o su icono en el borde) → pestaña **Database Inspector**.
3. Elegir el proceso `yunkang.ako` en el desplegable.
4. A la izquierda (panel *Databases*) aparece la base de datos y sus tablas; doble clic en una tabla muestra sus filas.
5. Marcar **Live updates** para que se refresque solo mientras usas la app. Con *Live updates* marcado la tabla es de **solo lectura**.
6. [Claude] El inspector también deja cambiar datos (doble clic en una celda) y ejecutar SQL de escritura (`UPDATE`, `INSERT`, `DELETE`) en **New Query**. **No hacerlo** salvo que Claude lo pida para una prueba: cambia la base de datos de verdad.

### Captura del emulador

1. En la barra de herramientas del emulador (en el panel *Running Devices*) hay un botón **Take screenshot** (icono de **cámara**). Clic.
2. Se abre la ventana *Take Screenshot*: **Save** → elegir carpeta (por ejemplo el Escritorio) → guardar. El nombre por defecto es `Screenshot_aaaammdd-hhmmss.png`.
3. Arrastrar la imagen al chat.
4. [Claude] Usar el botón, no `Ctrl+S`: la documentación oficial no da ese atajo para capturas y dentro de Android Studio `Ctrl+S` es *Save All*.

### Meter una foto en el emulador (S12, prueba P-M-08) [Claude]

1. Arrastrar el archivo desde el Explorador de Windows a la pantalla del emulador.
2. Se guarda en `/sdcard/Download/` del emulador. Se ve en la app **Files** (o *Downloads*) del emulador y en **Device Explorer** (abajo).
3. Abrir el **selector de fotos** de Ako y ver si sale (**comprobar en pantalla**: la documentación no dice si una foto arrastrada aparece ahí). Si no sale, avisar a Claude antes de probar otra cosa.

### Ver los archivos privados de la app (Device Explorer) [Claude]

1. **View → Tool Windows → Device Explorer**. Elegir el emulador en el desplegable.
2. Ir a `data/data/yunkang.ako/`: ahí están la base de datos (`databases/`) y los archivos que guarda la app (`files/`).
3. Solo mirar: doble clic abre una copia en Android Studio; clic derecho → guardar (*Save*) la copia en el PC. No borrar nada.
4. La imagen del emulador es «Google APIs» y con ella casi todo el sistema está oculto; la carpeta de una app de depuración como Ako sí se abre (**comprobar en pantalla**).

## 7. Pruebas

### Una clase de test

1. Abrir el archivo de test (en `app/src/test/...` o `app/src/androidTest/...`).
2. En el margen izquierdo, junto a `class ...Test`, hay un **triángulo verde**. Clic → **Run '...Test'**.
3. Para una sola prueba, el triángulo está junto a esa función `@Test`.
4. El resultado sale en la pestaña **Run**: barra **verde** = todas pasan; **roja** = alguna falla, con el nombre de la que falla a la izquierda y el motivo a la derecha. Copiar el motivo al chat.

### Todas las pruebas de `test`

1. Antes, que no haya nada pendiente de sincronizar (*Sync Now*). Clic derecho en la carpeta `app/src/test/java` → la opción que empieza por **Run** (algo como *Run 'Tests in 'ako''*; el nombre entre comillas cambia según la carpeta).
2. Mismo resultado en **Run**.
3. Las de `androidTest` se ejecutan igual pero necesitan el emulador arrancado y tardan más.

## 8. Git: solo mirar

- **No usar** el botón **Commit** (arriba a la derecha o `Ctrl+K`), ni el menú **Git**, ni **Push**. Los commits los hace Claude al cerrar la sesión, con la ficha del diario.
- **Enable Version Control Integration** no es un aviso: es una opción del menú **VCS**. No pulsarla, ni nada que diga **Create Git Repository**: crean un repositorio dentro de `app-ako/` (ver la guía de la S1, «Tres cosas que NO hay que hacer»).
- Si al crear un archivo sale **Add File to Git**: cancelar (el `git add` lo hace Claude). Para que no vuelva a preguntar: **File → Settings → Version Control → Confirmation** → *When files are created*: **Do not add**.
- Si Android Studio avisa de un repositorio Git sin registrar en **`C:\AKO`**: **Add root** (no crea nada, solo lo usa). Si nombra otra carpeta: **Ignore** y avisar a Claude.
- Si en algún asistente de proyecto sale la casilla **Create Git repository**: desmarcarla.
- Sí se puede abrir el panel **Git** (`Alt+9`) y su pestaña **Log** para ver el historial. Solo lectura: no pulsar nada que diga reset, revert, rebase o checkout.

## 9. Lo que NO tocar

1. **No actualizar Android Studio** ni sus plugins aunque salga un aviso abajo a la derecha. Decisión del proyecto: todos trabajamos con la misma versión. Cerrar el aviso o **Ignore**. [Claude] Excepción ya prevista: si al sincronizar dice que la versión de AGP no es compatible con este Android Studio no cerrar sin más: captura y al chat. No debería salir: el proyecto usa AGP 9.3.3, que este Android Studio admite (P42, guía de la S1, apartado 4).
2. **No aceptar** el **Upgrade Assistant** ni ningún aviso de «migrate» o «upgrade» de Gradle o AGP. Cerrar el aviso.
3. **No abrir SDK Manager** para instalar o quitar nada. El SDK de `C:\Android\Sdk` ya está como tiene que estar.
4. **No borrar** a mano las carpetas `.gradle` ni `build`. Si hace falta limpiar: **Build → Clean Project**.
5. **No cambiar** el ajuste **File → Settings → Build, Execution, Deployment → Build Tools → Gradle** (JDK, distribución, etc.).
6. Si un aviso no encaja en esta lista y no está claro, captura y al chat antes de pulsar nada.

## 10. Atajos que valen la pena

| Atajo | Qué hace |
|---|---|
| `Ctrl+S` | Guardar |
| `Ctrl+Z` | Deshacer (`Ctrl+Mayús+Z` rehace) |
| `Ctrl+Mayús+F` | Buscar un texto en todo el proyecto |
| `Mayús Mayús` (doble Mayús) | Buscar cualquier cosa: archivos, clases, acciones |
| `Ctrl+B` | Ir a donde está definido lo que hay bajo el cursor |
| `Alt+Enter` | Arreglo rápido (importar, crear, corregir) |
| `Ctrl+Alt+L` | Ordenar el formato del archivo |
| `Mayús+F10` | Run ▶ |
| `Ctrl+F9` | Compilar sin ejecutar (desde Meerkat lanza *Build 'app'*) |
| `Alt+1` | Mostrar u ocultar el panel Project |
| `Alt+9` | Panel Git (solo mirar) |
| `Ctrl+Alt+S` | Abrir Settings |

## 11. Cuando algo va mal

### «Gradle sync failed»

1. Pestaña **Build** (pestaña interna *Sync*) → copiar la primera línea roja y las siguientes → al chat.
2. Si Claude lo pide: icono del elefante para reintentar.
3. Último recurso, solo si Claude lo dice: **File → Invalidate Caches…** → **dejar las casillas sin marcar** → **Invalidate and Restart**. Tarda varios minutos al volver. (La primera casilla, *Clear file system cache and Local History*, borra también el historial local de cambios de Android Studio: no hace falta.)

### «Device not found» / «No target device found»

1. Desplegable de dispositivos → elegir `Pixel_6_API_34`.
2. Si no está: **Device Manager** → **▶** junto a `Pixel_6_API_34`.
3. Si sigue sin estar: captura del Device Manager al chat.

### El emulador se queda en negro

1. Esperar un minuto; la primera vez del día tarda.
2. Si sigue negro: cerrar la ventana del emulador con su `x`, esperar diez segundos, **Run ▶** otra vez.
3. Si se repite: **Device Manager** → menú `⋮` junto al dispositivo → **Stop** (si sigue encendido) y después, en el mismo menú, **Cold Boot** (arranque en frío: como encender un móvil apagado, sin el estado guardado).

### «Unresolved reference: nombre»

1. Falta un import o el nombre está mal escrito.
2. Clic sobre la palabra roja → `Alt+Enter` → si ofrece **Import**, aceptar.
3. Si no lo ofrece: comparar letra a letra con lo que dio Claude (mayúsculas incluidas). Si coincide, copiar la línea `e:` de **Build** al chat.

### La app se cierra sola al abrirla

1. Pestaña **Logcat** con `package:mine` (o `package:mine is:crash`).
2. Buscar `FATAL EXCEPTION`, copiar esa línea y las diez siguientes → al chat.

### Nada de lo anterior encaja

1. Captura de pantalla completa de Android Studio (`Win+Mayús+S`) y al chat, con una línea de lo que se estaba haciendo.

## Comprobado contra la documentación oficial (25 sep 2026) [Claude]

- https://developer.android.com/studio/intro/user-interface — zonas de la ventana; *tool window bar* con los botones de los paneles en los bordes; `Alt+1` Project, `Alt+9` Version Control.
- https://developer.android.com/studio/intro/keyboard-shortcuts — atajos de la tabla del apartado 10 (Windows/Linux): `Ctrl+S` Save all, `Ctrl+Mayús+F`, doble `Mayús`, `Ctrl+B`, `Alt+Enter`, `Ctrl+Alt+L`, `Mayús+F10`, `Ctrl+F9`, `Alt+1`, `Ctrl+Alt+S`, `Ctrl+F4`, `Ctrl+G`, `Ctrl+K`. https://www.jetbrains.com/help/idea/undoing-and-redoing-changes.html — `Ctrl+Z` / `Ctrl+Mayús+Z`.
- https://developer.android.com/build y https://developer.android.com/build/releases/about-agp — *Sync Now* y *File → Sync Project with Gradle Files*; tabla de compatibilidad Quail 3 → AGP hasta 9.3, Quail 4 → hasta 9.4.
- https://developer.android.com/studio/run — ventana *Build* con pestaña *Sync*; *View → Tool Windows → Build*.
- https://developer.android.com/studio/releases/past-releases/as-meerkat-release-notes — *Rebuild Project* pasa a llamarse *Clean and Assemble Project with Tests*; *Make Project* pasa a *Assemble Project*; `Ctrl+F9` lanza *Build 'configuración'*.
- https://developer.android.com/build/build-for-release — menú Build actual: *Clean Project* sigue existiendo; *Generate Bundle(s) / APK(s) → Generate APK(s)*; APK en `módulo/build/outputs/apk/`; la APK de *Run* lleva `testOnly` y solo se instala por `adb`.
- https://developer.android.com/studio/views/layout-editor — botones *Code / Split / Design* arriba a la derecha; `Alt+Mayús+flecha` para cambiar; *New → Layout Resource File*.
- https://www.jetbrains.com/help/idea/settings-auto-import.html y github.com/JetBrains/intellij-community (`KotlinCodeInsightWorkspaceSettingsProvider.kt`, `KotlinCopyPasteReferenceProcessor.kt`, `CodeInsightSettings.java`, `PackageDirectoryMismatchInspection.kt`, `KotlinBundle.properties`) — bloque Kotlin de *Auto Import* con solo dos casillas; *Insert imports on paste* en el bloque Java, por defecto *Always*, respetada al pegar Kotlin; arreglos rápidos *Change file's package to …* y *Move file to …*.
- https://developer.android.com/studio/debug/logcat — *View → Tool Windows → Logcat*; `package:mine` = paquetes del proyecto abierto; `is:crash`.
- https://developer.android.com/studio/inspect/database — *View → Tool Windows → App Inspection → Database Inspector*; *Live updates* deja la tabla en solo lectura; se pueden editar celdas y ejecutar `UPDATE`/`INSERT`/`DELETE` en consultas propias; necesita API 26 o superior.
- https://developer.android.com/studio/run/emulator y https://developer.android.com/studio/run/emulator-take-screenshots — botón *Take screenshot*; ventana con *Save*, nombre `Screenshot_aaaammdd-hhmmss.png` y carpeta a elegir. https://developer.android.com/studio/run/emulator-extended-controls — la carpeta fija de capturas solo se configura en *Extended controls → Settings* (emulador en ventana aparte). https://developer.android.com/studio/run/emulator-launch-separate-window — el emulador va dentro de Android Studio (*Running Devices*) por defecto.
- https://developer.android.com/studio/run/emulator-install-add-files — arrastrar un archivo al emulador lo deja en `/sdcard/Download/`; se ve en Device Explorer o en la app Downloads/Files. No dice nada del selector de fotos.
- https://developer.android.com/studio/debug/device-file-explorer — *View → Tool Windows → Device Explorer*; `data/data/nombre_app/` = almacenamiento interno de la app; aviso de que con imágenes Google APIs casi todo está oculto y de que las apps no depurables no se abren.
- https://developer.android.com/studio/run/managing-avds y https://developer.android.com/studio/run/emulator-snapshots — *Device Manager*: *Launch*, menú → *Stop*, *Wipe Data*; arranque en frío con **Cold Boot** desde el menú del dispositivo (no «Cold Boot Now»).
- https://developer.android.com/studio/test/test-in-android-studio — clic derecho en carpeta o archivo → *Run*; triángulo verde en el margen; resultados en la ventana *Run*; sincronizar antes.
- https://www.jetbrains.com/help/idea/invalidate-caches.html — *File → Invalidate Caches…*, casillas opcionales, *Invalidate and Restart*.
- https://www.jetbrains.com/help/idea/enabling-version-control.html, https://www.jetbrains.com/help/idea/set-up-a-git-repository.html, https://www.jetbrains.com/help/idea/adding-files-to-version-control.html y https://www.jetbrains.com/help/idea/log-tab.html — *VCS → Enable Version Control Integration* crea el repositorio en la raíz del proyecto; aviso de raíces sin registrar (*Add roots* / *Ignore*); *Settings → Version Control → Confirmation → When files are created: Do not add*; pestaña *Log* del panel `Alt+9`.
