package com.uber.rocket.dto;

import com.uber.rocket.entity.ride.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideInfoSimulationDTO {
    RideStatus status;
    List<Double> destination;
    String routeCoordinates;
}
