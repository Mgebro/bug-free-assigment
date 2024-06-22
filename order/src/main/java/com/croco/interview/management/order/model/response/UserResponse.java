package com.croco.interview.management.order.model.response;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record UserResponse(
        String userIdentifier
) {
}
