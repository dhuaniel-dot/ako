#!/bin/bash
# Hook SessionStart de Claude Code para Ako [Claude].
# Al abrir un chat sobre C:\AKO, mete en el contexto (por la salida estándar) lo que un chat nuevo
# necesita para no perder el hilo: si hay una sesión a medias (EN-CURSO.md), su contenido entero;
# y siempre, cuál es la última ficha del diario y su apartado "Siguiente sesión".
raiz="${CLAUDE_PROJECT_DIR:-.}"
diario="$raiz/docs/diario"

if [ -f "$diario/EN-CURSO.md" ]; then
  echo "=== HAY UNA SESIÓN A MEDIAS: docs/diario/EN-CURSO.md ==="
  cat "$diario/EN-CURSO.md"
  echo "=== FIN DE EN-CURSO.md ==="
fi

ultima=$(ls "$diario"/sesion-[0-9][0-9].md 2>/dev/null | sort | tail -1)
if [ -n "$ultima" ]; then
  echo "=== Última ficha del diario: $(basename "$ultima") ==="
  # Desde "## Siguiente sesión" hasta el final del archivo.
  sed -n '/^## Siguiente sesión/,$p' "$ultima"
fi
exit 0
