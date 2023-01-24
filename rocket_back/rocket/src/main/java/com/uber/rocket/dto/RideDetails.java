package com.uber.rocket.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideDetails {

    Long id;
    String driver;
    List<String> passengers;
    String routeLocation;
    String driverProfileImage;
    String start;
    String end;
    String price;
    String duration;
    double rating;
    List<ReviewDTO> reviews;
}
