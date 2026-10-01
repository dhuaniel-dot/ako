# Historial de cambios de `como-trabajamos` (completo)

Movido aquí el 1 de octubre de 2026 (revisión F14) para que la skill cargue menos en cada sesión. La skill conserva solo las entradas de la última sesión; al cerrar cada sesión, Claude pasa aquí las anteriores.

- **2026-10-01** · Daniel (S5): los repasos de lo no entendido van **siempre** a los PDF, no al chat; solo hay repaso en el chat si él lo pide. Sustituye a «se vuelve a comprobar al abrir la sesión siguiente».
- **2026-10-01** · Daniel: se mantiene «pega antes de la última `}`» para añadir métodos (no hace falta dar el archivo entero aunque dos veces quedaran dentro de otro método); Claude lee el archivo tras cada «ya está» y arregla la sangría. Cuando Daniel pide «revisa», Claude lee el archivo entero, no solo compila.
- **2026-10-01** · Daniel: el repaso de lo no entendido puede ir a los PDF del cierre en vez de darse en el chat; se vuelve a comprobar al abrir la sesión siguiente.
- **2026-10-01** · Daniel: los atajos y trucos de Android Studio que pregunta se apuntan en `docs/guias/android-studio-basico.md` (apartado 10, con casos de uso «quiero… → hago…»).
- **2026-09-30** · Daniel: al cerrar cada sesión, Claude pone al día las guías de las sesiones siguientes y `juego-de-datos.md` con lo decidido (`cerrar-sesion`, paso 5b).
- **2026-09-30** · Daniel: apartado «Para el vídeo» en cada ficha del diario, para montar después el guion.
- **2026-09-30** · Daniel: forma corta para crear archivos (`paquete → Kotlin Class/File → tipo`); el paso a paso, solo con lo nuevo.
- **2026-09-30** · Daniel: junto a la ruta de cada archivo nuevo, cómo crearlo (paquete, New → Kotlin Class/File y tipo); los bloques de archivos nuevos van sin `package`.
- **2026-09-30** · Daniel: no se sube la ficha al Project a mano; el chat del Project lee la carpeta `C:\AKO` entera.
- **2026-09-30** · Daniel: los resúmenes en PDF son para estudiar; no se suben a GitHub (`docs/resumenes/` en `.gitignore`), solo se envían por Gmail.
- **2026-09-30** · Daniel: solo nivel 1; lo que se empieza se termina; cualquier propuesta del nivel 2, con todo lo que implica (P115).
- **2026-09-30** · Daniel: avisar cuando algo del nivel 2 sea fácil, para decidir si sube al nivel 1.
- **2026-09-30** · Daniel: lo decidido aquí tiene prioridad sobre el spec; los cambios se apuntan en «Para el Project» (`decisiones-code.md` 6).
- **2026-09-30** · Daniel: decir el atajo cada vez que se hace algo en Android Studio; su teclado es un 75 % US sin Insert ni teclado numérico.
- **2026-09-29** · Daniel: somos un equipo; si algo se tuerce, Claude para, lo dice y se replanifica juntos (Fable como asesor si hace falta).
- **2026-09-29** · Daniel (P112): elige él las decisiones de código; mínimo 3 opciones, una de Claude y otra de una fuente de internet con enlace. Sustituye la regla del 25 sep.
- **2026-09-28** · Daniel: un «parón» no es un relevo; se sigue en el mismo chat.
- **2026-09-28** · Daniel: en cada cambio de chat, dos resúmenes en PDF (sencillo y normal) enviados a su Gmail.
- **2026-09-28** · Daniel: lenguaje más coloquial y ejemplos de la vida real en las explicaciones.
- **2026-09-24** · Creada en la sesión 00 con la visión de Daniel (P24, P30, P33, P36, P37 del análisis inicial) y los umbrales de contexto (60 / 75 / 85 %).
- **2026-09-24** · Daniel: sin comandos, las skills se activan por lo que dice; en cada cambio de chat y cada cierre se revisa si esta skill necesita cambios.
- **2026-09-24** · Daniel: cada vez que se cambia de chat (relevo o cierre de sesión), Claude entrega el primer mensaje del chat siguiente, listo para copiar, con el modelo recomendado.
- **2026-09-24** · Daniel: dos marchas, calidad ahora y eficiencia cuando apriete la fecha; tabla de qué cambia y qué no. Marcha vigente: calidad.
- **2026-09-26** · Daniel: Fable de guardia (*advisor*) apagado por defecto; Claude lo propone cuando algo se complica y Daniel lo activa con `/advisor fable` (regla global en `~/.claude/CLAUDE.md`).
- **2026-09-25** · Daniel: en dudas técnicas de código elige la recomendación de Claude; y quiere el nombre de cada vista junto a su código (2e, 3e…).
- **2026-09-24** · Daniel: cada mensaje de una sesión termina con «Ahora tú: …» (la acción o atajo que le toca, o «nada», o la corrección si usó algo que no debía).
