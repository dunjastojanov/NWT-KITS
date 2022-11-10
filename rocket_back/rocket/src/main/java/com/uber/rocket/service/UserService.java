package com.uber.rocket.service;

import com.uber.rocket.dto.*;
import com.uber.rocket.entity.user.RoleType;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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

    @Autowired
    private AuthService authService;

    @Autowired
    private ImageService imageService;

    public User getUserByEmail(String email) {
        Optional<User> client = userRepository.findByEmail(email);
        if (client.isEmpty())
            throw new RuntimeException("User not found");
        return client.get();
    }

    public String register(UserDTO userDTO) throws IllegalAccessException {
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
        return "Successful registration";
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

    public LoggedUserDTO getLoggedUser(HttpServletRequest request) {
        return new LoggedUserDTO(getUserFromRequest(request));
    }

    public ResponseObjectDTO updateUser(HttpServletRequest request, UpdateUserDataDTO updateUserDataDTO) {
        updateUserDataDTO.validateClassAttributes(updateUserDataDTO);
        User user = getUserFromRequest(request);
        updateUserData(user, updateUserDataDTO);
        return new ResponseObjectDTO(new LoggedUserDTO(user), "Successful update of user");
    }

    private void updateUserData(User user, UpdateUserDataDTO updateUserDataDTO) {
        user.setCity(updateUserDataDTO.getCity());
        user.setFirstName(updateUserDataDTO.getFirstName());
        user.setLastName(updateUserDataDTO.getLastName());
        user.setPhoneNumber(updateUserDataDTO.getPhoneNumber());
        userRepository.save(user);
    }

    private User getUserFromRequest(HttpServletRequest request) {
        String email = authService.getUsernameFromJWT(request.getHeader(AUTHORIZATION));
        return getUserByEmail(email);
    }

    public ResponseObjectDTO updateProfilePicture(HttpServletRequest request, MultipartFile multipart) throws IOException {
        User user = getUserFromRequest(request);
        user.setProfilePicture(imageService.saveProfilePicture(multipart, String.valueOf(user.getId())));
        userRepository.save(user);
        return new ResponseObjectDTO(user.getProfilePicture(), "Successful update of profile picture");
    }

    public String getProfilePicture(String email) {
        return getUserByEmail(email).getProfilePicture();
    }

    public Object updatePassword(PasswordChangeDTO passwordChangeDTO, HttpServletRequest request) {
        User user = getUserFromRequest(request);
        if (!bCryptPasswordEncoder.matches(passwordChangeDTO.getOldPassword(), user.getPassword()))
            throw new RuntimeException("Old password incorrect");
        user.setPassword(bCryptPasswordEncoder.encode(passwordChangeDTO.getNewPassword()));
        userRepository.save(user);
        return "Successful password update";
    }
}
