/*
package com.flightbooking.emailservice.controller;

import com.flightbooking.emailservice.entity.Booking;
import com.flightbooking.emailservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/notify")
    public void sendEmailByBooking(@RequestBody Booking booking) {
        booking.setUser("skj");
        emailService.sendEmailToUser(booking);
    }

    @GetMapping(value = "/notify")
    public void sendEmail() {
        Booking booking = new Booking();
        booking.setUser("skj");
        emailService.sendEmailToUser(booking);
    }

}
*/
