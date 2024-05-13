package com.asdeire.domain.service.impl;

import com.asdeire.persistence.entities.Rental;
import com.asdeire.persistence.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createRental(Rental rental) {
        rentalRepository.create(rental);
    }
}
