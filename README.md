# Awesome Hugo Blog

一个基于Spring Boot和Thymeleaf的博客系统，具有文章管理、评论系统、分类管理等功能。

## 功能特性

- 用户认证和权限管理
- 文章创建、编辑、删除功能
- 文章分类管理
- 评论和回复系统（支持匿名评论）
- 响应式设计，适配移动端
- Markdown编辑器支持
- 搜索功能

## 技术栈

- 后端：Spring Boot 2.7.0
- 前端：Thymeleaf模板引擎、Bootstrap 5
- 数据库：MySQL 8.0
- 安全框架：Spring Security
- 构建工具：Maven

## 环境要求

- Java 11或更高版本
- MySQL 8.0或更高版本
- Maven 3.6或更高版本

## 安装和配置

1. 克隆项目到本地：
   ```
   git clone <repository-url>
   ```

2. 创建MySQL数据库：
   ```sql
   CREATE DATABASE awesome_blog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   CREATE USER 'bloguser'@'localhost' IDENTIFIED BY 'blogpassword';
   GRANT ALL PRIVILEGES ON awesome_blog.* TO 'bloguser'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. 修改数据库配置（src/main/resources/application.properties）：
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/awesome_blog?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
   spring.datasource.username=bloguser
   spring.datasource.password=blogpassword
   ```

4. 构建项目：
   ```
   mvn clean install
   ```

5. 运行项目：
   ```
   mvn spring-boot:run
   ```

6. 访问应用：
   打开浏览器访问 http://localhost:8080

## 默认管理员账户

- 用户名：csq
- 密码：csq123

管理员可以通过 http://localhost:8080/admin 访问管理后台，进行文章管理、分类管理等操作。

## 数据库设计

数据库表包括：
- users：用户表
- articles：文章表
- categories：分类表
- comments：评论表

评论表支持层级结构，可以实现评论和回复功能。

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/example/awesomehugoblog/
│   │       ├── Application.java
│   │       ├── config/
│   │       ├── controller/
│   │       ├── entity/
│   │       ├── repository/
│   │       └── service/
│   └── resources/
│       ├── application.properties
│       ├── static/
│       │   └── css/
│       └── templates/
└── test/
    └── java/
        └── com/example/awesomehugoblog/
```

## 部署

1. 构建可执行JAR文件：
   ```
   mvn clean package
   ```

2. 运行JAR文件：
   ```
   java -jar target/awesomeHugoBlog-0.0.1-SNAPSHOT.jar
   ```

## 开发说明

- 使用Maven管理依赖
- 遵循Spring Boot最佳实践
- 使用JPA/Hibernate进行数据持久化
- 使用Thymeleaf模板引擎渲染页面
- 使用Bootstrap 5实现响应式设计

## 贡献

欢迎提交Issue和Pull Request来改进这个项目。

## 许可证

本项目采用MIT许可证。