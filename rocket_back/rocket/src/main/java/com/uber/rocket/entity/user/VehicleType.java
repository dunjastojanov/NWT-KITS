package com.uber.rocket.entity.user;

import lombok.Getter;

@Getter
public enum VehicleType {

    LIMOUSINE("limousine"),
    HATCHBACK("hatchback"),
    CARAVAN("caravan"),
    COUPE("coupe"),
    CONVERTIBLE("convertible"),
    MINIVAN("minivan"),
    SUV("suv"),
    PICKUP("pickup");
    private final String name;

    VehicleType(String name) {
        this.name = name;
    }
}
