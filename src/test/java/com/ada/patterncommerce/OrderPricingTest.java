package com.ada.patterncommerce;

import static org.assertj.core.api.Assertions.assertThat;

import com.ada.patterncommerce.domain.order.OrderLine;
import com.ada.patterncommerce.domain.pricing.SeasonalPricingStrategy;
import com.ada.patterncommerce.domain.pricing.StandardPricingStrategy;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderPricingTest {
    @Test
    void seasonalStrategyAppliesTenPercentDiscount() {
        List<OrderLine> lines = List.of(new OrderLine("SKU-1", 2, new BigDecimal("100.00")));

        assertThat(new StandardPricingStrategy().calculate(lines)).isEqualByComparingTo("200.00");
        assertThat(new SeasonalPricingStrategy().calculate(lines)).isEqualByComparingTo("180.00");
    }
}
