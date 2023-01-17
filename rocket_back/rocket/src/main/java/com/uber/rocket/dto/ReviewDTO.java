package com.uber.rocket.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO {
    private String reviewer;
    private double rating;
    private String text;
}
