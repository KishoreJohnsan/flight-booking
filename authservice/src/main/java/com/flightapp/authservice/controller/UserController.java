package com.flightapp.authservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightapp.airlineservice.entity.Booking;
import com.flightapp.airlineservice.entity.FlightSchedule;
import com.flightapp.authservice.entity.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/service/user")
public class UserController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Value("${flightschedule.service.url}")
    private String baseUrlSchedule;

    @Value("${booking.service.url}")
    private String baseUrlBooking;

    @GetMapping(value = "/")
    public String helloUser() {
        return "Hello User";
    }

    @GetMapping(value = "/schedule/{src}/{dest}")
    public ResponseEntity<?> getFlightsBySrcAndDest(@PathVariable String src, @PathVariable String dest) {

        String url = baseUrlSchedule.concat("/scheduleByStn/").concat(src).concat("/").concat(dest);
        HttpEntity<?> httpEntity = new HttpEntity<>(null, null);
        ParameterizedTypeReference<List<FlightSchedule>> type = new ParameterizedTypeReference<>() {
        };
        try {
            return restTemplate.exchange(url, HttpMethod.GET, httpEntity, type);
        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>(new ErrorResponse(e.getResponseBodyAsString()), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping(value = "/scheduleById/{scheduleId}")
    public ResponseEntity<?> getFlightScheduleById(@PathVariable String scheduleId){

        String url = baseUrlSchedule.concat("/scheduleById/").concat(scheduleId);
        HttpEntity<?> httpEntity = new HttpEntity<>(null,null);
        ParameterizedTypeReference<?> type = new ParameterizedTypeReference<>() {};
        try{
            return restTemplate.exchange(url, HttpMethod.GET, httpEntity, type );
        }catch (HttpClientErrorException.NotFound e){
            return new ResponseEntity<>(new ErrorResponse(e.getResponseBodyAsString()), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping(value = "/bookingByUser/{user}")
    public ResponseEntity<?> getBookingsByUser(@PathVariable String user) {

        String url = baseUrlBooking.concat("/booking/user/").concat(user);
        HttpEntity<?> httpEntity = new HttpEntity<>(null, null);
        ParameterizedTypeReference<List<Booking>> type = new ParameterizedTypeReference<>() {
        };
        try {
            return restTemplate.exchange(url, HttpMethod.GET, httpEntity, type);
        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>(new ErrorResponse(e.getResponseBodyAsString()), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping(value = "/bookingById/{bookingId}")
    public ResponseEntity<?> getBookingsById(@PathVariable String bookingId) {

        String url = baseUrlBooking.concat("/booking/").concat(bookingId);
        HttpEntity<?> httpEntity = new HttpEntity<>(null, null);
        ParameterizedTypeReference<?> type = new ParameterizedTypeReference<>() {
        };

        try {
            return restTemplate.exchange(url, HttpMethod.GET, httpEntity, type);
        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>(new ErrorResponse(e.getResponseBodyAsString()), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping(value = "/booking")
    public ResponseEntity<?> bookTicket(@RequestBody Booking booking){

        String url = baseUrlBooking.concat("/booking");
        HttpEntity<?> httpEntity = new HttpEntity<>(booking, null);
        ParameterizedTypeReference<?> type = new ParameterizedTypeReference<>() {
        };
        try {
            return restTemplate.exchange(url, HttpMethod.POST, httpEntity, type);
        } catch (HttpClientErrorException.Conflict e) {
            return new ResponseEntity<>(new ErrorResponse(e.getResponseBodyAsString()), HttpStatus.CONFLICT);
        }
    }

    @PutMapping(value = "/cancelBooking/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable String bookingId) throws JsonProcessingException {

        String url = baseUrlBooking.concat("/booking/cancel/").concat(bookingId);
        HttpEntity<?> httpEntity = new HttpEntity<>(null, null);
        ParameterizedTypeReference<?> type = new ParameterizedTypeReference<>() {
        };

        try {
            return restTemplate.exchange(url, HttpMethod.PUT, httpEntity, type);
        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>(new ErrorResponse(e.getResponseBodyAsString()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/booking")
    public ResponseEntity<?> deleteBooking(@RequestBody Booking booking){

        String url = baseUrlBooking + "/booking";
        HttpEntity<?> httpEntity = new HttpEntity<>(booking,null);
        ParameterizedTypeReference<?> type = new ParameterizedTypeReference<>() {};
        try{
            return restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, type );
        }catch (HttpClientErrorException.NotFound e){
            return new ResponseEntity<>(new ErrorResponse(e.getResponseBodyAsString()), HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/downloadTicket/{bookingId}", produces = MediaType.APPLICATION_PDF_VALUE, method = RequestMethod.GET)
    public ResponseEntity<?> downloadTicket(@PathVariable String bookingId) throws Exception {

        String url = baseUrlBooking.concat("/download/").concat(bookingId);
        HttpEntity<?> httpEntity = new HttpEntity<>(null, null);
        ParameterizedTypeReference<?> type = new ParameterizedTypeReference<>() {
        };

        try{
            return restTemplate.exchange(url, HttpMethod.GET, httpEntity, byte[].class);
        }catch (HttpClientErrorException e){
            return new ResponseEntity<>(new ErrorResponse(e.getResponseBodyAsString()), HttpStatus.NOT_FOUND);
            //throw new Exception();
        }

    }

    @GetMapping(value = "/sendEmail/{bookingId}")
    public ResponseEntity<?> sendEmailNotification(@PathVariable String bookingId){
        String url = baseUrlBooking.concat("/booking/email/").concat(bookingId);
        HttpEntity<?> httpEntity = new HttpEntity<>(null, null);
        ParameterizedTypeReference<?> type = new ParameterizedTypeReference<>() {
        };

        try {
            return restTemplate.exchange(url, HttpMethod.GET, httpEntity, type);
        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>(new ErrorResponse(e.getResponseBodyAsString()), HttpStatus.NOT_FOUND);
        }
    }


   /* @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException() {
        return new ResponseEntity<>(new ErrorResponse("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }*/

}
