package com.ada.patterncommerce.domain.pricing;

import com.ada.patterncommerce.domain.order.OrderLine;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class StandardPricingStrategy implements PricingStrategy {
    @Override
    public BigDecimal calculate(List<OrderLine> lines) {
        return lines.stream()
                .map(OrderLine::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String name() {
        return "STANDARD";
    }
}
