package com.asdeire.persistence.entities;

import java.util.UUID;

/**
 * Represents a category entity in the system.
 * Each category has a unique identifier, name, and description.
 */
public record Category(
        /** The unique identifier of the category. */
        UUID id,
        /** The name of the category. */
        String name,
        /** The description of the category. */
        String description
) {
    /**
     * Retrieves the name of the category.
     *
     * @return The name of the category.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the description of the category.
     *
     * @return The description of the category.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieves the unique identifier of the category.
     *
     * @return The unique identifier of the category.
     */
    public UUID getId() {
        return id;
    }
}
