package com.flightapp.bookingservice.controller;

import com.flightapp.bookingservice.entity.Booking;
import com.flightapp.bookingservice.entity.ErrorResponse;
import com.flightapp.bookingservice.exception.BookingAlreadyExistsException;
import com.flightapp.bookingservice.exception.BookingNotFoundException;
import com.flightapp.bookingservice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/flight/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;


    @GetMapping(value = "/booking/user/{user}")
    public ResponseEntity<List<Booking>> getAllBookingByUser(@PathVariable String user) throws BookingNotFoundException {
        return new ResponseEntity<>(bookingService.getBookingByUser(user), HttpStatus.OK);
    }

    @GetMapping(value = "/booking/{bookingId}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long bookingId) throws BookingNotFoundException {
        return new ResponseEntity<>(bookingService.getBookingById(bookingId), HttpStatus.OK);
    }


    @PostMapping(value = "/booking")
    public ResponseEntity<Boolean> saveAirline(@RequestBody Booking booking) throws BookingAlreadyExistsException {
        return new ResponseEntity<>(bookingService.saveBooking(booking), HttpStatus.CREATED);
    }

    @PutMapping(value = "/booking")
    public ResponseEntity<Boolean> updateAirline(@RequestBody Booking booking) throws BookingNotFoundException {
        return new ResponseEntity<>(bookingService.updateBooking(booking), HttpStatus.OK);

    }

    @DeleteMapping(value = "/booking")
    public ResponseEntity<Boolean> deleteAirline(@RequestBody Booking booking) throws BookingNotFoundException {
        return new ResponseEntity<>(bookingService.deleteBooking(booking.getBookingId()), HttpStatus.OK);
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookingNotFound(){
        return new ResponseEntity<>(new ErrorResponse("Booking not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookingAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleBookingAlreadyFound(){
        return new ResponseEntity<>(new ErrorResponse("Booking already present"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException() {
        return new ResponseEntity<>(new ErrorResponse("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
