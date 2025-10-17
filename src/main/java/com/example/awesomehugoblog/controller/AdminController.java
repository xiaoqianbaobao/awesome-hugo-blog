package com.example.awesomehugoblog.controller;

import com.example.awesomehugoblog.entity.Article;
import com.example.awesomehugoblog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public String admin(Model model) {
        // 获取所有文章用于统计
        List<Article> articles = articleService.findAll();
        
        // 计算文章总数
        long articleCount = articles.size();
        
        // 提取所有标签并统计
        Map<String, Integer> tagCountMap = new HashMap<>();
        for (Article article : articles) {
            if (article.getTags() != null && !article.getTags().trim().isEmpty()) {
                String[] tags = article.getTags().split(",");
                for (String tag : tags) {
                    String trimmedTag = tag.trim();
                    if (!trimmedTag.isEmpty()) {
                        tagCountMap.put(trimmedTag, tagCountMap.getOrDefault(trimmedTag, 0) + 1);
                    }
                }
            }
        }
        
        model.addAttribute("articleCount", articleCount);
        model.addAttribute("tagCount", tagCountMap.size());
        model.addAttribute("tags", tagCountMap);
        
        return "admin/index";
    }

    @GetMapping("/tags")
    public String tags(Model model) {
        // 获取所有文章
        List<Article> articles = articleService.findAll();
        
        // 提取所有标签并统计
        Map<String, Integer> tagCountMap = new HashMap<>();
        for (Article article : articles) {
            if (article.getTags() != null && !article.getTags().trim().isEmpty()) {
                String[] tags = article.getTags().split(",");
                for (String tag : tags) {
                    String trimmedTag = tag.trim();
                    if (!trimmedTag.isEmpty()) {
                        tagCountMap.put(trimmedTag, tagCountMap.getOrDefault(trimmedTag, 0) + 1);
                    }
                }
            }
        }
        
        model.addAttribute("tags", tagCountMap);
        return "admin/tags";
    }
}