package com.asdeire.persistence.entities;

import com.asdeire.persistence.entities.impl.Entity;

public record Car(int id, String brand, String model, int year, int categoryId, Double rating,
                  Boolean availability) implements Entity {
    public String getBrand() {
        return brand;
    }

    public int getId() {
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

    public int getCategoryID() {
   return categoryId;
    }
}
