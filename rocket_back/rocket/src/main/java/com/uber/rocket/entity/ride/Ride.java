package com.uber.rocket.entity.ride;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.entity.user.Vehicle;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Setter
@Getter
public class Ride {
    @Id
    @SequenceGenerator(name = "ride_sequence", sequenceName = "ride_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ride_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToMany
    @NotNull
    private Collection<User> passengers;

    @OneToOne
    @Nullable
    private Vehicle vehicle;

    @NotNull
    private String routeLocation;

    private LocalDateTime startTime;
    @Nullable
    private LocalDateTime endTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RideStatus status;

    @NotNull
    private Boolean splitFare;

    @NotNull
    private int price;

    @NotNull
    private int duration;

    private double length;

    public User getDriver() {
        return vehicle.getDriver();
    }

}
