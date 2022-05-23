package com.flightapp.flightscheduleservice.service;

import com.flightapp.flightscheduleservice.entity.FlightSchedule;
import com.flightapp.flightscheduleservice.exception.FlightScheduleAlreadyExistsException;
import com.flightapp.flightscheduleservice.exception.FlightScheduleNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FlightScheduleService {

    List<FlightSchedule> getAllFlightSchedules() throws FlightScheduleNotFoundException;

    FlightSchedule getFlightScheduleById(Long scheduleId) throws FlightScheduleNotFoundException;
    List<FlightSchedule> getFlightScheduleBySrcAndDest(String src, String dest) throws FlightScheduleNotFoundException;

    List<FlightSchedule> getFlightScheduleByAirlineAndFlightNo(String airline, String flightNo) throws FlightScheduleNotFoundException;

    boolean saveFlightSchedule(FlightSchedule schedule) throws FlightScheduleAlreadyExistsException;

    boolean updateFlightSchedule(FlightSchedule schedule) throws FlightScheduleNotFoundException;

    boolean deleteFlightSchedule(Long scheduleId) throws FlightScheduleNotFoundException;


}
