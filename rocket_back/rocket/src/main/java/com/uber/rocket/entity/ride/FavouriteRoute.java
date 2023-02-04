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
public class FavouriteRoute {
    @Id
    @SequenceGenerator(name = "favourite_route_sequence", sequenceName = "favourite_route_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "favourite_route_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Ride ride;
}
