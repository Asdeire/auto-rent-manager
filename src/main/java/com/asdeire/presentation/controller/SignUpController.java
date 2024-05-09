package com.asdeire.presentation.controller;

import com.asdeire.domain.dto.UserStoreDto;
import com.asdeire.domain.exception.ValidationException;
import com.asdeire.domain.service.impl.UserService;
import com.asdeire.presentation.viewmodel.UserViewModel;
import jakarta.validation.ConstraintViolation;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
public class SignUpController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    private UserViewModel userViewModel;
    @Autowired
    private UserService userService;

    public void setUserService(UserService userService){
        this.userService = userService;
    }

    @FXML
    public void initialize() {
        userViewModel = new UserViewModel(
                UUID.randomUUID(),
                usernameField.textProperty().get(),
                emailField.textProperty().get(),
                emailField.textProperty().get(),
                20000.00
        );
        // Зв'язування властивостей ViewModel з View
        bindFieldsToViewModel();
    }

    private void bindFieldsToViewModel() {
        usernameField.textProperty().bindBidirectional(userViewModel.usernameProperty());
        emailField.textProperty().bindBidirectional(userViewModel.emailProperty());
        passwordField.textProperty().bindBidirectional(userViewModel.passwordProperty());
    }

    @FXML
    public void onSubmit() {

        try {
            System.out.println("Saving User Data: " + userViewModel);

            UserStoreDto userStoreDto = new UserStoreDto(
                    userViewModel.getUsername(),
                    userViewModel.getEmail(),
                    userViewModel.getPassword()
            );

            userService.create(userStoreDto);

            // Відображення інформації через Alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User Information");
            alert.setHeaderText("User saved Successfully");
            alert.setContentText(userViewModel.toString());
            alert.showAndWait();

        } catch (ValidationException e) {
            // Отримання повідомлень про помилки валідації
            Set<? extends ConstraintViolation<?>> violations = e.getViolations();

            // Формування рядка з повідомленнями про помилки
            StringBuilder errorMessage = new StringBuilder("Validation errors:\n");
            for (ConstraintViolation<?> violation : violations) {
                errorMessage.append("- ").append(violation.getMessage()).append("\n");
            }

            // Відображення меседжу про помилки через Alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Error saving user");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
    }

}
