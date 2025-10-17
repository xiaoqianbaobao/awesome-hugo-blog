package com.example.awesomehugoblog.controller;

import com.example.awesomehugoblog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {
    
    @Autowired
    private ArticleService articleService;
    
    @GetMapping("/")
    public String index(Model model) {
        // 获取最新的几篇文章用于首页展示
        List<com.example.awesomehugoblog.entity.Article> latestArticles = articleService.findAll();
        model.addAttribute("latestArticles", latestArticles);
        return "index";
    }
    
    @GetMapping("/timeline")
    public String timeline() {
        return "timeline";
    }
    
    @GetMapping("/tags")
    public String tags() {
        return "tags";
    }
    
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    
    @GetMapping("/links")
    public String links() {
        return "links";
    }
}