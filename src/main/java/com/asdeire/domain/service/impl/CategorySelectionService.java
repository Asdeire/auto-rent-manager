package com.asdeire.domain.service.impl;

import com.asdeire.persistence.entities.Category;
import com.asdeire.persistence.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * A service responsible for selecting categories.
 */
@Service
public class CategorySelectionService {

    private final CategoryRepository categoryRepository;

    /**
     * Constructs a new CategorySelectionService with the specified CategoryRepository.
     *
     * @param categoryRepository the category repository.
     */
    @Autowired
    public CategorySelectionService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Retrieves all categories.
     *
     * @return a list of all categories.
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id the UUID of the category.
     * @return the category with the specified ID, or null if not found.
     */
    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }
}
