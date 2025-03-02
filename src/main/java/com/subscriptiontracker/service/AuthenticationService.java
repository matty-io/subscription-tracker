package com.subscriptiontracker.service;

import com.subscriptiontracker.DTO.LoginRequest;
import com.subscriptiontracker.DTO.UserResponse;
import com.subscriptiontracker.model.User;
import com.subscriptiontracker.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    public void login(HttpServletRequest request, HttpServletResponse response, LoginRequest body) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();
        SecurityContext context = strategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextRepository.saveContext(context, request, response);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.logoutHandler.logout(request, response, authentication);
    }

    @Transactional
    public UserResponse getSession(HttpServletRequest request) {
        User user = SecurityUtil.getAuthenticatedUser();
        return new UserResponse(user);
    }
}
