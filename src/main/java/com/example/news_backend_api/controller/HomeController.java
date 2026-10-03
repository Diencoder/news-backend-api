package com.example.news_backend_api.controller;

import com.example.news_backend_api.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<ApiResponse<Map<String, Object>>> home() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("application", "News Backend API");
        info.put("status", "Running");
        info.put("publicNewsUrl", "/api/public/news");
        info.put("categoriesUrl", "/api/public/categories");
        info.put("authLoginUrl", "/api/auth/login");
    
        return ResponseEntity.ok(ApiResponse.success("Chào mừng bạn đến với News Backend API!", info));
    }
}
