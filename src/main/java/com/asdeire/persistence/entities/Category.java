package com.asdeire.persistence.entities;

import com.asdeire.persistence.entities.impl.Entity;

public record Category(int id, String name, String description) implements Entity {
    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }
}
