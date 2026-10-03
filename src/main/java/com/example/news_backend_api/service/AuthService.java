package com.example.news_backend_api.service;

import com.example.news_backend_api.dto.request.LoginRequest;
import com.example.news_backend_api.dto.request.RegisterRequest;
import com.example.news_backend_api.dto.response.AuthResponse;
import com.example.news_backend_api.dto.response.UserResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserResponse getCurrentUser(String username);
}
