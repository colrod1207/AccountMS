# Checklist de Calidad — Entregable 4
Actualizado: 2025-09-15

## Pruebas y Cobertura
- [ ] `mvn clean test jacoco:report` ejecutado sin errores.
- [ ] **Cobertura** ≥ 80% líneas y ≥ 70% ramas (ver `target/site/jacoco/index.html`).
- [ ] Tests con casos felices + negativos (excepciones, límites, nulls).

## Estático (IDE & Maven)
- [ ] **SonarLint**: Analyze All Files; issues resueltos o documentados.
- [ ] **Checkstyle**: `mvn checkstyle:checkstyle` con resumen limpio (ver `target/site/checkstyle.html`).

## Diseño
- [ ] Documento **SOLID** completado con ejemplos del repo (`docs/SOLID.md`).
- [ ] Documento **Patrones** completado (`docs/PATRONES.md`).

## Evidencias
- [ ] Capturas de JaCoCo.
- [ ] Capturas de Checkstyle.
- [ ] Capturas de SonarLint.
