package com.uber.rocket.service;

import com.uber.rocket.dto.*;
import com.uber.rocket.entity.ride.*;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.entity.user.VehicleType;
import com.uber.rocket.mapper.FavouriteRouteMapper;
import com.uber.rocket.mapper.RideDetailsMapper;
import com.uber.rocket.mapper.RideHistoryMapper;
import com.uber.rocket.mapper.RideMapper;
import com.uber.rocket.repository.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RideService {

    @Autowired
    RideHistoryMapper rideHistoryMapper;
    @Autowired
    RideDetailsMapper rideDetailsMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private RideRepository repository;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private RideCancellationRepository rideCancellationRepository;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private FavouriteRouteRepository favouriteRouteRepository;
    @Autowired
    private FavouriteRouteMapper favouriteRouteMapper;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private DestinationService destinationService;

    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private RideMapper rideMapper;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class VehicleLastDestination {
        Destination endDestinationCurrentRide;
        Vehicle vehicle;
    }
    @Transactional(Transactional.TxType.REQUIRED)
    public void changeRidePalDriverStatus(String rideId, ChangeStatusDTO changeStatusDTO) {
        Optional<Ride> rideOpt = this.repository.findById(Long.parseLong(rideId));
        User user = this.userService.getById(Long.parseLong(changeStatusDTO.getUserId()));
        boolean driverAccepted = false;
        if (rideOpt.isPresent()) {
            Ride ride = rideOpt.get();
            if (user.getRoles().stream().toList().get(0).getRole().equalsIgnoreCase("DRIVER")) {
                ride = setDriverStatus(ride, changeStatusDTO, user);
                driverAccepted = true;
            }
            if (user.getRoles().stream().toList().get(0).getRole().equalsIgnoreCase("CLIENT")) {
                ride = setClientStatus(ride, changeStatusDTO);
            }
            System.out.println(ride);
            if (!driverAccepted && this.allAcceptedRide(ride)) {
                boolean foundDriver = this.findAndNotifyDriver(ride);
                if (!foundDriver) {
                    ride.setStatus(RideStatus.DENIED);
                }
                System.out.println(ride);
            }
            System.out.println(ride);
            ride = this.repository.save(ride);
            this.updateStatusOverSocket(ride);
            System.out.println(ride);
        } else {
            throw new RuntimeException("Ride not found");
        }
    }

    private boolean allAcceptedRide(Ride ride) {
        if (ride.getStatus() == RideStatus.REQUESTED) {
            for (Passenger passenger : ride.getPassengers()) {
                if (passenger.getUserRidingStatus() != UserRidingStatus.ACCEPTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @Transactional(Transactional.TxType.REQUIRED)
    public void updateStatusOverSocket(Ride ride) {
        Vehicle vehicle = ride.getVehicle();
        try{
            if (vehicle != null) {
                messagingTemplate.convertAndSendToUser(vehicle.getDriver().getEmail(), "/queue/rides", ride.getId());
            }
            for (Passenger passenger : ride.getPassengers()) {
                messagingTemplate.convertAndSendToUser(passenger.getUser().getEmail(), "/queue/rides", ride.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Ride setClientStatus(Ride ride,  ChangeStatusDTO changeStatusDTO) {
        for (Passenger passenger : ride.getPassengers()) {
            if (Objects.equals(passenger.getUser().getId(), Long.parseLong(changeStatusDTO.getUserId()))) {
                if (changeStatusDTO.getRidingStatus() == UserRidingStatus.DENIED) {
                    ride.setStatus(RideStatus.DENIED);
                    passenger.setUserRidingStatus(UserRidingStatus.DENIED);
                } else if (changeStatusDTO.getRidingStatus() == UserRidingStatus.ACCEPTED) {
                    passenger.setUserRidingStatus(UserRidingStatus.ACCEPTED);
                }
                this.passengerRepository.save(passenger);
            }
        }
        return ride;
    }
    private Ride setDriverStatus(Ride ride,  ChangeStatusDTO changeStatusDTO, User driver) {
        if (changeStatusDTO.getRidingStatus() == UserRidingStatus.DENIED) {
            ride.setStatus(RideStatus.DENIED);
        } else if (changeStatusDTO.getRidingStatus() == UserRidingStatus.ACCEPTED) {
            if (ride.isNow()) {
                ride.setStatus(RideStatus.CONFIRMED);
            } else ride.setStatus(RideStatus.SCHEDULED);
            Vehicle vehicle = this.vehicleService.getVehicleByDriver(driver);
            ride.setVehicle(vehicle);
        }
        return ride;
    }
    public Long createRide(RideDTO rideDTO) {
        Ride ride = rideMapper.mapToEntity(rideDTO);
        /*booelan passengersHaveFund = this.userService.checkPassengersTokens(ride);
        if (passengersHaveFund) {
            passengerRepository.saveAll(ride.getPassengers());
        } else {
            return -1;
        }
        */
        List<Destination> destinations = rideMapper.mapToDestination(rideDTO.getDestinations());
        ride = this.repository.save(ride);
        for (Destination destination : destinations) {
            destination.setRide(ride);
            this.destinationService.save(destination);
        }
        for (int i = 1; i < ride.getPassengers().size(); i++) {
            this.notificationService.addPassengerRequestNotification(ride.getPassengers().stream().toList().get(i).getUser(), ride);
        }
        if (this.allAcceptedRide(ride)) {
            this.findAndNotifyDriver(ride);
        }
        return ride.getId();
    }

    public UserDTO getRidingPal(String email) {
        User user = this.userService.getUserByEmail(email);
        if (!user.getRoles().stream().toList().get(0).getRole().equalsIgnoreCase("CLIENT")) throw new RuntimeException("User not found");
        RideDTO ride = getUserCurrentRide(user);
        if (ride != null) { return null; }
        return this.createRidingPal(user);
    }
    private UserDTO createRidingPal(User user) {
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getProfilePicture(), user.getRoles().iterator().next().getRole());
    }

    public RideDTO getUserCurrentRideByEmail(String email) {
        User user = this.userService.getUserByEmail(email);
        return this.getUserCurrentRide(user);
    }

    public RideDTO getUserCurrentRideById(Long id) {
        Optional<Ride> ride = this.repository.findById(id);
        if (ride.isPresent())
            return this.rideMapper.mapToDto(ride.get());
        return null;
    }

    public boolean findAndNotifyDriver(Ride ride) {
        Vehicle vehicle = this.lookForDriver(ride);
        if (vehicle != null) {
            this.notificationService.addDriverRideRequestNotification(vehicle.getDriver(), ride);
            List<NotificationDTO> notifications = this.notificationService.getNotificationsForUser(vehicle.getDriver());
            messagingTemplate.convertAndSendToUser(vehicle.getDriver().getEmail(), "/queue/notifications", notifications);
            return true;
        }
        return false;
    }
    public Vehicle lookForDriver(Ride ride) {
        Destination startDestination = this.destinationService.getStartDestinationByRide(ride);
        Vehicle closest = null;
        List<Vehicle> vehicles = this.vehicleService.findAvailableDrivers(ride.getVehicleTypeRequested(), ride.isKidFriendly(), ride.isPetFriendly());
        List<Vehicle> first = this.noCurrentNorFutureRide(vehicles);
        if (first.size() > 0) {
            closest = closestDriverWithoutCurrentRide(startDestination, first);
        } else {
            List<Vehicle> second = this.notCurrentButFuture(vehicles);
            if (second.size() > 0) {
                closest = closestDriverWithoutCurrentRide(startDestination, second);
            } else {
                List<VehicleLastDestination> third = this.haveCurrentButNoFuture(vehicles);
                if (third.size() > 0) {
                    closest = closestDriverWithCurrentRide(startDestination, third);
                }
            }
        }
        return closest;
    }
    public Vehicle closestDriverWithoutCurrentRide(Destination start, List<Vehicle> vehicles) {
        double startLongitude = start.getLongitude();
        double startLatitude = start.getLatitude();
        Vehicle closest = null;
        double min_distance = Double.POSITIVE_INFINITY;
        for (Vehicle vehicle : vehicles) {
            double distance = Math.sqrt(Math.pow(startLatitude - vehicle.getLatitude(), 2) + Math.pow(startLongitude - vehicle.getLongitude(), 2));
            if (distance < min_distance) {
                min_distance = distance;
                closest = vehicle;
            }
        }
        return closest;
    }

    public Vehicle closestDriverWithCurrentRide(Destination start, List<VehicleLastDestination> vehiclesDest) {
        double startLongitude = start.getLongitude();
        double startLatitude = start.getLatitude();
        Vehicle closest = null;
        double min_distance = Double.POSITIVE_INFINITY;
        for (VehicleLastDestination vehicleDest : vehiclesDest) {
            double distance = Math.sqrt(Math.pow(startLatitude - vehicleDest.getEndDestinationCurrentRide().getLatitude(), 2) + Math.pow(startLongitude - vehicleDest.getEndDestinationCurrentRide().getLongitude(), 2));
            if (distance < min_distance) {
                min_distance = distance;
                closest = vehicleDest.getVehicle();
            }
        }
        return closest;
    }

    private List<Vehicle> notCurrentButFuture(List<Vehicle> vehicles) {
        List<Vehicle> availableVehicles = new ArrayList<Vehicle>();
        for (Vehicle vehicle : vehicles) {
            if (currentlyAvailable(vehicle))
                availableVehicles.add(vehicle);
        }
        return availableVehicles;
    }

    private List<VehicleLastDestination> haveCurrentButNoFuture(List<Vehicle> vehicles) {
        List<VehicleLastDestination> availableVehicles = new ArrayList<VehicleLastDestination>();
        for (Vehicle vehicle : vehicles) {
            Destination lastDest = availableForFuture(vehicle);
            if (lastDest != null)  {
                availableVehicles.add(new VehicleLastDestination(lastDest, vehicle));
            }
        }
        return availableVehicles;
    }
    private List<Vehicle> noCurrentNorFutureRide(List<Vehicle> vehicles) {
        List<Vehicle> availableVehicles = new ArrayList<Vehicle>();
        for (Vehicle vehicle : vehicles) {
            if (fullyAvailable(vehicle))
                availableVehicles.add(vehicle);
        }
        return availableVehicles;
    }
    private boolean fullyAvailable(Vehicle vehicle) {
        List<Ride> rides = this.repository.findByDriverAndStatus(vehicle.getDriver(), RideStatus.DENIED, RideStatus.ENDED);
        return rides.size() == 0;
    }

    private boolean currentlyAvailable(Vehicle vehicle) {
        List<Ride> rides = this.repository.findByDriverAndStatus(vehicle.getDriver(), RideStatus.DENIED, RideStatus.ENDED);
        return rides.size() == 1 && rides.get(0).getStatus() == RideStatus.SCHEDULED;
    }
    private Destination availableForFuture(Vehicle vehicle) {
        List<Ride> rides = this.repository.findByDriverAndStatus(vehicle.getDriver(), RideStatus.DENIED, RideStatus.ENDED);
        if (rides.size() == 1 && rides.get(0).getStatus() != RideStatus.SCHEDULED) {
            return this.destinationService.getEndDestinationByRide(rides.get(0));
        }
        return null;
    }

    @Transactional
    public RideDTO getUserCurrentRide(User user) {
        List<Ride> rides = repository.findByPassengers(user.getId());
        if (rides.size() > 0)
            return this.rideMapper.mapToDto(rides.get(0));
        return null;
    }
    public List<FavouriteRouteDTO> getFavouriteRoutesForUser(HttpServletRequest request) {
        List<FavouriteRoute> favouriteRoutes = favouriteRouteRepository.findAllByUser(userService.getUserByEmail(userService.getLoggedUser(request).getEmail()));
        return favouriteRoutes.stream().map(route -> favouriteRouteMapper.mapToDto(route)).collect(Collectors.toList());
    }

    public FavouriteRouteDTO addFavouriteRoute(HttpServletRequest request, Long rideId) {
        User user = userService.getUserFromRequest(request);
        FavouriteRoute favouriteRoute = new FavouriteRoute();
        favouriteRoute.setRide(getRide(rideId));
        favouriteRoute.setUser(userService.getUserByEmail(user.getEmail()));
        favouriteRoute = favouriteRouteRepository.save(favouriteRoute);
        return favouriteRouteMapper.mapToDto(favouriteRoute);
    }

    public void deleteFavouriteRoute(Long favouriteRouteId) {
        favouriteRouteRepository.deleteById(favouriteRouteId);
    }

    public Ride getRide(Long id) {
        Optional<Ride> ride = repository.findById(id);
        if (ride.isPresent()) {
            return ride.get();
        } else {
            throw new RuntimeException("Ride with given id does not exist.");
        }
    }

    public Page<RideHistory> getRideHistoryForUser(HttpServletRequest request, int page, int size) {
        User user = userService.getUserFromRequest(request);
        return getRideHistory(page, size, user);
    }

    public Page<RideHistory> getAllRideHistory(int page, int size) {
        return getRideHistory(repository.findAllByStatus(PageRequest.of(page, size, Sort.by("startTime")), RideStatus.ENDED));
    }

    private Page<RideHistory> getRideHistory(Page<Ride> rides) {
        return rides.map(ride -> rideHistoryMapper.mapToDto(ride));
    }

    public Report getReportForUser(HttpServletRequest request, String type, DatePeriod datePeriod) {
        User user = userService.getUserFromRequest(request);
        LocalDateTime start = LocalDateTime.parse(datePeriod.getStartDate(), formatter);
        LocalDateTime end = LocalDateTime.parse(datePeriod.getEndDate(), formatter);

        List<Ride> rides = new ArrayList<>();

        if (user.getRoles().stream().anyMatch(role -> role.getRole().equals("CLIENT"))) {
            rides = repository.findByPassengerAndDatePeriod(
                    user,
                    start,
                    end);
        } else if (user.getRoles().stream().anyMatch(role -> role.getRole().equals("DRIVER"))) {
            rides = repository.findByDriverAndDatePeriod(
                    user,
                    start,
                    end);
        }

        return getReport(type, rides);
    }

    public Report getReportForAll(String type, DatePeriod datePeriod) {
        LocalDateTime start = LocalDateTime.parse(datePeriod.getStartDate(), formatter);
        LocalDateTime end = LocalDateTime.parse(datePeriod.getEndDate(), formatter);
        List<Ride> rides = repository.findByDatePeriod(start, end);
        return getReport(type, rides);
    }

    private Report getReport(String type, List<Ride> rides) {
        Report report = new Report();
        Map<String, List<Ride>> groupedByDate =
                rides.stream().collect(Collectors.groupingBy(
                        ride -> getDateString(ride.getStartTime())
                ));

        addDataset(type, report, groupedByDate);
        addTotalAndAverage(type, rides, report);

        return report;
    }

    private static void addTotalAndAverage(String type, List<Ride> rides, Report report) {
        switch (type) {
            case "rides" -> {
                report.setTotal(getRidesData(rides));
                report.setAverage(getRidesData(rides) / rides.size());
            }
            case "kilometers" -> {
                report.setTotal(getKilometerData(rides));
                report.setAverage(getKilometerData(rides) / rides.size());
            }
            case "money" -> {
                report.setTotal(getMoneyData(rides));
                report.setAverage(getMoneyData(rides) / rides.size());
            }
        }
    }

    private static void addDataset(String type, Report report, Map<String, List<Ride>> groupedByDate) {
        Dataset dataset = new Dataset();
        for (String key : groupedByDate.keySet().stream().toList().stream().sorted().toList()) {
            report.addLabel(key.toUpperCase());
            dataset.setLabel(type);
            dataset.setBackgroundColor("yellow");
            switch (type) {
                case "rides":
                    dataset.addData(getRidesData(groupedByDate.get(key)));
                case "kilometers":
                    dataset.addData(getKilometerData(groupedByDate.get(key)));
                case "money":
                    dataset.addData(getMoneyData(groupedByDate.get(key)));
            }
        }

        report.addDataset(dataset);
    }

    private static Double getMoneyData(List<Ride> rides) {
        return Double.valueOf(rides.stream().map(Ride::getPrice).reduce(0, Integer::sum));
    }

    private static Double getKilometerData(List<Ride> rides) {
        return rides.stream().map(Ride::getLength).reduce((double) 0, Double::sum);
    }

    private static Double getRidesData(List<Ride> rides) {
        return (double) rides.size();
    }

    private String getDateString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTime.format(formatter);
    }

    public RideDetails getRideDetails(Long id) {
        Optional<Ride> ride = repository.findById(id);
        if (ride.isPresent()) {
            return rideDetailsMapper.mapToDto(ride.get());
        } else throw new RuntimeException("Ride doesn't exist");
    }

    public Review addReview(HttpServletRequest request, NewReviewDTO dto) throws RuntimeException{
        return reviewService.addReview(request, dto, getRide(dto.getRideId()));
    }

    public Object getRideHistoryForUser(int page, int size, String email) {
        User user = userService.getUserByEmail(email);
        return getRideHistory(page, size, user);
    }

    private Page<RideHistory> getRideHistory(int page, int size, User user) {
        if (user.getRoles().stream().anyMatch(role -> role.getRole().equals("CLIENT"))) {
            return getRideHistory(repository.findByPassengers(PageRequest.of(page, size, Sort.by("startTime")), user));
        }
        if (user.getRoles().stream().anyMatch(role -> role.getRole().equals("DRIVER"))) {
            return getRideHistory(repository.findByVehicleDriver(PageRequest.of(page, size, Sort.by("startTime")), vehicleService.getVehicleByDriver(user)));
        }
        return null;
    }

    public void cancelRide(Long id, String reason) {
        Ride ride = getRide(id);
        RideCancellation rideCancellation = new RideCancellation();
        rideCancellation.setDriver(ride.getDriver());
        rideCancellation.setRide(ride);
        rideCancellation.setDescription(reason);
        ride.setStatus(RideStatus.DENIED);
        repository.save(ride);
        notificationService.addRideCanceledNotifications(rideCancellation);
        rideCancellationRepository.save(rideCancellation);
    }

    public RideSimulationDTO getRideForSimulation(Long vehicleId) {
        Optional<Vehicle> vehicleOpt = this.vehicleService.getVehicleById(vehicleId);
        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            Ride ride = this.repository.findRideByVehicleAndStatus(vehicle);
            RideSimulationDTO rsDTO = new RideSimulationDTO();
            rsDTO.setVehicleStatus(vehicle.getStatus());
            if (ride != null) {
                RideInfoSimulationDTO risDTO = new RideInfoSimulationDTO();
                risDTO.setStatus(ride.getStatus());
                risDTO.setRouteCoordinates(URLEncoder.encode(ride.getRouteLocation(), StandardCharsets.UTF_8));
                Destination dest = this.destinationService.getStartDestinationByRide(ride);
                List<Double> longLat = new ArrayList<>();
                longLat.add(dest.getLongitude());
                longLat.add(dest.getLatitude());
                risDTO.setDestination(longLat);
                rsDTO.setRide(risDTO);
            }
            return rsDTO;
        }
        return null;
    }

    public LocationDTO updateVehicleLocation(Long id, Double longitude, Double latitude) {
        Optional<Vehicle> vehicleOpt = this.vehicleService.getVehicleById(id);
        System.out.println(id);
        System.out.println(longitude);
        System.out.println(latitude);
        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            System.out.println(vehicle);
            vehicle.setLongitude(longitude);
            vehicle.setLatitude(latitude);
            this.vehicleService.save(vehicle);
            Ride ride = this.repository.findRideByVehicleAndStatus(vehicle);
            System.out.println(ride);
            if (ride != null) {
                LocationDTO locationDTO = new LocationDTO();
                locationDTO.setLatitude(vehicle.getLatitude());
                locationDTO.setLongitude(vehicle.getLongitude());
                this.updateLocationToPassengers(ride.getPassengers().stream().toList(),locationDTO);
            }
        }
        return null;
    }

    private void updateLocationToPassengers(List<Passenger> passengers, LocationDTO locationDTO) {
        for (Passenger passenger : passengers) {
            System.out.println(passenger.getUser().getEmail());
            System.out.println(locationDTO);
            messagingTemplate.convertAndSendToUser(passenger.getUser().getEmail(),"/queue/update-vehicle", locationDTO);
        }
    }

}
