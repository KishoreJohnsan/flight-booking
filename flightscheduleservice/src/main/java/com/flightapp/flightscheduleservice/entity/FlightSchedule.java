package com.flightapp.flightscheduleservice.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@Entity
@DynamicUpdate
public class FlightSchedule {

    @Id
    @SequenceGenerator(name="SCHEDULE_SEQ_GEN", sequenceName="SCHEDULE_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SCHEDULE_SEQ_GEN")
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
