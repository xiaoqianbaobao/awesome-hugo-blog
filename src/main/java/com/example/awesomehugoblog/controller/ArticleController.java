package com.example.awesomehugoblog.controller;

import com.example.awesomehugoblog.entity.Article;
import com.example.awesomehugoblog.entity.Category;
import com.example.awesomehugoblog.entity.Comment;
import com.example.awesomehugoblog.service.ArticleService;
import com.example.awesomehugoblog.service.CategoryService;
import com.example.awesomehugoblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ArticleController {
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private CommentService commentService;
    
    @GetMapping("/articles")
    public String list(Model model) {
        List<Article> articles = articleService.findAll();
        List<Category> categories = categoryService.findAll();
        model.addAttribute("articles", articles);
        model.addAttribute("categories", categories);
        return "articles/list";
    }
    
    @GetMapping("/articles/category/{categoryId}")
    public String listByCategory(@PathVariable Long categoryId, Model model) {
        List<Article> articles = articleService.findByCategory(categoryId);
        List<Category> categories = categoryService.findAll();
        Category currentCategory = categoryService.findById(categoryId).orElse(null);
        model.addAttribute("articles", articles);
        model.addAttribute("categories", categories);
        model.addAttribute("currentCategory", currentCategory);
        return "articles/list";
    }
    
    @GetMapping("/article/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id).orElse(null);
        List<Category> categories = categoryService.findAll();
        List<Comment> comments = commentService.getCommentsByArticleId(id);
        model.addAttribute("article", article);
        model.addAttribute("categories", categories);
        model.addAttribute("comments", comments);
        model.addAttribute("comment", new Comment()); // 用于表单绑定
        return "articles/detail";
    }
    
    @PostMapping("/article/{id}/comment")
    public String addComment(@PathVariable Long id, 
                            @RequestParam(required = false) String nickname,
                            @RequestParam String content) {
        commentService.addCommentToArticle(id, nickname, content);
        return "redirect:/article/" + id;
    }
    
    @PostMapping("/article/{id}/comment/{commentId}/reply")
    public String addReply(@PathVariable Long id,
                          @PathVariable Long commentId,
                          @RequestParam(required = false) String nickname,
                          @RequestParam String content) {
        commentService.addReplyToComment(id, commentId, nickname, content);
        return "redirect:/article/" + id;
    }
    
    // Admin controllers
    @GetMapping("/admin/articles")
    public String adminList(Model model) {
        List<Article> articles = articleService.findAll();
        List<Category> categories = categoryService.findAll();
        model.addAttribute("articles", articles);
        model.addAttribute("categories", categories);
        return "admin/articles/list";
    }
    
    @GetMapping("/admin/articles/new")
    public String newArticle(Model model) {
        model.addAttribute("article", new Article());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/articles/edit";
    }
    
    @GetMapping("/admin/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id).orElse(null);
        model.addAttribute("article", article);
        model.addAttribute("categories", categoryService.findAll());
        return "admin/articles/edit";
    }
    
    @PostMapping("/admin/articles/save")
    public String save(@Valid @ModelAttribute Article article, 
                      @RequestParam(required = false) Long categoryId,
                      BindingResult result, Model model) {
        if (result.hasErrors()) {
            // 如果有验证错误，返回编辑页面并显示错误信息
            model.addAttribute("article", article);
            model.addAttribute("categories", categoryService.findAll());
            return "admin/articles/edit";
        }
        
        // 设置分类
        if (categoryId != null) {
            Category category = categoryService.findById(categoryId).orElse(null);
            article.setCategory(category);
        }
        
        articleService.save(article);
        return "redirect:/admin/articles";
    }
    
    @PostMapping("/admin/articles/{id}/delete")
    public String delete(@PathVariable Long id) {
        articleService.deleteById(id);
        return "redirect:/admin/articles";
    }
}