package com.croco.interview.management.user.controller;

import com.croco.interview.management.user.model.request.CreateUserRequest;
import com.croco.interview.management.user.model.request.UpdateUserRequest;
import com.croco.interview.management.user.model.response.PageableResponse;
import com.croco.interview.management.user.model.response.UserResponse;
import com.croco.interview.management.user.model.response.UsersResponse;
import com.croco.interview.management.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/public/user")
    @ResponseStatus(HttpStatus.CREATED)
    void createUser(@RequestBody @Valid CreateUserRequest request) {
        userService.createUser(request);
    }

    @GetMapping("/user/list")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    PageableResponse<UsersResponse> getUsers(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size
    ) {
        return userService.getUsers(page, size);
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    UserResponse getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/user/me")
    UserResponse getMe(Principal principal) {
        return userService.getMe(principal.getName());
    }

    @PutMapping("/user/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateMe(Principal principal, @RequestBody @Valid UpdateUserRequest request) {
        userService.updateMe(principal.getName(), request);
    }

    @PutMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest request) {
        userService.updateUser(id, request);
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
