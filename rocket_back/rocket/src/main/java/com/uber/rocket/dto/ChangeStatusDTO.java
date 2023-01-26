package com.uber.rocket.dto;

import com.uber.rocket.entity.ride.UserRidingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStatusDTO {
    String userId;
    UserRidingStatus ridingStatus;
}
