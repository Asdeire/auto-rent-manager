package com.asdeire.domain.service.impl;

import com.asdeire.persistence.entities.Car;
import com.asdeire.persistence.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CarSelectionService {
    private final CarRepository carRepository;
    @Autowired
    public CarSelectionService(CarRepository carRepository){
        this.carRepository = carRepository;
    }

    public List<Car> getAllCarsByCategoryName(UUID categoryID) {
        return carRepository.findCarsByCategory(categoryID);
    }
}
