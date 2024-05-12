package com.asdeire.domain.service.impl;

import com.asdeire.domain.dto.UserStoreDto;
import com.asdeire.domain.exception.ValidationException;
import com.asdeire.persistence.entities.User;
import com.asdeire.persistence.repository.impl.UserRepository;
import com.password4j.Password;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final Validator validator;
    private final UserRepository userRepository;

    @Autowired
    public UserService(Validator validator, UserRepository userRepository) {
        this.validator = validator;
        this.userRepository = userRepository;
    }

    public User create(UserStoreDto userStoreDto) {
        var violations = validator.validate(userStoreDto);
        if (!violations.isEmpty()) {
            throw ValidationException.create("збереженні користувача", violations);
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

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Користувача з таким ім'ям не знайдено: " + username));
    }
}
