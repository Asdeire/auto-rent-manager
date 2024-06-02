package com.asdeire.presentation.viewmodel;

import java.time.LocalDate;
import java.util.Date;

public class ReviewViewModel {
    private String carName;
    private double rating;
    private String comment;
    private Date reviewDate;

    public ReviewViewModel(String carName, double rating, String comment, Date reviewDate) {
        this.carName = carName;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    public String getCarName() {
        return carName;
    }

    public double getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Date getReviewDate() {
        return reviewDate;
    }
}
