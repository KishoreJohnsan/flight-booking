package com.flightapp.bookingservice.service;

import com.flightapp.bookingservice.entity.Booking;
import com.flightapp.bookingservice.exception.BookingAlreadyExistsException;
import com.flightapp.bookingservice.exception.BookingNotFoundException;

import java.util.List;

public interface BookingService {

    List<Booking> getBookingByUser(String user) throws BookingNotFoundException;
    Booking getBookingById(Long bookingId) throws BookingNotFoundException;
    boolean saveBooking(Booking booking) throws BookingAlreadyExistsException;
    boolean updateBooking(Booking booking) throws BookingNotFoundException;

    boolean cancelBooking(Long bookingId) throws BookingNotFoundException;
    boolean deleteBooking(Long bookingId) throws BookingNotFoundException;


}
