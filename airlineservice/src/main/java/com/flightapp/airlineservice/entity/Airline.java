package com.flightapp.airlineservice.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


@Getter
@Setter
@Entity
@DynamicUpdate
@ToString
public class Airline {

    @Id
    @SequenceGenerator(name="AIRLINE_SEQ_GEN", sequenceName="AIRLINE_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="AIRLINE_SEQ_GEN")
    private Long airlineId;
    private String airlineName;
    private String contact;
    private String address;
    private String isActive;

}
