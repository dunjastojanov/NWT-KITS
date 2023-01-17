package com.uber.rocket.entity.user;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table
public class Message {
    @Id
    @SequenceGenerator(name = "message_sequence", sequenceName = "message_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "message_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToOne
    private User sender;

    @OneToOne
    private User receiver;

    private String message;

    private LocalDateTime sentAt;
}
