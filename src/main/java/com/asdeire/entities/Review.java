package com.asdeire.entities;

import com.asdeire.entities.impl.Entity;

public record Review(int reviewId, int userId, int carId, double rating, String comment,
                     java.sql.Date date) implements Entity {
}
