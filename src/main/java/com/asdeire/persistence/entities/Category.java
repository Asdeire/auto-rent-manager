package com.asdeire.persistence.entities;

import com.asdeire.persistence.entities.impl.Entity;

public record Category(int id, String name, String description) implements Entity {
}
