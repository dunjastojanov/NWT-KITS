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
public class PayInfo {
    @Id
    @SequenceGenerator(sequenceName = "pay_info_sequence", name = "pay_info_sequence", allocationSize = 1)
    @GeneratedValue(generator = "pay_info_sequence", strategy = GenerationType.AUTO)
    private Long id;
    //TODO we need to add attributes for paying info -> see paypal

}
