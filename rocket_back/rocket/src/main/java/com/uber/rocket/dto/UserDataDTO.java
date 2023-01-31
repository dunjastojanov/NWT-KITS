package com.uber.rocket.dto;

import com.uber.rocket.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDataDTO {
    private Long id;
    private String email;
    private String profilePicture;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String city;
    private ArrayList<String> roles;
    private String status = "";
    private double tokens = 0;

    public UserDataDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.profilePicture = user.getProfilePicture();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.city = user.getCity();
        this.roles = new ArrayList<>();
        this.tokens += user.getTokens();
        user.getRoles().forEach(role -> this.roles.add(role.getRole()));
    }
}
