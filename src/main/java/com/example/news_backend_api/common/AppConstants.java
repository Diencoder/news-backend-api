package com.example.news_backend_api.common;

public final class AppConstants {

    private AppConstants() {
        // Prevent instantiation
    }

    // Pagination & Sorting Constants
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION = "desc";

    // Security & Auth Constants
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    // Roles
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    // File Upload Constants
    public static final String[] ALLOWED_FILE_EXTENSIONS = {
        ".jpg", ".jpeg", ".png", ".webp", ".gif", ".pdf", ".docx", ".txt"
    };
    public static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10MB
}
