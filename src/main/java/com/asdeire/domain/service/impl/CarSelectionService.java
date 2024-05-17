package com.asdeire.domain.service.impl;

import com.asdeire.persistence.entities.Car;
import com.asdeire.persistence.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * A service responsible for selecting cars by category.
 */
@Service
public class CarSelectionService {
    private final CarRepository carRepository;

    /**
     * Constructs a new CarSelectionService with the specified CarRepository.
     *
     * @param carRepository the car repository.
     */
    @Autowired
    public CarSelectionService(CarRepository carRepository){
        this.carRepository = carRepository;
    }

    /**
     * Retrieves all cars belonging to a specific category.
     *
     * @param categoryID the UUID of the category.
     * @return a list of cars belonging to the specified category.
     */
    public List<Car> getAllCarsByCategoryName(UUID categoryID) {
        return carRepository.findCarsByCategory(categoryID);
    }
}
