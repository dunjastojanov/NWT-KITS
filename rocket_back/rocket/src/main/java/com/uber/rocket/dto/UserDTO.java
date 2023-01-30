package com.uber.rocket.dto;

import com.uber.rocket.entity.user.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.*;
@Data
@NoArgsConstructor
public class UserDTO {
    @PositiveOrZero
    Long id;
    @NotBlank
    String firstName;
    @NotBlank
    String lastName;
    @Email
    @NotBlank
    String email;
    String profilePicture;
    @NotBlank
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
