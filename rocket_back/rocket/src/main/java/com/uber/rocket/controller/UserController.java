package com.uber.rocket.controller;

import com.uber.rocket.dto.*;
import com.uber.rocket.service.LoginService;
import com.uber.rocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(userRegistrationDTO));
        } catch (RuntimeException | IllegalAccessException exception) {
            System.out.println(exception.getMessage());
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }


    @PostMapping("/login")
    public String loginUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            return loginService.login(request, response);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @GetMapping("/logged")
    public ResponseEntity<?> getLoggedUser(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(userService.getLoggedUser(request));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }


    @PutMapping
    public ResponseEntity<?> updateUserData(@Valid @RequestBody UpdateUserDataDTO updateUserDataDTO, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(userService.updateUser(request, updateUserDataDTO));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getClients(@RequestParam int number, @RequestParam int size, @RequestParam String filter) {
        try {
            return ResponseEntity.ok(userService.getClientByFilter(size, number, filter));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getAllNonAdministratorUsers() {
        try {
            return ResponseEntity.ok(userService.getAllNonAdministratorUsers());
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody PasswordChangeDTO passwordChangeDTO, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(userService.updatePassword(passwordChangeDTO, request));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping("/image")
    public ResponseEntity<?> updateProfilePicture(@RequestParam("file") MultipartFile multipart, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(userService.updateProfilePicture(request, multipart));
        } catch (RuntimeException | IOException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/block/{email}")
    public ResponseEntity<?> blockUser(@PathVariable String email, @RequestBody String reason) {
        try {
            return ResponseEntity.ok(userService.blockUser(email, reason));
        } catch (RuntimeException | IOException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/image/{email}")
    public ResponseEntity<?> getProfilePicture(@PathVariable String email) {
        try {
            return ResponseEntity.ok(userService.getProfilePicture(email));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }


    @GetMapping(path = "/confirm/{token}")
    public ResponseEntity<?> validateRegistration(@PathVariable("token") String token) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.validateRegistrationToken(token));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping(path = "/confirm")
    public ResponseEntity<?> validateChangingPassword(@Valid @RequestBody ForgetPasswordDTO forgetPasswordDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.validatePasswordToken(forgetPasswordDTO));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping(path = "/password")
    public ResponseEntity<?> forgottenPassword(@RequestBody String email) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.confirmForgottenPassword(email));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping(path = "/random/admin")
    public ResponseEntity<?> getRandomAdmin() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getRandomAdmin());
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping(path = "/google")
    public ResponseEntity<?> registerGoogleUser(@RequestBody GoogleUser googleUser, HttpServletRequest request, HttpServletResponse response) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.googleRegister(googleUser, request, response));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

}