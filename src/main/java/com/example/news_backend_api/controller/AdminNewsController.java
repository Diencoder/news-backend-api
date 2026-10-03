package com.example.news_backend_api.controller;

import com.example.news_backend_api.common.ApiResponse;
import com.example.news_backend_api.common.AppConstants;
import com.example.news_backend_api.common.PagedResponse;
import com.example.news_backend_api.dto.request.NewsRequest;
import com.example.news_backend_api.dto.response.NewsResponse;
import com.example.news_backend_api.service.NewsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/news")
public class AdminNewsController {

    private final NewsService newsService;

    public AdminNewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NewsResponse>> createNews(@Valid @RequestBody NewsRequest request,
                                                               Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        NewsResponse response = newsService.createNews(request, username);
        return new ResponseEntity<>(
            ApiResponse.created("News article created successfully", response),
            HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NewsResponse>> updateNews(@PathVariable Long id,
                                                               @Valid @RequestBody NewsRequest request,
                                                               Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        NewsResponse response = newsService.updateNews(id, request, username);
        return ResponseEntity.ok(ApiResponse.success("News article updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNews(@PathVariable Long id,
                                                        Authentication authentication) {
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        newsService.deleteNews(id, username);
        return ResponseEntity.ok(ApiResponse.success("News article deleted successfully", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<NewsResponse>>> getAllNews(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "published", required = false) Boolean published,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int size,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {

        PagedResponse<NewsResponse> response = newsService.getAdminNews(
            keyword, categoryId, published, page, size, sortBy, sortDir
        );
        return ResponseEntity.ok(ApiResponse.success("News articles fetched successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NewsResponse>> getNewsById(@PathVariable Long id) {
        NewsResponse response = newsService.getAdminNewsById(id);
        return ResponseEntity.ok(ApiResponse.success("News article fetched successfully", response));
    }
}
