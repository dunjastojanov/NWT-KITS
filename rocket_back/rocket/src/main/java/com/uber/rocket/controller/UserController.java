package com.uber.rocket.controller;

import com.uber.rocket.dto.UserDTO;
import com.uber.rocket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            userService.register(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException | IllegalAccessException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/logged")
    public ResponseEntity<?> getLoggedUser(HttpServletRequest request){
        try {
            return ResponseEntity.ok(userService.getLoggedUser(request));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }

    }


    @GetMapping(path = "/confirm/{token}")
    public ResponseEntity<?> confirm(@PathVariable("token") String token) {
        try {
            userService.validateRegistrationToken(token);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
