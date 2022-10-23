package com.uber.rocket.entity.ride;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table
public class Route {
    @Id
    @SequenceGenerator(name = "route_sequence", sequenceName = "route_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "route_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

}
