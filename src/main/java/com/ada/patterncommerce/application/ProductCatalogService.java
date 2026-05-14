package com.ada.patterncommerce.application;

import com.ada.patterncommerce.domain.InventoryItem;
import com.ada.patterncommerce.domain.Product;
import com.ada.patterncommerce.infrastructure.repository.InventoryRepository;
import com.ada.patterncommerce.infrastructure.repository.ProductRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductCatalogService implements CatalogQueryService {
    private final ProductRepository products;
    private final InventoryRepository inventory;

    public ProductCatalogService(ProductRepository products, InventoryRepository inventory) {
        this.products = products;
        this.inventory = inventory;
    }

    @Override
    public ProductView getProduct(String sku) {
        Product product = products.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + sku));
        int available = inventory.findBySku(sku).map(InventoryItem::available).orElse(0);
        return new ProductView(product.sku(), product.name(), product.price(), available);
    }

    @Override
    public List<ProductView> listProducts() {
        return products.findAll().stream()
                .sorted(Comparator.comparing(Product::sku))
                .map(product -> getProduct(product.sku()))
                .toList();
    }
}
