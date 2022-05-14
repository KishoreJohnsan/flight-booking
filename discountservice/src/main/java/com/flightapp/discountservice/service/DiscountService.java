package com.flightapp.discountservice.service;

import com.flightapp.discountservice.entity.Discount;
import com.flightapp.discountservice.exception.DiscountAlreadyExistsException;
import com.flightapp.discountservice.exception.DiscountNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DiscountService {

    List<Discount> getAllDiscounts() throws DiscountNotFoundException;
    Discount getDiscountByCode(String name) throws DiscountNotFoundException;
    boolean saveDiscount(Discount discount) throws DiscountAlreadyExistsException;
    boolean updateDiscount(Discount discount) throws DiscountNotFoundException;
    boolean deleteDiscount(Long id) throws DiscountNotFoundException;
}
