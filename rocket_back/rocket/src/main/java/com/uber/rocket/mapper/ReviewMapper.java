package com.uber.rocket.mapper;

import com.uber.rocket.dto.ReviewDTO;
import com.uber.rocket.entity.ride.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper implements Mapper<Review, ReviewDTO>{
    @Autowired
    private SimpleUserMapper simpleUserMapper;

    @Override
    public Review mapToEntity(ReviewDTO reviewDTO) {
        return null;
    }

    @Override
    public ReviewDTO mapToDto(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewer(simpleUserMapper.mapToDto(review.getPassenger()));
        dto.setText(review.getDescription());
        dto.setRating(review.getRating());
        return dto;
    }
}
