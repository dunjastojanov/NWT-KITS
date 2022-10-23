package com.uber.rocket.entity.user;


import com.uber.rocket.entity.ride.Route;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table
public class FavouriteRoute {
    @Id
    @SequenceGenerator(name = "favourite_route_sequence", sequenceName = "favourite_route_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "favourite_route_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;
    @OneToOne
    private Client user;

    @OneToMany
    private Collection<Route> routes;
}
