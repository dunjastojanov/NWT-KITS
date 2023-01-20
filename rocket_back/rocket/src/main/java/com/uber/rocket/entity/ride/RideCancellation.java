package com.uber.rocket.entity.ride;

import com.sun.istack.NotNull;
import com.uber.rocket.entity.user.User;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table
public class RideCancellation {
    @Id
    @SequenceGenerator(name = "ride_cancellation_sequence", sequenceName = "ride_cancellation_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ride_cancellation_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToOne
    @NotNull
    private User driver;

    @NotNull
    private String description;

    @ManyToOne
    private Ride ride;

}
