package com.asdeire.domain.service.impl;

import com.asdeire.domain.dto.UserStoreDto;
import com.asdeire.domain.exception.ValidationException;
import com.asdeire.persistence.entities.User;
import com.asdeire.persistence.repository.UserRepository;
import com.password4j.Password;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * A service responsible for user management operations.
 */
@Service
public class UserService {

    private final Validator validator;
    private final UserRepository userRepository;

    /**
     * Constructs a new UserService with the specified Validator and UserRepository.
     *
     * @param validator      the validator.
     * @param userRepository the user repository.
     */
    @Autowired
    public UserService(Validator validator, UserRepository userRepository) {
        this.validator = validator;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user based on the provided UserStoreDto.
     *
     * @param userStoreDto the user data.
     * @return the created user.
     * @throws ValidationException if validation fails.
     */
    public User create(UserStoreDto userStoreDto) {
        var violations = validator.validate(userStoreDto);
        if (!violations.isEmpty()) {
            throw ValidationException.create("saving user", violations);
        }

        User user = new User(
                UUID.randomUUID(),
                userStoreDto.username(),
                userStoreDto.email(),
                Password.hash(userStoreDto.password()).withBcrypt().getResult(),
                20000.00
        );

        userRepository.add(user);

        return user;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username.
     * @return the user with the specified username.
     * @throws RuntimeException if the user is not found.
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    /**
     * Updates an existing user.
     *
     * @param user the user to update.
     */
    public void update(User user) {
        userRepository.update(user);
    }
}
