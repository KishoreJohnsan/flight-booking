package com.flightapp.airlineservice.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Booking {

    private Long bookingId;
    private String source;
    private String destination;
    private String airline;
    private String date;
    private String time;
    private Double fare;
    private String user;
    private String status;
    private Integer seats;
    private String mealPreference;
}
