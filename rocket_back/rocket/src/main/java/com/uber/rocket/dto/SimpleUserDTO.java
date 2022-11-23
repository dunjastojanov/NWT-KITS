package com.uber.rocket.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleUserDTO {
    private String firstName;
    private String lastName;
}
