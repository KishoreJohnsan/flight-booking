package com.flightapp.discountservice.service;

import com.flightapp.discountservice.entity.Discount;
import com.flightapp.discountservice.exception.DiscountAlreadyExistsException;
import com.flightapp.discountservice.exception.DiscountNotFoundException;
import com.flightapp.discountservice.repo.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private DiscountRepository repo;

    @Override
    public List<Discount> getAllDiscounts() throws DiscountNotFoundException {
        List<Discount> discountList = repo.findAll();
        if (discountList.isEmpty())
            throw new DiscountNotFoundException();
        else
            return discountList;
    }

    @Override
    public Discount getDiscountByCode(String code) throws DiscountNotFoundException {
        Optional<Discount> discountOpt = repo.findByDiscountCode(code);
        if (discountOpt.isPresent()) {
            return discountOpt.get();
        } else
            throw new DiscountNotFoundException();
    }

    @Override
    public boolean saveDiscount(Discount discount) throws DiscountAlreadyExistsException {
        Optional<Discount> discountOpt = repo.findByDiscountCode(discount.getDiscountCode());
        if (discountOpt.isPresent()) {
            throw new DiscountAlreadyExistsException();
        } else
            repo.save(discount);

        return true;

    }

    @Override
    public boolean updateDiscount(Discount discount) throws DiscountNotFoundException {
        Optional<Discount> discountOpt = repo.findById(discount.getDiscountId());
        if (discountOpt.isPresent()) {
            repo.save(discount);
        } else
            throw new DiscountNotFoundException();
        return true;
    }

    @Override
    public boolean deleteDiscount(Long id) throws DiscountNotFoundException {
        Optional<Discount> discountOpt = repo.findById(id);
        if (discountOpt.isPresent()) {
            repo.deleteById(id);
        } else
            throw new DiscountNotFoundException();
        return true;

    }
}
