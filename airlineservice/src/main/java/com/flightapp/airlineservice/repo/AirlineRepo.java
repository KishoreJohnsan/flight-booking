package com.flightapp.airlineservice.repo;

import com.flightapp.airlineservice.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirlineRepo extends JpaRepository<Airline,Long> {

    Optional<Airline> findByAirlineName(String airlineName);
}
