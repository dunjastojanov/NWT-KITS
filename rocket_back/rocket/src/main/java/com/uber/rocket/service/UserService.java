package com.uber.rocket.service;

import com.uber.rocket.entity.user.User;
import com.uber.rocket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public User getUserByEmail(String email) {
        Optional<User> client= userRepository.findByEmail(email);
        if(client.isEmpty())
            throw new RuntimeException("User not found");
        return client.get();
    }


}
