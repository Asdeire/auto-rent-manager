package com.asdeire.persistence.entities;

import com.asdeire.persistence.entities.impl.Entity;

import java.util.UUID;

public record Category(UUID id, String name, String description)  {
    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }

    public UUID getId() {
        return id;
    }
}
