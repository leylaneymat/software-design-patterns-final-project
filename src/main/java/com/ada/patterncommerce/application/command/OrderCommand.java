package com.ada.patterncommerce.application.command;

import com.ada.patterncommerce.application.OrderResponse;

public interface OrderCommand {
    OrderResponse execute();
}
