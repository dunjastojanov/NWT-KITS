package com.uber.rocket.dto;

import com.uber.rocket.entity.user.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideSimulationDTO {
    VehicleStatus vehicleStatus;
    RideInfoSimulationDTO ride;
}
