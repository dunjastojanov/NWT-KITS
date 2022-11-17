package com.uber.rocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.lang.reflect.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO implements ValidateClassAttributes<UserRegistrationDTO> {

    @Email(message = "Wrong email format", regexp = ".+[@].+[\\.].+")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$", message = "Password must contain at least one upper letter, at least one lower latter and at least one digit")
    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "First name is mandatory")
    private String firstName;
    @NotBlank(message = "Last name is mandatory")
    private String lastName;
    @NotBlank(message = "Phone number is mandatory")
    private String phoneNumber;
    @NotBlank(message = "City is mandatory")
    private String city;

    public UserRegistrationDTO(DriverRegistrationDTO driverRegistrationDTO, String password) {
        this.email = driverRegistrationDTO.getEmail();
        this.lastName = driverRegistrationDTO.getLastName();
        this.phoneNumber = driverRegistrationDTO.getPhoneNumber();
        this.city = driverRegistrationDTO.getCity();
        this.firstName = driverRegistrationDTO.getFirstName();
        this.password = password;
    }

    @SneakyThrows
    @Override
    public void validateClassAttributes(UserRegistrationDTO userRegistrationDTO) {
        for (Field f : getClass().getDeclaredFields())
            if (f.get(this) == null || f.get(this).equals(""))
                throw new IllegalAccessException("Parameter " + f.getName() + " is mandatory.");
    }
}
