package com.uber.rocket.entity.user;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Role {
    @Id
    @SequenceGenerator(name = "role_sequence", sequenceName = "role_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "role_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    private String role;

}
