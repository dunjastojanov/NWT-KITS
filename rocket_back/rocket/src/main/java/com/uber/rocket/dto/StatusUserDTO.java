package com.uber.rocket.dto;

import com.uber.rocket.entity.ride.UserRidingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUserDTO {
    @PositiveOrZero
    Long id;
    @NotBlank
    String firstName;
    @NotBlank
    String lastName;
    @Email
    String email;
    //Staviti not blank kad prorade slike
    String profilePicture;
    @NotBlank
    String role;
    UserRidingStatus status;
}
