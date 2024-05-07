package com.asdeire.domain.service.impl;

import com.asdeire.domain.dto.UserStoreDto;
import com.asdeire.domain.exception.EmailException;
import com.asdeire.domain.exception.SignUpException;
import com.asdeire.persistence.entities.User;
import com.asdeire.persistence.repository.impl.UserRepository;
import com.password4j.Password;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.function.Supplier;

@Service
final class SignUpService {

    @Value("${mail.smtp.from}")
    private String EMAIL_FROM;
    private static final int VERIFICATION_CODE_EXPIRATION_MINUTES = 1;
    private static LocalDateTime codeCreationTime;
    private final UserRepository userRepository;
    private final Session session;
    private UserStoreDto userStoreDto;
    final Logger LOGGER = LoggerFactory.getLogger(SignUpService.class);


    SignUpService(UserRepository userRepository, Session session) {
        this.userRepository = userRepository;
        this.session = session;
    }

    public void signUp(String username,
                       String password,
                       String email,
                       int balance,
                       Supplier<String> waitForUserInput) {
        try {
            String verificationCode = generateAndSendVerificationCode(email);
            String userInputCode = waitForUserInput.get();

            verifyCode(userInputCode, verificationCode);

            // Додаємо користувача, якщо перевірка успішна
            userRepository.add(
                    new User(UUID.randomUUID(),
                            Password.hash(userStoreDto.password()).withBcrypt().getResult(),
                            email,
                            username,
                            balance)
            );

        } catch (Exception e) {
            throw new SignUpException("Помилка при збереженні користувача: %s"
                    .formatted(e.getMessage()));
        }
    }

    // відправлення на пошту
    private void sendVerificationCodeEmail(String email, String verificationCode) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Код підтвердження");
            message.setText("Ваш код підтвердження: " + verificationCode);
            Transport.send(message);

            LOGGER.info("Повідомлення успішно відправлено.");
        } catch (MessagingException e) {
            throw new EmailException("Помилка при відправці електронного листа: " + e.getMessage());
        }
    }

    private String generateAndSendVerificationCode(String email) {
        // Генерація 6-значного коду
        String verificationCode = String.valueOf((int) (Math.random() * 900000 + 100000));

        sendVerificationCodeEmail(email, verificationCode);

        codeCreationTime = LocalDateTime.now();

        return verificationCode;
    }

    // Перевірка введеного коду
    private static void verifyCode(String inputCode, String generatedCode) {
        LocalDateTime currentTime = LocalDateTime.now();
        long minutesElapsed = ChronoUnit.MINUTES.between(codeCreationTime, currentTime);

        if (minutesElapsed > VERIFICATION_CODE_EXPIRATION_MINUTES) {
            throw new SignUpException("Час верифікації вийшов. Спробуйте ще раз.");
        }

        if (!inputCode.equals(generatedCode)) {
            throw new SignUpException("Невірний код підтвердження.");
        }

        // Скидання часу створення коду
        codeCreationTime = null;
    }
}