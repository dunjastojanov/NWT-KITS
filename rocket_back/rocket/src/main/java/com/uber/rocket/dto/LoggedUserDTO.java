package com.uber.rocket.dto;

import com.uber.rocket.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggedUserDTO {
    private String email;
    private String profilePicture;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String city;

    public LoggedUserDTO(User user) {
        this.email = user.getEmail();
        this.profilePicture = user.getProfilePicture();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.city = user.getCity();
    }
}
