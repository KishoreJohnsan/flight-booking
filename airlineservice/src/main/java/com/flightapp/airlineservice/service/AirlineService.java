package com.flightapp.airlineservice.service;

import com.flightapp.airlineservice.entity.Airline;
import com.flightapp.airlineservice.exception.AirlineAlreadyExistsException;
import com.flightapp.airlineservice.exception.AirlineNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AirlineService {

    List<Airline> getAllAirlines() throws AirlineNotFoundException;
    Airline getAirlineByName(String name) throws AirlineNotFoundException;
    boolean saveAirline(Airline airline) throws AirlineAlreadyExistsException;
    boolean updateAirline(Airline airline) throws AirlineNotFoundException;
    boolean deleteAirline(Long id) throws AirlineNotFoundException;
}
