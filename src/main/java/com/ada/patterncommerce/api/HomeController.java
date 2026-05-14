package com.ada.patterncommerce.api;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    Map<String, Object> home() {
        return Map.of(
                "service", "Pattern Commerce",
                "status", "running",
                "endpoints", List.of(
                        "GET /api/products",
                        "GET /api/products/{sku}",
                        "POST /api/orders",
                        "POST /api/inventory/{sku}/restock?quantity=5",
                        "GET /actuator/health"
                )
        );
    }
}
