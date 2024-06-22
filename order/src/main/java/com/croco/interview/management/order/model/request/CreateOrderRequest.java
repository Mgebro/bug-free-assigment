package com.croco.interview.management.order.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateOrderRequest(
        @NotEmpty(message = "Product must not be empty or null!")
        String product,
        @NotEmpty(message = "Quantity must not be empty or null!")
        String quantity,
        @NotNull(message = "Price must not be null!")
        @DecimalMin(value = "0.0", message = "Price must be positive!")
        BigDecimal price
) {
}
