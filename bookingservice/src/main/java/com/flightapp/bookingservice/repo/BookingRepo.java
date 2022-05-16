package com.flightapp.bookingservice.repo;

import com.flightapp.bookingservice.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@RequestMapping
public interface BookingRepo extends JpaRepository<Booking,Long> {
    List<Booking> findByUser(String user);

    Optional<Booking> findByUserAndSourceAndDestinationAndDateAndTimeAndStatusEqualsIgnoreCase(String user, String source, String destination, String date, String time, String status);


}
