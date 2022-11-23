package com.uber.rocket.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO {
    private SimpleUserDTO reviewer;
    private float rating;
    private String text;
}
