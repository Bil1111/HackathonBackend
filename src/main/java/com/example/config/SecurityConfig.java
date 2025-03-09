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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF for REST APIs (if you're using stateless authentication)
                .authorizeRequests()
                .requestMatchers("/", "/login**", "/error", "/register").permitAll() // Allow public access to these pages
//                .anyRequest().authenticated() // Require authentication for other requests
                .and()
//                .oauth2Login() // Enable OAuth2 login
//                .loginPage("/api/users/register") // Redirect to this page if the user is not logged in
//                .defaultSuccessUrl("/about", true) // Redirect after successful login
//                .failureUrl("/login?error") // Optional: Redirect to login page with error if authentication fails
//                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter for API requests

        return http.build();
    }

    // Оголошення PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
