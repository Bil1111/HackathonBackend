package com.example.config.users.recoverPassword;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    // Метод для надсилання листа з посиланням на скидання пароля
    public void sendPasswordResetEmail(String email, String resetToken) {
        String resetLink = "http://localhost:4200/new-password2?token=" + resetToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("learnixsapteam@gmail.com");
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n" + resetLink);

        try {
            javaMailSender.send(message);
            System.out.println("Email sent successfully to: " + email);
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
