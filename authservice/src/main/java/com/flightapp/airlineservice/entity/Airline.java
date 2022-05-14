package com.flightapp.airlineservice.entity;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Airline {

    private Long airlineId;
    private String airlineName;
    private String contact;
    private String address;

    @Override
    public String toString() {
        return "Airline{" +
                "airlineId=" + airlineId +
                ", airlineName='" + airlineName + '\'' +
                ", contact='" + contact + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
