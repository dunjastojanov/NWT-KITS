package com.uber.rocket.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GoogleUser {
    private String id;
    private String email;
    private String photoUrl;
    private String firstName;
    private String lastName;
}
