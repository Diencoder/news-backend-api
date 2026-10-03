package com.example.news_backend_api.controller;

import com.example.news_backend_api.common.ApiResponse;
import com.example.news_backend_api.common.AppConstants;
import com.example.news_backend_api.common.PagedResponse;
import com.example.news_backend_api.dto.response.CategoryResponse;
import com.example.news_backend_api.dto.response.NewsResponse;
import com.example.news_backend_api.service.CategoryService;
import com.example.news_backend_api.service.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicNewsController {

    private final NewsService newsService;
    private final CategoryService categoryService;

    public PublicNewsController(NewsService newsService, CategoryService categoryService) {
        this.newsService = newsService;
        this.categoryService = categoryService;
    }

    @GetMapping("/news")
    public ResponseEntity<ApiResponse<PagedResponse<NewsResponse>>> getAllNews(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int size,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "keyword", required = false) String keyword) {

        PagedResponse<NewsResponse> pagedResponse = newsService.getPublicNews(
            page, size, sortBy, sortDir, categoryId, keyword
        );
        return ResponseEntity.ok(ApiResponse.success("News fetched successfully", pagedResponse));
    }

    @GetMapping("/news/{id}")
    public ResponseEntity<ApiResponse<NewsResponse>> getNewsById(@PathVariable Long id) {
        NewsResponse response = newsService.getPublicNewsById(id);
        return ResponseEntity.ok(ApiResponse.success("News details fetched successfully", response));
    }

    @GetMapping("/news/slug/{slug}")
    public ResponseEntity<ApiResponse<NewsResponse>> getNewsBySlug(@PathVariable String slug) {
        NewsResponse response = newsService.getPublicNewsBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success("News details fetched successfully", response));
    }

    @GetMapping("/news/top")
    public ResponseEntity<ApiResponse<List<NewsResponse>>> getTopViewedNews() {
        List<NewsResponse> list = newsService.getTopViewedNews();
        return ResponseEntity.ok(ApiResponse.success("Top viewed news fetched successfully", list));
    }

    @GetMapping("/news/latest")
    public ResponseEntity<ApiResponse<List<NewsResponse>>> getLatestNews() {
        List<NewsResponse> list = newsService.getLatestNews();
        return ResponseEntity.ok(ApiResponse.success("Latest news fetched successfully", list));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> list = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success("Categories fetched successfully", list));
    }
}
