package com.uber.rocket.service;

import com.uber.rocket.dto.DriverRegistrationDTO;
import com.uber.rocket.entity.user.*;
import com.uber.rocket.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private VehicleAdditionalFeaturesService additionalFeaturesService;

    public Object registerDriver(DriverRegistrationDTO driverRegistrationDTO) throws IOException {
        User driver = userService.registerDriver(driverRegistrationDTO);
        createVehicle(driverRegistrationDTO, driver);
        return "Successful driver registration";
    }

    private void createVehicle(DriverRegistrationDTO driverRegistrationDTO, User driver) {
        Vehicle vehicle = new Vehicle();
        vehicle.setDriver(driver);
        vehicle.setStatus(VehicleStatus.INACTIVE);
        vehicle.setVehicleType(VehicleType.valueOf(driverRegistrationDTO.getVehicleType().toUpperCase()));
        vehicle.setFeatures(new ArrayList<>());
        setAdditionalFeatures(vehicle, driverRegistrationDTO);
        vehicleRepository.save(vehicle);
    }

    public void setAdditionalFeatures(Vehicle vehicle, DriverRegistrationDTO driverRegistrationDTO) {
        if (driverRegistrationDTO.isKidFriendly())
            vehicle.getFeatures().add(additionalFeaturesService.getFeatureByName(AdditionalVehicleFeature.KID_FRIENDLY));
        if (driverRegistrationDTO.isPetFriendly())
            vehicle.getFeatures().add(additionalFeaturesService.getFeatureByName(AdditionalVehicleFeature.PET_FRIENDLY));
    }

    public Object getDriversByFilter(int size, int number, String filter) {
        return userService.getDriversByFilter(size, number, filter);
    }
}
