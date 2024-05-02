package com.asdeire.entities;

import com.asdeire.entities.impl.Entity;

public record Category(int id, String name, String description) implements Entity {
}
