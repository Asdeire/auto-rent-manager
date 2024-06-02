package com.asdeire.presentation.viewmodel;

import java.time.LocalDate;

public class RentalViewModel {

    private String carName;
    private LocalDate startDate;
    private LocalDate endDate;
    private double price;

    public RentalViewModel(String carName, LocalDate startDate, LocalDate endDate, double price) {
        this.carName = carName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }

    public String getCarName() {
        return carName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getPrice() {
        return price;
    }
}