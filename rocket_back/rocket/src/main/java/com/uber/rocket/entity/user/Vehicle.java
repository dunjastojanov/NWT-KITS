package com.uber.rocket.entity.user;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table
public class Vehicle {
    @Id
    @SequenceGenerator(name = "vehicle_sequence", sequenceName = "vehicle_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "vehicle_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToOne
    private User driver;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private boolean petFriendly;

    private boolean kidFriendly;

    private String currentLocation;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

}
