package com.uber.rocket.service;

import com.uber.rocket.entity.user.UpdateDriverPictureRequest;
import com.uber.rocket.repository.UpdateDriverPictureRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateDriverPictureRequestService {

    @Autowired
    private UpdateDriverPictureRequestRepository updateDriverPictureRequestRepository;

    public void createDriverPictureRequest(String newProfilePath, Long id) {
        UpdateDriverPictureRequest updateDriverPictureRequest = new UpdateDriverPictureRequest();
        updateDriverPictureRequest.setDriverId(id);
        updateDriverPictureRequest.setProfilePicture(newProfilePath);
        updateDriverPictureRequestRepository.save(updateDriverPictureRequest);
    }
}
