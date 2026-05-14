package com.ada.patterncommerce;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ada.patterncommerce.application.InventoryReservationService;
import com.ada.patterncommerce.domain.order.OrderLine;
import com.ada.patterncommerce.infrastructure.repository.InventoryRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class InventoryReservationServiceTest {
    @Test
    void rollsBackEarlierReservationsWhenLaterLineFails() {
        InventoryRepository inventory = new InventoryRepository();
        inventory.restock("SKU-1", 2);
        inventory.restock("SKU-2", 1);
        InventoryReservationService service = new InventoryReservationService(inventory);

        List<OrderLine> lines = List.of(
                new OrderLine("SKU-1", 2, BigDecimal.TEN),
                new OrderLine("SKU-2", 2, BigDecimal.TEN)
        );

        assertThatThrownBy(() -> service.reserve(lines))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Insufficient stock");

        assertThat(inventory.findBySku("SKU-1").orElseThrow().available()).isEqualTo(2);
        assertThat(inventory.findBySku("SKU-2").orElseThrow().available()).isEqualTo(1);
    }
}
