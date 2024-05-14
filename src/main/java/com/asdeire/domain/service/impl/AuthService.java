package com.asdeire.domain.service.impl;

import com.asdeire.domain.exception.AuthenticationException;
import com.asdeire.persistence.entities.User;
import com.asdeire.persistence.repository.UserRepository;
import com.asdeire.domain.exception.UserAlreadyAuthenticatedException;
import com.password4j.Password;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private User currentUser;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authenticate(String username, String password) {
        // Перевіряємо, чи вже існує аутентифікований користувач
        if (currentUser != null) {
            throw new UserAlreadyAuthenticatedException("Ви вже авторизувалися як: %s"
                    .formatted(currentUser.getUsername()));
        }

        User foundUser = userRepository.findByUsername(username)
                .orElseThrow(AuthenticationException::new);

        if (!Password.check(password, foundUser.getPassword()).withBcrypt()) {
            return false;
        }

        currentUser = foundUser;
        return true;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        if (currentUser == null) {
            throw new UserAlreadyAuthenticatedException("Ви ще не автентифікавані.");
        }
        currentUser = null;
    }
}
