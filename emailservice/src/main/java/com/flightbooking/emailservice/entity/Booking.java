package com.flightbooking.emailservice.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
    private String flightNumber;
    private String flightType;
}
