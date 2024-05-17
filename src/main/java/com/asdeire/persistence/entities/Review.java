package com.asdeire.persistence.entities;

import java.util.Date;
import java.util.UUID;

/**
 * Represents a review entity in the system.
 * Each review has a unique identifier, user ID, car ID, rating, comment, and date.
 */
public record Review(UUID id, UUID userId, UUID carId, double rating, String comment,
                     Date date) {
    /** Retrieves the unique identifier of the review. */
    public UUID getId() {
        return id;
    }

    /** Retrieves the user ID associated with the review. */
    public UUID getUserID() {
        return userId;
    }

    /** Retrieves the car ID associated with the review. */
    public UUID getCarID() {
        return carId;
    }

    /** Retrieves the rating of the review. */
    public double getRating() {
        return rating;
    }

    /** Retrieves the comment of the review. */
    public String getComment() {
        return comment;
    }

    /** Retrieves the date of the review. */
    public Date getDate() {
        return date;
    }
}

