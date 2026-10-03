package com.example.news_backend_api.controller;

import com.example.news_backend_api.common.ApiResponse;
import com.example.news_backend_api.dto.request.LoginRequest;
import com.example.news_backend_api.dto.request.RegisterRequest;
import com.example.news_backend_api.dto.response.AuthResponse;
import com.example.news_backend_api.dto.response.UserResponse;
import com.example.news_backend_api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(
            ApiResponse.created("User registered successfully", response),
            HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>(
                ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), "User is not authenticated"),
                HttpStatus.UNAUTHORIZED
            );
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        UserResponse response = authService.getCurrentUser(username);
        return ResponseEntity.ok(ApiResponse.success("User profile fetched successfully", response));
    }
}
