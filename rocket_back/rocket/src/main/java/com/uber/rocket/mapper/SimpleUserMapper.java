package com.uber.rocket.mapper;

import com.uber.rocket.dto.SimpleUserDTO;
import com.uber.rocket.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class SimpleUserMapper implements Mapper<User, SimpleUserDTO>{
    @Override
    public SimpleUserDTO mapToDto(User user) {
        SimpleUserDTO dto = new SimpleUserDTO();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        return dto;
    }
}
