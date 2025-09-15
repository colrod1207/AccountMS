# AccountMS — Entregable 4 (Calidad)
**Bootcamp Tech Girls Power – NTT DATA**  


Este proyecto contiene el microservicio **AccountMS** preparado para el Entregable 4: pruebas unitarias con **JUnit/Mockito**, cobertura con **JaCoCo**, análisis estático con **SonarLint** (en IDE) y **Checkstyle**, junto con mapeo de **SOLID** y patrones.

---

## 1️⃣ Requisitos
- **Java 17** (o 11 si el proyecto lo exige)
- **Maven 3.9+**
- IntelliJ IDEA (recomendado) con plugin **SonarLint**

---

## 2️⃣ Comandos esenciales
```bash
# 1) Compilar y ejecutar pruebas
mvn -q clean test

# 2) Generar cobertura JaCoCo
mvn -q clean test jacoco:report
# Reporte: target/site/jacoco/index.html

# 3) Ejecutar Checkstyle (Google Style)
mvn -q checkstyle:checkstyle
# Reporte: target/site/checkstyle.html

# 4) Árbol de dependencias (útil para revisar alcances)
mvn -q dependency:tree
```

> Si tu IDE muestra advertencias de SonarLint, corrige y vuelve a analizar: **SonarLint Tool Window → Analyze All Files**.

---

## 3️⃣ Plugins Maven (referencia rápida)
Si aún no están en tu `pom.xml`, agrega estas secciones dentro de `<build><plugins>`:

```xml
<!-- JaCoCo -->
<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.12</version>
  <executions>
    <execution>
      <goals>
        <goal>prepare-agent</goal>
      </goals>
    </execution>
    <execution>
      <id>report</id>
      <phase>test</phase>
      <goals>
        <goal>report</goal>
      </goals>
    </execution>
    <execution>
      <id>check</id>
      <goals>
        <goal>check</goal>
      </goals>
      <configuration>
        <rules>
          <rule>
            <element>BUNDLE</element>
            <limits>
              <limit>
                <counter>LINE</counter>
                <value>COVEREDRATIO</value>
                <minimum>0.80</minimum>
              </limit>
              <limit>
                <counter>BRANCH</counter>
                <value>COVEREDRATIO</value>
                <minimum>0.70</minimum>
              </limit>
            </limits>
          </rule>
        </rules>
      </configuration>
    </execution>
  </executions>
</plugin>

<!-- Checkstyle (Google Style) -->
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-checkstyle-plugin</artifactId>
  <version>3.5.0</version>
  <configuration>
    <configLocation>checkstyle.xml</configLocation>
    <encoding>UTF-8</encoding>
    <consoleOutput>true</consoleOutput>
    <failOnViolation>false</failOnViolation>
  </configuration>
  <executions>
    <execution>
      <id>checkstyle</id>
      <phase>verify</phase>
      <goals>
        <goal>checkstyle</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

Asegúrate de incluir un `checkstyle.xml` (Google Java Style).

---

## 4️⃣ Estructura sugerida
```
com.yourorg.accountms
├── interfaces      # controllers, dto, mappers
├── application     # casos de uso / orquestación
├── domain          # entidades, políticas (estrategies), servicios de dominio
└── infrastructure  # repositorios, config, adapters externos
```

---

## 5️⃣ Evidencias esperadas
- **JaCoCo**: captura del índice y clases clave con cobertura ≥ 80% líneas / ≥ 70% ramas.
- **Checkstyle**: resumen sin infracciones críticas.
- **SonarLint**: lista de issues resueltos o “0 issues” tras correcciones.
- **Tests**: cubren caminos felices + casos borde (nulls, excepciones, límites).

---

## 6️⃣ Notas rápidas de SOLID y patrones
- SRP: `AccountController`, `AccountService`, `AccountRepository`, `AccountMapper` con responsabilidades únicas.
- Strategy: políticas para comisiones/límites/validaciones (`FeePolicy`, `LimitPolicy`, `ValidationPolicy`).
- Factory: `AccountFactory` para instanciar cuentas por tipo.
- Repository: persistencia vía Spring Data.
- Template Method: flujo común para operaciones con hooks por tipo de cuenta.

---

## 7️⃣ Problemas comunes
- **Tests no levantan contexto**: revisa `@SpringBootTest` vs tests puros con Mockito; evita cargar contexto si no es necesario.
- **Plugins no ejecutan**: confirma que los bloques están **dentro de `<build><plugins>`**.
- **Cobertura baja**: agrega tests de ramas negativas y excepciones.

---

## 8️⃣ Autoras
- Antonella Hermayoni Carrasco Aguilar
- Rosario Katrina García Yallico

¡Éxitos con la entrega! 🚀
