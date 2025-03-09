package com.example.config.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserLoginRequest {

    @NotBlank(message = "Логін не може бути порожнім")
    private String login;

    @NotBlank(message = "Пароль не може бути порожнім")
    @Size(min = 6, max = 20, message = "Пароль має бути від 6 до 20 символів")
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
