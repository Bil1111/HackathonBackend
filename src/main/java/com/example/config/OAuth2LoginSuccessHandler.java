package com.example.config;

import com.example.config.token.JwtTokenProvider;
import com.example.config.users.User;
import com.example.config.users.UserRepository;
import com.example.config.users.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // Клас для генерації JWT

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // Отримуємо дані користувача
        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");

        User user = userService.registerOrUpdateUserFromGoogle(name, email,"");
        // Генеруємо JWT
        String token = jwtTokenProvider.generateToken(user);
        userService.registerOrUpdateUserFromGoogle(name, email, token);

        // Робимо редирект на фронтенд з токеном
        response.sendRedirect("http://localhost:4200/page8?token=" + token);
    }
}

