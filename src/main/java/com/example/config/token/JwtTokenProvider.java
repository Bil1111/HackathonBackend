package com.example.config.token;

import com.example.config.users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private final Key jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);


    // Генерація токена
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) // Задаємо email як subject
                .claim("id", user.getId())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .claim("email", user.getEmail())
                .claim("login", user.getLogin())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date()) // Час видачі токена
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // Підписуємо токен з використанням секретного ключа
                .compact(); // Повертаємо згенерований токен
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret) // Використовуємо вже наявний секретний ключ
                .build() // Будуємо парсер
                .parseClaimsJws(token) // Парсимо токен
                .getBody(); // Отримуємо Claims
        return claims.get("role", List.class); // "role" — ключ, під яким зберігається роль
    }

    public String getEmailFromToken(String token) {
        try {
            // Розбираємо токен і отримуємо claims
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret) // Використовуємо вже наявний секретний ключ
                    .build() // Створюємо парсер
                    .parseClaimsJws(token) // Парсимо токен
                    .getBody(); // Отримуємо claims з токену
            return claims.getSubject();
        } catch (Exception e) {
            // Обробка помилок, якщо токен недійсний або пошкоджений
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }
}