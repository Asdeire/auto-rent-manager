package com.asdeire.presentation.controller;

import com.asdeire.domain.service.impl.AuthService;
import com.asdeire.domain.service.impl.ReviewService;
import com.asdeire.persistence.entities.Car;
import com.asdeire.persistence.entities.Review;
import com.asdeire.persistence.entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;
/**
 * Controller class responsible for managing the review submission process for a car.
 * This class allows users to submit reviews with ratings and comments for a specific car.
 */
@Component
public class ReviewController {

    @FXML
    private HBox starButtonsContainer;

    @FXML
    private TextArea txtComment;

    @FXML
    private Label carLabel;

    @FXML
    private Parent rootNode;

    @Autowired
    private ReviewService reviewService;
    private final AuthService authenticationService;
    private final AnnotationConfigApplicationContext springContext;

    private User currentUser;
    private Car currentCar;

    private int rating = 0;

    /**
     * Constructs a new ReviewController with the specified authentication service and Spring application context.
     *
     * @param authenticationService The authentication service.
     * @param springContext The Spring application context.
     */
    public ReviewController(AuthService authenticationService, AnnotationConfigApplicationContext springContext) {
        this.authenticationService = authenticationService;
        this.springContext = springContext;
    }

    /**
     * Sets the current user for the review.
     *
     * @param currentUser The current user.
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Sets the car for which the review is being submitted.
     *
     * @param currentCar The current car.
     */
    public void setCurrentCar(Car currentCar) {
        this.currentCar = currentCar;
        carLabel.setText(STR."Car: \{currentCar.getBrand()} \{currentCar.getModel()}");
    }

    /**
     * Initializes the review submission form.
     */
    @FXML
    private void initialize() {
        for (int i = 1; i <= 5; i++) {
            int finalI = i;
            Button starButton = new Button("★");
            starButton.setOnAction(event -> setRating(finalI));
            starButtonsContainer.getChildren().add(starButton);
        }

        rootNode.setOnKeyPressed(this::handleKeyPress);
    }

    /**
     * Sets the rating for the review.
     *
     * @param selectedRating The selected rating.
     */
    private void setRating(int selectedRating) {
        rating = selectedRating;
        updateRatingButtons();
    }

    /**
     * Submits the review with the given rating and comment.
     *
     * @param event The action event.
     */
    @FXML
    private void submitReview(ActionEvent event) {
        if (rating == 0) {
            showAlert(AlertType.ERROR, "Error", "Please select a rating.");
            return;
        }

        String comment = txtComment.getText();

        Review review = new Review(UUID.randomUUID(), currentUser.getId(), currentCar.getId(), rating, comment, new Date());

        try {
            reviewService.save(review);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            openCategorySelection(stage, currentUser);
            showAlert(AlertType.INFORMATION, "Success", "Your review has been submitted successfully.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the appearance of the star rating buttons based on the current rating.
     */
    private void updateRatingButtons() {
        for (int i = 0; i < starButtonsContainer.getChildren().size(); i++) {
            Button starButton = (Button) starButtonsContainer.getChildren().get(i);
            starButton.setStyle(i < rating ? "-fx-background-color: #ffbf00; -fx-text-fill: white;" : "");
        }
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
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

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE ) {
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            try {
                openCategorySelection(currentStage, currentUser);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
