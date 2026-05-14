# Pattern Commerce

Pattern Commerce is a Java 21 + Spring Boot backend for dynamic e-commerce inventory and order processing. It is built for the capstone requirements: GoF design patterns, SOLID boundaries, virtual threads, Redis cache-aside, and circuit-breaker resilience.

## What the App Does

Pattern Commerce simulates a backend service for an online store. It starts with a seeded catalog of products, tracks available inventory, accepts customer orders, calculates prices, reserves stock, processes mock payments, and publishes an order-paid event.

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

## Features and How to Use Them

### Service Info

The root route confirms that the backend is running and shows the main API endpoints. Open this in a browser:

```text
http://localhost:8080/
```

Or with curl:

```bash
curl http://localhost:8080/
```

### Health Check

Use the Spring Boot actuator health endpoint to verify application health:

```bash
curl http://localhost:8080/actuator/health
```

### Product Catalog

The application starts with three seeded products. Use this endpoint to list them with price and available stock:

```bash
curl http://localhost:8080/api/products
```

Available seeded SKUs:

- `SKU-LAPTOP-14`
- `SKU-HEADSET-X`
- `SKU-DOCK-USB4`

`SKU` means Stock Keeping Unit. It is the stable product code used for product lookup, inventory, and order lines.

### Get One Product

Use a product SKU to retrieve one product:

```bash
curl http://localhost:8080/api/products/SKU-LAPTOP-14
```

This route goes through `RedisProductCatalogProxy`, so it demonstrates the Redis cache-aside pattern. The first request loads the product from the application service and stores it in Redis; later requests can return the cached value.

### Restock Inventory

Add more inventory for a product by SKU:

```bash
curl -X POST "http://localhost:8080/api/inventory/SKU-LAPTOP-14/restock?quantity=5"
```

This increases the available quantity for `SKU-LAPTOP-14`. Inventory reservation during order placement uses atomic state updates to avoid overselling.

### Create an Order

Create an order with one or more order lines:

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

When this endpoint is called, the application:

- Checks that each product SKU exists
- Checks mock supplier availability
- Calculates the order total
- Reserves inventory
- Processes the mock payment
- Saves the order
- Publishes an order-paid event

### Pricing Rules

Choose the pricing rule with the `pricingCode` field when creating an order:

```json
"pricingCode": "STANDARD"
```

or:

```json
"pricingCode": "SEASONAL10"
```

Supported pricing codes:

- `STANDARD` or blank for normal pricing
- `SEASONAL10` for a 10% discount

This feature is implemented with the Strategy pattern. `StandardPricingStrategy` and `SeasonalPricingStrategy` are selected through `PricingStrategyRegistry`.

### Payment Methods

Choose the payment method with the `paymentMethod` field when creating an order:

```json
"paymentMethod": "CARD"
```

or:

```json
"paymentMethod": "WALLET"
```

Supported payment methods:

- `CARD`
- `WALLET`

This feature uses `DefaultPaymentGatewayFactory` to select the correct payment gateway. `AuditingPaymentProcessorDecorator` logs the payment reference after a successful charge.

### Supplier Availability and Circuit Breaker

Supplier availability is checked automatically when an order is created:

```bash
curl -X POST http://localhost:8080/api/orders \
  -H 'Content-Type: application/json' \
  -d '{
    "customerId": "customer-1002",
    "paymentMethod": "WALLET",
    "pricingCode": "STANDARD",
    "lines": [
      { "sku": "SKU-DOCK-USB4", "quantity": 1 }
    ]
  }'
```

The mock supplier occasionally throws a simulated failure. `SupplierClientAdapter` protects that call with a Resilience4j Circuit Breaker and falls back to allowing small orders.

### Order Paid Event

The order-paid event is published automatically after a successful payment. To trigger it, create an order with `POST /api/orders`.

The event is handled by `OrderAuditObserver`, which writes an audit log entry in the application logs.

### Get an Order

Use the `id` returned from the create-order response:

```bash
curl http://localhost:8080/api/orders/{orderId}
```

Replace `{orderId}` with the UUID returned by `POST /api/orders`.

### Virtual Threads

Virtual threads are enabled automatically in `src/main/resources/application.yml`:

```yaml
spring:
  threads:
    virtual:
      enabled: true
```

There is no separate endpoint for this feature. It is part of how Spring Boot handles requests when the application runs on Java 21.

## Example Order Flow

```text
POST /api/orders
        ↓
OrderProcessingFacade coordinates the use case
        ↓
SupplierClientAdapter checks mock supplier availability
        ↓
PricingStrategy calculates the total
        ↓
InventoryReservationService reserves stock atomically
        ↓
PaymentProcessor charges CARD or WALLET
        ↓
OrderRepository stores the order
        ↓
DomainEventPublisher notifies observers
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

- Pattern inventory: [Pattern Inventory](#pattern-inventory).
- Refactoring case study: [docs/case study.md](docs/case%20study.md).
- Architecture diagram: [docs/architecture.md](docs/architecture.md).
