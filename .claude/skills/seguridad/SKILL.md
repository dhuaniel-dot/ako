---
name: seguridad
description: 'Reglas para que no se suba nunca a GitHub una clave de API, una contraseña, un keystore o un archivo de configuración con secretos (como claude_desktop_config.json o .env), y cómo revisar los repositorios si hay dudas. Aquí se van añadiendo las reglas de seguridad nuevas.'
when_to_use: 'Antes de cada commit o push; al crear o tocar .gitignore, .env, local.properties, gradle.properties, keystores, .mcp.json o cualquier configuración de Claude; cuando Daniel diga «seguridad», «claves», «¿se ha filtrado algo?», «revisa los repos», o escriba /seguridad.'
---

# Seguridad

Origen (28 sep 2026): un post enseñaba que buscando `claude_desktop_config.json` u `OPENAI_API_KEY` en GitHub salen cientos de claves de API expuestas por gente que subió su configuración. Daniel pidió que eso no pase nunca en sus repositorios. `ako` y `marcador` son **públicos**: todo lo que se sube lo puede leer cualquiera, **también en commits antiguos**.

## 1. Lo que no se sube nunca

- Claves de API y tokens: `sk-ant-…` (Anthropic), `sk-…` (OpenAI), `ghp_…` / `github_pat_…` (GitHub), `AIza…` (Google), `AKIA…` (AWS), `xoxb-…` (Slack).
- Archivos de configuración con secretos: `.env`, `.env.*`, `claude_desktop_config.json`, `.mcp.json`, `.claude/settings.local.json`.
- Firma de Android: `*.jks`, `*.keystore`, y `storePassword` / `keyPassword` escritos en `build.gradle.kts`.
- Cosas de este PC: `local.properties`, `C:\Users\dhuan\.gradle\gradle.properties`, cualquier cosa de `AppData`.
- Un PIN, una contraseña o un correo de verdad en el código o en las pruebas (en las pruebas se usan datos inventados, como `1234`).
- Datos personales en skills, `CLAUDE.md`, el diario o el código: teléfono, DNI, dirección, correo, contraseñas de cuentas, datos de otras personas (profesor, compañeros). Basta con «Daniel» y «el profesor». Las skills se escriben para que se puedan leer en público sin problema.
- El correo de los commits se ve en un repositorio público: usar el correo anónimo de GitHub (`…@users.noreply.github.com`, en GitHub → Settings → Emails) en `git config user.email`. Pendiente de decidir con Daniel (28 sep 2026).

Ako no necesita ninguna clave de API: el spec prohíbe servidores. Si algún día parece que hace falta una, **se para y se pregunta a Daniel**; no se escribe en el código.

## 2. Antes de cada commit (lo hace Claude, siempre)

1. `git status --short`: ¿aparece algún archivo de la lista del punto 1? Si aparece, **no se añade**, se mete en `.gitignore` y se avisa a Daniel.
2. Buscar secretos en lo que se va a subir:
   ```
   git diff --cached | grep -niE 'sk-ant-|sk-[A-Za-z0-9_-]{20,}|api[_-]?key|ghp_|github_pat_|AIza|AKIA|BEGIN .*PRIVATE|storePassword|keyPassword|password *='
   ```
   Si sale algo que no sea un falso positivo (p. ej. `numberPassword` es un tipo de campo, no una contraseña), **no se hace el commit**.
3. Nunca `git add -A` ni `git add .` a ciegas: se añaden los archivos por su nombre.
4. Red de seguridad automática (29 sep 2026): el hook `.claude/hooks/comprobar-commit.sh` (paso 4) bloquea los commits que hace Claude Code si lo cambiado lleva una clave con forma real (`sk-ant-…`, `ghp_…`, `AIza…`, `AKIA…`, clave privada). **No vigila los commits que Daniel hace a mano en Git Bash**: ahí sirve el punto 1 de este apartado.

## 3. Si una clave llega a subirse

Borrarla en un commit nuevo **no basta**: sigue en el historial y los bots de GitHub la encuentran en minutos.

1. **Revocarla primero** en la web del servicio (Anthropic: console.anthropic.com → API Keys; GitHub: Settings → Developer settings → Tokens) y crear otra.
2. Después, quitarla del archivo y meter el archivo en `.gitignore`.
3. Reescribir el historial necesita `push --force`, que está prohibido en este repositorio: se decide con Daniel, no se hace por cuenta propia.
4. Se apunta en la ficha del diario: qué pasó, por qué y cómo se resolvió (regla 6).

## 4. Revisar los repositorios (cuando Daniel lo pida)

Buscar en **todo el historial**, no solo en los archivos actuales:

```
git grep -nIiE '<patrón del punto 2>' $(git rev-list --all)
git log --all --name-only --pretty=format: | sort -u
```

Repositorios de Daniel: `ako` (público), `marcador` (público), `claude-pr-practice` (privado). Última revisión: 28 sep 2026, los tres limpios.

## 5. Reglas nuevas

Cuando aparezca otra cosa de seguridad («y cosas parecidas»), se añade aquí como una línea con la fecha y el motivo.
