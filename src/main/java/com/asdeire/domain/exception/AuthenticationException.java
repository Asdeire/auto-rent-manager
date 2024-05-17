package com.asdeire.domain.exception;

/**
 * An exception indicating authentication failure.
 */
public class AuthenticationException extends RuntimeException {

    /**
     * Constructs a new authentication exception with a default message.
     */
    public AuthenticationException() {
        super("Incorrect username or password.");
    }
}
