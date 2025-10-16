package com.example.awesomehugoblog.controller;

import com.example.awesomehugoblog.entity.Article;
import com.example.awesomehugoblog.service.ArticleService;
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
    
    @GetMapping("/articles")
    public String list(Model model) {
        List<Article> articles = articleService.findAll();
        model.addAttribute("articles", articles);
        return "articles/list";
    }
    
    @GetMapping("/article/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id).orElse(null);
        model.addAttribute("article", article);
        return "articles/detail";
    }
    
    // Admin controllers
    @GetMapping("/admin/articles")
    public String adminList(Model model) {
        List<Article> articles = articleService.findAll();
        model.addAttribute("articles", articles);
        return "admin/articles/list";
    }
    
    @GetMapping("/admin/articles/new")
    public String newArticle(Model model) {
        model.addAttribute("article", new Article());
        return "admin/articles/edit";
    }
    
    @GetMapping("/admin/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id).orElse(null);
        model.addAttribute("article", article);
        return "admin/articles/edit";
    }
    
    @PostMapping("/admin/articles/save")
    public String save(@Valid @ModelAttribute Article article, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // 如果有验证错误，返回编辑页面并显示错误信息
            model.addAttribute("article", article);
            return "admin/articles/edit";
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