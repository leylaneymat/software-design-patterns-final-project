package com.ada.patterncommerce;

import com.ada.patterncommerce.domain.Product;
import com.ada.patterncommerce.infrastructure.repository.InventoryRepository;
import com.ada.patterncommerce.infrastructure.repository.ProductRepository;
import java.math.BigDecimal;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PatternCommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatternCommerceApplication.class, args);
    }

    @Bean
    ApplicationRunner seedCatalog(ProductRepository products, InventoryRepository inventory) {
        return args -> {
            products.save(new Product("SKU-LAPTOP-14", "Aster 14 Pro Laptop", new BigDecimal("1499.00")));
            products.save(new Product("SKU-HEADSET-X", "ClearCall Wireless Headset", new BigDecimal("159.00")));
            products.save(new Product("SKU-DOCK-USB4", "USB4 Productivity Dock", new BigDecimal("229.00")));

            inventory.restock("SKU-LAPTOP-14", 18);
            inventory.restock("SKU-HEADSET-X", 80);
            inventory.restock("SKU-DOCK-USB4", 35);
        };
    }
}
