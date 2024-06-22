package com.croco.interview.management.user.model.response;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record UserResponse(
        String identifier,
        String firstName,
        String lastName
) {
}
