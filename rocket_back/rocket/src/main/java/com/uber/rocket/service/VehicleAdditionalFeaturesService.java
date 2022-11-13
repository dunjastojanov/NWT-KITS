package com.uber.rocket.service;

import com.uber.rocket.entity.user.AdditionalVehicleFeature;
import com.uber.rocket.entity.user.VehicleAdditionalFeatures;
import com.uber.rocket.repository.VehicleAdditionalFeaturesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehicleAdditionalFeaturesService {

    @Autowired
    private VehicleAdditionalFeaturesRepository vehicleAdditionalFeaturesRepository;

    public VehicleAdditionalFeatures getFeatureByName(AdditionalVehicleFeature kidFriendly) {
        Optional<VehicleAdditionalFeatures> feature = vehicleAdditionalFeaturesRepository.findByVehicleFeature(kidFriendly);
        if (feature.isEmpty())
            throw new RuntimeException("This vehicle feature doesn't exist");
        return feature.get();
    }
}
