package com.ada.patterncommerce.infrastructure.repository;

import com.ada.patterncommerce.domain.Product;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {
    private final ConcurrentHashMap<String, Product> products = new ConcurrentHashMap<>();

    public Product save(Product product) {
        products.put(product.sku(), product);
        return product;
    }

    public Optional<Product> findBySku(String sku) {
        return Optional.ofNullable(products.get(sku));
    }

    public Collection<Product> findAll() {
        return products.values();
    }
}
