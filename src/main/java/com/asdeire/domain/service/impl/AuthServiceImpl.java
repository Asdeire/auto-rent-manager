package com.asdeire.domain.service.impl;


import com.asdeire.domain.exception.AuthenticationException;
import com.asdeire.persistence.entities.User;
import com.asdeire.persistence.repository.impl.UserRepository;
import com.asdeire.domain.exception.UserAlreadyAuthenticatedException;
import com.password4j.Password;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl {

    private final UserRepository userRepository;
    private User user;


    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authenticate(String username, String password) {
        // Перевіряємо, чи вже існує аутентифікований користувач
        if (user != null) {
            throw new UserAlreadyAuthenticatedException("Ви вже авторизувалися як: %s"
                    .formatted(user.getUsername()));
        }

        User foundedUser = userRepository.findByUsername(username)
                .orElseThrow(AuthenticationException::new);

        if (!Password.check(password, foundedUser.password()).withBcrypt()) {
            return false;
        }

        user = foundedUser;
        return true;
    }


    public boolean isAuthenticated() {
        return user != null;
    }


    public User getUser() {
        return user;
    }

    public void logout() {
        if (user == null) {
            throw new UserAlreadyAuthenticatedException("Ви ще не автентифікавані.");
        }
        user = null;
    }
}
