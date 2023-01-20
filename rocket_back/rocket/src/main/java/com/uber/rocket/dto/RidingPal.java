package com.uber.rocket.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RidingPal {
    Long id;
    String firstName;
    String lastName;
    String email;
    String profilePicture;

    public RidingPal(Long id, String firstName, String lastName, String email, String profilePicture) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePicture = profilePicture;
    }
}
