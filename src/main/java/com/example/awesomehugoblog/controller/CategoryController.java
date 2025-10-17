package com.example.awesomehugoblog.controller;

import com.example.awesomehugoblog.entity.Category;
import com.example.awesomehugoblog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping
    public String list(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "admin/categories/list";
    }
    
    @GetMapping("/new")
    public String newCategory(Model model) {
        model.addAttribute("category", new Category());
        return "admin/categories/edit";
    }
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id).orElse(null);
        model.addAttribute("category", category);
        return "admin/categories/edit";
    }
    
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Category category, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("category", category);
            return "admin/categories/edit";
        }
        
        // 检查分类名称是否已存在
        if (category.getId() == null) {
            // 新建时检查名称是否重复
            if (categoryService.findByName(category.getName()).isPresent()) {
                result.rejectValue("name", "error.category", "分类名称已存在");
                model.addAttribute("category", category);
                return "admin/categories/edit";
            }
        } else {
            // 编辑时检查名称是否重复（排除自己）
            Category existing = categoryService.findByName(category.getName()).orElse(null);
            if (existing != null && !existing.getId().equals(category.getId())) {
                result.rejectValue("name", "error.category", "分类名称已存在");
                model.addAttribute("category", category);
                return "admin/categories/edit";
            }
        }
        
        categoryService.save(category);
        return "redirect:/admin/categories";
    }
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return "redirect:/admin/categories";
    }
}