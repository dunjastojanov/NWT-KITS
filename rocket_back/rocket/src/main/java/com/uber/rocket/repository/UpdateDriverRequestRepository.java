package com.uber.rocket.repository;

import com.uber.rocket.entity.user.UpdateDriverDataRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdateDriverRequestRepository extends JpaRepository<UpdateDriverDataRequest, Long> {
}
