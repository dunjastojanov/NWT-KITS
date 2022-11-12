package com.uber.rocket.controller;

import com.uber.rocket.dto.ForgetPasswordDTO;
import com.uber.rocket.dto.PasswordChangeDTO;
import com.uber.rocket.dto.UpdateUserDataDTO;
import com.uber.rocket.dto.UserDTO;
import com.uber.rocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(userDTO));
        } catch (RuntimeException | IllegalAccessException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
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
    public ResponseEntity<?> updateAttributes(@Valid @RequestBody UpdateUserDataDTO updateUserDataDTO, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(userService.updateUser(request, updateUserDataDTO));
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

    @DeleteMapping("/{email}")
    public ResponseEntity<?> blockUser(@PathVariable String email) {
        try {
            return ResponseEntity.ok(userService.blockUser(email));
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
    public ResponseEntity<?> forgottenPassword(@NotBlank @RequestParam("email") String email) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.confirmForgottenPassword(email));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }


}
