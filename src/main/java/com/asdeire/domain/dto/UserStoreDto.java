package com.asdeire.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * A record representing a data transfer object for creating a user.
 */
public record UserStoreDto(
        /**
         * The username.
         */
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 6, max = 32, message = "Username must be between 6 and 32 characters")
        String username,

        /**
         * The user's email.
         */
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        @Size(max = 128, message = "Email must not exceed 128 characters")
        String email,

        /**
         * The user's password.
         */
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
        String password

) {

}
