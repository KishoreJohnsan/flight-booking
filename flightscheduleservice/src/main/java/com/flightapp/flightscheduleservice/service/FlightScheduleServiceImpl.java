package com.flightapp.flightscheduleservice.service;

import com.flightapp.flightscheduleservice.entity.FlightSchedule;
import com.flightapp.flightscheduleservice.exception.FlightScheduleAlreadyExistsException;
import com.flightapp.flightscheduleservice.exception.FlightScheduleNotFoundException;
import com.flightapp.flightscheduleservice.repo.FlightScheduleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightScheduleServiceImpl implements FlightScheduleService{
    @Autowired
    private FlightScheduleRepo repo;
    @Override
    public List<FlightSchedule> getAllFlightSchedules() throws FlightScheduleNotFoundException{
        List<FlightSchedule> scheduleList = repo.findAll();
        if (scheduleList.isEmpty())
            throw new FlightScheduleNotFoundException();
        else
            return scheduleList;
    }


    @Override
    public List<FlightSchedule> getFlightScheduleBySrcAndDest(String src, String dest) throws FlightScheduleNotFoundException {
        List<FlightSchedule> scheduleList = repo.findBySourceAndDestination(src,dest);
        if(scheduleList.isEmpty())
            throw new FlightScheduleNotFoundException();
        else return scheduleList;
    }

    @Override
    public List<FlightSchedule> getFlightScheduleByAirlineAndFlightNo(String airline, String flightNo) throws FlightScheduleNotFoundException {
        List<FlightSchedule> scheduleList = repo.findByAirlineAndFlightNumber(airline,flightNo);
        if(scheduleList.isEmpty())
            throw new FlightScheduleNotFoundException();
        else return scheduleList;
    }

    @Override
    public boolean saveFlightSchedule(FlightSchedule schedule) throws FlightScheduleAlreadyExistsException {

        Optional<FlightSchedule> scheduleOpt = repo.findBySourceAndDestinationAndTimeAndDateAndAirlineAndFlightNumber(
                schedule.getSource(),schedule.getDestination(), schedule.getTime(), schedule.getDate(), schedule.getAirline(), schedule.getFlightNumber());
        //Optional<FlightSchedule> scheduleOpt = repo.findBySourceAndDestinationAndTimeAndDate(schedule.getSource(), schedule.getDestination(), schedule.getTime(), schedule.getDate());
        if (scheduleOpt.isPresent())
            throw new FlightScheduleAlreadyExistsException();
        else
            repo.save(schedule);

        return true;
    }

    @Override
    public boolean updateFlightSchedule(FlightSchedule schedule) throws FlightScheduleNotFoundException {
        Optional<FlightSchedule> scheduleOpt = repo.findById(schedule.getScheduleId());
        if (scheduleOpt.isPresent())
            repo.save(schedule);
        else
            throw new FlightScheduleNotFoundException();

        return true;
    }

    @Override
    public boolean deleteFlightSchedule(Long scheduleId) throws FlightScheduleNotFoundException {
        Optional<FlightSchedule> scheduleOpt = repo.findById(scheduleId);
        if (scheduleOpt.isPresent())
            repo.deleteById(scheduleId);
        else
            throw new FlightScheduleNotFoundException();

        return true;
    }
}


