package com.asdeire.persistence.entities;

import java.util.UUID;

/**
 * Represents a user entity in the system.
 * Each user has a unique identifier, username, email, password, and balance.
 */
public record User(UUID id, String username, String email, String password, Double balance) {

    /** Retrieves the unique identifier of the user. */
    public UUID getId() {
        return id;
    }

    /** Retrieves the username of the user. */
    public String getUsername() {
        return username;
    }

    /** Retrieves the email of the user. */
    public String getEmail() {
        return email;
    }

    /** Retrieves the password of the user. */
    public String getPassword() {
        return password;
    }

    /** Retrieves the balance of the user. */
    public Double getBalance() {
        return balance;
    }
}
