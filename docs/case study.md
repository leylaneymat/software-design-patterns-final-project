# Refactoring Case Study

## Legacy Problem

The imagined legacy version of this service placed catalog lookup, stock checks, payment selection, order persistence, supplier calls, and notifications inside a single `placeOrder` method. That method directly called a mock supplier API, used `if/else` blocks for payment types and discounts, and returned inconsistent errors. The result was tightly coupled code that was difficult to test and unsafe to extend.

## Refactoring Steps

1. **Extracted responsibilities by SRP.** Catalog reads, stock reservation, pricing, payment, supplier availability, and event publication became separate services.
2. **Replaced conditionals with patterns.** Discount logic became `PricingStrategy`; payment selection became `DefaultPaymentGatewayFactory`; order creation moved to `Order.Builder`.
3. **Protected external calls.** `SupplierClientAdapter` adapts the mock supplier contract and uses a Resilience4j Circuit Breaker with a fallback for small orders.
4. **Separated orchestration from details.** `OrderProcessingFacade` coordinates the use case, while `PlaceOrderCommand` encapsulates the executable business transaction.
5. **Added scalability boundaries.** `RedisProductCatalogProxy` implements cache-aside for product reads without changing the underlying `ProductCatalogService`.

## Result

The refactored design is open for new pricing rules, payment gateways, observers, and supplier integrations without editing the core order workflow. Each pattern removes a specific source of coupling rather than existing only for demonstration.
