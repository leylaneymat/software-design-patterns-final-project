package com.ada.patterncommerce.infrastructure.cache;

import com.ada.patterncommerce.application.CatalogQueryService;
import com.ada.patterncommerce.application.ProductCatalogService;
import com.ada.patterncommerce.application.ProductView;
import java.time.Duration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Primary
@Service
public class RedisProductCatalogProxy implements CatalogQueryService {
    private static final Logger log = LoggerFactory.getLogger(RedisProductCatalogProxy.class);
    private static final Duration TTL = Duration.ofMinutes(5);

    private final ProductCatalogService delegate;
    private final RedisTemplate<String, Object> redis;

    public RedisProductCatalogProxy(ProductCatalogService delegate, RedisTemplate<String, Object> redis) {
        this.delegate = delegate;
        this.redis = redis;
    }

    @Override
    public ProductView getProduct(String sku) {
        String key = "catalog:product:" + sku;
        try {
            Object cached = redis.opsForValue().get(key);
            if (cached instanceof ProductView productView) {
                return productView;
            }
            ProductView loaded = delegate.getProduct(sku);
            redis.opsForValue().set(key, loaded, TTL);
            return loaded;
        } catch (RedisConnectionFailureException ex) {
            log.warn("Redis unavailable, reading catalog directly for sku={}", sku);
            return delegate.getProduct(sku);
        }
    }

    @Override
    public List<ProductView> listProducts() {
        return delegate.listProducts();
    }
}
