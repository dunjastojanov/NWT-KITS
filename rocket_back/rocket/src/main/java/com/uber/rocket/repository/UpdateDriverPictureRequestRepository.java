package com.uber.rocket.repository;

import com.uber.rocket.entity.user.UpdateDriverPictureRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdateDriverPictureRequestRepository extends JpaRepository<UpdateDriverPictureRequest, Long> {



}
