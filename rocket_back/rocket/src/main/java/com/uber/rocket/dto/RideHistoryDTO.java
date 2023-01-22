package com.uber.rocket.dto;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideHistoryDTO {
    private String driver;
    private double price;
    private double duration;
    private String start;
    private String end;
    private Long id;
}
