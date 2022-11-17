package com.uber.rocket.entity.user;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table
public class UpdateDriverPictureRequest {
    @Id
    @SequenceGenerator(name = "update_driver_profile_picture_sequence", sequenceName = "update_driver_picture_request_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "update_driver_picture_request_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    private Long driverId;

    @NotNull
    private String profilePicture;


}
