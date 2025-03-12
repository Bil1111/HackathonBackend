package com.example.config;

import com.example.config.token.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) throws Exception {
        http
                .csrf().disable() // Вимкнути CSRF для REST API
                .authorizeRequests()
                .requestMatchers("/", "/login**", "/error", "/register").permitAll() // Публічні сторінки
               // .anyRequest().authenticated() // Інші запити вимагають авторизації
                .and()
                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler) // Використовуємо кастомний хендлер
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class); // Додаємо JWT-фільтр

        return http.build();
    }


    // Оголошення PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
