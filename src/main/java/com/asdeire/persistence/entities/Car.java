package com.asdeire.persistence.entities;

import com.asdeire.persistence.entities.impl.Entity;

import java.util.UUID;

/**
 * Represents a car entity in the system.
 * Each car has a unique identifier, brand, model, year of manufacture,
 * category ID, rating, availability status, and price.
 */
public record Car(
        /** The unique identifier of the car. */
        UUID id,
        /** The brand of the car. */
        String brand,
        /** The model of the car. */
        String model,
        /** The year of manufacture of the car. */
        int year,
        /** The unique identifier of the category to which the car belongs. */
        UUID categoryId,
        /** The rating of the car. */
        Double rating,
        /** The availability status of the car. */
        Boolean availability,
        /** The price of the car. */
        Double price
) implements Entity {
    /**
     * Retrieves the brand of the car.
     *
     * @return The brand of the car.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Retrieves the unique identifier of the car.
     *
     * @return The unique identifier of the car.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Checks if the car is available for rental.
     *
     * @return True if the car is available, false otherwise.
     */
    public Boolean isAvailability() {
        return availability;
    }

    /**
     * Retrieves the rating of the car.
     *
     * @return The rating of the car.
     */
    public Double getRating() {
        return rating;
    }

    /**
     * Retrieves the price of the car.
     *
     * @return The price of the car.
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Retrieves the model of the car.
     *
     * @return The model of the car.
     */
    public String getModel() {
        return model;
    }

    /**
     * Retrieves the year of manufacture of the car.
     *
     * @return The year of manufacture of the car.
     */
    public int getYear() {
        return year;
    }

    /**
     * Retrieves the unique identifier of the category to which the car belongs.
     *
     * @return The unique identifier of the category.
     */
    public UUID getCategoryId() {
        return categoryId;
    }
}

