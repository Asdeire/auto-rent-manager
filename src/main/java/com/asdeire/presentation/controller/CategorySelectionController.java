package com.asdeire.presentation.controller;

import com.asdeire.domain.service.impl.AuthService;
import com.asdeire.domain.service.impl.CategorySelectionService;
import com.asdeire.persistence.entities.Category;
import com.asdeire.persistence.entities.User;
import com.asdeire.presentation.Runner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.List;

/**
 * Controller for the category selection view.
 */
@Component
public class CategorySelectionController {

    @FXML
    private Label userLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private VBox categoryButtons;

    @FXML
    private Parent rootNode;

    private final AnnotationConfigApplicationContext springContext;

    private final CategorySelectionService categorySelectionService;
    private User currentUser;
    private AuthService authService;
    private CarSelectionController carSelectionController;

    private Stage previousStage;

    /**
     * Constructs a new CategorySelectionController with the specified Spring application context,
     * category selection service, and car selection controller.
     *
     * @param springContext           The Spring application context.
     * @param categorySelectionService The category selection service.
     * @param carSelectionController  The car selection controller.
     */
    public CategorySelectionController(AnnotationConfigApplicationContext springContext,
                                       CategorySelectionService categorySelectionService,
                                       CarSelectionController carSelectionController) {
        this.springContext = springContext;
        this.categorySelectionService = categorySelectionService;
        this.carSelectionController = carSelectionController;
    }

    /**
     * Initializes the controller.
     */
    @FXML
    private void initialize() {
        rootNode.setOnKeyPressed(this::handleKeyPress);
    }

    /**
     * Sets the current user.
     *
     * @param currentUser The current user.
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        updateUI();
    }

    /**
     * Sets the authentication service.
     *
     * @param authService The authentication service.
     */
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    private void updateUI() {
        if (currentUser != null) {
            userLabel.setText("User: " + currentUser.getUsername());
            balanceLabel.setText("Balance: $" + currentUser.getBalance());
            displayCategories();
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Warning", "Warning");
        }
    }

    private void displayCategories(){
        List<Category> categories = categorySelectionService.getAllCategories();

        for (Category category : categories) {
            Button button = new Button(category.getName());
            button.setMinSize(120, 50);
            button.setOnAction(event -> {
                try {
                    handleCategorySelection(event, category);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            categoryButtons.getChildren().add(button);
        }
    }

    /**
     * Handles category selection event.
     *
     * @param event    The action event.
     * @param category The selected category.
     * @throws IOException if an I/O error occurs.
     */
    @FXML
    private void handleCategorySelection(ActionEvent event, Category category) throws IOException{
        openCarSelection(category, (Stage) ((Node) event.getSource()).getScene().getWindow());
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    /**
     * Handles key press event.
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
                authService.logout();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void openCarSelection(Category category, Stage currentStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/asdeire/presentation/view/carSelection.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent root = fxmlLoader.load();

        CarSelectionController carSelectionController = fxmlLoader.getController();
        carSelectionController.setSelectedCategory(category);
        carSelectionController.setCurrentUser(currentUser);
        carSelectionController.setPreviousStage(currentStage);

        Scene scene = new Scene(root, 600, 400);

        Stage stage = new Stage();
        stage.setTitle("Cars");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.show();

        // Save the previous window
        this.previousStage = currentStage;
        currentStage.close();
    }

    @FXML
    private void openHistory(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/asdeire/presentation/view/history.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent root = fxmlLoader.load();

        HistoryController historyController = fxmlLoader.getController();
        historyController.setCurrentUser(currentUser);
        historyController.setPreviousStage((Stage) ((Node) event.getSource()).getScene().getWindow());

        Scene scene = new Scene(root, 600, 400);

        Stage stage = new Stage();
        stage.setTitle("History");
        stage.setScene(scene);
        stage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
