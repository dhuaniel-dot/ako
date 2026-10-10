> **Borrador del diagrama de clases del prototipo (nivel 1). Es parte del diseño: manda junto con el spec y las fichas. Alimenta el apartado 5 (clases y métodos clave) y el apartado 7 (diagrama de clases) de la memoria. Los archivos (PNG, SVG y `.puml` por figura) viven en el PC, en `Proyecto intermodular\Imágenes\Diagramas\` (ruta de la reorganización del 17-18 sep, P164).**
> **Actualizado en la fase 5b (24 sep 2026):** **P238 → A**: `ResumenDiaActivity`, `ResumenDiaViewModel` y la data class `ResumenDia` pasan a **`ResumenIngresosActivity`, `ResumenIngresosViewModel` y `ResumenIngresos`** (vocabulario de P149), cambiado en el texto y en los `.puml` de abajo; **las figuras PNG no se redibujan** (el diagrama definitivo sale del código en la fase 7). **P237** (repositorios en `datos/repositorios/`, `Hash` en `dominio/`) y **P243** (qué comprueba `Validacion`), en las tablas de los apartados 1 y 3.3. Ruta del PC corregida (P164).
> Creado el **17 de septiembre de 2026**, fase 2d, bloque 7 (P104-P112). Motivos en la entrada de la fase 2d de `diario+doc-decisiones.md`.
> **Actualizado el 17 de septiembre de 2026, en el cierre de la fase 2d.** Cuatro cambios, todos anotados sin redibujar las figuras: **el paquete raíz es `yunkang.ako`** (P138); **nacen tres clases de dominio puro —`Calculadora`, `Validacion` y `Hash`— en un paquete `dominio/` separado de `datos/`**, y `cambio()` y `hash()` **dejan de vivir** en `CuentaViewModel` y en `PinStore` (bloque 10); **`PinStore` lleva el algoritmo del PIN fijado** (P126); y **el aviso de platos sin enviar (RF-38) usa `ConfirmacionDialog`** y comparte caja con el de cobrar (P121). **Las cinco figuras siguen siendo las del bloque 7 y no muestran las tres clases nuevas: el diagrama definitivo de la fase 7, generado desde el código, sí las recoge.** Se retira el apartado *Pendiente de volcar*, ya consumido.
> **Actualizado el 10 de octubre de 2026 (parte 3 de la S13.5):** nombres nuevos puestos por Daniel: `Validacion` → `Comprobacion`, `LineaCarrito` → `PlatoApuntado`, `Hash` → `PinSalHash`, `PinStore` → `GuardaPin` e `ImageStore` → `Galeria`. El texto ya los usa; las figuras PlantUML siguen con los nombres del bloque 7 hasta que se rehagan desde el código en la fase 7.
> **Es un borrador.** Los nombres de clases y métodos los propuso Claude a partir de las fichas y las reglas R1-R16; **el diagrama definitivo se genera en la fase 7 desde el código real** (spec, apartado 12), y ahí cambiarán nombres. Lo que este documento fija es la **estructura**: capas, número de clases y quién habla con quién.

# Diagrama de clases — borrador, nivel 1

## 1. Qué es y qué se decidió

Un **diagrama de clases** es el plano de cómo se organiza el **código**: qué clases hay, qué atributos (con tipo) y qué métodos tiene cada una, y cómo se relacionan. Se diferencia del E-R en que el E-R describe **datos guardados** (tablas y claves) y no tiene comportamiento; el de clases describe **código** y por eso puede enseñar lo que no se guarda: el **carrito** (R10), el **PIN** (SharedPreferences, no Room), la **calculadora de cambio** y la lógica de las reglas de negocio.

| Decisión | Qué se fijó | P |
|---|---|---|
| Alcance | **Solo nivel 1** (spec, apartado 16), con el nivel anotado. Las 14 entidades entran todas porque las 14 tablas se crean desde el primer día; las que el nivel 1 no usa llevan la marca *«sin uso en nivel 1»*. Cada incremento de la 6b obliga a redibujar la figura afectada, como con los wireframes. **La excepción ya está escrita en el spec, apartado 16.5** (cierre de la 2d) | P104 |
| Notación | **PlantUML** (la misma herramienta que los casos de uso, P77); el E-R sigue en Mermaid. Ninguno de los documentos oficiales (guion, plantilla, normativa) fija herramienta; se eligió por la regla de Daniel *"lo que no vaya al documento se hace de la mejor forma para Claude Code"* y porque la letra impresa sale mayor que con Mermaid en las cuatro figuras | P105 |
| DAOs | **Uno por agregado**: `CategoriaDao`, `ProductoDao` (con alérgenos; en nivel 2, nutrición, modificadores, etiquetas y traducciones), `PrecargadosDao`, `MesaDao`, `ComandaDao` (con líneas) | P106 |
| Repositorios | **Tres**: `CartaRepository`, `ComandaRepository`, `SeguridadRepository`. Cada uno garantiza las reglas de su parte; el PIN no es Room | P107 |
| Pantallas y ViewModels | **Una Activity por pantalla de las fichas** (1, 2, 3, 2g, 5, 6); las vistas 5a/5b/5c y 6a/6b/6c son Fragments dentro de su Activity; **un ViewModel por Activity**. El carrito vive en `PedidoViewModel` y lo comparten 5a, 5b y 5c | P108 |
| Clases fuera de Room | Entran `GuardaPin`, `Galeria`, `Precarga`, `Carrito` y `PlatoApuntado`, como auxiliares | P109 |
| Atributos | Las entidades llevan sus campos **con tipos Kotlin y nulabilidad** (`imagen: String?`) | P111 |
| Figuras | **Vista general** (solo nombres, por capas) en el cuerpo de la memoria; **cuatro figuras detalladas** (entidades · base de datos, DAOs y repositorios · ViewModels y auxiliares · pantallas y adaptadores) en un anexo, una por página. Sustituye a P110 (dos figuras), que medidas salían a 3,6-3,9 pt | P112 |
| **Paquete raíz** | **`yunkang.ako`** (cierre de la 2d) | P138 |
| **Dominio puro** | **Paquete `dominio/`, separado de `datos/`**, con `Carrito`, `PlatoApuntado` y **tres clases nuevas: `Calculadora`, `Comprobacion` y `PinSalHash`** (bloque 10). **P237 → B (5b, 24 sep, delegada a Claude [Claude]): `PinSalHash` vive en `dominio/` (es puro: solo `javax.crypto`) y los repositorios viven en `datos/repositorios/`, no en `dominio/`, porque usan Room; `seguridad/` se queda solo con `GuardaPin`.** Así es verdad que todo `dominio/` se prueba sin emulador | P237 |

**Regla de Daniel fijada en este bloque (P112):** *lo que no vaya al documento ni a la presentación se hace de la mejor forma posible para Claude Code; no se recorta.*

**Interfaz:** vistas XML (`ConstraintLayout`, `dp`/`sp`, Glide), no Jetpack Compose — ya decidido en el spec, apartado 6 y apartado 15.

## 2. Inventario — 5 figuras, 58 clases

| Archivo | Figura | Página | Letra impresa (aprox.) |
|---|---|---|---|
| `clases-vista-general` | Vista general por capas: 30 cajas, solo nombres | Apaisada, cuerpo de la memoria | ~9 pt |
| `clases-entidades` | Figura 1 de 4: 14 entidades Room + 2 enumeraciones, con atributos | Apaisada, anexo | ~5,6 pt |
| `clases-acceso` | Figura 2 de 4: `AppDatabase`, `Precarga`, 5 DAOs, 3 repositorios, `PinStore`, 2 data class | Vertical, anexo | ~4,7 pt |
| `clases-viewmodels` | Figura 3 de 4: 6 ViewModels, `Carrito`, `LineaCarrito`, `ImageStore` | Vertical, anexo | ~5,7 pt |
| `clases-pantallas` | Figura 4 de 4: 6 Activities, 8 Fragments, 5 diálogos/hojas, 4 adaptadores | Vertical, anexo | ~5,1 pt |

Cada figura existe en `.png` (200 dpi), `.svg` y `.puml` (código fuente PlantUML, para regenerar).

> **Las 58 clases son las del bloque 7.** Las tres de dominio puro del bloque 10 (`Calculadora`, `Comprobacion`, `PinSalHash`) **no están dibujadas**: se decidieron después y redibujar dos figuras exige Java y Graphviz en el PC. **Están descritas en el apartado 3.3 y en `spec-claude-code.md`, apartado 3**, y **el diagrama definitivo de la fase 7 —que sale del código— las recogerá**. Contando las tres, la estructura del prototipo son **61 clases**.

## 3. Capas y clases

```mermaid
flowchart TB
    P["Pantallas — Activity / Fragment / Dialog<br/>pintan y recogen toques"] --> VM["ViewModels — uno por Activity<br/>estado de la pantalla, carrito, R4"]
    VM --> R["Repositorios — Carta, Comanda, Seguridad<br/>reglas de negocio R1-R16"]
    R --> D["DAOs — uno por agregado<br/>la única capa que sabe SQL"]
    D --> E["Entidades Room — las 14 tablas"]
    R --> X["Auxiliares — GuardaPin, Galeria, Precarga"]
    VM --> DOM["Dominio puro — Carrito, Calculadora,<br/>Comprobacion, PinSalHash: sin Android, se prueban solas"]
    R --> DOM
```

Cada capa habla solo con la de abajo. Las pantallas no ven los repositorios; los ViewModels no ven los DAOs. **El dominio puro no ve a nadie**: son funciones que reciben datos y devuelven datos, y por eso las seis pruebas de `test/` corren sin emulador.

### 3.1 Entidades Room (figura 1)

Las 14 tablas del spec, apartado 5, una clase `@Entity` por tabla, con los campos en tipos Kotlin. Convenciones: `Long` para claves, `Int` para céntimos, cantidades y números de plato/mesa, `Long` (epoch millis) para fechas, `?` donde el E-R marca nulable (P69). Dos enumeraciones: `EstadoComanda` (PENDIENTE / PAGADA / ANULADA) y `TipoModificador` (AÑADIR / QUITAR).

| Marca en la figura | Entidades | Por qué |
|---|---|---|
| `«Entity»` | Categoria, Producto, Alergeno, ProductoAlergeno, Mesa, Comanda, LineaComanda | Las usa el nivel 1 |
| `«Entity · solo precarga en nivel 1»` | Etiqueta | Se precargan las 3; los chips son el incremento 1 |
| `«Entity · sin uso en nivel 1»` | ProductoNutricion, Modificador, LineaModificador, ProductoEtiqueta, Idioma, ProductoTraduccion | La tabla existe desde el primer día (spec, apartado 16, transversal); ningún código del nivel 1 la lee ni la escribe |

Relaciones dibujadas: las claves foráneas del E-R, con `*-->` (composición) en `Comanda → LineaComanda` y `LineaComanda → LineaModificador` (la única CASCADE, R11), y con línea discontinua las dos referencias "congeladas" (`LineaComanda → Producto`, `LineaModificador → Modificador`, R14).

> **Ninguna entidad declara `CHECK`.** Los `>= 0` del spec, apartado 5 son restricción del dominio y **los garantiza `Comprobacion`**, llamada desde los repositorios (R8, verificación 9). Room no genera `CHECK` ni índices parciales.

### 3.2 Base de datos, DAOs y repositorios (figura 2)

| Clase | Tipo | Responsabilidad | Reglas que garantiza |
|---|---|---|---|
| `AppDatabase` | `RoomDatabase` | Declara las 14 entidades y expone los 5 DAOs | — |
| `Precarga` | `RoomDatabase.Callback` | En `onCreate`: categoría *Otros* (`esPorDefecto = true`), 60 mesas, 14 alérgenos, 3 etiquetas, 1 categoría y 1 plato de ejemplo (spec, apartado 11) | R16 (la categoría por defecto existe antes que cualquier plato) |
| `CategoriaDao` | `@Dao` | insertar, actualizar, todas, porDefecto, existeNombre | — |
| `ProductoDao` | `@Dao` | insertar, actualizar, porId, porCategoria, **visibles** (JOIN con categoría activa), hayAlgunoVisible, **existeNumero**, alergenosDe, guardarAlergenos (transacción) | R15 en la consulta; R9 en existeNumero |
| `PrecargadosDao` | `@Dao` | alergenos, etiquetas (lectura) e inserción para la precarga | — |
| `MesaDao` | `@Dao` | todas, insertarTodas | — |
| `ComandaDao` | `@Dao` | insertar, actualizar, porId, **pendienteDeMesa**, pendientesConTotal, lineasDe, insertarLineas, borrarLinea, contarLineas, **pagadasEntre**, mesasConProductoPendiente, mesasConCategoriaPendiente | Consultas de R3, R6 y del Resumen de ingresos |
| `CartaRepository` | clase | categorias, guardarCategoria, eliminarCategoria → mesas afectadas, platosDe, platosVisibles, hayPlatoVisible, plato, guardarPlato (con alérgenos), eliminarPlato → mesas afectadas, alergenos, alergenosDe | **R6, R8** (llamando a `Comprobacion`), **R9, R15, R16** |
| `ComandaRepository` | clase | mesasConEstado, comandaPendiente, **enviarCarrito** (crea o amplía), lineasDe, totalDe, **quitarLinea** (devuelve si la comanda quedó anulada), anular, cobrar (fechaCierre), resumenDelDia | **R1, R2, R3, R7, R8** (al congelar el precio), **R10, R14** |
| `SeguridadRepository` | clase | hayPin, crearPin, comprobarPin, cambiarPin | — |
| `GuardaPin` | SharedPreferences | generarSal (`SecureRandom`, 16 bytes), guardar, leer, existe. **Nunca guarda el PIN**, solo sal + hash (spec, apartado 9). **El cálculo del hash ya no vive aquí: lo hace `PinSalHash`** (bloque 10) | — |
| `MesaEstado` | data class | mesa + comandaId? + totalCentimos? — una fila de la rejilla (R3: la ocupación se calcula) | R3 |
| `ResumenIngresos` | data class | dia, comandas con total, numComandas, totalCentimos | R10 |

`ResultadoGuardado` (no dibujado, es un tipo de retorno) dice si se guardó o por qué no: número repetido (R9), nombre repetido, **precio negativo (R8)**, categoría eliminada (dispara la cadena 3e en la pantalla).

### 3.3 ViewModels, dominio puro y auxiliares (figura 3)

| ViewModel | Activity | Estado que guarda | Métodos | Usa |
|---|---|---|---|---|
| `SelectorViewModel` | 1 | hayPin, mesas (para 1d) | crearPin, comprobarPin, hayPlatoVisible (puerta de Pedir) | Seguridad, Carta, Comanda |
| `PanelViewModel` | 2 | categoriasConPlatos | guardarCategoria, eliminarCategoria, cambiarPin (1e) | Carta, Seguridad |
| `PlatoViewModel` | 3 | plato, alergenos, categorias | cargar, elegirFoto, guardar, eliminar | Carta, Galeria |
| `ResumenIngresosViewModel` | 2g | dia, resumen | elegirDia, lineasDe (recibo en solo lectura) | Comanda |
| `PedidoViewModel` | 5 | mesaId, categorias, platosVisibles, **carrito** | anadir, cambiarCantidad, quitar, **enviar** (R2, R4), comprobarPin (salir de Pedir, P41) | Carta, Comanda, Seguridad, Carrito |
| `CuentaViewModel` | 6 | mesas, comanda, lineas, total | abrirMesa, quitarLinea (R7), anular, cobrar, **cambio → llama a `Calculadora`** | Comanda, Calculadora |

**Dominio puro (paquete `dominio/`)** — ninguna de estas clases importa nada de Android, y ahí está su razón de ser: **son las seis pruebas de `test/`**.

| Clase | Qué hace | Prueba |
|---|---|---|
| `Carrito` | mesaId, lineas; `anadir` suma en la misma línea si el plato ya está; `cambiarCantidad`, `quitar`, `numPlatos`, `total`, `estaVacio` — **R4 y R10** | P-C-02, P-C-03 |
| `PlatoApuntado` | producto, cantidad, `importe()` — **R10** | P-C-01 |
| **`Calculadora`** | `cambio(totalCentimos, entregadoCentimos)` = entregado − total; puede salir negativo y no lanza nada. **No guarda nada** (spec, apartado 10) | P-C-04 |
| **`Comprobacion`** | `precioValido(centimos)`: **lanza excepción si es negativo** (**R8**). La llaman los tres repositorios antes de guardar un plato, un modificador o una línea. **También `cantidadValida(n)` (1-99, R4) y `pinValido(pin)` (cuatro cifras)** — P243 → A (5b): las tres comprobaciones que ya listaba `spec-claude-code.md`, apartado 3 | **P-C-09** (y P-C-03 por la cantidad) |
| **`PinSalHash`** | `pbkdf2(pin, sal)` y `coincide(pin, sal, hash)`: **`PBKDF2withHmacSHA256`, 100 000 iteraciones, clave de 256 bits** (P126). Solo calcula; **guardar es cosa de `GuardaPin`** | P-C-05 |

> **Por qué nacieron las tres** (bloque 10). En el borrador, `cambio()` vivía en `CuentaViewModel` y `hash()` dentro de `GuardaPin`. Probar un ViewModel arrastra `LiveData` y exige `InstantTaskExecutorRule`; probar `GuardaPin` arrastra SharedPreferences, que no existe fuera de Android. **Separar *calcular* de *guardar* y de *pintar*** deja seis pruebas que corren en el PC en segundos, y de paso da un sitio donde poner `Comprobacion`, que antes no existía porque se daba por hecho que R8 la imponía la base de datos.

**Otro auxiliar:** `Galeria` (guardar = redimensionar a ~1080 px + JPEG + archivo privado, devuelve la ruta; borrar — spec, apartado 15).

### 3.4 Pantallas y adaptadores (figura 4)

| Activity | Ficha | Contiene | Diálogos y hojas |
|---|---|---|---|
| `SelectorActivity` | 1 | `SelectorFragment` (1a), `CrearPinFragment` (1b), `RejillaMesasFragment` en modo *elegir* (1d) | `PinDialog` (1c) |
| `PanelActivity` | 2 | (vista única, 2a) | `CategoriaBottomSheet` (2b), `CambiarPinDialog` (1e), `MesasAfectadasDialog` (2e) |
| `PlatoActivity` | 3 | (vista única, 3a) | `MesasAfectadasDialog` (3d), `ConfirmacionDialog` (cadena 3e) |
| `ResumenIngresosActivity` | 2g | `ReciboFragment` con `soloLectura = true` | — |
| `PedidoActivity` | 5 | `CartaFragment` (5a), `FichaPlatoFragment` (5b), `CarritoFragment` (5c) | `PinDialog` (Salir), **`ConfirmacionDialog` (Enviar, y el aviso de platos sin enviar de RF-38)** |
| `CuentaActivity` | 6 | `RejillaMesasFragment` en modo *gestionar* (6a), `ComandaFragment` (6b), `ReciboFragment` (6c) | `ConfirmacionDialog` (Anular, quitar la última línea R7, Cobrar) |

**Componentes reutilizados** (es el argumento *"se reutiliza el componente, no la pantalla"* del spec, apartado 6): `RejillaMesasFragment` (un componente, dos modos: 1d y 6a), `ReciboFragment` (6c y el recibo en solo lectura de 2g), `ConfirmacionDialog` (una caja para todas las confirmaciones, como en los wireframes), `FilaPlatoAdapter` (fila de plato en 2a y 5a), `CategoriaAdapter` (cajas en 2a, fila horizontal en 5a), `MesaAdapter` (1d y 6a), `LineaAdapter` (5c, 6b, 6c y 2g).

> **El aviso de platos sin enviar (RF-38) no es una clase nueva** (P121, bloque 8): es `ConfirmacionDialog` con otro texto, y en los wireframes **comparte caja con `dialogo-cobrar`**. Se anota aquí porque al contar clases es fácil inventarse un `AvisoSalirDialog` que no existe.

## 4. Código fuente de las figuras (PlantUML)

Los cinco `.puml` están en `Imágenes\Diagramas\`. Se regeneran con `java -jar plantuml.jar -tpng -Sdpi=200 clases-*.puml` (necesita Graphviz). Se reproducen aquí para que ningún documento del Project dependa de un archivo del PC.

> **Son los del bloque 7.** No incluyen `Calculadora`, `Comprobacion` ni `PinSalHash`, ni el paquete `yunkang.ako`. **No se regeneran ahora**: el diagrama que va a la memoria en el apartado 7 se genera en la fase 7 desde el código real, y será el que las recoja.

### clases-vista-general.puml

```plantuml
@startuml clases-vista-general
title Diagrama de clases (borrador, nivel 1) — vista general por capas
skinparam shadowing false
skinparam packageStyle rectangle
skinparam nodesep 12
skinparam ranksep 30
skinparam class {
  BackgroundColor white
  BorderColor #444
  ArrowColor #444
  FontSize 12
}
skinparam package {
  FontSize 13
}
hide members
hide circle

package "Pantallas" {
  class SelectorActivity
  class PanelActivity
  class PlatoActivity
  class ResumenIngresosActivity
  class PedidoActivity
  class CuentaActivity
}
package "ViewModels" {
  class SelectorViewModel
  class PanelViewModel
  class PlatoViewModel
  class ResumenIngresosViewModel
  class PedidoViewModel
  class CuentaViewModel
}
package "Auxiliares" {
  class Carrito
  class ImageStore
  class PinStore
  class Precarga
}
package "Repositorios" {
  class CartaRepository
  class ComandaRepository
  class SeguridadRepository
}
package "DAOs" {
  class CategoriaDao
  class ProductoDao
  class PrecargadosDao
  class MesaDao
  class ComandaDao
}
package "Entidades Room (14 tablas)" {
  class Categoria
  class Producto
  class "ProductoNutricion,\nModificador,\nProductoAlergeno, Alergeno,\nProductoEtiqueta, Etiqueta,\nProductoTraduccion, Idioma" as Carta_resto
  class Mesa
  class Comanda
  class "LineaComanda,\nLineaModificador" as Comanda_lineas
}
SelectorActivity --> SelectorViewModel
PanelActivity --> PanelViewModel
PlatoActivity --> PlatoViewModel
ResumenIngresosActivity --> ResumenIngresosViewModel
PedidoActivity --> PedidoViewModel
CuentaActivity --> CuentaViewModel
SelectorViewModel --> SeguridadRepository
SelectorViewModel --> CartaRepository
PanelViewModel --> CartaRepository
PanelViewModel --> SeguridadRepository
PlatoViewModel --> CartaRepository
PlatoViewModel --> ImageStore
ResumenIngresosViewModel --> ComandaRepository
PedidoViewModel --> CartaRepository
PedidoViewModel --> ComandaRepository
PedidoViewModel --> Carrito
CuentaViewModel --> ComandaRepository
SeguridadRepository --> PinStore
CartaRepository --> CategoriaDao
CartaRepository --> ProductoDao
CartaRepository --> PrecargadosDao
ComandaRepository --> MesaDao
ComandaRepository --> ComandaDao
CategoriaDao ..> Categoria
ProductoDao ..> Producto
ProductoDao ..> Carta_resto
PrecargadosDao ..> Carta_resto
MesaDao ..> Mesa
ComandaDao ..> Comanda
ComandaDao ..> Comanda_lineas
Precarga ..> Categoria
Precarga ..> Mesa
@enduml
```

### clases-entidades.puml

```plantuml
@startuml clases-entidades
title Diagrama de clases (borrador, nivel 1) — figura 1 de 4: entidades Room (las 14 tablas)
skinparam classAttributeIconSize 0
skinparam shadowing false
skinparam nodesep 25
skinparam ranksep 40
skinparam packageStyle rectangle
skinparam class {
  BackgroundColor white
  BorderColor #444
  ArrowColor #444
  FontSize 12
  AttributeFontSize 11
}
skinparam note {
  BackgroundColor #FFF8DC
  BorderColor #999
  FontSize 10
}
hide empty members

package "Entidades Room — las 14 tablas del E-R" {
  class Categoria <<Entity>> {
    id: Long
    nombre: String
    imagen: String?
    orden: Int
    activo: Boolean
    esPorDefecto: Boolean
  }
  class Producto <<Entity>> {
    id: Long
    categoriaId: Long
    numero: Int
    nombre: String
    descripcion: String?
    precioCentimos: Int
    imagen: String?
    imagenNutricional: String?
    activo: Boolean
  }
  class ProductoNutricion <<Entity · sin uso en nivel 1>> {
    productoId: Long
    energiaKj: Int?
    grasasMg: Int?
    grasasSaturadasMg: Int?
    hidratosMg: Int?
    azucaresMg: Int?
    proteinasMg: Int?
    salMg: Int?
  }
  class Modificador <<Entity · sin uso en nivel 1>> {
    id: Long
    productoId: Long
    nombre: String
    precioCentimos: Int
    tipo: TipoModificador
    activo: Boolean
  }
  enum TipoModificador {
    AÑADIR
    QUITAR
  }
  class Alergeno <<Entity>> {
    id: Long
    nombre: String
  }
  class ProductoAlergeno <<Entity>> {
    productoId: Long
    alergenoId: Long
  }
  class Etiqueta <<Entity · solo precarga en nivel 1>> {
    id: Long
    nombre: String
    activo: Boolean
  }
  class ProductoEtiqueta <<Entity · sin uso en nivel 1>> {
    productoId: Long
    etiquetaId: Long
  }
  class Idioma <<Entity · sin uso en nivel 1>> {
    id: Long
    codigo: String
    nombre: String
    activo: Boolean
  }
  class ProductoTraduccion <<Entity · sin uso en nivel 1>> {
    productoId: Long
    idiomaId: Long
    nombre: String
  }
  class Mesa <<Entity>> {
    id: Long
    numero: Int
  }
  class Comanda <<Entity>> {
    id: Long
    mesaId: Long
    estado: EstadoComanda
    fechaCreacion: Long
    fechaCierre: Long?
  }
  enum EstadoComanda {
    PENDIENTE
    PAGADA
    ANULADA
  }
  class LineaComanda <<Entity>> {
    id: Long
    comandaId: Long
    productoId: Long
    cantidad: Int
    precioUnitarioCentimos: Int
    nombreProducto: String
  }
  class LineaModificador <<Entity · sin uso en nivel 1>> {
    id: Long
    lineaComandaId: Long
    modificadorId: Long
    precioCentimos: Int
    nombreModificador: String
    cantidad: Int
  }
}

Categoria "1" --> "*" Producto : categoriaId
Producto "1" --> "0..1" ProductoNutricion
Producto "1" --> "*" Modificador
Modificador ..> TipoModificador
Producto "1" --> "*" ProductoAlergeno
Alergeno "1" --> "*" ProductoAlergeno
Producto "1" --> "*" ProductoEtiqueta
Etiqueta "1" --> "*" ProductoEtiqueta
Producto "1" --> "*" ProductoTraduccion
Idioma "1" --> "*" ProductoTraduccion
Mesa "1" --> "*" Comanda : mesaId
Comanda "1" *--> "*" LineaComanda : comandaId
LineaComanda "1" *--> "*" LineaModificador : CASCADE (R11)
Producto "1" <.. "*" LineaComanda : productoId\n(nombre y precio congelados, R14)
Modificador "1" <.. "*" LineaModificador : modificadorId
Comanda ..> EstadoComanda

@enduml
```

### clases-acceso.puml

```plantuml
@startuml clases-acceso
title Diagrama de clases (borrador, nivel 1) — figura 2 de 4: base de datos, DAOs y repositorios
skinparam classAttributeIconSize 0
skinparam shadowing false
skinparam nodesep 25
skinparam ranksep 40
skinparam packageStyle rectangle
skinparam class {
  BackgroundColor white
  BorderColor #444
  ArrowColor #444
  FontSize 12
  AttributeFontSize 11
}
skinparam note {
  BackgroundColor #FFF8DC
  BorderColor #999
  FontSize 10
}
hide empty members
left to right direction
class Categoria <<Entity>>
class Producto <<Entity>>
class ProductoAlergeno <<Entity>>
class Alergeno <<Entity>>
class Etiqueta <<Entity>>
class Mesa <<Entity>>
class Comanda <<Entity>>
class LineaComanda <<Entity>>
class AppDatabase <<RoomDatabase>> {
categoriaDao(): CategoriaDao
productoDao(): ProductoDao
precargadosDao(): PrecargadosDao
mesaDao(): MesaDao
comandaDao(): ComandaDao
}
class Precarga <<RoomDatabase.Callback>> {
onCreate(db)
-crearCategoriaOtros()
-crear60Mesas()
-crear14Alergenos()
-crear3Etiquetas()
-crearEjemplo()
}
AppDatabase ..> Precarga : al crear la BD

interface CategoriaDao <<Dao>> {
insertar(c: Categoria): Long
actualizar(c: Categoria)
todas(): List<Categoria>
porDefecto(): Categoria
existeNombre(nombre: String, exceptoId: Long): Boolean
}
interface ProductoDao <<Dao>> {
insertar(p: Producto): Long
actualizar(p: Producto)
porId(id: Long): Producto
porCategoria(categoriaId: Long): List<Producto>
visibles(): List<Producto>
hayAlgunoVisible(): Boolean
existeNumero(numero: Int, exceptoId: Long): Boolean
alergenosDe(productoId: Long): List<Alergeno>
guardarAlergenos(productoId: Long, ids: List<Long>)
}
interface PrecargadosDao <<Dao>> {
alergenos(): List<Alergeno>
etiquetas(): List<Etiqueta>
insertarAlergenos(lista: List<Alergeno>)
insertarEtiquetas(lista: List<Etiqueta>)
}
interface MesaDao <<Dao>> {
todas(): List<Mesa>
insertarTodas(mesas: List<Mesa>)
}
interface ComandaDao <<Dao>> {
insertar(c: Comanda): Long
actualizar(c: Comanda)
porId(id: Long): Comanda
pendienteDeMesa(mesaId: Long): Comanda?
pendientesConTotal(): List<MesaConTotal>
lineasDe(comandaId: Long): List<LineaComanda>
insertarLineas(lineas: List<LineaComanda>)
borrarLinea(lineaId: Long)
contarLineas(comandaId: Long): Int
pagadasEntre(inicio: Long, fin: Long): List<Comanda>
mesasConProductoPendiente(productoId: Long): List<Int>
mesasConCategoriaPendiente(categoriaId: Long): List<Int>
}
CategoriaDao ..> Categoria
ProductoDao ..> Producto
ProductoDao ..> ProductoAlergeno
PrecargadosDao ..> Alergeno
PrecargadosDao ..> Etiqueta
MesaDao ..> Mesa
ComandaDao ..> Comanda
ComandaDao ..> LineaComanda

class CartaRepository {
categorias(): List<Categoria>
guardarCategoria(c: Categoria): ResultadoGuardado
eliminarCategoria(id: Long): List<Int>
platosDe(categoriaId: Long): List<Producto>
platosVisibles(): List<Producto>
hayPlatoVisible(): Boolean
plato(id: Long): Producto
guardarPlato(p: Producto, alergenos: List<Long>): ResultadoGuardado
eliminarPlato(id: Long): List<Int>
alergenos(): List<Alergeno>
alergenosDe(productoId: Long): List<Alergeno>
}
class ComandaRepository {
mesasConEstado(): List<MesaEstado>
comandaPendiente(mesaId: Long): Comanda?
enviarCarrito(mesaId: Long, carrito: Carrito): Long
lineasDe(comandaId: Long): List<LineaComanda>
totalDe(comandaId: Long): Int
quitarLinea(lineaId: Long): Boolean
anular(comandaId: Long)
cobrar(comandaId: Long)
resumenDelDia(dia: LocalDate): ResumenIngresos
}
class SeguridadRepository {
hayPin(): Boolean
crearPin(pin: String)
comprobarPin(pin: String): Boolean
cambiarPin(actual: String, nuevo: String): Boolean
}
class PinStore <<SharedPreferences>> {
-generarSal(): ByteArray
-hash(pin: String, sal: ByteArray): ByteArray
guardar(pin: String)
coincide(pin: String): Boolean
existe(): Boolean
}
class MesaEstado <<data class>> {
mesa: Mesa
comandaId: Long?
totalCentimos: Int?
}
class ResumenIngresos <<data class>> {
dia: LocalDate
comandas: List<ComandaConTotal>
numComandas: Int
totalCentimos: Int
}
CartaRepository --> CategoriaDao
CartaRepository --> ProductoDao
CartaRepository --> PrecargadosDao
ComandaRepository --> MesaDao
ComandaRepository --> ComandaDao
ComandaRepository ..> MesaEstado
ComandaRepository ..> ResumenIngresos
SeguridadRepository --> PinStore

note top of CartaRepository
  Garantiza R6 (aviso de mesas), R9 (número único),
  R15 (plato visible), R16 (una categoría por defecto)
end note
note top of ComandaRepository
  Garantiza R1 (una pendiente por mesa), R2 (crear o ampliar),
  R3 (ocupación calculada), R7 (sin líneas → ANULADA),
  R10 (total calculado), R14 (congelar nombre y precio)
end note
note bottom of PinStore
  PIN de 4 cifras. Guarda sal + hash PBKDF2,
  nunca el PIN (spec, apartado 9)
end note
@enduml
```

> **Lo que esta figura ya no refleja:** `PinStore.hash()` se saca a `Hash` (dominio puro) y los repositorios llaman a `Validacion.precioValido()` antes de guardar. Decidido en el bloque 10, después de dibujarla.

### clases-viewmodels.puml

```plantuml
@startuml clases-viewmodels
title Diagrama de clases (borrador, nivel 1) — figura 3 de 4: ViewModels y auxiliares
skinparam classAttributeIconSize 0
skinparam shadowing false
skinparam nodesep 25
skinparam ranksep 40
skinparam packageStyle rectangle
skinparam class {
  BackgroundColor white
  BorderColor #444
  ArrowColor #444
  FontSize 12
  AttributeFontSize 11
}
skinparam note {
  BackgroundColor #FFF8DC
  BorderColor #999
  FontSize 10
}
hide empty members
left to right direction

class CartaRepository
class ComandaRepository
class SeguridadRepository

class Carrito {
mesaId: Long
lineas: List<LineaCarrito>
anadir(p: Producto, cantidad: Int)
cambiarCantidad(productoId: Long, cantidad: Int)
quitar(productoId: Long)
numPlatos(): Int
total(): Int
estaVacio(): Boolean
}
class LineaCarrito <<data class>> {
producto: Producto
cantidad: Int
importe(): Int
}
class ImageStore {
guardar(uri: Uri): String
borrar(ruta: String)
-redimensionar(bitmap, maxLado: Int)
-comprimirJpeg(bitmap)
}
Carrito "1" *--> "*" LineaCarrito

class SelectorViewModel {
hayPin: LiveData<Boolean>
mesas: LiveData<List<MesaEstado>>
crearPin(pin: String)
comprobarPin(pin: String): Boolean
hayPlatoVisible(): Boolean
}
class PanelViewModel {
categoriasConPlatos: LiveData<List<CategoriaConPlatos>>
guardarCategoria(c: Categoria): ResultadoGuardado
eliminarCategoria(id: Long): List<Int>
cambiarPin(actual: String, nuevo: String): Boolean
}
class PlatoViewModel {
plato: LiveData<Producto>
alergenos: LiveData<List<Alergeno>>
categorias: LiveData<List<Categoria>>
cargar(id: Long?)
elegirFoto(uri: Uri)
guardar(p: Producto, alergenos: List<Long>): ResultadoGuardado
eliminar(): List<Int>
}
class ResumenIngresosViewModel {
dia: LiveData<LocalDate>
resumen: LiveData<ResumenIngresos>
elegirDia(dia: LocalDate)
lineasDe(comandaId: Long): List<LineaComanda>
}
class PedidoViewModel {
mesaId: Long
categorias: LiveData<List<Categoria>>
platosVisibles: LiveData<List<Producto>>
carrito: LiveData<Carrito>
anadir(p: Producto, cantidad: Int)
cambiarCantidad(productoId: Long, cantidad: Int)
quitar(productoId: Long)
enviar(): Long
comprobarPin(pin: String): Boolean
}
class CuentaViewModel {
mesas: LiveData<List<MesaEstado>>
comanda: LiveData<Comanda?>
lineas: LiveData<List<LineaComanda>>
total: LiveData<Int>
abrirMesa(mesaId: Long)
quitarLinea(lineaId: Long): Boolean
anular()
cobrar()
cambio(entregadoCentimos: Int): Int
}
SelectorViewModel --> SeguridadRepository
SelectorViewModel --> CartaRepository
SelectorViewModel --> ComandaRepository
PanelViewModel --> CartaRepository
PanelViewModel --> SeguridadRepository
PlatoViewModel --> CartaRepository
PlatoViewModel --> ImageStore
ResumenIngresosViewModel --> ComandaRepository
PedidoViewModel --> CartaRepository
PedidoViewModel --> ComandaRepository
PedidoViewModel --> SeguridadRepository
PedidoViewModel --> Carrito
CuentaViewModel --> ComandaRepository

note bottom of PedidoViewModel
  El carrito vive aquí: lo comparten 5a, 5b y 5c
  y no se guarda (R10). enviar() aplica R2 y R4
end note
note bottom of CuentaViewModel
  cambio() = entregado − total;
  no guarda nada (spec, apartado 10)
end note

@enduml
```

> **Lo que esta figura ya no refleja:** `CuentaViewModel.cambio()` delega en `Calculadora.cambio()`, y el paquete `dominio/` suma `Calculadora`, `Validacion` y `Hash` junto a `Carrito` y `LineaCarrito`.

### clases-pantallas.puml

```plantuml
@startuml clases-pantallas
title Diagrama de clases (borrador, nivel 1) — figura 4 de 4: pantallas y adaptadores
skinparam classAttributeIconSize 0
skinparam shadowing false
skinparam nodesep 25
skinparam ranksep 40
skinparam packageStyle rectangle
skinparam class {
  BackgroundColor white
  BorderColor #444
  ArrowColor #444
  FontSize 12
  AttributeFontSize 11
}
skinparam note {
  BackgroundColor #FFF8DC
  BorderColor #999
  FontSize 10
}
hide empty members
left to right direction
class SelectorViewModel
class PanelViewModel
class PlatoViewModel
class ResumenIngresosViewModel
class PedidoViewModel
class CuentaViewModel
class SelectorActivity <<pantalla 1>>
class SelectorFragment <<1a>>
class CrearPinFragment <<1b>>
class PinDialog <<1c · diálogo>>
class RejillaMesasFragment <<1d elegir · 6a gestionar>> {
modo: ModoRejilla
}
class PanelActivity <<pantalla 2 · 2a>>
class CategoriaBottomSheet <<2b · hoja inferior>>
class CambiarPinDialog <<1e · diálogo>>
class PlatoActivity <<pantalla 3 · 3a>>
class MesasAfectadasDialog <<3d / 2e · diálogo R6>>
class ConfirmacionDialog <<3e, Enviar, Cobrar, Anular, R7, puerta de Pedir>>
class ResumenIngresosActivity <<2g>>
class PedidoActivity <<pantalla 5>>
class CartaFragment <<5a>>
class FichaPlatoFragment <<5b>>
class CarritoFragment <<5c>>
class CuentaActivity <<pantalla 6>>
class ComandaFragment <<6b>>
class ReciboFragment <<6c · y solo lectura desde 2g>> {
soloLectura: Boolean
}

class FilaPlatoAdapter <<2a y 5a>>
class CategoriaAdapter <<2a cajas · 5a fila horizontal>>
class MesaAdapter <<1d y 6a>>
class LineaAdapter <<5c, 6b, 6c y 2g>>

SelectorActivity --> SelectorViewModel
SelectorActivity *--> SelectorFragment
SelectorActivity *--> CrearPinFragment
SelectorActivity *--> RejillaMesasFragment : modo elegir
SelectorFragment ..> PinDialog
PanelActivity --> PanelViewModel
PanelActivity ..> CategoriaBottomSheet
PanelActivity ..> CambiarPinDialog
PanelActivity ..> MesasAfectadasDialog
PlatoActivity --> PlatoViewModel
PlatoActivity ..> MesasAfectadasDialog
PlatoActivity ..> ConfirmacionDialog : cadena 3e
ResumenIngresosActivity --> ResumenIngresosViewModel
ResumenIngresosActivity *--> ReciboFragment : soloLectura
PedidoActivity --> PedidoViewModel
PedidoActivity *--> CartaFragment
PedidoActivity *--> FichaPlatoFragment
PedidoActivity *--> CarritoFragment
CartaFragment ..> PinDialog : Salir
CarritoFragment ..> ConfirmacionDialog : Enviar
CuentaActivity --> CuentaViewModel
CuentaActivity *--> RejillaMesasFragment : modo gestionar
CuentaActivity *--> ComandaFragment
CuentaActivity *--> ReciboFragment
ComandaFragment ..> ConfirmacionDialog : Anular, R7
ReciboFragment ..> ConfirmacionDialog : Cobrar
PanelActivity ..> FilaPlatoAdapter
CartaFragment ..> FilaPlatoAdapter
PanelActivity ..> CategoriaAdapter
CartaFragment ..> CategoriaAdapter
RejillaMesasFragment ..> MesaAdapter
CarritoFragment ..> LineaAdapter
ComandaFragment ..> LineaAdapter
ReciboFragment ..> LineaAdapter

note bottom of RejillaMesasFragment
  Un solo componente con dos modos (spec, apartado 6):
  elegir (1d) y gestionar (6a)
end note
@enduml
```
