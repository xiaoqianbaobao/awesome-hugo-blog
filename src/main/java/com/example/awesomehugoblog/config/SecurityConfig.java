package com.example.awesomehugoblog.config;

import com.example.awesomehugoblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("DEBUG: Configuring HttpSecurity");
        http
            .authorizeRequests()
                .antMatchers("/admin/**").authenticated()
                .anyRequest().permitAll()
            .and()
            .formLogin()
                .loginPage("/login")
                .successHandler((request, response, authentication) -> {
                    System.out.println("DEBUG: Login success, redirecting to /admin");
                    response.sendRedirect("/admin");
                })
                .permitAll()
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            .and()
            .csrf().disable();
        System.out.println("DEBUG: HttpSecurity configured");
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return username -> {
            System.out.println("DEBUG: Loading user: " + username);
            com.example.awesomehugoblog.entity.User user = userService.findByUsername(username);
            if (user == null) {
                System.out.println("DEBUG: User not found: " + username);
                throw new RuntimeException("用户不存在");
            }
            
            System.out.println("DEBUG: User found: " + user.getUsername() + ", password: " + user.getPassword());
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities("USER")
                    .build();
        };
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("DEBUG: Configuring AuthenticationManager");
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        System.out.println("DEBUG: AuthenticationManager configured");
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用自定义的密码编码器来添加调试信息
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                System.out.println("DEBUG: encode called with: " + rawPassword);
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                System.out.println("DEBUG: matches called with rawPassword: " + rawPassword);
                System.out.println("DEBUG: matches called with encodedPassword: " + encodedPassword);
                boolean result = userService.validatePassword(rawPassword.toString(), encodedPassword);
                System.out.println("DEBUG: matches result: " + result);
                return result;
            }
        };
    }
}