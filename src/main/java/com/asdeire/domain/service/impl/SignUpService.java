package com.asdeire.domain.service.impl;

import com.asdeire.domain.dto.UserStoreDto;
import com.asdeire.domain.exception.EmailException;
import com.asdeire.domain.exception.SignUpException;
import com.asdeire.persistence.entities.User;
import com.asdeire.persistence.repository.UserRepository;
import com.password4j.Password;
import jakarta.mail.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.InternetAddress;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * A service responsible for user sign-up operations.
 */
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

    /**
     * Constructs a new SignUpService with the specified UserRepository and Session.
     *
     * @param userRepository the user repository.
     * @param session        the mail session.
     */
    SignUpService(UserRepository userRepository, Session session) {
        this.userRepository = userRepository;
        this.session = session;
    }

    /**
     * Signs up a new user.
     *
     * @param username           the username.
     * @param password           the password.
     * @param email              the email.
     * @param balance            the balance.
     * @param waitForUserInput   a supplier to wait for user input.
     * @throws SignUpException if an error occurs during sign-up.
     */
    public void signUp(String username,
                       String password,
                       String email,
                       double balance,
                       Supplier<String> waitForUserInput) {
        try {
            String verificationCode = generateAndSendVerificationCode(email);
            String userInputCode = waitForUserInput.get();

            verifyCode(userInputCode, verificationCode);

            // Add the user if verification is successful
            userRepository.add(
                    new User(UUID.randomUUID(),
                            Password.hash(userStoreDto.password()).withBcrypt().getResult(),
                            email,
                            username,
                            balance)
            );

        } catch (Exception e) {
            throw new SignUpException("Error saving user: %s"
                    .formatted(e.getMessage()));
        }
    }

    // Email sending
    private void sendVerificationCodeEmail(String email, String verificationCode) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Verification Code");
            message.setText("Your verification code: " + verificationCode);
            Transport.send(message);

            LOGGER.info("Message successfully sent.");
        } catch (MessagingException e) {
            throw new EmailException("Error sending email: " + e.getMessage());
        }
    }

    private String generateAndSendVerificationCode(String email) {
        // Generate a 6-digit code
        String verificationCode = String.valueOf((int) (Math.random() * 900000 + 100000));

        sendVerificationCodeEmail(email, verificationCode);

        codeCreationTime = LocalDateTime.now();

        return verificationCode;
    }

    // Code verification
    private static void verifyCode(String inputCode, String generatedCode) {
        LocalDateTime currentTime = LocalDateTime.now();
        long minutesElapsed = ChronoUnit.MINUTES.between(codeCreationTime, currentTime);

        if (minutesElapsed > VERIFICATION_CODE_EXPIRATION_MINUTES) {
            throw new SignUpException("Verification time expired. Please try again.");
        }

        if (!inputCode.equals(generatedCode)) {
            throw new SignUpException("Incorrect verification code.");
        }

        // Reset code creation time
        codeCreationTime = null;
    }
}
