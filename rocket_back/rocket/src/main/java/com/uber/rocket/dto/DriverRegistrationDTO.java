package com.uber.rocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DriverRegistrationDTO {
    @Email(message = "Wrong email format", regexp = ".+[@].+[\\.].+")
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotBlank(message = "First name is mandatory")
    private String firstName;
    @NotBlank(message = "Last name is mandatory")
    private String lastName;
    @NotBlank(message = "Phone number is mandatory")
    private String phoneNumber;
    @NotBlank(message = "City is mandatory")
    private String city;
    @NotBlank(message = "Vehicle type is mandatory")
    @Pattern(regexp = "suv|limousine|hatchback|caravan|coupe|convertible|minivan|pickup", message = "Vehicle types can only be suv,limousine,hatchback,caravan,coupe,convertible,minivan or pickup")
    private String vehicleType;
    @NotNull(message = "Pet friendly feature must be assigned")
    private boolean petFriendly;
    @NotNull(message = "Kid friendly feature must be assigned")
    private boolean kidFriendly;
}


