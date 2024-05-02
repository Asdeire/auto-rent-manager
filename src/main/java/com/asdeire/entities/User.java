package com.asdeire.entities;

import com.asdeire.entities.impl.Entity;

public record User(int id, String username, String email, String password, double balance) implements Entity {
}
