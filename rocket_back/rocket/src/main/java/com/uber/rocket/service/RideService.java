package com.uber.rocket.service;

import com.uber.rocket.dto.*;
import com.uber.rocket.entity.ride.*;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.mapper.FavouriteRouteMapper;
import com.uber.rocket.mapper.RideDetailsMapper;
import com.uber.rocket.mapper.RideHistoryMapper;
import com.uber.rocket.mapper.RideMapper;
import com.uber.rocket.repository.DestinationRepository;
import com.uber.rocket.repository.FavouriteRouteRepository;
import com.uber.rocket.repository.PassengerRepository;
import com.uber.rocket.repository.RideRepository;
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
    private DestinationRepository destinationRepository;

    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private RideMapper rideMapper;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Transactional(Transactional.TxType.REQUIRED)
    public void changeRidePalDriverStatus(String rideId, ChangeStatusDTO changeStatusDTO) {
        Optional<Ride> rideOpt = this.repository.findById(Long.parseLong(rideId));
        User user = this.userService.getById(Long.parseLong(changeStatusDTO.getUserId()));
        if (rideOpt.isPresent()) {
            Ride ride = rideOpt.get();
            if (user.getRoles().stream().toList().get(0).getRole().equalsIgnoreCase("DRIVER")) {
                ride = setDriverStatus(ride, changeStatusDTO);
            }
            if (user.getRoles().stream().toList().get(0).getRole().equalsIgnoreCase("CLIENT")) {
                ride = setClientStatus(ride, changeStatusDTO);
            }
            //setStatusIfAllConfirmed(ride);
            System.out.println(ride);
            ride = this.repository.save(ride);
            this.updateStatusOverSocket(ride);
            System.out.println(ride);
        } else {
            throw new RuntimeException("Ride not found");
        }
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
    /*private void setStatusIfAllConfirmed(Ride ride) {
        boolean allPassengersAccepted = true;
        if (ride.getStatus() == RideStatus.CONFIRMED || ride.getStatus() == RideStatus.SCHEDULED) {
            for (Passenger passenger : ride.getPassengers()) {
                if (passenger.getUserRidingStatus() != UserRidingStatus.ACCEPTED) {
                    allPassengersAccepted = false;
                }
            }
            if (allPassengersAccepted) ride.setStatus(RideStatus.CONFIRMED);
        }
    }*/
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
    private Ride setDriverStatus(Ride ride,  ChangeStatusDTO changeStatusDTO) {
        if (changeStatusDTO.getRidingStatus() == UserRidingStatus.DENIED) {
            ride.setStatus(RideStatus.DENIED);
        } else if (changeStatusDTO.getRidingStatus() == UserRidingStatus.ACCEPTED) {
            if (ride.isNow()) {
                System.out.println("Setting confirmed on " + ride.getId());
                ride.setStatus(RideStatus.CONFIRMED);
            } else ride.setStatus(RideStatus.SCHEDULED);
        }
        return ride;
    }
    public String createRide(RideDTO rideDTO) {
        Ride ride = rideMapper.mapToEntity(rideDTO);
        List<Destination> destinations = rideMapper.mapToDestination(rideDTO.getDestinations());
        ride = this.repository.save(ride);
        for (Destination destination : destinations) {
            destination.setRide(ride);
            this.destinationRepository.save(destination);
        }
        for (int i = 1; i < ride.getPassengers().size(); i++) {
            this.notificationService.addPassengerRequestNotification(ride.getPassengers().stream().toList().get(i).getUser(), ride);
        }
        return "Ride successfully created.";
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

    public Page<RideHistoryDTO> getRideHistoryForUser(HttpServletRequest request, int page, int size) {
        User user = userService.getUserFromRequest(request);
        if (user.getRoles().stream().anyMatch(role -> role.getRole().equals("CLIENT"))) {
            return getRideHistory(repository.findByPassengers(PageRequest.of(page, size, Sort.by("startTime")), user));
        }
        if (user.getRoles().stream().anyMatch(role -> role.getRole().equals("DRIVER"))) {
            return getRideHistory(repository.findByVehicleDriver(PageRequest.of(page, size, Sort.by("startTime")), vehicleService.getVehicleByDriver(user)));
        }
        return null;
    }

    public Page<RideHistoryDTO> getAllRideHistory(int page, int size) {
        return getRideHistory(repository.findAllByStatus(PageRequest.of(page, size, Sort.by("startTime")), RideStatus.ENDED));
    }

    private Page<RideHistoryDTO> getRideHistory(Page<Ride> rides) {
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

    public Review addReview(HttpServletRequest request, NewReviewDTO dto) {
        return reviewService.addReview(request, dto, getRide(dto.getRideId()));
    }
}
