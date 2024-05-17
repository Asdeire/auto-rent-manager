package com.asdeire.domain.service.impl;

import com.asdeire.persistence.entities.Rental;
import com.asdeire.persistence.entities.User;
import com.asdeire.persistence.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * A service responsible for rental operations.
 */
@Service
public class RentalService {

    private final RentalRepository rentalRepository;

    /**
     * Constructs a new RentalService with the specified RentalRepository.
     *
     * @param rentalRepository the rental repository.
     */
    @Autowired
    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    /**
     * Creates a new rental.
     *
     * @param rental the rental to create.
     */
    public void createRental(Rental rental) {
        rentalRepository.create(rental);
    }

    /**
     * Checks if a user has sufficient funds to afford a rental.
     *
     * @param user  the user.
     * @param price the price of the rental.
     * @return true if the user has sufficient funds, false otherwise.
     */
    public boolean hasSufficientFunds(User user, Double price) {
        Double balance = user.getBalance();
        return balance.compareTo(price) >= 0;
    }
}
