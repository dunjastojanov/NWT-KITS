package com.uber.rocket.entity.user;


import com.uber.rocket.dto.UserRegistrationDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();


    @Column(unique = true)
    private String email;
    private String profilePicture;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String city;
    private boolean blocked;

    private Double tokens= (double) 0;

    public User(UserRegistrationDTO userRegistrationDTO) {
        this.setEmail(userRegistrationDTO.getEmail());
        this.setPassword(userRegistrationDTO.getPassword());
        this.setFirstName(userRegistrationDTO.getFirstName());
        this.setLastName(userRegistrationDTO.getLastName());
        this.setCity(userRegistrationDTO.getCity());
        this.setPhoneNumber(userRegistrationDTO.getPhoneNumber());
        this.setBlocked(true);
    }
}
