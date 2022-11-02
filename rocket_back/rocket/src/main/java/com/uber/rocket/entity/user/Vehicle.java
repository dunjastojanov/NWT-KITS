package com.uber.rocket.entity.user;

import com.sun.istack.NotNull;
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
    @NotNull
    private User driver;

    @NotNull
    private String type;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

}
