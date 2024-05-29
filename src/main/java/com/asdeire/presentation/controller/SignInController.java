package com.asdeire.presentation.controller;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import com.asdeire.domain.exception.AuthenticationException;
import com.asdeire.domain.exception.UserAlreadyAuthenticatedException;
import com.asdeire.domain.service.impl.AuthService;
import com.asdeire.domain.service.impl.CategorySelectionService;
import com.asdeire.domain.service.impl.UserService;
import com.asdeire.persistence.entities.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.text.html.StyleSheet;
import java.io.IOException;

/**
 * Controller class responsible for managing user sign-in functionality.
 * This class handles user authentication and navigation to the category selection window upon successful login.
 */
@Component
public class SignInController {

    @Autowired
    private UserService userService;
    @Autowired
    private CategorySelectionService categorySelectionService;
    private final AuthService authenticationService;
    @FXML
    public PasswordField passwordField;
    @FXML
    public CheckBox rememberMeCheckBox;
    @FXML
    private Button themeChangeButton;
    @FXML
    private TextField usernameField;
    private final AnnotationConfigApplicationContext springContext;
    private User currentUser;
    private boolean isLightTheme = true;

    /**
     * Constructs a new SignInController with the specified authentication service and Spring application context.
     *
     * @param authenticationService The authentication service.
     * @param springContext         The Spring application context.
     */
    public SignInController(AuthService authenticationService, AnnotationConfigApplicationContext springContext) {
        this.authenticationService = authenticationService;
        this.springContext = springContext;
    }

    private String theme = new PrimerLight().getUserAgentStylesheet();

    /**
     * Initializes the sign-in form.
     */
    @FXML
    public void initialize() {
        Application.setUserAgentStylesheet(theme);
        themeChangeButton.setText(isLightTheme ? "\uD83C\uDF19" : "☀");
    }

    /**
     * Handles the login action when the sign-in button is clicked.
     * Attempts to authenticate the user and navigate to the category selection window upon successful login.
     *
     * @param event The action event.
     * @throws IOException If an error occurs during navigation to the category selection window.
     */
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

    /**
     * Handles the sign-up action when the sign-up link is clicked.
     * Opens the sign-up window for user registration.
     *
     * @param event The action event.
     * @throws IOException If an error occurs during navigation to the sign-up window.
     */
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
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.setTitle("SignUp");

        stage.show();

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Navigates to the category selection window upon successful login.
     *
     * @param stage The stage to display the category selection window.
     * @throws IOException If an error occurs during navigation to the category selection window.
     */
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


    public void handleTheme(ActionEvent event) {
        if (isLightTheme) {
            Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
            setTheme(new PrimerDark().getUserAgentStylesheet());
            themeChangeButton.setText("☀");
            isLightTheme = false;
        } else {
            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
            setTheme(new PrimerLight().getUserAgentStylesheet());
            themeChangeButton.setText("\uD83C\uDF19");
            isLightTheme = true;
        }
    }

    private void setTheme(String userAgentStylesheet) {
        theme = userAgentStylesheet;
    }
}
