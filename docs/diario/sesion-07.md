# Sesión 07 — 2026-10-05

| Campo | Valor |
|---|---|
| **Fecha** | 2026-10-05 |
| **Sesión nº** | 07 |
| **Objetivo de la sesión** | Del spec: **Plato sin foto (3a, 3d, 3e)**; P-M-07 pasa y P-M-09, 10 y 11 quedan *Parcial* (guía S7, P6) |
| **Tiempo dedicado** | 11:35 – HH:MM (descontando pausas) |
| **Nivel / pieza** | Nivel 1 · <pantalla o capa> (p. ej. *entidades Room*, *pantalla 6c*) |
| **Commit final** | `abc1234` — mensaje del commit |
| **Contexto al cerrar** | NN % (el anillo junto al modelo; P156) |

## Qué se hizo

- Al abrir (11:35): comprobado en el código que `ConfirmacionDialog` manda `RESPUESTA_AFIRMATIVA = false` con el botón negativo (P144 A); la cadena 3e no lo toca.
- Preparación (apartado 0 de la guía), **hecha por Claude a petición de Daniel** («haz tú lo de la preparación»): app desinstalada (**se borran los datos del emulador**), `installDebug`, PIN 1234, Carnes y Postres creadas desde el `+` del Panel y Bebidas eliminada desde su lápiz (sin aviso: no tiene platos en mesas). En la base de datos: Otros (por defecto, `orden` 0), Bebidas (`activo` 0), Carnes (2), Postres (3) y Agua.
- Pieza 1: cadenas de la pantalla 3 en `strings.xml` (`comun_no`, `plato_*`, `carta_btn_salir`, `mesas_afectadas_titulo`, `<plurals>` `mesas_afectadas_cuerpo` (D15) y `recuperar_categoria_cuerpo` (P63 A), `categoria_eliminada_*`, `recuperar_categoria_*`). [Claude] Singular del segundo aviso 3e: «Volverá a la carta 1 plato.». Queda para la pieza 12: con 0 platos saldría «sus 0 platos» (el español no tiene `zero` en `<plurals>`). Compila (`assembleDebug`). Daniel, a «¿por qué no se escribe "Otros" en el aviso 3e-1?»: «porque dije que no se puede renombrar» (al revés: **sí** se puede renombrar, R16; por eso el nombre se lee de la base de datos buscando `esPorDefecto`).
- Pieza 2: borrador de `Formato.centimosDesde` probado por Claude con una prueba provisional de 20 casos (y el camino de ida y vuelta con `precio`) y deshecho. Tecleado por Daniel; no compilaba (problema 1) y Claude lo arregló. Compila. Daniel, a «¿qué da "7,5" y por qué no 705?»: «porque 05 son 5 céntimos y 50 son 50 céntimos» (bien: «5» se rellena a «50» y da 750). Commit `187b441` (piezas 1 y 2).
- Pieza 3: borrador (`PlatoActivity` con barra y título, `activity_plato.xml`, la `<activity>` en el manifiesto y `PanelActivity` con `Intent` y extras) probado por Claude en el emulador (tocar Agua → «Editar plato»; «+ Plato» → «Nuevo plato»; Atrás del sistema y la flecha vuelven al Panel) y deshecho. Tecleado por Daniel: la `<activity>` quedó fuera de `<application>` (problema 2) y la movió él al avisarle; Claude quitó dos líneas en blanco (mecánico). Comprobado en el emulador igual que el borrador. Daniel, a «¿y si `+ Plato` no grapara el id de la categoría?»: «se iría a la categoría por defecto» (bien, ficha 3). Commit `61085d5`.
- **P162 · Cómo recoge el formulario sus datos al abrirse → B** (Daniel eligió B entre A/B/C): A) cuatro `LiveData` (`plato`, `categorias`, `alergenos`, `alergenosDelPlato`) y `cargar()` solo con `savedInstanceState == null` (Claude, guía; pega: llegan por separado y, si Android restaura la app tras cerrarla en segundo plano, el formulario sale vacío) · **B) una sola bandeja `DatosFormulario` con las cuatro cosas en un solo `LiveData` (`datos`); `cargar()` la llena una vez con `viewModelScope.launch` y no repite; la pantalla la pide siempre** (https://developer.android.com/topic/architecture/ui-layer, «UI states: single stream or multiple streams?», consultado el 5 oct 2026) · C) `suspend fun cargar(id): DatosFormulario` desde `lifecycleScope`, como `comprobarPin` (Claude). Cambia la guía S7 (piezas 4 y 6: sin bandera `formularioRelleno` para esperar a plato y categorías) y el diagrama (`PlatoViewModel`): a «Para el Project» al cerrar.
- Pieza 4: borrador (`PlatoViewModel` con `DatosFormulario`, `cargar`, `Factory`; `PlatoActivity` con `by viewModels`, `cargar` y un `Log` provisional) probado por Claude: en Logcat, Agua → «Plato: Agua · categorías: 4 · alérgenos: 14 · marcados: []»; «+ Plato» → «Plato: null…». Deshecho. Tecleado por Daniel: igual que el borrador; compila y Logcat da lo mismo con Agua (comprobado por Claude instalando su código). Daniel, a «¿por qué la pantalla no pide el plato directamente al repositorio?»: «porque la libreta guarda los datos» (bien en parte: además, por el spec 3 la pantalla nunca habla con el repositorio; la libreta hace de intermediaria y sobrevive a que Android recree la pantalla).
- Descanso para comer de Daniel tras la pieza 4 (commit `f90ae18`). El emulador se apagó solo al llegar al límite de 2 h en segundo plano; Claude lo volvió a encender. Contexto al volver: 25 % (Daniel).
- Pieza 5: borrador de `activity_plato.xml` entero probado por Claude en el emulador y deshecho: se ve como 03a; con el teclado abierto *Guardar* sube por encima; el precio admite «18,50»; en el número, «-12» queda «12» (el teclado numérico de Gboard enseña la tecla «−», pero el campo no la deja entrar: `number` sin `numberSigned`). [Claude] El desplegable lleva su propio estilo (*ExposedDropdownMenu*) y pierde el retoque de P160; se le pone `android:theme="@style/ThemeOverlay.AKO.CampoTexto"`.

## Problemas y soluciones

| # | Qué falló | Cómo se resolvió | Justificación (por qué esta solución y no otra) | Fuente |
|---|---|---|---|---|
| 1 | Pieza 2: `compileDebugKotlin` falla («Compilation error») | La palabra «porque» de la respuesta a la pregunta de comprensión se escribió dentro de `Formato.kt` (Android Studio tenía el foco), en la línea `if (partes.size > 2) return null`; Claude la quitó | Es la misma lección del 1 oct (respuestas escritas en un XML): por eso se mira `git diff` antes de compilar tras cada «ya está». Se quita la palabra y nada más; el resto del bloque coincidía | Propio |
| 2 | Pieza 3: en `AndroidManifest.xml`, `<activity>` subrayada en rojo | El bloque se pegó debajo del primer comentario, fuera de `<application>`; Daniel lo cortó (Ctrl+X) y lo pegó dentro, tras la `<activity>` del Panel | Android solo conoce las pantallas declaradas **dentro** de `<application>`; fuera, la etiqueta no vale y abrir `PlatoActivity` cerraría la app. La indicación «antes de `</application>`» no bastó: la próxima vez, decir «después de la línea X» con el texto exacto de esa línea | Propio |

## Qué entendí y qué no

- **Entendí:** lo que ahora podría explicar sin leerlo (una línea por concepto).
- **No entendí todavía:** lo que funciona pero no sabría explicar. Se vuelve a ello en la sesión siguiente.

## Para el vídeo

- Lo que se ha explicado en la sesión y se podría contar en el vídeo de 10 minutos: una línea por idea, con la comparación de la vida real si la hubo y la decisión (Pnnn) que la respalda. Sirve para montar el guion al final.

## Pruebas

- Pruebas de código que pasan al terminar: P-C-nn, …
- Pruebas manuales ejecutadas en esta sesión: P-M-nn (resultado y fecha ya apuntados en `spec+doc-pruebas.md`).

## Uso de IA en esta sesión

- Qué pidió Daniel a Claude Code, qué generó, qué revisó o cambió Daniel a mano. Una línea por pieza. (Alimenta la frase de P37; **lo escrito coincide con lo hecho**.)

## Siguiente sesión

- Con qué se empieza, en una línea.
