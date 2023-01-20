package com.uber.rocket.dto;

import com.uber.rocket.entity.user.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    Long id;
    String firstName;
    String lastName;
    String email;
    String profilePicture;
    String role;

    public UserDTO(Long id, String firstName, String lastName, String email, String profilePicture, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePicture = profilePicture;
        this.role = role;
    }
}
