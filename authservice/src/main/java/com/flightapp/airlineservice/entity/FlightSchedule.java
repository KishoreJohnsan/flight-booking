package com.flightapp.airlineservice.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightSchedule {

    private Long scheduleId;
    private String flightNumber;
    private String airline;
    private String source;
    private String destination;
    private String flightType;
    private String time;
    private String date;
    private Double fare;


}
