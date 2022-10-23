package com.uber.rocket.entity.ride;

import com.sun.istack.NotNull;
import com.uber.rocket.entity.user.Client;
import com.uber.rocket.entity.user.Vehicle;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table
public class Ride {
    @Id
    @SequenceGenerator(name = "ride_sequence", sequenceName = "ride_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ride_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToMany
    @NotNull
    private Collection<Client> passengers;

    @OneToOne
    @NotNull
    private Vehicle vehicle;

    @OneToOne
    @NotNull
    private Route route;

    @OneToMany
    @NotNull
    private Collection<Destination> destination;

    @OneToMany
    @NotNull
    private Collection<Review> reviews;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RideStatus status;

    @NotNull
    private Boolean splitFare;

    @NotNull
    private int price;

}
