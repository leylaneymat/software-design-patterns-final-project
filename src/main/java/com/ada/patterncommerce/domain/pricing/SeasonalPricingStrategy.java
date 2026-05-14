package com.ada.patterncommerce.domain.pricing;

import com.ada.patterncommerce.domain.order.OrderLine;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SeasonalPricingStrategy implements PricingStrategy {
    private static final BigDecimal DISCOUNT = new BigDecimal("0.90");

    @Override
    public BigDecimal calculate(List<OrderLine> lines) {
        BigDecimal subtotal = lines.stream()
                .map(OrderLine::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return subtotal.multiply(DISCOUNT).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String name() {
        return "SEASONAL10";
    }
}
