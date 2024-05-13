package com.asdeire.presentation.controller;

import com.asdeire.domain.service.impl.RentalService;
import com.asdeire.persistence.entities.Car;
import com.asdeire.persistence.entities.Rental;
import com.asdeire.persistence.entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    private TextField priceField;

    private Car selectedCar;

    @Autowired
    private RentalService rentalService;
    private User currentUser;

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void initData(Car car) {
        this.selectedCar = car;
        carModelLabel.setText(STR."\{car.getBrand()} \{car.getModel()}");
        carAvailableLabel.setText(car.isAvailability().toString());
        carPriceLabel.setText(car.getPrice().toString());
        carRatingLabel.setText(car.getRating().toString());
        carYearLabel.setText(String.valueOf(car.getYear()));

        // Отримуємо поточну дату
        LocalDate today = LocalDate.now();

        // Встановлюємо обмеження на вибір дати в DatePicker
        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(today)); // Вимикаємо дні, які відбулися до сьогоднішньої дати
            }
        });

        // Встановлюємо обмеження на вибір дати в DatePicker
        endDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(today)); // Вимикаємо дні, які відбулися до сьогоднішньої дати
            }
        });

        startDatePicker.setValue(today);
    }

    @FXML
    private void createRental() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = endDatePicker.getValue();
        BigDecimal price = calculatePrice(startDate, endDate);

        Rental rental = new Rental();
        rental.setUserId(currentUser.getId());
        rental.setCarId(selectedCar.getId());
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setPrice(price);

        rentalService.createRental(rental);

        // Додайте логіку для закриття вікна оренди
    }

    private BigDecimal calculatePrice(LocalDate startDate, LocalDate endDate) {
        BigDecimal pricePerDay = BigDecimal.valueOf(selectedCar.getPrice());

        long numberOfDays = endDate.toEpochDay() - startDate.toEpochDay() + 1;

        if (numberOfDays < 0) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        BigDecimal totalPrice = pricePerDay.multiply(BigDecimal.valueOf(numberOfDays));

        return totalPrice;
    }

}
