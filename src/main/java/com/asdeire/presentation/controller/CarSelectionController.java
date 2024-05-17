package com.asdeire.presentation.controller;

import com.asdeire.domain.service.impl.CarSelectionService;
import com.asdeire.persistence.entities.Car;
import com.asdeire.persistence.entities.Category;
import com.asdeire.persistence.entities.User;
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
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the car selection view.
 */
@Component
public class CarSelectionController {
    @FXML
    private Label userLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private FlowPane carButtons;

    @FXML
    private Label categoryLabel;

    @FXML
    private Parent rootNode;

    @Autowired
    private CarSelectionService carSelectionService;
    private User currentUser;
    private Category selectedCategory;
    private final AnnotationConfigApplicationContext springContext;

    private Stage previousStage;

    /**
     * Constructs a new CarSelectionController with the specified Spring application context.
     *
     * @param springContext The Spring application context.
     */
    public CarSelectionController(AnnotationConfigApplicationContext springContext) {
        this.springContext = springContext;
    }

    /**
     * Sets the previous stage.
     *
     * @param previousStage The previous stage.
     */
    public void setPreviousStage(Stage previousStage) {
        this.previousStage = previousStage;
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
     * Sets the selected category.
     *
     * @param category The selected category.
     */
    public void setSelectedCategory(Category category) {
        this.selectedCategory = category;
    }

    private void updateUI() {
        if (currentUser != null) {
            userLabel.setText("User: " + currentUser.getUsername());
            balanceLabel.setText("Balance: $" + currentUser.getBalance());
            categoryLabel.setText("Category: " + selectedCategory.getName());
            displayCars();
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Warning", "Warning");
        }
    }

    @FXML
    private void initialize() {
        rootNode.setOnKeyPressed(this::handleKeyPress);
    }

    private void displayCars() {
        if (selectedCategory != null) {
            List<Car> carsInCategory = carSelectionService.getAllCarsByCategoryName(selectedCategory.getId());
            // Add car buttons to the flow pane
            for (Car car : carsInCategory) {
                Button button = new Button(car.getBrand() + " " + car.getModel());
                button.setMinSize(150, 70);
                button.setOnAction(event -> handleCarSelection(car, (Stage) ((Node) event.getSource()).getScene().getWindow()));
                carButtons.getChildren().add(button);
            }

        } else {
            showAlert(Alert.AlertType.WARNING, "No Category Selected", "Please select a category first.");
        }
    }

    @FXML
    private void handleCarSelection(Car car, Stage currentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/asdeire/presentation/view/rental.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            RentalController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            controller.initData(car);
            controller.setPreviousStage(currentStage);

            Stage stage = new Stage();
            stage.setMinHeight(380);
            stage.setMinWidth(300);
            stage.setScene(new Scene(root));
            stage.setTitle("Rent Menu");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE && previousStage != null) {
            previousStage.show();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
