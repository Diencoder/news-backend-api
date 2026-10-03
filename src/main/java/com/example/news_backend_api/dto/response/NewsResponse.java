package com.example.news_backend_api.dto.response;

import com.example.news_backend_api.model.News;
import java.time.LocalDateTime;

public class NewsResponse {

    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String content;
    private String thumbnailUrl;
    private CategoryResponse category;
    private Long views;
    private Boolean published;
    private UserResponse author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public NewsResponse() {
    }

    public static NewsResponse fromEntity(News news) {
        if (news == null) return null;
        NewsResponse res = new NewsResponse();
        res.setId(news.getId());
        res.setTitle(news.getTitle());
        res.setSlug(news.getSlug());
        res.setSummary(news.getSummary());
        res.setContent(news.getContent());
        res.setThumbnailUrl(news.getThumbnailUrl());
        if (news.getCategory() != null) {
            res.setCategory(CategoryResponse.fromEntity(news.getCategory()));
        }
        res.setViews(news.getViews());
        res.setPublished(news.getPublished());
        if (news.getAuthor() != null) {
            res.setAuthor(UserResponse.fromEntity(news.getAuthor()));
        }
        res.setCreatedAt(news.getCreatedAt());
        res.setUpdatedAt(news.getUpdatedAt());
        return res;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public CategoryResponse getCategory() {
        return category;
    }

    public void setCategory(CategoryResponse category) {
        this.category = category;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public UserResponse getAuthor() {
        return author;
    }

    public void setAuthor(UserResponse author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
