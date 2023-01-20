package com.uber.rocket.dto;
import javax.validation.constraints.*;
import com.uber.rocket.entity.user.VehicleType;

public class VehicleDTO {
    @PositiveOrZero
    Double longitude;
    @PositiveOrZero
    Double latitude;
    VehicleType type;
}
