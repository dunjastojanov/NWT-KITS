package com.uber.rocket.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideHistory {
    private String driver;
    private double price;
    private double duration;
    private String start;
    private String end;

    private String date;

    List<DestinationDTO> destinations;
    String routeLocation;
    private Long id;
}
