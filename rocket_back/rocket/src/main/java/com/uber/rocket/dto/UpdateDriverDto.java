package com.uber.rocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDriverDto {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String city;
    private String type;
    private boolean kidFriendly;
    private boolean petFriendly;
}
