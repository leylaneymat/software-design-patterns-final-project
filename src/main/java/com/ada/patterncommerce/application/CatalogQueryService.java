package com.ada.patterncommerce.application;

import java.util.List;

public interface CatalogQueryService {
    ProductView getProduct(String sku);

    List<ProductView> listProducts();
}
