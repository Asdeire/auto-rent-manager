package com.asdeire.domain.service.impl;

import com.asdeire.persistence.entities.Review;
import com.asdeire.persistence.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service responsible for managing reviews.
 */
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    /**
     * Constructs a new ReviewService with the specified ReviewRepository.
     *
     * @param reviewRepository the review repository.
     */
    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Saves a review.
     *
     * @param review the review to save.
     */
    public void save(Review review) {
        reviewRepository.create(review);
    }
}
