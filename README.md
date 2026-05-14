# Pattern Commerce

Pattern Commerce is a Java 21 + Spring Boot backend for dynamic e-commerce inventory and order processing. It is built for the capstone requirements: GoF design patterns, SOLID boundaries, virtual threads, Redis cache-aside, and circuit-breaker resilience.

## Tech Stack

- Java 21
- Spring Boot 3.3
- Spring Web, Validation, Actuator
- Redis via Spring Data Redis
- Resilience4j Circuit Breaker
- Maven

## Run Locally

Start Redis:

```bash
docker compose up -d redis
```

Run the service with JDK 21:

```bash
mvn spring-boot:run
```

The service starts on `http://localhost:8080`.

## API Examples

List products:

```bash
curl http://localhost:8080/api/products
```

Read one product through the Redis cache-aside proxy:

```bash
curl http://localhost:8080/api/products/SKU-LAPTOP-14
```

Create an order:

```bash
curl -X POST http://localhost:8080/api/orders \
  -H 'Content-Type: application/json' \
  -d '{
    "customerId": "customer-1001",
    "paymentMethod": "CARD",
    "pricingCode": "SEASONAL10",
    "lines": [
      { "sku": "SKU-LAPTOP-14", "quantity": 1 },
      { "sku": "SKU-HEADSET-X", "quantity": 2 }
    ]
  }'
```

## Pattern Inventory

| Category | Pattern | Classes | Architectural Reason |
| --- | --- | --- | --- |
| Creational | Builder | `Order.Builder` | Creates valid immutable order snapshots while hiding construction details. |
| Creational | Factory Method | `PaymentGatewayFactory`, `DefaultPaymentGatewayFactory`, `CardPaymentGateway`, `WalletPaymentGateway` | Selects payment gateway implementations without coupling order flow to provider classes. |
| Structural | Proxy | `RedisProductCatalogProxy` | Adds Redis cache-aside behavior around catalog reads without changing `ProductCatalogService`. |
| Structural | Adapter | `SupplierClientAdapter`, `MockSupplierApi` | Converts the mock supplier API into the internal `SupplierClient` contract. |
| Structural | Decorator | `AuditingPaymentProcessorDecorator` | Adds payment audit logging around `GatewayPaymentProcessor` without changing payment logic. |
| Structural | Facade | `OrderProcessingFacade` | Gives controllers one simple order use-case entry point over several services. |
| Behavioral | Strategy | `PricingStrategy`, `StandardPricingStrategy`, `SeasonalPricingStrategy`, `PricingStrategyRegistry` | Supports new pricing rules without modifying order orchestration. |
| Behavioral | Command | `OrderCommand`, `PlaceOrderCommand` | Encapsulates order placement as an executable business transaction. |
| Behavioral | Observer | `DomainEventPublisher`, `DomainEventListener`, `OrderAuditObserver` | Decouples post-payment reactions from the order workflow. |

## SOLID Notes

- **SRP:** Controllers handle HTTP, repositories store data, services execute business behavior, and infrastructure classes isolate Redis/payment/supplier concerns.
- **OCP:** New pricing strategies, payment gateways, event observers, and supplier adapters can be added through new classes instead of rewriting the facade.
- **DIP:** Application services depend on abstractions such as `CatalogQueryService`, `PaymentProcessor`, `SupplierClient`, and `PricingStrategy`.

## Performance and Resilience

- **Virtual Threads:** `spring.threads.virtual.enabled=true` enables virtual-thread request handling in Spring Boot. `AsyncConfig` also exposes a virtual-thread task executor.
- **Redis Cache-Aside:** `RedisProductCatalogProxy` checks Redis first, loads from `ProductCatalogService` on a miss, then writes the result with a TTL.
- **Circuit Breaker:** `SupplierClientAdapter` uses `@CircuitBreaker(name = "supplierAvailability")` around mock supplier availability checks and falls back to allowing small orders.

## Deliverables

- Pattern inventory: this README section.
- Refactoring case study: [docs/REFACTORING_CASE_STUDY.md](docs/REFACTORING_CASE_STUDY.md).
- Architecture diagram: [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).
