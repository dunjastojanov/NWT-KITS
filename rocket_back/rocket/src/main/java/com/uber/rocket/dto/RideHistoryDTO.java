package com.uber.rocket.dto;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideHistoryDTO {
    private String driver;
    private int price;
    private int duration;
    private String start;
    private String end;
    private Long id;
}
