#!/bin/bash
# Hook PreToolUse de Claude Code para el repositorio de Ako [Claude].
# Se ejecuta solo, antes de cada comando de consola. Si el comando es un `git commit`:
#   1) bloquea si el mensaje lleva "Co-Authored-By" (P226: los commits no llevan atribución);
#   2) exige que el mensaje empiece por "S<N>:" (spec, apartado 13);
#   3) exige que exista docs/diario/sesion-NN.md (la ficha de la sesión).
# Salida 0 = deja pasar. Salida 2 = bloquea y explica el motivo por stderr.
# Solo mira el campo tool_input.command del JSON (no la descripción ni el resto), para no
# bloquear, p. ej., un grep que contenga las palabras "git commit".

entrada=$(cat)
cmd=$(printf '%s' "$entrada" | python -c "import sys,json;print(json.load(sys.stdin).get('tool_input',{}).get('command',''))" 2>/dev/null)
[ -z "$cmd" ] && exit 0

# ¿Es un commit? (tolera opciones entre git y commit, p. ej. git -c x=y commit)
printf '%s' "$cmd" | grep -qE '(^|[;&|[:space:]])git[[:space:]]+([^;&|]*[[:space:]])?commit([[:space:]]|$)' || exit 0

if printf '%s' "$cmd" | grep -qi 'co-authored-by'; then
  echo "BLOQUEADO: el mensaje del commit lleva 'Co-Authored-By'. En Ako los commits no llevan atribución (P226); el uso de IA se declara en la ficha del diario." >&2
  exit 2
fi

# Mensaje: lo que sigue a -m, -am o --message; tiene que empezar por S<N>: (S1:, S08:, s3: se admiten)
n=$(printf '%s' "$cmd" | grep -oiE -- '(-[a-z]*m[[:space:]]+|--message[= ][[:space:]]*)["'"'"']?S0*([0-9]+):' | head -1 | grep -oE '[0-9]+:' | tr -d ':')
if [ -z "$n" ]; then
  echo "BLOQUEADO: el mensaje del commit tiene que empezar por S<N>: (p. ej. \"S3: DAOs y dominio puro\" o \"S3: WIP Carrito\"), como fija el spec, apartado 13." >&2
  exit 2
fi

nn=$(printf '%02d' "$((10#$n))")
raiz="${CLAUDE_PROJECT_DIR:-.}"
if [ ! -f "$raiz/docs/diario/sesion-$nn.md" ]; then
  echo "BLOQUEADO: no existe docs/diario/sesion-$nn.md. Cada sesión tiene su ficha del diario antes del commit (abrir-sesion la crea)." >&2
  exit 2
fi

exit 0
