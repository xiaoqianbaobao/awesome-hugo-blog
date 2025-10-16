package com.example.awesomehugoblog.service;

import com.example.awesomehugoblog.entity.Article;
import com.example.awesomehugoblog.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    
    @Autowired
    private ArticleRepository articleRepository;
    
    public List<Article> findAll() {
        return articleRepository.findAll();
    }
    
    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }
    
    public Article save(Article article) {
        article.setUpdatedAt(java.time.LocalDateTime.now());
        if (article.getCreatedAt() == null) {
            article.setCreatedAt(java.time.LocalDateTime.now());
        }
        return articleRepository.save(article);
    }
    
    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }
    
    public List<Article> findByTag(String tag) {
        return articleRepository.findByTagsContaining(tag);
    }
    
    public List<Article> search(String keyword) {
        return articleRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }
}