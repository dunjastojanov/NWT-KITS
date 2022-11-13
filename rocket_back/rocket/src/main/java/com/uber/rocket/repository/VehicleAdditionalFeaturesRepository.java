package com.uber.rocket.repository;

import com.uber.rocket.entity.user.AdditionalVehicleFeature;
import com.uber.rocket.entity.user.VehicleAdditionalFeatures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleAdditionalFeaturesRepository extends JpaRepository<VehicleAdditionalFeatures, Long> {
    Optional<VehicleAdditionalFeatures> findByVehicleFeature(AdditionalVehicleFeature kidFriendly);
}
