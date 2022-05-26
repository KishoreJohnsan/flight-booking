package com.flightbooking.emailservice.service;

import com.flightbooking.emailservice.entity.Booking;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailToUser(Booking booking){

        SimpleMailMessage message = new SimpleMailMessage();

        String user = booking.getUser().concat("@gmail.com");
        StringBuilder emailContent = new StringBuilder("Hello");
        StringBuilder subject = new StringBuilder("Test Mail");

        message.setFrom("noreply@intercontinentalescapes.com");
        message.setTo(user);
        message.setSubject(subject.toString());
        message.setText(emailContent.toString());
        mailSender.send(message);
        log.info("Mail has been sent to ", user);


    }

}
