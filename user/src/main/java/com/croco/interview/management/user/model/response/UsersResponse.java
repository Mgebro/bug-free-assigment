package com.croco.interview.management.user.model.response;

import com.croco.interview.management.user.model.enums.Role;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record UsersResponse(
        Long id,
        String identifier,
        Role role,
        String firstName,
        String lastName
) {
}
