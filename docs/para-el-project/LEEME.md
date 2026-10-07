# Para el Project — lo que Daniel corrige en el doc, por áreas

Creada el 7 de octubre de 2026 (P178 → C, revisión del 6 oct, R03) [Claude]. Hasta entonces todo vivía en una sola tabla en `docs/decisiones-code.md`, apartado 6; con 45 filas sin tachar, Daniel decidió **partirla en varios documentos**: cuando vaya al Project «Proyecto Intermodular» elige **qué documento de cambios aplicar** en esa sesión y se lo da a leer al chat del Project. La regla no cambia: lo decidido en Claude Code manda sobre el spec y sobre el Project (Daniel, 30 sep 2026); el doc explica el prototipo.

## Los documentos

| Documento | Qué corrige en el doc | Filas (7 oct) |
|---|---|---|
| `01-diagrama-de-clases-y-bd.md` | El diagrama de clases (`spec+doc-clases.md`), el modelo de datos y el E-R, los paquetes del spec (apartado 3) | 24 |
| `02-memoria.md` | La memoria: planificación, arquitectura y reglas, seguridad, pruebas, accesibilidad, localización, uso de IA, Anexos I y II, guion del vídeo | 20 |
| `03-pantallas-textos-y-requisitos.md` | Las fichas de pantalla, los wireframes, `textos-ui.md` y los requisitos (RF/RNF, nivel 2) | 13 |

## Cómo se usa

- **Cada fila es un cambio frente al spec o al doc**, con su fecha, su decisión (Pnnn) y dónde tocarlo. Una fila vive en **un solo documento** (el de su destino principal); si toca también otro sitio, la celda «Dónde tocarlo» lo dice.
- **Se tacha** (`~~así~~`) cuando Daniel diga que ya está en el doc. Las fuentes para la bibliografía están en las tablas del apartado 5 de `decisiones-code.md` (cada decisión lleva su enlace).
- **Al cerrar cada sesión** (`cerrar-sesion`, paso 5), las filas nuevas van **directamente** al documento de su área, no a `decisiones-code.md`; los «Anotar para el Project» sueltos de los apartados 5.x se pasan aquí.
- **Qué llevar al Project:** Daniel abre el Project, le dice al chat que lea `C:\AKO\docs\para-el-project\0N-….md` y corrija el doc; cuando el chat termine, se tachan las filas. Una sesión corta del Project por documento basta.
