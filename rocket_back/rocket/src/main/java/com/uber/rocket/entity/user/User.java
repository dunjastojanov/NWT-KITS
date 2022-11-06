package com.uber.rocket.entity.user;


import com.uber.rocket.dto.UserDTO;
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

    @OneToOne
    private PayInfo payInfo;

    @Column(unique = true)
    private String email;
    private String profilePicture;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String city;
    private boolean blocked;

    public User(UserDTO userDTO) {
        this.setEmail(userDTO.getEmail());
        this.setPassword(userDTO.getPassword());
        this.setFirstName(userDTO.getFirstName());
        this.setLastName(userDTO.getLastName());
        this.setCity(userDTO.getCity());
        this.setPhoneNumber(userDTO.getPhoneNumber());
        this.setBlocked(true);
    }
}
