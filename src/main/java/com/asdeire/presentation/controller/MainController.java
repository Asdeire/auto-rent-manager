package com.asdeire.presentation.controller;

import com.asdeire.domain.dto.UserStoreDto;
import com.asdeire.domain.service.impl.UserService;
import com.asdeire.presentation.viewmodel.UserViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class MainController {

    @Autowired
    private UserService userService;
    @FXML
    private Label idLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label balanceLabel;


    private UserViewModel userViewModel;

    @FXML
    public void initialize() {

        // Створення користувача з пустими даними як приклад
        userViewModel = new UserViewModel(
                UUID.randomUUID(),
                "DashaAst",
                "dast@example.com",
                "password2123",
                20000.00
        );

        // Зв'язування властивостей ViewModel з View
        bindFieldsToViewModel();
    }

    private void bindFieldsToViewModel() {
        idLabel.setText(userViewModel.getId().toString());
        usernameField.textProperty().bindBidirectional(userViewModel.usernameProperty());
        emailField.textProperty().bindBidirectional(userViewModel.emailProperty());
        passwordField.textProperty().bindBidirectional(userViewModel.passwordProperty());
        balanceLabel.setText(userViewModel.getBalance().toString());
    }

    @FXML
    private void onSave() {
        System.out.println("Saving User Data: " + userViewModel);

        // Відображення інформації через Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Information");
        alert.setHeaderText("User Data Saved Successfully");
        alert.setContentText(userViewModel.toString());
        alert.showAndWait();

        UserStoreDto userStoreDto = new UserStoreDto(
                userViewModel.getUsername(),
                userViewModel.getEmail(),
                userViewModel.getPassword()
        );


        userService.create(userStoreDto);

    }

    @FXML
    private void onCancel() {
        System.out.println("Operation Cancelled");
    }
}