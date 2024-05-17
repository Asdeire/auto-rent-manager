package com.asdeire.domain.exception;

import jakarta.validation.ConstraintViolation;
import java.io.Serial;
import java.util.Set;

/**
 * An exception indicating validation failure.
 */
public class ValidationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3145152535586258949L;

    private final Set<? extends ConstraintViolation<?>> violations;

    /**
     * Constructs a new validation exception with the specified detail message and violations.
     *
     * @param message    the detail message.
     * @param violations the set of constraint violations.
     */
    public ValidationException(String message, Set<? extends ConstraintViolation<?>> violations) {
        super(message);
        this.violations = violations;
    }

    /**
     * Gets the set of constraint violations.
     *
     * @return the set of constraint violations.
     */
    public Set<? extends ConstraintViolation<?>> getViolations() {
        return violations;
    }

    /**
     * Creates a new validation exception with the specified suffix and violations.
     *
     * @param suffix     the suffix to add to the error message.
     * @param violations the set of constraint violations.
     * @return the created validation exception.
     */
    public static ValidationException create(String suffix, Set<? extends ConstraintViolation<?>> violations) {
        return new ValidationException("Validation error: " + suffix, violations);
    }
}
