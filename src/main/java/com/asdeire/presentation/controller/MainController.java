package com.asdeire.presentation.controller;

import com.asdeire.domain.dto.UserStoreDto;
import com.asdeire.domain.service.impl.UserService;
import com.asdeire.presentation.viewmodel.UserViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import java.io.IOException;
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
        System.out.println(STR."Saving User Data: \{userViewModel}");

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
    private void onSignUp(ActionEvent event) throws IOException {
        // Відкриття вікна авторизації
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/asdeire/presentation/view/signUp.fxml"));
        Parent root = fxmlLoader.load();

        SignUpController signUpController = fxmlLoader.getController();
        signUpController.setUserService(userService);

        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setWidth(600);
        stage.setHeight(400);
        stage.setTitle("SignUp");

        stage.show();

        /*Закриття поточного вікна
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        stage.close();*/
    }
}