package com.example.ballup_backend.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
        registry.addMapping("/**")  // Cấu hình cho tất cả các API
                .allowedOrigins("http://localhost:3000", "https://your-frontend.com")  // Thêm URL của Frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Các phương thức HTTP được phép
                .allowedHeaders("*")  // Các header cho phép
                .allowCredentials(true);  // Cho phép gửi cookies và credentials
    }
}
