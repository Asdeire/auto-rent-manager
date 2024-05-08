package com.asdeire.domain.service.impl;

import com.asdeire.domain.dto.UserStoreDto;
import com.asdeire.domain.exception.ValidationException;
import com.asdeire.persistence.entities.User;
import com.asdeire.persistence.repository.impl.UserRepository;
import com.password4j.Password;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private Validator validator;
    private UserRepository userRepository;
    public User create(UserStoreDto userStoreDto) {
        var violations = validator.validate(userStoreDto);
        if (!violations.isEmpty()) {
            throw ValidationException.create("збереженні користувача", violations);
        }

        User user = new User(
                null,
                userStoreDto.username(),
                userStoreDto.email(),
                Password.hash(userStoreDto.password()).withBcrypt().getResult(),
                100000.00
        );

        userRepository.add(user);
return user;
    }
}
