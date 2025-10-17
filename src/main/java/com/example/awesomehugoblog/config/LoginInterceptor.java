package com.example.awesomehugoblog.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求的URI
        String uri = request.getRequestURI();
        
        // 如果是登录、注册相关页面，直接放行
        if (uri.startsWith("/login") || uri.startsWith("/register") || uri.equals("/") || uri.startsWith("/articles") || 
            uri.startsWith("/article/") || uri.startsWith("/timeline") || uri.startsWith("/tags") || 
            uri.startsWith("/about") || uri.startsWith("/links") || uri.startsWith("/css") || 
            uri.startsWith("/js") || uri.startsWith("/images") || uri.startsWith("/webjars")) {
            return true;
        }
        
        // 检查Spring Security的认证状态
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && 
                                 !"anonymousUser".equals(authentication.getPrincipal());
        
        // 如果没有登录且访问的是admin路径，重定向到登录页面
        if (!isAuthenticated && uri.startsWith("/admin")) {
            response.sendRedirect("/login");
            return false;
        }
        
        return true;
    }
}