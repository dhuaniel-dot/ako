> **Guía del juego de datos y del orden de las 29 pruebas manuales.** Escrita en la sesión 00 (noche del 24 al 25 sep 2026) [Claude]. Sale del apartado 2 de `docs/spec+doc-pruebas.md` (juego de datos y *orden de ejecución recomendado*), del calendario de las 29 P-M fijado esta noche con la decisión **P6** (Parcial con fecha y se repite entera cuando existe el prerrequisito) y de **P7** y **P8** (la categoría de ejemplo Bebidas y su eliminación). **No edita el plan de pruebas**: dice qué se hace, en qué orden y qué se anota en cada sesión. Todo lo que el plan no fija va marcado **[Claude]**.
> **Cuándo se usa:** en el bloque de pruebas de cada sesión, de la S5 a la S13 (apartado 3), y entera en la pasada final de la S13 (apartado 4). Las guías de sesión remiten aquí.
> **Botones:** los de los diálogos son los del wireframe (**P40**): donde el plan dice «Sí / No», «aceptar» o «confirmar», aquí va el botón real (tabla del apartado 2.0).

# Juego de datos y orden de las pruebas — guía

## 1. Qué es y cómo se usa

**El juego de datos** es la carta inventada con la que se prueba todo (apartado 2 del plan): PIN **1234**; **Carnes** con **12 · Entrecot · 18,50 €** (Gluten y Lácteos) y **14 · Pollo asado · 11,00 €**; **Postres** con **30 · Flan · 4,50 €**; **Helado 32 · 5,00 €** en **Otros**; y las mesas **4, 5, 6 y 7**. No se mete de golpe: **lo van creando las propias pruebas**, una detrás de otra. Por eso el orden importa: cada prueba tiene que encontrar hecho lo que pide su *Entrada* y dejar lo que necesita la siguiente.

**La precarga** es lo que la app trae al instalarse (RF-50, `Precarga`, S2): la categoría **Otros** (la de por defecto: siempre la última y sin interruptor, R16), las **60 mesas**, los **14 alérgenos** y la categoría de ejemplo **Bebidas** (activa, la primera) con el plato **1 · Agua · 1,50 €** (**P7**). Sin etiquetas: las 3 (Vegano, Vegetariano, Pescetariano) llegan con el incremento 1 (**P115**). Bebidas estorba en las pruebas del Panel y de la carta, así que **se elimina en cuanto dejan de necesitarla P-M-29 y P-M-05** (**P8**, paso 6 del montaje).

**Empezar de cero** (instalación limpia) borra la base de datos, el PIN y las fotos de la app. Dos formas:

- **Clear storage (recomendada, más rápida):** en el emulador, *Settings → Apps → All apps* (sale *See all N apps* si hay apps recientes) *→ AKO → Storage & cache → Clear storage →* **Delete** (la app se llama **AKO** en la lista: `app_name`, P105). La app sigue instalada: se abre y arranca en 1b como el primer día. *(Ruta comprobada en el `Pixel_6_API_34` el 24 sep 2026 [Claude], con el emulador en inglés; desde la S10 está en español, P103 → A, y los nombres del menú salen traducidos.)* Claude puede hacer lo mismo desde la consola con `adb shell pm clear yunkang.ako`.
- **Desinstalar:** mantener pulsado el icono de AKO → *App info → Uninstall*; después **Run ▶** en Android Studio.

Tres cosas que conviene saber:

- **Run ▶ no borra nada**: reinstala el código y **conserva** los datos. Si una sesión cambia una entidad, la app se cae al abrir con *«Room cannot verify the data integrity…»*: se arregla con Clear storage (la base de datos está en la versión 1 y no hay migraciones).
- **La foto de P-M-08 no se borra** con Clear storage: vive en la galería del emulador (*Download*), no en la app.
- **Regla [Claude]: desde la S6, el bloque de pruebas de cada sesión empieza con instalación limpia** y recorre el montaje del apartado 2 desde el paso 1 hasta la última prueba de la sesión. El motivo está en el apartado 2.8: varias pruebas **crean** datos (P-M-05 crea Carnes, P-M-07 el número 12, P-M-11 los números 31–33) y no se pueden repetir encima de sí mismas sin chocar con «nombre repetido» o «número repetido». Lo que ya pasó en sesiones anteriores se hace en **modo rápido** (apartado 2.9: solo los toques que crean datos, sin mirar ni anotar).

## 2. El montaje completo desde instalación limpia

### 2.0 Cómo se lee

- Cada paso es **una P-M** (se ejecuta y se anota) o **[a mano]** (prepara datos; no se anota). Los pasos a mano que el plan pone entre paréntesis en su orden recomendado están en su sitio; los que añado yo llevan **[Claude]**.
- «→» separa toques. «Salir → 1234» es salir de Pedir con el PIN (si hay platos en el carrito, antes sale el aviso RF-38). **Atrás** es la flecha ← de la barra o el Atrás del sistema.
- Al final de cada paso: **dónde te quedas**, para que el siguiente empiece bien.
- [Claude, puesta al día del 6 oct, como quedó Pedir en la S8] «Plato → Añadir» es: tocar el plato en la carta → ficha 5b → **Añadir — importe** (vuelve a la carta). «Enviar» es: **pastilla del carrito** → 5c → *Enviar* → aviso «¿Enviar el pedido?» → **Enviar**; se vuelve a la carta con el carrito vacío y el aviso «Pedido enviado a la mesa N». Donde un paso dice solo «→ Enviar», van esos toques.

**Botones reales de los diálogos (P40):**

| El plan dice | Diálogo | Botón negativo | Botón afirmativo |
|---|---|---|---|
| «aceptar» (Enviar, P-M-20) | ¿Enviar el pedido? | Cancelar | **Enviar** |
| «aceptar el aviso» (P-M-21) | ¿Salir sin enviar? | Cancelar | **Salir** |
| «al aceptar» (P-M-07, paso 7) | ¿Salir sin guardar? | Cancelar | **Salir** |
| «confirmar» (P-M-06) | 2e ¿Eliminar la categoría? | Cancelar | **Eliminar** |
| «confirmar» (P-M-10) | 3d ¿Eliminar el plato? | Cancelar | **Eliminar** |
| «Rechazar / Aceptar» (P-M-11, aviso 1) | 3e Categoría eliminada | **No** | **Sí, mover** |
| «No / Sí» (P-M-11, aviso 2) | 3e ¿Quieres recuperar Postres? | **No** | **Recuperar** |
| «cancelar / aceptar» (P-M-24) | ¿Quitar la última línea? | Cancelar | **Quitar y anular** |
| «No / Sí» (P-M-25) | ¿Anular la comanda de la mesa 5? | Cancelar | **Anular** |
| «No / Sí» (P-M-28) | ¿Cobrar la mesa 4? | Cancelar | **Cobrar** |
| puerta de Pedir (P-M-14) | No se puede pedir | — | **Aceptar** |

### 2.1 Bloque A — Selector y PIN (pasos 1–4, ~10 min)

1. **P-M-01.** Abrir Ako → sale **1b** sin Atrás y con el aviso → **1234** y **1235** → Aceptar → *«Los PIN no coinciden»*, campos vacíos → **1234** y **1234** → Aceptar → **1a**. *Te quedas en 1a.*
2. **P-M-29** (necesita Bebidas activa). Propietario → 1234 → **Panel**: Bebidas con *1 · Agua · 1,50 €* y **Otros la última** → [+ Plato] de cualquier caja → **14 alérgenos** → Atrás → Terminar → **Cuenta**: 60 mesas blancas → Atrás → **Pedir** → entra en 1d (Agua es visible) → Atrás → 1a. ~~**Database Inspector** → tabla `etiqueta`: **3 filas**.~~ (P115: ese paso se quita; no hay nada que lo sustituya [Claude].) *Te quedas en 1a.*
3. **P-M-02.** Propietario → **9999** → Aceptar → *«PIN incorrecto»*, se vacía y se sacude, sigue abierto → **1234** → Aceptar → Panel. *Te quedas en el Panel.*
4. **P-M-03.** Cambiar PIN → actual **0000**, nuevo **5678** dos veces → Aceptar → *«PIN incorrecto»* en el actual (el nuevo no se guarda) → actual **1234**, nuevo **5678** dos veces → Aceptar → Terminar → Propietario → **1234** (falla) → **5678** → Panel → Cambiar PIN → 5678 / 1234 / 1234 → Aceptar → Terminar → Propietario → **1234** → Panel. *Te quedas en el Panel con el PIN 1234.*
   - Ojo: 1e tiene los **tres campos a la vez** (D18) y Aceptar está apagado hasta tener 4 + 4 + 4 cifras; por eso en el primer intento hay que teclear también el nuevo, aunque el plan diga solo «Actual 0000».

### 2.2 Bloque B — La carta del Propietario (pasos 5–10, ~20 min)

5. **P-M-05** (necesita Bebidas activa). [+] de arriba → hoja **sin interruptor** → **Carnes**, sin foto → Guardar → [+] → **Postres** → Guardar (cada una sale la última **antes de Otros**) → lápiz de Carnes → aparece el interruptor *En la carta* → **Carnes y aves** → Guardar → lápiz → **Carnes** → Guardar → [+] → **Carnes** → Guardar → *«Ya existe una categoría con ese nombre»*, no guarda (P124: el aviso no mira mayúsculas, así que «carnes» también avisaría; la prueba usa el nombre idéntico y no cambia) → cerrar la hoja → lápiz de **Otros** → **sin interruptor** → cerrar. **La carta (P224):** Terminar → Pedir → mesa 7 → **Bebidas** sale con el **círculo «?»** (Carnes y Postres no salen: todavía no tienen platos) → Salir → 1234 → 1a.
6. **[a mano · P8] Eliminar Bebidas.** Propietario → 1234 → **lápiz de Bebidas → apagar *En la carta* → Guardar**. Sin aviso: no hay ninguna comanda. En el Panel, Bebidas sigue **la primera**, marcada *Categoría eliminada*, con Agua atenuado. *Te quedas en el Panel.* Desde aquí y hasta el paso 7, Pedir no entra (no hay ningún plato visible): es lo esperado.
7. **P-M-07.** [+ Plato] de **Carnes** → la categoría viene elegida; Guardar apagado → **Entrecot**, **12**, **18,50** → Guardar → [+ Plato] de Carnes → **Pollo asado**, **12**, **11,00** → Guardar → *«Ese número ya lo tiene otro plato»* → número **14** → Guardar → tocar Entrecot → número **13** → aviso *«Si alguien tiene apuntado el 12…»* → Atrás → *«¿Salir sin guardar?»* → **Salir** → Entrecot sigue con el 12 → [+ Plato] de **Postres** (aún activa) → **Flan**, **30**, **4,50** → Guardar. *Te quedas en el Panel.*
   - **[Claude]** El precio **11,00 se teclea ya en el paso 4 del plan**: sin precio, Guardar no se enciende y el aviso de número repetido no se puede provocar (apartado 2.8, choque 2).
8. **P-M-09.** Tocar Entrecot → ALÉRGENOS: **Gluten** y **Lácteos** → Guardar → tocar Entrecot → siguen marcados → Atrás → Terminar → Pedir → mesa 7 → Entrecot → desplegar *Alérgenos*: **Gluten, Lácteos** → Atrás → Pollo asado → desplegar: *«El restaurante no ha indicado alérgenos…»* → Atrás → Salir → 1234 → 1a.
9. **[a mano · Claude] Descripción de Entrecot** (la pide P-M-18 y ningún paso la pone). Propietario → 1234 → Entrecot → Descripción **«Lomo de vaca a la plancha con patatas.»** → Guardar → Terminar → 1a.
10. **P-M-13.** Propietario → Cancelar → 1a. Cuenta → **6a sin PIN** → Atrás → 1a. Pedir → **1d** → Atrás → 1a sin PIN. *Te quedas en 1a.*

### 2.3 Bloque C — Eliminar, recuperar y la cadena 3e (pasos 11–18, ~30 min)

11. **[a mano] Comanda de la mesa 5.** Pedir → **mesa 5** → Flan → Añadir → pastilla del carrito → Enviar → **Enviar** → Salir → 1234 → 1a. (Mesa 5 roja con 4,50 €.)
12. **P-M-06.** Propietario → 1234 → lápiz de **Postres** → apagar *En la carta* → Guardar → aviso **2e** con **Flan (mesa 5)** → **Eliminar** → Postres *Categoría eliminada*, Flan atenuado → Terminar → Cuenta → mesa 5 → **6b conserva 1 × Flan** → Atrás → Atrás → Pedir → mesa 7 → **ni Postres ni Flan** → Salir → 1234 → 1a.
13. **P-M-11.** Propietario → 1234 → [+ Plato] de Postres → **Tarta**, **31**, **5,00** → Guardar → aviso 1 *«La categoría Postres está eliminada. ¿Muevo el plato a Otros?»* → **No** → aviso 2 *«¿Quieres recuperar Postres?»* con **N = 1** (Flan; *«Platos que volverán a la carta: 1»*, P164 B [Claude]) → **No** → [+ Plato] de Postres → **Helado**, **32**, **5,00** → Guardar → aviso 1 → **Sí, mover** (Helado queda en **Otros**) → [+ Plato] de Postres → **Natillas**, **33**, **4,00** → Guardar → aviso 1 → **No** → aviso 2 con **N = 2** (Flan y Tarta) → **Recuperar** → Terminar → Pedir → mesa 7 → Postres con **Flan, Tarta y Natillas**; Otros con **Helado** → Salir → 1234 → 1a.
14. **[a mano] Eliminar Postres otra vez** (lo pide el plan). Propietario → 1234 → lápiz de Postres → apagar → Guardar → aviso 2e con **Flan (mesa 5)** → **Eliminar** → Terminar → 1a. Postres queda eliminada con Flan, Tarta y Natillas.
15. **[a mano] Comanda de la mesa 6.** Pedir → **mesa 6** → Pollo asado → Añadir → Enviar → **Enviar** → Salir → 1234 → 1a.
16. **P-M-10.** Propietario → 1234 → tocar **Pollo asado** → apagar *En la carta* → Guardar → aviso **3d** *«Pollo asado está en una comanda pendiente de la mesa 6…»* → **Eliminar** → en el Panel, Pollo asado con ***Eliminado*** → Terminar → Pedir → mesa 7 → en Carnes **solo Entrecot** → Salir → 1234 → Cuenta → mesa 6 → **6b conserva 1 × Pollo asado 11,00** → Atrás → Atrás → 1a.
17. **P-M-04.** Propietario → 1234 → recorrer las cajas: **Bebidas** (la primera, *Categoría eliminada*, Agua atenuado, **P8**) · **Carnes** (12 Entrecot, 14 Pollo asado con *Eliminado*) · **Postres** (*Categoría eliminada*: 30 Flan, 31 Tarta y 33 Natillas atenuados) · **Otros la última** (32 Helado), sin flechas ni interruptor. Platos por número, «?» donde no hay foto (Entrecot tiene miniatura solo si ya se hizo P-M-08), [+ Plato] fijo abajo, todas las cajas de la misma altura con scroll propio. *Te quedas en el Panel.*
18. **P-M-14.** **[a mano, antes]** lápiz de **Carnes** → apagar → Guardar (sin aviso: Pollo asado ya está eliminado y 2e solo cuenta platos activos, P147; si aun así saliera, **Eliminar**; ver apartado 2.8, choque 8) → tocar **Helado** → apagar *En la carta* → Guardar (sin aviso) → Terminar. **Prueba:** Pedir → *«Todas las categorías están eliminadas»* → **Aceptar** → 1a → Propietario → 1234 → lápiz de Carnes → encender → Guardar → Terminar → Pedir → entra en 1d → Atrás. **[a mano, después]** Propietario → 1234 → Helado → encender *En la carta* → Guardar → Terminar → 1a.
   - **Con Bebidas eliminada no hace falta eliminar Agua** (P8): Agua sigue con `activo = true`, pero su categoría está eliminada, así que ya no es visible (R15). Es justo lo que pide la *Entrada* («la categoría de ejemplo sin platos visibles»).

### 2.4 Bloque D — Pedir y Cuenta (pasos 19–29, ~35 min)

19. **P-M-17.** Pedir → mesa 7 → fila de categorías: **Carnes** y **Otros** (la última), cada una con el círculo «?»; **ni Postres ni Bebidas** → tocar Otros → salta a su sección y se resalta → Carnes: **solo Entrecot**; Otros: **Helado**. *Te quedas en la carta de la mesa 7.*
20. **P-M-18.** Entrecot → **5b**: foto grande (o «?» hasta P-M-08), **12**, **Entrecot**, **18,50 €**, la descripción, *Alérgenos*: **Gluten, Lácteos**, cantidad **1**, **«Añadir — 18,50 €»** → Atrás → Helado → «?» y el aviso de alérgenos → Atrás. *Carta con el carrito vacío.*
21. **P-M-19.** Entrecot → + (2) → Añadir → **«Carrito · 2 · 37,00 €»** → Entrecot → Añadir → **«3 · 55,50 €»** (una sola línea de 3) → Helado → Añadir → **«4 · 60,50 €»** → pastilla → **5c**: − de Helado **apagado** → + de Entrecot hasta **99** (son 96 toques; el + se apaga) → Quitar las dos líneas → **0,00 €** y Enviar apagado → Atrás. *Carta de la mesa 7, carrito vacío.*
22. **P-M-20.** Salir → 1234 → Pedir → **mesa 4** → Entrecot × 2 → Añadir → Helado → Añadir → pastilla → Enviar → *«Mesa 4 · 42,00 €»* → **Enviar** → Salir → 1234 → Cuenta → mesa 4 **roja con 42,00 €** → 6b: 2 × Entrecot 37,00 · 1 × Helado 5,00 → Atrás → Atrás → Pedir → mesa 4 → Entrecot → Añadir → Enviar → **Enviar** → Salir → 1234 → Cuenta → mesa 4: **60,50 €** con la línea nueva **1 × Entrecot 18,50** → Atrás → Atrás → Propietario → 1234 → Entrecot → precio **20,00** → Guardar → Terminar → Cuenta → mesa 4: las líneas siguen a **18,50** → Atrás → Atrás. **[a mano, después]** Propietario → 1234 → Entrecot → precio **18,50** → Guardar → Terminar → 1a.
23. **P-M-21.** Pedir → mesa 7 → Helado → Añadir → **Salir** → *«¿Salir sin enviar? Hay 1 plato en el carrito sin enviar. Se perderá.»* → **Cancelar** (carrito intacto) → Salir → **Salir** → 1c → 1234 → 1a → Pedir → mesa 7 → carrito **vacío**. *Te quedas en la carta de la mesa 7.*
24. **P-M-16.** Salir → 1c → Cancelar (sigue la carta) → **Atrás del sistema** → el mismo 1c → **9999** → sacudida → **1234** → 1a.
25. **P-M-22.** Cuenta (sin PIN) → rejilla 4 × 15: mesa 4 **roja 60,50 €**, mesa 7 blanca (las 5 y 6 también están rojas a esta altura: es lo esperado) → mesa 4 → 6b → Atrás → mesa 7 → snackbar *«La mesa 7 no tiene comanda»*. *Te quedas en 6a.*
26. **P-M-23.** Mesa 4 → 6b *«Mesa 4»*: 2 × Entrecot 37,00 · 1 × Helado 5,00 · 1 × Entrecot 18,50; **TOTAL 60,50 €**; Anular, Dar la cuenta y Quitar en cada línea; sin *+ Añadir platos*. *Te quedas en 6b.*
27. **P-M-24.** Quitar Helado → **55,50** → Quitar 1 × Entrecot → **37,00** → Quitar la última → *«¿Quitar la última línea?»* → **Cancelar** → Quitar → **Quitar y anular** → vuelve a 6a, mesa 4 **blanca** → Atrás → Propietario → 1234 → Resumen de ingresos → vacío (*«No se cobró ninguna comanda ese día»*): la anulada no aparece → Atrás → Terminar → 1a.
28. **P-M-25.** Cuenta → **mesa 5** → 6b → Anular → *«¿Anular la comanda de la mesa 5? No se puede deshacer»* → **Cancelar** (sigue igual) → Anular → **Anular** → 6a, mesa 5 **blanca** → Atrás → Propietario → 1234 → Resumen de ingresos → sigue vacío → Atrás → Terminar → 1a.
29. **[a mano · Claude] Anular la comanda de la mesa 6.** Cuenta → mesa 6 → Anular → **Anular** → Atrás → 1a. Sin esto, P-M-15 no encuentra «resto sin comanda» (apartado 2.8, choque 1). Se anula y no se cobra: cobrarla metería 11,00 € en el Resumen de P-M-12.

### 2.5 Bloque E — Recibo, cobro y Resumen de ingresos (pasos 30–35, ~25 min)

30. **[a mano] Comanda de 42,00 € de la mesa 4** (lo pide el plan). Pedir → mesa 4 → Entrecot × 2 → Añadir → Helado → Añadir → Enviar → **Enviar** → Salir → 1234 → 1a. (Entrecot vuelve a costar 18,50 desde el paso 22.)
31. **P-M-15.** Pedir → rejilla de **60 mesas, 4 × 15**, con scroll y el total a 12 sp: mesa 4 **roja con 42,00 €**, mesa 7 blanca → mesa 4 → barra **«Mesa 4»** → Salir → 1234 → Pedir → mesa 7 → **«Mesa 7»** → Salir → 1234 → 1a.
32. **P-M-26.** **[a mano, antes]** Propietario → 1234 → Entrecot → nombre **«Entrecot de ternera»** → Guardar → Terminar. **Prueba:** Cuenta → mesa 4 → **Dar la cuenta** → 6c *«Mesa 4 · Recibo»*: **2 × Entrecot** 37,00 (el nombre congelado) · 1 × Helado 5,00 · **TOTAL 42,00 €**; *Entregado* vacío con *(opcional)*; Cobrar → Atrás → 6b. **[a mano, después]** Atrás → Atrás → Propietario → 1234 → Entrecot → nombre **«Entrecot»** → Guardar → Terminar → Cuenta → mesa 4 → Dar la cuenta. *Te quedas en 6c.*
33. **P-M-27.** Entregado **50** → Cambio **8,00 €** → **40** → **−2,00 €** → **42** → **0,00 €** → borrar el campo → sin cambio y **Cobrar activo**.
34. **P-M-28.** Cobrar → *«¿Cobrar la mesa 4?»* → **Cancelar** (sigue en 6c) → Cobrar → **Cobrar** → 6a, mesa 4 **blanca** → Atrás → Pedir → mesa 4 → Helado → Añadir → Enviar → **Enviar** (comanda nueva, R1) → Salir → 1234 → Propietario → 1234 → Resumen de ingresos → **mesa 4 · 42,00 €** → Atrás → Terminar. **[a mano, después]** (lo pide el plan) Cuenta → mesa 4 (5,00 €) → Dar la cuenta → Cobrar → **Cobrar** → Atrás → 1a.
35. **P-M-12.** Propietario → 1234 → **Resumen de ingresos** → viene con **hoy**: **mesa 4 · 42,00 €** y **mesa 4 · 5,00 €**, sin las anuladas (mesas 4, 5 y 6) → pie **2 comandas · 47,00 €** → tocar la de 42,00 → **recibo en solo lectura** (sin Cobrar, «Entrecot» congelado) → Atrás → fecha: **ayer** → lista vacía, *«No se cobró ninguna comanda ese día»*, 0,00 € → Atrás → Terminar. El Resumen no está en Cuenta (mirar 6a).
   - **Cuidado con la fecha:** los pasos 30–35 tienen que hacerse **el mismo día** (sin cruzar la medianoche), y el día anterior no puede haber cobros. Con instalación limpia al empezar, lo segundo se cumple siempre.

### 2.6 Bloque F — La foto (pasos 36–37, ~10 min)

36. **P-M-08.** Antes: una foto **> 2 MB y > 3000 px** en la galería del emulador (arrastrar el archivo a la ventana del emulador: queda en *Download*). Propietario → 1234 → Entrecot → **+ de la foto** → elegir → Guardar → **miniatura** en el Panel (Pollo asado sigue con «?») → Terminar → Pedir → mesa 7 → Entrecot → **foto grande** (placeholder neutro mientras carga) → Salir → 1234. **Device Explorer:** `files/fotos/` con un JPEG de lado mayor ≤ ~1080 px que pesa una fracción del original. **Database Inspector:** `producto.imagen` guarda solo la ruta.
37. **[a mano · Claude] Remate de P-M-04 y P-M-18** (solo en S12 y S13). Mirar el Panel (miniatura de Entrecot) y la ficha 5b de Entrecot (foto grande). Con esto, P-M-04 y P-M-18 se anotan enteras (apartado 2.8, choque 4).

### 2.7 Lo que cambia P8 (Bebidas eliminada) en tres pruebas

- **P-M-04:** el Panel enseña **Bebidas la primera** (orden 1), marcada *Categoría eliminada*, con **Agua atenuado**; el resto, igual. El orden de cajas es *Bebidas, Carnes, Postres, Otros*.
- **P-M-14:** no se elimina Agua: con Bebidas eliminada ya no es visible. En *Observaciones*: «Agua no se eliminó: Bebidas está eliminada desde el montaje (P8), así que no es visible».
- **P-M-17:** la carta **no** enseña Bebidas (R15); el resultado esperado no cambia.

**Qué se escribe en el plan de pruebas y cuándo** (esta noche no se edita): al ejecutarlas por primera vez, Claude añade al final de la *Entrada*:

- **P-M-04** (S6): «; **Bebidas eliminada** con 1 Agua (P8): sale la primera, marcada *Categoría eliminada*, con Agua atenuado».
- **P-M-17** (S8): «; **Bebidas eliminada** (P8): no aparece en la carta».

### 2.8 Choques entre pruebas y cómo se arreglan [Claude]

1. **La mesa 6 se queda abierta.** El orden del plan crea la comanda de la mesa 6 (antes de P-M-10) y nunca la cierra, así que P-M-15 no encuentra «resto sin comanda». **Arreglo:** paso 29, anularla (no cobrarla: rompería los 47,00 € de P-M-12).
2. **P-M-07, paso 4, no se puede hacer tal cual:** «nombre Pollo asado, número 12; Guardar» sin precio deja Guardar apagado (lo dice el mismo resultado esperado). **Arreglo:** teclear 11,00 ya en el paso 4.
3. **Nadie pone la descripción de Entrecot** y P-M-18 la espera. **Arreglo:** paso 9.
4. **P-M-04 y P-M-18 piden «Entrecot con foto»**, pero el orden deja P-M-08 la última. **Arreglo:** en S12 y S13, remate del paso 37 (P-M-08 ya mira la miniatura del Panel y la foto de 5b; se anotan las tres a la vez). La alternativa (pasar P-M-08 justo antes de P-M-04) la descartó Daniel: **P88 → A**, P-M-08 la última.
5. **P-M-04: Postres tiene más que Flan.** Después de P-M-11, Postres eliminada tiene **Flan, Tarta y Natillas**. No contradice el resultado esperado; se anota en *Observaciones*.
6. **Las pruebas que crean datos no se repiten encima de sí mismas** (P-M-05, 07, 11: nombre y número repetidos). **Arreglo:** instalación limpia al empezar las pruebas de cada sesión (apartado 1).
7. **«Ayer» en P-M-12** solo está vacío si el día anterior no hubo cobros. **Arreglo:** instalación limpia en la S10 y en la S13, y los pasos 30–35 el mismo día.
8. **Posible aviso 2e al eliminar Carnes** en la preparación de P-M-14: Carnes tiene Pollo asado (ya eliminado) en la comanda de la mesa 6. Depende de si la consulta de 2e cuenta los platos ya eliminados: Daniel decidió que **solo cuenta los activos** (P55 → A), y desde la revisión del 1 oct la consulta lo cumple: `ComandaDao.mesasConCategoriaPendiente` (la de `CartaRepository.mesasAfectadasPorCategoria`) filtra `p.activo = 1` (P147), así que **no sale** el aviso por Pollo asado. Si aun así saliera, no rompe nada: **Eliminar**, y se anota en la ficha (sería un fallo).
9. ~~**P-M-11, paso 1: «Volverán a la carta sus 1 platos.»** Con N = 1 la cadena `recuperar_categoria_cuerpo` queda mal en español. No rompe la prueba; Daniel decidió pasarla a `<plurals>` en la S7 (P63 → A).~~ **Resuelto en la S7 por P164 B** (5 oct, sustituye a P63 A) [Claude, puesta al día]: `recuperar_categoria_cuerpo` es una cadena normal con una frase neutra que vale para cualquier número, también el 0: «Platos que volverán a la carta: %1$d» (en P-M-11, «…: 1» con Tarta y «…: 2» con Natillas).

### 2.9 Modo rápido: solo los datos

Para las sesiones en que lo anterior ya pasó. Deja **el mismo estado que el paso 17** (después de P-M-04), en unos 15–20 min:

1. Clear storage → abrir → PIN **1234** dos veces.
2. Propietario → 1234 → [+] **Carnes** → [+] **Postres**.
3. Lápiz de **Bebidas** → apagar → Guardar.
4. [+ Plato] de Carnes: **Entrecot 12 · 18,50** (Gluten, Lácteos y la descripción del paso 9) y **Pollo asado 14 · 11,00**; [+ Plato] de Postres: **Flan 30 · 4,50**.
5. Pedir → mesa 5 → Flan → Enviar.
6. Eliminar **Postres** (2e → Eliminar).
7. [+ Plato] de Postres: **Tarta 31** (No, No), **Helado 32** (Sí, mover), **Natillas 33** (No, Recuperar).
8. Eliminar **Postres** otra vez (2e → Eliminar).
9. Pedir → mesa 6 → Pollo asado → Enviar.
10. Eliminar **Pollo asado** (3d → Eliminar).
11. *(Solo si hay que llegar al bloque D con P-M-20 ya pasada)* Pedir → mesa 4 → 2 × Entrecot + 1 × Helado → Enviar; Pedir → mesa 4 → 1 × Entrecot → Enviar. Deja el estado del final del paso 22.

P-M-14 no se repite en modo rápido: su efecto neto es cero (elimina y recupera Carnes y Helado).

## 3. Ejecución sesión por sesión (S5 → S13)

### 3.0 Reglas comunes

- **Cuándo:** el bloque de pruebas va al final de la sesión, con todas las piezas hechas, justo antes de `cerrar-sesion`. Empieza con **instalación limpia** (desde la S6, apartado 1).
- **Qué se anota en `docs/spec+doc-pruebas.md`** (lo hace Claude en `cerrar-sesion`, paso 3, con lo que Daniel confirme viendo las capturas; desde el 7 oct, P176, Claude escribe antes la lista de pruebas con sus pasos, Daniel la aprueba y Claude las pasa con `adb`): *Resultado* **Pasa**, **Parcial** o **Falla**; *Observaciones* con lo visto y, si es Parcial, **qué falta y en qué sesión se repite**; *Fecha* en formato dd/mm/aaaa. **Al repetirla [Claude]:** se sobrescriben *Resultado* y *Fecha*, y en *Observaciones* queda el historial en una línea («S6 02/10: Parcial, faltaba … · S8 09/10: entera»). Nunca se añade una fila nueva ni se renumera.
- **Qué se anota en `docs/estado-nivel.md`** (regla del brief): un RF pasa a `implementado, no probado` cuando su código existe pero su P-M queda Parcial, y a `implementado` cuando su P-M pasa entera. Se pone la sesión y se actualiza el recuento de arriba.
- **Las que ya pasaron** en sesiones anteriores no se vuelven a anotar hasta la S13: si hay que hacerlas para montar los datos, se hacen en modo rápido.

### S5 — Selector y PIN

- **Montaje:** instalación limpia → pasos **1, 2** (solo lo que existe), **3, 4, 10**.
- **Pruebas:** P-M-01, 02 y 03 **Pasa** (primera vez). P-M-13 **Parcial**: Propietario → 1c → Cancelar sí; Cuenta y Pedir enseñan la caja provisional (`pendiente_sesion_posterior`); se repite entera en la **S9**. P-M-29 **Parcial**: la app arranca con el PIN creado y ~~el inspector enseña 3 filas en `etiqueta`~~ (P115: paso quitado [Claude]); faltan el Panel (S6), el formulario (S7), 1d (S8) y Cuenta (S9); se repite entera en la **S9**.
- **A mano:** nada.
- **Anotar:** las cinco con fecha. **`estado-nivel.md`:** RF-01, RF-02, RF-03 → `implementado`; RF-24 → `implementado, no probado`. (RF-50 ya está `implementado, no probado` desde la S2.)
- **Hecho (1 oct 2026):** así quedaron las cinco en `spec+doc-pruebas.md`. P-M-02 y P-M-03 se pasaron desde el **Panel provisional** de la S5 (solo *← Atrás*, *Cambiar PIN* y *Terminar*); P-M-03 con el nuevo 5678 tecleado ya en el primer intento (D18, como dice el paso 4). La caja provisional de *Pedir* y *Cuenta* es un `ConfirmacionDialog` con un solo botón (*Aceptar*).
- **Tiempo:** ~15 min.

### S6 — Panel (platos y comandas con el Database Inspector)

En la S6 no existen el formulario del plato (S7) ni Pedir (S8): los platos y la comanda se meten a mano con el **Database Inspector** (spec 11: «con platos creados a mano en el inspector si hace falta»). Cómo abrirlo: `docs/guias/android-studio-basico.md`, apartado *Database Inspector*; para escribir SQL, el botón de **nueva consulta** (*Open New Query Tab*, comprobar el nombre en pantalla), **una sentencia cada vez** y *Run*.

- **Antes de escribir nada:** doble clic en las tablas `producto`, `comanda` y `linea_comanda` y **comprobar que las columnas se llaman como en el spec 5** (`categoria_id`, `precio_centimos`, `es_por_defecto`, `mesa_id`, `fecha_creacion`, `fecha_cierre`, `comanda_id`, `producto_id`, `precio_unitario_centimos`, `nombre_producto`). Si alguna se llama distinto en tu código, se cambia en la sentencia. El estado se guarda con el nombre del enum (`'PENDIENTE'`): conversor incorporado de Room, P113.
- **Montaje:** instalación limpia → paso **1** → paso **2** hasta el Panel (Bebidas con Agua y Otros la última) → paso **5** sin la carta → paso **6** (eliminar Bebidas).
- **A mano, 1 — Flan y la comanda de la mesa 5** (para P-M-06; Postres todavía activa). [Claude] Datos de prueba, no es código de la app:

```sql
-- [Claude] S6 · datos de prueba para P-M-06 (una sentencia cada vez; sin `disponible`, P115)
INSERT INTO producto (categoria_id, numero, nombre, precio_centimos, activo)
  VALUES ((SELECT id FROM categoria WHERE nombre = 'Postres'), 30, 'Flan', 450, 1);
INSERT INTO comanda (mesa_id, estado, fecha_creacion)
  VALUES ((SELECT id FROM mesa WHERE numero = 5), 'PENDIENTE', strftime('%s','now') * 1000);
INSERT INTO linea_comanda (comanda_id, producto_id, cantidad, precio_unitario_centimos, nombre_producto)
  VALUES ((SELECT MAX(id) FROM comanda), (SELECT id FROM producto WHERE numero = 30), 1, 450, 'Flan');
```

  Después: **Terminar → Propietario → 1234** para que el Panel vuelva a leer (desde P49 B el Panel se pone al día solo cuando la **app** cambia una tabla; lo escrito desde fuera, con el inspector o con `sqlite3`, puede no avisarle, y al volver a entrar se abre un Panel nuevo que lee la base de datos; si no sale Flan, cerrar la app y abrirla).
- **P-M-06** (paso 12, solo sus pasos 1–2): aviso 2e con Flan (mesa 5) → **Eliminar** → Postres eliminada con Flan atenuado. **[Claude]** Se mira además en el inspector lo que 6b enseñará en la S9: la línea de Flan sigue en `linea_comanda` (R5) y `producto.activo` de Flan sigue a 1. **Parcial**: faltan 6b (S9) y la carta (S8); se repite entera en la **S9**.
- **A mano, 2 — los platos de P-M-04:**

```sql
-- [Claude] S6 · datos de prueba para P-M-04 (sin `disponible`, P115)
INSERT INTO producto (categoria_id, numero, nombre, precio_centimos, activo) VALUES
  ((SELECT id FROM categoria WHERE nombre = 'Carnes'), 12, 'Entrecot', 1850, 1),
  ((SELECT id FROM categoria WHERE nombre = 'Carnes'), 14, 'Pollo asado', 1100, 0),
  ((SELECT id FROM categoria WHERE es_por_defecto = 1), 32, 'Helado', 500, 1);
```

- **P-M-04** (paso 17): Bebidas la primera y eliminada con Agua, Carnes (Entrecot y Pollo asado con *Eliminado*), Postres eliminada con Flan, Otros la última con Helado. Todos con «?». **Parcial**: platos metidos a mano en el inspector y Entrecot sin foto; se repite entera en la **S12**. Se añade la línea P8 a su *Entrada* (apartado 2.7).
- **P-M-05** (paso 5): **Parcial**: falta ver en la carta el círculo «?» de la categoría sin foto; se repite entera en la **S8**.
- **P-M-29:** si se quiere, una línea más en *Observaciones* («S6: Panel con Bebidas y Otros visto»); sigue Parcial.
- **`estado-nivel.md`:** RF-04, RF-05, RF-06 → `implementado, no probado`.
- **Tiempo:** ~40 min (la primera vez con el inspector cuesta).

### S7 — Plato sin foto

Ya se crean platos con el formulario; la comanda de la mesa 6 todavía va a mano, por el inspector o con `sqlite3` (Pedir llega en la S8) [Claude, puesta al día del 5 oct: en la S7 la metió Claude con `sqlite3`, con las mismas sentencias de abajo]. Los platos que la S6 metió a mano **chocarían** con P-M-07 (número 12 repetido): por eso se empieza con instalación limpia.

- **Montaje:** instalación limpia → paso **1** → paso **2** hasta el formulario ([+ Plato] → 14 alérgenos) → paso **5** en modo rápido → **6** → **7** (P-M-07) → **8** (P-M-09, solo pasos 1–2) → **9** → eliminar Postres (lápiz → apagar → Guardar; **sin aviso**, porque en la S7 no se crea la comanda de la mesa 5 [Claude]) → **13** (P-M-11, pasos 1–3) → **14** (sin aviso, por lo mismo).
- **A mano — la comanda de la mesa 6** (para P-M-10; Pollo asado ya existe):

```sql
-- [Claude] S7 · datos de prueba para P-M-10 (una sentencia cada vez)
INSERT INTO comanda (mesa_id, estado, fecha_creacion)
  VALUES ((SELECT id FROM mesa WHERE numero = 6), 'PENDIENTE', strftime('%s','now') * 1000);
INSERT INTO linea_comanda (comanda_id, producto_id, cantidad, precio_unitario_centimos, nombre_producto)
  VALUES ((SELECT MAX(id) FROM comanda), (SELECT id FROM producto WHERE numero = 14), 1, 1100, 'Pollo asado');
```

- → **16** (P-M-10, pasos 1–3) → **17** (mirar el Panel, sin anotar).
- **Pruebas:** P-M-07 **Pasa**. P-M-09 **Parcial** (faltan los pasos 3–4, la ficha 5b: S8). P-M-10 **Parcial** (comanda de la mesa 6 metida a mano —en la S7, con `sqlite3`—; faltan la carta, S8, y 6b, S9; [Claude] en la base de datos se comprueba que la línea sigue y que `activo` de Pollo asado es 0); se repite entera en la **S9**. P-M-11 **Parcial** (falta el paso 4, la carta: S8). P-M-29: una línea más en *Observaciones* si se quiere (formulario con 14 alérgenos).
- **`estado-nivel.md`:** RF-08 → `implementado`; RF-10, RF-11, RF-12 → `implementado, no probado`.
- **Tiempo:** ~50 min.

### S8 — Pedir (desde aquí, todo desde la app)

Ya no hace falta el inspector (ni `sqlite3`): las comandas se envían desde Pedir, también la de la mesa 6. Cuenta sigue siendo la caja provisional hasta la S9.

- **Montaje:** instalación limpia → pasos **1**, **2** (sin Cuenta), **5** (entera, con la carta), **6**, **7** (rápido), **8** (entera), **9**, **10** (Cuenta aún provisional), **11**, **12** (pasos 1–2 y 4), **13** (entera), **14**, **15**, **16** (pasos 1–4), **17** (mirar), **18**, **19**, **20**, **21**, **22** (pasos 1, 3 y 5), **23**, **24**.
- **P-M-15 en la S8 [Claude]:** se intercala **dentro del paso 22**, justo después del primer envío (mesa 4 con 42,00 €): Salir → 1234 → P-M-15 entera → y se sigue con el paso 3 de P-M-20. Las mesas 5 y 6 salen también rojas (sin Cuenta no se pueden cerrar): no afecta a lo que mira la prueba; va en *Observaciones*.
- **Primera vez:** P-M-14, 15, 16, 17, 19 y 21 **Pasa**. P-M-18 **Parcial** (falta la foto grande de Entrecot: S12). P-M-20 **Parcial** (los pasos 2, 4 y 6 miran Cuenta: S9; [Claude] mientras, el inspector enseña una sola comanda PENDIENTE de la mesa 4 con tres líneas a 1850, 500 y 1850). Se añade la línea P8 a la *Entrada* de P-M-17.
- **Repetidas enteras:** P-M-05, P-M-09 y P-M-11 → **Pasa**.
- **Siguen Parcial** (se puede anotar lo nuevo en *Observaciones*): P-M-13 (1d ya real; falta 6a), P-M-29 (1d ya real; falta Cuenta), P-M-06 y P-M-10 (la carta ya se ve; falta 6b).
- **`estado-nivel.md`:** RF-25, RF-26, RF-28, RF-29, RF-36, RF-38 → `implementado`; RF-30, RF-37 → `implementado, no probado`; RF-05, RF-10, RF-12 → `implementado`.
- **Tiempo:** ~75 min. Es la sesión más larga en pruebas: si se hace relevo, que sea antes del bloque de pruebas, no en medio.
- **Hecho (6 oct 2026)** [Claude, puesta al día del 6 oct]: la pasada la **ejecutó Claude** con `adb` a petición de Daniel. App desinstalada (se borraron los datos del emulador) e instalada; **todo desde la app y los envíos desde Pedir** (sin inspector ni `sqlite3`): PIN 1234 (con «Los PIN no coinciden» antes) → P-M-29 (pasos 1, 2 y 4) → P-M-05 → Bebidas eliminada (P8) → Entrecot 12, Pollo asado (12 → «Ese número ya lo tiene otro plato» → 14), Flan 30 → P-M-09 → descripción de Entrecot → P-M-13 (Cuenta aún provisional) → envío de Flan a la mesa 5 → P-M-06 (pasos 1–2 y 4) → P-M-11 → Postres eliminada otra vez → envío de Pollo asado a la mesa 6 → P-M-10 (pasos 1–4) → P-M-04 mirada → P-M-14 → P-M-17 → P-M-18 (todo menos la foto) → P-M-19 → P-M-20 (pasos 1, 3 y 5) con P-M-15 intercalada → P-M-21 → P-M-16. Resultado en `spec+doc-pruebas.md` (06/10/2026): **Pasa** P-M-05, 09, 11, 14, 15, 16, 17, 19 y 21; **Parcial** P-M-06 y P-M-10 (falta 6b, S9), P-M-13 y P-M-29 (falta Cuenta, S9), P-M-20 (pasos 2, 4 y 6, en 6b, S9), P-M-18 y P-M-04 (la foto, S12). En P-M-19, con 96 toques de `adb` muy seguidos se perdieron algunos; con toques más pausados el + llega a 99 y se apaga (tropiezo de la herramienta, no de la app). El emulador quedó con las mesas 4 (60,50 €), 5 y 6 rojas: la S9 empieza con instalación limpia igualmente.

### S9 — Cuenta

P-M-29 pide app **recién instalada**, y P-M-06, P-M-10 y P-M-20 necesitan sus comandas reales: instalación limpia y montaje entero hasta el paso 34 (sin el Resumen de ingresos, que llega en la S10: su botón sigue en la caja provisional).

- **Montaje:** instalación limpia → pasos **1–34** (pasos 3 y 4 se pueden saltar: no crean datos). Lo que ya pasó, en modo rápido pero haciendo sus toques.
- **Decidido (P83 → A, Daniel, 25 sep):** P-M-29 **en su sitio** (paso 2) con una sola instalación limpia, como dice este apartado (en la guía de la S9, hueco 9, es la opción B). La S10 empieza con instalación limpia igualmente.
- **Lo que recoge de la S8** [Claude, puesta al día del 6 oct]: P-M-06 (falta el paso 3, 6b de la mesa 5), P-M-10 (falta el paso 5, 6b de la mesa 6), P-M-13 (falta *Cuenta*, el paso 2), P-M-20 (faltan los pasos 2, 4 y 6, en Cuenta) y P-M-29 (falta *Cuenta*, el paso 3). Lo que ya pasó en la S8 (P-M-05, 09, 11, 14–17, 19 y 21) va en modo rápido, haciendo sus toques, y no se vuelve a anotar hasta la S13 (apartado 3.0).
- **Primera vez:** P-M-22, 23, 26, 27 **Pasa**. P-M-24 **Parcial** (falta el paso 5, Resumen de ingresos: S10). P-M-25 **Parcial** (falta el paso 3: S10). P-M-28 **Parcial** (falta el paso 5: S10).
- **Repetidas enteras:** P-M-13, P-M-29, P-M-06, P-M-10 y P-M-20 → **Pasa**.
- **Siguen Parcial:** P-M-04 y P-M-18 (la foto, S12).
- **Revisión:** el `revisor` revisa el código al cerrar (P35), después de las pruebas.
- **`estado-nivel.md`:** RF-40, RF-41, RF-43, RF-44 → `implementado`; RF-42, RF-45, RF-46 → `implementado, no probado`; RF-24, RF-50, RF-06, RF-11, RF-37 → `implementado`.
- **Tiempo:** ~90 min.

### S10 — Resumen de ingresos

Es el **ensayo general** de la pasada final (sin la foto). Instalación limpia por dos motivos: P-M-24, 25 y 28 necesitan sus comandas exactas, y el «ayer» de P-M-12 tiene que estar vacío (si la S9 fue ayer, sus cobros saldrían).

- **Montaje:** instalación limpia → modo rápido (apartado 2.9, puntos 1–11) → pasos **23–35** (del 23 al 26 sin anotar, en rápido; 27, 28, 34 y 35 con la prueba entera). Pasos 30–35 el mismo día.
- **Primera vez:** P-M-12 **Pasa**.
- **Repetidas enteras:** P-M-24, P-M-25 y P-M-28 → **Pasa**.
- **`estado-nivel.md`:** RF-20 → `implementado`; RF-42, RF-45, RF-46 → `implementado`.
- **Tiempo:** ~60 min.

### S11 — Pruebas de Room

- **Ninguna P-M.** Ojo: al terminar `connectedDebugAndroidTest`, Gradle suele **desinstalar** la app del emulador (comprobarlo en la sesión): los datos de prueba desaparecen. No pasa nada, porque la S12 empieza con instalación limpia.

### S12 — Fotos

- **Antes:** la foto grande en la galería (paso 36).
- **Montaje mínimo [Claude]** (~10 min; deja exactamente la *Entrada* de P-M-04): instalación limpia → PIN 1234 → [+] Carnes y Postres → eliminar Bebidas → Entrecot 12 · 18,50 (Gluten, Lácteos, descripción del paso 9), Pollo asado 14 · 11,00, Flan 30 · 4,50 en Postres → **[+ Plato] de Otros: Helado 32 · 5,00** (directo, sin la cadena 3e) → eliminar Postres → eliminar Pollo asado (ningún aviso: no hay comandas).
- **Pruebas:** paso **36** (P-M-08, primera vez: **Pasa**) → paso **17** (P-M-04 **entera**; con este montaje Postres solo tiene Flan, como en la *Entrada* del plan, y no Tarta ni Natillas) → paso **19** hasta la carta → paso **20** (P-M-18 **entera**). Hechos después del 36, los pasos 17 y 20 ya miran la foto: hacen de remate (paso 37).
- **[Claude] Fuera de las P-M:** ninguna prueba pone foto a una categoría (P13 la activa hoy). Se comprueba una vez a mano (lápiz de Carnes → + Elegir → foto → Guardar → Pedir: Carnes con su foto redonda) y se escribe en la ficha del diario, no en el plan.
- **`estado-nivel.md`:** RF-09 → `implementado`; RF-04, RF-30 → `implementado`.
- **Tiempo:** ~40 min.

### S13 — Cierre: la pasada final

- **Primero las once P-C** (`testDebugUnitTest` y `connectedDebugAndroidTest`): la segunda puede desinstalar la app, así que va **antes** de la pasada manual.
- **Después, la pasada final entera** del apartado 4: las 29, todas anotadas con la fecha de la S13 (*Observaciones*: «Pasada final S13» detrás del historial).
- **`estado-nivel.md`:** los 29 RF del nivel 1 en `implementado` con su sesión; recuento de arriba a 29.

## 4. La pasada final de la S13

Desde **instalación limpia**, los pasos **1 a 37** del apartado 2, en este orden de pruebas:

**01 → 29 → 02 → 03 → 05 → (eliminar Bebidas) → 07 → 09 → (descripción) → 13 → (mesa 5) → 06 → 11 → (eliminar Postres) → (mesa 6) → 10 → 04 → 14 → 17 → 18 → 19 → 20 → 21 → 16 → 22 → 23 → 24 → 25 → (anular mesa 6) → (mesa 4 · 42,00) → 15 → 26 → 27 → 28 → 12 → 08 → (remate de 04 y 18).**

Es el orden recomendado del plan con cuatro añadidos [Claude]: la descripción de Entrecot (paso 9), anular la mesa 6 (paso 29), el precio en el paso 4 de P-M-07 y el remate de la foto (paso 37). P-M-08 va la última, y P-M-04 y P-M-18 se anotan tras el remate.

**Cuánto se tarda (aprox.):** bloque A 10 min · B 20 · C 30 · D 35 · E 25 · F 10 → **unas 2 h 10 min de toques**, y **2 h 30 min – 3 h** con las notas de cada prueba. Las once P-C, unos 10 min más.

- **Se puede partir en dos** (por ejemplo, A–C un día y D–F otro): los datos se conservan si no se hace Clear storage ni se desinstala entre medias. Cortar siempre en 1a, nunca con un carrito a medias.
- **Si una prueba falla:** se anota *Falla* con lo que se vio y se sigue si el estado de los datos sigue valiendo para las siguientes. Si el arreglo obliga a cambiar el código, al terminar se repite esa prueba (y las que dependan de ella) en su punto del montaje, en modo rápido hasta llegar.
- **Quién la pasa (P176, 7 oct):** Claude escribe la lista de las 29 con sus pasos, Daniel la revisa y da el visto bueno, Claude las pasa con `adb` bloque a bloque y Daniel revisa las capturas y confirma *Pasa/Falla*; Claude lo pasa al plan en `cerrar-sesion`.

## 5. Tabla resumen

| P-M | RF | Primera ejecución | Estado entonces (qué falta) | Se repite entera en | Paso del montaje |
|---|---|---|---|---|---|
| 01 | RF-01 | S5 | Pasa | (S13) | 1 |
| 02 | RF-02 | S5 | Pasa | (S13) | 3 |
| 03 | RF-03 | S5 | Pasa | (S13) | 4 |
| 04 | RF-04 | S6 | Parcial (platos en el inspector; Entrecot sin foto; S7 y S8: mirada, Parcial solo por la foto [Claude, 6 oct]) | S12 | 17 (+ 37) |
| 05 | RF-05 | S6 | Parcial (círculo «?» en la carta) | S8 | 5 |
| 06 | RF-06 | S6 | Parcial (comanda de la mesa 5 en el inspector; sin 6b ni carta; S8: carta vista, falta 6b [Claude, 6 oct]) | S9 | 12 |
| 07 | RF-08 | S7 | Pasa | (S13) | 7 |
| 08 | RF-09 | S12 | Pasa | (S13) | 36 |
| 09 | RF-10 | S7 | Parcial (pasos 3–4, ficha 5b) | S8 | 8 |
| 10 | RF-11 | S7 | Parcial (comanda de la mesa 6 a mano, con `sqlite3`; carta S8, 6b S9; S8: carta vista, falta 6b [Claude, 6 oct]) | S9 | 16 |
| 11 | RF-12 | S7 | Parcial (paso 4, la carta) | S8 | 13 |
| 12 | RF-20 | S10 | Pasa | (S13) | 35 |
| 13 | RF-24 | S5 | Parcial (1d S8, 6a S9; S8: 1d vista, falta 6a [Claude, 6 oct]) | S9 | 10 |
| 14 | RF-25 | S8 | Pasa | (S13) | 18 |
| 15 | RF-26 | S8 | Pasa (mesas 5 y 6 rojas, en *Observaciones*) | (S13) | 31 (S8: dentro del 22) |
| 16 | RF-28 | S8 | Pasa | (S13) | 24 |
| 17 | RF-29 | S8 | Pasa | (S13) | 19 |
| 18 | RF-30 | S8 | Parcial (foto grande de Entrecot) | S12 | 20 (+ 37) |
| 19 | RF-36 | S8 | Pasa | (S13) | 21 |
| 20 | RF-37 | S8 | Parcial (pasos 2, 4 y 6 miran Cuenta) | S9 | 22 |
| 21 | RF-38 | S8 | Pasa | (S13) | 23 |
| 22 | RF-40 | S9 | Pasa | (S13) | 25 |
| 23 | RF-41 | S9 | Pasa | (S13) | 26 |
| 24 | RF-46 | S9 | Parcial (paso 5, Resumen de ingresos) | S10 | 27 |
| 25 | RF-42 | S9 | Parcial (paso 3, Resumen de ingresos) | S10 | 28 |
| 26 | RF-43 | S9 | Pasa | (S13) | 32 |
| 27 | RF-44 | S9 | Pasa | (S13) | 33 |
| 28 | RF-45 | S9 | Parcial (paso 5, Resumen de ingresos) | S10 | 34 |
| 29 | RF-50 | S5 | Parcial (Panel S6, formulario S7, 1d S8, Cuenta S9; S8: pasos 1, 2 y 4 vistos, falta Cuenta [Claude, 6 oct]) | S9 | 2 |

**En la S13 pasan las 29**, en el orden del apartado 4, desde instalación limpia.
