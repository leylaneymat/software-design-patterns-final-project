package com.ada.patterncommerce.domain.pricing;

import com.ada.patterncommerce.domain.order.OrderLine;
import java.math.BigDecimal;
import java.util.List;

public interface PricingStrategy {
    BigDecimal calculate(List<OrderLine> lines);

    String name();
}
