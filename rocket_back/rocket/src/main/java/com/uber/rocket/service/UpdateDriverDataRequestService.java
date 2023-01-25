package com.uber.rocket.service;

import com.uber.rocket.dto.UpdateDriverDto;
import com.uber.rocket.dto.UpdateUserDataDTO;
import com.uber.rocket.entity.user.UpdateDriverDataRequest;
import com.uber.rocket.entity.user.VehicleType;
import com.uber.rocket.repository.UpdateDriverRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateDriverDataRequestService {
    @Autowired
    private UpdateDriverRequestRepository updateDriverRequestRepository;

    @Autowired
    private NotificationService notificationService;

    public void createDriverDataRequest(UpdateDriverDto dto, Long driverId) {
        UpdateDriverDataRequest updateDriverDataRequest = new UpdateDriverDataRequest();
        updateDriverDataRequest.setDriverId(driverId);
        updateDriverDataRequest.setFirstName(dto.getFirstName());
        updateDriverDataRequest.setLastName(dto.getLastName());
        updateDriverDataRequest.setPhoneNumber(dto.getPhoneNumber());
        updateDriverDataRequest.setCity(dto.getCity());
        updateDriverDataRequest.setValidated(false);
        updateDriverDataRequest.setKidFriendly(dto.isKidFriendly());
        updateDriverDataRequest.setPetFriendly(dto.isPetFriendly());
        updateDriverDataRequest.setType(VehicleType.valueOf(dto.getType().toUpperCase()));
        updateDriverRequestRepository.save(updateDriverDataRequest);
        notificationService.addUpdateDriverDataRequestNotification(updateDriverDataRequest);
    }

    public UpdateDriverDataRequest getById(Long id) {
        Optional<UpdateDriverDataRequest> driverDataRequest = updateDriverRequestRepository.findById(id);
        if (driverDataRequest.isEmpty())
            throw new RuntimeException("Driver data request with this id doesn't exist");
        return driverDataRequest.get();
    }

    public UpdateDriverDataRequest save(UpdateDriverDataRequest updateDriverDataRequest) {
        return updateDriverRequestRepository.save(updateDriverDataRequest);
    }
}
