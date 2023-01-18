package com.uber.rocket.repository;

import com.uber.rocket.entity.user.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findByPaypalPaymentId(String paypalPaymentId);
}
