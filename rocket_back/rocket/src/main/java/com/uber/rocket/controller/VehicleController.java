package com.uber.rocket.controller;

import com.uber.rocket.dto.DriverRegistrationDTO;
import com.uber.rocket.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<?> registerDriver(@Valid @RequestBody DriverRegistrationDTO driverRegistrationDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.registerDriver(driverRegistrationDTO));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getDrivers(@RequestParam int number, @RequestParam int size, @RequestParam String filter) {
        try {
            return ResponseEntity.ok(vehicleService.getDriversByFilter(size, number, filter));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

}