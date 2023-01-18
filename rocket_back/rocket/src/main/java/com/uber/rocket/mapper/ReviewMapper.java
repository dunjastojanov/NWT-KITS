package com.uber.rocket.mapper;

import com.uber.rocket.dto.ReviewDTO;
import com.uber.rocket.entity.ride.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper implements Mapper<Review, ReviewDTO>{

    @Override
    public ReviewDTO mapToDto(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewer(review.getPassenger().getFullName());
        dto.setText(review.getDescription());
        dto.setRating(review.getRating());
        return dto;
    }
}
