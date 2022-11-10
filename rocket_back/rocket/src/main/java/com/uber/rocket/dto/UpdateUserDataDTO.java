package com.uber.rocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.validation.constraints.NotBlank;
import java.lang.reflect.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDataDTO implements ValidateClassAttributes<UpdateUserDataDTO> {
    @NotBlank(message = "First name is mandatory")
    private String firstName;
    @NotBlank(message = "Last name is mandatory")
    private String lastName;
    @NotBlank(message = "Phone number is mandatory")
    private String phoneNumber;
    @NotBlank(message = "City is mandatory")
    private String city;

    @SneakyThrows
    @Override
    public void validateClassAttributes(UpdateUserDataDTO updateUserDataDTO) {
        for (Field f : getClass().getDeclaredFields())
            if (f.get(this) == null || f.get(this).equals(""))
                throw new IllegalAccessException("Parameter " + f.getName() + " is mandatory.");
    }
}
