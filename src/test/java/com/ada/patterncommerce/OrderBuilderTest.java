package com.ada.patterncommerce;

import static org.assertj.core.api.Assertions.assertThat;

import com.ada.patterncommerce.domain.order.Order;
import com.ada.patterncommerce.domain.order.OrderLine;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class OrderBuilderTest {
    @Test
    void builderCreatesImmutableOrderSnapshot() {
        Order order = Order.builder("customer-1")
                .addLine(new OrderLine("SKU-1", 1, new BigDecimal("15.00")))
                .total(new BigDecimal("13.50"))
                .build();

        assertThat(order.customerId()).isEqualTo("customer-1");
        assertThat(order.lines()).hasSize(1);
        assertThat(order.total()).isEqualByComparingTo("13.50");
    }
}
