package com.flightapp.bookingservice.controller;

import com.flightapp.bookingservice.entity.Booking;
import com.flightapp.bookingservice.exception.BookingAlreadyExistsException;
import com.flightapp.bookingservice.exception.BookingNotFoundException;
import com.flightapp.bookingservice.repo.BookingRepo;
import com.flightapp.bookingservice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/flight/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepo repo;


    @GetMapping(value = "/booking/user/{user}")
    public ResponseEntity<List<Booking>> getAllBookingByUser(@PathVariable String user) throws BookingNotFoundException {
        return new ResponseEntity<>(bookingService.getBookingByUser(user), HttpStatus.OK);
    }

    @GetMapping(value = "/booking/{bookingId}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long bookingId) throws BookingNotFoundException {
        return new ResponseEntity<>(bookingService.getBookingById(bookingId), HttpStatus.OK);
    }

    @PostMapping(value = "/booking")
    public ResponseEntity<Boolean> saveBooking(@RequestBody Booking booking) throws BookingAlreadyExistsException {
        return new ResponseEntity<>(bookingService.saveBooking(booking), HttpStatus.CREATED);
    }

    @PutMapping(value = "/booking")
    public ResponseEntity<Boolean> updateBooking(@RequestBody Booking booking) throws BookingNotFoundException {
        return new ResponseEntity<>(bookingService.updateBooking(booking), HttpStatus.OK);
    }

    @PutMapping(value = "/booking/cancel/{bookingId}")
    public ResponseEntity<Boolean> cancelBooking(@PathVariable Long bookingId) throws BookingNotFoundException {
        return new ResponseEntity<>(bookingService.cancelBooking(bookingId), HttpStatus.CREATED);

    }

    @DeleteMapping(value = "/booking")
    public ResponseEntity<Boolean> deleteAirline(@RequestBody Booking booking) throws BookingNotFoundException {
        return new ResponseEntity<>(bookingService.deleteBooking(booking.getBookingId()), HttpStatus.OK);
    }

    @GetMapping(value = "/download/{bookingId}")
    public ResponseEntity<InputStreamResource> downloadTicket(@PathVariable Long bookingId) throws BookingNotFoundException, IOException {

        Map<String, Object> map = bookingService.generateTicket(bookingId);
        String user = (String) map.get("User");
        File ticket = (File) map.get("Ticket");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(ticket));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        String filename = user.concat("_").concat(bookingId.toString()).concat(".pdf");
        ContentDisposition contentDisposition = ContentDisposition.attachment().filename(filename).build();
        headers.setContentDisposition(contentDisposition);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<?> handleBookingNotFound(){
        return new ResponseEntity<>("Booking Not Found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookingAlreadyExistsException.class)
    public ResponseEntity<?> handleBookingAlreadyFound(){
        return new ResponseEntity<>("Booking already present", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException() {
        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
