package com.example.news_backend_api.service;

import com.example.news_backend_api.common.PagedResponse;
import com.example.news_backend_api.dto.request.NewsRequest;
import com.example.news_backend_api.dto.response.NewsResponse;

import java.util.List;

public interface NewsService {

    // Public APIs
    PagedResponse<NewsResponse> getPublicNews(int page, int size, String sortBy, String sortDir, Long categoryId, String keyword);

    NewsResponse getPublicNewsById(Long id);

    NewsResponse getPublicNewsBySlug(String slug);

    List<NewsResponse> getTopViewedNews();

    List<NewsResponse> getLatestNews();

    // Private / Admin APIs (CRUD, Search, Sort)
    NewsResponse createNews(NewsRequest request, String currentUsername);

    NewsResponse updateNews(Long id, NewsRequest request, String currentUsername);

    void deleteNews(Long id, String currentUsername);

    PagedResponse<NewsResponse> getAdminNews(String keyword, Long categoryId, Boolean published,
                                            int page, int size, String sortBy, String sortDir);

    NewsResponse getAdminNewsById(Long id);
}
