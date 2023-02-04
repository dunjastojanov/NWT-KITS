package com.uber.rocket.mapper;

import com.uber.rocket.dto.UpdateUserDataDTO;
import com.uber.rocket.entity.user.UpdateDriverDataRequest;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserDataMapper implements Mapper<UpdateDriverDataRequest, UpdateUserDataDTO> {

    @Override
    public UpdateUserDataDTO mapToDto(UpdateDriverDataRequest updateDriverDataRequest) {
        UpdateUserDataDTO updateUserDataDTO = new UpdateUserDataDTO();
        updateUserDataDTO.setCity(updateDriverDataRequest.getCity());
        updateUserDataDTO.setFirstName(updateDriverDataRequest.getFirstName());
        updateUserDataDTO.setLastName(updateDriverDataRequest.getLastName());
        updateUserDataDTO.setPhoneNumber(updateDriverDataRequest.getPhoneNumber());
        return updateUserDataDTO;
    }
}
