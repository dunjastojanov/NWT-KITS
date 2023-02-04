package com.uber.rocket.dto;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;

import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.entity.user.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {
    @PositiveOrZero
    Double longitude;
    @PositiveOrZero
    Double latitude;
    VehicleType type;

    boolean petFriendly;
    boolean kidFriendly;


    public VehicleDTO(Vehicle vehicle) {
        longitude = vehicle.getLongitude();
        latitude = vehicle.getLatitude();
        type = vehicle.getVehicleType();
        petFriendly = vehicle.isPetFriendly();
        kidFriendly = vehicle.isKidFriendly();
    }
}
