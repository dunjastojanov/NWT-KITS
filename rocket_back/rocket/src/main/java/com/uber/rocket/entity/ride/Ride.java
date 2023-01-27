package com.uber.rocket.entity.ride;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.entity.user.VehicleType;
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
@ToString
public class Ride {
    @Id
    @SequenceGenerator(name = "ride_sequence", sequenceName = "ride_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ride_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToMany
    @NotNull
    @JoinColumn(name = "ride_id")
    private Collection<Passenger> passengers;
    @OneToOne
    @Nullable
    private Vehicle vehicle;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleTypeRequested;
    private boolean petFriendly;
    private boolean kidFriendly;

    @NotNull
    @Column(length = 512)
    private String routeLocation;

    private boolean now;
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

    //menjano
    @NotNull
    private Double duration;

    @NotNull
    private double length;

    public User getDriver() {
        return vehicle.getDriver();
    }

    public List<User> getUsers() {
        return passengers.stream().map(Passenger::getUser).toList();
    }

}
