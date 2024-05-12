package com.asdeire.persistence.entities;

import com.asdeire.persistence.entities.impl.Entity;

import java.util.UUID;


public record User(UUID id, String username, String email, String password, double balance)  {

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

    public UUID getId() {
        return id;
    }
}
