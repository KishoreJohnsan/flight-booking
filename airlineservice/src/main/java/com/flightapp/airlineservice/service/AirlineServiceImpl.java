package com.flightapp.airlineservice.service;

import com.flightapp.airlineservice.entity.Airline;
import com.flightapp.airlineservice.exception.AirlineAlreadyExistsException;
import com.flightapp.airlineservice.exception.AirlineNotFoundException;
import com.flightapp.airlineservice.repo.AirlineRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class AirlineServiceImpl implements AirlineService {

    @Autowired
    private AirlineRepo repo;

    @Override
    public List<Airline> getAllAirlines() throws AirlineNotFoundException {
        List<Airline> airlineList = repo.findAll();
        if (airlineList.isEmpty())
            throw new AirlineNotFoundException();
        else
            return airlineList;
    }

    @Override
    public Airline getAirlineById(Long airlineId) throws AirlineNotFoundException {
        Optional<Airline> airlineOpt = repo.findById(airlineId);
        if (airlineOpt.isPresent()) {
            return airlineOpt.get();
        } else
            throw new AirlineNotFoundException();
    }

    @Override
    public Airline getAirlineByName(String name) throws AirlineNotFoundException {
        Optional<Airline> airlineOpt = repo.findByAirlineName(name);
        if (airlineOpt.isPresent()) {
            return airlineOpt.get();
        } else
            throw new AirlineNotFoundException();

    }

    @Override
    public boolean saveAirline(Airline airline) throws AirlineAlreadyExistsException {
        Optional<Airline> airlineOpt = repo.findByAirlineName(airline.getAirlineName());
        if (airlineOpt.isPresent())
            throw new AirlineAlreadyExistsException();
        else
            repo.save(airline);

        return true;

    }

    @Override
    public boolean updateAirline(Airline airline) throws AirlineNotFoundException {
        Optional<Airline> airlineOpt = repo.findById(airline.getAirlineId());
        if (airlineOpt.isPresent())
            repo.save(airline);
        else
            throw new AirlineNotFoundException();

        return true;
    }

    @Override
    public boolean deleteAirline(Long id) throws AirlineNotFoundException {
        Optional<Airline> airlineOpt = repo.findById(id);
        if (airlineOpt.isPresent())
            repo.deleteById(id);
        else
            throw new AirlineNotFoundException();

        return true;

    }
}
