package com.asdeire.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.UUID;

/**
 * A record representing a data transfer object for updating a user.
 */
public record UserUpdateDto(
        /**
         * The user's identifier.
         */
        @NotNull(message = "User identifier is missing")
        UUID id,

        /**
         * The username.
         */
        @Size(min = 6, max = 32, message = "Username must be between 6 and 32 characters")
        String username,

        /**
         * The user's email.
         */
        @Email(message = "Invalid email format")
        @Size(max = 128, message = "Email must not exceed 128 characters")
        String email,

        /**
         * The user's password.
         */
        @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
        String password,

        /**
         * The path to the user's avatar.
         */
        Path avatar,

        /**
         * The user's birthday.
         */
        @Past(message = "Birthday must be in the past")
        LocalDate birthday

) {

}
