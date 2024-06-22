package com.croco.interview.management.user.controller;

import com.croco.interview.management.user.model.request.CreateUserRequest;
import com.croco.interview.management.user.model.request.UpdateUserRequest;
import com.croco.interview.management.user.model.response.PageableResponse;
import com.croco.interview.management.user.model.response.UserResponse;
import com.croco.interview.management.user.model.response.UsersResponse;
import com.croco.interview.management.user.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createUser(@RequestBody @Valid CreateUserRequest request) {
        userService.createUser(request);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    @SecurityRequirement(name = "Authorization")
    PageableResponse<UsersResponse> getUsers(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size
    ) {
        return userService.getUsers(page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Authorization")
    UserResponse getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/me")
    UserResponse getMe(Principal principal) {
        return userService.getMe(principal.getName());
    }

    @PutMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateMe(Principal principal, @RequestBody @Valid UpdateUserRequest request) {
        userService.updateMe(principal.getName(), request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest request) {
        userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
