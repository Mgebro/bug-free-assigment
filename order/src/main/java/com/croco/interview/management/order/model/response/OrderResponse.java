package com.croco.interview.management.order.model.response;

import com.croco.interview.management.order.model.enums.Status;
import lombok.Builder;

import java.math.BigDecimal;

@Builder(setterPrefix = "with")
public record OrderResponse(
        String product,
        String quantity,
        BigDecimal price,
        UserResponse user,
        Status status
) {
}
