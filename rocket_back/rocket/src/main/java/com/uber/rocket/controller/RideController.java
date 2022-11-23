package com.uber.rocket.controller;

import com.uber.rocket.pagination.Pagination;
import com.uber.rocket.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/ride")
public class RideController {
    private final RideService rideService;
    private final Pagination pagination;

    @Autowired
    public RideController(RideService rideService, Pagination pagination) {
        this.rideService = rideService;
        this.pagination = pagination;
    }

    @GetMapping("/{size}/{page}")
    public ResponseEntity<?> getRideHistoryForUser(HttpServletRequest request, @PathVariable int page, @PathVariable int size) {
        try {
            return ResponseEntity.ok(pagination.paginate(rideService.getRideHistoryForUser(request), size, page));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/all/{size}/{page}")
    public ResponseEntity<?> getAllRideHistory(@PathVariable int page, @PathVariable int size) {
        try {
            return ResponseEntity.ok(pagination.paginate(rideService.getAllRideHistory(), size, page));
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
