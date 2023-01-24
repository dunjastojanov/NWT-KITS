package com.uber.rocket.dto;

import lombok.*;
import com.uber.rocket.entity.user.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleUser {
    private String fullName;
    private String email;

    public SimpleUser(User user) {
        fullName = user.getFullName();
        email = user.getEmail();
    }
}
