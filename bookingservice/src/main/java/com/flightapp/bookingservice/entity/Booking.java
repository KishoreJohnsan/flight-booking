package com.flightapp.bookingservice.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@Entity
@DynamicUpdate
public class Booking {

    @Id
    @SequenceGenerator(name="BOOKING_SEQ_GEN", sequenceName="BOOKING_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="BOOKING_SEQ_GEN")
    private Long bookingId;
    private String source;
    private String destination;
    private String airline;
    private String date;
    private String time;
    private Double fare;
    private String user;
    private String status = "BOOKED";
    private Integer seats;
    private String mealPreference;
}
