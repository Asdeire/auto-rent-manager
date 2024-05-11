package com.asdeire.presentation.controller;

import com.asdeire.domain.exception.AuthenticationException;
import com.asdeire.domain.exception.UserAlreadyAuthenticatedException;
import com.asdeire.domain.service.impl.AuthServiceImpl;
import com.asdeire.domain.service.impl.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SignInController {


    @Autowired
    private UserService userService;
    private final AuthServiceImpl authenticationService;
    @FXML
    public PasswordField passwordField;
    @FXML
    public CheckBox rememberMeCheckBox;
    @FXML
    private TextField usernameField;

    public SignInController(AuthServiceImpl authenticationService) {
        this.authenticationService = authenticationService;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean rememberMe = rememberMeCheckBox.isSelected();

        try {
            boolean authenticated = authenticationService.authenticate(username, password);

            if (authenticated) {
                showAlert(Alert.AlertType.INFORMATION, "Успіх", "Вхід виконано успішно!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Невірний логін або пароль.");
            }
        } catch (UserAlreadyAuthenticatedException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", e.getMessage());
        } catch (AuthenticationException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Невірний логін або пароль.");
        }
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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
