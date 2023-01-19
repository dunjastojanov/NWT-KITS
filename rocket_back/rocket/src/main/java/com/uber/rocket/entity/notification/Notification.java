package com.uber.rocket.entity.notification;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.uber.rocket.entity.user.User;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table
public class Notification {
    @Id
    @SequenceGenerator(name = "notification_sequence", sequenceName = "notification_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "notification_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;
    @NotNull
    @ManyToOne
    User user;
    @NotNull
    String html;
    @NotNull
    String title;
    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    @NotNull
    boolean read;
    @Nullable
    Long entityId;

}
