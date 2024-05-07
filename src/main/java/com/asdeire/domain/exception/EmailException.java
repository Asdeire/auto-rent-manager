package com.asdeire.domain.exception;

import java.io.Serial;

public class EmailException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5195839463159306456L;

    public EmailException(String message) {
        super(message);
    }
}