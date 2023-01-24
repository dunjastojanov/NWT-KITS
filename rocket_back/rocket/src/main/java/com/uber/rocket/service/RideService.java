package com.uber.rocket.service;

import com.uber.rocket.dto.*;
import com.uber.rocket.entity.ride.*;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.mapper.FavouriteRouteMapper;
import com.uber.rocket.mapper.RideDetailsMapper;
import com.uber.rocket.mapper.RideHistoryMapper;
import com.uber.rocket.mapper.RideMapper;
import com.uber.rocket.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private DestinationRepository destinationRepository;
    @Autowired
    private RideMapper rideMapper;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    public String createRide(RideDTO rideDTO) {
        Ride ride = rideMapper.mapToEntity(rideDTO);
        List<Destination> destinations = rideMapper.mapToDestination(rideDTO.getDestinations());
        ride = this.repository.save(ride);
        for (Destination destination : destinations) {
            destination.setRide(ride);
            this.destinationRepository.save(destination);
        }
        return "Ride successfully created.";
    }

    public UserDTO getRidingPal(String email) {
        User user = this.userService.getUserByEmail(email);
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

    public Object getRideHistoryForUser(HttpServletRequest request, int page, int size, String email) {
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
        notificationService.addRideCanceledNotifications(rideCancellation);
        rideCancellationRepository.save(rideCancellation);
    }
}
