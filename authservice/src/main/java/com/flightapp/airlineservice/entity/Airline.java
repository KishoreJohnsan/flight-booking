package com.flightapp.airlineservice.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class Airline {

    private Long airlineId;
    private String airlineName;
    private String contact;
    private String address;
    private String isActive;

}
