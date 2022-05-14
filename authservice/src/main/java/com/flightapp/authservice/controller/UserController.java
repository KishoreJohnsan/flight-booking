package com.flightapp.authservice.controller;

import com.flightapp.airlineservice.entity.Booking;
import com.flightapp.airlineservice.entity.FlightSchedule;
import com.flightapp.authservice.entity.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/service/user")
public class UserController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${flightschedule.service.url}")
    private String baseUrlSchedule;

    @Value("${booking.service.url}")
    private String baseUrlBooking;

    @GetMapping(value = "/")
    public String helloUser() {
        return "Hello User";
    }

    @GetMapping(value = "/schedule/{src}/{dest}")
    public ResponseEntity<List<FlightSchedule>> getFlightsBySrcAndDest(@PathVariable String src, @PathVariable String dest) {

        String url = baseUrlSchedule.concat("/scheduleByStn/").concat(src).concat("/").concat(dest);
        HttpEntity<?> httpEntity = new HttpEntity<>(null, null);
        ParameterizedTypeReference<List<FlightSchedule>> type = new ParameterizedTypeReference<>() {
        };
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, type);

    }

    @GetMapping(value = "/bookingByUser/{user}")
    public ResponseEntity<List<Booking>> getBookingsByUser(@PathVariable String user) {

        String url = baseUrlBooking.concat("/booking/user/").concat(user);
        HttpEntity<?> httpEntity = new HttpEntity<>(null, null);
        ParameterizedTypeReference<List<Booking>> type = new ParameterizedTypeReference<>() {
        };
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, type);

    }

    @GetMapping(value = "/bookingById/{bookingId}")
    public ResponseEntity<?> getBookingsById(@PathVariable String bookingId) {

        String url = baseUrlBooking.concat("/booking/").concat(bookingId);
        HttpEntity<?> httpEntity = new HttpEntity<>(null, null);
        ParameterizedTypeReference<Booking> type = new ParameterizedTypeReference<>() {
        };

        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, type);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException() {
        return new ResponseEntity<>(new ErrorResponse("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
