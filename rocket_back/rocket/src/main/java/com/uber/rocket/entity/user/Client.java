package com.uber.rocket.entity.user;

//import com.uber.rocket.entity.user.Role;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.Columns;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table
public class Client {
    @Id
    @SequenceGenerator(name = "client_sequence", sequenceName = "client_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "client_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToMany
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

}
