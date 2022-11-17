package com.uber.rocket.service;

import com.uber.rocket.dto.UpdateUserDataDTO;
import com.uber.rocket.entity.user.UpdateDriverDataRequest;
import com.uber.rocket.repository.UpdateDriverRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateDriverDataRequestService {
    @Autowired
    private UpdateDriverRequestRepository updateDriverRequestRepository;

    public void createDriverDataRequest(UpdateUserDataDTO updateUserDataDTO, Long driverId) {
        UpdateDriverDataRequest updateDriverDataRequest = new UpdateDriverDataRequest();
        updateDriverDataRequest.setDriverId(driverId);
        updateDriverDataRequest.setFirstName(updateUserDataDTO.getFirstName());
        updateDriverDataRequest.setLastName(updateUserDataDTO.getLastName());
        updateDriverDataRequest.setPhoneNumber(updateUserDataDTO.getPhoneNumber());
        updateDriverDataRequest.setCity(updateUserDataDTO.getCity());
        updateDriverRequestRepository.save(updateDriverDataRequest);
    }

}
