package com.example.awesomehugoblog.controller;

import com.example.awesomehugoblog.entity.User;
import com.example.awesomehugoblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    // 移除了手动处理登录的@PostMapping("/login")方法
    // 让Spring Security来处理登录
    
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
    
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@RequestParam String username, 
                          @RequestParam String password, 
                          @RequestParam String confirmPassword, 
                          Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "两次输入的密码不一致");
            return "register";
        }
        
        if (userService.findByUsername(username) != null) {
            model.addAttribute("error", "用户名已存在");
            return "register";
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.save(user);
        model.addAttribute("success", "注册成功，请登录");
        return "login";
    }
}