package com.flightapp.authservice.controller;

import com.flightapp.airlineservice.entity.Airline;

import com.flightapp.airlineservice.entity.Booking;
import com.flightapp.airlineservice.entity.FlightSchedule;
import com.flightapp.authservice.entity.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/service/admin")
public class AdminController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ConsumerFactory<String, Airline> consumerFactory;

    @Value("${airline.service.url}")
    private String baseUrlAirline;

    @Value("${flightschedule.service.url}")
    private String baseUrlSchedule;

    @GetMapping(value = "/")
    public String helloAdmin() {
        return "Hello Admin";
    }

    @GetMapping(value = "/airline")
    public ResponseEntity<?> getAirlines(){

        String url = baseUrlAirline + "/airline";
        HttpEntity<?> httpEntity = new HttpEntity<>(null,null);
        ParameterizedTypeReference<?> type = new ParameterizedTypeReference<>() {};
        try{
            return restTemplate.exchange(url, HttpMethod.GET, httpEntity, type );
        }catch (HttpClientErrorException.NotFound e){
            return new ResponseEntity<>(new ErrorResponse(e.getResponseBodyAsString()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/airlineById/{airlineId}")
    public ResponseEntity<?> getAirlines(@PathVariable String airlineId){

        String url = baseUrlAirline.concat("/airlineById/").concat(airlineId);
        HttpEntity<?> httpEntity = new HttpEntity<>(null,null);
        ParameterizedTypeReference<?> type = new ParameterizedTypeReference<>() {};
        try{
            return restTemplate.exchange(url, HttpMethod.GET, httpEntity, type );
        }catch (HttpClientErrorException.NotFound e){
            return new ResponseEntity<>(new ErrorResponse(e.getResponseBodyAsString()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/airline/{airlineName}")
    public ResponseEntity<?> getAirlineByName(@PathVariable String airlineName) throws Exception {

        String url = baseUrlAirline.concat("/airline/").concat(airlineName);
        HttpEntity<?> httpEntity = new HttpEntity<>(null,null);
        ParameterizedTypeReference<String> type = new ParameterizedTypeReference<>() {};
        restTemplate.exchange(url, HttpMethod.GET, httpEntity, type);

        try (Consumer<String, Airline> consumer = consumerFactory.createConsumer()) {
            TopicPartition tp = new TopicPartition("airline_topic", 0);
            consumer.assign(Collections.singleton(tp));
            consumer.seek(tp, 2);
            ConsumerRecords<String, Airline> records = consumer.poll(Duration.ofSeconds(5));
            return new ResponseEntity<>(records.iterator().next().value(), HttpStatus.OK);
        }catch(Exception e){
            throw new Exception("Internal Server Error");
        }
    }

    @PostMapping(value = "/airline")
    public ResponseEntity<?> saveAirline(@RequestBody Airline airline){

        String url = baseUrlAirline.concat("/airline");
        HttpEntity<?> httpEntity = new HttpEntity<>(airline, null);
        ParameterizedTypeReference<?> type = new ParameterizedTypeReference<>() {
        };
        try {
            return restTemplate.exchange(url, HttpMethod.POST, httpEntity, type);
        } catch (HttpClientErrorException.Conflict e) {
            return new ResponseEntity<>(new ErrorResponse(e.getResponseBodyAsString()), HttpStatus.CONFLICT);
        }
    }

    @PutMapping(value = "/airline")
    public ResponseEntity<?> updateAirline(@RequestBody Airline airline){

        String url = baseUrlAirline.concat("/airline");
        HttpEntity<?> httpEntity = new HttpEntity<>(airline, null);
        ParameterizedTypeReference<?> type = new ParameterizedTypeReference<>() {
        };
        try {
            return restTemplate.exchange(url, HttpMethod.PUT, httpEntity, type);
        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>(new ErrorResponse(e.getResponseBodyAsString()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/airline")
    public ResponseEntity<?> deleteAirline(@RequestBody Airline airline){

        String url = baseUrlAirline.concat("/airline");
        HttpEntity<?> httpEntity = new HttpEntity<>(airline, null);
        ParameterizedTypeReference<?> type = new ParameterizedTypeReference<>() {
        };
        try {
            return restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, type);
        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>(new ErrorResponse(e.getResponseBodyAsString()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/schedule")
    public ResponseEntity<?> getFlightSchedule(){

        String url = baseUrlSchedule + "/schedule";
        HttpEntity<?> httpEntity = new HttpEntity<>(null,null);
        ParameterizedTypeReference<?> type = new ParameterizedTypeReference<>() {};
        try{
            return restTemplate.exchange(url, HttpMethod.GET, httpEntity, type );
        }catch (HttpClientErrorException.NotFound e){
            return new ResponseEntity<>(new ErrorResponse(e.getResponseBodyAsString()), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping(value = "/schedule")
    public ResponseEntity<?> saveFlightSchedule(@RequestBody FlightSchedule schedule){

        String url = baseUrlSchedule + "/schedule";
        HttpEntity<?> httpEntity = new HttpEntity<>(schedule,null);
        ParameterizedTypeReference<?> type = new ParameterizedTypeReference<>() {};
        try{
            return restTemplate.exchange(url, HttpMethod.POST, httpEntity, type );
        }catch (HttpClientErrorException.Conflict e){
            return new ResponseEntity<>(new ErrorResponse(e.getResponseBodyAsString()), HttpStatus.CONFLICT);
        }

    }

    @GetMapping(value = "/scheduleById/{scheduleId}")
    public ResponseEntity<?> getFlightScheduleById(@PathVariable String scheduleId){

        String url = baseUrlSchedule.concat("/scheduleById/").concat(scheduleId);
        HttpEntity<?> httpEntity = new HttpEntity<>(null,null);
        ParameterizedTypeReference<?> type = new ParameterizedTypeReference<>() {};
        try{
            return restTemplate.exchange(url, HttpMethod.GET, httpEntity, type );
        }catch (HttpClientErrorException.NotFound e){
            return new ResponseEntity<>(new ErrorResponse(e.getResponseBodyAsString()), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping(value = "/schedule")
    public ResponseEntity<?> saveEditedFlightSchedule(@RequestBody FlightSchedule schedule){

        String url = baseUrlSchedule + "/schedule";
        HttpEntity<?> httpEntity = new HttpEntity<>(schedule,null);
        ParameterizedTypeReference<?> type = new ParameterizedTypeReference<>() {};
        try{
            return restTemplate.exchange(url, HttpMethod.PUT, httpEntity, type );
        }catch (HttpClientErrorException.Conflict e){
            return new ResponseEntity<>(new ErrorResponse(e.getResponseBodyAsString()), HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping(value = "/schedule")
    public ResponseEntity<?> deleteFlightSchedule(@RequestBody FlightSchedule schedule){

        String url = baseUrlSchedule + "/schedule";
        HttpEntity<?> httpEntity = new HttpEntity<>(schedule,null);
        ParameterizedTypeReference<?> type = new ParameterizedTypeReference<>() {};
        try{
            return restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, type );
        }catch (HttpClientErrorException.NotFound e){
            return new ResponseEntity<>(new ErrorResponse(e.getResponseBodyAsString()), HttpStatus.NOT_FOUND);
        }

    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException() {
        return new ResponseEntity<>(new ErrorResponse("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
