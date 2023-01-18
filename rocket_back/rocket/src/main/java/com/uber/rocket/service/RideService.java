package com.uber.rocket.service;

import com.uber.rocket.dto.*;
import com.uber.rocket.entity.ride.FavouriteRoute;
import com.uber.rocket.entity.ride.Review;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.mapper.RideDetailsMapper;
import com.uber.rocket.mapper.RideHistoryMapper;
import com.uber.rocket.repository.FavouriteRouteRepository;
import com.uber.rocket.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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
    private FavouriteRouteRepository favouriteRouteRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public List<FavouriteRouteDTO> getFavouriteRoutesForUser(HttpServletRequest request) {
        List<FavouriteRoute> favouriteRoutes = favouriteRouteRepository.findAllByUser(userService.getUserByEmail(userService.getLoggedUser(request).getEmail()));
        return  favouriteRoutes.stream().map(FavouriteRouteDTO::new).collect(Collectors.toList());
    }

    public FavouriteRouteDTO addFavouriteRoute(HttpServletRequest request, Long rideId) {
        User user = userService.getUserFromRequest(request);
        FavouriteRoute favouriteRoute = new FavouriteRoute();
        favouriteRoute.setRide(getRide(rideId));
        favouriteRoute.setUser(userService.getUserByEmail(user.getEmail()));
        favouriteRoute = favouriteRouteRepository.save(favouriteRoute);
        return new FavouriteRouteDTO(favouriteRoute);
    }

    public void deleteFavouriteRoute(Long favouriteRouteId) {
        favouriteRouteRepository.deleteById(favouriteRouteId);
    }

    public Ride getRide(Long id) {
        Optional<Ride> ride = repository.findById(id);
        if (ride.isPresent()) {
            return ride.get();
        }
        else {
            throw new RuntimeException("Ride with given id does not exist.");

        }    }

    public Page<RideHistoryDTO> getRideHistoryForUser(HttpServletRequest request, int page, int size) {
        User user = userService.getUserFromRequest(request);
        return getRideHistory(repository.findByPassenger(user).stream().distinct().collect(Collectors.toList()), page, size);
    }

    public Page<RideHistoryDTO> getAllRideHistory(int page, int size) {
        return getRideHistory(repository.findAll(), page, size);
    }

    private Page<RideHistoryDTO> getRideHistory(List<Ride> rides, int page, int size) {
        int start = (page - 1) * size;
        int end = Math.min(page * size, rides.size());
        return new PageImpl<>(rides.stream().map(ride -> rideHistoryMapper.mapToDto(ride)).toList().subList(start, end));
    }

    public Report getReportForUser(HttpServletRequest request, String type, DatePeriod datePeriod) {
        User user = userService.getUserFromRequest(request);
        LocalDateTime start = LocalDateTime.parse(datePeriod.getStartDate(), formatter);
        LocalDateTime end = LocalDateTime.parse(datePeriod.getEndDate(), formatter);
        List<Ride> rides = repository.findByPassengerAndDatePeriod(
                user,
                start,
                end);
        return getReport(type, rides);
    }

    public Report getReportForAll(String type, DatePeriod datePeriod) {
        List<Ride> rides = repository.findByDatePeriod(
                LocalDate.parse(datePeriod.getStartDate(), formatter),
                LocalDate.parse(datePeriod.getEndDate(), formatter));
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
        }
        else throw new RuntimeException("Ride doesn't exist");
    }

    public Review addReview(HttpServletRequest request, NewReviewDTO dto) {
        return reviewService.addReview(request, dto, getRide(dto.getRideId()));
    }
}
