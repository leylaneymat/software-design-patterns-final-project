package com.ada.patterncommerce;

import static org.assertj.core.api.Assertions.assertThat;

import com.ada.patterncommerce.domain.InventoryItem;
import org.junit.jupiter.api.Test;

class InventoryItemTest {
    @Test
    void reserveUsesAtomicCompareAndSet() {
        InventoryItem item = new InventoryItem("SKU-1", 3);

        assertThat(item.reserve(2)).isTrue();
        assertThat(item.reserve(2)).isFalse();
        assertThat(item.available()).isEqualTo(1);
    }
}
