package com.uber.rocket.entity.ride;

import com.sun.istack.NotNull;
import com.uber.rocket.entity.user.User;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table
@Entity
public class Review {
    @Id
    @SequenceGenerator(name = "review_sequence", sequenceName = "review_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "review_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToOne
    @NotNull
    private User passenger;

    @NotNull
    private int driverRating;

    @NotNull
    private int vehicleRating;

    @NotNull
    private String description;

}
