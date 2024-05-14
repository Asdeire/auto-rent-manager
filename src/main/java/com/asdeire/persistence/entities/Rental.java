package com.asdeire.persistence.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Rental {
    private UUID rentalId;
    private UUID userId;
    private UUID carId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double price;

    // Конструктор
    public Rental(UUID rentalId, UUID userId, UUID carId, LocalDate startDate, LocalDate endDate, Double price) {
        this.rentalId = rentalId;
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }
    public Rental(){};


    public UUID getRentalId() {
        return rentalId;
    }

    public void setRentalId(UUID rentalId) {
        this.rentalId = rentalId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getCarId() {
        return carId;
    }

    public void setCarId(UUID carId) {
        this.carId = carId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
