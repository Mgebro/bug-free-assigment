package com.croco.interview.management.order.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateOrderRequest(
        String product,
        String quantity,
        BigDecimal price
) {
}
