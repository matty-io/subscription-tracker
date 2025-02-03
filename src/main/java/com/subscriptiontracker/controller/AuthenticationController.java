package com.subscriptiontracker.controller;

import com.subscriptiontracker.DTO.LoginRequest;
import com.subscriptiontracker.DTO.UserResponse;
import com.subscriptiontracker.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginRequest body) {
        authenticationService.login(request, response, body);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authenticationService.logout(request, response);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/me")
    public ResponseEntity<UserResponse> getSession(HttpServletRequest request) {
        return ResponseEntity.ok(authenticationService.getSession(request));
    }
}
