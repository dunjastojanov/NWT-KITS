package com.uber.rocket.service;

import com.uber.rocket.dto.UserDTO;
import com.uber.rocket.entity.user.RoleType;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RoleService roleService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private EmailService emailService;

    public User getUserByEmail(String email) {
        Optional<User> client = userRepository.findByEmail(email);
        if (client.isEmpty())
            throw new RuntimeException("User not found");
        return client.get();
    }

    public void register(UserDTO userDTO) throws IllegalAccessException {
        userDTO.validateClassAttributes(userDTO);
        Optional<User> client = userRepository.findByEmail(userDTO.getEmail());
        if (client.isPresent())
            throw new RuntimeException("User with this email already exists");
        User user = createUser(userDTO);
        user.getRoles().add(roleService.getRoleByUserRole(RoleType.CLIENT));
        userRepository.save(user);
        String token = UUID.randomUUID().toString();
        confirmationTokenService.createToken(user, token);
        emailService.sendRegistrationEmail(user, token);
    }

    private User createUser(UserDTO userDTO) {
        User user = new User(userDTO);
        user.setRoles(new ArrayList<>());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new ArrayList<>());
        return user;
    }

    public void validateRegistrationToken(String token) {
        User user = confirmationTokenService.validateToken(token);
        user.setBlocked(false);
        userRepository.save(user);
    }
}
