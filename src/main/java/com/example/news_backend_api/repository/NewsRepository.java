package com.example.news_backend_api.repository;

import com.example.news_backend_api.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    Optional<News> findBySlug(String slug);

    Optional<News> findByIdAndPublishedTrue(Long id);

    Optional<News> findBySlugAndPublishedTrue(String slug);

    Boolean existsBySlug(String slug);

    Page<News> findByPublishedTrue(Pageable pageable);

    Page<News> findByPublishedTrueAndCategoryId(Long categoryId, Pageable pageable);

    List<News> findTop5ByPublishedTrueOrderByViewsDesc();

    List<News> findTop5ByPublishedTrueOrderByCreatedAtDesc();

    @Query("SELECT n FROM News n WHERE n.published = true AND " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(n.summary) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:categoryId IS NULL OR n.category.id = :categoryId)")
    Page<News> searchPublicNews(@Param("keyword") String keyword,
                                @Param("categoryId") Long categoryId,
                                Pageable pageable);

    @Query("SELECT n FROM News n WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(n.summary) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:categoryId IS NULL OR n.category.id = :categoryId) AND " +
           "(:published IS NULL OR n.published = :published)")
    Page<News> searchAdminNews(@Param("keyword") String keyword,
                               @Param("categoryId") Long categoryId,
                               @Param("published") Boolean published,
                               Pageable pageable);
}
