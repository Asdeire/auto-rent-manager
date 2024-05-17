package com.asdeire.domain.exception;

/**
 * An exception indicating an issue related to user sign-up.
 */
public class SignUpException extends RuntimeException {

    /**
     * Constructs a new sign-up exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public SignUpException(String message) {
        super(message);
    }
}
