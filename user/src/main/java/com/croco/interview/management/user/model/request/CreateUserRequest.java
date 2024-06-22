package com.croco.interview.management.user.model.request;

import com.croco.interview.management.user.validator.PasswordMatch;
import jakarta.validation.constraints.NotEmpty;

@PasswordMatch
public record CreateUserRequest(
        @NotEmpty(message = "Identifier should not be empty or null!")
        String identifier,
        @NotEmpty(message = "First name should not be empty or null!")
        String firstName,
        @NotEmpty(message = "Last name should not be empty or null!")
        String lastName,
        @NotEmpty(message = "Password should not be empty or null!")
        String password,
        @NotEmpty(message = "Password confirmation should not be empty or null")
        String passwordConfirmation
) {
}
