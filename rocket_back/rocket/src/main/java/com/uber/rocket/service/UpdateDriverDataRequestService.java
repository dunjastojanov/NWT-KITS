package com.uber.rocket.service;

import com.uber.rocket.dto.UpdateUserDataDTO;
import com.uber.rocket.entity.user.UpdateDriverDataRequest;
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

    public void createDriverDataRequest(UpdateUserDataDTO updateUserDataDTO, Long driverId) {
        UpdateDriverDataRequest updateDriverDataRequest = new UpdateDriverDataRequest();
        updateDriverDataRequest.setDriverId(driverId);
        updateDriverDataRequest.setFirstName(updateUserDataDTO.getFirstName());
        updateDriverDataRequest.setLastName(updateUserDataDTO.getLastName());
        updateDriverDataRequest.setPhoneNumber(updateUserDataDTO.getPhoneNumber());
        updateDriverDataRequest.setCity(updateUserDataDTO.getCity());
        updateDriverDataRequest.setValidated(false);
        updateDriverRequestRepository.save(updateDriverDataRequest);
        notificationService.addUpdateDriverDataRequestNotification(updateDriverDataRequest);
    }

    public UpdateDriverDataRequest getById(Long id) {
        Optional<UpdateDriverDataRequest> driverDataRequest = updateDriverRequestRepository.findById(id);
        if (driverDataRequest.isEmpty())
            throw new RuntimeException("Driver data request with this id doesn't exist");
        return driverDataRequest.get();
    }
}
