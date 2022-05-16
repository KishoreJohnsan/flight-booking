package com.flightapp.authservice.service;

import com.flightapp.airlineservice.entity.Airline;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AirlineListener {
    private static final String TOPIC = "airline_topic";

    @KafkaListener(topics = TOPIC, groupId="group_id", containerFactory = "userKafkaListenerFactory")
    public void getAirlineDataFromTopic(Airline airline) {
        System.out.println("Airline Data Received: " + airline);
    }

}