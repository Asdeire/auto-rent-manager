package com.asdeire.domain.service.impl;

import com.asdeire.persistence.entities.Rental;
import com.asdeire.persistence.entities.User;
import com.asdeire.persistence.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;

    @Autowired
    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }


    public void createRental(Rental rental) {
        rentalRepository.create(rental);
    }

    public boolean hasSufficientFunds(User user, Double price) {
        Double balance = user.getBalance();
        return balance.compareTo(price) >= 0;
    }
}
