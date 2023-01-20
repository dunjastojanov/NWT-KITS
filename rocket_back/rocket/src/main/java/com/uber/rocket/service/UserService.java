package com.uber.rocket.service;

import com.uber.rocket.dto.*;
import com.uber.rocket.entity.tokens.ConformationTokenType;
import com.uber.rocket.entity.user.*;
import com.uber.rocket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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

    public UserDTO getRidingPal(String email) {
        User user = this.getUserByEmail(email);
        return this.createRidingPal(user);
    }
    private UserDTO createRidingPal(User user) {
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getProfilePicture(), user.getRoles().iterator().next().getRole());
    }
    public String register(UserRegistrationDTO userRegistrationDTO) throws IllegalAccessException {
        userRegistrationDTO.validateClassAttributes(userRegistrationDTO);
        Optional<User> user = userRepository.findByEmail(userRegistrationDTO.getEmail());
        if (user.isPresent())
            throw new RuntimeException("User with this email already exists");
        User client = createUser(userRegistrationDTO);
        client.getRoles().add(roleService.getRoleByUserRole(RoleType.CLIENT));
        userRepository.save(client);
        String token = UUID.randomUUID().toString();
        confirmationTokenService.createToken(client, token, ConformationTokenType.REGISTRATION_CONFORMATION_TOKEN);
        try {
            emailService.sendEmailWithTokenByEmailSubject(client, token, EmailSubject.REGISTRATION_EMAIL);
        } catch (IOException e) {
            throw new RuntimeException("There was some error in sending email");
        }
        return "Successful registration. We have sent an email for registration verification";
    }

    private User createUser(UserRegistrationDTO userRegistrationDTO) {
        User user = new User(userRegistrationDTO);
        user.setRoles(new ArrayList<>());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return user;
    }

    public ResponseObjectDTO validateRegistrationToken(String token) {
        User user = confirmationTokenService.validateToken(token);
        user.setBlocked(false);
        userRepository.save(user);
        return new ResponseObjectDTO(null, "Successful registration verification");
    }

    public UserDataDTO getLoggedUser(HttpServletRequest request) {
        return new UserDataDTO(getUserFromRequest(request));
    }

    public ResponseObjectDTO updateUser(HttpServletRequest request, UpdateUserDataDTO updateUserDataDTO) {
        updateUserDataDTO.validateClassAttributes(updateUserDataDTO);
        User user = getUserFromRequest(request);
        updateUserData(user, updateUserDataDTO);
        return new ResponseObjectDTO(new UserDataDTO(user), "Successful update of user");
    }

    public void updateUserData(User user, UpdateUserDataDTO updateUserDataDTO) {
        user.setCity(updateUserDataDTO.getCity());
        user.setFirstName(updateUserDataDTO.getFirstName());
        user.setLastName(updateUserDataDTO.getLastName());
        user.setPhoneNumber(updateUserDataDTO.getPhoneNumber());
        userRepository.save(user);
    }

    public User getUserFromRequest(HttpServletRequest request) {
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

    public Object validatePasswordToken(ForgetPasswordDTO forgetPasswordDTO) {
        User user = confirmationTokenService.validateToken(forgetPasswordDTO.getToken());
        user.setPassword(bCryptPasswordEncoder.encode(forgetPasswordDTO.getPassword()));
        userRepository.save(user);
        return "Successful password update";
    }

    public Object confirmForgottenPassword(String email) {
        User user = getUserByEmail(email);
        String token = String.valueOf(UUID.randomUUID());
        confirmationTokenService.createToken(user, token, ConformationTokenType.PASSWORD_CONFORMATION_TOKEN);
        try {
            emailService.sendEmailWithTokenByEmailSubject(user, token, EmailSubject.FORGOTTEN_PASSWORD);
        } catch (IOException e) {
            throw new RuntimeException("There was some error in sending email");
        }
        return "We have sent an email for changing password";
    }

    public Object blockUser(String email) throws IOException {
        User user = getUserByEmail(email);
        if (user.isBlocked())
            throw new RuntimeException("User is already blocked");
        user.setBlocked(true);
        userRepository.save(user);
        emailService.sendEmailByEmailSubject(user, EmailSubject.BLOCKED_NOTIFICATION);
        return "User is successfully blocked";
    }

    public User registerDriver(DriverRegistrationDTO driverRegistrationDTO) throws IOException {
        Optional<User> user = userRepository.findByEmail(driverRegistrationDTO.getEmail());
        if (user.isPresent())
            throw new RuntimeException("User with this email already exists");
        String password = "Driver" + driverRegistrationDTO.getFirstName() + "123";
        User driver = createUser(new UserRegistrationDTO(driverRegistrationDTO, password));
        driver.getRoles().add(roleService.getRoleByUserRole(RoleType.DRIVER));
        driver.setBlocked(false);
        emailService.sendEmailWithTokenByEmailSubject(driver, password, EmailSubject.DRIVER_REGISTRATION_NOTIFICATION);
        userRepository.save(driver);
        return driver;
    }

    public Object getDriversByFilter(int size, int number, String filter) {
        String role = RoleType.DRIVER.name();
        return userRepository.searchAllFirstNameStartingWithOrLastNameStartingWith(role, filter, PageRequest.of(number, size));
    }

    public Object getClientByFilter(int size, int number, String filter) {
        String role = RoleType.CLIENT.name();
        return userRepository.searchAllFirstNameStartingWithOrLastNameStartingWith(role, filter, PageRequest.of(number, size));

    }

    public User getById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            throw new RuntimeException("There is no user with this id");
        return user.get();
    }


    public void updateDriverPicture(UpdateDriverPictureRequest driverPictureRequest) {
        User user = getById(driverPictureRequest.getDriverId());
        user.setProfilePicture(driverPictureRequest.getProfilePicture());
        userRepository.save(user);
    }

    public void addTokens(Payment data) {
        User user = getById(data.getUserId());
        user.setTokens(user.getTokens() + data.getAmount());
        userRepository.save(user);
    }

}
