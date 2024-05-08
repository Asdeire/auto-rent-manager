package com.asdeire.persistence.entities;

import com.asdeire.persistence.entities.impl.Entity;

import java.util.UUID;

public record Car(UUID id, String brand, String model, int year, UUID categoryId, Double rating,
                  Boolean availability) implements Entity {
    public String getBrand() {
        return brand;
    }

    public UUID getId() {
        return id;
    }

    public Boolean isAvailability() {
        return availability;
    }

    public Double getRating() {
        return rating;
    }


    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

}
