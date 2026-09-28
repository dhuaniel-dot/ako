> **Plantilla de `docs/diario/EN-CURSO.md`** [Claude]: el estado exacto de una sesión cuando se cambia de chat sin cerrarla (skill `relevo`). Se rellena en un minuto, no se redacta. La lee `abrir-sesion` (y el hook de arranque la mete en el contexto del chat nuevo); la borra `cerrar-sesion` después de pasar sus decisiones a la ficha y a `docs/decisiones-code.md`.

```markdown
# EN CURSO — Sesión NN — <fecha y hora del relevo>

**Objetivo de la sesión:** <la línea del spec>
**Motivo del relevo:** chat largo (NN %) · cambio de modelo · fin del día · emergencia (>85 %)

## Pieza en curso
- Qué es: <nombre de la pieza y archivo(s)>
- Estado: compila / no compila / sin empezar
- Qué falta exactamente: <una o dos líneas>

## Terminado hoy
- <pieza> (commit `abc1234`) …

## Archivos tocados sin commit (`git status --short`)
```
<pegar la salida>
```

## Último commit
`abc1234` — <mensaje>

## El bloque de código de la pieza en curso, tal como se le dio a Daniel
(ruta del archivo y el bloque entero; es lo que `verificar` compara en el chat nuevo)

## Decisiones tomadas en este chat que no están en ningún documento
- <decisión> · [Claude] si la propuso Claude · motivo en una línea

## Lo que Daniel dijo que no entendió todavía
- …

## Los tres pasos siguientes, en orden
1. …
2. …
3. …

**Modelo y esfuerzo para el chat siguiente:** <Opus 5.5 medio / alto / Fable si es hito>
```
