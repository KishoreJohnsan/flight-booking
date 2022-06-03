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
public class CustomHandler implements RequestHandler<Booking, APIGatewayProxyResponseEvent> {

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

        String user = event.getUser().concat("@gmail.com");
        StringBuilder emailContent = new StringBuilder("Dear ").append(event.getUser()).append(",");

        if(event.getStatus().equalsIgnoreCase("BOOKED")) {
            emailContent.append("\n\n").append("Thank you for using Interncontinental Escapes for booking ticket");
            emailContent.append("\n\nYour Booking Details are below:");
            emailContent.append("\nPNR : ").append(event.getBookingId());
            emailContent.append("\nFlight Number: ").append(event.getFlightNumber());
            emailContent.append("\nAirline : ").append(event.getAirline());
            emailContent.append("\nDate : ").append(event.getDate());
            emailContent.append("\nTime : ").append(event.getTime());
            emailContent.append("\nSeats : ").append(event.getSeats());
            emailContent.append("\nMeal Preference : ").append(event.getMealPreference().toUpperCase());
            emailContent.append("\nFare : ").append(event.getFare());
        }else{
            emailContent.append("\n\nWe wish to inform you that your ticket against PNR ").append(event.getBookingId());
            emailContent.append(" has been cancelled successfully");
        }

        emailContent.append("\n\n").append("Warm Regards,");
        emailContent.append("\nCustomer Care");
        emailContent.append("\nIntercontinental Escapes");

        StringBuilder subject = new StringBuilder();
        if(event.getStatus().equalsIgnoreCase("BOOKED")) {
            subject.append("Booking Confirmation for ");
            subject.append(event.getSource()).append(" to ").append(event.getDestination());
            subject.append(" on ").append(event.getDate());
        }else{
            subject.append("Cancel Ticekt");
        }

        StringBuilder response = new StringBuilder("Ticket Details has been sent to user ");
        response.append(event.getUser());

        try {
            message.setFrom(new InternetAddress("noreply@intercontinentalescapes.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user));
            message.setSubject(subject.toString());
            message.setText(emailContent.toString());
            message.saveChanges();
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        log.info("Mail has been sent to ", user);
        return new APIGatewayProxyResponseEvent().withBody(response.toString()).withStatusCode(200);
    }
}
