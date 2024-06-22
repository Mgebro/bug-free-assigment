package com.croco.interview.management.user.security.controller;

import com.croco.interview.management.user.security.model.request.AuthorizationRequest;
import com.croco.interview.management.user.security.model.response.AuthorizationResponse;
import com.croco.interview.management.user.security.service.AuthorizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/public/authorize")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping
    @ResponseStatus(HttpStatus.FOUND)
    AuthorizationResponse authorize(@RequestBody @Valid AuthorizationRequest request) {
        return authorizationService.authorize(request);
    }

}
