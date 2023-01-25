package com.uber.rocket.entity.user;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table
public class UpdateDriverDataRequest {
    @Id
    @SequenceGenerator(name = "update_driver_data_request_sequence", sequenceName = "update_driver_data_request_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "update_driver_data_request_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    private Long driverId;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String city;

    @NotNull
    private boolean kidFriendly;

    @NotNull
    private boolean petFriendly;

    @NotNull
    private VehicleType type;
    @NotNull
    private boolean validated;

}
