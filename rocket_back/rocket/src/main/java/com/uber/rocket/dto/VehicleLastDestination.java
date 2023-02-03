package com.uber.rocket.dto;

import com.uber.rocket.entity.ride.Destination;
import com.uber.rocket.entity.user.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleLastDestination {
    Destination endDestinationCurrentRide;
    Vehicle vehicle;
}