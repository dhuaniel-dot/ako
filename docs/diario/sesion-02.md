# Sesión 02 — 2026-09-30

| Campo | Valor |
|---|---|
| **Fecha** | 2026-09-30 |
| **Sesión nº** | 02 |
| **Objetivo de la sesión** | 14 entidades, `Converters`, `AppDatabase`, `Precarga` (sin `Converters` desde P113): compila y, al arrancar, el inspector de base de datos enseña las 14 tablas con *Otros*, 60 mesas, 14 alérgenos, 3 etiquetas y el ejemplo (Bebidas / Agua, P7) |
| **Tiempo dedicado** | Inicio 11:16 – … |
| **Nivel / pieza** | Nivel 1 · capa de datos (entidades Room y precarga, RF-50) |
| **Commit final** | `abc1234` — mensaje del commit |

## Qué se hizo

- Lista de lo que existe al terminar y no existía al empezar (clases, pantallas, pruebas que pasan).
- Qué se dejó a medias y en qué estado exacto (para retomarlo sin adivinar).

## Problemas y soluciones

| # | Qué falló | Cómo se resolvió | Justificación (por qué esta solución y no otra) | Fuente |
|---|---|---|---|---|
| 1 | Pieza 1: los paquetes salían fuera de `yunkang.ako` (`java/dato/dao`, `java/datos/repositorios`), uno con mayúscula (`Datos`) y `entidades` suelto en vez de dentro de `datos` | Claude quitó las carpetas vacías (`rmdir`, que solo borra carpetas vacías) y Daniel las volvió a crear con clic derecho **sobre `yunkang.ako`** y el nombre con punto (`datos.entidades`) | Clic derecho sobre `java` crea el paquete en la raíz; el paquete tiene que coincidir con la línea `package yunkang.ako.…` de cada archivo. Rehacerlas vacías era más rápido que moverlas | Propio |

## Qué entendí y qué no

- **Entendí** (repaso de la S1, 30 sep, palabras de Daniel): **Gradle**: «es quien hace que todo lo que hemos escrito de la app funcione, en el sentido de darle significado a las cosas y de compilar» (faltaba: también descarga las librerías y fabrica el APK) · **catálogo de versiones**: la versión de Room se cambia «en lo de librerías de Gradle» (`libs.versions.toml`; faltaba el porqué: un solo sitio para todo el proyecto) · **`minSdk 26`**: «no estaría la función del PIN, ya que fue en la 26 donde se implementó». · **RESTRICT**: «no se puede [borrar Bebidas] ya que [Agua] sigue apuntando» · **índice único** (número de plato): «para que se pueda identificar y no se confundan».
- **`dominio`**, primer intento: «un espacio en blanco donde actuar con más libertad». Tras el ejemplo de la libreta de reglas eligió bien (B): reglas y cuentas sin nada de Android, que se prueban en el ordenador sin emulador. Dudaba si «sin nada de Android» quería decir «sin código»: es código Kotlin normal que no usa ninguna clase de Android.
- **CASCADE**, primer intento: creía que al borrar una comanda se borra todo, y al borrar un plato sus modificadores. Corregido: la única CASCADE es `linea_modificador → linea_comanda` (al quitar una línea se van los extras elegidos en esa línea); comandas y platos no se borran nunca.
- **No entendí todavía:** lo que funciona pero no sabría explicar. Se vuelve a ello en la sesión siguiente.

## Pruebas

- Pruebas de código que pasan al terminar: P-C-nn, …
- Pruebas manuales ejecutadas en esta sesión: P-M-nn (resultado y fecha ya apuntados en `spec+doc-pruebas.md`).

## Uso de IA en esta sesión

- Repaso de la S1 y conceptos de base de datos: Claude explicó con ejemplos; Daniel contestó con sus palabras.
- Pieza 1 (paquetes): los creó Daniel en Android Studio; Claude quitó las carpetas vacías mal puestas y comprobó el disco.
- Pieza 2, decisión P113: Claude buscó las opciones (una en las notas de versión oficiales de Room) y Daniel eligió B con su propio argumento; Claude actualizó spec y guías.

## Siguiente sesión

- Con qué se empieza, en una línea.
