package com.example.config.users.recoverPassword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SMSService {

    @Autowired
    private TwilioService twilioService;

    public void sendPasswordResetCode(String phoneNumber, String resetToken) {
        String message = "Your password reset token: " + resetToken;
        twilioService.sendSMS(phoneNumber, message);
    }
}
