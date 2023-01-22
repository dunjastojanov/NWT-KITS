package com.uber.rocket.mapper;

import com.uber.rocket.dto.FavouriteRouteDTO;
import com.uber.rocket.entity.ride.FavouriteRoute;
import com.uber.rocket.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FavouriteRouteMapper implements Mapper<FavouriteRoute, FavouriteRouteDTO>{

    @Autowired
    DestinationService destinationService;
    @Override
    public FavouriteRouteDTO mapToDto(FavouriteRoute favouriteRoute) {
        FavouriteRouteDTO favouriteRouteDTO = new FavouriteRouteDTO();
        favouriteRouteDTO.setId(favouriteRoute.getId());
        favouriteRouteDTO.setStart(destinationService.getStartAddressByRide(favouriteRoute.getRide()));
        favouriteRouteDTO.setEnd(destinationService.getEndAddressByRide(favouriteRoute.getRide()));
        return favouriteRouteDTO;
    }
}
