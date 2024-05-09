package com.asdeire.persistence.entities;

import java.util.Date;
import java.util.UUID;

public record Review(UUID id, UUID userId, UUID carId, double rating, String comment,
                     Date date) {
    public UUID getUserID() {
        return userId;
    }

    public UUID getCarID() {
        return carId;
    }

    public double getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date.toString();
    }

    public UUID getId() {
        return id;
    }

}
