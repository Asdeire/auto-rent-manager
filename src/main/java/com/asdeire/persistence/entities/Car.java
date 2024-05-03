package com.asdeire.persistence.entities;

import com.asdeire.persistence.entities.impl.Entity;

public record Car(int id, String brand, String model, int year, int categoryId, Double rating,
                  Boolean availability) implements Entity {
}
