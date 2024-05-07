package com.asdeire.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.UUID;

public record UserUpdateDto(
        @NotNull(message = "Відсутній іденитфікатор користувача")
        UUID id,

        @Size(min = 6, max = 32, message = "Ім'я користувача має містити від 6 до 32 символів")
        String username,

        @Email(message = "Неправильний формат електронної пошти")
        @Size(max = 128, message = "Email не повинен перевищувати 128 символів")
        String email,

        @Size(min = 8, max = 72, message = "Пароль повинен містити від 8 до 72 символів")
        String password,

        Path avatar,

        @Past(message = "День народження має бути в минулому")
        LocalDate birthday

        ) {

}