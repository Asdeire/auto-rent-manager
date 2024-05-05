package com.asdeire.persistence.entities;

import com.asdeire.persistence.entities.impl.Entity;

public record User(int id, String username, String email, String password, double balance) implements Entity {
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public int getId() {
        return id;
    }
}
