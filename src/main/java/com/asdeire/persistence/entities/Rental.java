package com.asdeire.persistence.entities;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents a rental entity in the system.
 * Each rental has a unique identifier, user ID, car ID, start date, end date, and price.
 */
public class Rental {
    /** The unique identifier of the rental. */
    private UUID rentalId;
    /** The user ID associated with the rental. */
    private UUID userId;
    /** The car ID associated with the rental. */
    private UUID carId;
    /** The start date of the rental period. */
    private LocalDate startDate;
    /** The end date of the rental period. */
    private LocalDate endDate;
    /** The price of the rental. */
    private Double price;

    /**
     * Constructs a new Rental object with the specified attributes.
     *
     * @param rentalId   The unique identifier of the rental.
     * @param userId     The user ID associated with the rental.
     * @param carId      The car ID associated with the rental.
     * @param startDate  The start date of the rental period.
     * @param endDate    The end date of the rental period.
     * @param price      The price of the rental.
     */
    public Rental(UUID rentalId, UUID userId, UUID carId, LocalDate startDate, LocalDate endDate, Double price) {
        this.rentalId = rentalId;
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }

    /** Default constructor */
    public Rental() {}

    /** Retrieves the unique identifier of the rental. */
    public UUID getRentalId() {
        return rentalId;
    }

    /** Sets the unique identifier of the rental. */
    public void setRentalId(UUID rentalId) {
        this.rentalId = rentalId;
    }

    /** Retrieves the user ID associated with the rental. */
    public UUID getUserId() {
        return userId;
    }

    /** Sets the user ID associated with the rental. */
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    /** Retrieves the car ID associated with the rental. */
    public UUID getCarId() {
        return carId;
    }

    /** Sets the car ID associated with the rental. */
    public void setCarId(UUID carId) {
        this.carId = carId;
    }

    /** Retrieves the start date of the rental period. */
    public LocalDate getStartDate() {
        return startDate;
    }

    /** Sets the start date of the rental period. */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /** Retrieves the end date of the rental period. */
    public LocalDate getEndDate() {
        return endDate;
    }

    /** Sets the end date of the rental period. */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /** Retrieves the price of the rental. */
    public Double getPrice() {
        return price;
    }

    /** Sets the price of the rental. */
    public void setPrice(Double price) {
        this.price = price;
    }
}
