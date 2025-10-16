package com.example.awesomehugoblog.repository;

import com.example.awesomehugoblog.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByTagsContaining(String tag);
    List<Article> findByTitleContainingOrContentContaining(String title, String content);
}