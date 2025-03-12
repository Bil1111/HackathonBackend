package com.example.config.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegistrationRequest {
    @NotBlank(message = "Ім'я не може бути порожнім")
    private String firstName;
    @NotBlank(message = "Прізвище не може бути порожнім")
    private String lastName;
    @Email(message = "Вкажіть валідний email")
    @NotBlank(message = "Email не може бути порожнім")
    private String email;
    @NotBlank(message = "Номер телефону не може бути порожнім")
    private String phoneNumber;
    @NotBlank(message = "Логін не може бути порожнім")
    private String login;
    @NotBlank(message = "Пароль не може бути порожнім")
    @Size(min = 6, max = 20, message = "Пароль має бути від 6 до 20 символів")
    private String password;

    public UserRegistrationRequest(String firstName, String lastName, String email, String phoneNumber, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.login = login;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

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
