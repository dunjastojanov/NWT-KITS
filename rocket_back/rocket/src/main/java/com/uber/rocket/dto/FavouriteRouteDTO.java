package com.uber.rocket.dto;

import com.uber.rocket.entity.ride.FavouriteRoute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FavouriteRouteDTO {
    String start;
    String end;
    Long id;
}
