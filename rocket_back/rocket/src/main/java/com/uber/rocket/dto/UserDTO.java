package com.uber.rocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements ValidateClassAttributes<UserDTO> {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String city;

    @SneakyThrows
    @Override
    public void validateClassAttributes(UserDTO userDTO) {
        for (Field f : getClass().getDeclaredFields())
            if (f.get(this) == null || f.get(this).equals(""))
                throw new IllegalAccessException("Parameter " + f.getName() + " is mandatory.");
    }
}
