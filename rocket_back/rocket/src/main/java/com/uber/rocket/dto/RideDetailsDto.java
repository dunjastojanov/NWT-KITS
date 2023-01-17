package com.uber.rocket.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideDetailsDto {
    String driver;
    String start;
    String end;
    String price;
    String duration;
    double rating;
    List<ReviewDTO> reviews;
}
