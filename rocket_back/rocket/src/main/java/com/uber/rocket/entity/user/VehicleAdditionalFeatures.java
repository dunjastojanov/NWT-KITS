package com.uber.rocket.entity.user;


import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class VehicleAdditionalFeatures {
    @Id
    @SequenceGenerator(name = "vehicle_additional_features_sequence", sequenceName = "vehicle_additional_features_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "vehicle_additional_features_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AdditionalVehicleFeature vehicleFeature;

}
