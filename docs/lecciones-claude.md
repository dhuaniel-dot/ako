# Lecciones para Claude Code (se cargan con CLAUDE.md)

Una línea por cosa que Claude hizo mal o que costó más de lo debido y que puede repetirse, con la regla que lo evita. Se añade al cerrar la sesión (o en el relevo) y se quita cuando deja de hacer falta. Corto a propósito: se lee en cada chat. Lo que es un problema de código y su solución va a la ficha del diario, no aquí.

- (24 sep 2026) Lo que Claude Code escribe bajo `AppData` acaba en la carpeta privada de la app de Claude y Android Studio no lo ve: instalar y guardar fuera de `AppData` (`C:\Android\Sdk`, `C:\AKO`).
- (25 sep 2026) Compilar por consola no prueba que Android Studio sincronice el proyecto: el IDE limita la versión de AGP (tabla de developer.android.com/build/releases/about-agp). Antes de fijar o subir AGP, mirar esa tabla contra la versión instalada (`product-info.json`).
- (25 sep 2026) Varios subagentes escribiendo guías que dependen unas de otras divergen en nombres y contratos (`Formato.precio` con o sin «€», quién crea cada modelo, en qué sesión se repite cada prueba): fijar antes un brief común con nombres, calendario y contratos, y cerrar con una revisión cruzada por agentes que no las escribieron.
- (25 sep 2026) Si el límite de uso corta a un subagente, lo escrito en disco se conserva pero su informe final se pierde: que escriban el archivo pronto y retomarlos con `SendMessage` (no relanzarlos desde cero).
- (25 sep 2026) En Git Bash, `local.properties` con `sdk.dir=C\:\\Android\\Sdk` da «Invalid file path»; con barras normales (`sdk.dir=C:/Android/Sdk`) funciona.
- (24 sep 2026) `set JAVA_HOME=…` no funciona ni en PowerShell ni en Git Bash; Gradle usa el JBR de Android Studio por `~/.gradle/gradle.properties`, no hace falta tocar el entorno.
- (29 sep 2026) Los permisos de la app no dejan a Claude editar los hooks de `.claude/hooks/` (protección contra que la IA cambie sus propias reglas): dar el bloque a Daniel para que lo pegue (Bloc de notas: `notepad <ruta>`) y probar el hook ejecutándolo con un JSON de ejemplo.
