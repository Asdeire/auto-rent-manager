package com.asdeire.persistence.entities;

import com.asdeire.persistence.entities.impl.Entity;

import java.util.Date;

public record Review(int id, int userId, int carId, double rating, String comment,
                     Date date) implements Entity {
    public int getUserID() {
        return userId;
    }

    public int getCarID() {
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

    public int getId() {
    return id;
    }
}
