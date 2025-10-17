package com.example.awesomehugoblog.service;

import com.example.awesomehugoblog.entity.Article;
import com.example.awesomehugoblog.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    
    @Autowired
    private ArticleRepository articleRepository;
    
    @Autowired
    private CommentService commentService;
    
    public List<Article> findAll() {
        return articleRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
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
    
    @Transactional
    public void deleteById(Long id) {
        // 先删除与文章相关的评论（包括回复）
        commentService.deleteCommentsByArticleId(id);
        // 再删除文章
        articleRepository.deleteById(id);
    }
    
    public List<Article> findByTag(String tag) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        return articleRepository.findByTagsContaining(tag, sort);
    }
    
    public List<Article> search(String keyword) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        return articleRepository.findByTitleContainingOrContentContaining(keyword, keyword, sort);
    }
    
    public List<Article> findByCategory(Long categoryId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        return articleRepository.findByCategoryId(categoryId, sort);
    }
}