package com.asdeire.presentation.controller;

import com.asdeire.domain.dto.UserStoreDto;
import com.asdeire.domain.exception.ValidationException;
import com.asdeire.domain.service.impl.UserService;
import com.asdeire.presentation.Runner;
import com.asdeire.presentation.viewmodel.UserViewModel;
import jakarta.validation.ConstraintViolation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

/**
 * Controller class responsible for managing user sign-up functionality.
 * This class handles user registration, input validation, and navigation to the main application window upon successful registration.
 */
@Component
public class SignUpController {
    @FXML
    private Parent rootNode;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    private UserViewModel userViewModel;
    @Autowired
    private UserService userService;
    @FXML
    private Button submitButton;

    /**
     * Sets the UserService for user registration.
     *
     * @param userService The UserService instance.
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Initializes the sign-up form.
     */
    @FXML
    public void initialize() {
        rootNode.setOnKeyPressed(this::handleKeyPress);

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

    /**
     * Binds the form fields to the ViewModel properties.
     */
    private void bindFieldsToViewModel() {
        usernameField.textProperty().bindBidirectional(userViewModel.usernameProperty());
        emailField.textProperty().bindBidirectional(userViewModel.emailProperty());
        passwordField.textProperty().bindBidirectional(userViewModel.passwordProperty());
    }

    /**
     * Handles the form submission when the submit button is clicked.
     * Attempts to create a new user and navigate to the main application window upon successful registration.
     * Displays validation errors in case of input validation failure.
     *
     * @param event The action event.
     * @throws Exception If an error occurs during navigation to the main application window.
     */
    @FXML
    public void onSubmit(ActionEvent event) throws Exception {

        try {

            UserStoreDto userStoreDto = new UserStoreDto(
                    userViewModel.getUsername(),
                    userViewModel.getEmail(),
                    userViewModel.getPassword()
            );

            userService.create(userStoreDto);

            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();

            Runner runner = new Runner();
            runner.start(new Stage());

            // Відображення інформації через Alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User Information");
            alert.setHeaderText("User saved Successfully");
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

    /**
     * Handles key press events.
     * Closes the sign-up window and navigates back to the main application window upon pressing the ESCAPE key.
     *
     * @param event The key event.
     */
    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            Runner runner = new Runner();
            try {
                runner.start(new Stage());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
