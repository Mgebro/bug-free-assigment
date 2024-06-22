package com.croco.interview.management.user.model.request;

import com.croco.interview.management.user.model.enums.Role;
import jakarta.validation.constraints.NotEmpty;

public record UpdateUserRequest(
        @NotEmpty(message = "Identifier should not be empty or null!")
        String identifier,
        @NotEmpty(message = "First name should not be empty or null!")
        String firstName,
        @NotEmpty(message = "Last name should not be empty or null!")
        String lastName,
        String password,
        Role role
) {
}
