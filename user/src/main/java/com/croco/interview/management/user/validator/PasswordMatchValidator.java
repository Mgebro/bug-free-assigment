package com.croco.interview.management.user.validator;

import com.croco.interview.management.user.model.request.CreateUserRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, CreateUserRequest> {
    @Override
    public boolean isValid(CreateUserRequest createUserRequest, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.equals(createUserRequest.password(), createUserRequest.passwordConfirmation());
    }
}
