package com.uber.rocket.entity.ride;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table
public class Destination {
    @Id
    @SequenceGenerator(name = "destination_sequence", sequenceName = "destination_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "destination_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    private String city;
    @NotNull
    private String street;
    @NotNull
    private String number;

    public String toString() {
        return  street + " " + number + ", " + city;
    }
}
