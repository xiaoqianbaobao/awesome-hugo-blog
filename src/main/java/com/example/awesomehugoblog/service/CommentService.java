package com.example.awesomehugoblog.service;

import com.example.awesomehugoblog.entity.Article;
import com.example.awesomehugoblog.entity.Comment;
import com.example.awesomehugoblog.repository.ArticleRepository;
import com.example.awesomehugoblog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private ArticleRepository articleRepository;
    
    public List<Comment> getCommentsByArticleId(Long articleId) {
        List<Comment> comments = commentRepository.findByArticleIdAndParentIsNullOrderByCreatedAtAsc(articleId);
        // 为每个评论加载回复
        for (Comment comment : comments) {
            loadReplies(comment);
        }
        return comments;
    }
    
    private void loadReplies(Comment comment) {
        List<Comment> replies = commentRepository.findRepliesByParentIdOrderByCreatedAtAsc(comment.getId());
        comment.setReplies(replies);
        // 递归加载回复的回复
        for (Comment reply : replies) {
            loadReplies(reply);
        }
    }
    
    public Comment saveComment(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }
    
    public Comment addCommentToArticle(Long articleId, String nickname, String content) {
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        if (articleOptional.isPresent()) {
            Comment comment = new Comment();
            comment.setNickname(nickname);
            comment.setContent(content);
            comment.setArticle(articleOptional.get());
            return saveComment(comment);
        }
        return null;
    }
    
    public Comment addReplyToComment(Long articleId, Long parentId, String nickname, String content) {
        Optional<Article> articleOptional = articleRepository.findById(articleId);
        Optional<Comment> parentOptional = commentRepository.findById(parentId);
        
        if (articleOptional.isPresent() && parentOptional.isPresent()) {
            Comment reply = new Comment();
            reply.setNickname(nickname);
            reply.setContent(content);
            reply.setArticle(articleOptional.get());
            reply.setParent(parentOptional.get());
            return saveComment(reply);
        }
        return null;
    }
    
    @Transactional
    public void deleteCommentsByArticleId(Long articleId) {
        // 删除所有属于这篇文章的回复（有父评论的评论）
        commentRepository.deleteRepliesByArticleId(articleId);
        
        // 删除所有属于这篇文章的顶级评论（没有父评论的评论）
        commentRepository.deleteByArticleId(articleId);
    }
}