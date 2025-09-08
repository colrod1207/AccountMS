# 🏦 Microservicio de Gestión de Cuentas Bancarias

Un microservicio RESTful desarrollado con Spring Boot para la gestión de cuentas bancarias, que permite crear, consultar y realizar operaciones de depósito y retiro.

## 📊 Estado del Proyecto

![Java](https://img.shields.io/badge/java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen)
![MongoDB](https://img.shields.io/badge/MongoDB-Database-green)
![OpenAPI](https://img.shields.io/badge/docs-Swagger-blue?logo=swagger)
![Maven](https://img.shields.io/badge/Maven-Build-red)

## 🏗️ Arquitectura del Sistema

### Diagrama de Secuencia

![Diagrama de Secuencia](src/diagrams/Diagrama%20de%20secuencia.png)

### Diagrama de Componentes

![Diagrama de Componentes](src/diagrams/Diagrama%20de%20componentes.png)

### Diagrama de Flujo

![Diagrama de Flujo](src/diagrams/Diagrama%20de%20flujo.png)

## 🚀 Características Principales

- ✅ **Gestión de Cuentas**: Crear, consultar, listar y eliminar cuentas bancarias
- ✅ **Operaciones Bancarias**: Depósitos y retiros con validaciones
- ✅ **Tipos de Cuenta**: Soporte para cuentas de ahorro (SAVINGS) y corriente (CHECKING)
- ✅ **Validación de Cliente**: Integración con servicios externos para validar clientes
- ✅ **Documentación OpenAPI**: Especificación completa con Swagger UI
- ✅ **Base de Datos NoSQL**: Persistencia con MongoDB
- ✅ **Calidad de Código**: Checkstyle, formateo automático y cobertura de pruebas

## 🛠️ Stack Tecnológico

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17 | Lenguaje de programación |
| Spring Boot | 3.3.5 | Framework de aplicación |
| Spring Data MongoDB | - | Acceso a datos |
| Spring WebFlux | - | Cliente reactivo |
| Lombok | 1.18.38 | Reducción de código boilerplate |
| SpringDoc OpenAPI | 2.5.0 | Documentación de API |
| Maven | - | Gestión de dependencias y construcción |

## 📁 Estructura del Proyecto

```
AccountMS/
├── src/main/java/org/taller01/accountms/
│   ├── controller/          # Controladores REST
│   │   └── AccountController.java
│   ├── service/            # Lógica de negocio
│   │   └── AccountService.java
│   ├── repository/         # Acceso a datos
│   │   └── AccountRepository.java
│   ├── domain/            # Entidades de dominio
│   │   ├── Account.java
│   │   └��─ AccountType.java
│   ├── dto/               # Objetos de transferencia
│   │   ├── request/       # DTOs de entrada
│   │   └── response/      # DTOs de salida
│   └── exception/         # Manejo de excepciones
├��─ src/main/resources/
│   ├── openapi/           # Especificación OpenAPI
│   ├── application.properties
│   └── checkstyle.xml
└── src/gen/openapi/       # Código generado por OpenAPI
```

## 🔌 API Endpoints

### 📋 Gestión de Cuentas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/cuentas` | Listar todas las cuentas |
| `GET` | `/cuentas/{id}` | Obtener cuenta por ID |
| `POST` | `/cuentas` | Crear nueva cuenta |
| `DELETE` | `/cuentas/{id}` | Eliminar cuenta |

### 💰 Operaciones Bancarias

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `PUT` | `/cuentas/{cuentaId}/depositar` | Realizar depósito |
| `PUT` | `/cuentas/{cuentaId}/retirar` | Realizar retiro |

### 📋 Ejemplos de Uso

#### Crear una Cuenta
```json
POST /cuentas
{
  "clientId": 12345,
  "type": "SAVINGS",
  "initialBalance": 1000.00
}
```

#### Realizar un Depósito
```json
PUT /cuentas/1/depositar
{
  "amount": 500.00
}
```

#### Realizar un Retiro
```json
PUT /cuentas/1/retirar
{
  "amount": 200.00
}
```

## 🗄️ Modelo de Datos

### Entidad Account
```java
{
  "id": "string",
  "clientId": "string",
  "accountNumber": "string", // Único
  "currency": "string",
  "balance": "BigDecimal",
  "active": "boolean",
  "type": "SAVINGS | CHECKING"
}
```

### Tipos de Cuenta
- **SAVINGS**: Cuenta de ahorros
- **CHECKING**: Cuenta corriente

## 🚀 Configuración y Ejecución

### Prerrequisitos
- Java 17+
- Maven 3.6+
- MongoDB 4.0+

### Instalación

1. **Clonar el repositorio**
```bash
git clone <repository-url>
cd AccountMS
```

2. **Configurar MongoDB**
```properties
# application.properties
spring.data.mongodb.uri=mongodb://localhost:27017/accountsdb
```

3. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8081`

### ��� Documentación de la API

- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8081/v3/api-docs

## 📐 Formateo de Código con Maven

Este proyecto utiliza el plugin `formatter-maven-plugin` para formatear automáticamente el código según el Google Java Style Guide.

### ✅ Archivos que se formatean

- Archivos fuente Java (`.java`)
- Archivos de configuración XML (`.xml`)
- Archivos de datos JSON (`.json`)
- Archivos YAML (`.yaml`, `.yml`)

### 🔧 Cómo aplicar el formateo

- **Formatear todos los archivos soportados:**
```bash
mvn formatter:format
```

- **Validar el formateo (útil antes de hacer commit):**
```bash
mvn formatter:validate
```

> El plugin está configurado para ejecutarse automáticamente durante la fase `validate` del ciclo de vida de Maven. Esto significa que el formateo se aplicará al ejecutar comandos como `mvn install`, `mvn verify`, o `mvn package`.

### 📁 Configuración de estilo
Las reglas de formateo están definidas en el archivo:
```
eclipse-java-google-style.xml
```

## 🧪 Calidad de Código

### Ejecutar Verificaciones
```bash
# Ejecutar todas las verificaciones
mvn verify

# Solo Checkstyle
mvn checkstyle:check

# Cobertura de pruebas
mvn jacoco:report
```

### 🔍 Herramientas de Calidad

- **Checkstyle**: Verificación de estilo de código
- **JaCoCo**: Cobertura de pruebas
- **Formatter**: Formateo automático según Google Style Guide

## 🤝 Contribución

1. Fork el proyecto
2. Crear una rama para la feature (`git checkout -b feature/AmazingFeature`)
3. Commit los cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## 📝 Notas de Desarrollo

- El proyecto utiliza **Lombok** para reducir el código boilerplate
- La documentación de la API se genera automáticamente con **OpenAPI 3.0**
- Se utiliza **MongoDB** como base de datos NoSQL para flexibilidad en el esquema
- El patrón de arquitectura sigue los principios de **Clean Architecture**
- Se implementa validación de cliente mediante llamadas a servicios externos

## 🔮 Roadmap

- [ ] Implementación de transacciones distribuidas
- [ ] Autenticación y autorización con JWT
- [ ] Métricas y monitoreo con Micrometer
- [ ] Containerización con Docker
- [ ] Despliegue en Kubernetes
