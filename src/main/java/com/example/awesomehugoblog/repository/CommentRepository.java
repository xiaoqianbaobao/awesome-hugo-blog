package com.example.awesomehugoblog.repository;

import com.example.awesomehugoblog.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleIdAndParentIsNullOrderByCreatedAtAsc(Long articleId);
    
    @Query("SELECT c FROM Comment c WHERE c.parent.id = :parentId ORDER BY c.createdAt ASC")
    List<Comment> findRepliesByParentIdOrderByCreatedAtAsc(@Param("parentId") Long parentId);
    
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.article.id = :articleId")
    void deleteByArticleId(@Param("articleId") Long articleId);
    
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.parent.id = :parentId")
    void deleteByParentId(@Param("parentId") Long parentId);
    
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.article.id = :articleId AND c.parent IS NOT NULL")
    void deleteRepliesByArticleId(@Param("articleId") Long articleId);
}