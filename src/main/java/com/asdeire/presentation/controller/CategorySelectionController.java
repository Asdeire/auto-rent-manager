package com.asdeire.presentation.controller;

import com.asdeire.domain.service.impl.AuthServiceImpl;
import com.asdeire.domain.service.impl.CarSelectionService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

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

    @Autowired
    private CategorySelectionService categorySelectionService;
    private User currentUser;
    private Category currentCategory;
    private AuthServiceImpl authService;
    private CarSelectionController carSelectionController;

    @FXML
    private void initialize() {
        rootNode.setOnKeyPressed(this::handleKeyPress);
        loadCarSelectionController();
    }

    CategorySelectionController(AnnotationConfigApplicationContext springContext) {
        this.springContext = springContext;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        updateUI();
    }

    public void setAuthService(AuthServiceImpl authService) {
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



    private void displayCategories() {
        List<Category> categories = categorySelectionService.getAllCategories();

        for (Category category : categories) {
            Button button = new Button(category.getName());
            button.setMinSize(100, 50);
            button.setOnAction(event -> handleCategorySelection(category));
            categoryButtons.getChildren().add(button);
        }
    }

    @FXML
    private void handleCategorySelection(Category category) {
        if (carSelectionController != null) {
            carSelectionController.setSelectedCategory(category);
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
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

    private void loadCarSelectionController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/asdeire/presentation/view/carSelection.fxml"));
        Parent carSelectionRoot;
        try {
            carSelectionRoot = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        carSelectionController = loader.getController();
    }
}
