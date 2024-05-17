package com.asdeire.domain.exception;

/**
 * An exception indicating that a user is already authenticated.
 */
public class UserAlreadyAuthenticatedException extends RuntimeException {

    /**
     * Constructs a new user already authenticated exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public UserAlreadyAuthenticatedException(String message) {
        super(message);
    }
}
