package com.example.awesomehugoblog.service;

import com.example.awesomehugoblog.entity.User;
import com.example.awesomehugoblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.MessageDigest;
import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void initAdminUser() {
        // 检查是否已存在管理员用户
        if (userRepository.findByUsername("csq") == null) {
            User admin = new User();
            admin.setUsername("csq");
            admin.setPassword(encryptPassword("csq123"));
            userRepository.save(admin);
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        user.setPassword(encryptPassword(user.getPassword()));
        return userRepository.save(user);
    }

    public String encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return "SHA256:" + hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        System.out.println("DEBUG: validatePassword called with rawPassword: " + rawPassword);
        System.out.println("DEBUG: validatePassword called with encodedPassword: " + encodedPassword);
        String encryptedRawPassword = encryptPassword(rawPassword);
        System.out.println("DEBUG: Raw password: " + rawPassword);
        System.out.println("DEBUG: Encoded password from DB: " + encodedPassword);
        System.out.println("DEBUG: Encrypted raw password: " + encryptedRawPassword);
        boolean matches = encodedPassword.equals(encryptedRawPassword);
        System.out.println("DEBUG: Passwords match: " + matches);
        return matches;
    }
}