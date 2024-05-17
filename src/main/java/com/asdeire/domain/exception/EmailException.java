package com.asdeire.domain.exception;

/**
 * An exception indicating an issue related to email processing.
 */
public class EmailException extends RuntimeException {

    /**
     * Constructs a new email exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public EmailException(String message) {
        super(message);
    }
}
