# Ako — inventario de textos de la interfaz (nivel 1)

> Fuentes leídas enteras: `docs/spec-claude-code.md` (apartados 4.1, 6, 7, 9, 10), `docs/spec+doc-pantallas.md` (las siete fichas), `docs/wireframes/spec+doc-wireframes.md` (las 21 leyendas), `docs/spec+doc-pruebas.md` (P-M-01 a P-M-29, columna *Resultado esperado*) y los 21 PNG de `docs/wireframes/`.
> Decisiones ya tomadas que mandan sobre los documentos: (1) el segundo aviso de la cadena 3e dice **recuperar**, no *reactivar*; (2) la categoría de ejemplo precargada es **Bebidas** con el plato **1 · Agua · 1,50 €**; (3) el snackbar de mesa blanca es **«La mesa N no tiene comanda»**.
>
> Convenciones: los textos con variable llevan marcador (`%1$d` entero, `%1$s` texto ya formateado). Los textos que ningún documento fija literalmente van marcados **[no fijado — propuesta]** y se cuentan aparte. Los textos de nivel 2 y 3 están en un apartado propio al final y **no** entran en la tabla del nivel 1. En «Fuente», *leyenda NN #k* remite a la fila k de la leyenda del dibujo NN en `spec+doc-wireframes.md`; *ficha N* remite a `spec+doc-pantallas.md`; *spec §x* a `spec-claude-code.md`.

---

## 1. Pantalla 1 — Selector de rol y PIN (1a, 1b, 1c, 1d, 1e)

| Clave propuesta | Texto literal ES | Tipo | Dónde aparece | Fuente |
|---|---|---|---|---|
| `app_nombre` | AKO (P105, 28 sep) | título | 1a (nombre grande arriba); también de fondo en 1c; `android:label` de la app | 01a-selector.png; leyenda 01a #1; spec §1 y §4.1 |
| `selector_subtitulo` | Pedidos para bares y restaurantes | etiqueta | 1a, debajo del nombre | 01a-selector.png (texto [Claude] del dibujo; no está en ninguna ficha) |
| `selector_btn_propietario` | Propietario | botón | 1a, primer botón grande | 01a-selector.png; ficha 1 · Vistas 1a; P-M-13 |
| `selector_btn_pedir` | Pedir | botón | 1a, segundo botón grande (el de acento) | 01a-selector.png; ficha 1 · Vistas 1a; P-M-13 |
| `selector_btn_cuenta` | Cuenta | botón | 1a, tercer botón grande | 01a-selector.png; ficha 1 · Vistas 1a; P-M-13 |
| `puerta_pedir_titulo` | No se puede pedir | diálogo título | Puerta de Pedir (`ConfirmacionDialog`, solo botón Aceptar) | **[no fijado — propuesta]**; el spec §7 y wireframes §2 solo dicen que usa la caja de `dialogo-cobrar` |
| `puerta_pedir_carta_vacia` | La carta está vacía | diálogo mensaje | Puerta de Pedir cuando no hay ningún plato existente (no provocable en el prototipo, P123) | spec §4.1 y §6; ficha 1 · Validaciones; P-M-14 |
| `puerta_pedir_categorias_eliminadas` | Todas las categorías están eliminadas | diálogo mensaje | Puerta de Pedir cuando hay platos pero ninguno visible | spec §4.1 y §6; ficha 1 · Validaciones; P-M-14 |
| `pin_crear_barra` | Crear PIN | título (barra superior, sin Atrás) | 1b | 01b-crear-pin.png; leyenda 01b #1 |
| `pin_crear_titulo` | Crea el PIN del Propietario | título | 1b, cabecera del formulario | 01b-crear-pin.png |
| `pin_crear_subtitulo` | Protege el Panel y la salida de Pedir. | etiqueta | 1b, bajo el título | 01b-crear-pin.png |
| `pin_hint_pin` | PIN (4 cifras) | etiqueta de campo | 1b, primer campo | 01b-crear-pin.png; leyenda 01b #2 |
| `pin_hint_repite` | Repite el PIN | etiqueta de campo | 1b, segundo campo | 01b-crear-pin.png |
| `pin_crear_aviso` | Si lo olvidas, no se puede recuperar: habrá que reinstalar la app. | mensaje (caja fija visible) | 1b, bajo los campos | 01b-crear-pin.png; leyenda 01b #3 (ver discrepancia D1: ficha y P-M-01 lo acortan) |
| `pin_no_coinciden` | Los PIN no coinciden | mensaje (error; los campos se vacían) | 1b y 1e, al no coincidir las dos entradas | ficha 1 · Validaciones; leyenda 01b #4; P-M-01 |
| `pin_incorrecto` | PIN incorrecto | mensaje (error; el campo se vacía y se sacude) | 1c (entrar en Propietario, salir de Pedir) y 1e (PIN actual) | ficha 1 · Validaciones; leyenda 1c #2; spec §4.1; P-M-02, P-M-03, P-M-16 |
| `pin_introducir_titulo` | Introduce el PIN | diálogo título | 1c | dialogo-1c-introducir-pin.png (ver D8: la ficha llama a la vista «Introducir PIN») |
| `pin_cambiar_titulo` | Cambiar PIN | diálogo título | 1e (mismo texto que el botón del Panel) | dialogo-1e-cambiar-pin.png; leyenda 1e #1 |
| `pin_hint_actual` | PIN actual | hint | 1e, primer campo | dialogo-1e-cambiar-pin.png |
| `pin_hint_nuevo` | PIN nuevo | hint | 1e, segundo campo | dialogo-1e-cambiar-pin.png |
| `pin_hint_repite_nuevo` | Repite el PIN nuevo | hint | 1e, tercer campo | dialogo-1e-cambiar-pin.png |
| `mesa_elegir_titulo` | Elige la mesa | título (barra superior, con ← Atrás) | 1d (rejilla en modo *elegir*) | 01d-elegir-mesa.png; leyenda 01d #1 |

Botones Aceptar y Cancelar de 1b, 1c y 1e: ver `comun_aceptar` y `comun_cancelar` (tabla 8). Número de mesa y total de la celda roja: `%1$d` y `comun_precio` (tabla 8).

---

## 2. Pantalla 2 — Panel del Propietario (2a, 2b, 2e)

| Clave propuesta | Texto literal ES | Tipo | Dónde aparece | Fuente |
|---|---|---|---|---|
| `panel_titulo` | Categorías | título (barra superior) | 2a | 02a-panel.png (el croquis de la ficha 2 decía *CATEGORÍAS*, superado) |
| `panel_nueva_categoria_cd` | Nueva categoría | contentDescription del botón «+» de la barra | 2a, arriba a la derecha | **[no fijado — propuesta]**; leyenda 02a #1 («[+] crea una categoría»); spec §10 exige contentDescription |
| `panel_btn_resumen_ingresos` | Resumen de ingresos | botón | 2a, bajo la barra | 02a-panel.png; leyenda 02a #2; spec §4.1 `[Resumen de ingresos]`; P-M-12 (ver D2: el fondo de dialogo-1e aún dice *Resumen por día*) |
| `panel_editar_categoria_cd` | Editar categoría | contentDescription del lápiz ✎ de cada caja | 2a, cabecera de cada caja | **[no fijado — propuesta]**; leyenda 02a #3 («lápiz = editar»); P-M-05 «Lápiz de Carnes» |
| `panel_categoria_eliminada` | Categoría eliminada | etiqueta (junto al nombre de la caja) | 2a, caja de una categoría con `activo = false` | 02a-panel.png; leyenda 02a #7; spec §4.1; ficha 2 · Validaciones; P-M-04 (ver D3: dialogo-1e dice *Categoría desactivada*) |
| `panel_plato_eliminado` | Eliminado | etiqueta (segunda línea de la fila, atenuada) | 2a, fila de plato con `activo = false` | 02a-panel.png; leyenda 02a #5; spec §4.1 y §10; ficha 2; P-M-04, P-M-10 |
| `panel_btn_mas_plato` | + Plato | botón (fijo abajo de cada caja) | 2a | 02a-panel.png; leyenda 02a #6; spec §4.1 `[+ Plato]`; P-M-04, P-M-07 |
| `panel_btn_cambiar_pin` | Cambiar PIN | botón | 2a, barra inferior izquierda | 02a-panel.png; leyenda 02a #8; spec §4.1; P-M-03 |
| `panel_btn_terminar` | Terminar | botón (acento) | 2a, barra inferior derecha | 02a-panel.png; leyenda 02a #8; spec §4.1; P-M-03 |
| `categoria_titulo_editar` | Editar categoría | título (hoja inferior) | 2b al editar | 02b-editar-categoria.png; leyenda 02b #1 |
| `categoria_titulo_nueva` | Nueva categoría | título (hoja inferior) | 2b al crear | **[no fijado — propuesta]**; leyenda 02b #1 solo dice «Al crear: solo nombre y foto» |
| `categoria_hint_nombre` | Nombre | etiqueta de campo | 2b | 02b-editar-categoria.png |
| `categoria_foto_opcional` | Foto (opcional) | etiqueta de campo | 2b, sobre la miniatura | 02b-editar-categoria.png |
| `categoria_btn_elegir_foto` | + Elegir | botón | 2b, junto a la miniatura | 02b-editar-categoria.png |
| `categoria_activa` | Categoría activa | etiqueta de interruptor | 2b solo al editar (nunca en la categoría por defecto, R16) | 02b-editar-categoria.png; leyenda 02b #3 (ver D4: spec §4.1 lo llama *En la carta*) |
| `categoria_nombre_repetido` | Ya existe una categoría con ese nombre | mensaje (error de campo) | 2b al guardar con nombre repetido | **[no fijado — propuesta]**; P-M-05 paso 4 «aviso de nombre repetido y no se guarda»; spec §6 `ResultadoGuardado.NombreRepetido` |
| `categoria_eliminar_titulo` | ¿Eliminar la categoría? | diálogo título | 2e (aviso agrupado R6 al apagar el interruptor en 2b) | **[no fijado — propuesta]** por analogía con «¿Eliminar el plato?» de dialogo-r6; leyenda r6 #2; P-M-06 |
| `categoria_eliminar_cuerpo` | Estos platos están en comandas pendientes: %1$s. No se quitarán de esas comandas. | diálogo mensaje | 2e; `%1$s` = lista «Flan (mesa 5), …» | **[no fijado — propuesta]**; leyenda r6 #2 «lista de platos y mesas, una sola confirmación»; P-M-06 «lista Flan y mesa 5» |

Botones de 2b y 2e: `comun_guardar`, `comun_cancelar`, `comun_eliminar` (tabla 8). Fila de plato: `comun_plato_numero_nombre` y `comun_precio` (tabla 8). El nombre de la categoría en la cabecera de la caja se pinta en mayúsculas (CARNES) con `textAllCaps`: es dato, no cadena.

---

## 3. Pantalla 2g — Resumen de ingresos (y recibo en solo lectura)

| Clave propuesta | Texto literal ES | Tipo | Dónde aparece | Fuente |
|---|---|---|---|---|
| `resumen_titulo` | Resumen | título (barra superior, con ← Atrás) | 2g | 02g-resumen-ingresos.png (ver D5: spec y leyenda dicen *Resumen de ingresos*) |
| `resumen_dia` | Día | etiqueta de campo (selector de fecha) | 2g, arriba | 02g-resumen-ingresos.png; leyenda 02g #1 |
| *(formato, no cadena)* | Jueves, 17 de septiembre de 2026 | valor del selector de fecha | 2g | 02g-resumen-ingresos.png dibuja «Jueves, 17/09/2026»; desde la S13 (1 → B) es la forma larga del idioma del móvil, `ofLocalizedDate(FormatStyle.FULL)`, con la primera en mayúscula (en inglés, «Thursday, September 17, 2026») |
| `resumen_seccion_comandas` | COMANDAS COBRADAS | título de sección | 2g, sobre la lista | 02g-resumen-ingresos.png |
| `resumen_fila_hora` | Cobrada a las %1$s | etiqueta (segunda línea de cada fila; `%1$s` = hora HH:mm) | 2g, cada comanda PAGADA del día | 02g-resumen-ingresos.png; leyenda 02g #2 |
| `resumen_pie_comandas` | Comandas cobradas | etiqueta (pie, sobre el número) | 2g, pie izquierda | 02g-resumen-ingresos.png; leyenda 02g #3 (ver D6: P-M-12 escribe el pie como «2 comandas · 47,00 €») |
| `resumen_pie_total` | Total del día | etiqueta (pie, sobre el importe) | 2g, pie derecha | 02g-resumen-ingresos.png; leyenda 02g #3 |
| `resumen_vacio` | No se cobró ninguna comanda ese día | mensaje (lista vacía; el total queda a 0,00 €) | 2g, día sin comandas PAGADAS | ficha 2 · 2g; P-M-12 |

La primera línea de cada fila es `comun_mesa` («Mesa 9») y el importe `comun_precio`. Al tocar una fila se abre `ReciboFragment` con `recibo_titulo` («Mesa %1$d · Recibo»), sin *Cobrar* ni calculadora (spec §4.1, leyenda 02g #2).

---

## 4. Pantalla 3 — Alta y edición de plato (3a, 3d, 3e)

| Clave propuesta | Texto literal ES | Tipo | Dónde aparece | Fuente |
|---|---|---|---|---|
| `plato_titulo_nuevo` | Nuevo plato | título (barra superior, con ← Atrás) | 3a al crear | 03a-formulario-1.png y -2.png; croquis ficha 3 |
| `plato_titulo_editar` | Editar plato | título (barra superior) | 3a al editar | **[no fijado — propuesta]**; ningún dibujo ni ficha da el título de la edición |
| `plato_elegir_foto_cd` | Elegir foto | contentDescription del botón redondo «+» sobre la foto | 3a, esquina de la foto | **[no fijado — propuesta]**; leyenda 03a-1 #1; P-M-08 «[+] de la foto» |
| `foto_error` | No se ha podido usar esa foto | Snackbar cuando la foto elegida no se puede leer al guardar (archivo roto, no es una imagen) | 3a y hoja 2b | **[Claude, S12, 8 oct]** P96 A |
| `plato_hint_nombre` | Nombre * | etiqueta de campo | 3a | 03a-formulario-1.png; leyenda 03a-1 #2 (obligatorios con *) |
| `plato_hint_numero` | Número * | etiqueta de campo | 3a | 03a-formulario-1.png; leyenda 03a-1 #3 |
| `plato_hint_precio` | Precio (€) * | etiqueta de campo | 3a | 03a-formulario-1.png |
| `plato_hint_categoria` | Categoría * | etiqueta de campo (desplegable) | 3a | 03a-formulario-1.png; leyenda 03a-1 #4 |
| `plato_hint_descripcion` | Descripción | etiqueta de campo | 3a | 03a-formulario-1.png; ficha 3 · Campos |
| `plato_seccion_alergenos` | ALÉRGENOS | título de sección | 3a, sobre las 14 casillas | 03a-formulario-1.png y -2.png; leyenda 03a-2 #1; P-M-09 («en ALÉRGENOS marcar…») |
| `plato_en_la_carta` | En la carta | etiqueta de interruptor | 3a, bajo los alérgenos | 03a-formulario-2.png; leyenda 03a-2 #2; spec §4.1; ficha 3 · Campos; P-M-10 |
| `plato_numero_repetido` | Ese número ya lo tiene otro plato | mensaje (error de campo; Guardar no guarda) | 3a al guardar con número repetido (R9) | **[no fijado — propuesta]**; spec §4.1 «aviso de número repetido (R9)»; ficha 3 · Validaciones; P-M-07 paso 4 |
| `plato_numero_cambiado` | Si alguien tiene apuntado el %1$d, dejará de corresponder | mensaje (aviso al cambiar el número de un plato ya creado) | 3a | ficha 3 · Validaciones («…el 12…»); P-M-07 paso 6; spec §4.1 («…el N…») |
| `plato_salir_titulo` | ¿Salir sin guardar? | diálogo título | 3a, Atrás con cambios sin guardar | **[no fijado — propuesta]** |
| `plato_salir_cuerpo` | Se perderán los cambios | diálogo mensaje | 3a, Atrás con cambios sin guardar | spec §4.1 y §7; ficha 3 · Validaciones; leyenda 03a-2 #3; P-M-07 paso 7 |
| `plato_salir_btn_salir` | Salir | diálogo botón (afirmativo) | Mismo diálogo | **[no fijado — propuesta]**; P-M-07 solo dice «al aceptar» |
| `mesas_afectadas_titulo` | ¿Eliminar el plato? | diálogo título | 3d (R6, al apagar *En la carta* y guardar) | dialogo-r6-mesas-afectadas.png; leyenda r6 #1 |
| `mesas_afectadas_cuerpo` | %1$s está en comandas pendientes de las mesas %2$s. No se quitará de esas comandas. | diálogo mensaje (`%1$s` = nombre del plato, `%2$s` = «4 y 7») | 3d | dialogo-r6-mesas-afectadas.png («Costillas a la barbacoa está en comandas pendientes de las mesas 4 y 7. No se quitará de esas comandas.»). Nota: con una sola mesa (P-M-10, «mesa 6») haría falta la variante «de la mesa %2$s»: no fijada |
| `categoria_eliminada_titulo` | Categoría eliminada | diálogo título | 3e, aviso 1 de 2 | dialogo-3e-1-mover.png; leyenda 3e-1 #1 |
| `categoria_eliminada_cuerpo` | La categoría %1$s está eliminada. ¿Muevo el plato a %2$s? | diálogo mensaje (`%1$s` = categoría eliminada, `%2$s` = nombre de la categoría por defecto, renombrable por R16) | 3e, aviso 1 de 2 | dialogo-3e-1-mover.png; ficha 3 · 3e («La categoría Carnes está eliminada. ¿Muevo el plato a Otros?»); spec §6; P-M-11 («…Postres…») |
| `categoria_eliminada_btn_mover` | Sí, mover | diálogo botón (afirmativo) | 3e, aviso 1 | dialogo-3e-1-mover.png (ver D9: ficha y P-M-11 dicen *Aceptar / Rechazar*) |
| `recuperar_categoria_titulo` | ¿Quieres recuperar %1$s? | diálogo título | 3e, aviso 2 de 2 | **Decisión tomada** (recuperar); ficha 3 · 3e y P-M-11 dicen *recuperar*; dialogo-3e-2-reactivar.png y spec §6 dicen *reactivar* (D10) |
| `recuperar_categoria_cuerpo` | Volverán a la carta sus %1$d platos. | diálogo mensaje | 3e, aviso 2 de 2 | dialogo-3e-2-reactivar.png; ficha 3 · 3e (con punto final); P-M-11 y spec §6 (sin punto) |
| `recuperar_categoria_btn_recuperar` | Recuperar | diálogo botón (afirmativo) | 3e, aviso 2 | **Decisión tomada**; el dibujo dice *Reactivar*; P-M-11 dice *Sí* (D10) |

Botón negativo de los dos avisos 3e: `comun_no` («No», dialogo-3e-1 y -2). Botones de 3d: `comun_cancelar` y `comun_eliminar`. Botón inferior fijo: `comun_guardar`. Los 14 nombres de alérgenos de la lista: tabla 9. El «?» de la foto: `comun_sin_foto_cd`.

---

## 5. Pantalla 5 — Pedir (5a Carta, 5b Ficha del plato, 5c Carrito)

| Clave propuesta | Texto literal ES | Tipo | Dónde aparece | Fuente |
|---|---|---|---|---|
| `carta_btn_salir` | Salir | botón (barra superior, izquierda; sin flecha) | 5a | 05a-carta.png; leyenda 05a #1; spec §4.1 y §7; ficha 5 · Disposición; P-M-16, P-M-21 |
| `carta_carrito_pastilla` | Carrito · %1$d · %2$s | botón (pastilla flotante abajo a la derecha; `%1$d` = número de platos, `%2$s` = total formateado) | 5a | 05a-carta.png («Carrito · 3 · 23,40 €»); leyenda 05a #8; croquis ficha 5 (ver D7: P-M-19 escribe «2 · 37,00 €») |
| `ficha_alergenos` | Alérgenos | etiqueta de desplegable | 5b | 05b-ficha-plato.png; leyenda 05b #4; P-M-09 y P-M-18 («desplegar *Alérgenos*») |
| `ficha_alergeno_item` | • %1$s | etiqueta (una línea por alérgeno marcado) | 5b, dentro del desplegable | 05b-ficha-plato.png («• Pescado»); P-M-18 («Gluten, Lácteos») |
| `ficha_sin_alergenos` | El restaurante no ha indicado alérgenos para este plato. Pregunta al personal. | mensaje (dentro del desplegable) | 5b, plato sin alérgenos marcados | spec §4.1; ficha 5 · Validaciones; leyenda 05b #4; P-M-09; P-M-18 |
| `ficha_cantidad` | Cantidad | etiqueta | 5b, junto a − 1 + | 05b-ficha-plato.png; leyenda 05b #5 |
| `ficha_btn_anadir` | Añadir — %1$s | botón (acento, fijo abajo; `%1$s` = importe formateado; guion largo U+2014 con espacios) | 5b | 05b-ficha-plato.png («Añadir — 14,00 €»); ficha 5 · Vistas («Añadir — 12,50 €»); spec §4.1 («Añadir — precio»); P-M-18 («Añadir — 18,50 €») |
| `carrito_titulo` | Carrito · Mesa %1$d | título (barra superior, con ← Atrás) | 5c | 05c-carrito.png («Carrito · Mesa 4»); leyenda 05c #1 |
| `carrito_btn_enviar` | Enviar | botón (acento; apagado con carrito vacío) | 5c | 05c-carrito.png; leyenda 05c #5; spec §4.1; P-M-19, P-M-20 |
| `enviar_titulo` | ¿Enviar el pedido? | diálogo título | Confirmación de Enviar (`ConfirmacionDialog`) | **[no fijado — propuesta]**; spec §7 y wireframes §2 solo dicen que usa la caja de dialogo-cobrar |
| `enviar_cuerpo` | Mesa %1$d · %2$s | diálogo mensaje (`%2$s` = total formateado) | Confirmación de Enviar | spec §4.1 («confirmación *"Mesa N · total"*»); leyenda 05c #5; ficha 5 · Validaciones; P-M-20 («"Mesa 4" y 42,00 €») |
| `enviar_btn_enviar` | Enviar | diálogo botón (afirmativo) | Confirmación de Enviar | **[no fijado — propuesta]** (mismo texto que el botón de 5c; P-M-20 solo dice «aceptar») |
| `salir_sin_enviar_titulo` | ¿Salir sin enviar? | diálogo título | Aviso RF-38 al pulsar Salir o Atrás con carrito no vacío, antes del PIN | **[no fijado — propuesta]** |
| `salir_sin_enviar_cuerpo` | Se perderán %1$d platos sin enviar | diálogo mensaje (necesita `<plurals>`: «Se perderá 1 plato sin enviar» / «Se perderán %1$d platos sin enviar») | Aviso RF-38 | ficha 1 · Flujo («Aviso: se perderán N platos»); spec §4.1 y §7; P-M-21 («aviso de que se perderán los platos sin enviar (1 plato)») — la frase exacta no está fijada |
| `salir_sin_enviar_btn_salir` | Salir | diálogo botón (afirmativo) | Aviso RF-38 | **[no fijado — propuesta]**; P-M-21 dice «aceptar el aviso» |

Compartidos: «Mesa %1$d» de la barra de 5a → `comun_mesa` (05a #2, spec §4.1, P-M-15); título de 5b «21 · Merluza a la plancha» → `comun_plato_numero_nombre`; precio → `comun_precio`; líneas del carrito «2 × Entrecot» → `comun_linea`; «Quitar» → `comun_quitar`; «TOTAL» → `comun_total`; − y + → `comun_menos` / `comun_mas`; el «?» de foto → `comun_sin_foto_cd`; el diálogo de PIN al salir es 1c (`pin_introducir_titulo`). Cabeceras de sección de la lista («CARNES», «PESCADOS») y nombres de la fila de categorías son datos. La descripción del plato es dato (la del dibujo, «Merluza fresca a la plancha con patatas panadera y aceite de oliva.», es texto de ejemplo). Los nombres largos se cortan con «…» (`ellipsize="end"`, no es cadena).

---

## 6. Pantalla 6 — Cuenta (6a Rejilla, 6b Comanda, 6c Recibo)

| Clave propuesta | Texto literal ES | Tipo | Dónde aparece | Fuente |
|---|---|---|---|---|
| `cuenta_titulo` | Cuenta | título (barra superior, con ← Atrás) | 6a (rejilla en modo *gestionar*) | 06a-rejilla-mesas.png; leyenda 06a #1 |
| `cuenta_mesa_sin_comanda` | La mesa %1$d no tiene comanda | snackbar | 6a al tocar una mesa blanca | **Decisión tomada**; 06a-rejilla-mesas.png («La mesa 5 no tiene comanda»); leyenda 06a #4; spec §4.1; ficha 6 · Validaciones («La mesa N…»); P-M-22 («La mesa 7…») |
| `comanda_btn_anular` | Anular | botón (barra superior, derecha) | 6b | 06b-comanda.png; leyenda 06b #4; P-M-23, P-M-25 |
| `comanda_btn_dar_cuenta` | Dar la cuenta | botón (acento, fijo abajo) | 6b | 06b-comanda.png; leyenda 06b #5; spec §4.1; P-M-23, P-M-26 |
| `anular_titulo` | ¿Anular la comanda de la mesa %1$d? | diálogo título | Confirmación de Anular (`ConfirmacionDialog`) | leyenda dialogo-cobrar #1 («¿Anular la comanda de la mesa 4? No se puede deshacer»); P-M-25 («…mesa 5? No se puede deshacer»). El reparto título/cuerpo no está dibujado (D11) |
| `anular_cuerpo` | No se puede deshacer | diálogo mensaje | Confirmación de Anular | leyenda dialogo-cobrar #1; P-M-25 (sin punto final en las dos fuentes) |
| `ultima_linea_titulo` | ¿Quitar la última línea? | diálogo título | Aviso R7 al quitar la última línea de 6b | dialogo-r7-ultima-linea.png; leyenda r7 #1 |
| `ultima_linea_cuerpo` | La comanda se quedará vacía y se anulará. La mesa %1$d quedará libre. | diálogo mensaje | Aviso R7 | dialogo-r7-ultima-linea.png («…La mesa 4 quedará libre.») |
| `ultima_linea_btn_quitar_anular` | Quitar y anular | diálogo botón (afirmativo) | Aviso R7 | dialogo-r7-ultima-linea.png |
| `recibo_titulo` | Mesa %1$d · Recibo | título (barra superior, con ← Atrás) | 6c y recibo en solo lectura desde 2g | 06c-recibo.png («Mesa 4 · Recibo»); dialogo-cobrar.png (fondo); croquis ficha 6; P-M-26 («6c "Mesa 4 · Recibo"») |
| `recibo_entregado` | Entregado | etiqueta de campo | 6c, calculadora de cambio | 06c-recibo.png; leyenda 06c #3; spec §4.1 («campo *Entregado* opcional»); ficha 6; P-M-26, P-M-27 |
| `recibo_opcional` | (opcional) | etiqueta (bajo *Entregado*) | 6c | 06c-recibo.png; croquis ficha 6; P-M-26 («marcado opcional») |
| `recibo_euro_sufijo` | € | etiqueta (sufijo a la derecha del campo Entregado) | 6c | 06c-recibo.png |
| `recibo_cambio` | Cambio | etiqueta | 6c (valor = entregado − total, en negativo si falta) | 06c-recibo.png; leyenda 06c #4; P-M-27 |
| `recibo_btn_cobrar` | Cobrar | botón (acento, fijo abajo; no aparece en solo lectura) | 6c | 06c-recibo.png; leyenda 06c #5; spec §4.1; P-M-26, P-M-28 |
| `cobrar_titulo` | ¿Cobrar la mesa %1$d? | diálogo título | Confirmación de Cobrar | dialogo-cobrar.png («¿Cobrar la mesa 4?»); leyenda dialogo-cobrar #1 |
| `cobrar_cuerpo` | Total: %1$s.\nLa comanda quedará PAGADA y no se podrá modificar. | diálogo mensaje (`%1$s` = total formateado; salto de línea tras el punto) | Confirmación de Cobrar | dialogo-cobrar.png («Total: 39,50 €. / La comanda quedará PAGADA y no se podrá modificar.») |
| ~~`cobrar_btn_cobrar`~~ → `recibo_btn_cobrar` | Cobrar | diálogo botón (afirmativo) | Confirmación de Cobrar | dialogo-cobrar.png (ver D12: P-M-28 usa *No / Sí*). [Claude, S9, revisión B3] No existe clave aparte: el botón del aviso usa la misma cadena que el botón de 6c (P40), como dice el borrador |

Compartidos: «Mesa 4» de 6b → `comun_mesa` (06b, P-M-23 «6b con "Mesa 4"»); líneas «2 × Entrecot» → `comun_linea`; «Quitar» de cada línea → `comun_quitar` (06b #1; P-M-23 lo escribe en minúscula, D13); «TOTAL» → `comun_total`; importes → `comun_precio`; «Cancelar» de los diálogos → `comun_cancelar`; botón negativo de Anular → `comun_cancelar` y afirmativo → `comanda_btn_anular` (P40 → A: el botón dice lo que hace; el «No / Sí» de P-M-25 se lee como negativo / afirmativo, D11; corregido el 7 oct, C06: `comun_si` no existe). Número de mesa en la celda: `%1$d`.

---

## 7. Diálogos comunes (`ConfirmacionDialog`) — resumen de las siete cajas

Un solo componente (título, texto, botón afirmativo, botón negativo). Esta tabla no añade claves: agrupa las ya listadas para ver de un vistazo qué texto lleva cada caso.

| Caso | Título | Texto | Botón negativo | Botón afirmativo | Fuente del dibujo |
|---|---|---|---|---|---|
| Cobrar | `cobrar_titulo` | `cobrar_cuerpo` | `comun_cancelar` | `recibo_btn_cobrar` | dialogo-cobrar.png |
| Anular | `anular_titulo` | `anular_cuerpo` | `comun_cancelar` | `comanda_btn_anular` | no dibujado (misma caja); leyenda dialogo-cobrar #1; P-M-25 |
| Última línea (R7) | `ultima_linea_titulo` | `ultima_linea_cuerpo` | `comun_cancelar` | `ultima_linea_btn_quitar_anular` | dialogo-r7-ultima-linea.png |
| Mesas afectadas plato (3d, R6) | `mesas_afectadas_titulo` | `mesas_afectadas_cuerpo` | `comun_cancelar` | `comun_eliminar` | dialogo-r6-mesas-afectadas.png |
| Mesas afectadas categoría (2e, R6) | `categoria_eliminar_titulo` [propuesta] | `categoria_eliminar_cuerpo` [propuesta] | `comun_cancelar` | `comun_eliminar` | no dibujado; leyenda r6 #2 |
| 3e aviso 1 | `categoria_eliminada_titulo` | `categoria_eliminada_cuerpo` | `comun_no` | `categoria_eliminada_btn_mover` | dialogo-3e-1-mover.png |
| 3e aviso 2 | `recuperar_categoria_titulo` | `recuperar_categoria_cuerpo` | `comun_no` | `recuperar_categoria_btn_recuperar` | dialogo-3e-2-reactivar.png (texto corregido por decisión) |
| Enviar | `enviar_titulo` [propuesta] | `enviar_cuerpo` | `comun_cancelar` | `enviar_btn_enviar` [propuesta] | no dibujado; spec §4.1 |
| Salir con platos sin enviar (RF-38) | `salir_sin_enviar_titulo` [propuesta] | `salir_sin_enviar_cuerpo` | `comun_cancelar` | `salir_sin_enviar_btn_salir` [propuesta] | no dibujado; ficha 1 · Flujo; P-M-21 |
| Salir del plato con cambios | `plato_salir_titulo` [propuesta] | `plato_salir_cuerpo` | `comun_cancelar` | `plato_salir_btn_salir` [propuesta] | no dibujado; ficha 3; P-M-07 |
| Puerta de Pedir (dos mensajes) | `puerta_pedir_titulo` [propuesta] | `puerta_pedir_carta_vacia` / `puerta_pedir_categorias_eliminadas` | — | `comun_aceptar` | no dibujado; wireframes §2 |
| PIN (1c) y Cambiar PIN (1e) | `pin_introducir_titulo` / `pin_cambiar_titulo` | campos | `comun_cancelar` | `comun_aceptar` | dialogo-1c-introducir-pin.png; dialogo-1e-cambiar-pin.png |

---

## 8. Textos comunes (barras, botones y formatos compartidos)

| Clave propuesta | Texto literal ES | Tipo | Dónde aparece | Fuente |
|---|---|---|---|---|
| `comun_atras` | ← Atrás | botón (flecha + palabra, arriba a la izquierda) | 1d, 2a, 2b (fondo), 2g, 3a, 5b, 5c, 6a, 6b, 6c | Todos los PNG salvo 1a, 1b, 5a; spec §4.1 («flecha ← con la palabra *Atrás*») y §7. Si la flecha va como icono, la cadena queda en «Atrás» y sirve también de contentDescription |
| `comun_aceptar` | Aceptar | botón / diálogo botón | 1b (botón grande), 1c, 1e, puerta de Pedir | 01b-crear-pin.png; dialogo-1c; dialogo-1e; P-M-01, P-M-02, P-M-03 |
| `comun_cancelar` | Cancelar | diálogo botón (negativo) | 1c, 1e, 3d, R7, Cobrar, Enviar, RF-38, salir del plato | dialogo-1c; dialogo-1e; dialogo-r6; dialogo-r7; dialogo-cobrar; P-M-13, P-M-16 («cancelar») |
| `comun_guardar` | Guardar | botón (acento, fijo abajo) | 2b, 3a | 02b-editar-categoria.png; 03a-formulario-1/-2.png; leyenda 03a-1 #5; P-M-05, P-M-07 |
| `comun_si` | Sí | diálogo botón (afirmativo) | Anular (P-M-25) | P-M-25, P-M-28 («**No** … **Sí**») |
| `comun_no` | No | diálogo botón (negativo) | 3e-1, 3e-2, Anular | dialogo-3e-1-mover.png; dialogo-3e-2-reactivar.png; P-M-11, P-M-25, P-M-28 |
| `comun_quitar` | Quitar | botón (en cada línea) | 5c, 6b | 05c-carrito.png; 06b-comanda.png; leyenda 06b #1; P-M-19, P-M-23, P-M-24 |
| `comun_eliminar` | Eliminar | diálogo botón (afirmativo) | 3d y 2e | dialogo-r6-mesas-afectadas.png |
| `comun_mesa` | Mesa %1$d | título / etiqueta | 5a (barra), 6b (barra), 2g (fila), confirmación de Enviar | 05a-carta.png; 06b-comanda.png; 02g-resumen-ingresos.png; spec §4.1 («"Mesa N"»); P-M-15, P-M-20, P-M-23 |
| `comun_precio` | %1$s € | etiqueta (importe: coma decimal, dos cifras, espacio fino y €) | Todas las listas, celdas rojas, totales, cambio | Todos los dibujos («18,50 €», «39,50 €», «−2,00 €» en P-M-27); spec §5.1 (la interfaz convierte 1850 ↔ 18,50 €) |
| `comun_total` | TOTAL | etiqueta | 5c, 6b, 6c | 05c-carrito.png; 06b-comanda.png; 06c-recibo.png; spec §4.1; P-M-23, P-M-26 |
| `comun_linea` | %1$d × %2$s | etiqueta (cantidad × nombre congelado; signo × U+00D7) | 5c, 6b, 6c, recibo solo lectura | 05c, 06b, 06c («2 × Entrecot»); spec §4.1 («cantidad × nombre»); P-M-20, P-M-23 |
| `comun_plato_numero_nombre` | %1$d · %2$s | etiqueta (número · nombre; punto medio U+00B7) | 2a, 5a (filas), 5b (título) | 02a-panel.png; 05a-carta.png; 05b-ficha-plato.png («12 · Entrecot», «21 · Merluza a la plancha») |
| `comun_sin_foto_cd` | Sin foto | contentDescription del «?» | Miniaturas de 2a y 5a, foto grande de 3a y 5b, círculo de categoría en 5a y 2b | spec §9 («contentDescription = "Sin foto"»); ficha 5 · Disposición y Validaciones; P-M-08 |
| `comun_menos` | − | botón (signo menos U+2212) | 5b, 5c | 05b-ficha-plato.png; 05c-carrito.png; P-M-19 |
| `comun_mas` | + | botón | 5b, 5c; también el «+» de 2a (categoría) y el de la foto en 3a | 05b, 05c, 02a, 03a-1 |
| `comun_menos_cd` | Quitar uno | contentDescription de − | 5b, 5c | **[no fijado — propuesta]**; spec §10 exige contentDescription en todo elemento interactivo |
| `comun_mas_cd` | Añadir uno | contentDescription de + | 5b, 5c | **[no fijado — propuesta]** |

---

## 9. Precarga y nombres fijos (van a la base de datos en `Precarga.onCreate`; conviene que salgan de `strings.xml` para no escribirlos en el código)

| Clave propuesta | Texto literal ES | Tipo | Dónde aparece | Fuente |
|---|---|---|---|---|
| `alergeno_gluten` | Gluten | etiqueta (casilla / viñeta) | 3a lista de alérgenos; 5b desplegable | 03a-formulario-2.png (orden del dibujo, columna izquierda-derecha por filas); P-M-09 |
| `alergeno_crustaceos` | Crustáceos | etiqueta | ídem | 03a-formulario-2.png |
| `alergeno_huevos` | Huevos | etiqueta | ídem | 03a-formulario-2.png |
| `alergeno_pescado` | Pescado | etiqueta | ídem; 05b-ficha-plato.png («• Pescado») | 03a-formulario-2.png |
| `alergeno_cacahuetes` | Cacahuetes | etiqueta | ídem | 03a-formulario-2.png |
| `alergeno_soja` | Soja | etiqueta | ídem | 03a-formulario-2.png |
| `alergeno_lacteos` | Lácteos | etiqueta | ídem | 03a-formulario-2.png; P-M-09 |
| `alergeno_frutos_cascara` | Frutos de cáscara | etiqueta | ídem | 03a-formulario-2.png |
| `alergeno_apio` | Apio | etiqueta | ídem | 03a-formulario-2.png |
| `alergeno_mostaza` | Mostaza | etiqueta | ídem | 03a-formulario-2.png |
| `alergeno_sesamo` | Sésamo | etiqueta | ídem | 03a-formulario-2.png |
| `alergeno_sulfitos` | Sulfitos | etiqueta | ídem | 03a-formulario-2.png |
| `alergeno_altramuces` | Altramuces | etiqueta | ídem | 03a-formulario-2.png |
| `alergeno_moluscos` | Moluscos | etiqueta | ídem | 03a-formulario-2.png |
| ~~`etiqueta_vegano`~~ | ~~Vegano~~ | **EXCLUIDA del nivel 1 (P115):** la tabla `etiqueta` y su precarga llegan con el incremento 1 | — | spec §4.2 inc. 1; P115 |
| ~~`etiqueta_vegetariano`~~ | ~~Vegetariano~~ | ídem (P115) | — | ídem |
| ~~`etiqueta_pescetariano`~~ | ~~Pescetariano~~ | ídem (P115) | — | ídem |
| `precarga_categoria_por_defecto` | Otros | dato precargado (categoría con `esPorDefecto = true`; renombrable, se reconoce por la columna) | 2a (última caja), 5a (última de la fila), 3e-1 (`%2$s`) | spec §4.1, §5.1, §6; ficha 2; P-M-04, P-M-11, P-M-17, P-M-29 |
| `precarga_categoria_ejemplo` | Bebidas | dato precargado (categoría de ejemplo) | 2a, 5a | **Decisión tomada**; 05a-carta.png y 02a-panel.png muestran «Bebidas» (en 02a como eliminada, P223) |
| `precarga_plato_ejemplo` | Agua | dato precargado (plato de ejemplo, número 1, 150 céntimos; se ve como «1 · Agua · 1,50 €») | 2a, 5a | **Decisión tomada**; spec §5.1 («1 categoría y 1 plato de ejemplo»); P-M-29 |

Orden legal de los 14 alérgenos (anexo II del Reglamento (UE) 1169/2011), por si la precarga quiere seguirlo en vez del orden del dibujo: Gluten · Crustáceos · Huevos · Pescado · Cacahuetes · Soja · Lácteos · Frutos de cáscara · Apio · Mostaza · Sésamo · Sulfitos · Altramuces · Moluscos. Coincide con el dibujo leído por filas (izquierda, derecha).

---

## 10. Textos de nivel 2 y 3 — EXCLUIDOS del nivel 1 (para que nadie los meta en `strings.xml` de la S13)

| Texto literal | Nivel / incremento | Fuente |
|---|---|---|
| Se la doy al cliente | 2 · inc. 2 (interruptor en 1d) | ficha 1; leyenda 01d #4 |
| Sin fijar la pantalla, el cliente podría salir de la app | 2 · inc. 2 | ficha 1 · Validaciones |
| Etiquetar · Eliminar y recuperar · Vista previa · Etiquetas · Idiomas | 2 · inc. 1, 8, 6, 1, 10 (botones del Panel) | ficha 2 · croquis; leyenda 02a #2 y #8 |
| Ningún plato cumple los filtros (+ botón para quitarlos) | 2 · inc. 1 | ficha 5; spec §4.2 |
| Agrandar / Modo Agrandar | 2 · inc. 3 | ficha 5 croquis |
| Valores por ración · Tabla nutricional · energía, grasas, de las cuales saturadas, hidratos de carbono, de los cuales azúcares, proteínas, sal | 2 · inc. 5 | ficha 3 y 5; spec §4.2 |
| Modificadores · sin cebolla · extra queso · AÑADIR / QUITAR | 2 · inc. 4 | ficha 3 croquis |
| Gestionar etiquetas · ETIQUETAS · Eliminada · «N platos» · Sin gluten | 2 · inc. 1 (pantalla 7) | ficha 3 croquis; ficha 7 |
| + Añadir platos · − / + en 6b · Imprimir · No hay impresora configurada | 2 · inc. 9 | ficha 6; leyenda 06b #2, 06c #5 |
| Liberar mesa · Empezar comanda nueva (mesa verde) | 3 | ficha 6 |
| Lo pedido por la mesa (5e) | 3 | ficha 5 |
| IDIOMAS · (base) · «N de M» · SIN TRADUCIR · TRADUCIDOS · Guardado (snackbar) · Primero crea algún plato · Este idioma tiene 14 traducciones. ¿Seguro? · ES rechazado | 2 · inc. 10 (pantalla 8) | ficha 8; spec §4.2 |
| Disponible / Desactivar / Activar / Desactivado (agotado temporal) | 2 · inc. 12 | spec §4.2; leyenda 03a-2 #2 |
| Línea de resumen «14 platos · 3 eliminados · 5 categorías» | 3 | ficha 2 croquis |
| Flechas ▲▼ de las categorías | 2 · inc. 8 | leyenda 02a #3 |
| Resumen de ingresos por periodos (años → meses → calendario) | 2 · inc. 7 | spec §4.2 |

---

## (a) Discrepancias entre fuentes para el mismo texto (sin resolver)

| # | Texto | Versión A (fuente) | Versión B (fuente) | Versión C (fuente) |
|---|---|---|---|---|
| D1 | Aviso de 1b | «Si lo olvidas, no se puede recuperar: habrá que reinstalar la app.» (01b-crear-pin.png) | «Si lo olvidas, no se puede recuperar» (ficha 1 · Vistas 1b; P-M-01) | «si lo olvidas, no se puede recuperar» en minúscula (spec §4.1) |
| D2 | Botón del Panel al Resumen | «Resumen de ingresos» (02a-panel.png; 02b; spec §4.1; leyenda 02a #2; P-M-12) | «Resumen por día» (fondo de dialogo-1e-cambiar-pin.png, dibujo no redibujado tras P149) | — |
| D3 | Marca de categoría eliminada en 2a | «Categoría eliminada» (02a-panel.png; spec §4.1; ficha 2; P-M-04) | «Categoría desactivada» (fondo de dialogo-1e-cambiar-pin.png, anterior a P153) | — |
| D4 | Interruptor de 2b | «Categoría activa» (02b-editar-categoria.png) | «*En la carta*: apagarlo elimina la categoría» (spec §4.1, pantalla 2) | «interruptor de activo» (ficha 2 · Vistas 2b; leyenda 02b #1; P-M-05, descripción, no literal) |
| D5 | Título de la pantalla 2g | «Resumen» (02g-resumen-ingresos.png) | «Resumen de ingresos» (spec §4.1; leyenda 02g cabecera; ficha 2 · 2g) | — |
| D6 | Pie de 2g | Dos etiquetas «Comandas cobradas» / «6» y «Total del día» / «224,30 €» (02g-resumen-ingresos.png) | Una línea «2 comandas · 47,00 €» (P-M-12) | «cuántas comandas y el total» (spec §4.1; ficha 2 · 2g, descripción) |
| D7 | Pastilla del carrito | «Carrito · 3 · 23,40 €» (05a-carta.png; croquis ficha 5) | «2 · 37,00 €», «3 · 55,50 €», «4 · 60,50 €» sin la palabra *Carrito* (P-M-19) | «número de platos y total» (spec §4.1; leyenda 05a #8, descripción) |
| D8 | Título del diálogo 1c | «Introduce el PIN» (dialogo-1c-introducir-pin.png) | «1c Introducir PIN» (ficha 1 · Vistas; leyenda; spec §4.1: nombre de la vista, no necesariamente el título) | — |
| D9 | Botones del aviso 3e-1 | «No» / «Sí, mover» (dialogo-3e-1-mover.png) | «Aceptar» / «Rechazar» (ficha 3 · 3e) | «Rechazar» / «Aceptar» (P-M-11, pasos 1-3) |
| D10 | Aviso 3e-2 (título y botón) | «¿Quieres reactivar Carnes?» / «Reactivar» (dialogo-3e-2-reactivar.png; leyenda 3e-2; spec §6 «¿Quieres reactivar <categoría>?…») | «¿Quieres recuperar Carnes? Volverán a la carta sus 8 platos.» / botones «Sí» / «No» (ficha 3 · 3e) | «¿Quieres recuperar Postres? Volverán a la carta sus N platos» sin punto / «Sí» / «No» (P-M-11). **Decisión tomada: recuperar / Recuperar** |
| D11 | Confirmación de Anular | «¿Anular la comanda de la mesa 4? No se puede deshacer» en una sola frase, misma caja que Cobrar (leyenda dialogo-cobrar #1) | «¿Anular la comanda de la mesa 5? No se puede deshacer», botones «No» / «Sí» (P-M-25) | El dibujo de dialogo-cobrar usa «Cancelar» / «Cobrar»: no está fijado si Anular usa «Cancelar / Anular» o «No / Sí» |
| D12 | Botones de la confirmación de Cobrar | «Cancelar» / «Cobrar» (dialogo-cobrar.png) | «No» / «Sí» (P-M-28, pasos 1-2) | — |
| D13 | Botón de quitar línea en 6b | «Quitar» en negrita (06b-comanda.png; 05c-carrito.png) | «[quitar]» en minúscula (croquis ficha 6; P-M-23 «quitar en cada línea») | — |
| D14 | Cuerpo del aviso 3e-2 | «Volverán a la carta sus 8 platos.» con punto (dialogo-3e-2; ficha 3) | «Volverán a la carta sus N platos» sin punto (P-M-11; spec §6) | — |
| D15 | Cuerpo del aviso R6 (3d) con una sola mesa | «…está en comandas pendientes de las mesas 4 y 7. No se quitará de esas comandas.» (dialogo-r6, plural) | «Aviso 3d (R6) con la mesa 6» (P-M-10): hace falta la forma singular y no está escrita | — |
| D16 | Aviso RF-38 | «Aviso: se perderán N platos» (ficha 1 · Flujo) | «aviso de que se perderán los platos sin enviar (1 plato)» (P-M-21) | «aviso previo si hay platos en el carrito sin enviar» (spec §4.1). Ninguna fuente da la frase literal |
| D17 | Snackbar de mesa blanca (solo el número de ejemplo) | «La mesa 5 no tiene comanda» (06a-rejilla-mesas.png) | «La mesa 7 no tiene comanda» (P-M-22) | «La mesa N no tiene comanda» (spec §4.1; ficha 6). **Decisión tomada: «La mesa N no tiene comanda» con marcador** |
| D18 | Campos del diálogo 1e | Tres campos a la vez: «PIN actual», «PIN nuevo», «Repite el PIN nuevo» (dialogo-1e-cambiar-pin.png) | «Primero el actual; si es incorrecto, no se llega a pedir el nuevo» (leyenda 1e #1; ficha 1 · Vistas 1e; P-M-03): sugiere pedir el nuevo en un segundo paso | — |
| D19 | Sección de alérgenos | «ALÉRGENOS» en mayúsculas (03a-formulario-1/-2.png; P-M-09) | «Alérgenos» en minúsculas como título del desplegable de 5b (05b-ficha-plato.png; P-M-18) | Son dos sitios distintos; se anota por si se quiere una sola cadena con `textAllCaps` |
| D20 | «Otros» en el aviso 3e-1 | «¿Muevo el plato a Otros?» literal (ficha 3; dialogo-3e-1; P-M-11; spec §6) | R16: la categoría por defecto «se puede renombrar» y «se reconoce por la columna, nunca por el nombre» (spec §5.1, §6; ficha 2) → el texto debe llevar marcador `%2$s`, no «Otros» fijo | — |

---

## (b) Recuento de cadenas del nivel 1

| Tabla | Fijadas por alguna fuente | Propuestas (no fijadas) | Total |
|---|---|---|---|
| 1 Selector / PIN | 21 | 1 (`puerta_pedir_titulo`) | 22 |
| 2 Panel + 2b + 2e | 12 | 6 (`panel_nueva_categoria_cd`, `panel_editar_categoria_cd`, `categoria_titulo_nueva`, `categoria_nombre_repetido`, `categoria_eliminar_titulo`, `categoria_eliminar_cuerpo`) | 18 |
| 3 Resumen de ingresos (2g) | 7 | 0 | 7 (más un formato de fecha que no es cadena) |
| 4 Plato + 3d + 3e | 18 (incluidas las 2 corregidas por decisión: `recuperar_categoria_titulo`, `recuperar_categoria_btn_recuperar`) | 5 (`plato_titulo_editar`, `plato_elegir_foto_cd`, `plato_numero_repetido`, `plato_salir_titulo`, `plato_salir_btn_salir`) | 23 |
| 5 Pedir (5a, 5b, 5c) | 10 | 5 (`enviar_titulo`, `enviar_btn_enviar`, `salir_sin_enviar_titulo`, `salir_sin_enviar_btn_salir`, y `salir_sin_enviar_cuerpo`, cuya frase exacta no está fijada) | 15 |
| 6 Cuenta (6a, 6b, 6c) | 18 | 0 | 18 |
| 8 Comunes | 16 | 2 (`comun_menos_cd`, `comun_mas_cd`) | 18 |
| 9 Precarga y nombres fijos | 17 (sin las 3 `etiqueta_*`, P115) | 0 | 17 |
| **Total** | **119** | **19** | **138** |

Sin contar la precarga (que va a la base de datos), la interfaz del nivel 1 necesita **121 cadenas** en `strings.xml` (102 fijadas + 19 propuestas), más un `<plurals>` para el aviso RF-38 y, si se quiere, la variante singular del aviso R6 (D15). Las 17 de la precarga (P115: sin las 3 etiquetas) pueden vivir también en `strings.xml` (recomendado: así la precarga no lleva texto en el código y la versión EN queda alineada) o directamente en `Precarga.kt`.

---

## (c) contentDescription que exige el nivel 1 (spec §10: «en todo elemento interactivo e imagen»)

| Elemento | Texto | Clave | Fijado por |
|---|---|---|---|
| El «?» de plato sin foto (miniatura en 2a y 5a; foto grande en 3a y 5b) y el círculo «?» de categoría sin foto (5a, 2b) | Sin foto | `comun_sin_foto_cd` | spec §9; ficha 5; P-M-08 (**fijado**) |
| Flecha ← de la barra superior (si se separa de la palabra) | Atrás | `comun_atras` | spec §4.1 y §7 (**fijado**, es la misma palabra) |
| Botón «+» de la barra del Panel (crear categoría) | Nueva categoría | `panel_nueva_categoria_cd` | propuesta |
| Lápiz ✎ de cada caja del Panel | Editar categoría | `panel_editar_categoria_cd` | propuesta (misma palabra que el título de la hoja 2b) |
| Botón «+ Plato» | (lleva texto: no necesita cd) | `panel_btn_mas_plato` | — |
| Botón redondo «+» sobre la foto en 3a | Elegir foto | `plato_elegir_foto_cd` | propuesta |
| Botón «+ Elegir» de 2b | (lleva texto) | `categoria_btn_elegir_foto` | — |
| Botones − y + de cantidad (5b, 5c) | Quitar uno / Añadir uno | `comun_menos_cd` / `comun_mas_cd` | propuesta |
| Miniatura con foto real (2a, 5a, 5b) | nombre del plato (dato) | — | spec §10 (imagen con descripción: usar el nombre del plato) |
| Foto redonda de categoría con foto (5a) | nombre de la categoría (dato) | — | spec §10 |
| Celda de mesa en la rejilla (1d, 6a) | «Mesa %1$d» (+ «, %2$s» si tiene comanda) | `comun_mesa` (+ `comun_precio`) | propuesta de composición; el número ya es visible |
| Interruptores «Categoría activa» (2b) y «En la carta» (3a) | (llevan etiqueta de texto) | `categoria_activa` / `plato_en_la_carta` | — |
| Desplegable «Alérgenos» (5b) con su ▾ | (lleva texto) | `ficha_alergenos` | — |
| Selector de fecha «Día» (2g) con su ▾ | (lleva etiqueta y valor) | `resumen_dia` | — |
