package com.uber.rocket.service;

import com.uber.rocket.dto.RideHistoryDTO;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.mapper.RideHistoryMapper;
import com.uber.rocket.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class RideService {

    @Autowired
    RideHistoryMapper rideHistoryMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private RideRepository repository;

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
}
