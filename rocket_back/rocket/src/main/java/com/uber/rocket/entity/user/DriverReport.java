package com.uber.rocket.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table
public class DriverReport {

    @Id
    @SequenceGenerator(name = "driver_report_sequence", sequenceName = "driver_report_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "driver_report_sequence")
    private Long id;

    @ManyToOne
    private User driver;
    @ManyToOne
    private User passenger;
}
