# `C:\AKO\` — la carpeta del prototipo: documentación al día para Claude Code y, desde la S1, la app

Creada el 24 de septiembre de 2026 por el chat de la fase 5b (preparación del código). **Se edita.** Es **la raíz del repositorio Git del prototipo** (decisión de Daniel, 24 sep: fuera de `Proyecto intermodular\` y fuera de OneDrive). El proyecto de Android Studio se crea aquí dentro en la S1, al lado de `docs\` y `CLAUDE.md`. Desde la S1 aquí escribe Claude Code; los chats de Claude solo actualizan `docs\` (copias) y con confirmación de Daniel.

**Manda esta carpeta, no `Proyecto intermodular\Claude Code\`** (copias del 17 sep, ya superadas: filas del registro aplicadas aquí el 24 sep). Los `.md` de `docs/` son copias corregidas de los documentos del Project de Claude; la versión de referencia sigue siendo la del Project (`claude/*.md`) y `Documentos del proyecto\`; cuando cambie un documento allí, se vuelve a copiar aquí.

## Orden de lectura (para Claude Code y para Daniel)

1. `CLAUDE.md` — reglas de trabajo, stack fijo, vocabulario obligatorio, lo que no se hace nunca.
2. `docs/spec-claude-code.md` — el diseño completo y el orden de construcción en 13 sesiones. **Entero, la primera vez.**
3. `docs/spec+doc-requisitos.md` y `docs/estado-nivel.md` — qué hay que construir (53 RF) y en qué estado está cada cosa.
4. `docs/spec+doc-clases.md` — los nombres de clases, métodos y paquetes (borrador).
5. `docs/spec+doc-pantallas.md` y `docs/wireframes/` — al programar cada pantalla: su ficha y su dibujo.
6. `docs/spec+doc-pruebas.md` — al cerrar cada sesión: qué pruebas la cierran.
7. `docs/plantilla-diario.md` — al empezar y cerrar cada sesión: la ficha del diario.

## Qué es cada archivo

| Archivo | Qué es | Origen (Project / PC) |
|---|---|---|
| `ANALISIS-INICIAL.md` | El análisis de Claude Code de la noche del 24 sep (plan, sesiones, contradicciones, instalación, skills, chats y las 36 preguntas contestadas en la sesión 00) [Claude] | Escrito por Claude Code |
| `docs/decisiones-code.md` | Registro de lo decidido en Claude Code (respuestas de la sesión 00 y decisiones de cada sesión): lo lee cada chat nuevo para no volver a preguntar. El apartado 5.2 tiene las preguntas P42–P103 de la noche del 24 al 25 sep, **ya contestadas** [Claude] | Nace en el repositorio (sesión 00) |
| `docs/textos-ui.md` | Inventario de las 141 cadenas del nivel 1 con clave propuesta para `strings.xml`, fuente y las 20 discrepancias entre fuentes [Claude] | Nace en el repositorio (sesión 00) |
| `docs/guias/` | Guías por sesión: `sesion-01.md` paso a paso (comprobada contra developer.android.com el 25 sep) y **`sesion-02.md` a `sesion-13.md`** como planes de piezas sin código (S6, S8 y S9, las difíciles, con los huecos de su Plan Mode); **`juego-de-datos.md`** (cómo montar los datos de las pruebas desde instalación limpia y el orden de las 29 pruebas manuales sesión por sesión, con qué queda *Parcial* y hasta cuándo); **`readme-borrador.md`** (el `README.md` que Daniel lee y pega en la S1); `android-studio-basico.md` (la herramienta para quien nunca la ha abierto, comprobada contra la documentación oficial); `strings-es-borrador.xml` (129 cadenas y 2 plurals del nivel 1 listos para pegar en su sesión) y `stack-verificado/` (los archivos de Gradle comprobados compilando el 24 sep; ver P42 y P43 antes de usarlos) [Claude] | Nace en el repositorio (sesión 00; S6–S13, juego de datos y README la noche del 24 al 25) |
| `docs/plantilla-en-curso.md` · `docs/plantilla-readme.md` | Plantillas del `EN-CURSO.md` (relevo entre chats) y del `README.md` de la S1 [Claude] | Nace en el repositorio (sesión 00) |
| `.claude/` | Skills del repositorio (`abrir-sesion`, `cerrar-sesion`, `relevo`, `verificar`, `como-trabajamos`), el subagente `agents/revisor.md`, los hooks `comprobar-commit.sh` (bloquea commits sin `S<N>:`, sin ficha o con atribución) y `al-empezar.sh` (mete EN-CURSO y la última ficha en el contexto), y `settings.json` (hooks y comandos destructivos prohibidos). Se suben al repositorio [Claude] | Escrito por Claude Code en la sesión 00 |
| `.gitignore` · `.gitattributes` | Lo que no se sube (build, .idea, local.properties…) y los finales de línea (sh en LF, bat en CRLF) [Claude] | Sesión 00 |
| `PRIMER-MENSAJE-para-Claude-Code.md` | El mensaje que Daniel pega en la primera sesión de Claude Code (con la nota de cómo abrirla: sesión Local con `C:\AKO` como carpeta del proyecto) | Escrito en la 5b |
| `LEEME.md` | Este índice | Escrito en la 5b |
| `CLAUDE.md` | Reglas de trabajo entre Daniel y Claude Code. Claude Code lo lee solo en cada sesión | `spec-claude-md.md`, desde la línea `# Ako` (versión 5b: eliminar ≠ desactivar, Resumen de ingresos, commits sin atribución) |
| `docs/spec-claude-code.md` | Diseño completo y orden de construcción: stack, paquetes, niveles e incrementos, 14 tablas, 15 reglas, seguridad, imágenes, estilo, 13 sesiones, pruebas, diario, Git, qué pasa después de la S13 | `spec-claude-code.md` (versión 5b, con las filas 3, 5, 7, 34-36, 38, 39, 43, 48, 51 y 53 del registro aplicadas) |
| `docs/spec+doc-pantallas.md` | Fichas de las siete pantallas | `spec+doc-pantallas.md` (fila 32: P224) |
| `docs/wireframes/*.png` | Los 21 dibujos del nivel 1 (14 vistas y 7 diálogos). `02g-resumen-ingresos.png` es el que en `Imágenes\Wireframes\` todavía se llama `02g-resumen-por-dia` (fila 6: lo renombra Daniel) | `Imágenes\Wireframes\` (versión del 23 sep: `02a`, `02b` y `05a` redibujados por P223 y P224) |
| `docs/wireframes/spec+doc-wireframes.md` | Las 21 leyendas numeradas y las decisiones de disposición; enlaza cada PNG | `spec+doc-wireframes.md` (filas 6, 10 y 30) |
| `docs/spec+doc-requisitos.md` | 53 RF (29 · 22 · 2) y 25 RNF | `spec+doc-requisitos.md` (recuento del nivel 2 corregido a 22) |
| `docs/spec+doc-clases.md` | Borrador del diagrama de clases: 58 clases, cinco figuras, código PlantUML | `spec+doc-clases.md` (filas 10, 47, 49 y 52: `ResumenIngresos…`, `Validacion`, repositorios en `datos/`) |
| `docs/spec+doc-pruebas.md` | Plan de pruebas: 29 P-M + 9 P-C, con las columnas de resultado vacías | `spec+doc-pruebas.md` (fila 31: P224) |
| `docs/estado-nivel.md` | Tabla diseñado / implementado, 53 filas en *diseñado* | Generada en la 5b desde los RF (P247 → A) [Claude] |
| `docs/plantilla-diario.md` | Plantilla de la ficha de diario por sesión | `proceso-plantilla-diario-desarrollo.md`, apartados 1 y 2 |
| `docs/diario/` | Una ficha por sesión: `sesion-01.md`, `sesion-02.md`… (vacía hasta la S1) | — |

## Lo que NO está aquí, a propósito

- `spec+doc-diseno-app.md` (el spec principal) y las figuras del E-R (P134): lo que Claude Code necesita de ellos está en `docs/spec-claude-code.md`; una sola fuente evita contradicciones. Si Claude Code pregunta por algo que no encuentra, se busca en el Project y se le copia aquí.
- El registro de decisiones (`diario+doc-decisiones.md`) y los documentos de proceso: viven en el Project de Claude.
- Los PDF oficiales de la FP: en `FP\`.
