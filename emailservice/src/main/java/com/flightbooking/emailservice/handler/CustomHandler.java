package com.flightbooking.emailservice.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.flightbooking.emailservice.entity.Booking;
import com.flightbooking.emailservice.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@Slf4j
public class CustomHandler implements RequestHandler<Booking, APIGatewayProxyResponseEvent> {

   @Autowired
   private EmailService emailService;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(Booking event, Context context) {

        log.info(event.toString());
        emailService.sendEmailToUser(event);
        return new APIGatewayProxyResponseEvent().withBody("Booking Details has been sent to user").withStatusCode(HttpStatus.OK.value());
    }
}
