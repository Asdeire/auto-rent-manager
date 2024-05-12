package com.asdeire.domain.service.impl;

import com.asdeire.persistence.dao.CategoryJdbcDao;
import com.asdeire.persistence.entities.Category;
import com.asdeire.persistence.repository.impl.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class CategorySelectionService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategorySelectionService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }
}
