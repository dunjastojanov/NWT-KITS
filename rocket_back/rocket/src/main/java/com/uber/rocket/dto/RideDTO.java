package com.uber.rocket.dto;

import com.uber.rocket.entity.ride.Destination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RideDTO {
    Long rideId;
    UserDTO client;
    StatusUserDTO[] ridingPals;
    boolean isSplitFair;
    StatusUserDTO driver;
    boolean isRouteFavorite;
    double estimatedDistance;
    double estimatedTime;
    double price;
    String route;
    DestinationDTO[] destinations;
    VehicleDTO vehicle;
    boolean isNow;
    String time;
    List<String> features;
    public void setIsNow(Boolean isNow) {
        this.isNow = isNow;
    }
    public Boolean getIsNow() {
        return this.isNow;
    }
    public void setIsRouteFavorite(Boolean isRouteFavorite) {
        this.isRouteFavorite = isRouteFavorite;
    }
    public Boolean getIsRouteFavorite() {
        return this.isRouteFavorite;
    }
    public void setIsSplitFair(Boolean isSplitFair) {
        this.isSplitFair = isSplitFair;
    }
    public Boolean getIsSplitFair() {
        return this.isSplitFair;
    }
}
