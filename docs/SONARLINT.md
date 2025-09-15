# SonarLint — Guía y Evidencias
Actualizado: 2025-09-15

## Configuración en IntelliJ
1. Instala el plugin **SonarLint** (File → Settings → Plugins → SonarLint).
2. Modo **Standalone** es suficiente. Si tienen SonarQube/SonarCloud, pueden **Bind** opcionalmente.
3. En la ventana **SonarLint**: clic en **Analyze All Files**.

## Reglas típicas revisadas
- Nomenclatura de paquetes/clases.
- Complejidad ciclomática (métodos largos).
- Manejo de excepciones (evitar `Exception` genérica).
- Logs (`private static final Logger logger = LoggerFactory.getLogger(...)`).
- Nullability, recursos cerrados, serialización, etc.

## Cómo dejar evidencia
- Ejecuta **Analyze All Files** y toma captura del panel: `0 issues` o lista de issues resueltos.
- Adjunta capturas en el informe (carpeta `docs/evidencias/sonarlint/`).

## Comandos complementarios
No aplica por Maven; SonarLint corre en el IDE. Aun así, puedes anotar en el reporte:
- Fecha/hora del análisis.
- Número de archivos analizados.
- Issues resueltos (antes/después).
