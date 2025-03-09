package com.example.config.users.recoverPassword;

public class PasswordResetRequest {
    private String email;

    // Геттери та сеттери
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Конструктор за замовчуванням (якщо потрібно)
    public PasswordResetRequest() {
    }

    // Конструктор з параметрами
    public PasswordResetRequest(String email) {
        this.email = email;
    }
}

