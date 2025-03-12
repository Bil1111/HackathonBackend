package com.example.config.users.recoverPassword;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    public void sendSMS(String to, String message) {
        Twilio.init(accountSid, authToken);
        try {
            Message sms = Message.creator(
                    new com.twilio.type.PhoneNumber(to),
                    new com.twilio.type.PhoneNumber(twilioPhoneNumber),
                    message
            ).create();
            System.out.println("SMS відправлено: " + sms.getSid());
        } catch (Exception e) {
            System.err.println("Помилка відправки SMS: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

