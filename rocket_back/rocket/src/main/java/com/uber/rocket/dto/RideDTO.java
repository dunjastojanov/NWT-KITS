package com.uber.rocket.dto;

import com.uber.rocket.entity.ride.Destination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RideDTO {
    @PositiveOrZero
    Long rideId;
    @Valid
    UserDTO client;
    @Valid
    @Size(min = 0)
    List<StatusUserDTO> ridingPals;
    Boolean isSplitFair;
    @Null
    @Valid
    StatusUserDTO driver;
    Boolean isRouteFavorite;
    @Positive
    Double estimatedDistance;
    @Positive
    Double estimatedTime;
    @Positive
    Integer price;
    @NotBlank
    String route;
    @Valid
    @Size(min = 2)
    List<DestinationDTO> destinations;
    @Valid
    VehicleDTO vehicle;
    Boolean isNow;
    @NotBlank
    String time;
    @Size(min = 0)
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
    public void addRidingPal(StatusUserDTO user) {
        if (this.ridingPals != null) {
            this.ridingPals.add(user);
        } else {
            this.ridingPals = new ArrayList<>();
            this.ridingPals.add(user);
        }
    }
    public void addDestination(DestinationDTO destinationDTO) {
        if (this.destinations != null) {
            this.destinations.add(destinationDTO);
        } else {
            this.destinations = new ArrayList<>();
            this.destinations.add(destinationDTO);
        }
    }
}
