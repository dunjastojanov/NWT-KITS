package com.uber.rocket.entity.user;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table
public class EditInfoRequest {
    @Id
    @SequenceGenerator(name = "edit_info_request_sequence", sequenceName = "edit_info_request_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "edit_info_request_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    private Long driverId;

    @NotNull
    private String imageUrl;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String city;

}
