package com.asdeire.domain.service.impl;

import com.asdeire.domain.exception.AuthenticationException;
import com.asdeire.domain.exception.UserAlreadyAuthenticatedException;
import com.asdeire.persistence.entities.User;
import com.asdeire.persistence.repository.UserRepository;
import com.password4j.Password;
import org.springframework.stereotype.Service;

/**
 * A service responsible for authentication operations.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private User currentUser;

    /**
     * Constructs a new AuthService with the specified UserRepository.
     *
     * @param userRepository the user repository.
     */
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Authenticates a user with the given username and password.
     *
     * @param username the username.
     * @param password the password.
     * @return true if authentication is successful, false otherwise.
     * @throws UserAlreadyAuthenticatedException if a user is already authenticated.
     * @throws AuthenticationException          if authentication fails.
     */
    public boolean authenticate(String username, String password) {
        // Check if a user is already authenticated
        if (currentUser != null) {
            throw new UserAlreadyAuthenticatedException("You are already authenticated as: " + currentUser.getUsername());
        }

        User foundUser = userRepository.findByUsername(username)
                .orElseThrow(AuthenticationException::new);

        if (!Password.check(password, foundUser.getPassword()).withBcrypt()) {
            return false;
        }

        currentUser = foundUser;
        return true;
    }

    /**
     * Checks if a user is authenticated.
     *
     * @return true if a user is authenticated, false otherwise.
     */
    public boolean isAuthenticated() {
        return currentUser != null;
    }

    /**
     * Gets the currently authenticated user.
     *
     * @return the currently authenticated user.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Logs out the currently authenticated user.
     *
     * @throws UserAlreadyAuthenticatedException if no user is currently authenticated.
     */
    public void logout() {
        if (currentUser == null) {
            throw new UserAlreadyAuthenticatedException("You are not authenticated yet.");
        }
        currentUser = null;
    }
}
