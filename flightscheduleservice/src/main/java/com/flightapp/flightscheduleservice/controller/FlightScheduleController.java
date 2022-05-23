package com.flightapp.flightscheduleservice.controller;

import com.flightapp.flightscheduleservice.entity.FlightSchedule;
import com.flightapp.flightscheduleservice.exception.FlightScheduleAlreadyExistsException;
import com.flightapp.flightscheduleservice.exception.FlightScheduleNotFoundException;
import com.flightapp.flightscheduleservice.service.FlightScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/flight/schedules")
public class FlightScheduleController {

    @Autowired
    private FlightScheduleService flightScheduleService;

    @GetMapping(value = "/schedule")
    public ResponseEntity<List<FlightSchedule>> getAllFlightSchedules() throws FlightScheduleNotFoundException {
        return new ResponseEntity<>(flightScheduleService.getAllFlightSchedules(), HttpStatus.OK);
    }

    @GetMapping(value = "/scheduleById/{scheduleId}")
    public ResponseEntity<FlightSchedule> getFlightScheduleById(@PathVariable Long scheduleId) throws FlightScheduleNotFoundException {
        return new ResponseEntity<>(flightScheduleService.getFlightScheduleById(scheduleId), HttpStatus.OK);
    }

    @GetMapping(value = "/scheduleByStn/{src}/{dest}")
    public ResponseEntity<List<FlightSchedule>> getFlightScheduleBySrcAndDest(@PathVariable String src, @PathVariable String dest) throws FlightScheduleNotFoundException {
        return new ResponseEntity<>(flightScheduleService.getFlightScheduleBySrcAndDest(src,dest), HttpStatus.OK);
    }

    @GetMapping(value = "/scheduleByFlt/{airline}/{flightNo}")
    public ResponseEntity<List<FlightSchedule>> getFlightScheduleByAirlineAndFlightNo(@PathVariable String airline, @PathVariable String flightNo) throws FlightScheduleNotFoundException {
        return new ResponseEntity<>(flightScheduleService.getFlightScheduleByAirlineAndFlightNo(airline,flightNo), HttpStatus.OK);
    }

    @PostMapping(value = "/schedule")
    public ResponseEntity<Boolean> saveFlightSchedule(@RequestBody FlightSchedule flightSchedule) throws FlightScheduleAlreadyExistsException {
        return new ResponseEntity<>(flightScheduleService.saveFlightSchedule(flightSchedule), HttpStatus.CREATED);
    }

    @PutMapping(value = "/schedule")
    public ResponseEntity<Boolean> updateFlightSchedule(@RequestBody FlightSchedule flightSchedule) throws FlightScheduleNotFoundException {
        return new ResponseEntity<>(flightScheduleService.updateFlightSchedule(flightSchedule), HttpStatus.OK);

    }

    @DeleteMapping(value = "/schedule")
    public ResponseEntity<Boolean> deleteFlightSchedule(@RequestBody FlightSchedule flightSchedule) throws FlightScheduleNotFoundException {
        return new ResponseEntity<>(flightScheduleService.deleteFlightSchedule(flightSchedule.getScheduleId()), HttpStatus.OK);
    }

    @ExceptionHandler(FlightScheduleNotFoundException.class)
    public ResponseEntity<?>handleFlightScheduleNotFound(){
        return new ResponseEntity<>("FlightSchedule not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FlightScheduleAlreadyExistsException.class)
    public ResponseEntity<?> handleFlightScheduleAlreadyFound(){
        return new ResponseEntity<>("FlightSchedule already present", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public  ResponseEntity<?> handleException(){
        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
