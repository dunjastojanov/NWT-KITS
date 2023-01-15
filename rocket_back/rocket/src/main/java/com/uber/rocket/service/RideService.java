package com.uber.rocket.service;

import com.uber.rocket.dto.*;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.mapper.RideHistoryMapper;
import com.uber.rocket.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RideService {

    @Autowired
    RideHistoryMapper rideHistoryMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private RideRepository repository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public List<RideHistoryDTO> getRideHistoryForUser(HttpServletRequest request) {
        User user = userService.getUserFromRequest(request);
        return getRideHistory(repository.findByPassenger(user));
    }

    public List<RideHistoryDTO> getAllRideHistory() {
        return getRideHistory(repository.findAll());
    }

    private List<RideHistoryDTO> getRideHistory(List<Ride> rides) {
        return rides.stream().map(ride -> rideHistoryMapper.mapToDto(ride)).toList();
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

        Dataset dataset = new Dataset();
        for (String key : groupedByDate.keySet()) {
            report.addLabel(key);
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
        report.setTotal(rides.size());
        report.setAverage(report.getTotal()/groupedByDate.size());

        return report;
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




}
