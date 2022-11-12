package com.uber.rocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ForgetPasswordDTO {

    @NotBlank(message = "Old password is mandatory")
    private String token;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$",
            message = "Old Password must contain at least one upper letter, at least one lower latter and at least one digit")
    @NotBlank(message = "Old password is mandatory")
    private String password;
}
