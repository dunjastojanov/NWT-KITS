package com.uber.rocket.entity.user;

import lombok.*;

import javax.persistence.*;
import java.util.List;

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

    @ManyToMany
    private List<VehicleAdditionalFeatures> features;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

}
