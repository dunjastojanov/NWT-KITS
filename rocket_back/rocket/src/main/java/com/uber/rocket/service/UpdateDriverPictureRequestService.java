package com.uber.rocket.service;

import com.uber.rocket.entity.user.UpdateDriverPictureRequest;
import com.uber.rocket.repository.UpdateDriverPictureRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateDriverPictureRequestService {

    @Autowired
    private UpdateDriverPictureRequestRepository updateDriverPictureRequestRepository;

    public void createDriverPictureRequest(String newProfilePath, Long id) {
        UpdateDriverPictureRequest updateDriverPictureRequest = new UpdateDriverPictureRequest();
        updateDriverPictureRequest.setDriverId(id);
        updateDriverPictureRequest.setProfilePicture(newProfilePath);
        updateDriverPictureRequest.setValidated(false);
        updateDriverPictureRequestRepository.save(updateDriverPictureRequest);
    }

    public UpdateDriverPictureRequest getById(Long id) {
        Optional<UpdateDriverPictureRequest> driverPictureRequest = updateDriverPictureRequestRepository.findById(id);
        if (driverPictureRequest.isEmpty())
            throw new RuntimeException("There is no request for updating driver picture with this id");
        return driverPictureRequest.get();
    }
}
