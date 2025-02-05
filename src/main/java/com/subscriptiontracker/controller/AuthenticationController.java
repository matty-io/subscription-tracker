package com.subscriptiontracker.controller;

import com.subscriptiontracker.DTO.LoginRequest;
import com.subscriptiontracker.DTO.UserResponse;
import com.subscriptiontracker.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/login/{provider}")
    public void initiateOAuthLogin(@PathVariable String provider, HttpServletResponse response) throws IOException {
        ClientRegistration registration = clientRegistrationRepository.findByRegistrationId(provider);

        if (registration == null) {
            response.sendRedirect("/login?error=invalid_provider"); // Redirect with error
            return;
        }

        String authorizationUrl = buildAuthorizationUrl(registration);

        response.sendRedirect(authorizationUrl);
    }

    private String buildAuthorizationUrl(ClientRegistration registration) {
        return String.format("%s?response_type=code&client_id=%s&redirect_uri=%s&scope=%s",
                registration.getProviderDetails().getAuthorizationUri(),
                registration.getClientId(),
                registration.getRedirectUri(), // Ensure this is correctly configured
                String.join(",", registration.getScopes())
        );
    }

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
