package com.flightapp.bookingservice.service;

import com.flightapp.bookingservice.entity.Booking;
import com.flightapp.bookingservice.exception.BookingAlreadyExistsException;
import com.flightapp.bookingservice.exception.BookingNotFoundException;
import com.flightapp.bookingservice.repo.BookingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService{

    @Autowired
    private BookingRepo repo;
    @Override
    public List<Booking> getBookingByUser(String user) throws BookingNotFoundException {
        List<Booking> bookingList = repo.findAll();
        if(bookingList.isEmpty())
            throw new BookingNotFoundException();
        else
            return bookingList;
    }

    @Override
    public Booking getBookingById(Long bookingId) throws BookingNotFoundException {
        Optional<Booking> bookingOpt = repo.findById(bookingId);
        if(bookingOpt.isPresent())
            return bookingOpt.get();
        else
            throw new BookingNotFoundException();
    }

    @Override
    public boolean saveBooking(Booking booking) throws BookingAlreadyExistsException {
        Optional<Booking> bookingOpt = repo.findByUserAndSourceAndDestinationAndDateAndTimeAndStatusEqualsIgnoreCase(
                booking.getUser(), booking.getSource(), booking.getDestination(), booking.getDate(), booking.getTime(), "BOOKED"
        );
        if(bookingOpt.isPresent())
            throw  new BookingAlreadyExistsException();
        else {
            booking.setStatus("BOOKED");
            repo.save(booking);
        }

        return true;
    }

    @Override
    public boolean updateBooking(Booking booking) throws BookingNotFoundException {
        Optional<Booking> bookingOpt = repo.findById(booking.getBookingId());
        if(bookingOpt.isPresent())
            repo.save(booking);
        else
            throw new BookingNotFoundException();

        return true;
    }

    @Override
    public boolean cancelBooking(Long bookingId) throws BookingNotFoundException {
        Optional<Booking> bookingOpt = repo.findById(bookingId);
        if(bookingOpt.isPresent()){
            Booking booking = bookingOpt.get();
            booking.setStatus("CANCELLED");
            repo.save(booking);
        } else
            throw new BookingNotFoundException();

        return true;
    }

    @Override
    public boolean deleteBooking(Long bookingId) throws BookingNotFoundException {
        Optional<Booking> bookingOpt = repo.findById(bookingId);
        if(bookingOpt.isPresent())
            repo.deleteById(bookingId);
        else
            throw new BookingNotFoundException();

        return true;
    }
}
