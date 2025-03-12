package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Дозволити CORS для всіх маршрутів
                .allowedOrigins("*") // Дозволити запити з будь-якого домену
                .allowedMethods("*") // Дозволити всі методи (GET, POST, PUT, DELETE, OPTIONS і т. д.)
                .allowedHeaders("*"); // Дозволити всі заголовки
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:src/main/resources/images/");
    }
}
