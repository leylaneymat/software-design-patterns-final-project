package com.ada.patterncommerce.application;

import com.ada.patterncommerce.domain.order.OrderLine;
import com.ada.patterncommerce.domain.pricing.PricingStrategyRegistry;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderPricingService {
    private final PricingStrategyRegistry strategies;

    public OrderPricingService(PricingStrategyRegistry strategies) {
        this.strategies = strategies;
    }

    public BigDecimal price(List<OrderLine> lines, String pricingCode) {
        return strategies.resolve(pricingCode).calculate(lines);
    }
}
