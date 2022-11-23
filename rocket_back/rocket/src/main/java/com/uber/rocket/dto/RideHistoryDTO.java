package com.uber.rocket.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideHistoryDTO {
    private SimpleUserDTO driver;
    private List<SimpleUserDTO> passengers;
    private int price;
    private int duration;
    private String start;
    private String end;
    private float rating;
    private List<ReviewDTO> reviews;
}
