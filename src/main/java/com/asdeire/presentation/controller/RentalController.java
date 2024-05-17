package com.asdeire.presentation.controller;

import com.asdeire.domain.service.impl.AuthService;
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
import java.util.Optional;
import java.util.UUID;
/**
 * Controller class responsible for managing car rental operations in the application.
 * This class handles the creation of rental transactions, validation of rental parameters, and interaction with the user interface.
 */
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
    private final AuthService authenticationService;
    private Stage previousStage;

    /**
     * Constructs a new RentalController with the specified Spring application context and authentication service.
     *
     * @param springContext The Spring application context.
     * @param authenticationService The authentication service.
     */
    public RentalController(AnnotationConfigApplicationContext springContext, AuthService authenticationService) {
        this.springContext = springContext;
        this.authenticationService = authenticationService;
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
    }

    /**
     * Initializes the data for the rental process with the given car.
     *
     * @param car The car for rental.
     */
    public void initData(Car car) {
        this.selectedCar = car;
        showCarInfo(car);
        setDateLimit();
        rootNode.setOnKeyPressed(this::handleKeyPress);
    }

    /**
     * Handles the creation of a rental transaction.
     */
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
        if (!selectedCar.isAvailability()) {
            showAlert(Alert.AlertType.ERROR, "Warning", "The car is not available");
        } else {
            try {
                rentalService.createRental(rental);

                userService.update(updetedUser);

                Stage stage = (Stage) submitButton.getScene().getWindow();
                stage.close();
                showConfirmAlert(stage, updetedUser);
                previousStage.close();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Warning", "Failed to execute rent");
            }
        }
    }

    /**
     * Calculates the total price for the rental based on the start and end dates.
     *
     * @param startDate The start date of the rental.
     * @param endDate The end date of the rental.
     * @return The total price for the rental.
     */
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

    /**
     * Displays an alert with the specified type, title, and message.
     *
     * @param alertType The type of the alert.
     * @param title The title of the alert.
     * @param message The message of the alert.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    /**
     * Displays a confirmation alert for the successful rental transaction.
     *
     * @param stage The stage to display the alert.
     * @param updatedUser The updated user after the rental transaction.
     * @throws IOException If an error occurs while opening the review window.
     */
    private void showConfirmAlert(Stage stage, User updatedUser) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText("The rental was successful, would you like to leave a review?");
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            openReviewWindow(stage, updatedUser);
        } else {
            openCategorySelection(stage, updatedUser);
        }
    }

    /**
     * Displays car information in the UI labels.
     *
     * @param car The car object to display information for.
     */
    private void showCarInfo(Car car) {
        carModelLabel.setText(STR."Model: \{car.getBrand()} \{car.getModel()}");
        carAvailableLabel.setText(STR."Available: \{car.isAvailability()}");
        carPriceLabel.setText(STR."Price per day: \{car.getPrice()}");
        carRatingLabel.setText(STR."Rating: \{car.getRating()}");
        carYearLabel.setText(STR."Year: \{car.getYear()}");
    }

    /**
     * Sets the date limit for selecting dates in the date pickers.
     */
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

    /**
     * Handles key press events, particularly the ESCAPE key to close the current stage.
     *
     * @param event The key event.
     */
    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        }
    }

    /**
     * Opens the category selection window.
     *
     * @param stage The stage to display the category selection window.
     * @param updatedUser The updated user after the rental transaction.
     * @throws IOException If an error occurs while opening the category selection window.
     */
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

    /**
     * Opens the review window.
     *
     * @param stage The stage to display the review window.
     * @param updatedUser The updated user after the rental transaction.
     * @throws IOException If an error occurs while opening the review window.
     */
    private void openReviewWindow(Stage stage, User updatedUser) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/asdeire/presentation/view/review.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent root = fxmlLoader.load();

        ReviewController reviewController = fxmlLoader.getController();
        reviewController.setCurrentUser(updatedUser);
        reviewController.setCurrentCar(selectedCar);

        Scene scene = new Scene(root);
        stage.setMinHeight(40 - 0);
        stage.setMinWidth(300);
        stage.setScene(scene);
        stage.setTitle("Review");
        stage.show();
    }
}
