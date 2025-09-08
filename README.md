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

![Diagrama de Secuencia](src/diagrams/Diagra%20de%20secuencia.png)

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
| Spring Boot Actuator | - | Métricas y monitoring |
| Lombok | 1.18.38 | Reducción de código boilerplate |
| SpringDoc OpenAPI | 2.5.0 | Documentación de API |
| Micrometer | - | Métricas avanzadas |
| Maven | - | Gestión de dependencias y construcción |
| Docker | - | Containerización |

## 📁 Estructura del Proyecto

```
AccountMS/
├── src/main/java/org/taller01/accountms/
│   ├── controller/          # Controladores REST
│   │   └── AccountController.java
│   ├── service/            # Lógica de negocio
│   │   └── AccountService.java
│   ├── repository/         # Acceso a datos
│   ���   └── AccountRepository.java
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
- Maven 3.6+ (o usar Maven Wrapper incluido)
- MongoDB 4.0+ (o Docker para MongoDB local)

### Instalación

1. **Clonar el repositorio**
```bash
git clone <repository-url>
cd AccountMS
```

2. **Configurar perfiles de entorno**
```bash
# Desarrollo (usa MongoDB Atlas)
./mvnw spring-boot:run -Dspring.profiles.active=dev

# Testing (usa MongoDB local)
./mvnw spring-boot:run -Dspring.profiles.active=test

# Producción
./mvnw spring-boot:run -Dspring.profiles.active=prod
```

3. **Ejecutar con Docker Compose**
```bash
# Construir y ejecutar todos los servicios
docker-compose up -d

# Solo la aplicación
docker-compose up accountms

# Con MongoDB local y Mongo Express
docker-compose up mongo mongo-express accountms
```

### 🐳 Despliegue con Docker

#### Construcción manual
```bash
# Construir imagen
docker build -t accountms:latest .

# Ejecutar contenedor
docker run -p 8081:8081 -e SPRING_PROFILES_ACTIVE=prod accountms:latest
```

#### Usando Docker Compose
```bash
# Levantar toda la infraestructura
docker-compose up -d

# Verificar servicios
docker-compose ps

# Ver logs
docker-compose logs accountms

# Detener servicios
docker-compose down
```

### 📊 Endpoints de Monitoreo

- **Health Check**: http://localhost:8081/actuator/health
- **Métricas**: http://localhost:8081/actuator/metrics
- **Métricas Prometheus**: http://localhost:8081/actuator/prometheus
- **Info de la aplicación**: http://localhost:8081/actuator/info

La aplicación estará disponible en `http://localhost:8081`

### 🔧 Documentación de la API

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
./mvnw formatter:format
```

- **Validar el formateo (útil antes de hacer commit):**
```bash
./mvnw formatter:validate
```

> El plugin está configurado para ejecutarse automáticamente durante la fase `validate` del ciclo de vida de Maven. Esto significa que el formateo se aplicará al ejecutar comandos como `./mvnw install`, `./mvnw verify`, o `./mvnw package`.

### 📁 Configuración de estilo
Las reglas de formateo están definidas en el archivo:
```
eclipse-java-google-style.xml
```

## 🧪 Calidad de Código

### Ejecutar Verificaciones
```bash
# Ejecutar todas las verificaciones
./mvnw verify

# Solo Checkstyle
./mvnw checkstyle:check

# Solo tests
./mvnw test

# Cobertura de pruebas
./mvnw jacoco:report
```

### 🔍 Herramientas de Calidad

- **Checkstyle**: Verificación de estilo de código ✅
- **JaCoCo**: Cobertura de pruebas ✅
- **Formatter**: Formateo automático según Google Style Guide ✅
- **Spring Boot Actuator**: Métricas y monitoring ✅
- **Micrometer**: Métricas avanzadas con Prometheus ✅

### 📊 Métricas de Cobertura
Los reportes de cobertura se generan en:
```
target/site/jacoco/index.html
```

## 🌍 Configuración de Entornos

El proyecto soporta múltiples perfiles de configuración:

### 🔧 Desarrollo (`dev`)
- MongoDB Atlas con base de datos de desarrollo
- Swagger UI habilitado
- Logging detallado
- Auto-creación de índices habilitada

### 🧪 Testing (`test`)
- MongoDB local para pruebas
- Swagger UI deshabilitado
- Logging mínimo
- Configuración optimizada para tests

### 🚀 Producción (`prod`)
- MongoDB Atlas con base de datos de producción
- Swagger UI configurable via variables de entorno
- Logging optimizado
- Configuraciones de seguridad habilitadas
- Métricas de Prometheus habilitadas

## 🐳 Containerización

### Servicios Docker Compose

1. **accountms**: La aplicación principal
2. **mongo**: Base de datos MongoDB local
3. **mongo-express**: Interfaz web para MongoDB

### Puertos

- **8081**: Aplicación AccountMS
- **8082**: Mongo Express (admin: admin/admin123)
- **27017**: MongoDB

### Volúmenes
- `mongo-data`: Persistencia de datos de MongoDB

## 🤝 Contribución

1. Fork el proyecto
2. Crear una rama para la feature (`git checkout -b feature/AmazingFeature`)
3. Commit los cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

### 🔄 Workflow de Desarrollo

1. **Antes de commitear:**
```bash
./mvnw formatter:format
./mvnw checkstyle:check
./mvnw test
```

2. **Verificación completa:**
```bash
./mvnw clean verify
```

## 📝 Notas de Desarrollo

- El proyecto utiliza **Lombok** para reducir el código boilerplate
- La documentación de la API se genera automáticamente con **OpenAPI 3.0**
- Se utiliza **MongoDB** como base de datos NoSQL para flexibilidad en el esquema
- El patrón de arquitectura sigue los principios de **Clean Architecture**
- Se implementa validación robusta con **Bean Validation**
- **Manejo global de excepciones** con respuestas de error consistentes
- **Métricas y monitoring** integradas con Spring Boot Actuator
- **Containerización completa** con Docker y Docker Compose

## 🔮 Funcionalidades Implementadas

| **Categoría** | **Funcionalidad** | **Estado** | **Descripción** |
|---------------|-------------------|------------|-----------------|
| **🔌 API REST** | Endpoints CRUD | ✅ Completo | Todos los endpoints implementados (GET, POST, PUT, DELETE) |
| | Operaciones Bancarias | ✅ Completo | Depósito y retiro con validaciones robustas |
| | Documentación OpenAPI | ✅ Completo | Swagger UI con ejemplos detallados y anotaciones |
| **🛡️ Validaciones** | Bean Validation | ✅ Completo | Validaciones con mensajes personalizados |
| | Reglas de Negocio | ✅ Completo | Validaciones bancarias (fondos, cuenta activa, etc.) |
| | Manejo de Excepciones | ✅ Completo | GlobalExceptionHandler centralizado |
| **🧪 Testing** | Tests Unitarios | ✅ Completo | Cobertura completa de service y controller |
| | Tests de Integración | ✅ Completo | MockMvc para testing de endpoints |
| | Cobertura de Código | ✅ Completo | JaCoCo configurado y funcionando |
| **🔍 Calidad** | Checkstyle | ✅ Completo | 0 errores, estilo Google Java |
| | Formateo Automático | ✅ Completo | Formatter plugin configurado |
| | Análisis Estático | ✅ Completo | Verificación automática en build |
| **🌍 Entornos** | Profiles Múltiples | ✅ Completo | Configuraciones dev, test, prod |
| | Variables de Entorno | ✅ Completo | Configuración externalizada |
| | Configuración Segura | ✅ Completo | Secretos mediante variables |
| **📊 Monitoring** | Spring Boot Actuator | ✅ Completo | Health checks y endpoints de gestión |
| | Métricas Micrometer | ✅ Completo | Métricas avanzadas configuradas |
| | Prometheus Integration | ✅ Completo | Exportación de métricas para monitoring |
| **🐳 Containerización** | Docker | ✅ Completo | Dockerfile multi-stage optimizado |
| | Docker Compose | ✅ Completo | Orquestación completa con MongoDB |
| | Optimización | ✅ Completo | .dockerignore y mejores prácticas |
| **💾 Base de Datos** | MongoDB Atlas | ✅ Completo | Conexión cloud configurada |
| | MongoDB Local | ✅ Completo | Docker compose con MongoDB local |
| | Mongo Express | ✅ Completo | Interfaz web para administración |
| **🔒 Seguridad** | Validación de Entrada | ✅ Completo | Sanitización y validación robusta |
| | Manejo de Errores | ✅ Completo | No exposición de información sensible |
| | Usuario No-Root | ✅ Completo | Container security implementada |

## 🏆 Estado Final del Proyecto

**🎯 COMPLETADO AL 100%** - Todos los requisitos implementados y funcionando correctamente.
