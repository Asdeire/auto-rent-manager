package com.asdeire.persistence;


import com.asdeire.persistence.entities.User;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.UUID;


@Configuration
@ComponentScan("com.asdeire")
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

    @Value("${mail.smtp.host}")
    private String EMAIL_HOST;
    @Value("${mail.smtp.port}")
    private String EMAIL_PORT;
    @Value("${mail.smtp.auth}")
    private String EMAIL_AUTH;
    @Value("${mail.smtp.starttls.enable}")
    private String EMAIL_TLS;
    @Value("${mail.smtp.username}")
    private String EMAIL_USERNAME;
    @Value("${mail.smtp.password}")
    private String EMAIL_PASSWORD;

    @Bean
    Validator validator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            return factory.getValidator();
        }
    }

    @Bean
    Session session() {
        // Властивості для конфігурації підключення до поштового сервера
        Properties properties = new Properties();
        properties.put("mail.smtp.host", EMAIL_HOST);
        properties.put("mail.smtp.port", EMAIL_PORT);
        properties.put("mail.smtp.auth", EMAIL_AUTH);
        properties.put("mail.smtp.starttls.enable", EMAIL_TLS);

        // Отримання сесії з автентифікацією
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}