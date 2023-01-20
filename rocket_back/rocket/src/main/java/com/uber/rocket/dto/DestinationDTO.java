package com.uber.rocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinationDTO {
    @PositiveOrZero
    int index;
    @NotBlank
    String address;
    @Positive
    double longitude;
    @Positive
    double latitude;

}
