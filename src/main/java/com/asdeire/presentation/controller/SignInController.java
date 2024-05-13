package com.asdeire.presentation.controller;

import com.asdeire.domain.exception.AuthenticationException;
import com.asdeire.domain.exception.UserAlreadyAuthenticatedException;
import com.asdeire.domain.service.impl.AuthServiceImpl;
import com.asdeire.domain.service.impl.CategorySelectionService;
import com.asdeire.domain.service.impl.UserService;
import com.asdeire.persistence.entities.User;
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
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SignInController {


    @Autowired
    private UserService userService;
    @Autowired
    private CategorySelectionService categorySelectionService;
    private final AuthServiceImpl authenticationService;
    @FXML
    public PasswordField passwordField;
    @FXML
    public CheckBox rememberMeCheckBox;
    @FXML
    private TextField usernameField;
    private final AnnotationConfigApplicationContext springContext;
    private User currentUser;

    public SignInController(AuthServiceImpl authenticationService, AnnotationConfigApplicationContext springContext) {
        this.authenticationService = authenticationService;
        this.springContext = springContext;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean rememberMe = rememberMeCheckBox.isSelected();

        try {
            boolean authenticated = authenticationService.authenticate(username, password);
            currentUser = userService.getUserByUsername(username);

            if (authenticated) {
                openCategorySelection((Stage) ((Node) event.getSource()).getScene().getWindow());
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

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openCategorySelection(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/asdeire/presentation/view/categorySelection.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent root = fxmlLoader.load();

        CategorySelectionController categorySelectionController = fxmlLoader.getController();
        categorySelectionController.setCurrentUser(currentUser);
        categorySelectionController.setAuthService(authenticationService);

        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("Category");
        stage.setScene(scene);
        stage.show();
    }



}
