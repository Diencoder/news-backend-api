package com.example.news_backend_api.service.impl;

import com.example.news_backend_api.common.PagedResponse;
import com.example.news_backend_api.common.SlugUtils;
import com.example.news_backend_api.dto.request.NewsRequest;
import com.example.news_backend_api.dto.response.NewsResponse;
import com.example.news_backend_api.exception.BadRequestException;
import com.example.news_backend_api.exception.ResourceNotFoundException;
import com.example.news_backend_api.model.Category;
import com.example.news_backend_api.model.News;
import com.example.news_backend_api.model.Role;
import com.example.news_backend_api.model.User;
import com.example.news_backend_api.repository.CategoryRepository;
import com.example.news_backend_api.repository.NewsRepository;
import com.example.news_backend_api.repository.UserRepository;
import com.example.news_backend_api.service.NewsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public NewsServiceImpl(NewsRepository newsRepository,
                           CategoryRepository categoryRepository,
                           UserRepository userRepository) {
        this.newsRepository = newsRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    private Sort createSort(String sortBy, String sortDir) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortProperty = (sortBy == null || sortBy.trim().isEmpty()) ? "createdAt" : sortBy;
        return Sort.by(direction, sortProperty);
    }

    private String generateUniqueSlug(String title, Long currentId) {
        String baseSlug = SlugUtils.toSlug(title);
        if (baseSlug.isEmpty()) {
            baseSlug = "tin-tuc";
        }
        String slug = baseSlug;
        int counter = 1;

        while (true) {
            var existing = newsRepository.findBySlug(slug);
            if (existing.isEmpty() || (currentId != null && existing.get().getId().equals(currentId))) {
                break;
            }
            slug = baseSlug + "-" + counter++;
        }
        return slug;
    }

    // ==========================================
    // Public APIs
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<NewsResponse> getPublicNews(int page, int size, String sortBy, String sortDir,
                                                    Long categoryId, String keyword) {
        Pageable pageable = PageRequest.of(page, size, createSort(sortBy, sortDir));
        Page<News> newsPage = newsRepository.searchPublicNews(keyword, categoryId, pageable);

        List<NewsResponse> content = newsPage.getContent().stream()
            .map(NewsResponse::fromEntity)
            .collect(Collectors.toList());

        return new PagedResponse<>(
            content,
            newsPage.getNumber(),
            newsPage.getSize(),
            newsPage.getTotalElements(),
            newsPage.getTotalPages(),
            newsPage.isLast()
        );
    }

    @Override
    @Transactional
    public NewsResponse getPublicNewsById(Long id) {
        News news = newsRepository.findByIdAndPublishedTrue(id)
            .orElseThrow(() -> new ResourceNotFoundException("News", "id", id));

        // Increment view count
        news.setViews(news.getViews() + 1);
        newsRepository.save(news);

        return NewsResponse.fromEntity(news);
    }

    @Override
    @Transactional
    public NewsResponse getPublicNewsBySlug(String slug) {
        News news = newsRepository.findBySlugAndPublishedTrue(slug)
            .orElseThrow(() -> new ResourceNotFoundException("News", "slug", slug));

        // Increment view count
        news.setViews(news.getViews() + 1);
        newsRepository.save(news);

        return NewsResponse.fromEntity(news);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponse> getTopViewedNews() {
        return newsRepository.findTop5ByPublishedTrueOrderByViewsDesc().stream()
            .map(NewsResponse::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponse> getLatestNews() {
        return newsRepository.findTop5ByPublishedTrueOrderByCreatedAtDesc().stream()
            .map(NewsResponse::fromEntity)
            .collect(Collectors.toList());
    }

    // ==========================================
    // Private / Admin APIs
    // ==========================================

    @Override
    @Transactional
    public NewsResponse createNews(NewsRequest request, String currentUsername) {
        User author = userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUsername));

        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        News news = new News();
        news.setTitle(request.getTitle());
        news.setSlug(generateUniqueSlug(request.getTitle(), null));
        news.setSummary(request.getSummary());
        news.setContent(request.getContent());
        news.setThumbnailUrl(request.getThumbnailUrl());
        news.setCategory(category);
        news.setPublished(request.getPublished() != null ? request.getPublished() : true);
        news.setAuthor(author);

        News saved = newsRepository.save(news);
        return NewsResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public NewsResponse updateNews(Long id, NewsRequest request, String currentUsername) {
        News news = newsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("News", "id", id));

        User currentUser = userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUsername));

        // Allow update if admin or the original author
        if (currentUser.getRole() != Role.ROLE_ADMIN &&
            (news.getAuthor() == null || !news.getAuthor().getId().equals(currentUser.getId()))) {
            throw new BadRequestException("You do not have permission to edit this article");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));

        if (!news.getTitle().equalsIgnoreCase(request.getTitle())) {
            news.setSlug(generateUniqueSlug(request.getTitle(), id));
        }

        news.setTitle(request.getTitle());
        news.setSummary(request.getSummary());
        news.setContent(request.getContent());
        news.setThumbnailUrl(request.getThumbnailUrl());
        news.setCategory(category);
        if (request.getPublished() != null) {
            news.setPublished(request.getPublished());
        }

        News updated = newsRepository.save(news);
        return NewsResponse.fromEntity(updated);
    }

    @Override
    @Transactional
    public void deleteNews(Long id, String currentUsername) {
        News news = newsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("News", "id", id));

        User currentUser = userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUsername));

        // Allow deletion if admin or the original author
        if (currentUser.getRole() != Role.ROLE_ADMIN &&
            (news.getAuthor() == null || !news.getAuthor().getId().equals(currentUser.getId()))) {
            throw new BadRequestException("You do not have permission to delete this article");
        }

        newsRepository.delete(news);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<NewsResponse> getAdminNews(String keyword, Long categoryId, Boolean published,
                                                   int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, createSort(sortBy, sortDir));
        Page<News> newsPage = newsRepository.searchAdminNews(keyword, categoryId, published, pageable);

        List<NewsResponse> content = newsPage.getContent().stream()
            .map(NewsResponse::fromEntity)
            .collect(Collectors.toList());

        return new PagedResponse<>(
            content,
            newsPage.getNumber(),
            newsPage.getSize(),
            newsPage.getTotalElements(),
            newsPage.getTotalPages(),
            newsPage.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public NewsResponse getAdminNewsById(Long id) {
        News news = newsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("News", "id", id));
        return NewsResponse.fromEntity(news);
    }
}
