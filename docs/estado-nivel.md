> **Estado del nivel — tabla diseñado / implementado (P137).** Una fila por requisito funcional (los 53 RF de `spec+doc-requisitos.md`), agrupados por nivel y, en el nivel 2, por incremento. **Se actualiza al cerrar cada sesión** (es parte del ritual: `spec-claude-code.md`, apartado 11). Es la fuente de la tabla *diseñado / implementado* de los apartados 5 y 7 de la memoria.
> Generada en la fase 5b (24 de septiembre de 2026, P247 → A) con las 53 filas en *diseñado* [Claude]. Estados posibles: `diseñado` · `en curso` · `implementado` · `implementado, no probado`. En *Sesión* va el número de la sesión que lo implementó (S1…S13) o del incremento (inc. N).

# Estado del nivel — Ako

**El nivel 1 (29 RF) es lo único obligatorio: el objetivo de la fase es el prototipo.** El nivel 2 (22 RF, 12 incrementos en orden) entra solo cuando el nivel 1 esté terminado, probado y documentado **y la memoria (el doc) terminada**, y solo si queda tiempo; cada incremento entra **entero** (sus tablas, columnas, precarga y función) y después se actualiza el doc (**P115**, Daniel, 30 sep 2026; sustituye a P3). Nada de código «por si acaso» que no use el nivel 1. El nivel 3 (2 RF) solo si sobra. Lo que no entre se declara *diseñado, no implementado* en la memoria.

| Nivel | RF | Estado |
|---|---|---|
| 1 | 29 | **29 implementados** (todos; los tres últimos, RF-04, RF-09 y RF-30, en la S12; la pasada final de la S13, 8 oct 2026, los confirmó a los 29 sin cambiar ningún estado) |
| 2 | 22 | 0 implementados |
| 3 | 2 | 0 implementados |

## Nivel 1 — obligatorio (29 RF)

| RF | Función | Pantalla · vista | Actor | Nivel | Estado | Sesión |
|---|---|---|---|---|---|---|
| RF-01 | Crear el PIN en el primer arranque, escrito dos veces, con aviso de que no se recupera | 1 · 1b | Propietario | 1 | implementado | S5 |
| RF-02 | Entrar en Propietario introduciendo el PIN; error con sacudida e intentos ilimitados | 1 · 1c | Propietario | 1 | implementado | S5 |
| RF-03 | Cambiar el PIN (actual + nuevo dos veces) | 1 · 1e | Propietario | 1 | implementado | S5 |
| RF-04 | Ver la carta en modo edición: cajas por categoría con sus platos, incluidos los **eliminados** marcados con la palabra *Eliminado* | 2 · 2a | Propietario | 1 | implementado | S6 |
| RF-05 | Crear y editar una categoría (nombre, foto, activo); la categoría por defecto sin interruptor ni flechas | 2 · 2b | Propietario | 1 | implementado | S6 |
| RF-06 | **Eliminar** una categoría avisando de las mesas con sus platos en comandas pendientes, sin borrar líneas | 2 · 2b, 2e | Propietario | 1 | implementado | S6 |
| RF-08 | Crear y editar un plato: nombre, número, precio y categoría obligatorios; descripción y activo | 3 · 3a | Propietario | 1 | implementado | S7 |
| RF-09 | Añadir foto al plato: elegir, redimensionar (~1080px), comprimir a JPEG, guardar como archivo | 3 · 3a | Propietario | 1 (última pieza) | implementado | S12 |
| RF-10 | Marcar los alérgenos del plato entre los 14 legales | 3 · 3a | Propietario | 1 | implementado | S7 |
| RF-11 | **Eliminar** un plato avisando de las mesas afectadas, sin borrar líneas | 3 · 3d | Propietario | 1 | implementado | S7 |
| RF-12 | Al guardar un plato en una categoría **eliminada**, cadena de dos avisos (mover a la categoría por defecto / recuperarla con contador) | 3 · 3e | Propietario | 1 | implementado | S7 |
| RF-20 | Resumen de ingresos: selector de fecha (por defecto, hoy); lista de las comandas PAGADAS con `fecha_cierre` en ese día, con mesa, hora e importe; abajo, cuántas comandas y el total; tocar una abre su recibo en solo lectura | 2 · 2g | Propietario | 1 | implementado | S10 |
| RF-24 | Elegir rol en el selector: Propietario, Pedir, Cuenta | 1 · 1a | Cliente (Pedir) | 1 | implementado | S5 |
| RF-25 | Puerta de Pedir: no entrar sin ningún plato visible, con dos mensajes según el motivo | 1 | Cliente (Pedir) | 1 | implementado | S8 |
| RF-26 | Elegir la mesa en la rejilla en modo *elegir* (colores y totales visibles, toda mesa elegible) | 1 · 1d | Cliente (Pedir) | 1 | implementado | S8 |
| RF-28 | Salir de Pedir con botón visible o Atrás, pidiendo el PIN | 1 · 1c | Cliente (Pedir) | 1 | implementado | S8 |
| RF-29 | Consultar la carta: categorías + una lista por secciones; tocar una categoría salta a su sección; solo platos visibles | 5 · 5a | Cliente (Pedir) | 1 | implementado | S8 |
| RF-30 | Ver la ficha del plato: foto (o "?"), número, nombre, precio, descripción, alérgenos desplegables (o aviso *"pregunta al personal"*) | 5 · 5b | Cliente (Pedir) | 1 | implementado | S8 |
| RF-36 | Añadir al carrito con cantidad 1-99; +/−, quitar; líneas idénticas se suman | 5 · 5c | Cliente (Pedir) | 1 | implementado | S8 |
| RF-37 | Enviar con confirmación (mesa y total): crea la comanda o añade líneas; congela nombre y precio | 5 · 5c | Cliente (Pedir) | 1 | implementado | S8 |
| RF-38 | Avisar al salir con platos sin enviar | 5 / 1 | Cliente (Pedir) | 1 | implementado | S8 |
| RF-40 | Ver la rejilla de 60 mesas en modo *gestionar*: blanco / rojo con total / verde con lo cobrado | 6 · 6a | Camarero (Cuenta) | 1 (verde: 3) | implementado | S9 |
| RF-41 | Ver la comanda de una mesa: líneas, modificadores y total calculado | 6 · 6b | Camarero (Cuenta) | 1 | implementado | S9 |
| RF-42 | Anular la comanda con confirmación → ANULADA con `fecha_cierre`, mesa a blanco | 6 · 6b | Camarero (Cuenta) | 1 | implementado | S9 |
| RF-43 | Mostrar el recibo en pantalla con los nombres congelados en español | 6 · 6c | Camarero (Cuenta) | 1 | implementado | S9 |
| RF-44 | Calcular el cambio desde un importe entregado opcional (negativo si falta) | 6 · 6c | Camarero (Cuenta) | 1 | implementado | S9 |
| RF-45 | Cobrar con confirmación → PAGADA con `fecha_cierre` | 6 · 6c | Camarero (Cuenta) | 1 | implementado | S9 |
| RF-46 | Quitar una línea de la comanda desde Cuenta; aviso al quitar la última (la comanda pasa a ANULADA); los modificadores se van con la línea | 6 · 6b | Camarero (Cuenta) | 1 | implementado | S9 |
| RF-50 | Precargar al instalar: categoría por defecto, 60 mesas, 14 alérgenos, 1 categoría y 1 plato de ejemplo (sin etiquetas: llegan con el incremento 1, P115) | — | Sistema | 1 | implementado | S2 |

## Nivel 2 — incrementos, en este orden (22 RF)

### Incremento 1 — Etiquetas y chips (4 RF)

| RF | Función | Pantalla · vista | Actor | Estado | Sesión |
|---|---|---|---|---|---|
| RF-14 | Marcar etiquetas del plato entre las activas | 3 · 3a | Propietario | diseñado | — |
| RF-17 | Etiquetar varios platos a la vez eligiendo primero la etiqueta | 2 · 2c | Propietario | diseñado | — |
| RF-21 | Gestionar etiquetas: crear (solo nombre, único), renombrar, **eliminar** con contador de platos | 7 | Propietario | diseñado | — |
| RF-31 | Filtrar por etiquetas con chips apilables (un plato cumple todos los activos) | 5 · 5a | Cliente (Pedir) | diseñado | — |

### Incremento 2 — Modo kiosco (1 RF)

| RF | Función | Pantalla · vista | Actor | Estado | Sesión |
|---|---|---|---|---|---|
| RF-27 | Interruptor *"Se la doy al cliente"*: fijar pantalla (kiosco) o modo camarero con cambio de mesa sin salir | 1 · 1d | Cliente (Pedir) | diseñado | — |

### Incremento 3 — Modo Agrandar (1 RF)

| RF | Función | Pantalla · vista | Actor | Estado | Sesión |
|---|---|---|---|---|---|
| RF-35 | Activar el Modo Agrandar (magnificación) | 5 · 5a | Cliente (Pedir) | diseñado | — |

### Incremento 4 — Modificadores (2 RF)

| RF | Función | Pantalla · vista | Actor | Estado | Sesión |
|---|---|---|---|---|---|
| RF-13 | Añadir modificadores al plato (nombre, tipo AÑADIR/QUITAR, precio; QUITAR siempre a 0) | 3 · 3b | Propietario | diseñado | — |
| RF-33 | Elegir modificadores en la ficha; los de añadir con cantidad 1-9 | 5 · 5b | Cliente (Pedir) | diseñado | — |

### Incremento 5 — Tabla nutricional (2 RF)

| RF | Función | Pantalla · vista | Actor | Estado | Sesión |
|---|---|---|---|---|---|
| RF-15 | Rellenar la tabla nutricional (7 apartados en kJ y mg; kcal calculadas) | 3 · 3c | Propietario | diseñado | — |
| RF-32 | Ver la información nutricional en desplegable (campos si los hay; si no, la foto) | 5 · 5b | Cliente (Pedir) | diseñado | — |

### Incremento 6 — Vista previa (1 RF)

| RF | Función | Pantalla · vista | Actor | Estado | Sesión |
|---|---|---|---|---|---|
| RF-19 | Vista previa de la carta sin carrito, Añadir ni Enviar | 2 · 2f | Propietario | diseñado | — |

### Incremento 7 — Resumen de ingresos por periodos (1 RF)

| RF | Función | Pantalla · vista | Actor | Estado | Sesión |
|---|---|---|---|---|---|
| RF-52 | Resumen de ingresos por periodos: navegación años → meses → calendario con el total cobrado de cada día → día (que abre el Resumen de ingresos) | 2 · 2g | Propietario | diseñado | — |

### Incremento 8 — Orden ▲▼ y [Eliminar y recuperar] en masa (2 RF)

| RF | Función | Pantalla · vista | Actor | Estado | Sesión |
|---|---|---|---|---|---|
| RF-07 | Ordenar las categorías con flechas ▲▼; extremos y penúltima ▼ bloqueados | 2 · 2a | Propietario | diseñado | — |
| RF-18 | **Eliminar y recuperar** varios platos a la vez con un único aviso agrupado | 2 · 2d, 2e | Propietario | diseñado | — |

### Incremento 9 — Cuenta completa (3 RF)

| RF | Función | Pantalla · vista | Actor | Estado | Sesión |
|---|---|---|---|---|---|
| RF-51 | Añadir líneas y cambiar cantidades desde Cuenta (`[+ Añadir platos]` abre la carta en modo camarero fijada a la mesa; +/− en las líneas) | 6 · 6b | Camarero (Cuenta) | diseñado | — |
| RF-47 | Abrir la carta (pantalla 5) en modo camarero fijada a la mesa desde 6b, una mesa blanca o una verde; Enviar añade y vuelve a 6b | 6 → 5 | Camarero (Cuenta) | diseñado | — |
| RF-48 | Botón *Imprimir* que informa de que no hay impresora | 6 · 6c | Camarero (Cuenta) | diseñado | — |

### Incremento 10 — Idiomas y traducciones (3 RF)

| RF | Función | Pantalla · vista | Actor | Estado | Sesión |
|---|---|---|---|---|---|
| RF-22 | Gestionar idiomas: crear (nombre + código de dos letras, único, *ES* rechazado), editar, **eliminar**; avisos con contador | 8 · 8a, 8b, 8d | Propietario | diseñado | — |
| RF-23 | Traducir nombres de platos: dos listas, autoguardado con snackbar, copiar el nombre español, vaciar = pendiente | 8 · 8c | Propietario | diseñado | — |
| RF-34 | Cambiar el idioma de la carta; sin traducción, nombre en español | 5 · 5d | Cliente (Pedir) | diseñado | — |

### Incremento 11 — Foto de la tabla nutricional (1 RF)

| RF | Función | Pantalla · vista | Actor | Estado | Sesión |
|---|---|---|---|---|---|
| RF-16 | Subir una foto de la tabla nutricional como atajo | 3 · 3c | Propietario | diseñado | — |

### Incremento 12 — Agotado temporal (producto.disponible) (1 RF)

| RF | Función | Pantalla · vista | Actor | Estado | Sesión |
|---|---|---|---|---|---|
| RF-53 | **Desactivar y activar un plato** (agotado temporal, `producto.disponible`): sale de la carta del cliente, sigue visible en el Panel con la palabra *Desactivado* y vuelve con todo intacto | 3 · 3a · 2 · 2a | Propietario | diseñado | — |

## Nivel 3 — solo si sobra tiempo (2 RF)

| RF | Función | Pantalla · vista | Actor | Nivel | Estado | Sesión |
|---|---|---|---|---|---|---|
| RF-39 | Ver lo pedido por la mesa, solo lectura | 5 · 5e | Cliente (Pedir) | 3 | diseñado | — |
| RF-49 | Mesa verde: Liberar o Empezar comanda nueva | 6 · 6a | Camarero (Cuenta) | 3 | diseñado | — |

## Notas

- RF-09 (foto del plato) es nivel 1 pero **la última pieza** (S12): toda la app funciona sin fotos.
- RF-40: el blanco y el rojo de la rejilla son nivel 1; el **verde** (mesa cobrada) es nivel 3 y va con RF-49.
- RF-53 (agotado temporal): ~~la columna `producto.disponible` existe desde la S2 con `true` por defecto~~ → la columna `producto.disponible` **no existe en el nivel 1**: llega entera con el incremento 12, junto con la pantalla que la cambia (P115).
- S4 (1 oct 2026): ningún RF cambia de estado (los repositorios no son un RF). Las reglas R1–R16 del nivel 1 tienen dueño en el código (repositorios, DAOs o la base de datos); comprobado por el revisor independiente.
- S5 (1 oct 2026): RF-01, RF-02 y RF-03 → *implementado* (P-M-01, 02 y 03 pasan; el Panel al que lleva el PIN es todavía el provisional de la S5). RF-24 → *implementado, no probado*: el Selector y Propietario funcionan, pero Pedir y Cuenta abren una caja provisional hasta la S8/S9 (P-M-13 Parcial). RF-50 sigue igual (P-M-29 Parcial).
- S7 (5 oct 2026): RF-08 → *implementado* (P-M-07 pasa entera). RF-10, RF-11 y RF-12 → *implementado, no probado*: P-M-09, 10 y 11 quedan *Parcial* porque sus últimos pasos miran la carta (S8) o la Cuenta (S9). RF-09 (foto) sigue *diseñado* hasta la S12.
- S8 (6 oct 2026): RF-25, RF-26, RF-28, RF-29, RF-36 y RF-38 → *implementado* (P-M-14, 15, 16, 17, 19 y 21 pasan enteras). RF-30 y RF-37 → *implementado, no probado*: P-M-18 queda *Parcial* por la foto grande (S12) y P-M-20 por los pasos que miran 6b (S9). RF-05, RF-10 y RF-12 → *implementado* (P-M-05, 09 y 11 pasan enteras con la carta). RF-06 y RF-11 siguen *no probado* (P-M-06 y 10 esperan a 6b, S9); RF-24 y RF-50 también (Cuenta, S9).
- S9 (7 oct 2026): RF-40, RF-41, RF-43 y RF-44 → *implementado* (P-M-22, 23, 26 y 27 pasan; RF-40 solo blanco y rojo: el verde es nivel 3). RF-42, RF-45 y RF-46 → *implementado, no probado*: P-M-25, 28 y 24 quedan *Parcial* por el Resumen de ingresos (S10). RF-06, RF-11, RF-24, RF-37 y RF-50 → *implementado* (P-M-06, 10, 13, 20 y 29 pasan enteras con Cuenta). Siguen sin probar del todo RF-04 y RF-30 (la foto, S12).
- S10 (8 oct 2026): RF-20 → *implementado* (P-M-12 pasa). RF-42, RF-45 y RF-46 → *implementado* (P-M-25, 28 y 24 pasan enteras con el Resumen de ingresos; su *Sesión* sigue siendo la S9, que los construyó). Del nivel 1 solo quedan RF-04 y RF-30 (*no probados*, la foto) y RF-09 (*diseñado*): los tres esperan la S12.
- S12 (8 oct 2026): RF-09 → *implementado* (P-M-08 pasa, primera vez). RF-04 y RF-30 → *implementado* (P-M-04 y P-M-18 pasan enteras con la foto de Entrecot; su *Sesión* sigue siendo la S6 y la S8). **Los 29 RF del nivel 1 están implementados**; la S13 los vuelve a pasar todos en la pasada final.
- S15 (10 oct 2026): extra fuera del spec (no es un RF de ningún nivel): icono de modo claro/oscuro en 1a; P-M-30 pasa (9 de 9).
