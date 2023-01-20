package com.uber.rocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUserDTO {
    Long id;
    String firstName;
    String lastName;
    String email;
    String profilePicture;
    String role;
    String status;
}
