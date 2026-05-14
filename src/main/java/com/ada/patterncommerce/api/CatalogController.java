package com.ada.patterncommerce.api;

import com.ada.patterncommerce.application.CatalogQueryService;
import com.ada.patterncommerce.application.ProductView;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class CatalogController {
    private final CatalogQueryService catalog;

    public CatalogController(CatalogQueryService catalog) {
        this.catalog = catalog;
    }

    @GetMapping
    List<ProductView> listProducts() {
        return catalog.listProducts();
    }

    @GetMapping("/{sku}")
    ProductView getProduct(@PathVariable String sku) {
        return catalog.getProduct(sku);
    }
}
