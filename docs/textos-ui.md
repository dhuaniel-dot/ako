# Textos de la interfaz de Ako

Esta tabla sale de los dos strings.xml de la app: values/strings.xml (español, el idioma por defecto) y values-en/strings.xml (inglés). Si cambia un strings.xml, se cambia aquí. La regeneré el 8 de octubre de 2026; el borrador de la sesión 00, con las dudas D1 a D20 y los textos del nivel 2, está en docs/guias/textos-ui-borrador-s00.md.

Hay 143 claves: 139 string y 4 plurals. 116 se traducen y tienen su inglés; 27 no se traducen: 10 son símbolos y formatos (AKO, %1$s €, −, +, ×) y 17 son datos de la precarga (los 14 alérgenos, Otros, Bebidas y Agua), que son datos de la carta y no frases de la app.

Cómo se lee: el orden es el de strings.xml, por pantallas. %1$d es un número entero y %1$s un texto ya escrito (por ejemplo, un importe con su €). En los plurals, one es la forma de uno y other la de varios. «Dónde se usa» son los archivos que nombran la clave. La nota es el comentario que lleva la cadena en strings.xml. Los espacios duros salen como espacios normales.

---

## 1. Nombre de la app

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `app_name` | AKO | — (no se traduce) | `AndroidManifest.xml`, `fragment_selector.xml` |  |

## 2. Comunes: botones y barras que se repiten en varias pantallas

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `comun_atras` | ← Atrás | ← Back | `activity_panel.xml`, `activity_plato.xml`, `fragment_carrito.xml`, `fragment_comanda.xml`, `fragment_ficha_plato.xml`, `fragment_recibo.xml`, `fragment_rejilla_mesas.xml`, `fragment_resumen_lista.xml` |  |
| `comun_atras_cd` | Atrás | Back | `activity_panel.xml`, `activity_plato.xml`, `fragment_carrito.xml`, `fragment_comanda.xml`, `fragment_ficha_plato.xml`, `fragment_recibo.xml`, `fragment_rejilla_mesas.xml`, `fragment_resumen_lista.xml` | [Claude] Lo que lee el lector de pantalla en el botón Atrás (sin la flecha) |
| `comun_aceptar` | Aceptar | OK | `CambiarPinDialog.kt`, `PinDialog.kt`, `SelectorFragment.kt`, `fragment_crear_pin.xml` |  |
| `comun_cancelar` | Cancelar | Cancel | `CambiarPinDialog.kt`, `CarritoFragment.kt`, `ComandaFragment.kt`, `MesasAfectadasDialog.kt`, `PedidoActivity.kt`, `PinDialog.kt`, `PlatoActivity.kt`, `ReciboFragment.kt` |  |
| `comun_guardar` | Guardar | Save | `activity_plato.xml`, `sheet_categoria.xml` |  |
| `comun_eliminar` | Eliminar | Remove | `MesasAfectadasDialog.kt` |  |
| `comun_precio` | %1$s € | — (no se traduce) | `CarritoFragment.kt`, `CartaFragment.kt`, `ComandaCobradaAdapter.kt`, `ComandaFragment.kt`, `FichaPlatoFragment.kt`, `FilaPlatoAdapter.kt`, `LineaAdapter.kt`, `MesaAdapter.kt`, `ReciboFragment.kt`, `ResumenListaFragment.kt` | Precio ya formateado + espacio que no se parte (&#160;) + € |
| `comun_plato_numero_nombre` | %1$d · %2$s | — (no se traduce) | `FichaPlatoFragment.kt`, `FilaPlatoAdapter.kt` | «12 · Entrecot»: número · nombre (punto medio) |
| `comun_mas` | + | — (no se traduce) | `activity_panel.xml`, `activity_plato.xml`, `fragment_ficha_plato.xml`, `item_linea.xml` |  |
| `comun_sin_foto_cd` | Sin foto | No photo | `Fotos.kt`, `activity_plato.xml`, `fragment_ficha_plato.xml`, `item_categoria_fila.xml`, `item_fila_plato.xml`, `sheet_categoria.xml` |  |
| `comun_y` | y | and | `MesasAfectadasDialog.kt` | [Claude] La «y» de las listas («mesas 5, 6 y 7»); los espacios los pone el código (XML se come los de los bordes) |
| `comun_no` | No | No | `PlatoActivity.kt` | Botón negativo de los dos avisos 3e |

## 3. 1a · Selector de rol

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `selector_subtitulo` | Pedidos para bares y restaurantes | Orders for bars and restaurants | `fragment_selector.xml` |  |
| `selector_btn_propietario` | Propietario | Owner | `fragment_selector.xml` |  |
| `selector_btn_pedir` | Pedir | Order | `fragment_selector.xml` |  |
| `selector_btn_cuenta` | Cuenta | Bill | `fragment_selector.xml` |  |

## 4. 1b · Crear PIN (barra superior sin Atrás)

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `pin_crear_barra` | Crear PIN | Create PIN | `fragment_crear_pin.xml` |  |
| `pin_crear_titulo` | Crea el PIN del Propietario | Create the Owner PIN | `fragment_crear_pin.xml` |  |
| `pin_crear_subtitulo` | Protege el Panel y la salida de Pedir. | It protects the Panel and the way out of Order. | `fragment_crear_pin.xml` |  |
| `pin_hint_pin` | PIN (4 cifras) | PIN (4 digits) | `dialog_pin.xml`, `fragment_crear_pin.xml` |  |
| `pin_hint_repite` | Repite el PIN | Repeat the PIN | `fragment_crear_pin.xml` |  |
| `pin_crear_aviso` | Si lo olvidas, no se puede recuperar: habrá que reinstalar la app. | If you forget it, it cannot be recovered: the app will have to be reinstalled. | `fragment_crear_pin.xml` | Versión larga del wireframe |
| `pin_no_coinciden` | Los PIN no coinciden | The PINs do not match | `CambiarPinDialog.kt`, `CrearPinFragment.kt` | Errores del PIN (1b, 1c y 1e) |
| `pin_incorrecto` | PIN incorrecto | Wrong PIN | `CambiarPinDialog.kt`, `PinDialog.kt` |  |

## 5. 1c · Introducir PIN

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `pin_introducir_titulo` | Introduce el PIN | Enter the PIN | `PinDialog.kt` |  |

## 6. 1e · Cambiar PIN (los tres campos a la vez)

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `pin_cambiar_titulo` | Cambiar PIN | Change PIN | `CambiarPinDialog.kt`, `activity_panel.xml` | El título sirve también para el botón del Panel |
| `pin_hint_actual` | PIN actual | Current PIN | `dialog_cambiar_pin.xml` |  |
| `pin_hint_nuevo` | PIN nuevo | New PIN | `dialog_cambiar_pin.xml` |  |
| `pin_hint_repite_nuevo` | Repite el PIN nuevo | Repeat the new PIN | `dialog_cambiar_pin.xml` |  |

## 7. 2a · Panel: botón Terminar

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `panel_btn_terminar` | Terminar | Finish | `activity_panel.xml` |  |

## 8. 2a · Panel

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `panel_titulo` | Categorías | Categories | `activity_panel.xml` |  |
| `panel_btn_resumen_ingresos` | Resumen de ingresos | Income summary | `activity_panel.xml` |  |
| `panel_categoria_eliminada` | Categoría eliminada | Category removed | `PlatoActivity.kt`, `item_categoria_caja.xml` |  |
| `panel_plato_eliminado` | Eliminado | Removed | `item_fila_plato.xml` |  |
| `panel_btn_mas_plato` | + Plato | + Dish | `item_categoria_caja.xml` |  |
| `panel_plegar_cd` | Plegar | Collapse | `item_categoria_caja.xml` | [Claude] El texto de partida del icono de plegar en item_categoria_caja.xml; al pintar la caja, CategoriaAdapter pone el que lleva el nombre (panel_plegar_categoria_cd / panel_desplegar_categoria_cd) |

## 9. 2b · Hoja de categoría

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `categoria_titulo_nueva` | Nueva categoría | New category | `CategoriaBottomSheet.kt`, `activity_panel.xml` | Los títulos sirven también de contentDescription del + y del lápiz |
| `categoria_titulo_editar` | Editar categoría | Edit category | `CategoriaBottomSheet.kt`, `item_categoria_caja.xml` |  |
| `categoria_hint_nombre` | Nombre | Name | `sheet_categoria.xml` |  |
| `categoria_foto_opcional` | Foto (opcional) | Photo (optional) | `sheet_categoria.xml` |  |
| `categoria_btn_elegir_foto` | + Elegir | + Choose | `sheet_categoria.xml` |  |
| `plato_en_la_carta` | En la carta | On the menu | `activity_plato.xml`, `sheet_categoria.xml` | El interruptor de 2b se llama igual que el del plato; apagarlo ELIMINA la categoría |
| `categoria_nombre_repetido` | Ya existe una categoría con ese nombre | A category with that name already exists | `CategoriaBottomSheet.kt` |  |

## 10. 2e · Aviso agrupado al eliminar una categoría con platos en comandas pendientes

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `categoria_eliminar_titulo` | ¿Eliminar la categoría? | Remove the category? | `MesasAfectadasDialog.kt` |  |
| `categoria_eliminar_cuerpo` (plurals) | one: Este plato está en comandas pendientes: %1$s. No se quitará de esas comandas. · other: Estos platos están en comandas pendientes: %1$s. No se quitarán de esas comandas. | one: This dish is in pending orders: %1$s. It will not be taken off those orders. · other: These dishes are in pending orders: %1$s. They will not be taken off those orders. | `MesasAfectadasDialog.kt` | %1$s = la lista de platos, p. ej. «Flan (mesa 5), Helado (mesas 5 y 7)» |
| `categoria_eliminar_plato_mesas` (plurals) | one: %1$s (mesa %2$s) · other: %1$s (mesas %2$s) | one: %1$s (table %2$s) · other: %1$s (tables %2$s) | `MesasAfectadasDialog.kt` | [Claude] Un plato con sus mesas: %1$s = nombre, %2$s = «5» o «5 y 7» |

## 11. 3a · Formulario del plato

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `plato_titulo_nuevo` | Nuevo plato | New dish | `PlatoActivity.kt` |  |
| `plato_titulo_editar` | Editar plato | Edit dish | `PlatoActivity.kt` |  |
| `foto_error` | No se ha podido usar esa foto | That photo could not be used | `CategoriaBottomSheet.kt`, `PlatoActivity.kt` | La foto elegida no se ha podido leer (archivo roto, no es una imagen) |
| `plato_elegir_foto_cd` | Elegir foto | Choose photo | `activity_plato.xml` |  |
| `plato_hint_nombre` | Nombre * | Name * | `activity_plato.xml` | Obligatorios con * |
| `plato_hint_numero` | Número * | Number * | `activity_plato.xml` |  |
| `plato_hint_precio` | Precio (€) * | Price (€) * | `activity_plato.xml` |  |
| `plato_hint_categoria` | Categoría * | Category * | `activity_plato.xml` |  |
| `plato_hint_descripcion` | Descripción | Description | `activity_plato.xml` |  |
| `plato_seccion_alergenos` | ALÉRGENOS | ALLERGENS | `activity_plato.xml` |  |
| `plato_numero_repetido` | Ese número ya lo tiene otro plato | Another dish already has that number | `PlatoActivity.kt` | Error del campo número al guardar (número de plato sin repetir) |
| `plato_numero_cambiado` | Si alguien tiene apuntado el %1$d, dejará de corresponder | If someone has number %1$d written down, it will no longer match | `PlatoActivity.kt` | Ayuda al cambiar el número de un plato ya creado; %1$d = el número ANTERIOR |

## 12. 3a · Atrás con cambios sin guardar · comun_cancelar / carta_btn_salir

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `plato_salir_titulo` | ¿Salir sin guardar? | Leave without saving? | `PlatoActivity.kt` |  |
| `plato_salir_cuerpo` | Se perderán los cambios | The changes will be lost | `PlatoActivity.kt` |  |
| `carta_btn_salir` | Salir | Leave | `PedidoActivity.kt`, `PlatoActivity.kt`, `fragment_carta.xml` | El mismo «Salir» que la barra de la carta (5a) |

## 13. 3d · Aviso de las mesas al apagar «En la carta» y guardar · comun_cancelar / comun_eliminar

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `mesas_afectadas_titulo` | ¿Eliminar el plato? | Remove the dish? | `MesasAfectadasDialog.kt` |  |
| `mesas_afectadas_cuerpo` (plurals) | one: %1$s está en una comanda pendiente de la mesa %2$s. No se quitará de esa comanda. · other: %1$s está en comandas pendientes de las mesas %2$s. No se quitará de esas comandas. | one: %1$s is in a pending order at table %2$s. It will not be taken off that order. · other: %1$s is in pending orders at tables %2$s. It will not be taken off those orders. | `MesasAfectadasDialog.kt` | Cuenta las mesas; %1$s = nombre del plato, %2$s = «6» o «4 y 7» |

## 14. 3e · aviso 1 de 2 · título: panel_categoria_eliminada · comun_no / categoria_eliminada_btn_mover

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `categoria_eliminada_cuerpo` | La categoría %1$s está eliminada. ¿Muevo el plato a %2$s? | The category %1$s is removed. Shall I move the dish to %2$s? | `PlatoActivity.kt` | %1$s = la categoría eliminada, %2$s = el nombre REAL de la categoría por defecto |
| `categoria_eliminada_btn_mover` | Sí, mover | Yes, move | `PlatoActivity.kt` |  |

## 15. 3e · aviso 2 de 2 · comun_no / recuperar_categoria_btn_recuperar (recuperar, no reactivar)

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `recuperar_categoria_titulo` | ¿Quieres recuperar %1$s? | Do you want to restore %1$s? | `PlatoActivity.kt` |  |
| `recuperar_categoria_cuerpo` | Platos que volverán a la carta: %1$d | Dishes that will return to the menu: %1$d | `PlatoActivity.kt` | Frase neutra que vale para 0, 1 o varios; %1$d = platos que vuelven |
| `recuperar_categoria_btn_recuperar` | Recuperar | Restore | `PlatoActivity.kt` |  |

## 16. Comunes que estrena Pedir (también los usan Cuenta y el recibo)

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `comun_mesa` | Mesa %1$d | Table %1$d | `CartaFragment.kt`, `ComandaCobradaAdapter.kt`, `ComandaFragment.kt`, `MesaAdapter.kt` |  |
| `comun_total` | TOTAL | TOTAL | `fragment_carrito.xml`, `fragment_comanda.xml`, `fragment_recibo.xml` |  |
| `comun_quitar` | Quitar | Take off | `item_linea.xml` | «Quitar» en cada línea del carrito |
| `comun_linea` | %1$d × %2$s | — (no se traduce) | `LineaAdapter.kt` | «2 × Entrecot»: cantidad × nombre (signo × U+00D7) |
| `comun_numero` | %1$d | — (no se traduce) | `FichaPlatoFragment.kt`, `MesaAdapter.kt`, `PlatoActivity.kt`, `ResumenListaFragment.kt` | Un número entero suelto (mesa, cantidad, número de plato, comandas del día), escrito con los dígitos del idioma del móvil y no con toString() |
| `comun_menos` | − | — (no se traduce) | `fragment_ficha_plato.xml`, `item_linea.xml` | Signo menos U+2212 (el + ya está arriba, comun_mas) |
| `comun_menos_cd` | Quitar uno | One less | `fragment_ficha_plato.xml`, `item_linea.xml` | Lo que lee el lector de pantalla en los botones − y + de 5b y 5c |
| `comun_mas_cd` | Añadir uno | One more | `fragment_ficha_plato.xml`, `item_linea.xml` |  |
| `linea_menos_cd` | Quitar uno de %1$s | One less of %1$s | `LineaAdapter.kt` | Lo que lee el lector de pantalla en los botones que se repiten, con el nombre para distinguirlos: −, + y Quitar de cada línea (5c, 6b), y plegar y lápiz de cada caja del Panel (2a) |
| `linea_mas_cd` | Añadir uno de %1$s | One more of %1$s | `LineaAdapter.kt` |  |
| `linea_quitar_cd` | Quitar %1$s | Take off %1$s | `LineaAdapter.kt` |  |
| `panel_plegar_categoria_cd` | Plegar %1$s | Collapse %1$s | `CategoriaAdapter.kt` |  |
| `panel_desplegar_categoria_cd` | Desplegar %1$s | Expand %1$s | `CategoriaAdapter.kt` |  |
| `panel_editar_categoria_cd` | Editar %1$s | Edit %1$s | `CategoriaAdapter.kt` |  |

## 17. 1d · rejilla en modo elegir

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `mesa_elegir_titulo` | Elige la mesa | Choose the table | `SelectorActivity.kt` |  |
| `mesa_ocupada_cd` | %1$s, %2$s | — (no se traduce) | `MesaAdapter.kt` | Lo que lee el lector de pantalla en una mesa ocupada: «Mesa 4, 42,00 €» (%1$s = comun_mesa ya montado, %2$s = total ya con su €); la coma sale de aquí, no del código (para español e inglés) |

## 18. Puerta de Pedir · solo el botón comun_aceptar

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `puerta_pedir_titulo` | No se puede pedir | Ordering is not possible | `SelectorFragment.kt` |  |
| `puerta_pedir_carta_vacia` | La carta está vacía | The menu is empty | `SelectorViewModel.kt` |  |
| `puerta_pedir_categorias_eliminadas` | Todas las categorías están eliminadas | All the categories are removed | `SelectorViewModel.kt` |  |

## 19. 5a · la carta

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `carta_carrito_pastilla` | Carrito · %1$d · %2$s | Cart · %1$d · %2$s | `CartaFragment.kt` | %1$d = número de platos, %2$s = total ya con su € (comun_precio) |

## 20. 5b · ficha del plato

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `ficha_alergenos` | Alérgenos | Allergens | `fragment_ficha_plato.xml` | Título del desplegable |
| `ficha_alergeno_item` | • %1$s | — (no se traduce) | `FichaPlatoFragment.kt` | Una línea por alérgeno marcado |
| `ficha_sin_alergenos` | El restaurante no ha indicado alérgenos para este plato. Pregunta al personal. | The restaurant has not listed any allergens for this dish. Ask the staff. | `FichaPlatoFragment.kt` |  |
| `ficha_cantidad` | Cantidad | Quantity | `fragment_ficha_plato.xml` |  |
| `ficha_btn_anadir` | Añadir — %1$s | Add — %1$s | `FichaPlatoFragment.kt` | Guion largo U+2014; %1$s = importe ya con su € |
| `carrito_tope_99` | Máximo 99 unidades por plato | Maximum 99 units per dish | `FichaPlatoFragment.kt` | [Claude] Aviso al topar en 99 al añadir |

## 21. 5c · carrito

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `carrito_titulo` | Carrito · Mesa %1$d | Cart · Table %1$d | `CarritoFragment.kt` | El botón sirve también de afirmativo en la confirmación |
| `carrito_btn_enviar` | Enviar | Send | `CarritoFragment.kt`, `fragment_carrito.xml` |  |

## 22. 5c · confirmación de Enviar · comun_cancelar / carrito_btn_enviar

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `enviar_titulo` | ¿Enviar el pedido? | Send the order? | `CarritoFragment.kt` | %2$s = total ya con su € |
| `enviar_cuerpo` | Mesa %1$d · %2$s | Table %1$d · %2$s | `CarritoFragment.kt` |  |
| `enviar_hecho` | Pedido enviado a la mesa %1$d | Order sent to table %1$d | `CarritoFragment.kt` | [Claude] Aviso tras enviar |
| `salir_sin_enviar_titulo` | ¿Salir sin enviar? | Leave without sending? | `PedidoActivity.kt` | Salir con platos sin enviar, antes del PIN · comun_cancelar / carta_btn_salir |
| `salir_sin_enviar_cuerpo` (plurals) | one: Hay %1$d plato en el carrito sin enviar. Se perderá. · other: Hay %1$d platos en el carrito sin enviar. Se perderán. | one: There is %1$d dish in the cart not sent yet. It will be lost. · other: There are %1$d dishes in the cart not sent yet. They will be lost. | `PedidoActivity.kt` | %1$d = platos en el carrito: decide la forma y se escribe en la frase |

## 23. 6 · Cuenta (6a Rejilla, 6b Comanda, 6c Recibo)

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `cuenta_titulo` | Cuenta | Bill | `CuentaActivity.kt` |  |
| `cuenta_mesa_sin_comanda` | La mesa %1$d no tiene comanda | Table %1$d has no order | `CuentaActivity.kt` | Snackbar al tocar una mesa blanca |
| `comanda_btn_anular` | Anular | Void | `ComandaFragment.kt`, `fragment_comanda.xml` | Barra de 6b y también botón afirmativo del aviso de Anular |
| `comanda_btn_dar_cuenta` | Dar la cuenta | Give the bill | `fragment_comanda.xml` |  |
| `recibo_titulo` | Mesa %1$d · Recibo | Table %1$d · Receipt | `ReciboFragment.kt` | 6c y el recibo en solo lectura de 2g |
| `recibo_entregado` | Entregado | Given | `fragment_recibo.xml` |  |
| `recibo_opcional` | (opcional) | (optional) | `fragment_recibo.xml` |  |
| `recibo_euro_sufijo` | € | — (no se traduce) | `fragment_recibo.xml` | Sufijo a la derecha del campo Entregado |
| `recibo_cambio` | Cambio | Change | `fragment_recibo.xml` |  |
| `recibo_btn_cobrar` | Cobrar | Charge | `ReciboFragment.kt`, `fragment_recibo.xml` | Botón de 6c y también botón afirmativo del aviso de Cobrar |

## 24. 6b · aviso de Anular · comun_cancelar / comanda_btn_anular

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `anular_titulo` | ¿Anular la comanda de la mesa %1$d? | Void the order of table %1$d? | `ComandaFragment.kt` |  |
| `anular_cuerpo` | No se puede deshacer | This cannot be undone | `ComandaFragment.kt` |  |

## 25. 6b · Aviso al quitar la última línea (sin líneas = anulada) · comun_cancelar / ultima_linea_btn_quitar_anular

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `ultima_linea_titulo` | ¿Quitar la última línea? | Take off the last line? | `ComandaFragment.kt` |  |
| `ultima_linea_cuerpo` | La comanda se quedará vacía y se anulará. La mesa %1$d quedará libre. | The order will be empty and will be voided. Table %1$d will be free. | `ComandaFragment.kt` |  |
| `ultima_linea_btn_quitar_anular` | Quitar y anular | Take off and void | `ComandaFragment.kt` |  |

## 26. 6c · aviso de Cobrar · comun_cancelar / recibo_btn_cobrar

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `cobrar_titulo` | ¿Cobrar la mesa %1$d? | Charge table %1$d? | `ReciboFragment.kt` | %1$s = total ya con su € |
| `cobrar_cuerpo` | Total: %1$s. / La comanda quedará PAGADA y no se podrá modificar. | Total: %1$s. / The order will be PAID and cannot be changed. | `ReciboFragment.kt` |  |

## 27. 2g · Resumen de ingresos (el título dice «Resumen de ingresos»; el pie en dos etiquetas)

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `resumen_titulo` | Resumen de ingresos | Income summary | `fragment_resumen_lista.xml` | La fecha del campo Día no es una cadena: la escribe Formato.fecha en el idioma del móvil |
| `resumen_dia` | Día | Day | `fragment_resumen_lista.xml` |  |
| `resumen_seccion_comandas` | COMANDAS COBRADAS | CHARGED ORDERS | `fragment_resumen_lista.xml` |  |
| `resumen_fila_hora` | Cobrada a las %1$s | Charged at %1$s | `ComandaCobradaAdapter.kt` | %1$s = la hora de cobro ya escrita por Formato.hora («14:32») |
| `resumen_pie_comandas` | Comandas cobradas | Charged orders | `fragment_resumen_lista.xml` |  |
| `resumen_pie_total` | Total del día | Day total | `fragment_resumen_lista.xml` |  |
| `resumen_vacio` | No se cobró ninguna comanda ese día | No order was charged that day | `fragment_resumen_lista.xml` |  |

## 28. Precarga: estos textos se guardan en la base de datos la primera vez que se abre la app

| Clave | Español | Inglés | Dónde se usa | Nota |
|---|---|---|---|---|
| `alergeno_01_gluten` | Gluten | — (no se traduce) | `Precarga.kt` | Los 14 alérgenos legales (anexo II del Reglamento UE 1169/2011). El número fija el orden |
| `alergeno_02_crustaceos` | Crustáceos | — (no se traduce) | `Precarga.kt` |  |
| `alergeno_03_huevos` | Huevos | — (no se traduce) | `Precarga.kt` |  |
| `alergeno_04_pescado` | Pescado | — (no se traduce) | `Precarga.kt` |  |
| `alergeno_05_cacahuetes` | Cacahuetes | — (no se traduce) | `Precarga.kt` |  |
| `alergeno_06_soja` | Soja | — (no se traduce) | `Precarga.kt` |  |
| `alergeno_07_lacteos` | Lácteos | — (no se traduce) | `Precarga.kt` |  |
| `alergeno_08_frutos_cascara` | Frutos de cáscara | — (no se traduce) | `Precarga.kt` |  |
| `alergeno_09_apio` | Apio | — (no se traduce) | `Precarga.kt` |  |
| `alergeno_10_mostaza` | Mostaza | — (no se traduce) | `Precarga.kt` |  |
| `alergeno_11_sesamo` | Sésamo | — (no se traduce) | `Precarga.kt` |  |
| `alergeno_12_sulfitos` | Sulfitos | — (no se traduce) | `Precarga.kt` |  |
| `alergeno_13_altramuces` | Altramuces | — (no se traduce) | `Precarga.kt` |  |
| `alergeno_14_moluscos` | Moluscos | — (no se traduce) | `Precarga.kt` |  |
| `precarga_categoria_por_defecto` | Otros | — (no se traduce) | `Precarga.kt` | Categoría por defecto (se reconoce por esPorDefecto, nunca por el nombre) |
| `precarga_categoria_ejemplo` | Bebidas | — (no se traduce) | `Precarga.kt` | Categoría y plato de ejemplo: 1 · Agua · 1,50 € |
| `precarga_plato_ejemplo` | Agua | — (no se traduce) | `Precarga.kt` |  |

---

## Cómo quedaron las dudas de la sesión 00 (D1–D20)

El borrador las dejó «sin resolver»; en el código quedaron así (decisiones en `docs/decisiones-code.md`).

| # | Duda | Cómo quedó |
|---|---|---|
| D1 | Aviso de 1b, corto o largo | Largo, como el dibujo: «Si lo olvidas, no se puede recuperar: habrá que reinstalar la app.» (`pin_crear_aviso`) |
| D2 | Botón del Panel al Resumen | «Resumen de ingresos» (`panel_btn_resumen_ingresos`) |
| D3 | Marca de categoría eliminada | «Categoría eliminada» (`panel_categoria_eliminada`) |
| D4 | Interruptor de 2b | El mismo que el del plato: «En la carta» (`plato_en_la_carta`, P41); apagarlo elimina la categoría |
| D5 | Título de 2g | «Resumen de ingresos» (`resumen_titulo`) |
| D6 | Pie de 2g | Dos etiquetas, como el dibujo: «Comandas cobradas» y «Total del día» (`resumen_pie_comandas`, `resumen_pie_total`) |
| D7 | Pastilla del carrito | «Carrito · %1$d · %2$s» (`carta_carrito_pastilla`) |
| D8 | Título de 1c | «Introduce el PIN» (`pin_introducir_titulo`) |
| D9 | Botones del aviso 3e-1 | «No» / «Sí, mover» (`comun_no`, `categoria_eliminada_btn_mover`; P40: el botón dice lo que hace) |
| D10 | Aviso 3e-2: reactivar o recuperar | **Recuperar** (P9): «¿Quieres recuperar %1$s?» · «No» / «Recuperar» |
| D11 | Confirmación de Anular | «¿Anular la comanda de la mesa %1$d?» · «No se puede deshacer»; botones «Cancelar» / «Anular» (P40) |
| D12 | Botones de Cobrar | «Cancelar» / «Cobrar» (P40) |
| D13 | Botón de quitar línea | «Quitar» (`comun_quitar`) |
| D14 | Cuerpo del aviso 3e-2 | Frase neutra para 0, 1 o varios: «Platos que volverán a la carta: %1$d» (P164 B) |
| D15 | Aviso R6 con una sola mesa | `<plurals>` con su singular: `mesas_afectadas_cuerpo` y `categoria_eliminar_cuerpo` |
| D16 | Aviso RF-38 | `<plurals>`: one: Hay %1$d plato en el carrito sin enviar. Se perderá. · other: Hay %1$d platos en el carrito sin enviar. Se perderán. (`salir_sin_enviar_cuerpo`) |
| D17 | Snackbar de mesa blanca | «La mesa %1$d no tiene comanda» (`cuenta_mesa_sin_comanda`) |
| D18 | Campos de 1e | Los tres a la vez (P46 B): «PIN actual», «PIN nuevo» y «Repite el PIN nuevo» |
| D19 | Alérgenos en 3a y 5b | Dos cadenas: «ALÉRGENOS» en 3a (`plato_seccion_alergenos`) y «Alérgenos» en 5b (`ficha_alergenos`) |
| D20 | «Otros» dentro del aviso 3e-1 | Con marcador: «La categoría %1$s está eliminada. ¿Muevo el plato a %2$s?»; `%2$s` es el nombre real de la categoría por defecto (R16) |

## Textos que no son cadenas

- **La fecha del campo *Día* de 2g** la escribe `Formato.fecha` con la forma larga del idioma del móvil (`ofLocalizedDate(FormatStyle.FULL)`, S13 1 → B): «Jueves, 8 de octubre de 2026» / «Thursday, October 8, 2026». La hora de cobro, `Formato.hora` («14:32»).
- **Los importes** («18,50») los escribe `Formato.precio` y llevan coma también en inglés (H08 del 6 oct, declarado).
- **Los nombres de platos, categorías y alérgenos** son datos de la base, no cadenas de la app (salvo los de la precarga, que salen de aquí la primera vez).

