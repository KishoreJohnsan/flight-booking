package com.flightapp.airlineservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flightapp.airlineservice.entity.Airline;
import com.flightapp.airlineservice.entity.ErrorResponse;
import com.flightapp.airlineservice.exception.AirlineAlreadyExistsException;
import com.flightapp.airlineservice.exception.AirlineNotFoundException;
import com.flightapp.airlineservice.service.AirlineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/flight/airlines")
public class AirlineController {

    @Autowired
    private AirlineService airlineService;

    @Autowired
    private KafkaTemplate<String, Airline> kafkaTemplate;

    @GetMapping(value = "/airline")
    public ResponseEntity<List<Airline>> getAllAirlines() throws AirlineNotFoundException {
        return new ResponseEntity<>(airlineService.getAllAirlines(), HttpStatus.OK);
    }

    @GetMapping(value = "/airlineById/{airlineId}")
    public ResponseEntity<Airline> getAirlineById(@PathVariable Long airlineId) throws AirlineNotFoundException {
        return new ResponseEntity<>(airlineService.getAirlineById(airlineId), HttpStatus.OK);
    }

    //Kafka Producer
    @GetMapping(value = "/airline/{airlineName}")
    public ResponseEntity<String> getAirlineByName(@PathVariable String airlineName) throws AirlineNotFoundException, JsonProcessingException {
        Airline airline = airlineService.getAirlineByName(airlineName);

        String kafkaTopic = "airline_topic";
        kafkaTemplate.send(kafkaTopic, airline);
        return new ResponseEntity<>("Airline Details Published", HttpStatus.OK);
    }

    /*@GetMapping(value = "/airline/{airlineName}")
    public ResponseEntity<Airline> getAirlineByName(@PathVariable String airlineName) throws AirlineNotFoundException {
        return new ResponseEntity<>(airlineService.getAirlineByName(airlineName), HttpStatus.OK);
    }*/

    @PostMapping(value = "/airline")
    public ResponseEntity<Boolean> saveAirline(@RequestBody Airline airline) throws AirlineAlreadyExistsException {
        //log.info(airline.toString());
        return new ResponseEntity<>(airlineService.saveAirline(airline), HttpStatus.CREATED);
    }

    @PutMapping(value = "/airline")
    public ResponseEntity<Boolean> updateAirline(@RequestBody Airline airline) throws AirlineNotFoundException {
        return new ResponseEntity<>(airlineService.updateAirline(airline), HttpStatus.OK);

    }

    @DeleteMapping(value = "/airline")
    public ResponseEntity<Boolean> deleteAirline(@RequestBody Airline airline) throws AirlineNotFoundException {
        return new ResponseEntity<>(airlineService.deleteAirline(airline.getAirlineId()), HttpStatus.OK);
    }

    @ExceptionHandler(AirlineNotFoundException.class)
    public ResponseEntity<?> handleAirlineNotFound(){
        return new ResponseEntity<>("Airline not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AirlineAlreadyExistsException.class)
    public ResponseEntity<?> handleAirlineAlreadyExists(){
        return new ResponseEntity<>("Airline already present", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(){
        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
