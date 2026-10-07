# Pendientes de entender — lista viva (P177, revisión del 6 oct, R01 → A)

Una línea por concepto que salió en una sesión como «no lo entiendo» o «a medias» (apartado *Qué entendí y qué no* de las fichas) y **todavía no está explicado con las palabras de Daniel** en una ficha posterior. Se añade al cerrar cada sesión (`cerrar-sesion`, paso 4) y se cierra en la apertura siguiente: **al abrir cada sesión, 10 minutos de «explícamelo tú»** sobre **dos** conceptos que elige Daniel (`abrir-sesion`, paso 5); la respuesta va a la ficha de ese día y aquí se apunta la fecha. Los PDF del cierre (P157) explican; el chat solo comprueba (regla del 1 oct). Práctica de recuperación: recordar cuesta más que releer y por eso se queda (https://www.learningscientists.org/retrieval-practice).

**Cambio del 7 oct 2026 (S9, P183 → A, Daniel):** los diez minutos al abrir se suspenden; la lista se cierra entera en una **S14 solo de preguntas y repaso**, después de la S13. Se sigue añadiendo al cerrar cada sesión.

Creada el 7 oct 2026 [Claude] a partir de las fichas S03–S08 (lo que el informe `2026-10-06-riesgos.md` contó como «no sé» sin cerrar).

| Concepto | Salió en | Pista de la vida real (para el PDF) | Explicado con mis palabras el |
|---|---|---|---|
| Qué es un **DAO** y qué hace `@Query` (lo que Room comprueba al compilar) | S3, S4 | El camarero que lleva la nota a la cocina y trae el plato: tú no entras en la cocina (la base de datos), le pides a él | — |
| **JUnit**: qué es una prueba de código y qué hace el «actor» de la prueba | S3, S4 | Un inspector que entra en el bar cada mañana y pide lo mismo para ver si sale igual | — |
| `SUM` + `GROUP BY` en SQL (el total de una comanda, P131) | S3 | Sumar la cuenta por mesas: «todo lo de la mesa 4 junto, todo lo de la 5 junto» | — |
| `suspend` frente a `withContext(Dispatchers.Default)`: quién cambia de hilo (el PIN, P134) | S4, S5 | `suspend` es «puedo esperar»; `withContext` es «esto lo hace otro cocinero» | — |
| **R6**: por qué eliminar un plato no toca las comandas | S4, S7 | Quitar un plato de la carta no quita lo que ya está pedido en una mesa | — |
| El **contraste** (4,5:1) y por qué el naranja no sirve como texto | S5, S6 | Leer un cartel naranja sobre fondo blanco a tres metros | — |
| `DiffUtil` y `RecycledViewPool` en las listas | S6 | El camarero que solo cambia los platos que han cambiado y reutiliza las bandejas | — |
| **`Flow`**: quién avisa a la pantalla cuando cambia la base de datos (nadie «recarga», P49 B / P167 A) | S6, S8 | Un grifo abierto: cada vez que cambia el agua, sale sola; no hay que ir a buscarla | — |
| **Dónde vive cada regla** R1–R16 (base de datos, repositorio o pantalla) | S7 | Quién pone cada norma en un bar: el ayuntamiento, el dueño o el camarero | — |
| El **tercer sitio de R4**: `enviarCarrito` exige 1–99 antes de guardar (P129) | S8 | Tres filtros: el botón, el carrito y el almacén | — |
| Qué hay en memoria si `anadir` no vuelve a dejar el carrito en el tablón (`LiveData`) | S8 | El paquete cambia por dentro, pero el timbre no suena: la pantalla no se entera | — |
| Qué cambia entre 1d y 6a **fuera** de la rejilla (quién la aloja y qué hace con la mesa tocada) | S8 | La misma mesa de recepción sirve para dos oficinas: cambia quién atiende | — |
| Por qué se programa «La carta está vacía» si Agua no se puede borrar | S8 | El código no da por hecho cómo empezó la base de datos (RF-25) | — |
| **`SavedStateHandle`** (la caja fuerte de la libreta, P174) frente a recrear la pantalla | revisión 6 oct | La libreta sobrevive a girar el móvil; la caja fuerte, a que Android cierre la app por falta de memoria | — |
| **El portero del doble toque** (`GuardaDobleToque`, P171): `elapsedRealtime` y los 500 ms | revisión 6 oct | Un portero que mira el reloj y no deja entrar dos veces al mismo en medio segundo | — |
| Por qué la mesa sale **blanca** tras R7, Anular o Cobrar sin tocar la tabla `mesa` (R3: ocupada = tiene comanda PENDIENTE, lo calcula la consulta) | S9 | El cartel de «ocupado» no se cuelga: lo decide quien mira si hay alguien sentado | — |
| Por qué los datos de un Fragment van en `arguments` y no en el constructor (si Android lo rehace, «Mesa 0») | S9 | La nota grapada al plato sobrevive; lo que dijiste en voz alta al camarero, no | — |
| El **sobre de ida y vuelta** del cambio (P192 B): el recibo avisa de lo tecleado, la libreta calcula y la Activity devuelve el resultado | S9 | El camarero no hace la cuenta: se la pasa a caja y caja le devuelve el cambio | — |
