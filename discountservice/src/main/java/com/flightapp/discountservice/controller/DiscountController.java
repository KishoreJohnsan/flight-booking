package com.flightapp.discountservice.controller;

import com.flightapp.discountservice.entity.Discount;
import com.flightapp.discountservice.entity.ErrorResponse;
import com.flightapp.discountservice.exception.DiscountAlreadyExistsException;
import com.flightapp.discountservice.exception.DiscountNotFoundException;
import com.flightapp.discountservice.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/flight/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping(value = "/discount")
    public ResponseEntity<List<Discount>> getAllAirlines() throws DiscountNotFoundException {
        return new ResponseEntity<>(discountService.getAllDiscounts(), HttpStatus.OK);
    }

    @GetMapping(value = "/discount/{discountCode}")
    public ResponseEntity<Discount> getDiscountByName(@PathVariable String discountCode) throws DiscountNotFoundException {
        return new ResponseEntity<>(discountService.getDiscountByCode(discountCode), HttpStatus.OK);
    }

    @PostMapping(value = "/discount")
    public ResponseEntity<Boolean> saveDiscount(@RequestBody Discount discount) throws DiscountAlreadyExistsException {
        return new ResponseEntity<>(discountService.saveDiscount(discount), HttpStatus.CREATED);
    }

    @PutMapping(value = "/discount")
    public ResponseEntity<Boolean> updateDiscount(@RequestBody Discount discount) throws DiscountNotFoundException {
        return new ResponseEntity<>(discountService.updateDiscount(discount), HttpStatus.OK);

    }

    @DeleteMapping(value = "/discount")
    public ResponseEntity<Boolean> deleteDiscount(@RequestBody Discount discount) throws DiscountNotFoundException {
        return new ResponseEntity<>(discountService.deleteDiscount(discount.getDiscountId()), HttpStatus.OK);
    }

    @ExceptionHandler(DiscountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDiscountNotFound(){
        return new ResponseEntity<>(new ErrorResponse("Discount not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DiscountAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleDiscountAlreadyFound(){
        return new ResponseEntity<>(new ErrorResponse("Discount already present"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(){
        return  new ResponseEntity<>(new ErrorResponse("Internal Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
