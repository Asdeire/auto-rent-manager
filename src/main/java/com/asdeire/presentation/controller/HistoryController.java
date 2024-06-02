package com.asdeire.presentation.controller;

import com.asdeire.persistence.entities.Car;
import com.asdeire.persistence.entities.Rental;
import com.asdeire.persistence.entities.Review;
import com.asdeire.persistence.entities.User;
import com.asdeire.persistence.repository.CarRepository;
import com.asdeire.persistence.repository.RentalRepository;
import com.asdeire.persistence.repository.ReviewRepository;
import com.asdeire.presentation.viewmodel.RentalViewModel;
import com.asdeire.presentation.viewmodel.ReviewViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class HistoryController {

    @FXML
    private TableView<RentalViewModel> rentalTableView;

    @FXML
    private TableColumn<RentalViewModel, String> rentalCarNameColumn;

    @FXML
    private TableColumn<RentalViewModel, LocalDate> startDateColumn;

    @FXML
    private TableColumn<RentalViewModel, LocalDate> endDateColumn;

    @FXML
    private TableColumn<RentalViewModel, Double> priceColumn;

    @FXML
    private TableView<ReviewViewModel> reviewTableView;

    @FXML
    private TableColumn<ReviewViewModel, String> reviewCarNameColumn;

    @FXML
    private TableColumn<ReviewViewModel, Double> ratingColumn;

    @FXML
    private TableColumn<ReviewViewModel, String> commentColumn;

    @FXML
    private TableColumn<ReviewViewModel, Date> reviewDateColumn;

    @FXML
    private Parent rootNode;


    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private User currentUser;
    private Stage previousStage;

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setPreviousStage(Stage previousStage) {
        this.previousStage = previousStage;
    }

    @FXML
    public void initialize() {
        rootNode.setOnKeyPressed(this::handleKeyPress);

        rentalCarNameColumn.setCellValueFactory(new PropertyValueFactory<>("carName"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        reviewCarNameColumn.setCellValueFactory(new PropertyValueFactory<>("carName"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        reviewDateColumn.setCellValueFactory(new PropertyValueFactory<>("reviewDate"));

        rentalTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        reviewTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    @FXML
    private void showReviews() {
        UUID userId = currentUser.getId();
        List<Review> reviews = reviewRepository.findAllByUserId(userId);

        ObservableList<ReviewViewModel> reviewViews = FXCollections.observableArrayList();
        for (Review review : reviews) {
            String carName = getCarNameById(review.getCarID());
            reviewViews.add(new ReviewViewModel(carName, review.getRating(), review.getComment(), review.getDate()));
        }

        rentalTableView.setVisible(false);
        rentalTableView.setManaged(false);
        reviewTableView.setItems(reviewViews);
        reviewTableView.setVisible(true);
        reviewTableView.setManaged(true);
    }

    @FXML
    private void showRentals() {
        UUID userId = currentUser.getId();
        List<Rental> rentals = rentalRepository.findAllByUserId(userId);

        ObservableList<RentalViewModel> rentalViews = FXCollections.observableArrayList();
        for (Rental rental : rentals) {
            String carName = getCarNameById(rental.getCarId());
            rentalViews.add(new RentalViewModel(carName, rental.getStartDate(), rental.getEndDate(), rental.getPrice()));
        }

        reviewTableView.setVisible(false);
        reviewTableView.setManaged(false);
        rentalTableView.setItems(rentalViews);
        rentalTableView.setVisible(true);
        rentalTableView.setManaged(true);
    }

    private String getCarNameById(UUID uuid) {
        try {
            Car car = carRepository.findById(uuid);
            return STR."\{car.getBrand()} \{car.getModel()}";
        } catch (NullPointerException e) {
            return "Unknown Car";
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
}
