package com.uber.rocket.entity.user;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@Data
public class LogInfo {
    @Id
    @SequenceGenerator(name = "log_info_sequence", sequenceName = "log_info_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "log_info_sequence")
    @Setter(AccessLevel.NONE)
    private Long id;
    private Long userId;
    private LocalDateTime begging;
    private LocalDateTime ending;

}
