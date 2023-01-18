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

    public FavouriteRouteDTO(FavouriteRoute favouriteRoute) {
        start = favouriteRoute.getRide().getStart();
        end = favouriteRoute.getRide().getEnd();
        id = favouriteRoute.getId();
    }

}
