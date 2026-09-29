#!/bin/bash
# Hook PreToolUse de Claude Code para el repositorio de Ako [Claude].
# Se ejecuta solo, antes de cada comando de consola. Si el comando es un `git commit`:
#   1) (quitado el 29 sep 2026, P111: desde entonces los commits SÍ llevan la línea de coautor de Claude);
#   2) exige que el mensaje empiece por "S<N>:" (spec, apartado 13);
#   3) exige que exista docs/diario/sesion-NN.md (la ficha de la sesión);
#   4) bloquea si lo que se va a subir parece llevar una clave de API o una clave privada
#      (skill seguridad) [Claude].
# Salida 0 = deja pasar. Salida 2 = bloquea y explica el motivo por stderr.
# Solo mira el campo tool_input.command del JSON (no la descripción ni el resto), para no
# bloquear, p. ej., un grep que contenga las palabras "git commit".

entrada=$(cat)
cmd=$(printf '%s' "$entrada" | python -c "import sys,json;print(json.load(sys.stdin).get('tool_input',{}).get('command',''))" 2>/dev/null)
[ -z "$cmd" ] && exit 0

# ¿Es un commit? (tolera opciones entre git y commit, p. ej. git -c x=y commit)
printf '%s' "$cmd" | grep -qE '(^|[;&|[:space:]])git[[:space:]]+([^;&|]*[[:space:]])?commit([[:space:]]|$)' || exit 0

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

# 4) ¿Lo que se va a subir lleva una clave? (skill seguridad) [Claude]
# git diff HEAD = todo lo cambiado desde el último commit (lo añadido con git add y lo que no).
# Solo se miran las líneas nuevas (las que empiezan por +).
# Los patrones son los de las claves reales: Anthropic, OpenAI, GitHub, Google, AWS y claves privadas.
claves='sk-ant-[A-Za-z0-9_-]{20,}|sk-[A-Za-z0-9]{32,}|ghp_[A-Za-z0-9]{30,}|github_pat_[A-Za-z0-9_]{30,}|AIza[0-9A-Za-z_-]{35}|AKIA[0-9A-Z]{16}|BEGIN [A-Z ]*PRIVATE KEY'
if git -C "$raiz" diff HEAD | grep '^+' | grep -qE "$claves"; then
  echo "BLOQUEADO: lo que se va a subir parece llevar una clave de API o una clave privada. Quítala, mete el archivo en .gitignore y avisa a Daniel (skill seguridad)." >&2
  exit 2
fi

exit 0
