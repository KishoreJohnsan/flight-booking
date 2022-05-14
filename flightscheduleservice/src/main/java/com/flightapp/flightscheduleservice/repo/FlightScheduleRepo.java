package com.flightapp.flightscheduleservice.repo;

import com.flightapp.flightscheduleservice.entity.FlightSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightScheduleRepo extends JpaRepository<FlightSchedule, Long> {
    List<FlightSchedule> findBySourceAndDestination(String source, String destination);

    List<FlightSchedule> findByAirlineAndFlightNumber(String airline, String flightNumber);

    Optional<FlightSchedule> findBySourceAndDestinationAndTimeAndDate(String source, String destination, String time, String date);

    Optional<FlightSchedule> findBySourceAndDestinationAndTimeAndDateAndAirlineAndFlightNumber(String source, String destination, String time, String date, String airline, String flightNumber);





}
