package com.asdeire.presentation.viewmodel;

import javafx.beans.property.*;

import java.util.UUID;
import java.util.StringJoiner;

/**
 * View model class representing user information for use in JavaFX applications.
 */
public class UserViewModel {

    private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final DoubleProperty balance = new SimpleDoubleProperty();

    /**
     * Constructs a UserViewModel with the specified parameters.
     *
     * @param id       The unique identifier of the user.
     * @param username The username of the user.
     * @param email    The email address of the user.
     * @param password The password of the user.
     * @param balance  The balance of the user.
     */
    public UserViewModel(UUID id, String username, String email, String password, Double balance) {
        this.id.set(id);
        this.username.set(username);
        this.email.set(email);
        this.password.set(password);
        this.balance.set(balance);
    }

    public UUID getId() {
        return id.get();
    }

    public ObjectProperty<UUID> idProperty() {
        return id;
    }

    public void setId(UUID id) {
        this.id.set(id);
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public Double getBalance() {
        return balance.get();
    }

    public DoubleProperty balanceProperty() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance.set(balance);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserViewModel.class.getSimpleName() + "[", "]")
                .add("id=" + id.get())
                .add("username=" + username.get())
                .add("email=" + email.get())
                .add("password=" + password.get())
                .add("balance=" + balance.get())
                .toString();
    }
}
