# Patrones de Diseño aplicados/propuestos
Actualizado: 2025-09-15

- **Repository (Spring Data):** aísla persistencia de la lógica de negocio.
- **Strategy:** `FeePolicy`, `LimitPolicy`, `ValidationPolicy` para reglas intercambiables.
- **Factory Method / Abstract Factory:** `AccountFactory` para crear cuentas por tipo.
- **Builder:** construcción de DTOs de respuesta evitando telescoping constructors.
- **Template Method:** flujo general de operaciones de cuenta con hooks por tipo.
- **Adapter:** integrar servicios externos (Clientes/KYC) adaptando modelos externos a internos.
- **Singleton (por Spring Beans):** clientes HTTP (`WebClient`) definidos una sola vez.

## Trazabilidad
- Mapear cada patrón a clases concretas en `interfaces/`, `application/`, `domain/`, `infrastructure/`.
