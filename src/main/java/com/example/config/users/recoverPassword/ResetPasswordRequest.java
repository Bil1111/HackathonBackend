package com.example.config.users.recoverPassword;

public class ResetPasswordRequest {
    private String token;
    private String newPassword;

    // Геттери та сеттери
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    // Конструктор за замовчуванням (якщо потрібно)
    public ResetPasswordRequest() {
    }

    // Конструктор з параметрами
    public ResetPasswordRequest(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }
}
