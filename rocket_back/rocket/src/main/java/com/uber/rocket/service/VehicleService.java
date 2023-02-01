package com.uber.rocket.service;

import com.uber.rocket.dto.*;
import com.uber.rocket.entity.user.*;
import com.uber.rocket.mapper.UpdateUserDataMapper;
import com.uber.rocket.repository.DriverReportRepository;
import com.uber.rocket.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UpdateDriverDataRequestService updateDriverDataRequestService;

    @Autowired
    private UpdateDriverPictureRequestService updateDriverPictureRequestService;

    @Autowired
    private LogInfoService logInfoService;
    @Autowired
    private UpdateUserDataMapper updateUserDataMapper;

    @Autowired
    private DriverReportRepository driverReportRepository;

    private final static double GARAGE_LONGITUDE = 19.8335;
    private final static double GARAGE_LATITUDE = 45.2671;


    public Object registerDriver(DriverRegistrationDTO driverRegistrationDTO) throws IOException {
        User driver = userService.registerDriver(driverRegistrationDTO);
        createVehicle(driverRegistrationDTO, driver);
        return "Successful driver registration";
    }

    public Vehicle getVehicleByDriver(User driver) {
        return vehicleRepository.findFirstByDriver(driver);
    }

    private void createVehicle(DriverRegistrationDTO driverRegistrationDTO, User driver) {
        Vehicle vehicle = new Vehicle();
        vehicle.setDriver(driver);
        vehicle.setStatus(VehicleStatus.INACTIVE);
        vehicle.setVehicleType(VehicleType.valueOf(driverRegistrationDTO.getVehicleType().toUpperCase()));
        vehicle.setKidFriendly(driverRegistrationDTO.isKidFriendly());
        vehicle.setPetFriendly(driverRegistrationDTO.isPetFriendly());
        vehicle.setLongitude(GARAGE_LONGITUDE);
        vehicle.setLatitude(GARAGE_LATITUDE);
        vehicleRepository.save(vehicle);
    }

    public Object getDriversByFilter(int size, int number, String filter) {
        return userService.getDriversByFilter(size, number, filter);
    }

    public Object updateDriverData(HttpServletRequest request, @Valid UpdateDriverDto updateUserDataDTO) {
        User user = userService.getUserFromRequest(request);
        updateDriverDataRequestService.createDriverDataRequest(updateUserDataDTO, user.getId());
        return "Successfully requested the update of drivers information";
    }

    public Object requestDriverPictureUpdate(HttpServletRequest request, MultipartFile multipart) throws IOException {
        User user = userService.getUserFromRequest(request);
        String newProfilePath = imageService.saveProfilePicture(multipart, String.valueOf(user.getId()));
        updateDriverPictureRequestService.createDriverPictureRequest(newProfilePath, user.getId());
        return "Successfully requested the update of driver profile picture";
    }

    public Object validateVehicleDataUpdate(EvaluationDTO evaluationDTO) {
        UpdateDriverDataRequest driverDataRequest = updateDriverDataRequestService.getById(evaluationDTO.getId());
        if (driverDataRequest.isValidated())
            throw new RuntimeException("This request has already been validated");
        driverDataRequest.setValidated(true);
        if (evaluationDTO.isConfirmed()) {
            User user = userService.getById(driverDataRequest.getDriverId());
            userService.updateUserData(user, updateUserDataMapper.mapToDto(driverDataRequest));
            Vehicle vehicle = vehicleRepository.findFirstByDriver(user);
            vehicle.setVehicleType(driverDataRequest.getType());
            vehicle.setKidFriendly(driverDataRequest.isKidFriendly());
            vehicle.setPetFriendly(driverDataRequest.isPetFriendly());
            vehicleRepository.save(vehicle);
            notificationService.deleteNotification(driverDataRequest.getId());
            return "Request is successfully confirmed";
        } else {
            return "Request is successfully denied";
        }
    }

    public Object validateVehiclePictureUpdate(EvaluationDTO evaluationDTO) {
        UpdateDriverPictureRequest driverPictureRequest = updateDriverPictureRequestService.getById(evaluationDTO.getId());
        if (driverPictureRequest.isValidated())
            throw new RuntimeException("This request has already been validated");
        driverPictureRequest.setValidated(true);
        if (evaluationDTO.isConfirmed()) {
            userService.updateDriverPicture(driverPictureRequest);
            return "Request is successfully confirmed";
        } else {
            imageService.deletePicture(driverPictureRequest.getProfilePicture());
            return "Request is successfully denied";
        }
    }

    public Object updateDriverStatus(HttpServletRequest request, String status) {
        User user = userService.getUserFromRequest(request);
        Vehicle vehicle = vehicleRepository.findFirstByDriver(user);
        try {
            if (VehicleStatus.valueOf(status.toUpperCase()).equals(VehicleStatus.ACTIVE)) {
                if (logInfoService.hasDriverExceededWorkingHours(vehicle.getDriver().getId())) {
                    vehicle.setStatus(VehicleStatus.INACTIVE);
                    messagingTemplate.convertAndSendToUser(vehicle.getDriver().getEmail(), "/user/queue/driver/status", vehicle.getStatus());
                }
                logInfoService.startCountingHours(vehicle.getDriver().getId());
            }
            if (VehicleStatus.valueOf(status.toUpperCase()).equals(VehicleStatus.INACTIVE)) {
                logInfoService.endWorkingHourCount(vehicle.getDriver().getId());
            }
            vehicle.setStatus(VehicleStatus.valueOf(status.toUpperCase()));
            return vehicleRepository.save(vehicle).getStatus().name();

        } catch (IllegalArgumentException exception) {
            throw new RuntimeException("Status name does not exist");
        }
    }

    public List<Vehicle> findAvailableDrivers(VehicleType type, boolean kidFriendly, boolean petFriendly) {
        return this.getActiveVehicleByRequirements(type, kidFriendly, petFriendly);
    }

    public List<Vehicle> getActiveVehicleByRequirements(VehicleType type, boolean kidFriendly, boolean petFriendly) {
        List<Vehicle> vehicles = this.vehicleRepository.findByStatusAndTypeAndKidAndPetFriendly(VehicleStatus.ACTIVE, type, kidFriendly, petFriendly);
        return vehicles;
    }

    public List<Vehicle> getActiveVehicles() {
        return this.vehicleRepository.findByActiveStatus();
    }

    public Object getVehicleByDriver(HttpServletRequest request) {
        User user = userService.getUserFromRequest(request);
        return new VehicleDTO(getVehicleByDriver(user));
    }

    public User getDriverByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    public List<VehicleSimulationDTO> getAllVehicles() {
        List<Vehicle> vehicles = this.vehicleRepository.findAll();
        List<VehicleSimulationDTO> vehicleDTOS = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            VehicleSimulationDTO dto = new VehicleSimulationDTO();
            dto.setLatitude(vehicle.getLatitude());
            dto.setLongitude(vehicle.getLongitude());
            dto.setId(vehicle.getId());
            vehicleDTOS.add(dto);
        }
        return vehicleDTOS;
    }

    public Optional<Vehicle> getVehicleById(Long id) {
        return this.vehicleRepository.findById(id);
    }

    public void save(Vehicle vehicle) {
        this.vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllActiveVehicles(VehicleStatus vehicleStatus) {
        return vehicleRepository.findAllByStatusIs(vehicleStatus);
    }

    public void reportDriver(HttpServletRequest request, Long driverId) {
        DriverReport driverReport = new DriverReport();
        driverReport.setPassenger(userService.getUserFromRequest(request));
        driverReport.setDriver(userService.getById(driverId));
        driverReportRepository.save(driverReport);
    }
}
