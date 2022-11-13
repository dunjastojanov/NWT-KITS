package com.uber.rocket.entity.ride;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table
@Entity
public class FutureRideRequest {
    @Id
    @SequenceGenerator(name = "future_ride_request_sequence", sequenceName = "future_ride_request_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "future_ride_request_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    private LocalDateTime timeStamp;

    @NotNull
    private int paid;

    //TODO additional feature, putanja (od-do)

}
