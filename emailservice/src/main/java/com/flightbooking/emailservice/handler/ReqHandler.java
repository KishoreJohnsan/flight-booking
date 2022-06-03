package com.flightbooking.emailservice.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.flightbooking.emailservice.entity.Booking;
import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
public class ReqHandler  implements RequestHandler<Booking, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(Booking event, Context context) {

        log.info(event.toString());

        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.mailtrap.io");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.connectiontimeout", "5000");
        props.setProperty("mail.smtp.timeout", "5000");
        props.setProperty("mail.smtp.Writetimeout", "5000");
        props.setProperty("mail.smtp.starttls.enable", "true");

        String username = "f0b2b173beeb8f";
        String password = "c716cf6e902054";
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);

        StringBuilder response = new StringBuilder("Ticket Details has been sent to user ");
        response.append(event.getUser());

        try {
            message.setFrom(new InternetAddress("noreply@intercontinentalescapes.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("skj_lamba@gmail.com"));
            message.setSubject("Test Mail");
            message.setText("Test Mail from AWS Lambda");
            message.saveChanges();
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        log.info("Mail has been sent ");
        return new APIGatewayProxyResponseEvent().withBody(response.toString()).withStatusCode(200);
    }
}
