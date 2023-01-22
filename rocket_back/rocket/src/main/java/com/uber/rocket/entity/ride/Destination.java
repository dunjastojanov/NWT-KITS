package com.uber.rocket.entity.ride;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Setter
@Getter
@ToString
public class Destination {
    @Id
    @SequenceGenerator(name = "destination_sequence", sequenceName = "destination_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "destination_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne()
    private Ride ride;

    private String address;
    private double longitude;
    private double latitude;

}
