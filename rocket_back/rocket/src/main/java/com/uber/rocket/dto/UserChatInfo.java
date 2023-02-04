package com.uber.rocket.dto;

import com.uber.rocket.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChatInfo {
    private String profilePicture;
    private String email;
    private String firstName;
    private String lastName;

    public UserChatInfo(User user) {
        this.profilePicture = user.getProfilePicture();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
    }
}
