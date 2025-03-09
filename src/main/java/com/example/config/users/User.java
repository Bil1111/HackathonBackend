package com.example.config.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(50)")
    @NotEmpty(message = "Ім'я не може бути порожнім")
    private String firstName;
    @Column(columnDefinition = "VARCHAR(50)")
    @NotEmpty(message = "Прізвище не може бути порожнім")
    private String lastName;

    @Column(columnDefinition = "VARCHAR(255)")
    @NotEmpty(message = "Email не може бути порожнім")
    @Email(message = "Введіть дійсний email")
    private String email;

    @Column(columnDefinition = "VARCHAR(20)")
    @NotEmpty(message = "Номер телефону не може бути порожнім")
    private String phoneNumber;
    @Column(columnDefinition = "VARCHAR(255)")
    @NotEmpty(message = "Логін не може бути порожнім")
    private String login;

    @Column(columnDefinition = "VARCHAR(255)")
    @NotEmpty(message = "Пароль не може бути порожнім")
    @Size(min = 6, message = "Пароль має містити не менше 6 символів")
    private String password;

    @Column(unique = true, length = 2048)
    private String authToken;
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String firstName,String lastName,String email,String phoneNumber, String login, String encodedPassword, Role role) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.email = email;
        this.phoneNumber=phoneNumber;
        this.login=login;
        this.password = encodedPassword;
        this.role=role;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", authToken='" + authToken + '\'' +
                ", role=" + role +
                '}';
    }
}