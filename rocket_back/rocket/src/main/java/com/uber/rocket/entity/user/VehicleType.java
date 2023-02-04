package com.uber.rocket.entity.user;

import lombok.Getter;

@Getter
public enum VehicleType {

    LIMOUSINE("Limousine"),
    HATCHBACK("Hatchback"),
    CARAVAN("Caravan"),
    COUPE("Coupe"),
    CONVERTIBLE("Convertible"),
    MINIVAN("Minivan"),
    SUV("Suv"),
    PICKUP("Pickup");
    private final String name;

    VehicleType(String name) {
        this.name = name;
    }
}
