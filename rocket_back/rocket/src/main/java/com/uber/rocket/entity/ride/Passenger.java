package com.uber.rocket.entity.ride;

import com.uber.rocket.entity.user.User;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Setter
@Getter
@ToString
public class Passenger {
    @Id
    @SequenceGenerator(name = "passenger_sequence", sequenceName = "passenger_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "passenger_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    User user;

    @Enumerated(EnumType.STRING)
    UserRidingStatus userRidingStatus;

    boolean booked;
    public Passenger(User user, UserRidingStatus userRidingStatus) {
        this.user = user;
        this.userRidingStatus = userRidingStatus;
        this.booked = false;
    }
}
