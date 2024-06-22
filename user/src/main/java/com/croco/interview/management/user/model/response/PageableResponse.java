package com.croco.interview.management.user.model.response;

import java.util.List;

public record PageableResponse<T>(
        Integer page,
        Integer size,
        Long total,
        List<T> content
) {
}
