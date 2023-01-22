package com.uber.rocket.dto;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
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
}
