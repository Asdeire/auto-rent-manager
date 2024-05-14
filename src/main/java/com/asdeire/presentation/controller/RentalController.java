package com.asdeire.presentation.controller;

import com.asdeire.domain.service.impl.AuthServiceImpl;
import com.asdeire.domain.service.impl.RentalService;
import com.asdeire.domain.service.impl.UserService;
import com.asdeire.persistence.entities.Car;
import com.asdeire.persistence.entities.Rental;
import com.asdeire.persistence.entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class RentalController {

    @FXML
    private Label carModelLabel;
    @FXML
    private Label carYearLabel;
    @FXML
    private Label carRatingLabel;
    @FXML
    private Label carAvailableLabel;
    @FXML
    private Label carPriceLabel;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Button submitButton;
    private Car selectedCar;
    @FXML
    private Parent rootNode;

    @Autowired
    private RentalService rentalService;
    @Autowired
    private UserService userService;
    private User currentUser;
    private final AnnotationConfigApplicationContext springContext;
    private final AuthServiceImpl authenticationService;
    private Stage previousStage;

    public RentalController(AnnotationConfigApplicationContext springContext, AuthServiceImpl authenticationService) {
        this.springContext = springContext;
        this.authenticationService = authenticationService;
    }

    public void setPreviousStage(Stage previousStage) {
        this.previousStage = previousStage;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void initData(Car car) {
        this.selectedCar = car;
        showCarInfo(car);
        setDateLimit();
        rootNode.setOnKeyPressed(this::handleKeyPress);
    }

    @FXML
    private void createRental() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        Double price = calculatePrice(startDate, endDate);

        // Перевірка на валідність дат
        if (startDate.isAfter(endDate)) {
            showAlert(Alert.AlertType.ERROR, "Date error", "End date must be after start date");
            return;
        }

        // Перевірка на достатність коштів у користувача
        if (!rentalService.hasSufficientFunds(currentUser, price)) {
            showAlert(Alert.AlertType.ERROR, "Insufficient funds", "You do not have enough funds to rent this car.");
            return;
        }

        Rental rental = new Rental();
        rental.setRentalId(UUID.randomUUID());
        rental.setUserId(currentUser.getId());
        rental.setCarId(selectedCar.getId());
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setPrice(price);

        // Оновлення балансу користувача
        Double newBalance = currentUser.getBalance() - price;

        User updetedUser = new User(currentUser.getId(),
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getPassword(),
                newBalance);

        try {
            rentalService.createRental(rental);

            userService.update(updetedUser);

            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();
            previousStage.close();
            openCategorySelection(stage, updetedUser);
            showAlert(Alert.AlertType.CONFIRMATION, "Success", "The rental was successful, would you like to leave a review?");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Warning", "Failed to execute rent");
        }
    }

    private Double calculatePrice(LocalDate startDate, LocalDate endDate) {
        Double pricePerDay = selectedCar.getPrice();

        try {
            long numberOfDays = endDate.toEpochDay() - startDate.toEpochDay() + 1;

            Double totalPrice = pricePerDay * Double.valueOf(numberOfDays);

            return totalPrice;
        } catch (NullPointerException e) {
            showAlert(Alert.AlertType.ERROR, "Warning", "Date cannot be null");
            return null;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private void showCarInfo(Car car) {
        carModelLabel.setText("Model: " + car.getBrand() + " " + car.getModel());
        carAvailableLabel.setText("Available: " + car.isAvailability());
        carPriceLabel.setText("Price per day: " + car.getPrice());
        carRatingLabel.setText("Rating: " + car.getRating());
        carYearLabel.setText("Year: " + car.getYear());
    }

    private void setDateLimit() {
        // Отримуємо поточну дату
        LocalDate today = LocalDate.now();

        // Встановлюємо обмеження на вибір дати в DatePicker
        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(today));
            }
        });
        endDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(today));
            }
        });

        startDatePicker.setValue(today);
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        }
    }

    private void openCategorySelection(Stage stage, User updatedUser) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/asdeire/presentation/view/categorySelection.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent root = fxmlLoader.load();

        CategorySelectionController categorySelectionController = fxmlLoader.getController();
        categorySelectionController.setCurrentUser(updatedUser);
        categorySelectionController.setAuthService(authenticationService);

        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("Category");
        stage.setScene(scene);
        stage.show();
    }
}
