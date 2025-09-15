# Principios SOLID en AccountMS
Actualizado: 2025-09-15

## S — Single Responsibility Principle
- **AccountController**: expone endpoints REST (capa interfaces).
- **AccountService**: reglas de negocio (capa application/domain según tu estructura).
- **AccountRepository**: persistencia (Spring Data).
- **AccountMapper**: conversión DTO ↔ Entidad.

**Beneficio:** menos acoplamiento, tests más simples.

## O — Open/Closed Principle
- Políticas de comisiones/límites como estrategias: `FeePolicy`, `LimitPolicy`, `ValidationPolicy`.
- **Extender** agregando nuevas implementaciones sin **modificar** código existente.

## L — Liskov Substitution Principle
- Jerarquías que cumplen contrato `Account` (p.ej., `SavingsAccount`, `CheckingAccount`) sin romper pre/postcondiciones.

## I — Interface Segregation Principle
- Separar contratos pequeños: `TransferService`, `DepositService`, `WithdrawalService` en lugar de una mega‑interfaz.

## D — Dependency Inversion Principle
- Depender de **interfaces** (puertos) como `NotificationPort`, `FraudCheckPort` e inyectar implementaciones vía Spring (`@Configuration/@Bean`).

---

## Evidencias en el código
- Controladores, servicios y repositorios separados por paquetes.
- Uso de inyección de dependencias y mocks en tests para demostrar DIP/ISP.
