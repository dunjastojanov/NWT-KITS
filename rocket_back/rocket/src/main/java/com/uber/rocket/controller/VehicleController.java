package com.uber.rocket.controller;

import com.uber.rocket.dto.*;
import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.entity.user.User;

import com.uber.rocket.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public ResponseEntity<?> registerDriver(@Valid @RequestBody DriverRegistrationDTO driverRegistrationDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.registerDriver(driverRegistrationDTO));
        } catch (RuntimeException | IOException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/vehicle")
    public ResponseEntity<?> getVehicleForLoggedDriver(HttpServletRequest request) {
        try {
            return ResponseEntity.ok().body(vehicleService.getVehicleByDriver(request));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateDriverData(@Valid @RequestBody UpdateDriverDto updateUserDataDTO, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(vehicleService.updateDriverData(request, updateUserDataDTO));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping("/image")
    public ResponseEntity<?> updateDriverProfile(@RequestParam("file") MultipartFile multipart, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(vehicleService.requestDriverPictureUpdate(request, multipart));
        } catch (RuntimeException | IOException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping("/{status}")
    public ResponseEntity<?> updateDriverStatus(HttpServletRequest request, @PathVariable String status) {
        try {
            return ResponseEntity.ok(vehicleService.updateDriverStatus(request, status));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/validate/image")
    public ResponseEntity<?> validateVehiclePictureUpdate(@RequestBody EvaluationDTO evaluationDTO) {
        try {
            return ResponseEntity.ok(vehicleService.validateVehiclePictureUpdate(evaluationDTO));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateVehicleDataUpdate(@Valid @RequestBody EvaluationDTO evaluationDTO) {
        try {
            return ResponseEntity.ok(vehicleService.validateVehicleDataUpdate(evaluationDTO));
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

    @GetMapping("/driver/status/{email}")
    public void getVehicleStatus(@PathVariable("email") String email) {
        Vehicle vehicle = vehicleService.getVehicleByDriver(vehicleService.getDriverByEmail(email));
        messagingTemplate.convertAndSendToUser(vehicle.getDriver().getEmail(), "/queue/driver/status", vehicle.getStatus());
    }


    @GetMapping(value = "get-all", produces = "application/json")
    public List<VehicleSimulationDTO> getAllVehicles() {
        System.out.println("aloo");
        List<VehicleSimulationDTO> dtos = this.vehicleService.getAllVehicles();
        dtos.forEach(System.out::println);
        return dtos;
    }

    @PostMapping("/report/driver/{driverId}")
    public ResponseEntity<?> reportDriver(@PathVariable Long driverId, HttpServletRequest request){
        try {
            vehicleService.reportDriver(request, driverId);
            return ResponseEntity.ok("Successfully reported the driver.");
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}