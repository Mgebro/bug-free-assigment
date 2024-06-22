package com.croco.interview.management.user.security.model.response;

public record AuthorizationResponse(
        String token,
        Long expiresIn
) {
}
