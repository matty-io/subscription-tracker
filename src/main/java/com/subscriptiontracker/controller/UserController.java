package com.subscriptiontracker.controller;

import com.subscriptiontracker.DTO.CreateUserRequest;
import com.subscriptiontracker.DTO.UserResponse;
import com.subscriptiontracker.config.ApplicationProperties;
import com.subscriptiontracker.service.UserService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;
    private final ApplicationProperties applicationProperties;

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.create(request));
    }

    /**
     * Verify the email of the user, redirect to the login page.
     */
    @GetMapping("/verify-email")
    public RedirectView verifyEmail(@RequestParam String token) {
        userService.verifyEmail(token);
        return new RedirectView(applicationProperties.getLoginPageUrl());
    }
}
