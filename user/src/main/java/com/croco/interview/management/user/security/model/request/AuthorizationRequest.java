package com.croco.interview.management.user.security.model.request;

import jakarta.validation.constraints.NotEmpty;

public record AuthorizationRequest(
        @NotEmpty(message = "Cannot authorize without username!")
        String identifier,
        @NotEmpty(message = "Cannot authorize without password!")
        String password
) {
}
