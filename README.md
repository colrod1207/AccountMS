# 🏦 AccountMS – Microservicio de Cuentas

Este microservicio gestiona operaciones CRUD sobre cuentas bancarias y mantiene el saldo disponible, siguiendo una arquitectura contract-first basada en OpenAPI. Está construido con Spring Boot, MongoDB y herramientas de calidad integradas para asegurar consistencia y mantenibilidad. Soporta programación reactiva con Spring WebFlux.

---

## 🏷️ Badges

![Java](https://img.shields.io/badge/language-Java%2017-blue)
![Build](https://github.com/natalygiron/AccountMS/actions/workflows/maven.yml/badge.svg)
[![Swagger](https://img.shields.io/badge/docs-Swagger-blue?logo=swagger)](http://localhost:8081/swagger-ui/index.html)
<!-- ![License](https://img.shields.io/github/license/natalygiron/AccountMS) -->
---

## 📘 API Endpoints

| Método | Endpoint           | Descripción                          |
|--------|--------------------|--------------------------------------|
| POST   | `/accounts`        | Registrar nueva cuenta               |
| GET    | `/accounts`        | Listar todas las cuentas             |
| GET    | `/accounts/{id}`   | Obtener cuenta por ID                |
| PUT    | `/accounts/{id}`   | Actualizar cuenta por ID             |
| PATCH  | `/accounts/{id}`   | Actualizar parcialmente una cuenta   |
| DELETE | `/accounts/{id}`   | Eliminar cuenta por ID               |

📎 Documentación interactiva: [Swagger UI](http://localhost:8081/swagger-ui/index.html)

---

## 🧪 Checklist de calidad

Antes de hacer commit o crear un pull request:

- [x] Código formateado (`mvn formatter:format`)
- [x] Reglas de estilo validadas (`mvn checkstyle:check`)
- [x] Pruebas ejecutadas (`mvn test`)
- [x] Cobertura generada (`mvn jacoco:report`)
- [x] Documentación actualizada (`account-ms-openapi.yaml`)
- [x] Commit claro y descriptivo
- [x] Pull request creado (no push directo a `main`)

---

## 📥 Pull Request Template

> Este repositorio requiere que todos los cambios pasen por revisión vía Pull Request.

```markdown
# 📦 Pull Request – AccountMS

## ✅ Descripción del cambio
<!-- Explica brevemente qué se implementa o corrige -->

## 🔍 Checklist de calidad
- [ ] Código formateado (`mvn formatter:format`)
- [ ] Reglas de estilo validadas (`mvn checkstyle:check`)
- [ ] Pruebas ejecutadas (`mvn test`)
- [ ] Cobertura generada (`mvn jacoco:report`)
- [ ] Documentación actualizada (`account-ms-openapi.yaml`)
- [ ] Commit claro y descriptivo
- [ ] Rama actualizada con `main`
- [ ] Revisión solicitada

## 📎 Referencias
<!-- Enlace a ticket, historia de usuario o documentación relacionada -->

## 👥 Revisor(es) sugerido(s)
<!-- Menciona a quien debería revisar este PR -->
```
---
## 📦 Estructura del proyecto
```código
account-ms/
├── src/
│   ├── main/
│   │   ├── java/com/bootcamp/accountms/
│   │   │   ├── controller/
│   │   │   ├── dto/request/
│   │   │   ├── dto/response/
│   │   │   ├── domain/
│   │   │   ├── service/
│   │   │   └── api/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── checkstyle.xml
│   │       └── openapi/account-ms-openapi.yaml
├── pom.xml
└── .github/workflows/maven.yml
```
---

## 🚨 Errores estándar de la API

| Código | Tipo de error           | Descripción breve                           | Recomendación para el cliente       |
|----|-------------------------|---------------------------------------------|-------------------------------------|
| 400 | Bad Request             | Datos inválidos o faltantes en la solicitud | Verifica campos requeridos y formato |
| 404 | Not Found               | Cuenta no encontrado por ID                 | Asegúrate de que el ID exista       |
| 422 | Unprocessable Entity    | Datos válidos pero no procesables           | Revisa reglas de negocio            |
| 500 | Internal Server Error   | Error inesperado en el servidor             | Intenta nuevamente o contacta soporte |

📎 Todos los errores deben incluir un cuerpo JSON con estructura clara:

```
