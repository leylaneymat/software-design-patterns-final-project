package com.ada.patterncommerce.api;

import com.ada.patterncommerce.domain.payment.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record PlaceOrderRequest(
        @NotBlank String customerId,
        @NotEmpty List<@Valid LineRequest> lines,
        @NotNull PaymentMethod paymentMethod,
        String pricingCode
) {
    public record LineRequest(@NotBlank String sku, @Positive int quantity) {
    }
}
