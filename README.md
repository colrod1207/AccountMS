## 📊 Project Status

![Java](https://img.shields.io/badge/java-17-blue)
![OpenAPI](https://img.shields.io/badge/docs-Swagger-blue?logo=swagger)
<!-- ![License](https://img.shields.io/github/license/colrod1207/AccountMS)
![Build](https://img.shields.io/github/actions/workflow/status/colrod1207/AccountMS/maven.yml)
![Coverage](https://img.shields.io/codecov/c/github/colrod1207/AccountMS) -->

## 📐 Code Formatting with Maven
This project uses the formatter-maven-plugin to automatically format source code and resource files according to the Google Java Style Guide.

### ✅ What it formats

- Java source files (.java)

- XML configuration files (.xml)

- JSON data files (.json)

- YAML files (.yaml, .yml)

### 🔧 How to apply formatting

- To format all supported files:

```bash
mvn formatter:format
```

- To validate formatting (useful before committing):

```bash
mvn formatter:validate
```

> The plugin is configured to run automatically during the validate phase of the Maven lifecycle. This means formatting will be applied when running commands like mvn install, mvn verify, or mvn package.

### 📁 Style configuration
The formatting rules are defined in the file:
```Código
eclipse-java-google-style.xml
```
